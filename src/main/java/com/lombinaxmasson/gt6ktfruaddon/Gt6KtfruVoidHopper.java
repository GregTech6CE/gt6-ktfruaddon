package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

/**
 * Void hopper. Slot 0 is the filter. Slots 1–9 take matching item entities.
 * A screwdriver and a soft hammer are not in {@link AddonTools}, so the blacklist
 * and the clear action are gui toggles.
 */
public final class Gt6KtfruVoidHopper implements MachineBehavior {
    private static final int FILTER = 0;
    private static final int BLACKLIST = 0;
    private static final int CLEAR = 1;
    private static final int EXAMINED_PER_TICK = 64;
    private static final int TAKEN_PER_TICK = 16;

    private static final long[] INPUT = {64, 32, 128, 512};
    private static final int[] RANGE = {4, 8, 16, 24};

    @Override
    public void tick(MachineBehaviorContext context) {
        long input = input(context);
        long stored = context.energy();
        if (stored < input / 2) {
            context.working(false);
            return;
        }
        context.consume(Math.min(stored, input * 2));
        boolean absorbed = absorb(context, RANGE[index(context)]);
        context.working(absorbed);
        context.progress(absorbed ? 1 : 0);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return slot == FILTER;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, net.neoforged.neoforge.fluids.FluidStack stack) {
        return false;
    }

    @Override
    public void onToggle(MachineBehaviorContext context, int toggle) {
        if (toggle != CLEAR || !context.toggle(CLEAR)) return;
        ItemStack filter = context.item(FILTER);
        context.setItem(FILTER, ItemStack.EMPTY);
        if (!filter.isEmpty()) context.insertOutput(filter);
        context.setToggle(BLACKLIST, false);
        context.setToggle(CLEAR, false);
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (AddonTools.isMagnifyingGlass(held)) {
            ItemStack filter = context.item(FILTER);
            String name = filter.isEmpty() ? "-" : filter.getHoverName().getString();
            player.displayClientMessage(Component.translatable(
                    context.toggle(BLACKLIST)
                            ? "gt6ktfruaddon.void_hopper.mode.blacklist"
                            : "gt6ktfruaddon.void_hopper.mode.whitelist",
                    name), false);
            return true;
        }
        if (held.isEmpty()) return false;
        ItemStack previous = context.item(FILTER);
        context.setItem(FILTER, held.copyWithCount(1));
        if (!player.getAbilities().instabuild) held.shrink(1);
        if (!previous.isEmpty()) player.getInventory().placeItemBackInInventory(previous);
        player.displayClientMessage(Component.translatable(
                "gt6ktfruaddon.void_hopper.filter", context.item(FILTER).getHoverName()), false);
        return true;
    }

    private static boolean absorb(MachineBehaviorContext context, int range) {
        var pos = context.pos();
        var box = new AABB(pos.getX() - range, pos.getY() - range, pos.getZ() - range,
                pos.getX() + range + 1.0, pos.getY() + range + 1.0, pos.getZ() + range + 1.0);
        ItemStack filter = context.item(FILTER);
        boolean blacklist = context.toggle(BLACKLIST);
        int examined = 0;
        int taken = 0;
        boolean absorbed = false;
        for (ItemEntity entity : context.level().getEntitiesOfClass(ItemEntity.class, box)) {
            if (examined >= EXAMINED_PER_TICK || taken >= TAKEN_PER_TICK) break;
            examined++;
            if (!entity.isAlive()) continue;
            ItemStack stack = entity.getItem();
            if (stack.isEmpty() || !passes(filter, blacklist, stack)) continue;
            int before = stack.getCount();
            ItemStack left = context.insertOutput(stack.copy());
            if (left.getCount() == before) continue;
            if (left.isEmpty()) entity.discard();
            else entity.setItem(left);
            taken++;
            absorbed = true;
        }
        return absorbed;
    }

    /** An empty filter allows everything unless the blacklist toggle is on. */
    private static boolean passes(ItemStack filter, boolean blacklist, ItemStack stack) {
        if (filter.isEmpty()) return !blacklist;
        boolean same = ItemStack.isSameItemSameComponents(filter, stack);
        return blacklist ? !same : same;
    }

    private static long input(MachineBehaviorContext context) {
        return INPUT[index(context)];
    }

    private static int index(MachineBehaviorContext context) {
        int index = context.tier() - 1;
        if (index < 0 || index >= INPUT.length) return 0;
        return index;
    }
}
