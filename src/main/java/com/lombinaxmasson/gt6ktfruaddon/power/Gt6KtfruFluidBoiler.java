package com.lombinaxmasson.gt6ktfruaddon.power;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Fluid boiler. Hot-fuel rows fill a heat buffer of {@code rate * 2}.
 * Each 80 heat turns 1 mB of distilled water into 160 mB of steam.
 * The middle structure slice is fixed at one; the source allows eight.
 */
public final class Gt6KtfruFluidBoiler implements MachineBehavior {
    static final long RATE = 4096;
    private static final String HOT = "gt.recipe.fuels.hot";
    private static final int FUEL = 0;
    private static final int WATER = 1;
    private static final int STEAM = 2;
    private static final int EXHAUST = 3;

    @Override
    public void tick(MachineBehaviorContext context) {
        if (!context.structureFormed()) {
            context.working(false);
            return;
        }
        burn(context);
        boil(context);
        context.working(context.state().getLong("heat") > 0 || !context.fluid(STEAM).isEmpty());
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (tank == WATER) return same(stack, "DistW");
        return tank == FUEL && !same(stack, "DistW") && !same(stack, "Steam");
    }

    private static void burn(MachineBehaviorContext context) {
        long heat = context.state().getLong("heat");
        long cap = RATE * 2;
        if (heat >= cap) return;
        FluidStack fuel = context.fluid(FUEL);
        if (fuel.isEmpty()) return;
        if (context.fluid(EXHAUST).getAmount() * 2L >= context.capacity(EXHAUST)) return;
        AddonRecipes.Line line = AddonRecipes.map(HOT).find(List.of(), List.of(fuel), false, 0, 0);
        if (line == null || line.euPerTick() >= 0 || line.duration() <= 0) return;
        FluidStack[] inputs = line.fluidInputs();
        if (inputs == null || inputs.length == 0 || inputs[0] == null || inputs[0].isEmpty()) return;
        int need = inputs[0].getAmount();
        if (fuel.getAmount() < need) return;
        FluidStack exhaust = exhaust(line);
        if (exhaust != null && context.fill(EXHAUST, exhaust.copy(), true) < exhaust.getAmount()) return;
        context.drain(FUEL, need, false);
        if (exhaust != null) context.fill(EXHAUST, exhaust.copy(), false);
        long gain = -line.euPerTick() * line.duration();
        if (gain < 0) gain = Long.MAX_VALUE / 4;
        context.state().setLong("heat", Math.min(cap, heat + gain));
    }

    private static void boil(MachineBehaviorContext context) {
        long heat = context.state().getLong("heat");
        int water = context.fluid(WATER).getAmount();
        long conversions = Math.min(context.capacity(STEAM) / 2560L, Math.min(Math.min(RATE, heat) / 80, water));
        if (conversions < 1) return;
        FluidStack steam = AddonFluids.still("Steam", 1);
        if (steam == null) return;
        int room = context.fill(STEAM, steam.copyWithAmount((int) Math.min(conversions * 160, Integer.MAX_VALUE)), true);
        conversions = Math.min(conversions, room / 160L);
        if (conversions < 1) return;
        context.drain(WATER, (int) conversions, false);
        context.fill(STEAM, steam.copyWithAmount((int) (conversions * 160)), false);
        context.state().setLong("heat", heat - conversions * 80);
    }

    private static FluidStack exhaust(AddonRecipes.Line line) {
        FluidStack[] outputs = line.fluidOutputs();
        if (outputs == null || outputs.length == 0 || outputs[0] == null || outputs[0].isEmpty()) return null;
        return outputs[0];
    }

    private static boolean same(FluidStack stack, String name) {
        FluidStack fluid = AddonFluids.still(name, 1);
        return fluid != null && stack.getFluid() == fluid.getFluid();
    }
}
