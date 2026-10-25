package com.lombinaxmasson.gt6ktfruaddon.research;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.TtlRegistry;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PlayerMemory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

/** Research monitor index and pairing state. Access is confined to the server thread. */
public final class ResearchNetwork {
    static final String TREE = "minecraft:paper,minecraft:glass,minecraft:iron_ingot";
    private static final Direction[] HORIZONTAL = {
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    private static final TtlRegistry<MonitorEntry> MONITORS = new TtlRegistry<>();
    private static final PlayerMemory<DimPos> PENDING_MONITOR = new PlayerMemory<>(DimPos::dim);

    private ResearchNetwork() {}

    public static DimPos takePendingMonitor(UUID player, String dim) {
        DimPos key = PENDING_MONITOR.get(player);
        if (key != null && dim.equals(key.dim())) {
            PENDING_MONITOR.remove(player);
            return key;
        }
        return null;
    }

    public static BlockPos findAdjacentMonitor(Level level, BlockPos pos) {
        if (level == null || pos == null) return null;
        String dim = level.dimension().location().toString();
        long now = level.getGameTime();
        for (Direction side : HORIZONTAL) {
            BlockPos neighbor = pos.relative(side);
            MonitorEntry entry = MONITORS.get(new DimPos(dim, neighbor), level.getGameTime());
            if (entry != null) {
                return neighbor;
            }
        }
        return null;
    }

    public static boolean isMonitorOnline(Level level, BlockPos monitorPos) {
        if (level == null || monitorPos == null) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos), level.getGameTime());
        return entry != null;
    }

    public static boolean isUnlocked(Level level, BlockPos monitorPos, String itemId) {
        if (level == null || monitorPos == null || itemId == null || itemId.isEmpty()) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos), level.getGameTime());
        if (entry == null) return false;
        return entry.unlocked.contains(itemId);
    }

    public static boolean unlockItem(Level level, BlockPos monitorPos, String itemId) {
        if (level == null || monitorPos == null || itemId == null || itemId.isEmpty()) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos), level.getGameTime());
        if (entry == null) return false;
        entry.unlocked.add(itemId);
        entry.dirty = true;
        return true;
    }

    public static boolean addPoints(Level level, BlockPos monitorPos, long points) {
        if (level == null || monitorPos == null || points <= 0) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos), level.getGameTime());
        if (entry == null) return false;
        entry.points += points;
        entry.dirty = true;
        return true;
    }

    static void update(MachineBehaviorContext context) {
        String raw = context.state().getString("tree");
        if (raw.isEmpty()) {
            raw = TREE;
            context.state().setString("tree", raw);
        }
        String dim = context.level().dimension().location().toString();
        DimPos key = new DimPos(dim, context.pos().immutable());
        long now = context.level().getGameTime();
                MonitorEntry entry = MONITORS.get(key, now);
        if (entry == null) {
            entry = new MonitorEntry(parseSet(raw), context.state().getLong("points"));
            MONITORS.put(key, entry, now);
        } else {
            entry.unlocked.addAll(parseSet(raw));
            MONITORS.put(key, entry, now);
            if (entry.dirty) {
                context.state().setString("tree", String.join(",", entry.unlocked));
                context.state().setLong("points", entry.points);
                entry.dirty = false;
            }
        }

    }

    static void remove(MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
        MONITORS.remove(new DimPos(dim, context.pos().immutable()));
    }

    static void remember(UUID player, MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
        PENDING_MONITOR.put(player, new DimPos(dim, context.pos().immutable()));
    }

    private static Set<String> parseSet(String csv) {
        Set<String> set = new LinkedHashSet<>();
        if (csv == null || csv.isEmpty()) return set;
        Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(set::add);
        return set;
    }

    private static final class MonitorEntry {
        final Set<String> unlocked;
        long points;
        boolean dirty;

        MonitorEntry(Set<String> unlocked, long points) {
            this.unlocked = unlocked;
            this.points = points;
        }
    }
}
