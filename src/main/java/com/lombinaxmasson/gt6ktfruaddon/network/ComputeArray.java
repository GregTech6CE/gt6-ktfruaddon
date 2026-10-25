package com.lombinaxmasson.gt6ktfruaddon.network;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PosCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/** Converts allocated Normal compute to the configured output unit. */
public final class ComputeArray extends BehaviorBase {
    private final long provided;
    private final long required;
    private final String unit;

    public ComputeArray(long provided, long required, String unit) {
        this.provided = provided;
        this.required = required;
        this.unit = unit;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        boolean formed = context.structureFormed();
        boolean active = false;
        if (formed) {
            if (required <= 0L) {
                active = true;
            } else {
                BlockPos upstream = PosCodec.parse(context.state().getString("controller"));
                if (upstream != null && ComputeNetwork.allocate(context.level(), upstream, "Normal", required)) {
                    active = true;
                }
            }
        }
        ComputeNetwork.publish(context, formed, unit, active ? provided : 0L);
        context.working(active);
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        ComputeNetwork.selectArray(player.getUUID(), context);
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.array",
                context.structureFormed() ? 1 : 0, required, provided, unit), false);
        return true;
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        ComputeNetwork.remove(context);
    }
}
