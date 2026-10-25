package com.lombinaxmasson.gt6ktfruaddon.network;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.EnergyBridge;

import com.lombinaxmasson.gt6ktfruaddon.network.WirelessInbox.Mark;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Inductive towers send within 64 blocks with distance loss; quantum towers send across dimensions. */
public final class WirelessTower extends BehaviorBase {
    static final long INDUCTIVE_CAPACITY = 367_001_600_000L;
    static final long QUANTUM_CAPACITY = 1_468_006_400_000L;
    private final boolean quantum;

    public WirelessTower(boolean quantum) {
        this.quantum = quantum;
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        Mark pending = WirelessInbox.take(player.getUUID());
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

    @Override
    public void tick(MachineBehaviorContext context) {
        EnergyBridge.pull(context, "EU");
        boolean sealed = context.toggle(0);
        context.working(sealed && context.structureFormed());
        if (!sealed || !context.structureFormed()) return;
        long packet = quantum ? 32768 : 8192;
        long amps = quantum ? 64 : 16;
        for (Mark link : links(context)) {
            Level world = world(context, link.dim());
            if (world == null || !world.hasChunkAt(link.pos()) || !receiverBlock(world, link.pos(), quantum)) continue;
            if (!quantum && !inRange(context.pos(), link.pos())) continue;
            var key = new WirelessInbox.Key(link.dim(), link.pos().getX(), link.pos().getY(), link.pos().getZ());
            long room = WirelessInbox.room(key);
            long sentAmps = Math.min(amps, room / packet);
            if (sentAmps < 1) continue;
            long each = quantum ? packet : lossy(packet, context.pos(), link.pos());
            long cost = each * sentAmps;
            if (context.energy() < cost || !context.consume(cost)) continue;
            WirelessInbox.give(key, packet * sentAmps);
        }
    }

    /** The tower pays packet / (1 - loss), where loss is 5% to 30% over 64 blocks. */
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
}
