package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gregtech.gregtech.api.addon.GregTechMaterials.Component;
import com.gregtech.gregtech.api.addon.GregTechMaterials.StackComposition;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.FlywheelStorage;

class FlywheelStorageTest {
    @Test
    void onlyTheRealSingleMaterialPrefixIdentifiesAWheel() {
        var units = List.of(new Component("Steel", 8));
        assertEquals("Steel", FlywheelStorage.materialName(new StackComposition("flywheel", units)));
        assertEquals("", FlywheelStorage.materialName(new StackComposition("flywheel_fake", units)));
        assertEquals("", FlywheelStorage.materialName(new StackComposition("ingot", units)));
        assertEquals("", FlywheelStorage.materialName(StackComposition.EMPTY));
        assertEquals("", FlywheelStorage.materialName(new StackComposition("flywheel", List.of(new Component("Steel", 0)))));
        assertEquals("", FlywheelStorage.materialName(new StackComposition("flywheel", List.of(new Component("Steel", 8), new Component("Wood", 8)))));
    }

    @Test
    void sourceWheelFormulaUsesMassQualityAndSpeed() {
        var wheel = FlywheelStorage.wheel(10, 1, 1);
        assertEquals(4, wheel.rpm());
        assertEquals(5120, wheel.storage());
        float rpm = (float) (Math.pow(0.4, 2.4) * 2 * 4);
        assertEquals(rpm, FlywheelStorage.wheel(10, 0, 2).rpm());
        assertEquals(FlywheelStorage.EMPTY, FlywheelStorage.wheel(0, 1, 1));
        assertEquals(FlywheelStorage.EMPTY, FlywheelStorage.wheel(10, 1, Float.NaN));
    }

    @Test
    void mixedWheelsUseTheSlowestSpeedRatherThanSummingTheirIndividualCapacities() {
        var slow = FlywheelStorage.wheel(10, 1, 1);
        var fast = FlywheelStorage.wheel(20, 1, 2);
        var box = FlywheelStorage.box(List.of(slow, fast), 8, 4);
        assertEquals(4, box.rpm());
        assertEquals(15360, box.capacity());
    }

    @Test
    void machineRatingCapsBoxSpeedAndAnEmptyBoxHasNoStorage() {
        var wheel = FlywheelStorage.wheel(10, 1, 1);
        assertEquals(2560, FlywheelStorage.box(List.of(wheel), 1, 2).capacity());
        assertEquals(0, FlywheelStorage.box(List.of(FlywheelStorage.EMPTY), 8, 4).capacity());
        assertEquals(0, FlywheelStorage.box(List.of(), 8, 4).rpm());
    }

    @Test
    void transformBatteryCountsStackSizeAndSaturatesWithoutWrapping() {
        var wheel = FlywheelStorage.wheel(10, 1, 1);
        assertEquals(15360, FlywheelStorage.addWheels(0, wheel, 3));
        assertEquals(Long.MAX_VALUE, FlywheelStorage.addWheels(Long.MAX_VALUE - 1, wheel, 1));
        assertEquals(Long.MAX_VALUE, FlywheelStorage.addWheels(0, new FlywheelStorage.Wheel(1, Long.MAX_VALUE), 2));
        assertEquals(0, FlywheelStorage.addWheels(0, wheel, 0));
    }

    @Test
    void shrinkingAndEmptyingRemoveExcessEnergyWhileGrowingPreservesIt() {
        var machine = new FakeMachine();
        machine.energy = 1000;
        FlywheelStorage.resize(machine.context, 500);
        assertEquals(500, machine.energy);
        assertEquals(500, machine.capacity);
        FlywheelStorage.resize(machine.context, 2000);
        assertEquals(500, machine.energy);
        FlywheelStorage.resize(machine.context, 0);
        assertEquals(0, machine.energy);
        assertEquals(0, machine.capacity);
        assertThrows(IllegalArgumentException.class, () -> FlywheelStorage.resize(machine.context, -1));
    }
}
