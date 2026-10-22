package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * Gas battery. Poles stay in the slots. The electrolyte tank has to be full.
 * Recipe rows are not registered: every source row contains a zero-amount fluid.
 */
public final class Gt6KtfruFuelBattery implements MachineBehavior {
    private static final int FUEL_A = 0;
    private static final int FUEL_B = 1;
    private static final int OUT_A = 2;
    private static final int OUT_B = 3;
    private static final int ELECTROLYTE = 4;
    /** Largest {@code |EUt| * duration} on the unread-as-registered source rows, 384 * 13. */
    private static final long RECIPE_HEADROOM = 4992;

    @Override
    public void tick(MachineBehaviorContext context) {
        long rate = rate(context);
        if (context.energy() >= rate) context.emit(rate, 1);
        pushDown(context);
        boolean crafted = false;
        if (ready(context, rate)) crafted = craft(context, rate);
        context.working(crafted);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return stack.getDescriptionId().contains("ktfru.item.battery.pole");
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return tank == FUEL_A || tank == FUEL_B || tank == ELECTROLYTE;
    }

    private static boolean ready(MachineBehaviorContext context, long rate) {
        return context.energy() < rate * 2
                && !context.item(0).isEmpty()
                && !context.item(1).isEmpty()
                && context.fluid(ELECTROLYTE).getAmount() >= electrolyte(context);
    }

    private static boolean craft(MachineBehaviorContext context, long rate) {
        AddonRecipes.Line line = find(context);
        if (line == null || !outputsFit(context, line)) return false;
        boolean crafted = false;
        for (int pass = 0; pass < 16 && context.energy() < rate * 2; pass++) {
            if (!outputsFit(context, line) || !consumeFluids(context, line.fluidInputs())) break;
            fillOutputs(context, line.fluidOutputs());
            long gain = Math.abs(line.euPerTick()) * Math.max(1, line.duration());
            context.generate(gain);
            crafted = true;
        }
        return crafted;
    }

    private static AddonRecipes.Line find(MachineBehaviorContext context) {
        return AddonRecipes.map(Gt6KtfruMachines.FUEL_BATTERY).find(
                List.of(context.item(0), context.item(1)),
                List.of(context.fluid(FUEL_A), context.fluid(FUEL_B), context.fluid(ELECTROLYTE)),
                true, 2, 0);
    }

    private static boolean outputsFit(MachineBehaviorContext context, AddonRecipes.Line line) {
        FluidStack[] outputs = line.fluidOutputs();
        if (outputs == null || outputs.length == 0) return true;
        if (outputs.length > 2) return false;
        if (!fits(context, OUT_A, outputs[0])) return false;
        return outputs.length < 2 || fits(context, OUT_B, outputs[1]);
    }

    private static boolean fits(MachineBehaviorContext context, int tank, FluidStack stack) {
        if (stack == null || stack.isEmpty()) return true;
        FluidStack here = context.fluid(tank);
        if (!here.isEmpty() && !FluidStack.isSameFluidSameComponents(here, stack)) return false;
        return here.getAmount() + stack.getAmount() <= context.capacity(tank);
    }

    private static boolean consumeFluids(MachineBehaviorContext context, FluidStack[] inputs) {
        if (inputs == null) return true;
        for (FluidStack need : inputs) {
            if (need == null || need.isEmpty() || need.getAmount() <= 0) continue;
            int tank = tankHolding(context, need);
            if (tank < 0) return false;
            FluidStack preview = context.drain(tank, need.getAmount(), true);
            if (preview.getAmount() < need.getAmount()) return false;
        }
        for (FluidStack need : inputs) {
            if (need == null || need.isEmpty() || need.getAmount() <= 0) continue;
            context.drain(tankHolding(context, need), need.getAmount(), false);
        }
        return true;
    }

    private static int tankHolding(MachineBehaviorContext context, FluidStack need) {
        for (int tank : new int[] {FUEL_A, FUEL_B, ELECTROLYTE}) {
            FluidStack here = context.fluid(tank);
            if (!here.isEmpty() && FluidStack.isSameFluidSameComponents(here, need)) return tank;
        }
        return -1;
    }

    private static void fillOutputs(MachineBehaviorContext context, FluidStack[] outputs) {
        if (outputs == null) return;
        if (outputs.length > 0 && outputs[0] != null && !outputs[0].isEmpty())
            context.fill(OUT_A, outputs[0], false);
        if (outputs.length > 1 && outputs[1] != null && !outputs[1].isEmpty())
            context.fill(OUT_B, outputs[1], false);
    }

    private static void pushDown(MachineBehaviorContext context) {
        IFluidHandler handler = context.level().getCapability(
                Capabilities.FluidHandler.BLOCK, context.pos().below(), Direction.UP);
        if (handler == null) return;
        for (int tank = OUT_A; tank <= OUT_B; tank++) {
            FluidStack fluid = context.fluid(tank);
            if (fluid.isEmpty()) continue;
            int moved = handler.fill(fluid.copy(), IFluidHandler.FluidAction.EXECUTE);
            if (moved > 0) context.drain(tank, moved, false);
        }
    }

    static long rate(int tier) { return tier <= 1 ? 128 : 512; }

    static int electrolyte(int tier) { return tier <= 1 ? 1000 : 4000; }

    private static long rate(MachineBehaviorContext context) { return rate(context.tier()); }

    private static int electrolyte(MachineBehaviorContext context) { return electrolyte(context.tier()); }

    static long capacity(long rate) { return rate * 2 + RECIPE_HEADROOM; }
}
