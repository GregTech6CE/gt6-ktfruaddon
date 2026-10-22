package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Research blocks. The monitor keeps a small tree of vanilla items.
 * The registration names a 512 EU output and no input. The behavior machine
 * still needs an input face, and this class does not create that energy.
 */
public final class Gt6KtfruResearch implements MachineBehavior {
    static final String TREE = "minecraft:paper,minecraft:glass,minecraft:iron_ingot";

    private final boolean monitor;

    public Gt6KtfruResearch(boolean monitor) {
        this.monitor = monitor;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (monitor && context.state().getString("tree").isEmpty()) {
            context.state().setString("tree", TREE);
        }
        context.working(false);
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
        if (!monitor || !AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        player.displayClientMessage(Component.translatable(
                "gt6ktfruaddon.research.tree", context.state().getString("tree")), false);
        return true;
    }
}
