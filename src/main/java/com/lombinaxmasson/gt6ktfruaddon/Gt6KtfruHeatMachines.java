package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * Card 12 controllers. Each layout name is the machine type.
 * A behavior machine still needs one energy input, so a face that the structure
 * leaves open carries it when the source itself has no input.
 */
public final class Gt6KtfruHeatMachines {
    private static final String GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";

    private Gt6KtfruHeatMachines() {}

    public static void add(List<AddonMachine> machines) {
        machines.add(sunHeater());
        machines.add(sunMirror());
        machines.add(tidal());
        machines.add(oilMiner());
        machines.add(mantle());
        machines.add(fuelDeburn());
    }

    public static void addParts(List<LayoutPart> parts) {
        parts.add(casing("heat_pipe", "Silver", 6.0F));
        parts.add(casing("heat_pipe_top", "Silver", 6.0F));
        parts.add(casing("tidal_blade", "StainlessSteel", 6.0F));
        parts.add(casing("large_sun_mirror_frame", "StainlessSteel", 10.0F));
        parts.add(casing("large_sun_mirror", "Silver", 10.0F));
        parts.add(casing("oil_miner_drill", "StainlessSteel", 6.0F));
        parts.add(casing("mantle_drill_rod", "Tungsten", 6.0F));
        parts.add(casing("enhanced_frame", "VanadiumSteel", 16.0F));
        parts.add(casing("chemical_reactor_pipe", "StainlessSteel", 1.0F));
    }

    public static boolean owns(String machineType) {
        return Gt6KtfruMachines.SUN_HEATER.equals(machineType)
                || Gt6KtfruMachines.SUN_MIRROR.equals(machineType)
                || Gt6KtfruMachines.TIDAL.equals(machineType)
                || Gt6KtfruMachines.OIL_MINER.equals(machineType)
                || Gt6KtfruMachines.MANTLE.equals(machineType)
                || Gt6KtfruMachines.FUEL_DEBURN_TYPE.equals(machineType);
    }

    public static MachineBehavior create(String machineType) {
        if (Gt6KtfruMachines.SUN_HEATER.equals(machineType)) return new Gt6KtfruSunHeater();
        if (Gt6KtfruMachines.SUN_MIRROR.equals(machineType)) return new Gt6KtfruSunMirror();
        if (Gt6KtfruMachines.TIDAL.equals(machineType)) return new Gt6KtfruTidalGenerator();
        if (Gt6KtfruMachines.OIL_MINER.equals(machineType)) return new Gt6KtfruOilMiner();
        if (Gt6KtfruMachines.MANTLE.equals(machineType)) return new Gt6KtfruMantleHeater();
        if (Gt6KtfruMachines.FUEL_DEBURN_TYPE.equals(machineType)) return new Gt6KtfruFuelDeburner();
        throw new IllegalArgumentException(machineType);
    }

    /**
     * One absorb layer. The front is the open face; the block under the controller is a wall.
     * Nominal 640 HU accepts 320 to 1280.
     */
    private static BehaviorMachine sunHeater() {
        return BehaviorMachine.builder(MachineCasing.builder(Gt6KtfruMachines.SUN_HEATER, "StainlessSteel")
                        .machineType(Gt6KtfruMachines.SUN_HEATER).tier(3).build())
                .energy(EnergyInput.builder("HU", 640).faces(MachineFace.FRONT)
                        .capacity(Gt6KtfruSunHeater.capacity()).build())
                .gui(GUI)
                .build();
    }

    /** No energy in the source. The bottom is the only open face. */
    private static BehaviorMachine sunMirror() {
        return BehaviorMachine.builder(MachineCasing.builder(Gt6KtfruMachines.SUN_MIRROR, "StainlessSteel")
                        .machineType(Gt6KtfruMachines.SUN_MIRROR).tier(3).build())
                .energy(EnergyInput.builder("HU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(GUI)
                .build();
    }

    /**
     * RU leaves the top. The back is the required input: bottom, front and both sides
     * are structure cells. Capacity holds the largest positive sine result, 224.
     */
    private static BehaviorMachine tidal() {
        return BehaviorMachine.builder(MachineCasing.builder(Gt6KtfruMachines.TIDAL, "StainlessSteel")
                        .machineType(Gt6KtfruMachines.TIDAL).tier(3).build())
                .energy(EnergyInput.builder("RU", 1).faces(MachineFace.BACK)
                        .capacity(Gt6KtfruTidalGenerator.MAX_RATE).build())
                .energyOutput(EnergyOutput.builder("RU", Gt6KtfruTidalGenerator.MAX_RATE, 1)
                        .faces(MachineFace.TOP).build())
                .gui(GUI)
                .build();
    }

    /** KU 128 on the open bottom. The source window is 16 to 1024; this input accepts 64 to 256. */
    private static BehaviorMachine oilMiner() {
        return BehaviorMachine.builder(MachineCasing.builder(Gt6KtfruMachines.OIL_MINER, "StainlessSteel")
                        .machineType(Gt6KtfruMachines.OIL_MINER).tier(3).build())
                .energy(EnergyInput.builder("KU", 128).faces(MachineFace.BOTTOM).capacity(1024).build())
                .gui(GUI)
                .progress(70, 8)
                .fluid(32000, 26, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(32000, 44, 8, BehaviorMachine.SlotRole.OUTPUT)
                .build();
    }

    /**
     * EU has no nominal in the source. 1024 accepts 512 to 2048, inside 64 to 16384.
     * The bottom is a wall, so that face only satisfies the required input.
     * RU on the open front is the drill progress.
     */
    private static BehaviorMachine mantle() {
        return BehaviorMachine.builder(MachineCasing.builder(Gt6KtfruMachines.MANTLE, "Tungsten")
                        .machineType(Gt6KtfruMachines.MANTLE).tier(5).build())
                .energy(EnergyInput.builder("EU", 1024).faces(MachineFace.BOTTOM).capacity(16384).build())
                .auxiliaryEnergy(EnergyInput.builder("RU", 1).anyPacket()
                        .faces(MachineFace.FRONT).capacity(1_000_000_000L).build())
                .gui(GUI)
                .build();
    }

    /**
     * EU window in the source is 32 to 2048. Nominal 512 accepts 256 to 1024.
     * HU is the second input and can be any packet. Tanks cover the five source rows:
     * at most three fluids in and one recycled-fuel out. The map still says 6 and 6.
     */
    private static BehaviorMachine fuelDeburn() {
        return BehaviorMachine.builder(MachineCasing.builder(Gt6KtfruMachines.FUEL_DEBURN_TYPE, "Tungsten")
                        .machineType(Gt6KtfruMachines.FUEL_DEBURN_TYPE).tier(5).build())
                .energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(8192).build())
                .auxiliaryEnergy(EnergyInput.builder("HU", 1).anyPacket()
                        .faces(MachineFace.TOP).capacity(1_000_000_000L).build())
                .gui(GUI)
                .progress(126, 8)
                .item(26, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(64000, 48, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(64000, 66, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(64000, 84, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(16000, 102, 8, BehaviorMachine.SlotRole.OUTPUT)
                .recipeMaps(RecipeMaps.FUEL_DEBURN)
                .build();
    }

    private static LayoutPart casing(String name, String material, float hardness) {
        return LayoutPart.builder(name, material).hardness(hardness).build();
    }
}
