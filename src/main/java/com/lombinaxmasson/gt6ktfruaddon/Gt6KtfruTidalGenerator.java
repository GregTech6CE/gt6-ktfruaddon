package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Tidal generator. Once the structure matches, the top face emits
 * {@code sin(worldTime / 31.4) * 128 + 96} RU. A negative result is not a packet.
 */
public final class Gt6KtfruTidalGenerator implements MachineBehavior {
    static final int MAX_RATE = 224;

    @Override
    public void tick(MachineBehaviorContext context) {
        if (!context.structureFormed()) {
            context.working(false);
            return;
        }
        int rate = rate(context.level().getGameTime());
        context.state().setInt("rate", rate);
        if (rate < 1) {
            context.working(false);
            return;
        }
        if (context.energy() < rate) context.generate(rate - context.energy());
        if (context.energy() >= rate) context.emit(rate, 1);
        context.working(true);
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
        if (!com.gregtech.gregtech.api.addon.platform.AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) {
            return false;
        }
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.tidal.rate",
                context.state().getInt("rate")), false);
        return true;
    }

    static int rate(long timer) {
        return (int) (Math.sin(timer / 31.4) * 128) + 96;
    }
}
