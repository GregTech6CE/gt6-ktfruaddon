package com.lombinaxmasson.gt6ktfruaddon.research;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/** Live room index. The interior is [cx - 3..cx + 3, cy + 1..cy + 7, cz - 3..cz + 3]. */
public final class MicrogravityRooms {
    public static final int SHELL_RADIUS = 4;
    public static final int SHELL_HEIGHT = 8;
    private static final long EXPIRY_TICKS = 20L;
    private static final Map<Key, Long> ACTIVE_ROOMS = new HashMap<>();

    private MicrogravityRooms() {}

    public static synchronized void updateRoom(Level level, BlockPos controller, boolean active) {
        if (level == null || controller == null) return;
        Key key = new Key(level.dimension().location().toString(), controller.immutable());
        if (active) {
            ACTIVE_ROOMS.put(key, level.getGameTime());
        } else {
            ACTIVE_ROOMS.remove(key);
        }
    }

    public static synchronized boolean hasMicrogravity(Level level, BlockPos pos) {
        if (level == null || pos == null) return false;
        String dim = level.dimension().location().toString();
        long now = level.getGameTime();
        Iterator<Map.Entry<Key, Long>> it = ACTIVE_ROOMS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Key, Long> entry = it.next();
            if (now - entry.getValue() > EXPIRY_TICKS) {
                it.remove();
                continue;
            }
            if (!dim.equals(entry.getKey().dim())) continue;
            if (insideInterior(entry.getKey().pos(), pos)) return true;
        }
        return false;
    }

    private static boolean insideInterior(BlockPos c, BlockPos p) {
        return p.getX() >= c.getX() - (SHELL_RADIUS - 1) && p.getX() <= c.getX() + (SHELL_RADIUS - 1)
                && p.getY() >= c.getY() + 1 && p.getY() <= c.getY() + (SHELL_HEIGHT - 1)
                && p.getZ() >= c.getZ() - (SHELL_RADIUS - 1) && p.getZ() <= c.getZ() + (SHELL_RADIUS - 1);
    }

    private record Key(String dim, BlockPos pos) {}
}
