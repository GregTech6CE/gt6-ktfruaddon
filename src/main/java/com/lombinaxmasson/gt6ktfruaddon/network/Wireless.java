package com.lombinaxmasson.gt6ktfruaddon.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Inductive links stay in one dimension, within 64 blocks, and lose energy with distance.
 * Quantum links cross dimensions, ignore distance, and only talk to the quantum receiver.
 * The hub stores EU. The source can lock another energy type; this port does not.
 */
public final class Wireless implements MachineBehavior {
    static final long INDUCTIVE_CAPACITY = 367_001_600_000L;
    static final long QUANTUM_CAPACITY = 1_468_006_400_000L;

    enum Kind { INDUCTIVE, QUANTUM, INDUCTIVE_RECEIVER, QUANTUM_RECEIVER }

    private final Kind kind;

    public Wireless(Kind kind) {
        this.kind = kind;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (kind == Kind.INDUCTIVE || kind == Kind.QUANTUM) tickTower(context);
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
        if (kind == Kind.INDUCTIVE_RECEIVER || kind == Kind.QUANTUM_RECEIVER) {
            Link.remember(player.getUUID(), new Mark(dim, context.pos(), kind == Kind.QUANTUM_RECEIVER));
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.wireless.remembered",
                    context.pos().getX(), context.pos().getY(), context.pos().getZ()), false);
            return true;
        }
        Mark pending = Link.take(player.getUUID());
        boolean quantum = kind == Kind.QUANTUM;
        if (pending != null && pending.quantum() == quantum && receiver(context, pending)) {
            List<Mark> links = links(context);
            if (links.size() < (quantum ? 32 : 8) && links.stream().noneMatch(link -> link.same(pending))) {
                links.add(pending);
                write(context, links);
            }
        }
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.wireless.links", links(context).size()), false);
        return true;
    }

    private void tickTower(MachineBehaviorContext context) {
        long aux = context.energy("EU");
        if (aux > 0) {
            long move = Math.min(aux, Math.max(0L, context.capacity() - context.energy()));
            if (move > 0 && context.consume("EU", move)) context.generate(move);
        }
        boolean sealed = context.toggle(0);
        boolean quantum = kind == Kind.QUANTUM;
        context.working(sealed && context.structureFormed());
        if (!sealed || !context.structureFormed()) return;
        long packet = quantum ? 32768 : 8192;
        long amps = quantum ? 64 : 16;
        for (Mark link : links(context)) {
            Level world = world(context, link.dim());
            if (world == null || !world.hasChunkAt(link.pos()) || !receiverBlock(world, link.pos(), quantum)) continue;
            if (!quantum && !inRange(context.pos(), link.pos())) continue;
            var key = new Inbox.Key(link.dim(), link.pos().getX(), link.pos().getY(), link.pos().getZ());
            long room = Inbox.room(key);
            long sentAmps = Math.min(amps, room / packet);
            if (sentAmps < 1) continue;
            long each = quantum ? packet : lossy(packet, context.pos(), link.pos());
            long cost = each * sentAmps;
            if (context.energy() < cost || !context.consume(cost)) continue;
            Inbox.give(key, packet * sentAmps);
        }
    }

    private void tickReceiver(MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
        var key = new Inbox.Key(dim, context.pos().getX(), context.pos().getY(), context.pos().getZ());
        boolean quantum = kind == Kind.QUANTUM_RECEIVER;
        long packet = quantum ? 32768 : 8192;
        long amps = quantum ? 16 : 4;
        boolean stopped = context.toggle(0);
        long room = Math.max(0, context.capacity() - context.energy());
        Inbox.setRoom(key, stopped ? 0 : room);
        if (stopped) {
            context.working(false);
            return;
        }
        long incoming = Inbox.pull(key);
        if (incoming > 0) context.generate(incoming);
        long packets = Math.min(amps, context.energy() / packet);
        if (packets > 0) context.emit(packet, packets);
        context.working(packets > 0);
    }

    /** Loss is clamp(0.05 + 0.25 * min(1, distance / 64), 0, 0.95). The tower pays the inflated cost. */
    private static long lossy(long packet, BlockPos from, BlockPos to) {
        double distance = Math.sqrt(from.distSqr(to));
        double loss = 0.05 + 0.25 * Math.min(1.0, distance / 64.0);
        if (loss > 0.95) loss = 0.95;
        return (long) Math.ceil(packet / (1.0 - loss));
    }

    private static boolean inRange(BlockPos from, BlockPos to) {
        return from.distSqr(to) <= 64.0 * 64.0;
    }

    private static boolean receiver(MachineBehaviorContext context, Mark mark) {
        Level world = world(context, mark.dim());
        return world != null && world.hasChunkAt(mark.pos()) && receiverBlock(world, mark.pos(), mark.quantum());
    }

    private static boolean receiverBlock(Level world, BlockPos pos, boolean quantum) {
        var id = BuiltInRegistries.BLOCK.getKey(world.getBlockState(pos).getBlock());
        String path = quantum ? WirelessMachines.QUANTUM_RECEIVER : WirelessMachines.INDUCTIVE_RECEIVER;
        return id != null && Ktfru.MODID.equals(id.getNamespace()) && path.equals(id.getPath());
    }

    private static Level world(MachineBehaviorContext context, String dim) {
        if (context.level().dimension().location().toString().equals(dim)) return context.level();
        if (!(context.level() instanceof ServerLevel serverLevel)) return null;
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(dim));
        return serverLevel.getServer().getLevel(key);
    }

    private static List<Mark> links(MachineBehaviorContext context) {
        List<Mark> found = new ArrayList<>();
        String raw = context.state().getString("links");
        if (raw.isEmpty()) return found;
        for (String piece : raw.split("\\|")) {
            int split = piece.lastIndexOf(',');
            int middle = piece.lastIndexOf(',', split - 1);
            int first = piece.lastIndexOf(',', middle - 1);
            if (first < 0) continue;
            try {
                String dim = piece.substring(0, first);
                int x = Integer.parseInt(piece.substring(first + 1, middle));
                int y = Integer.parseInt(piece.substring(middle + 1, split));
                int z = Integer.parseInt(piece.substring(split + 1));
                found.add(new Mark(dim, new BlockPos(x, y, z), kindOf(context)));
            } catch (NumberFormatException ignored) {
                // A broken entry is skipped.
            }
        }
        return found;
    }

    private static boolean kindOf(MachineBehaviorContext context) {
        var id = BuiltInRegistries.BLOCK.getKey(context.level().getBlockState(context.pos()).getBlock());
        return id != null && WirelessMachines.QUANTUM.equals(id.getPath());
    }

    private static void write(MachineBehaviorContext context, List<Mark> links) {
        var text = new StringBuilder();
        for (Mark mark : links) {
            if (!text.isEmpty()) text.append('|');
            text.append(mark.dim()).append(',').append(mark.pos().getX()).append(',')
                    .append(mark.pos().getY()).append(',').append(mark.pos().getZ());
        }
        context.state().setString("links", text.toString());
    }

    private record Mark(String dim, BlockPos pos, boolean quantum) {
        boolean same(Mark other) {
            return quantum == other.quantum && dim.equals(other.dim) && pos.equals(other.pos);
        }
    }

    private static final class Link {
        private static final Map<UUID, Mark> PENDING = new HashMap<>();

        static void remember(UUID player, Mark mark) {
            PENDING.put(player, mark);
        }

        static Mark take(UUID player) {
            return PENDING.remove(player);
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
