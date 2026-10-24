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
 * Mantle heater. An RU packet whose energy is above 128 raises progress by
 * {@code sqrt(energy - 128)}. Past 16384 the stored heat climbs toward 5000000.
 * The source emits that heat from five blocks above the top; this buffer only stores it.
 * The drill column down to bedrock is not part of the fixed layout.
 */
public final class MantleHeater implements MachineBehavior {
    static final long HEAT_CAPACITY = 5_000_000;

    @Override
    public void tick(MachineBehaviorContext context) {
        var state = context.state();
        long arrived = context.energy("RU");
        if (arrived > 0) context.consume("RU", arrived);
        if (!context.structureFormed()) {
            context.working(false);
            return;
        }
        int progress = state.getInt("progress");
        if (arrived > 128 && progress < 32767) {
            progress = Math.min(32767, progress + (int) Math.sqrt(arrived - 128));
            state.setInt("progress", progress);
        }
        long heat = state.getLong("heat");
        if (progress > 16384 && heat < HEAT_CAPACITY) {
            long add = (long) (((progress - 16384) / 16.0) * (1.1 - heat / (double) HEAT_CAPACITY));
            if (add > 0) state.setLong("heat", Math.min(HEAT_CAPACITY, heat + add));
        }
        context.working(progress > 16384);
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
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.mantle.heat",
                context.state().getLong("heat"), context.state().getInt("progress")), false);
        return true;
    }
}
