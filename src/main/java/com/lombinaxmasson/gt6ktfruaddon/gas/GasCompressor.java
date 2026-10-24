package com.lombinaxmasson.gt6ktfruaddon.gas;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Gas compressor. Each tick spends the KU stored for this tick and offers
 * ten millibuckets per KU to the compressed drum on the back face.
 * The packet is spent even when the drum accepts nothing.
 */
public final class GasCompressor implements MachineBehavior {
    @Override
    public void tick(MachineBehaviorContext context) {
        voidNonGas(context);
        long ku = context.energy();
        int accepted = ku > 0 ? pushBack(context, ku) : 0;
        if (ku > 0) context.consume(ku);
        context.working(accepted > 0);
        context.progress(fillFraction(context));
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, net.minecraft.world.item.ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        if (!AddonFluids.gas(stack)) return false;
        FluidStack here = context.fluid(tank);
        return here.isEmpty() || FluidStack.isSameFluidSameComponents(here, stack);
    }

    private static void voidNonGas(MachineBehaviorContext context) {
        FluidStack fluid = context.fluid(0);
        if (!fluid.isEmpty() && !AddonFluids.gas(fluid)) {
            context.drain(0, fluid.getAmount(), false);
        }
    }

    /** Offers {@code ku * 10} mB to the drum behind the compressor. */
    private static int pushBack(MachineBehaviorContext context, long ku) {
        FluidStack gas = context.fluid(0);
        if (gas.isEmpty()) return 0;
        long offer = Math.min(gas.getAmount(), ku * 10L);
        if (offer > Integer.MAX_VALUE) offer = Integer.MAX_VALUE;
        if (offer <= 0) return 0;
        int accepted = context.pushCompressedGas(
                context.facing().getOpposite(), gas.copyWithAmount((int) offer));
        if (accepted > 0) context.drain(0, accepted, false);
        return accepted;
    }

    private static float fillFraction(MachineBehaviorContext context) {
        int capacity = context.capacity(0);
        if (capacity <= 0) return 0;
        return context.fluid(0).getAmount() / (float) capacity;
    }
}
