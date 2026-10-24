package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import net.minecraft.core.BlockPos;

/** The {@code x,y,z} text in which a machine keeps a linked position. */
public final class PosCodec {
    private PosCodec() {}

    public static String format(BlockPos pos) {
        return pos.getX() + "," + pos.getY() + "," + pos.getZ();
    }

    public static BlockPos parse(String raw) {
        if (raw == null || raw.isEmpty()) return null;
        String[] parts = raw.split(",");
        if (parts.length != 3) return null;
        try {
            return new BlockPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
