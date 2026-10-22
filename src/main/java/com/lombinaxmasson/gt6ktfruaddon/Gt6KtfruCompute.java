package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Wireless controllers link a normal compute node inside 512 blocks of the same dimension.
 * Arrays report the source compute numbers while the structure is formed. They do not schedule a cluster.
 * A structure compute cell accepts one node block, not every compute part.
 */
public final class Gt6KtfruCompute implements MachineBehavior {
    static final long REACH = 512;

    enum Kind { PLAIN, CLUSTER, NODE, WIRELESS, ARRAY }

    private final Kind kind;
    private final long provided;
    private final long required;
    private final String unit;

    public Gt6KtfruCompute(Kind kind, long provided, long required, String unit) {
        this.kind = kind;
        this.provided = provided;
        this.required = required;
        this.unit = unit;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        boolean formed = context.structureFormed();
        context.working(kind == Kind.ARRAY && formed);
        if (kind == Kind.WIRELESS) context.working(formed && !context.state().getString("links").isEmpty());
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return kind == Kind.CLUSTER;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return false;
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        String dim = context.level().dimension().location().toString();
        if (kind == Kind.NODE) {
            Link.remember(player.getUUID(), new Mark(dim, context.pos(), unit));
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.node",
                    provided, unit), false);
            return true;
        }
        if (kind == Kind.WIRELESS) {
            Mark pending = Link.take(player.getUUID());
            if (pending != null && dim.equals(pending.dim()) && reachable(context.pos(), pending.pos())
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
        if (kind == Kind.ARRAY) {
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.array",
                    context.structureFormed() ? 1 : 0, required, provided, unit), false);
            return true;
        }
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.plain", provided), false);
        return true;
    }

    private static boolean reachable(BlockPos from, BlockPos to) {
        return from.distSqr(to) <= REACH * REACH;
    }

    private static boolean node(MachineBehaviorContext context, BlockPos pos, String prefix) {
        if (!context.level().hasChunkAt(pos)) return false;
        BlockState state = context.level().getBlockState(pos);
        var id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return id != null && Gt6KtfruAddon.MODID.equals(id.getNamespace()) && id.getPath().startsWith(prefix);
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

    private record Mark(String dim, BlockPos pos, String unit) {}

    private static final class Link {
        private static final Map<UUID, Mark> PENDING = new HashMap<>();

        static void remember(UUID player, Mark mark) {
            PENDING.put(player, mark);
        }

        static Mark take(UUID player) {
            return PENDING.remove(player);
        }
    }
}
