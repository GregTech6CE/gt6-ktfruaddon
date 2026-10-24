package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Discrete-pulse laser inertial fusion controller ({@code LaserFusionControllerBase}).
 * Experimental (30071): 14 arrays x 2M = 29,360,128 LU peak, 2 stores x 8M = 16,777,216 LU pulse capacity,
 * 131,072 LU max packet, 240 tick cooldown.
 * Industrial (30072): 72 arrays x 40M = 3,019,898,880 LU peak, 8 stores x 128M = 1,073,741,824 LU pulse capacity,
 * 2,097,152 LU max packet, 80 tick cooldown.
 */
public final class Gt6KtfruLaserFusion implements MachineBehavior {
    public static final int STATE_IDLE = 0;
    public static final int STATE_CHARGING = 1;
    public static final int STATE_COOLING = 2;
    public static final int STATE_ERROR = 3;

    private final long pulseCapacity;
    private final long peakEnergy;
    private final int cooldownTicks;

    public Gt6KtfruLaserFusion(long pulseCapacity, long peakEnergy, int cooldownTicks) {
        this.pulseCapacity = pulseCapacity;
        this.peakEnergy = peakEnergy;
        this.cooldownTicks = cooldownTicks;
    }

    public static Gt6KtfruLaserFusion experimental() {
        return new Gt6KtfruLaserFusion(2L * 8L * 1024L * 1024L, 14L * 2L * 1024L * 1024L, 20 * 12);
    }

