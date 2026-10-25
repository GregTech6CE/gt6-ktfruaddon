package com.lombinaxmasson.gt6ktfruaddon.network;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/** Compute user status display; it does not publish compute to the network. */
public class ComputeUser extends BehaviorBase {
    private final long provided;

    public ComputeUser(long provided) {
        this.provided = provided;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        context.working(false);
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        ComputeNetwork.remove(context);
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.plain", provided), false);
        return true;
    }
}
