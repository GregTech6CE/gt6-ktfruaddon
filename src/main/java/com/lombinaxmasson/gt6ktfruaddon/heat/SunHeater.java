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
 * Sun heater fixed at one absorb layer. HU stays in the buffer. The source dumps it
 * from the 3×3 above the top layer, and {@code emit} only reaches a declared face.
 */
public final class SunHeater implements MachineBehavior {
    static final long PER_LAYER = 256;
    static final long STORE_PER_LAYER = 5_000_000;
    static final long LAYERS = 1;

    @Override
    public void tick(MachineBehaviorContext context) {
        boolean formed = context.structureFormed();
        long stored = context.energy();
        long rate = formed ? emitRate(stored, context.capacity()) : 0;
        context.state().setLong("rate", rate);
        context.working(formed && rate > 0 && stored > rate * 9);
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
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.sun_heater.heat",
                context.energy(), context.state().getLong("rate")), false);
        return true;
    }

    static long capacity() {
        return STORE_PER_LAYER * LAYERS + 10_000_000;
    }

    /** Source {@code getEmitRate} for one absorb layer. */
    static long emitRate(long stored, long capacity) {
        long max = PER_LAYER * LAYERS;
        long hot = capacity / 5;
        if (hot < 1 || stored > hot) return max;
        return stored * max / hot;
    }
}
