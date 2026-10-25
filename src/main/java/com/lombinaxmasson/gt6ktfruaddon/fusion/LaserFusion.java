package com.lombinaxmasson.gt6ktfruaddon.fusion;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.RecipeRunner;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.Slots;

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
public final class LaserFusion extends BehaviorBase {
    public static final int STATE_IDLE = 0;
    public static final int STATE_CHARGING = 1;
    public static final int STATE_COOLING = 2;
    public static final int STATE_ERROR = 3;

    static final Slots SLOTS = new Slots(Slots.range(0, 2), List.of(2), Slots.range(0, 2), Slots.range(2, 2));
    private final RecipeRunner runner = new RecipeRunner(SLOTS, List.of(RecipeMaps.LASER_FUSION));
    private final long pulseCapacity;
    private final long peakEnergy;
    private final int cooldownTicks;

    public LaserFusion(long pulseCapacity, long peakEnergy, int cooldownTicks) {
        this.pulseCapacity = pulseCapacity;
        this.peakEnergy = peakEnergy;
        this.cooldownTicks = cooldownTicks;
    }

    public static LaserFusion experimental() {
        return new LaserFusion(2L * 8L * 1024L * 1024L, 14L * 2L * 1024L * 1024L, 20 * 12);
    }

    public static LaserFusion industrial() {
        return new LaserFusion(8L * 134_217_728L, 72L * 41_943_040L, 20 * 4);
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
        AddonRecipes.Line line = runner.find(context);
        if (line == null) return false;
        long requiredPulse = line.special() > 0
                ? line.special()
                : Math.max(1L, line.euPerTick()) * Math.max(1L, line.duration());
        if (pulse < requiredPulse || peakEnergy < requiredPulse) return false;
        if (!runner.complete(context, line)) return false;

        context.state().setLong("pulse", pulse - requiredPulse);
        context.state().setInt("cooldown", cooldownTicks);
        context.state().setInt("state", STATE_COOLING);
        context.working(true);
        context.progress(1.0F);
        return true;
    }

}
