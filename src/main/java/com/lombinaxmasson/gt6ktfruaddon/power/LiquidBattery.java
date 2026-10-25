package com.lombinaxmasson.gt6ktfruaddon.power;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Liquid battery. RU enters the auxiliary buffer and moves into the main
 * buffer, whose cap is the formed sink. One air cell is layer 0, so the
 * multiplier is 1. Output size follows the adaptive fill between the
 * registered minimum and maximum. Loss runs on the same 200-tick phase
 * as the other batteries, and only while the buffer is above the midpoint.
 */
public final class LiquidBattery extends BehaviorBase {
    static final int TANK = 8_000;
    private static final int HOT = 573;

    private final long outputMin;
    private final long outputMax;
    private final long ampere;
    private final double lossPercent;

    public LiquidBattery(long outputMin, long outputMax, long ampere, double lossPercent) {
        this.outputMin = outputMin;
        this.outputMax = outputMax;
        this.ampere = ampere;
        this.lossPercent = lossPercent;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        voidUnsafe(context);
        long computed = context.structureFormed() ? capacity(context) : 0;
        long stored = context.energy();
        context.setCapacity(computed > 0 ? computed : Math.max(stored, 1));
        takeAuxiliary(context);
        stored = context.energy();
        long midpoint = (outputMin + outputMax) / 2;
        boolean active = stored > midpoint;
        if (active && computed > 0) emit(context, computed);
        if (active) lose(context);
        long cap = context.capacity();
        context.progress(cap <= 0 ? 0 : Math.min(1.0F, context.energy() / (float) cap));
        context.working(active && computed > 0);
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return stack != null && !stack.isEmpty() && !unsafe(stack);
    }

    private void voidUnsafe(MachineBehaviorContext context) {
        FluidStack fluid = context.fluid(0);
        if (fluid.isEmpty() || !unsafe(fluid)) return;
        context.drain(0, fluid.getAmount(), false);
    }

    /**
     * Source voids gas and power-conducting fluids; acid, magic and hot fluids explode.
     * The behavior context has no explosion, so every unsafe fluid is refused and voided.
     */
    private static boolean unsafe(FluidStack stack) {
        return conductive(stack) || corrosive(stack);
    }

    private static boolean conductive(FluidStack stack) {
        return AddonFluids.gas(stack) || AddonFluids.plasma(stack);
    }

    private static boolean corrosive(FluidStack stack) {
        return AddonFluids.acid(stack) || AddonFluids.magic(stack) || temperature(stack) > HOT;
    }

    /** Layer 0 of one 8000 mB cell. Density at or below 10 uses 0.01. */
    private static long capacity(MachineBehaviorContext context) {
        FluidStack fluid = context.fluid(0);
        if (fluid.isEmpty()) return 0;
        int density = fluid.getFluid().getFluidType().getDensity(fluid);
        double factor = density <= 10 ? 0.01D : density / 1000.0D;
        return (long) (fluid.getAmount() * factor);
    }

    private void takeAuxiliary(MachineBehaviorContext context) {
        long available = context.energy("RU");
        if (available <= 0) return;
        long moved = context.generate(available);
        if (moved > 0) context.consume("RU", moved);
    }

    private void emit(MachineBehaviorContext context, long cap) {
        long stored = Math.min(context.energy(), cap);
        if (stored < 1) return;
        long fill = ampere * stored / cap;
        long bound = cap / ampere;
        long size;
        if (bound < 1) {
            size = outputMax;
        } else if (fill * bound == stored) {
            size = outputMax;
        } else {
            size = outputMin + (outputMax - outputMin) * (stored - fill * bound) / bound;
        }
        if (size < 1) size = 1;
        if (size > outputMax) size = outputMax;
        long packets = fill + (stored == cap ? 0 : 1);
        if (packets > ampere) packets = ampere;
        if (packets >= 1) context.emit(size, packets);
    }

    /** Keep {@code 1 - percent/100} when the local timer is 5 mod 200. */
    private void lose(MachineBehaviorContext context) {
        int timer = context.state().getInt("loss") + 1;
        context.state().setInt("loss", timer);
        if (timer % 200 != 5) return;
        long stored = context.energy();
        long kept = (long) Math.floor(stored * (1.0D - lossPercent / 100.0D));
        long drop = stored - kept;
        if (drop > 0) context.consume(drop);
    }

    private static int temperature(FluidStack stack) {
        return stack.getFluid().getFluidType().getTemperature(stack);
    }
}
