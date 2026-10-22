package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * The wireless battery in this checkout. Capacity stays 367001600000.
 * While sealed and formed, each linked receiver is offered its own packet.
 * The receiver applies that offer on its own tick, then emits from the front.
 */
public final class Gt6KtfruWireless implements MachineBehavior {
    static final long CAPACITY = 367_001_600_000L;
    static final long VOLTAGE = 8192;
    static final long AMPERE = 4;
    static final int LINKS = 8;

    private final boolean tower;

    public Gt6KtfruWireless(boolean tower) {
        this.tower = tower;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (tower) tickTower(context);
        else tickReceiver(context);
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
        String dim = context.level().dimension().location().toString();
        if (!tower) {
            Link.remember(player, new Mark(dim, context.pos()));
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.wireless.remembered",
                    context.pos().getX(), context.pos().getY(), context.pos().getZ()), false);
            return true;
        }
        Mark pending = Link.take(player);
        if (pending != null && dim.equals(pending.dim()) && receiver(context, pending.pos())) {
            List<BlockPos> links = links(context);
            if (!links.contains(pending.pos()) && links.size() < LINKS) {
                links.add(pending.pos());
                write(context, links);
            }
        }
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.wireless.links",
                context.state().getString("links")), false);
        return true;
    }

    private static void tickTower(MachineBehaviorContext context) {
        boolean sealed = context.toggle(0);
        context.working(sealed && context.structureFormed());
        if (!sealed || !context.structureFormed()) return;
        String dim = context.level().dimension().location().toString();
        for (BlockPos pos : links(context)) {
            if (!context.level().hasChunkAt(pos) || !receiver(context, pos)) continue;
            var key = new Inbox.Key(dim, pos.getX(), pos.getY(), pos.getZ());
            long room = Inbox.room(key);
            long amps = Math.min(AMPERE, room / VOLTAGE);
            long cost = VOLTAGE * amps;
            if (amps < 1 || context.energy() <= VOLTAGE * AMPERE || !context.consume(cost)) continue;
            Inbox.give(key, cost);
        }
    }

    private static void tickReceiver(MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
        var key = new Inbox.Key(dim, context.pos().getX(), context.pos().getY(), context.pos().getZ());
        boolean stopped = context.toggle(0);
        long room = Math.max(0, context.capacity() - context.energy());
        Inbox.setRoom(key, stopped ? 0 : room);
        if (stopped) {
            context.working(false);
            return;
        }
        long incoming = Inbox.pull(key);
        if (incoming > 0) context.generate(incoming);
        long packets = Math.min(AMPERE, context.energy() / VOLTAGE);
        if (packets > 0) context.emit(VOLTAGE, packets);
        context.working(packets > 0);
    }

    private static boolean receiver(MachineBehaviorContext context, BlockPos pos) {
        var id = BuiltInRegistries.BLOCK.getKey(context.level().getBlockState(pos).getBlock());
        return id != null && Gt6KtfruAddon.MODID.equals(id.getNamespace()) && "wireless_receiver".equals(id.getPath());
    }

    private static List<BlockPos> links(MachineBehaviorContext context) {
        List<BlockPos> found = new ArrayList<>();
        String raw = context.state().getString("links");
        if (raw.isEmpty()) return found;
        for (String piece : raw.split(";")) {
            String[] xyz = piece.split(",");
            if (xyz.length != 3) continue;
            try {
                found.add(new BlockPos(Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2])));
            } catch (NumberFormatException ignored) {
                // A broken entry is skipped. The rest of the list stays.
            }
        }
        return found;
    }

    private static void write(MachineBehaviorContext context, List<BlockPos> links) {
        var text = new StringBuilder();
        for (BlockPos pos : links) {
            if (!text.isEmpty()) text.append(';');
            text.append(pos.getX()).append(',').append(pos.getY()).append(',').append(pos.getZ());
        }
        context.state().setString("links", text.toString());
    }

    private record Mark(String dim, BlockPos pos) {}

    private static final class Link {
        private static final Map<java.util.UUID, Mark> PENDING = new HashMap<>();

        static void remember(Player player, Mark mark) {
            PENDING.put(player.getUUID(), mark);
        }

        static Mark take(Player player) {
            return PENDING.remove(player.getUUID());
        }
    }

    static final class Inbox {
        record Key(String dim, int x, int y, int z) {}

        private static final Map<Key, Long> ROOM = new HashMap<>();
        private static final Map<Key, Long> WAITING = new HashMap<>();

        static void setRoom(Key key, long room) {
            ROOM.put(key, room);
        }

        static long room(Key key) {
            return ROOM.getOrDefault(key, 0L);
        }

        static void give(Key key, long amount) {
            WAITING.merge(key, amount, Long::sum);
        }

        static long pull(Key key) {
            Long waiting = WAITING.remove(key);
            return waiting == null ? 0 : waiting;
        }
    }
}
