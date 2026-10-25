package com.lombinaxmasson.gt6ktfruaddon.network;

import java.util.UUID;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PlayerMemory;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimensionMemory;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;

import net.minecraft.core.BlockPos;

/** Wireless pairing memory and cross-dimension receiver inbox. Server thread only. */
final class WirelessInbox {
    private WirelessInbox() {}

    record Mark(String dim, BlockPos pos, boolean quantum) {
        boolean same(Mark other) {
            return quantum == other.quantum && dim.equals(other.dim) && pos.equals(other.pos);
        }
    }

    private static final PlayerMemory<Mark> PENDING = new PlayerMemory<>(Mark::dim);

    static void remember(UUID player, Mark mark) {
        PENDING.put(player, mark);
    }

    static Mark take(UUID player) {
        return PENDING.remove(player);
    }


    private static final DimensionMemory<Long> ROOM = new DimensionMemory<>();
    private static final DimensionMemory<Long> WAITING = new DimensionMemory<>();

    static void setRoom(DimPos key, long room) {
        ROOM.put(key, room);
    }

    static long room(DimPos key) {
        Long room = ROOM.get(key);
        return room == null ? 0L : room;
    }

    static void give(DimPos key, long amount) {
        Long waiting = WAITING.get(key);
        WAITING.put(key, (waiting == null ? 0L : waiting) + amount);
    }

    static long pull(DimPos key) {
        Long waiting = WAITING.remove(key);
        return waiting == null ? 0 : waiting;
    }

}
