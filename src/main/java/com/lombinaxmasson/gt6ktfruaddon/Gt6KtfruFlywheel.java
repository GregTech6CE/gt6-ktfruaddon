package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Flywheel box. Capacity stays 0: the material catalog has no mass, tool quality
 * or tool speed, and the flywheel prefix is not registered. A right click can
 * still park an item whose id contains {@code flywheel}.
 */
public final class Gt6KtfruFlywheel implements MachineBehavior {
    private static final long[] VMAX = {8, 32, 128, 512, 2048, 8192};

    private final int ampere;
    private final double lossPercent;

    public Gt6KtfruFlywheel(int ampere, double lossPercent) {
        this.ampere = ampere;
        this.lossPercent = lossPercent;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        applyLoss(context);
        long size = vmax(context);
        long stored = context.energy();
        if (size >= 1 && stored >= size) {
            long packets = Math.min(ampere, stored / size);
            if (packets >= 1) context.emit(size, packets);
        }
        context.working(context.energy() > 0);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, net.neoforged.neoforge.fluids.FluidStack stack) {
        return false;
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (AddonTools.isMagnifyingGlass(held)) {
            player.displayClientMessage(Component.translatable(
                    "gt6ktfruaddon.flywheel.contents", contents(context)), false);
            return true;
        }
        if (held.isEmpty()) return takeOne(context, player);
        if (!flywheel(held)) return false;
        for (int slot = 0; slot < context.itemSlots(); slot++) {
            if (!context.item(slot).isEmpty()) continue;
            context.setItem(slot, held.copyWithCount(1));
            if (!player.getAbilities().instabuild) held.shrink(1);
            return true;
        }
        return false;
    }

    /** Source loss: every 200 ticks, on tick 5, keep {@code 1 - percent/100}. */
    private void applyLoss(MachineBehaviorContext context) {
        int timer = context.state().getInt("loss") + 1;
        context.state().setInt("loss", timer);
        if (timer % 200 != 5) return;
        long stored = context.energy();
        long kept = (long) Math.floor(stored * (1.0 - lossPercent / 100.0));
        long drop = stored - kept;
        if (drop > 0) context.consume(drop);
    }

    private static boolean takeOne(MachineBehaviorContext context, Player player) {
        for (int slot = context.itemSlots() - 1; slot >= 0; slot--) {
            ItemStack stack = context.item(slot);
            if (stack.isEmpty()) continue;
            context.setItem(slot, ItemStack.EMPTY);
            player.getInventory().placeItemBackInInventory(stack);
            return true;
        }
        return false;
    }

    private static String contents(MachineBehaviorContext context) {
        StringBuilder text = new StringBuilder();
        for (int slot = 0; slot < context.itemSlots(); slot++) {
            ItemStack stack = context.item(slot);
            if (stack.isEmpty()) continue;
            if (!text.isEmpty()) text.append(", ");
            text.append(stack.getHoverName().getString());
        }
        return text.isEmpty() ? "-" : text.toString();
    }

    private static boolean flywheel(ItemStack stack) {
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id != null && id.getPath().contains("flywheel");
    }

    private static long vmax(MachineBehaviorContext context) {
        int index = context.tier() - 1;
        if (index < 0 || index >= VMAX.length) return VMAX[0];
        return VMAX[index];
    }
}
