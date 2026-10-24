package com.lombinaxmasson.gt6ktfruaddon.power;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * Gas battery. Poles stay in the slots. The electrolyte tank has to hold its amount and is not drained.
 * Nickel with CaTiO3 requires yttrium-zirconium oxide. Carbon with platinum requires sulfuric acid.
 * Each recipe row names that fluid as a 0 mB presence-only catalyst.
 * Toggle 0 stops processing. Toggle 1 opens the electrolyte tank and closes the fuel tanks.
 */
public final class Gt6KtfruFuelBattery implements MachineBehavior {
    private static final int FUEL_A = 0;
    private static final int FUEL_B = 1;
    private static final int OUT_A = 2;
    private static final int OUT_B = 3;
    private static final int ELECTROLYTE = 4;
    /** Same order as the toggles on the behavior machine. */
    private static final int STOPPED = 0;
    private static final int EDITING = 1;
    /** Largest {@code |EUt| * duration} on the source rows, 384 * 13. */
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
        return context.item(slot).isEmpty() && stack.getDescriptionId().contains("ktfru.item.battery.pole");
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (context.toggle(EDITING)) return tank == ELECTROLYTE;
        return tank == FUEL_A || tank == FUEL_B;
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.fuel_battery.composition",
                name(context.fluid(ELECTROLYTE)), name(context.item(0)), name(context.item(1))), false);
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.fuel_battery.input",
                name(context.fluid(FUEL_A)), name(context.fluid(FUEL_B))), false);
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.fuel_battery.output",
                name(context.fluid(OUT_A)), name(context.fluid(OUT_B))), false);
        return true;
    }

    private static boolean ready(MachineBehaviorContext context, long rate) {
        return !context.toggle(STOPPED)
                && !context.toggle(EDITING)
                && context.energy() < rate * 2
                && !context.item(0).isEmpty()
                && !context.item(1).isEmpty()
                && context.fluid(ELECTROLYTE).getAmount() >= electrolyte(context)
                && electrolyteMatches(context);
    }

    /** The pole pair names the electrolyte. Either order is the same pair. */
    private static boolean electrolyteMatches(MachineBehaviorContext context) {
        String left = context.item(0).getDescriptionId();
        String right = context.item(1).getDescriptionId();
        if (pair(left, right, "ktfru.item.battery.pole.nickel", "ktfru.item.battery.pole.catio3"))
            return sameFluid(context.fluid(ELECTROLYTE), "GenLiquid_YttriumZirconiumOxide");
        if (pair(left, right, "ktfru.item.battery.pole.carbon", "ktfru.item.battery.pole.platinum"))
            return sameFluid(context.fluid(ELECTROLYTE), "GenLiquid_SulfuricAcid");
        return false;
    }

    private static boolean pair(String left, String right, String a, String b) {
        return (left.contains(a) && right.contains(b)) || (left.contains(b) && right.contains(a));
    }

    private static boolean sameFluid(FluidStack tank, String field) {
        FluidStack want = AddonFluids.still(field, 1);
        return want != null && !want.isEmpty() && FluidStack.isSameFluidSameComponents(tank, want);
    }

    private static boolean craft(MachineBehaviorContext context, long rate) {
        if (!electrolyteMatches(context)) return false;
        AddonRecipes.Line line = find(context);
        if (line == null || !outputsFit(context, line)) return false;
        alignFuels(context, line);
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

    /** Put the recipe's first fuel into tank 0 when it is sitting in tank 1. */
    private static void alignFuels(MachineBehaviorContext context, AddonRecipes.Line line) {
        FluidStack[] inputs = line.fluidInputs();
        if (inputs == null || inputs.length == 0 || inputs[0] == null || inputs[0].isEmpty()) return;
        FluidStack first = context.fluid(FUEL_A);
        if (!first.isEmpty() && FluidStack.isSameFluidSameComponents(first, inputs[0])) return;
        FluidStack second = context.fluid(FUEL_B);
        if (second.isEmpty() || !FluidStack.isSameFluidSameComponents(second, inputs[0])) return;
        int firstAmount = first.getAmount();
        int secondAmount = second.getAmount();
        if (firstAmount > 0) context.drain(FUEL_A, firstAmount, false);
        context.drain(FUEL_B, secondAmount, false);
        context.fill(FUEL_A, second, false);
        if (firstAmount > 0) context.fill(FUEL_B, first, false);
    }

    private static Component name(FluidStack stack) {
        return stack == null || stack.isEmpty()
                ? Component.translatable("gt6ktfruaddon.fuel_battery.empty") : stack.getHoverName();
    }

    private static Component name(ItemStack stack) {
        return stack == null || stack.isEmpty()
                ? Component.translatable("gt6ktfruaddon.fuel_battery.empty") : stack.getHoverName();
    }

    private static AddonRecipes.Line find(MachineBehaviorContext context) {
        return AddonRecipes.map(RecipeMaps.FUEL_BATTERY).find(
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
        for (int tank : new int[] {FUEL_A, FUEL_B}) {
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
