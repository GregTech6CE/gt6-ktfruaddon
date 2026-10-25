package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeTiming;

class RecipeTimingTest {
    @Test
    void durationAndSignedPowerRemainDistinct() {
        var processing = RecipeTiming.ticks(600).euPerTick(32);
        assertEquals(600, processing.duration());
        assertEquals(32, processing.euPerTick());
        var fuel = RecipeTiming.ticks(9).euPerTick(-384);
        assertEquals(9, fuel.duration());
        assertEquals(-384, fuel.euPerTick());
        assertEquals(0, RecipeTiming.ticks(1).euPerTick(0).euPerTick());
    }

    @Test
    void zeroAndNegativeDurationAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> RecipeTiming.ticks(0));
        assertThrows(IllegalArgumentException.class, () -> RecipeTiming.ticks(-1));
    }
}
