package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.ToLongBiFunction;
import java.util.function.Consumer;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/** Shared recipe progress and transactions. Each placed machine owns its runner. Server thread only. */
public final class RecipeRunner {
    @FunctionalInterface public interface Finder { AddonRecipes.Line find(MachineBehaviorContext context); }
    private final Slots slots;
    private final Finder finder;
    private String energy;
    private BiPredicate<MachineBehaviorContext, AddonRecipes.Line> allowed = (context, line) -> true;
    private ToLongBiFunction<MachineBehaviorContext, AddonRecipes.Line> step = (context, line) -> 1L;
    private Consumer<MachineBehaviorContext> finished = context -> {};

    public RecipeRunner(Slots slots, Finder finder) { this.slots = slots; this.finder = finder; }
    public RecipeRunner(Slots slots, List<String> maps) {
        this(slots, context -> {
            var items = java.util.stream.Stream.concat(slots.itemsIn().stream(), slots.itemsOut().stream())
                    .map(context::item).toList();
            var fluids = slots.fluidsIn().stream().map(context::fluid).toList();
            for (String map : maps) {
                var line = AddonRecipes.map(map).find(items, fluids, true, slots.itemsIn().size(), slots.itemsOut().size());
                if (line != null) return line;
            }
            return null;
        });
    }
    public RecipeRunner energy(String type) { energy = type; return this; }
    public RecipeRunner requires(BiPredicate<MachineBehaviorContext, AddonRecipes.Line> test) { allowed = test; return this; }
    public RecipeRunner step(ToLongBiFunction<MachineBehaviorContext, AddonRecipes.Line> progress) { step = progress; return this; }
    public RecipeRunner after(Consumer<MachineBehaviorContext> effect) { finished = effect; return this; }

    private boolean searched;
    private List<ItemStack> previousItems = List.of();
    private List<FluidStack> previousFluids = List.of();
    private AddonRecipes.Line cached;

    public AddonRecipes.Line find(MachineBehaviorContext context) {
        // Output items participate because the SDK supports rows requiring empty output slots.
        var items = java.util.stream.Stream.concat(slots.itemsIn().stream(), slots.itemsOut().stream())
                .map(context::item).toList();
        var fluids = slots.fluidsIn().stream().map(context::fluid).toList();
        boolean same = searched && items.size() == previousItems.size() && fluids.size() == previousFluids.size();
        for (int i = 0; same && i < items.size(); i++) same = ItemStack.matches(items.get(i), previousItems.get(i));
        for (int i = 0; same && i < fluids.size(); i++) {
            FluidStack a = fluids.get(i), b = previousFluids.get(i);
            same = a.getAmount() == b.getAmount() && (a.isEmpty() && b.isEmpty() || FluidStack.isSameFluidSameComponents(a, b));
        }
        if (!same) {
            cached = finder.find(context);
            previousItems = items.stream().map(ItemStack::copy).toList();
            previousFluids = fluids.stream().map(FluidStack::copy).toList();
            searched = true;
        }
        return cached;
    }

    public void invalidate() { searched = false; }
    public Slots slots() { return slots; }

    public void tick(MachineBehaviorContext context) {
        context.working(false);
        AddonRecipes.Line line = find(context);
        if (line == null || !allowed.test(context, line)) { idle(context); return; }
        if (!canComplete(context, line)) {
            context.progress(Math.min(1.0F, context.state().getLong("progress") / (float) Math.max(1L, line.duration())));
            return;
        }
        long duration = Math.max(1L, line.duration());
        long progress = context.state().getLong("progress");
        if (progress >= duration) {
            if (complete(context, line)) progress = 0;
        } else {
            long eu = Math.max(1L, line.euPerTick());
            boolean paid = energy == null ? context.consume(eu) : context.consume(energy, eu);
            if (paid) {
                long advance = step.applyAsLong(context, line);
                progress = advance > 0 && progress > Long.MAX_VALUE - advance ? Long.MAX_VALUE : Math.max(0, progress + advance);
                if (progress >= duration && complete(context, line)) progress = 0;
                context.working(true);
            }
        }
        StateChanges.setLong(context.state(), "progress", progress);
        context.progress(Math.min(1.0F, progress / (float) duration));
    }

    public boolean canComplete(MachineBehaviorContext context, AddonRecipes.Line line) {
        return RecipeTransaction.plan(context, slots, line) != null;
    }
    public boolean complete(MachineBehaviorContext context, AddonRecipes.Line line) {
        if (!allowed.test(context, line)) return false;
        RecipeTransaction transaction = RecipeTransaction.plan(context, slots, line);
        if (transaction == null) return false;
        var selected = new java.util.ArrayList<ItemStack>();
        ItemStack[] outputs = line.itemOutputs();
        int[] chances = line.outputChances();
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] == null || outputs[i].isEmpty()) continue;
            if (OutputRolls.succeeds(chances[i], () -> context.level().getRandom().nextInt(10000))) selected.add(outputs[i]);
        }
        transaction = RecipeTransaction.plan(context, slots, line, selected.toArray(ItemStack[]::new));
        if (transaction == null) throw new IllegalStateException("Output subset did not fit after full preflight");
        transaction.apply(context);
        finished.accept(context);
        return true;
    }
    public void idle(MachineBehaviorContext context) {
        context.working(false);
        StateChanges.setLong(context.state(), "progress", 0L);
        context.progress(0);
    }
}
