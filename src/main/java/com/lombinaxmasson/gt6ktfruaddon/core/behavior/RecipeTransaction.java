package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/** Preflights all input and output slots together, then applies the transaction on the server thread. */
final class RecipeTransaction {
    private final Slots slots;
    private final List<ItemStack> itemsIn;
    private final List<ItemStack> itemsOut;
    private final List<FluidStack> fluidsIn;
    private final List<FluidStack> fluidsOut;

    private RecipeTransaction(Slots slots, List<ItemStack> itemsIn, List<ItemStack> itemsOut,
                              List<FluidStack> fluidsIn, List<FluidStack> fluidsOut) {
        this.slots = slots; this.itemsIn = itemsIn; this.itemsOut = itemsOut;
        this.fluidsIn = fluidsIn; this.fluidsOut = fluidsOut;
    }

    static RecipeTransaction plan(MachineBehaviorContext context, Slots slots, AddonRecipes.Line line) {
        var items = new Ledger<>(RecipeStacks.ITEMS);
        var fluids = new Ledger<>(RecipeStacks.FLUIDS);
        var itemsIn = items.consume(slots.itemsIn().stream().map(context::item).toList(),
                needs(line.itemInputs(), line.keptInputs()), RecipeStacks.ITEMS::same);
        var fluidsIn = fluids.consume(slots.fluidsIn().stream().map(context::fluid).toList(),
                needs(line.fluidInputs(), line.keptFluidInputs()), RecipeStacks.FLUIDS::same);
        var itemsOut = items.produce(slots.itemsOut().stream().map(context::item).toList(),
                Arrays.asList(line.itemOutputs()), (slot, stack) -> stack.getMaxStackSize());
        var fluidsOut = fluids.produce(slots.fluidsOut().stream().map(context::fluid).toList(),
                Arrays.asList(line.fluidOutputs()), (slot, stack) -> context.capacity(slots.fluidsOut().get(slot)));
        if (itemsIn == null || fluidsIn == null || itemsOut == null || fluidsOut == null) return null;
        return new RecipeTransaction(slots, itemsIn, itemsOut, fluidsIn, fluidsOut);
    }

    private static <S> List<Ledger.Need<S>> needs(S[] stacks, int[] kept) {
        var result = new ArrayList<Ledger.Need<S>>();
        for (int i = 0; i < stacks.length; i++) {
            int index = i;
            result.add(new Ledger.Need<>(stacks[i], Arrays.stream(kept).anyMatch(k -> k == index)));
        }
        return result;
    }

    void apply(MachineBehaviorContext context) {
        for (int i = 0; i < itemsIn.size(); i++) {
            int slot = slots.itemsIn().get(i);
            if (!ItemStack.matches(context.item(slot), itemsIn.get(i))) context.setItem(slot, itemsIn.get(i));
        }
        for (int i = 0; i < itemsOut.size(); i++) {
            int slot = slots.itemsOut().get(i);
            if (!ItemStack.matches(context.item(slot), itemsOut.get(i))) context.setItem(slot, itemsOut.get(i));
        }
        for (int i = 0; i < fluidsIn.size(); i++) {
            int tank = slots.fluidsIn().get(i);
            int drain = context.fluid(tank).getAmount() - fluidsIn.get(i).getAmount();
            if (drain > 0) context.drain(tank, drain, false);
        }
        for (int i = 0; i < fluidsOut.size(); i++) {
            int tank = slots.fluidsOut().get(i);
            int fill = fluidsOut.get(i).getAmount() - context.fluid(tank).getAmount();
            if (fill > 0) context.fill(tank, fluidsOut.get(i).copyWithAmount(fill), false);
        }
    }
}
