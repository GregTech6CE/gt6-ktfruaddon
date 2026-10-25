package com.lombinaxmasson.gt6ktfruaddon.network;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

import com.lombinaxmasson.gt6ktfruaddon.network.WirelessInbox.Mark;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/** Receives queued wireless energy and emits packets to adjacent machines. */
public final class WirelessReceiver extends BehaviorBase {
    private final boolean quantum;

    public WirelessReceiver(boolean quantum) {
        this.quantum = quantum;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
        var key = new DimPos(dim, context.pos().immutable());
        long packet = quantum ? 32768 : 8192;
        long amps = quantum ? 16 : 4;
        boolean stopped = context.toggle(0);
        long now = context.level().getGameTime();
        if (stopped) {
            WirelessInbox.setRoom(key, 0L, now);
            context.working(false);
            return;
        }
        WirelessInbox.receive(key, context::generate);
        long packets = Math.min(amps, context.energy() / packet);
        if (packets > 0) context.emit(packet, packets);
        WirelessInbox.setRoom(key, Math.max(0L, context.capacity() - context.energy()), now);
        context.working(packets > 0);
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        WirelessInbox.remove(new DimPos(context.level().dimension().location().toString(), context.pos().immutable()));
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        String dim = context.level().dimension().location().toString();
        WirelessInbox.remember(player.getUUID(), new Mark(dim, context.pos(), quantum));
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.wireless.remembered",
                context.pos().getX(), context.pos().getY(), context.pos().getZ()), false);
        return true;
    }
}
