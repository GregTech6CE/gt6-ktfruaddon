package com.lombinaxmasson.gt6ktfruaddon.research;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.EnergyBridge;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PosCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Optical microscope (256 EU/item) and electron microscope (4096 EU/item).
 * Consumes items from slot 0 while the structure is formed, unlocking the item in the bound
 * or adjacent {@code research_monitor} and placing any remainder into slot 1.
 */
public final class Microscope extends BehaviorBase {
    private final long perItem;

    public Microscope(long perItem) {
        this.perItem = perItem;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        EnergyBridge.pull(context, "EU");
        if (!context.structureFormed()) {
            context.working(false);
            return;
        }
        BlockPos monitor = resolveMonitor(context);
        ItemStack stack = context.item(0);
        if (stack.isEmpty() || context.energy() < perItem) {
            context.working(false);
            return;
        }
        int remaining = stack.getCount() - 1;
        if (remaining > 0 && !context.item(1).isEmpty()) {
            context.working(false);
            return;
        }
        if (!context.consume(perItem)) {
            context.working(false);
            return;
        }
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id != null && monitor != null) {
            Research.unlockItem(context.level(), monitor, id.toString());
        }
        if (remaining <= 0) {
            context.setItem(0, ItemStack.EMPTY);
        } else {
            ItemStack remainder = stack.copy();
            remainder.setCount(remaining);
            context.setItem(1, remainder);
            context.setItem(0, ItemStack.EMPTY);
        }
        context.working(true);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return slot == 0 && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        String dim = context.level().dimension().location().toString();
        DimPos pending = Research.takePendingMonitor(player.getUUID(), dim);
        if (pending != null) {
            context.state().setString("monitor", PosCodec.format(pending.pos()));
        }
        BlockPos monitor = resolveMonitor(context);
        String label = monitor == null ? "none" : PosCodec.format(monitor);
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.microscope.status",
                context.structureFormed() ? 1 : 0, label, perItem), false);
        return true;
    }

    private static BlockPos resolveMonitor(MachineBehaviorContext context) {
        BlockPos saved = PosCodec.parse(context.state().getString("monitor"));
        if (saved != null && Research.isMonitorOnline(context.level(), saved)) {
            return saved;
        }
        BlockPos adjacent = Research.findAdjacentMonitor(context.level(), context.pos());
        if (adjacent != null) {
            context.state().setString("monitor", PosCodec.format(adjacent));
            return adjacent;
        }
        return saved;
    }
}
