package com.lombinaxmasson.gt6ktfruaddon.network;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

/** Sums bound Normal compute nodes within 512 blocks in the same dimension. */
public final class WirelessComputeController extends BehaviorBase {
    @Override
    public void tick(MachineBehaviorContext context) {
        boolean formed = context.structureFormed();
        long total = ComputeNetwork.wirelessOutput(context, formed ? links(context) : List.of());
        context.state().setLong("available", total);
        context.working(formed && total > 0);
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        String dim = context.level().dimension().location().toString();
        BlockPos here = context.pos().immutable();
        ComputeNetwork.Mark pending = ComputeNetwork.selectWireless(player.getUUID(), context);
        if (pending != null && dim.equals(pending.dim()) && ComputeNetwork.reachable(here, pending.pos())
                && node(context, pending.pos(), "wireless_compute_node")) {
            List<BlockPos> links = links(context);
            if (!links.contains(pending.pos())) {
                links.add(pending.pos());
                write(context, links);
            }
        }
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.links",
                context.state().getString("links").isEmpty() ? 0 : links(context).size()), false);
        return true;
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        ComputeNetwork.remove(context);
    }

    private static boolean node(MachineBehaviorContext context, BlockPos pos, String prefix) {
        if (!context.level().hasChunkAt(pos)) return false;
        BlockState state = context.level().getBlockState(pos);
        var id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return id != null && Ktfru.MODID.equals(id.getNamespace()) && id.getPath().startsWith(prefix);
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
                // A broken entry is skipped.
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
}
