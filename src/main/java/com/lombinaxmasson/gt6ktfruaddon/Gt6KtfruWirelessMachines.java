package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/** Inductive tower 30114, quantum hub 30115, and receivers 31117 and 31118. */
public final class Gt6KtfruWirelessMachines {
    static final String INDUCTIVE = "wireless_battery_inductive";
    static final String QUANTUM = "wireless_battery_quantum";
    static final String INDUCTIVE_RECEIVER = "wireless_receiver_inductive";
    static final String QUANTUM_RECEIVER = "wireless_receiver_quantum";
    private static final String GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";

    private Gt6KtfruWirelessMachines() {}

    public static void add(List<AddonMachine> machines) {
        machines.add(inductiveTower());
        machines.add(quantumTower());
        machines.add(receiver(INDUCTIVE_RECEIVER, "Tungstensteel", 8192, 4));
        machines.add(receiver(QUANTUM_RECEIVER, "Naquadria", 32768, 16));
    }

    public static Set<String> behaviorTypes() {
        return Set.of(INDUCTIVE, QUANTUM, INDUCTIVE_RECEIVER, QUANTUM_RECEIVER);
    }

    public static boolean owns(String machineType) {
        return behaviorTypes().contains(machineType);
    }

    public static MachineBehavior create(String machineType) {
        return switch (machineType) {
            case INDUCTIVE -> new Gt6KtfruWireless(Gt6KtfruWireless.Kind.INDUCTIVE);
            case QUANTUM -> new Gt6KtfruWireless(Gt6KtfruWireless.Kind.QUANTUM);
            case QUANTUM_RECEIVER -> new Gt6KtfruWireless(Gt6KtfruWireless.Kind.QUANTUM_RECEIVER);
            default -> new Gt6KtfruWireless(Gt6KtfruWireless.Kind.INDUCTIVE_RECEIVER);
        };
    }

    /** Nominal 4096 accepts 2048–8192 on the main input. */
    private static BehaviorMachine inductiveTower() {
        return BehaviorMachine.builder(MachineCasing.builder(INDUCTIVE, "Tungstensteel").machineType(INDUCTIVE).build())
                .energy(EnergyInput.builder("EU", 4096).faces(MachineFace.BOTTOM).capacity(Gt6KtfruWireless.INDUCTIVE_CAPACITY).build())
                .gui(GUI)
                .toggle("gt6ktfruaddon.wireless_battery.sealed", 152, 64, true)
                .build();
    }

    /** Quantum hub accepts 1–32768 EU via auxiliary energy and 8192–32768 EU on the bottom. */
    private static BehaviorMachine quantumTower() {
        return BehaviorMachine.builder(MachineCasing.builder(QUANTUM, "Naquadria").machineType(QUANTUM).build())
                .energy(EnergyInput.builder("EU", 16384).faces(MachineFace.BOTTOM).capacity(Gt6KtfruWireless.QUANTUM_CAPACITY).build())
                .auxiliaryEnergy(EnergyInput.builder("EU", 16384)
                        .range(1, 32768)
                        .faces(MachineFace.BACK, MachineFace.LEFT, MachineFace.RIGHT, MachineFace.TOP)
                        .capacity(Gt6KtfruWireless.QUANTUM_CAPACITY)
                        .build())
                .gui(GUI)
                .toggle("gt6ktfruaddon.wireless_battery.sealed", 152, 64, true)
                .build();
    }

    private static BehaviorMachine receiver(String id, String material, long voltage, long ampere) {
        return BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(voltage * ampere).build())
                .energyOutput(EnergyOutput.builder("EU", voltage, ampere).faces(MachineFace.FRONT).build())
                .gui(GUI)
                .toggle("gt6ktfruaddon.wireless_receiver.stopped", 152, 64, false)
                .build();
    }
}
