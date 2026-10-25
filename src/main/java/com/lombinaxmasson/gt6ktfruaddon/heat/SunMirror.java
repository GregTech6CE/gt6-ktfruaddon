package com.lombinaxmasson.gt6ktfruaddon.heat;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/**
 * Large sun mirror. The frame can form. Aiming needs a stored sun-heater coordinate,
 * and none of the three addon tools writes one.
 */
public final class SunMirror extends BehaviorBase {
    @Override
    public void tick(MachineBehaviorContext context) {
        context.working(context.structureFormed());
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.sun_mirror.no_target"), false);
        return true;
    }
}
