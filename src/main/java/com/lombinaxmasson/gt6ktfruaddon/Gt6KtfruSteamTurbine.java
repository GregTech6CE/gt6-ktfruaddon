package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Large steam turbine. The 1.21 shaft turns two millibuckets of steam into one RU
 * and condenses 170 millibuckets into one of distilled water. Waste is on, so a
 * packet that does not leave is dropped. The gulp is capped at {@code rate * 2}
 * because the declared output is one packet of {@code rate}.
 */
public final class Gt6KtfruSteamTurbine implements MachineBehavior {
    static final int STEAM_PER_WATER = 170;
    private final long rate;

    Gt6KtfruSteamTurbine(long rate) {
        this.rate = rate;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (!context.structureFormed()) {
            context.working(false);
            return;
        }
        int available = context.fluid(0).getAmount();
        int gulp = (int) Math.min(available, rate * 2L);
        gulp -= gulp % 2;
        if (gulp < 2) {
            context.working(false);
            return;
        }
        FluidStack drained = context.drain(0, gulp, false);
        if (drained.isEmpty()) {
            context.working(false);
            return;
        }
        condense(context, drained.getAmount());
        long ru = Math.min(rate, drained.getAmount() / 2L);
        if (context.energy() < ru) context.generate(ru - context.energy());
        long sent = context.energy() >= ru ? context.emit(ru, 1) : 0;
        if (sent == 0 && context.energy() > 0) context.consume(context.energy());
        context.working(sent > 0);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return tank == 0 && steam(stack);
    }

    private static void condense(MachineBehaviorContext context, int taken) {
        long counter = context.state().getLong("steam") + taken;
        long water = counter / STEAM_PER_WATER;
        context.state().setLong("steam", counter % STEAM_PER_WATER);
        if (water < 1) return;
        FluidStack distilled = AddonFluids.still("DistW", (int) Math.min(water, Integer.MAX_VALUE));
        if (distilled != null) context.fill(1, distilled, false);
    }

    private static boolean steam(FluidStack stack) {
        FluidStack steam = AddonFluids.still("Steam", 1);
        return steam != null && stack != null && !stack.isEmpty()
                && stack.getFluid() == steam.getFluid();
    }
}
