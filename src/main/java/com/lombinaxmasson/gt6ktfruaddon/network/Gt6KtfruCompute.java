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
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PosCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Addon-side compute cluster topology.
 * Compute nodes register their live output at {@code (dim, pos)}.
 * Wireless controllers sum bound wireless nodes within 512 blocks of the same dimension.
 * Biology, quantum and spacetime arrays can bind an upstream controller and expose their converted compute.
 * AI centers and downstream arrays allocate compute per server tick from their bound controller.
 */
public final class Gt6KtfruCompute implements MachineBehavior {
    static final long REACH = 512L;
    private static final long EXPIRY_TICKS = 20L;

    private static final Map<DimPos, NodeEntry> NODES = new HashMap<>();
    private static final Map<DimPos, ControllerEntry> CONTROLLERS = new HashMap<>();
    private static final Map<UUID, Mark> PENDING_NODE = new HashMap<>();
    private static final Map<UUID, DimPos> PENDING_CONTROLLER = new HashMap<>();

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

    public static synchronized DimPos takePendingController(UUID player, String dim) {
        DimPos key = PENDING_CONTROLLER.get(player);
        if (key != null && dim.equals(key.dim())) {
            PENDING_CONTROLLER.remove(player);
            return key;
        }
        return null;
    }

    public static synchronized boolean isControllerOnline(Level level, BlockPos pos) {
        if (level == null || pos == null) return false;
        String dim = level.dimension().location().toString();
        ControllerEntry entry = CONTROLLERS.get(new DimPos(dim, pos));
        return entry != null && entry.formed && level.getGameTime() - entry.lastTick <= EXPIRY_TICKS;
    }

    public static synchronized boolean allocate(Level level, BlockPos controllerPos, String neededUnit, long amount) {
        if (level == null || controllerPos == null || amount <= 0) return false;
        String dim = level.dimension().location().toString();
        long now = level.getGameTime();
        ControllerEntry entry = CONTROLLERS.get(new DimPos(dim, controllerPos));
        if (entry == null || !entry.formed || now - entry.lastTick > EXPIRY_TICKS) return false;
        if (!entry.unit.equalsIgnoreCase(neededUnit)) return false;
        if (entry.allocatedTick != now) {
            entry.allocatedTick = now;
            entry.allocated = 0L;
        }
        if (entry.available - entry.allocated < amount) return false;
        entry.allocated += amount;
        return true;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
        BlockPos here = context.pos().immutable();
        DimPos self = new DimPos(dim, here);
        long now = context.level().getGameTime();

        if (kind == Kind.NODE) {
            synchronized (Gt6KtfruCompute.class) {
                NODES.put(self, new NodeEntry(provided, unit, now));
            }
            context.working(true);
            return;
        }
        if (kind == Kind.WIRELESS) {
            boolean formed = context.structureFormed();
            long total = 0L;
            if (formed) {
                List<BlockPos> linked = links(context);
                synchronized (Gt6KtfruCompute.class) {
                    for (BlockPos nodePos : linked) {
                        if (!reachable(here, nodePos)) continue;
                        NodeEntry node = NODES.get(new DimPos(dim, nodePos));
                        if (node != null && now - node.lastTick <= EXPIRY_TICKS && "Normal".equals(node.unit)) {
                            total += node.power;
                        }
                    }
                    updateController(self, formed, "Normal", total, now);
                }
            } else {
                synchronized (Gt6KtfruCompute.class) {
                    updateController(self, false, "Normal", 0L, now);
                }
            }
            context.state().setLong("available", total);
            context.working(formed && total > 0);
            return;
        }
        if (kind == Kind.ARRAY) {
            boolean formed = context.structureFormed();
            boolean active = false;
            if (formed) {
                if (required <= 0L) {
                    active = true;
                } else {
                    BlockPos upstream = PosCodec.parse(context.state().getString("controller"));
                    if (upstream != null && allocate(context.level(), upstream, "Normal", required)) {
                        active = true;
                    }
                }
            }
            long out = active ? provided : 0L;
            synchronized (Gt6KtfruCompute.class) {
                updateController(self, formed, unit, out, now);
            }
            context.working(active);
            return;
        }
        context.working(false);
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
        DimPos self = new DimPos(dim, context.pos().immutable());
        synchronized (Gt6KtfruCompute.class) {
            NODES.remove(self);
            CONTROLLERS.remove(self);
        }
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
        BlockPos here = context.pos().immutable();
        if (kind == Kind.NODE) {
            synchronized (Gt6KtfruCompute.class) {
                PENDING_NODE.put(player.getUUID(), new Mark(dim, here, unit));
            }
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.node",
                    provided, unit), false);
            return true;
        }
        if (kind == Kind.WIRELESS) {
            Mark pending;
            synchronized (Gt6KtfruCompute.class) {
                pending = PENDING_NODE.remove(player.getUUID());
                PENDING_CONTROLLER.put(player.getUUID(), new DimPos(dim, here));
            }
            if (pending != null && dim.equals(pending.dim()) && reachable(here, pending.pos())
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
            synchronized (Gt6KtfruCompute.class) {
                DimPos upstream = PENDING_CONTROLLER.get(player.getUUID());
                if (upstream != null && dim.equals(upstream.dim()) && !upstream.pos().equals(here)) {
                    PENDING_CONTROLLER.remove(player.getUUID());
                    context.state().setString("controller", PosCodec.format(upstream.pos()));
                } else {
                    PENDING_CONTROLLER.put(player.getUUID(), new DimPos(dim, here));
                }
            }
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.array",
                    context.structureFormed() ? 1 : 0, required, provided, unit), false);
            return true;
        }
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.compute.plain", provided), false);
        return true;
    }

    private static void updateController(DimPos key, boolean formed, String unit, long available, long now) {
        ControllerEntry entry = CONTROLLERS.get(key);
        if (entry == null) {
            entry = new ControllerEntry();
            CONTROLLERS.put(key, entry);
        }
        entry.formed = formed;
        entry.unit = unit;
        entry.available = available;
        entry.lastTick = now;
    }

    private static boolean reachable(BlockPos from, BlockPos to) {
        return from.distSqr(to) <= REACH * REACH;
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

    private record Mark(String dim, BlockPos pos, String unit) {}
    private record NodeEntry(long power, String unit, long lastTick) {}

    private static final class ControllerEntry {
        boolean formed;
        String unit = "Normal";
        long available;
        long allocated;
        long allocatedTick = -1L;
        long lastTick;
    }
}
