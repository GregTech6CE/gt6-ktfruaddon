package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Compute blocks from this checkout. The wireless controller stores one
 * coordinate and accepts it inside 512 blocks. Electric reach is the six
 * neighbours. Wired reach walks this addon's network cable.
 */
public final class Gt6KtfruCompute implements MachineBehavior {
    static final long REACH = 512L;

    enum Kind {
        WIRELESS, ELECTRIC, WIRED, NODE, PLAIN;

        boolean saves() {
            return this == WIRELESS || this == ELECTRIC || this == WIRED;
        }
    }

    private final Kind kind;

    public Gt6KtfruCompute(Kind kind) {
        this.kind = kind;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (!kind.saves()) {
            context.working(false);
            return;
        }
        boolean linked = linked(context);
        context.state().setBoolean("linked", linked);
        context.working(linked);
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
        Mark pending = Link.take(player.getUUID());
        if (kind.saves() && pending != null && !pending.pos().equals(context.pos())) {
            var state = context.state();
            state.setBoolean("has", true);
            state.setString("dim", pending.dim());
            state.setInt("x", pending.pos().getX());
            state.setInt("y", pending.pos().getY());
            state.setInt("z", pending.pos().getZ());
            boolean linked = linked(context);
            state.setBoolean("linked", linked);
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.saved",
                    pending.dim(), pending.pos().getX(), pending.pos().getY(), pending.pos().getZ(),
                    Component.translatable(linked ? "gt6ktfruaddon.compute.in" : "gt6ktfruaddon.compute.out")), false);
            return true;
        }
        String dim = context.level().dimension().location().toString();
        Link.remember(player.getUUID(), new Mark(dim, context.pos()));
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.remembered",
                dim, context.pos().getX(), context.pos().getY(), context.pos().getZ()), false);
        return true;
    }

    private boolean linked(MachineBehaviorContext context) {
        var state = context.state();
        if (!state.getBoolean("has") || !(context.level() instanceof ServerLevel server)) return false;
        ResourceLocation id = ResourceLocation.tryParse(state.getString("dim"));
        if (id == null) return false;
        ServerLevel world = server.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, id));
        if (world == null) return false;
        BlockPos target = new BlockPos(state.getInt("x"), state.getInt("y"), state.getInt("z"));
        if (!world.hasChunkAt(target)) return false;
        BlockState there = world.getBlockState(target);
        boolean same = world.dimension().equals(context.level().dimension());
        if (!same) return false;
        return switch (kind) {
            case WIRELESS -> (node(there) || controller(there)) && inside(context.pos(), target);
            case ELECTRIC -> controller(there) && orthogonal(context.pos(), target);
            case WIRED -> controller(there) && (orthogonal(context.pos(), target) || cabled(world, context.pos(), target));
            default -> false;
        };
    }

    private static boolean inside(BlockPos from, BlockPos to) {
        long dx = (long) to.getX() - from.getX();
        long dy = (long) to.getY() - from.getY();
        long dz = (long) to.getZ() - from.getZ();
        return dx * dx + dy * dy + dz * dz < REACH * REACH;
    }

    private static boolean orthogonal(BlockPos from, BlockPos to) {
        int dx = Math.abs(to.getX() - from.getX());
        int dy = Math.abs(to.getY() - from.getY());
        int dz = Math.abs(to.getZ() - from.getZ());
        return dx + dy + dz == 1;
    }

    private static boolean cabled(ServerLevel world, BlockPos origin, BlockPos target) {
        var queue = new ArrayDeque<BlockPos>();
        Set<BlockPos> seen = new HashSet<>();
        for (Direction face : Direction.values()) {
            BlockPos next = origin.relative(face);
            if (cable(world, next)) queue.add(next);
        }
        int guard = 0;
        while (!queue.isEmpty() && guard++ < 256) {
            BlockPos here = queue.removeFirst();
            if (!seen.add(here)) continue;
            for (Direction face : Direction.values()) {
                BlockPos next = here.relative(face);
                if (next.equals(target)) return true;
                if (cable(world, next) && !seen.contains(next)) queue.add(next);
            }
        }
        return false;
    }

    private static boolean cable(Level world, BlockPos pos) {
        if (!world.hasChunkAt(pos)) return false;
        return path(world.getBlockState(pos)).equals("network_cable");
    }

    private static boolean node(BlockState state) {
        String path = path(state);
        return path.equals("compute_cluster") || path.equals("compute_cluster_simple");
    }

    private static boolean controller(BlockState state) {
        String path = path(state);
        return path.equals("electric_controller") || path.equals("wireless_controller") || path.equals("wired_controller");
    }

    private static String path(BlockState state) {
        var id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (id == null || !Gt6KtfruAddon.MODID.equals(id.getNamespace())) return "";
        return id.getPath();
    }

    private record Mark(String dim, BlockPos pos) {}

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
