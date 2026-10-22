package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/**
 * Card 17. This checkout has {@code WirelessBatteryBase} and one receiver class.
 * Neither is registered in {@code tileEntityInit0}. The quantum tower is absent.
 */
public final class Gt6KtfruWirelessMachines {
    static final String BATTERY = "wireless_battery";
    static final String RECEIVER = "wireless_receiver";
    private static final String GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";

    private Gt6KtfruWirelessMachines() {}

    public static void add(List<AddonMachine> machines) {
        machines.add(battery());
        machines.add(receiver());
    }

    public static void addLayouts(List<StructureLayout> layouts) {
        layouts.add(Gt6KtfruWirelessLayouts.battery(BATTERY));
    }

    public static Set<String> behaviorTypes() {
        return Set.of(BATTERY, RECEIVER);
    }

    public static boolean owns(String machineType) {
        return behaviorTypes().contains(machineType);
    }

    public static MachineBehavior create(String machineType) {
        return new Gt6KtfruWireless(BATTERY.equals(machineType));
    }

    /** Nominal 4096 sits in the registered 2048–8192 window. */
    private static BehaviorMachine battery() {
        return BehaviorMachine.builder(MachineCasing.builder(BATTERY, "Tungstensteel").machineType(BATTERY).build())
                .energy(EnergyInput.builder("EU", 4096).faces(MachineFace.BOTTOM).capacity(Gt6KtfruWireless.CAPACITY).build())
                .gui(GUI)
                .toggle("gt6ktfruaddon.wireless_battery.sealed", 152, 64, true)
                .build();
    }

    /** Bottom 1 EU is the required input. The front emits the 8192 EU packet. */
    private static BehaviorMachine receiver() {
        return BehaviorMachine.builder(MachineCasing.builder(RECEIVER, "Tungstensteel").machineType(RECEIVER).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(Gt6KtfruWireless.VOLTAGE * Gt6KtfruWireless.AMPERE).build())
                .energyOutput(EnergyOutput.builder("EU", Gt6KtfruWireless.VOLTAGE, Gt6KtfruWireless.AMPERE).faces(MachineFace.FRONT).build())
                .gui(GUI)
                .toggle("gt6ktfruaddon.wireless_receiver.stopped", 152, 64, false)
                .build();
    }
}
