package com.lombinaxmasson.gt6ktfruaddon.network;

import java.util.List;
import java.util.UUID;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PosCodec;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.TtlRegistry;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PlayerMemory;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.StateChanges;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/** Live compute topology and pairing state. Access is confined to the server thread. */
public final class ComputeNetwork {
    static final long REACH = 512L;

    private static final TtlRegistry<NodeEntry> NODES = new TtlRegistry<>();
    private static final TtlRegistry<ControllerEntry> CONTROLLERS = new TtlRegistry<>();
    private static final PlayerMemory<Mark> PENDING_NODE = new PlayerMemory<>(Mark::dim);
    private static final PlayerMemory<DimPos> PENDING_CONTROLLER = new PlayerMemory<>(DimPos::dim);

    private ComputeNetwork() {}

    public static DimPos takePendingController(UUID player, String dim) {
        DimPos key = PENDING_CONTROLLER.get(player);
        if (key != null && dim.equals(key.dim())) {
            PENDING_CONTROLLER.remove(player);
            return key;
        }
        return null;
    }

    public static boolean isControllerOnline(Level level, BlockPos pos) {
        if (level == null || pos == null) return false;
        String dim = level.dimension().location().toString();
        ControllerEntry entry = CONTROLLERS.get(new DimPos(dim, pos), level.getGameTime());
        return entry != null && entry.formed;
    }

    public static boolean allocate(Level level, BlockPos controllerPos, String neededUnit, long amount) {
        if (level == null || controllerPos == null || amount <= 0) return false;
        String dim = level.dimension().location().toString();
        long now = level.getGameTime();
        ControllerEntry entry = CONTROLLERS.get(new DimPos(dim, controllerPos), now);
        if (entry == null || !entry.formed) return false;
        if (!entry.unit.equalsIgnoreCase(neededUnit)) return false;
        if (entry.allocatedTick != now) {
            entry.allocatedTick = now;
            entry.allocated = 0L;
        }
        if (entry.available - entry.allocated < amount) return false;
        entry.allocated += amount;
        return true;
    }

    private static void updateController(DimPos key, boolean formed, String unit, long available, long now) {
        ControllerEntry entry = CONTROLLERS.get(key, now);
        if (entry == null) {
            entry = new ControllerEntry();
        }
        entry.formed = formed;
        entry.unit = unit;
        entry.available = available;
        CONTROLLERS.put(key, entry, now);
    }

    static void registerNode(MachineBehaviorContext context, long power, String unit) {
        NODES.put(key(context), new NodeEntry(power, unit), context.level().getGameTime());
    }

    static void publish(MachineBehaviorContext context, boolean formed, String unit, long output) {
        updateController(key(context), formed, unit, output, context.level().getGameTime());
    }

    static long wirelessOutput(MachineBehaviorContext context, List<BlockPos> linked) {
        String dim = context.level().dimension().location().toString();
        long now = context.level().getGameTime();
        long total = 0L;
        if (context.structureFormed()) {
            for (BlockPos nodePos : linked) {
                if (!reachable(context.pos(), nodePos)) continue;
                NodeEntry node = NODES.get(new DimPos(dim, nodePos), now);
                if (node != null && "Normal".equals(node.unit)) {
                    total += node.power;
                }
            }
        }
        updateController(key(context), context.structureFormed(), "Normal", total, now);
        return total;
    }

    static void remove(MachineBehaviorContext context) {
        DimPos self = key(context);
        NODES.remove(self);
        CONTROLLERS.remove(self);
    }

    static void rememberNode(UUID player, MachineBehaviorContext context, String unit) {
        DimPos self = key(context);
        PENDING_NODE.put(player, new Mark(self.dim(), self.pos(), unit));
    }

    static Mark selectWireless(UUID player, MachineBehaviorContext context) {
        Mark pending = PENDING_NODE.remove(player);
        PENDING_CONTROLLER.put(player, key(context));
        return pending;
    }

    static void selectArray(UUID player, MachineBehaviorContext context) {
        DimPos self = key(context);
        DimPos upstream = PENDING_CONTROLLER.get(player);
        if (upstream != null && self.dim().equals(upstream.dim()) && !upstream.pos().equals(self.pos())) {
            PENDING_CONTROLLER.remove(player);
            StateChanges.setString(context.state(), "controller", PosCodec.format(upstream.pos()));
        } else {
            PENDING_CONTROLLER.put(player, self);
        }
    }

    private static DimPos key(MachineBehaviorContext context) {
        return new DimPos(context.level().dimension().location().toString(), context.pos().immutable());
    }

    static boolean reachable(BlockPos from, BlockPos to) {
        return from.distSqr(to) <= REACH * REACH;
    }

    record Mark(String dim, BlockPos pos, String unit) {}
    private record NodeEntry(long power, String unit) {}

    private static final class ControllerEntry {
        boolean formed;
        String unit = "Normal";
        long available;
        long allocated;
        long allocatedTick = -1L;
    }
}
