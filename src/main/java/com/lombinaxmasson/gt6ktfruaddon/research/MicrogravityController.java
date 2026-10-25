package com.lombinaxmasson.gt6ktfruaddon.research;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.StateChanges;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/** Spins for 600 ticks at 2048 EU/tick, then registers its microgravity room. */
public final class MicrogravityController extends BehaviorBase {
    @Override
    public void tick(MachineBehaviorContext context) {
        tickRoom(context);
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        MicrogravityRooms.updateRoom(context.level(), context.pos(), false);
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        int spin = context.state().getInt("spin");
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.microgravity_room.status",
                context.structureFormed() ? 1 : 0, spin, spin >= 600 ? 1 : 0), false);
        return true;
    }

    private static void tickRoom(MachineBehaviorContext context) {
        if (!context.structureFormed() || !context.consume(2048)) {
            StateChanges.setInt(context.state(), "spin", 0);
            context.working(false);
            context.progress(0);
            MicrogravityRooms.updateRoom(context.level(), context.pos(), false);
            return;
        }
        int spin = Math.min(600, context.state().getInt("spin") + 1);
        StateChanges.setInt(context.state(), "spin", spin);
        context.progress(spin / 600.0F);
        boolean active = spin >= 600;
        context.working(active);
        MicrogravityRooms.updateRoom(context.level(), context.pos(), active);
    }
}
