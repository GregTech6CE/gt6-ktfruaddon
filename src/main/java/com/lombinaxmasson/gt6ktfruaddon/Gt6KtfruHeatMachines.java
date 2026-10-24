package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.core.layout.Parts;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * Card 12 controllers. Each layout name is the machine type.
 * A behavior machine still needs one energy input, so a face that the structure
 * leaves open carries it when the source itself has no input.
 */
public final class Gt6KtfruHeatMachines implements Feature {
    static final String SUN_HEATER = "sun_heater";
    static final String SUN_MIRROR = "sun_heater_mirror_large";
    static final String TIDAL = "tidal_wave_generator";
    static final String OIL_MINER = "oil_miner";
    static final String MANTLE = "mantle_heater";
    /** Layout name, machine type and block id. The recipe map is {@link RecipeMaps#FUEL_DEBURN}. */
    static final String FUEL_DEBURN_TYPE = "fuel_deburn_factory";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(SUN_HEATER, Gt6KtfruSunHeater::new)
            .add(SUN_MIRROR, Gt6KtfruSunMirror::new)
            .add(TIDAL, Gt6KtfruTidalGenerator::new)
            .add(OIL_MINER, Gt6KtfruOilMiner::new)
            .add(MANTLE, Gt6KtfruMantleHeater::new)
            .add(FUEL_DEBURN_TYPE, Gt6KtfruFuelDeburner::new)
            .build();

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.FUEL_DEBURN, "Fuel Deburner", "fueldeburner", 3, 3, 0, 6, 6, 0, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(sunHeater());
        machines.add(sunMirror());
        machines.add(tidal());
        machines.add(oilMiner());
        machines.add(mantle());
        machines.add(fuelDeburn());
        return List.copyOf(machines);
    }

    @Override
    public List<LayoutPart> parts() {
        List<LayoutPart> parts = new ArrayList<>();
        parts.add(Parts.casing("heat_pipe", "Silver", 6.0F));
        parts.add(Parts.casing("heat_pipe_top", "Silver", 6.0F));
        parts.add(Parts.casing("tidal_blade", "StainlessSteel", 6.0F));
        parts.add(Parts.casing("large_sun_mirror_frame", "StainlessSteel", 10.0F));
        parts.add(Parts.casing("large_sun_mirror", "Silver", 10.0F));
        parts.add(Parts.casing("oil_miner_drill", "StainlessSteel", 6.0F));
        parts.add(Parts.casing("mantle_drill_rod", "Tungsten", 6.0F));
        parts.add(Parts.casing("enhanced_frame", "VanadiumSteel", 16.0F));
        parts.add(Parts.casing("chemical_reactor_pipe", "StainlessSteel", 1.0F));
        return List.copyOf(parts);
    }

    @Override
    public List<StructureLayout> layouts() {
        return List.of(
                Gt6KtfruHeatLayouts.sunHeater(),
                Gt6KtfruHeatLayouts.sunMirror(),
                Gt6KtfruHeatLayouts.tidal(),
                Gt6KtfruHeatLayouts.oilMiner(),
                Gt6KtfruHeatLayouts.mantle(),
                Gt6KtfruHeatLayouts.fuelDeburn());
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    /**
     * One absorb layer. The front is the open face; the block under the controller is a wall.
     * Nominal 640 HU accepts 320 to 1280.
     */
    private static BehaviorMachine sunHeater() {
        return BehaviorMachine.builder(MachineCasing.builder(SUN_HEATER, "StainlessSteel")
                        .machineType(SUN_HEATER).tier(3).build())
                .energy(EnergyInput.builder("HU", 640).faces(MachineFace.FRONT)
                        .capacity(Gt6KtfruSunHeater.capacity()).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build();
    }

    /** No energy in the source. The bottom is the only open face. */
    private static BehaviorMachine sunMirror() {
        return BehaviorMachine.builder(MachineCasing.builder(SUN_MIRROR, "StainlessSteel")
                        .machineType(SUN_MIRROR).tier(3).build())
                .energy(EnergyInput.builder("HU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build();
    }

    /**
     * RU leaves the top. The back is the required input: bottom, front and both sides
     * are structure cells. Capacity holds the largest positive sine result, 224.
     */
    private static BehaviorMachine tidal() {
        return BehaviorMachine.builder(MachineCasing.builder(TIDAL, "StainlessSteel")
                        .machineType(TIDAL).tier(3).build())
                .energy(EnergyInput.builder("RU", 1).faces(MachineFace.BACK)
                        .capacity(Gt6KtfruTidalGenerator.MAX_RATE).build())
                .energyOutput(EnergyOutput.builder("RU", Gt6KtfruTidalGenerator.MAX_RATE, 1)
                        .faces(MachineFace.TOP).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build();
    }

    /** KU 128 on the open bottom. The source window is 16 to 1024; this input accepts 64 to 256. */
    private static BehaviorMachine oilMiner() {
        return BehaviorMachine.builder(MachineCasing.builder(OIL_MINER, "StainlessSteel")
                        .machineType(OIL_MINER).tier(3).build())
                .energy(EnergyInput.builder("KU", 128).faces(MachineFace.BOTTOM).capacity(1024).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
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
        return BehaviorMachine.builder(MachineCasing.builder(MANTLE, "Tungsten")
                        .machineType(MANTLE).tier(5).build())
                .energy(EnergyInput.builder("EU", 1024).faces(MachineFace.BOTTOM).capacity(16384).build())
                .auxiliaryEnergy(EnergyInput.builder("RU", 1).anyPacket()
                        .faces(MachineFace.FRONT).capacity(1_000_000_000L).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build();
    }

    /**
     * EU window in the source is 32 to 2048. Nominal 512 accepts 256 to 1024.
     * HU is the second input and can be any packet. Tanks cover the five source rows:
     * at most three fluids in and one recycled-fuel out. The map still says 6 and 6.
     */
    private static BehaviorMachine fuelDeburn() {
        return BehaviorMachine.builder(MachineCasing.builder(FUEL_DEBURN_TYPE, "Tungsten")
                        .machineType(FUEL_DEBURN_TYPE).tier(5).build())
                .energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(8192).build())
                .auxiliaryEnergy(EnergyInput.builder("HU", 1).anyPacket()
                        .faces(MachineFace.TOP).capacity(1_000_000_000L).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .progress(126, 8)
                .item(26, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(64000, 48, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(64000, 66, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(64000, 84, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(16000, 102, 8, BehaviorMachine.SlotRole.OUTPUT)
                .recipeMaps(RecipeMaps.FUEL_DEBURN)
                .build();
    }

}
