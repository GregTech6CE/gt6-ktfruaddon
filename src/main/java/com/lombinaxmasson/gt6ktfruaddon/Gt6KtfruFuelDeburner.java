package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Fuel deburn factory. Temperature is kelvin stored in milli-units. Ambient is gregapi
 * {@code C}, 293 K. Self mass stays 81920 because item and fluid mass are not readable.
 * HU adds {@code energy * 64 / mass}. Cooling is {@code min(10 * |T - C| / mass, |T - C|)}.
 * Progress for the tick is {@code EU * temperature factor}, and one completion spends the
 * circuit and the fluids.
 */
public final class Gt6KtfruFuelDeburner implements MachineBehavior {
    static final long MASS = 81920;
    static final long AMBIENT_MILLI = 293_000;
    static final long MAX_MILLI = 4_000_000;
    private static final int OUT = 3;

    @Override
    public void tick(MachineBehaviorContext context) {
        heat(context);
        long temp = context.state().getLong("temp");
        if (!context.structureFormed() || temp > MAX_MILLI) {
            context.working(false);
            context.progress(0);
            return;
        }
        AddonRecipes.Line line = find(context);
        if (line == null) {
            context.state().setLong("progress", 0);
            context.progress(0);
            context.working(false);
            return;
        }
        long duration = Math.max(1, line.duration());
        long progress = context.state().getLong("progress");
        long eu = Math.max(1, line.euPerTick());
        if (progress >= duration) {
            if (finish(context, line)) progress = 0;
        } else if (context.energy() >= eu && context.consume(eu)) {
            progress = Math.max(0, progress + Math.round(eu * factor(temp, line)));
            if (progress >= duration && finish(context, line)) progress = 0;
            context.working(true);
        } else {
            context.working(false);
        }
        context.state().setLong("progress", progress);
        context.progress(Math.min(1.0F, progress / (float) duration));
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return slot == 0 && context.item(0).isEmpty() && circuit(stack) >= 0;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return tank >= 0 && tank < OUT && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.deburn.temperature",
                context.state().getLong("temp") / 1000.0), false);
        return true;
    }

    private static void heat(MachineBehaviorContext context) {
        var state = context.state();
        long temp = state.getLong("temp");
        if (temp == 0) temp = AMBIENT_MILLI;
        long hu = context.energy("HU");
        if (hu > 0 && context.consume("HU", hu)) temp += hu * 64L * 1000L / MASS;
        long delta = Math.abs(temp - AMBIENT_MILLI);
        long cool = Math.min(10L * delta / MASS, delta);
        if (temp > AMBIENT_MILLI) temp -= cool;
        else temp += cool;
        state.setLong("temp", temp);
    }

    /** Source {@code getTempFactor}. A zero difference is the cap, 128. */
    static double factor(long tempMilli, AddonRecipes.Line line) {
        double recipeFactor = 20.1 - 20.0 * Math.min(1.0, line.euPerTick() / 1024.0);
        if (recipeFactor <= 0) return 0;
        double delta = Math.abs(tempMilli / 1000.0 - line.special());
        if (delta < 1.0e-6) return 128;
        double value = (256.0 * recipeFactor / Math.sqrt(delta) - Math.pow(delta, 1.5) / recipeFactor) / recipeFactor;
        return Math.min(128.0, value);
    }

    private static AddonRecipes.Line find(MachineBehaviorContext context) {
        if (context.item(0).isEmpty()) return null;
        return AddonRecipes.map(Gt6KtfruMachines.FUEL_DEBURN).find(
                List.of(context.item(0)),
                List.of(context.fluid(0), context.fluid(1), context.fluid(2)),
                true, 1, 0);
    }

    private static boolean finish(MachineBehaviorContext context, AddonRecipes.Line line) {
        FluidStack[] inputs = line.fluidInputs();
        FluidStack[] outputs = line.fluidOutputs();
        if (outputs == null || outputs.length != 1 || outputs[0] == null || outputs[0].isEmpty()) return false;
        if (!fits(context, outputs[0]) || !holds(context, inputs) || context.item(0).isEmpty()) return false;
        for (FluidStack need : inputs) {
            if (need == null || need.isEmpty()) continue;
            context.drain(tankHolding(context, need), need.getAmount(), false);
        }
        ItemStack circuit = context.item(0).copy();
        circuit.shrink(1);
        context.setItem(0, circuit.isEmpty() ? ItemStack.EMPTY : circuit);
        context.fill(OUT, outputs[0], false);
        return true;
    }

    private static boolean holds(MachineBehaviorContext context, FluidStack[] inputs) {
        if (inputs == null) return true;
        for (FluidStack need : inputs) {
            if (need == null || need.isEmpty()) continue;
            int tank = tankHolding(context, need);
            if (tank < 0) return false;
            FluidStack preview = context.drain(tank, need.getAmount(), true);
            if (preview.getAmount() < need.getAmount()) return false;
        }
        return true;
    }

    private static int tankHolding(MachineBehaviorContext context, FluidStack need) {
        for (int tank = 0; tank < OUT; tank++) {
            FluidStack here = context.fluid(tank);
            if (!here.isEmpty() && FluidStack.isSameFluidSameComponents(here, need)) return tank;
        }
        return -1;
    }

    private static boolean fits(MachineBehaviorContext context, FluidStack stack) {
        FluidStack here = context.fluid(OUT);
        if (!here.isEmpty() && !FluidStack.isSameFluidSameComponents(here, stack)) return false;
        return here.getAmount() + stack.getAmount() <= context.capacity(OUT);
    }

    static int circuit(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return -1;
        for (int n = 0; n <= 4; n++) {
            ItemStack tag = AddonItems.selectorTag(n);
            if (tag != null && !tag.isEmpty() && ItemStack.isSameItem(tag, stack)) return n;
        }
        return -1;
    }
}
