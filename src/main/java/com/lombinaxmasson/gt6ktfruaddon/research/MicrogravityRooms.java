package com.lombinaxmasson.gt6ktfruaddon.research;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.TtlRegistry;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/** Live room index. The interior is [cx - 3..cx + 3, cy + 1..cy + 7, cz - 3..cz + 3]. */
public final class MicrogravityRooms {
    public static final int SHELL_RADIUS = 4;
    public static final int SHELL_HEIGHT = 8;
    private static final TtlRegistry<Boolean> ACTIVE_ROOMS = new TtlRegistry<>();

    private MicrogravityRooms() {}

    public static void updateRoom(Level level, BlockPos controller, boolean active) {
        if (level == null || controller == null) return;
        DimPos key = new DimPos(level.dimension().location().toString(), controller.immutable());
        if (active) {
            ACTIVE_ROOMS.put(key, true, level.getGameTime());
        } else {
            ACTIVE_ROOMS.remove(key);
        }
    }

    public static boolean hasMicrogravity(Level level, BlockPos pos) {
        if (level == null || pos == null) return false;
        String dim = level.dimension().location().toString();
        long now = level.getGameTime();
        for (DimPos key : ACTIVE_ROOMS.keys(dim, now)) {
            if (insideInterior(key.pos(), pos)) return true;
        }
        return false;
    }

    private static boolean insideInterior(BlockPos c, BlockPos p) {
        return p.getX() >= c.getX() - (SHELL_RADIUS - 1) && p.getX() <= c.getX() + (SHELL_RADIUS - 1)
                && p.getY() >= c.getY() + 1 && p.getY() <= c.getY() + (SHELL_HEIGHT - 1)
                && p.getZ() >= c.getZ() - (SHELL_RADIUS - 1) && p.getZ() <= c.getZ() + (SHELL_RADIUS - 1);
    }

}
