package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Large compressed-gas valve. The source tank only accepts gas from a compressor
 * and pushes whatever is stored into the fluid handler on its front. This valve
 * accepts gas from pipes into its own tanks, and each tick offers the front
 * neighbor a compressed-gas drum fill. A pipe or an ordinary tank on that face
 * accepts nothing from {@code pushCompressedGas}; pipes can still pull because
 * the tanks are {@code ANY}.
 */
public final class Gt6KtfruGasTank implements MachineBehavior {
    @Override
    public void tick(MachineBehaviorContext context) {
        if (!context.structureFormed()) {
            context.working(false);
            context.progress(0);
            return;
        }
        voidNonGas(context);
        boolean pushed = pushFront(context);
        context.working(pushed);
        context.progress(fillFraction(context));
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, net.minecraft.world.item.ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        if (!AddonFluids.gas(stack)) return false;
        FluidStack resident = resident(context);
        if (!resident.isEmpty() && !FluidStack.isSameFluidSameComponents(resident, stack)) return false;
        FluidStack here = context.fluid(tank);
        return here.isEmpty() || FluidStack.isSameFluidSameComponents(here, stack);
    }

    private static void voidNonGas(MachineBehaviorContext context) {
        for (int tank = 0; tank < context.tanks(); tank++) {
            FluidStack fluid = context.fluid(tank);
            if (!fluid.isEmpty() && !AddonFluids.gas(fluid)) {
                context.drain(tank, fluid.getAmount(), false);
            }
        }
    }

    /** Offers the first occupied tank to the compressed drum in front. */
    private static boolean pushFront(MachineBehaviorContext context) {
        for (int tank = 0; tank < context.tanks(); tank++) {
            FluidStack fluid = context.fluid(tank);
            if (fluid.isEmpty()) continue;
            int accepted = context.pushCompressedGas(context.facing(), fluid.copy());
            if (accepted <= 0) return false;
            context.drain(tank, accepted, false);
            return true;
        }
        return false;
    }

    private static float fillFraction(MachineBehaviorContext context) {
        long stored = 0;
        long capacity = 0;
        for (int tank = 0; tank < context.tanks(); tank++) {
            stored += context.fluid(tank).getAmount();
            capacity += context.capacity(tank);
        }
        if (capacity <= 0) return 0;
        return stored / (float) capacity;
    }

    private static FluidStack resident(MachineBehaviorContext context) {
        for (int tank = 0; tank < context.tanks(); tank++) {
            FluidStack fluid = context.fluid(tank);
            if (!fluid.isEmpty()) return fluid;
        }
        return FluidStack.EMPTY;
    }
}
