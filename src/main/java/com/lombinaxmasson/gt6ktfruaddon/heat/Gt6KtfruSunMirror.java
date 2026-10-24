package com.lombinaxmasson.gt6ktfruaddon.heat;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Large sun mirror. The frame can form. Aiming needs a stored sun-heater coordinate,
 * and none of the three addon tools writes one.
 */
public final class Gt6KtfruSunMirror implements MachineBehavior {
    @Override
    public void tick(MachineBehaviorContext context) {
        context.working(context.structureFormed());
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return false;
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.sun_mirror.no_target"), false);
        return true;
    }
}
