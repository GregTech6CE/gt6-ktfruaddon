package com.lombinaxmasson.gt6ktfruaddon.network;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/** Publishes the live output of a compute node. */
public final class ComputeNode extends BehaviorBase {
    private final long provided;
    private final String unit;

    public ComputeNode(long provided, String unit) {
        this.provided = provided;
        this.unit = unit;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        ComputeNetwork.registerNode(context, provided, unit);
        context.working(true);
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        ComputeNetwork.rememberNode(player.getUUID(), context, unit);
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.node", provided, unit), false);
        return true;
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        ComputeNetwork.remove(context);
    }
}
