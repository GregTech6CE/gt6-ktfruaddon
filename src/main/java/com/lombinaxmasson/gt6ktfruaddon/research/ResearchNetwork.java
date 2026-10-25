package com.lombinaxmasson.gt6ktfruaddon.research;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

/** Research monitor index and pairing state. Access is confined to the server thread. */
public final class ResearchNetwork {
    static final String TREE = "minecraft:paper,minecraft:glass,minecraft:iron_ingot";
    private static final long EXPIRY_TICKS = 20L;
    private static final Direction[] HORIZONTAL = {
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    private static final Map<DimPos, MonitorEntry> MONITORS = new HashMap<>();
    private static final Map<UUID, DimPos> PENDING_MONITOR = new HashMap<>();

    private ResearchNetwork() {}

    public static synchronized DimPos takePendingMonitor(UUID player, String dim) {
        DimPos key = PENDING_MONITOR.get(player);
        if (key != null && dim.equals(key.dim())) {
            PENDING_MONITOR.remove(player);
            return key;
        }
        return null;
    }

    public static synchronized BlockPos findAdjacentMonitor(Level level, BlockPos pos) {
        if (level == null || pos == null) return null;
        String dim = level.dimension().location().toString();
        long now = level.getGameTime();
        for (Direction side : HORIZONTAL) {
            BlockPos neighbor = pos.relative(side);
            MonitorEntry entry = MONITORS.get(new DimPos(dim, neighbor));
            if (entry != null && now - entry.lastTick <= EXPIRY_TICKS) {
                return neighbor;
            }
        }
        return null;
    }

    public static synchronized boolean isMonitorOnline(Level level, BlockPos monitorPos) {
        if (level == null || monitorPos == null) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos));
        return entry != null && level.getGameTime() - entry.lastTick <= EXPIRY_TICKS;
    }

    public static synchronized boolean isUnlocked(Level level, BlockPos monitorPos, String itemId) {
        if (level == null || monitorPos == null || itemId == null || itemId.isEmpty()) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos));
        if (entry == null || level.getGameTime() - entry.lastTick > EXPIRY_TICKS) return false;
        return entry.unlocked.contains(itemId);
    }

    public static synchronized boolean unlockItem(Level level, BlockPos monitorPos, String itemId) {
        if (level == null || monitorPos == null || itemId == null || itemId.isEmpty()) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos));
        if (entry == null || level.getGameTime() - entry.lastTick > EXPIRY_TICKS) return false;
        entry.unlocked.add(itemId);
        entry.dirty = true;
        return true;
    }

    public static synchronized boolean addPoints(Level level, BlockPos monitorPos, long points) {
        if (level == null || monitorPos == null || points <= 0) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos));
        if (entry == null || level.getGameTime() - entry.lastTick > EXPIRY_TICKS) return false;
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
        synchronized (ResearchNetwork.class) {
            MonitorEntry entry = MONITORS.get(key);
            if (entry == null) {
                entry = new MonitorEntry(parseSet(raw), context.state().getLong("points"), now);
                MONITORS.put(key, entry);
            } else {
                entry.unlocked.addAll(parseSet(raw));
                entry.lastTick = now;
                if (entry.dirty) {
                    context.state().setString("tree", String.join(",", entry.unlocked));
                    context.state().setLong("points", entry.points);
                    entry.dirty = false;
                }
            }
        }
    }

    static synchronized void remove(MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
        MONITORS.remove(new DimPos(dim, context.pos().immutable()));
    }

    static synchronized void remember(UUID player, MachineBehaviorContext context) {
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
        long lastTick;
        boolean dirty;

        MonitorEntry(Set<String> unlocked, long points, long lastTick) {
            this.unlocked = unlocked;
            this.points = points;
            this.lastTick = lastTick;
        }
    }
}
