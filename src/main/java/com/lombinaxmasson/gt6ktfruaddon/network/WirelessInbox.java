package com.lombinaxmasson.gt6ktfruaddon.network;

import java.util.UUID;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimensionMemory;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PlayerMemory;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.TtlRegistry;

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


    private static final class Credit {
        long remaining;
        Credit(long remaining) { this.remaining = remaining; }
    }
    private static final TtlRegistry<Credit> ROOM = new TtlRegistry<>();
    private static final DimensionMemory<Long> WAITING = new DimensionMemory<>();

    static void setRoom(DimPos key, long room, long tick) {
        ROOM.put(key, new Credit(Math.max(0L, room - waiting(key))), tick);
    }

    /** Reserve before paying, and return the reservation when payment fails. */
    static long send(DimPos key, long packet, long maxAmps, long tick, LongPredicate pay) {
        if (packet <= 0 || maxAmps <= 0) return 0;
        Credit credit = ROOM.get(key, tick);
        if (credit == null) return 0;
        long amps = Math.min(maxAmps, credit.remaining / packet);
        if (amps <= 0) return 0;
        long amount = amps * packet;
        credit.remaining -= amount;
        if (!pay.test(amps)) { credit.remaining += amount; return 0; }
        WAITING.put(key, waiting(key) + amount);
        return amps;
    }

    static long waiting(DimPos key) {
        Long amount = WAITING.get(key);
        return amount == null ? 0L : amount;
    }

    /** Retain any energy the current buffer cannot store. */
    static long receive(DimPos key, LongUnaryOperator store) {
        long queued = waiting(key);
        if (queued == 0) return 0;
        long accepted = Math.max(0, Math.min(queued, store.applyAsLong(queued)));
        if (accepted == queued) WAITING.remove(key);
        else WAITING.put(key, queued - accepted);
        return accepted;
    }

    static void remove(DimPos key) {
        ROOM.remove(key);
        WAITING.remove(key);
    }
}