    public static Gt6KtfruLaserFusion industrial() {
        return new Gt6KtfruLaserFusion(8L * 134_217_728L, 72L * 41_943_040L, 20 * 4);
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        var state = context.state();
        if (!context.structureFormed()) {
            long buffer = context.energy();
            if (buffer > 0) context.consume(buffer);
            long aux = context.energy("LU");
            if (aux > 0) context.consume("LU", aux);
            state.setInt("state", STATE_ERROR);
            context.working(false);
            context.progress(0);
            return;
        }
        int cooldown = state.getInt("cooldown");
        if (cooldown > 0) {
            long buffer = context.energy();
            if (buffer > 0) context.consume(buffer);
            long aux = context.energy("LU");
            if (aux > 0) context.consume("LU", aux);
            cooldown--;
            state.setInt("cooldown", cooldown);
            state.setInt("state", STATE_COOLING);
            context.working(true);
            context.progress(cooldown / (float) Math.max(1, cooldownTicks));
            return;
        }
        long pulse = state.getLong("pulse");
        long incoming = context.energy();
        if (incoming > 0) {
            long room = Math.max(0L, pulseCapacity - pulse);
            long take = Math.min(incoming, room);
            if (take > 0 && context.consume(take)) {
                pulse += take;
            }
            long leftover = context.energy();
            if (leftover > 0) context.consume(leftover);
        }
        long auxIncoming = context.energy("LU");
        if (auxIncoming > 0) {
            long room = Math.max(0L, pulseCapacity - pulse);
            long take = Math.min(auxIncoming, room);
            if (take > 0 && context.consume("LU", take)) {
                pulse += take;
            }
            long leftover = context.energy("LU");
            if (leftover > 0) context.consume("LU", leftover);
        }
        if (tryFirePulse(context, pulse)) {
            return;
        }
        state.setLong("pulse", pulse);
        state.setInt("state", pulse > 0 ? STATE_CHARGING : STATE_IDLE);
        context.working(pulse > 0);
        context.progress(Math.min(1.0F, pulse / (float) Math.max(1L, pulseCapacity)));
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return (slot == 0 || slot == 1) && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return (tank == 0 || tank == 1) && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        int st = context.state().getInt("state");
        long pulse = context.state().getLong("pulse");
        int cooldown = context.state().getInt("cooldown");
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.laser_fusion.status",
                st, pulse, pulseCapacity, peakEnergy, cooldown), false);
        return true;
    }

    private boolean tryFirePulse(MachineBehaviorContext context, long pulse) {
        AddonRecipes.Line line = AddonRecipes.map(RecipeMaps.LASER_FUSION).find(
                List.of(context.item(0), context.item(1)),
                List.of(context.fluid(0), context.fluid(1)),
                true, 1, 2);
        if (line == null) return false;
        long requiredPulse = line.special() > 0
                ? line.special()
                : Math.max(1L, line.euPerTick()) * Math.max(1L, line.duration());
        if (pulse < requiredPulse || peakEnergy < requiredPulse) return false;
        if (!pays(context, line) || !canStoreOutputs(context, line)) return false;

        ItemStack[] inItems = line.itemInputs();
        if (inItems != null) {
            for (int i = 0; i < inItems.length; i++) {
                ItemStack need = inItems[i];
                if (need == null || need.isEmpty() || kept(line, i)) continue;
                int slot = inputSlotHolding(context, need);
                if (slot >= 0) {
                    ItemStack have = context.item(slot).copy();
                    have.shrink(need.getCount());
                    context.setItem(slot, have.isEmpty() ? ItemStack.EMPTY : have);
                }
            }
        }
        FluidStack[] inFluids = line.fluidInputs();
        if (inFluids != null) {
            for (FluidStack need : inFluids) {
                if (need == null || need.isEmpty()) continue;
                int tank = inputTankHolding(context, need);
                if (tank >= 0) context.drain(tank, need.getAmount(), false);
            }
        }
        FluidStack[] outFluids = line.fluidOutputs();
        if (outFluids != null) {
            for (FluidStack out : outFluids) {
                if (out == null || out.isEmpty()) continue;
                for (int tank = 2; tank <= 3; tank++) {
                    if (fitsTank(context, tank, out)) {
                        context.fill(tank, out.copy(), false);
                        break;
                    }
                }
            }
        }
        ItemStack[] outItems = line.itemOutputs();
        if (outItems != null && outItems.length > 0 && outItems[0] != null && !outItems[0].isEmpty()) {
            ItemStack out = outItems[0].copy();
            ItemStack have = context.item(2);
            if (!have.isEmpty()) out.grow(have.getCount());
            context.setItem(2, out);
        }

        context.state().setLong("pulse", pulse - requiredPulse);
        context.state().setInt("cooldown", cooldownTicks);
        context.state().setInt("state", STATE_COOLING);
        context.working(true);
        context.progress(1.0F);
        return true;
    }

    private static boolean pays(MachineBehaviorContext context, AddonRecipes.Line line) {
        ItemStack[] inItems = line.itemInputs();
        if (inItems != null) {
            for (ItemStack need : inItems) {
                if (need == null || need.isEmpty()) continue;
                if (inputSlotHolding(context, need) < 0) return false;
            }
        }
        FluidStack[] inFluids = line.fluidInputs();
        if (inFluids != null) {
            for (FluidStack need : inFluids) {
                if (need == null || need.isEmpty()) continue;
                if (inputTankHolding(context, need) < 0) return false;
            }
        }
        return true;
    }

    private static boolean canStoreOutputs(MachineBehaviorContext context, AddonRecipes.Line line) {
        FluidStack[] outFluids = line.fluidOutputs();
        if (outFluids != null) {
            for (FluidStack out : outFluids) {
                if (out == null || out.isEmpty()) continue;
                if (!fitsTank(context, 2, out) && !fitsTank(context, 3, out)) return false;
            }
        }
        ItemStack[] outItems = line.itemOutputs();
        if (outItems != null && outItems.length > 0 && outItems[0] != null && !outItems[0].isEmpty()) {
            ItemStack have = context.item(2);
            if (!have.isEmpty() && (!ItemStack.isSameItemSameComponents(have, outItems[0])
                    || have.getCount() + outItems[0].getCount() > have.getMaxStackSize())) {
                return false;
            }
        }
        return true;
    }

    private static int inputSlotHolding(MachineBehaviorContext context, ItemStack need) {
        for (int slot = 0; slot <= 1; slot++) {
            ItemStack have = context.item(slot);
            if (!have.isEmpty() && ItemStack.isSameItemSameComponents(have, need)
                    && have.getCount() >= need.getCount()) {
                return slot;
            }
        }
        return -1;
    }

    private static int inputTankHolding(MachineBehaviorContext context, FluidStack need) {
        for (int tank = 0; tank <= 1; tank++) {
            FluidStack have = context.fluid(tank);
            if (!have.isEmpty() && FluidStack.isSameFluidSameComponents(have, need)
                    && have.getAmount() >= need.getAmount()) {
                return tank;
            }
        }
        return -1;
    }

    private static boolean fitsTank(MachineBehaviorContext context, int tank, FluidStack stack) {
        FluidStack have = context.fluid(tank);
        if (have.isEmpty()) return stack.getAmount() <= context.capacity(tank);
        return FluidStack.isSameFluidSameComponents(have, stack)
                && have.getAmount() + stack.getAmount() <= context.capacity(tank);
    }

    private static boolean kept(AddonRecipes.Line line, int index) {
        int[] kept = line.keptInputs();
        if (kept == null) return false;
        for (int k : kept) if (k == index) return true;
        return false;
    }
}
