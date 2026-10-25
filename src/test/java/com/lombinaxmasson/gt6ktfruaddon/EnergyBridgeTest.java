package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.EnergyBridge;

/** The move from the auxiliary buffer into the main buffer. */
class EnergyBridgeTest {
    @Test
    void movesAllOfItWhenTheMainBufferHasRoom() {
        FakeMachine machine = new FakeMachine();
        machine.capacity = 1000;
        machine.auxiliary.put("EU", 100L);
        assertEquals(100, EnergyBridge.pull(machine.context, "EU"));
        assertEquals(100, machine.energy);
        assertEquals(0, machine.auxiliary.get("EU"));
    }

    @Test
    void leavesWhatDoesNotFitInTheAuxiliaryBuffer() {
        FakeMachine machine = new FakeMachine();
        machine.capacity = 1000;
        machine.energy = 950;
        machine.auxiliary.put("EU", 100L);
        assertEquals(50, EnergyBridge.pull(machine.context, "EU"));
        assertEquals(1000, machine.energy);
        assertEquals(50, machine.auxiliary.get("EU"));
    }

    @Test
    void takesOnlyWhatTheMainBufferStoredSoNothingIsLost() {
        FakeMachine machine = new FakeMachine();
        machine.capacity = 1000;
        machine.energy = 700;
        machine.auxiliary.put("RU", 1000L);
        long before = machine.energy + machine.auxiliary.get("RU");
        EnergyBridge.pull(machine.context, "RU");
        assertEquals(before, machine.energy + machine.auxiliary.get("RU"));
    }

    @Test
    void doesNothingWhenThereIsNothingToMove() {
        FakeMachine machine = new FakeMachine();
        assertEquals(0, EnergyBridge.pull(machine.context, "EU"));
        assertEquals(List.of(), machine.consumed);
    }

    @Test
    void doesNothingWhenTheMainBufferIsFull() {
        FakeMachine machine = new FakeMachine();
        machine.capacity = 1000;
        machine.energy = 1000;
        machine.auxiliary.put("EU", 100L);
        assertEquals(0, EnergyBridge.pull(machine.context, "EU"));
        assertEquals(100, machine.auxiliary.get("EU"));
        assertEquals(List.of(), machine.consumed);
    }

    @Test
    void movesTheTypeItWasAskedFor() {
        FakeMachine machine = new FakeMachine();
        machine.auxiliary.put("EU", 10L);
        machine.auxiliary.put("RU", 20L);
        assertEquals(20, EnergyBridge.pull(machine.context, "RU"));
        assertEquals(10, machine.auxiliary.get("EU"));
        assertEquals(List.of("RU:20"), machine.consumed);
    }
}
