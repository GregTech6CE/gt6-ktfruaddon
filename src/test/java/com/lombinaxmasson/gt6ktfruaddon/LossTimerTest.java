package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.LossTimer;

/** When stored energy is lost, and how much. */
class LossTimerTest {
    @Test
    void happensOnTickFiveOfEveryTwoHundred() {
        for (long tick : List.of(5L, 205L, 405L, 1_000_005L)) assertTrue(LossTimer.due(tick), "tick " + tick);
        for (long tick : List.of(0L, 4L, 6L, 100L, 204L, 206L)) assertFalse(LossTimer.due(tick), "tick " + tick);
    }

    @Test
    void theShareIsWhatIsLeftAfterKeepingTheRoundedDownRest() {
        assertEquals(500, LossTimer.share(1000, 50));
        assertEquals(501, LossTimer.share(1001, 50));
        assertEquals(0, LossTimer.share(1000, 0));
        assertEquals(1000, LossTimer.share(1000, 100));
        assertEquals(0, LossTimer.share(0, 10));
    }

    @Test
    void aSmallBufferLosesOneUnitEachTime() {
        assertEquals(1, LossTimer.share(1, 0.1));
        assertEquals(1, LossTimer.share(10, 0.1));
    }

    @Test
    void takesTheShareOutOfTheMachineOnTheTickForIt() {
        FakeMachine machine = new FakeMachine();
        machine.energy = 1000;
        assertEquals(500, LossTimer.lose(machine.context, 5, 50));
        assertEquals(500, machine.energy);
        assertEquals(List.of("main:500"), machine.consumed);
    }

    @Test
    void doesNothingOnTheOtherTicks() {
        FakeMachine machine = new FakeMachine();
        machine.energy = 1000;
        for (long tick = 0; tick < 200; tick++) {
            if (tick != 5) assertEquals(0, LossTimer.lose(machine.context, tick, 50), "tick " + tick);
        }
        assertEquals(1000, machine.energy);
        assertEquals(List.of(), machine.consumed);
    }

    @Test
    void losesAgainOnTheNextPeriod() {
        FakeMachine machine = new FakeMachine();
        machine.energy = 1000;
        LossTimer.lose(machine.context, 5, 50);
        LossTimer.lose(machine.context, 205, 50);
        assertEquals(250, machine.energy);
    }

    @Test
    void anEmptyBufferLosesNothingAndConsumesNothing() {
        FakeMachine machine = new FakeMachine();
        assertEquals(0, LossTimer.lose(machine.context, 5, 50));
        assertEquals(List.of(), machine.consumed);
    }

    @Test
    void writesNoStateSoNoCounterHasToBeKept() {
        FakeMachine machine = new FakeMachine();
        machine.energy = 1000;
        for (long tick = 0; tick < 400; tick++) LossTimer.lose(machine.context, tick, 10);
        assertEquals(List.of(), List.copyOf(machine.longs.keySet()));
    }
}
