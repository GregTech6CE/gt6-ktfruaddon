package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.Tiers;

class TiersTest {
    @Test
    void ladderIncludesBothBoundaries() {
        assertEquals(0, Tiers.index("Miner", 1, 4));
        assertEquals(3, Tiers.index("Miner", 4, 4));
        assertEquals(5, Tiers.index("Flywheel", 6, 6));
    }

    @Test
    void invalidTiersCannotSilentlyFallBack() {
        for (int tier : new int[] {Integer.MIN_VALUE, -1, 0, 5, Integer.MAX_VALUE})
            assertThrows(IllegalArgumentException.class, () -> Tiers.index("Miner", tier, 4));
        assertThrows(IllegalArgumentException.class, () -> Tiers.index("Flywheel", 7, 6));
        assertThrows(IllegalArgumentException.class, () -> Tiers.index("empty", 1, 0));
    }
}
