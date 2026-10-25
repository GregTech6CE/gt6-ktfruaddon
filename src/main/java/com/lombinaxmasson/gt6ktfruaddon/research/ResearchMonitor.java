package com.lombinaxmasson.gt6ktfruaddon.research;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/** Registers and persists the research tree and progress of a monitor. */
public final class ResearchMonitor extends BehaviorBase {
    @Override
    public void tick(MachineBehaviorContext context) {
        ResearchNetwork.update(context);
        context.working(false);
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        ResearchNetwork.remove(context);
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        ResearchNetwork.remember(player.getUUID(), context);
        player.displayClientMessage(Component.translatable(
                "gt6ktfruaddon.research.tree", context.state().getString("tree")), false);
        return true;
    }
}
