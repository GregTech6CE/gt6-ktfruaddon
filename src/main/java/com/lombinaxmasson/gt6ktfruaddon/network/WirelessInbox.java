package com.lombinaxmasson.gt6ktfruaddon.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.BlockPos;

/** Wireless pairing memory and cross-dimension receiver inbox. Server thread only. */
final class WirelessInbox {
    private WirelessInbox() {}

    record Mark(String dim, BlockPos pos, boolean quantum) {
        boolean same(Mark other) {
            return quantum == other.quantum && dim.equals(other.dim) && pos.equals(other.pos);
        }
    }

    private static final Map<UUID, Mark> PENDING = new HashMap<>();

    static void remember(UUID player, Mark mark) {
        PENDING.put(player, mark);
    }

    static Mark take(UUID player) {
        return PENDING.remove(player);
    }

    record Key(String dim, int x, int y, int z) {}

    private static final Map<Key, Long> ROOM = new HashMap<>();
    private static final Map<Key, Long> WAITING = new HashMap<>();

    static void setRoom(Key key, long room) {
        ROOM.put(key, room);
    }

    static long room(Key key) {
        return ROOM.getOrDefault(key, 0L);
    }

    static void give(Key key, long amount) {
        WAITING.merge(key, amount, Long::sum);
    }

    static long pull(Key key) {
        Long waiting = WAITING.remove(key);
        return waiting == null ? 0 : waiting;
    }

}
