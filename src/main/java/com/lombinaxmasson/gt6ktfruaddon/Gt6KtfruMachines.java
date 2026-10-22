package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.AddonMachineContributor;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.CompressedGasTankDeclaration;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.FluidPipeDeclaration;
import com.gregtech.gregtech.api.addon.FuelGenerator;
import com.gregtech.gregtech.api.addon.FuelOutput;
import com.gregtech.gregtech.api.addon.ItemPipeDeclaration;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.MachineIo;
import com.gregtech.gregtech.api.addon.RecipeMachine;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;
import com.gregtech.gregtech.api.addon.platform.BehaviorMachineContributor;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/**
 * Single-block machines from {@code tileEntityInit0}. Recipe machines are
 * 20000–27542. Fuel generators are the gas turbines and diesel engines from
 * 10000–10160. The large recipe machines are the three distillation towers and
 * the ore processing system. The two large compressed-gas valves and the five
 * gas compressors are behavior machines. Teflon fluid pipes, PVC item pipes
 * and the five compressed-gas drums are separate declarations. Card 08 adds the
 * void hoppers, miners, water mill, manual motors and flywheel boxes.
 * Card 09 adds the two gas batteries. Rows keep both poles and leave the electrolyte out.
 * Card 10 adds the transformer, flywheel-gear and light-transformer parts.
 * Card 11 adds the mask aligners, the CNC machine and their structure parts.
 * Card 12 adds the sun heater, large mirror, tidal generator, oil miner,
 * mantle heater and fuel deburn factory.
 * Card 13 adds the large gas and steam turbines, dynamos, expanded motors and fluid boiler.
 * Card 14 adds the liquid batteries and transform batteries.
 */
public final class Gt6KtfruMachines implements AddonMachineContributor, BehaviorMachineContributor {
    public static final String FLUID_HEATING = "gt6ktfruaddon.recipe.fluid_heating";
    public static final String LIGHT_MIXER = "gt6ktfruaddon.recipe.light_mixer";
    public static final String CIRCUIT_ASSEMBLER = "gt6ktfruaddon.recipe.circuit_assembler";
    public static final String LASER_CUTTER = "gt6ktfruaddon.recipe.laser_cutter";
    public static final String HEAT_MIXER = "gt6ktfruaddon.recipe.heat_mixer";
    public static final String ELECTRONICS_DESIGNER = "gt6ktfruaddon.recipe.electronics_designer";
    public static final String FLAW_DETECTOR = "gt6ktfruaddon.recipe.flaw_detector";
    public static final String WAFER_TESTER = "gt6ktfruaddon.recipe.wafer_tester";
    public static final String WAFER_COATER = "gt6ktfruaddon.recipe.wafer_coater";
    public static final String ULTRASONIC_MIXER = "gt6ktfruaddon.recipe.ultrasonic_mixer";
    public static final String TINY_DISTILL = "gt6ktfruaddon.recipe.tiny_distill_tower";
    public static final String SMALL_DISTILL = "gt6ktfruaddon.recipe.small_distill_tower";
    public static final String DISTILL = "gt6ktfruaddon.recipe.distill_tower";
    /** Layout name and machine type. The controller id uses the same string. */
    public static final String TINY_DISTILL_TYPE = "tiny_distillation_tower";
    public static final String SMALL_DISTILL_TYPE = "small_distillation_tower";
    public static final String DISTILL_TYPE = "distillation_tower";
    public static final String ORE_PROCESS = "gt6ktfruaddon.recipe.ore_processing_system";
    /** Source {@code ktfru.recipe.fuelbattery}. No rows are added. */
    public static final String FUEL_BATTERY = "gt6ktfruaddon.recipe.fuel_battery";
    public static final String GAS_BATTERY = "gas_battery";
    public static final String MASK_ALIGNER = "gt6ktfruaddon.recipe.mask_aligner";
    public static final String CNC = "gt6ktfruaddon.recipe.cnc";
    public static final String MASK_ALIGNER_UV = "mask_aligner_uv";
    public static final String MASK_ALIGNER_UV_PLUS = "mask_aligner_uv_plus";
    public static final String MASK_ALIGNER_DUV = "mask_aligner_duv";
    public static final String MASK_ALIGNER_EUV = "mask_aligner_euv";
    public static final String CNC_MACHINE = "cnc_machine";
    public static final String FUEL_DEBURN = "gt6ktfruaddon.recipe.fuel_deburner";
    public static final String SUN_HEATER = "sun_heater";
    public static final String SUN_MIRROR = "sun_heater_mirror_large";
    public static final String TIDAL = "tidal_wave_generator";
    public static final String OIL_MINER = "oil_miner";
    public static final String MANTLE = "mantle_heater";
    /** Layout name, machine type and block id. The recipe map is {@link #FUEL_DEBURN}. */
    public static final String FUEL_DEBURN_TYPE = "fuel_deburn_factory";
    public static final String TRANSFORMER_SMALL = "transformer_small";
    public static final String TRANSFORMER_LARGE = "transformer_large";
    public static final String FLYWHEEL_GEAR_SMALL = "flywheel_gear_small";
    public static final String FLYWHEEL_GEAR_LARGE = "flywheel_gear_large";
    public static final String LIGHT_TRANSFORMER_SMALL = "light_transformer_small";
    public static final String LIGHT_TRANSFORMER_LARGE = "light_transformer_large";
    /** Layout name and machine type. The controller id uses the same string. */
    public static final String ORE_PROCESS_TYPE = "ore_processing_system";
    /** Built-in GT fuel map behind {@code FM.Gas}. Already contains hydrogen. */
    public static final String GAS_FUEL = "gt.recipe.fuels.gas";
    /** Built-in GT fuel map behind {@code FM.Engine}. Already contains diesel. */
    public static final String ENGINE_FUEL = "gt.recipe.fuels.engine";
    /** Source 30100. Layout name, machine type and block id. */
    public static final String STEEL_GAS_TANK = "steel_compressed_gas_tank";
    /** Source 30101. */
    public static final String STAINLESS_GAS_TANK = "stainless_compressed_gas_tank";
    /** Shared machine type for source 22000–22004. Block ids stay per tier. */
    public static final String GAS_COMPRESSOR = "gas_compressor";
    public static final String VOID_HOPPER = "void_hopper";
    public static final String MINER = "miner";
    public static final String WATER_MILL = "water_mill";
    public static final String MANUAL_MOTOR = "manual_motor";
    public static final String FLYWHEEL = "flywheel";
    public static final String LARGE_FLYWHEEL = "large_flywheel";
    public static final String ELECTRIC_FLYWHEEL = "electric_flywheel";
    public static final String LARGE_ELECTRIC_FLYWHEEL = "large_electric_flywheel";
    public static final String VACUUM_FLYWHEEL = "vacuum_flywheel";
    public static final String LARGE_VACUUM_FLYWHEEL = "large_vacuum_flywheel";
    private static final String KINETIC_GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";
    public static final String BRONZE_COMPRESSOR = "gas_compressor_bronze";
    public static final String STEEL_GAS_DRUM = "steel_gas_drum";
    public static final String STAINLESS_GAS_DRUM = "stainless_gas_drum";
    public static final String MANASTEEL_GAS_DRUM = "manasteel_gas_drum";
    public static final String NETHERITE_GAS_DRUM = "netherite_gas_drum";
    public static final String TUNGSTENSTEEL_GAS_DRUM = "tungstensteel_gas_drum";
    public static final String[] GAS_DRUMS = {
            STEEL_GAS_DRUM, STAINLESS_GAS_DRUM, MANASTEEL_GAS_DRUM,
            NETHERITE_GAS_DRUM, TUNGSTENSTEEL_GAS_DRUM
    };
    /** PipeSpec.PipeSize names, lower case. */
    public static final String[] FLUID_PIPE_SIZES = {
            "tiny", "small", "medium", "large", "huge", "quadruple", "nonuple"
    };
    /** ItemPipeSpec.ItemPipeSize names, lower case. */
    public static final String[] ITEM_PIPE_SIZES = {
            "medium", "large", "huge", "restrictive_medium", "restrictive_large", "restrictive_huge"
    };
    public static final String TEFLON_PIPE = "teflon";
    public static final String PVC_PIPE = "pvc";

    public static String fluidPipeId(String size) {
        return "pipe_" + size + "_" + TEFLON_PIPE;
    }

    public static String itemPipeId(String size) {
        return "item_pipe_" + size + "_" + PVC_PIPE;
    }
    /**
     * Source {@code NBT_TANK_CAPACITY}. One behavior tank stops at
     * 1,024,000,000 mB, so the valve splits this across seven tanks.
     */
    public static final long GAS_TANK_CAPACITY = 6_912_000_000L;

    private static final String[] ELECTRIC = {
            "", "SteelGalvanized", "Aluminium", "StainlessSteel", "Chromium", "Titanium"
    };
    private static final long[] VOLTAGE = {0, 32, 128, 512, 2048, 8192};
    private static final String[] TIER = {"", "lv", "mv", "hv", "ev", "iv"};

    @Override
    public String addonId() {
        return Gt6KtfruAddon.MODID;
    }

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(map(FLUID_HEATING, "Fluid Heating", "fluidheating", 0, 0, 0, 1, 1, 1, 1));
        maps.add(map(LIGHT_MIXER, "Light Mixer", "lightmixer", 6, 3, 0, 6, 6, 0, 1));
        maps.add(map(CIRCUIT_ASSEMBLER, "Circuit Assembler", "circuitassembler", 9, 3, 1, 1, 0, 0, 1));
        maps.add(map(LASER_CUTTER, "Laser Cutter", "lasercutter", 2, 3, 1, 1, 0, 0, 1));
        maps.add(map(HEAT_MIXER, "Heat Mixer", "heatmixer", 6, 3, 0, 6, 3, 0, 1));
        maps.add(map(ELECTRONICS_DESIGNER, "Electronics Designer", "electronicsdesigner", 6, 3, 1, 3, 0, 0, 1));
        maps.add(map(FLAW_DETECTOR, "Flaw Detector", "flawdetector", 3, 3, 1, 0, 0, 0, 1));
        maps.add(map(WAFER_TESTER, "Wafer Tester", "wafertester", 3, 3, 0, 0, 0, 0, 1));
        maps.add(map(WAFER_COATER, "Wafer Coater", "wafercoater", 3, 3, 0, 3, 0, 0, 1));
        maps.add(map(ULTRASONIC_MIXER, "Ultrasonic Mixer", "ultrasonicmixer", 6, 3, 0, 6, 2, 0, 1));
        maps.add(map(TINY_DISTILL, "Tiny Distillation Tower", "tinydistillationtower", 1, 3, 0, 1, 3, 0, 1));
        maps.add(map(SMALL_DISTILL, "Small Distillation Tower", "smalldistillationtower", 1, 3, 0, 1, 6, 0, 1));
        maps.add(map(DISTILL, "Distillation Tower", "distillationtower", 1, 3, 0, 1, 9, 0, 1));
        maps.add(map(ORE_PROCESS, "Ore Process System", "oreprocesssystem", 3, 9, 1, 3, 3, 0, 1));
        maps.add(map(FUEL_BATTERY, "Fuel Battery", "gascompressor", 2, 0, 2, 3, 2, 2, 4));
        maps.add(map(MASK_ALIGNER, "Mask Aligner", "gascompressor", 3, 3, 1, 3, 0, 0, 1));
        maps.add(map(CNC, "CNC", "gascompressor", 4, 3, 1, 3, 0, 1, 1));
        maps.add(map(FUEL_DEBURN, "Fuel Deburner", "gascompressor", 3, 3, 0, 6, 6, 0, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> addonMachines() {
        List<AddonMachine> machines = new ArrayList<>();
        heat(machines, "fluid_heater", "fluidheater", "HU", FLUID_HEATING,
                "Steel", "Invar", "Titanium", "TungstenCarbide");
        kinetic(machines, "light_mixer", "lightmixer", LIGHT_MIXER, new int[] {4, 8, 16, 32},
                "Bronze", "Steel", "Titanium", "Tungstensteel");
        electric(machines, "circuit_assembler", "circuitassembler", "LU", CIRCUIT_ASSEMBLER, 1, 5, null);
        electric(machines, "laser_cutter", "lasercutter", "LU", LASER_CUTTER, 1, 5, null);
        electric(machines, "heat_mixer", "heatmixer", "EU", HEAT_MIXER, 1, 5, null);
        electric(machines, "electronics_designer", "eda", "EU", ELECTRONICS_DESIGNER, 1, 5, null);
        electric(machines, "flaw_detector", "flawdetector", "EU", FLAW_DETECTOR, 1, 5, null);
        electric(machines, "electric_light_mixer", "lightmixerelectric", "EU", LIGHT_MIXER, 1, 5,
                new int[] {4, 8, 16, 32, 64});
        electric(machines, "wafer_tester", "wafertester", "EU", WAFER_TESTER, 1, 5, null);
        electric(machines, "wafer_coater", "wafercoater", "EU", WAFER_COATER, 1, 5, null);
        electric(machines, "ultrasonic_mixer", "ultrasonicmixer", "EU", ULTRASONIC_MIXER, 3, 5, null);
        machines.add(distillationTower(TINY_DISTILL_TYPE, TINY_DISTILL, 1024));
        machines.add(distillationTower(SMALL_DISTILL_TYPE, SMALL_DISTILL, 2048));
        machines.add(distillationTower(DISTILL_TYPE, DISTILL, 4096));
        machines.add(oreProcessingSystem());
        machines.add(gasTank(STEEL_GAS_TANK, "Steel"));
        machines.add(gasTank(STAINLESS_GAS_TANK, "StainlessSteel"));
        machines.add(compressor(BRONZE_COMPRESSOR, "Bronze", 1, 32, 8_000));
        machines.add(compressor("gas_compressor_steel", "Steel", 2, 128, 20_000));
        machines.add(compressor("gas_compressor_titanium", "Titanium", 3, 512, 80_000));
        machines.add(compressor("gas_compressor_tungstensteel", "Tungstensteel", 4, 2048, 200_000));
        machines.add(compressor("gas_compressor_adamantium", "Adamantium", 5, 8192, 800_000));
        kineticBehaviors(machines);
        machines.add(gasBattery("gas_battery_aluminium", "Aluminium", 1));
        machines.add(gasBattery("gas_battery_stainless", "StainlessSteel", 2));
        transformerParts(machines);
        machines.add(maskAligner(MASK_ALIGNER_UV, "Aluminium", 2, 128, 64, 256,
                MachineFace.TOP, MachineFace.BACK, null, false));
        machines.add(maskAligner(MASK_ALIGNER_UV_PLUS, "StainlessSteel", 3, 512, 256, 1024,
                null, MachineFace.TOP, MachineFace.BACK, false));
        machines.add(maskAligner(MASK_ALIGNER_DUV, "Chromium", 4, 2048, 1024, 4096,
                MachineFace.TOP, MachineFace.BACK, null, true));
        machines.add(maskAligner(MASK_ALIGNER_EUV, "Tungsten", 5, 8192, 4096, 16384,
                MachineFace.TOP, MachineFace.BACK, null, true));
        machines.add(cncMachine());
        gas(machines, "bronze", "Bronze", 16);
        gas(machines, "steel", "Steel", 32);
        gas(machines, "invar", "Invar", 64);
        gas(machines, "titanium", "Titanium", 128);
        gas(machines, "tungstensteel", "Tungstensteel", 372);
        gas(machines, "iridium", "Iridium", 768);
        gas(machines, "chromium", "Chromium", 256);
        diesel(machines, "diesel", "bronze", "Bronze", 3300, 16);
        diesel(machines, "diesel", "arsenic_copper", "ArsenicCopper", 3300, 16);
        diesel(machines, "diesel", "arsenic_bronze", "ArsenicBronze", 3300, 24);
        diesel(machines, "diesel", "steel", "Steel", 3300, 32);
        diesel(machines, "diesel", "invar", "Invar", 3300, 64);
        diesel(machines, "diesel", "titanium", "Titanium", 3300, 128);
        diesel(machines, "diesel", "tungstensteel", "Tungstensteel", 3300, 372);
        diesel(machines, "diesel", "iridium", "Iridium", 3300, 768);
        diesel(machines, "diesel", "chromium", "Chromium", 3300, 256);
        diesel(machines, "turbo_diesel", "bronze", "Bronze", 5200, 24);
        diesel(machines, "turbo_diesel", "arsenic_copper", "ArsenicCopper", 5200, 24);
        diesel(machines, "turbo_diesel", "arsenic_bronze", "ArsenicBronze", 5200, 36);
        diesel(machines, "turbo_diesel", "steel", "Steel", 5200, 48);
        diesel(machines, "turbo_diesel", "invar", "Invar", 5200, 96);
        diesel(machines, "turbo_diesel", "titanium", "Titanium", 5200, 172);
        diesel(machines, "turbo_diesel", "tungstensteel", "Tungstensteel", 5200, 558);
        diesel(machines, "turbo_diesel", "iridium", "Iridium", 5200, 1152);
        diesel(machines, "turbo_diesel", "chromium", "Chromium", 5200, 384);
        diesel(machines, "turbo_diesel", "osmium", "Osmium", 5000, 2304);
        diesel(machines, "turbo_diesel", "trinaquadalloy", "Trinaquadalloy", 4800, 4608);
        Gt6KtfruHeatMachines.add(machines);
        Gt6KtfruTurbines.add(machines);
        Gt6KtfruBatteries.add(machines);
        return List.copyOf(machines);
    }

    @Override
    public List<StructureLayout> structureLayouts() {
        var layouts = new ArrayList<>(List.of(
                distillationLayout(TINY_DISTILL_TYPE, 4),
                distillationLayout(SMALL_DISTILL_TYPE, 6),
                distillationLayout(DISTILL_TYPE, 9),
                oreProcessingLayout(),
                gasTankLayout(STEEL_GAS_TANK, "dense_steel_wall"),
                gasTankLayout(STAINLESS_GAS_TANK, "tank_wall_dense"),
                maskAlignerUvLayout(),
                maskAlignerUvPlusLayout(),
                maskAlignerDuvLayout(MASK_ALIGNER_DUV),
                maskAlignerDuvLayout(MASK_ALIGNER_EUV),
                cncLayout(),
                Gt6KtfruHeatLayouts.sunHeater(),
                Gt6KtfruHeatLayouts.sunMirror(),
                Gt6KtfruHeatLayouts.tidal(),
                Gt6KtfruHeatLayouts.oilMiner(),
                Gt6KtfruHeatLayouts.mantle(),
                Gt6KtfruHeatLayouts.fuelDeburn()));
        Gt6KtfruTurbines.addLayouts(layouts);
        Gt6KtfruBatteries.addLayouts(layouts);
        return List.copyOf(layouts);
    }

    @Override
    public List<LayoutPart> layoutParts() {
        List<LayoutPart> parts = new ArrayList<>(List.of(
                casing("aluminium_wall", "Aluminium", 4.0F),
                casing("sunlight_absorber", "Silver", 6.0F),
                casing("catalytic_reactor_casing", "Tungstensteel", 10.0F),
                casing("mask_aligner_platform_motor", "StainlessSteel", 6.0F),
                casing("mask_aligner_wafer_platform", "StainlessSteel", 6.0F),
                casing("cnc_platform", "Aluminium", 6.0F),
                casing("cnc_motor", "Aluminium", 6.0F),
                casing("cnc_cutter_head", "StainlessSteel", 6.0F),
                casing("light_module_uv", "Aluminium", 6.0F),
                casing("light_module_uv_plus", "StainlessSteel", 6.0F),
                casing("light_module_duv", "Chromium", 6.0F),
                casing("light_module_euv", "Tungstensteel", 6.0F),
                casing("mask_aligner_shell", "StainlessSteel", 10.0F),
                casing("mask_aligner_io_uv", "Aluminium", 6.0F),
                casing("mask_aligner_io_uv_plus", "StainlessSteel", 6.0F),
                energyModule("mask_aligner_energy_uv", "Aluminium", 192, 64, 256),
                energyModule("mask_aligner_energy_uv_plus", "StainlessSteel", 768, 256, 1024)));
        Gt6KtfruHeatMachines.addParts(parts);
        Gt6KtfruTurbines.addParts(parts);
        Gt6KtfruBatteries.addParts(parts);
        return List.copyOf(parts);
    }

    @Override
    public Set<String> machineTypes() {
        var types = new LinkedHashSet<>(Set.of(STEEL_GAS_TANK, STAINLESS_GAS_TANK, GAS_COMPRESSOR,
                VOID_HOPPER, MINER, WATER_MILL, MANUAL_MOTOR,
                FLYWHEEL, LARGE_FLYWHEEL, ELECTRIC_FLYWHEEL, LARGE_ELECTRIC_FLYWHEEL,
                VACUUM_FLYWHEEL, LARGE_VACUUM_FLYWHEEL, GAS_BATTERY,
                TRANSFORMER_SMALL, TRANSFORMER_LARGE,
                FLYWHEEL_GEAR_SMALL, FLYWHEEL_GEAR_LARGE,
                LIGHT_TRANSFORMER_SMALL, LIGHT_TRANSFORMER_LARGE,
                SUN_HEATER, SUN_MIRROR, TIDAL, OIL_MINER, MANTLE, FUEL_DEBURN_TYPE));
        types.addAll(Gt6KtfruTurbines.behaviorTypes());
        types.addAll(Gt6KtfruBatteries.behaviorTypes());
        return Set.copyOf(types);
    }

    @Override
    public MachineBehavior create(String machineType) {
        if (STEEL_GAS_TANK.equals(machineType) || STAINLESS_GAS_TANK.equals(machineType)) {
            return new Gt6KtfruGasTank();
        }
        if (GAS_COMPRESSOR.equals(machineType)) return new Gt6KtfruGasCompressor();
        if (VOID_HOPPER.equals(machineType)) return new Gt6KtfruVoidHopper();
        if (MINER.equals(machineType)) return new Gt6KtfruMiner();
        if (WATER_MILL.equals(machineType)) return new Gt6KtfruWaterMill();
        if (MANUAL_MOTOR.equals(machineType)) return new Gt6KtfruManualMotor();
        if (FLYWHEEL.equals(machineType)) return new Gt6KtfruFlywheel(4, 0.1);
        if (LARGE_FLYWHEEL.equals(machineType)) return new Gt6KtfruFlywheel(16, 0.15);
        if (ELECTRIC_FLYWHEEL.equals(machineType)) return new Gt6KtfruFlywheel(4, 0.07);
        if (LARGE_ELECTRIC_FLYWHEEL.equals(machineType)) return new Gt6KtfruFlywheel(16, 0.1);
        if (VACUUM_FLYWHEEL.equals(machineType)) return new Gt6KtfruFlywheel(4, 0.0001);
        if (LARGE_VACUUM_FLYWHEEL.equals(machineType)) return new Gt6KtfruFlywheel(16, 0.00012);
        if (GAS_BATTERY.equals(machineType)) return new Gt6KtfruFuelBattery();
        if (transformerType(machineType)) return new Gt6KtfruTransformerPart(transformerAmpere(machineType));
        if (Gt6KtfruHeatMachines.owns(machineType)) return Gt6KtfruHeatMachines.create(machineType);
        if (Gt6KtfruTurbines.owns(machineType)) return Gt6KtfruTurbines.create(machineType);
        if (Gt6KtfruBatteries.owns(machineType)) return Gt6KtfruBatteries.create(machineType);
        throw new IllegalArgumentException(machineType);
    }

    @Override
    public List<FluidPipeDeclaration> fluidPipes() {
        return List.of(new FluidPipeDeclaration(TEFLON_PIPE, "Teflon", 800, true, true, false, true, 0, 0));
    }

    @Override
    public List<ItemPipeDeclaration> itemPipes() {
        return List.of(new ItemPipeDeclaration(PVC_PIPE, "PVC", 8192, 4));
    }

    @Override
    public List<CompressedGasTankDeclaration> compressedGasTanks() {
        return List.of(
                drum(STEEL_GAS_DRUM, "Steel", 12_800_000, false, false, false),
                drum(STAINLESS_GAS_DRUM, "StainlessSteel", 12_800_000, true, false, false),
                drum(MANASTEEL_GAS_DRUM, "Manasteel", 12_800_000, true, false, true),
                drum(NETHERITE_GAS_DRUM, "Netherite", 25_600_000, true, true, true),
                drum(TUNGSTENSTEEL_GAS_DRUM, "Tungstensteel", 51_200_000, false, false, true));
    }

    private static RecipeTable map(String name, String title, String gui,
            int inputItems, int outputItems, int minimalInputItems,
            int inputFluids, int outputFluids, int minimalInputFluids, int minimalInputs) {
        return RecipeTable.machine(name)
                .title(title)
                .gui("gt6ktfruaddon:textures/gui/machines/" + gui)
                .inputItems(inputItems)
                .outputItems(outputItems)
                .minimalInputItems(minimalInputItems)
                .inputFluids(inputFluids)
                .outputFluids(outputFluids)
                .minimalInputFluids(minimalInputFluids)
                .minimalInputs(minimalInputs)
                .needsOutputs(true)
                .bucketIn(true)
                .bucketOut(true)
                .build();
    }

    private static void heat(List<AddonMachine> out, String prefix, String type, String energy, String recipeMap,
            String... materials) {
        String[] suffix = {"steel", "invar", "titanium", "tungsten_carbide"};
        for (int i = 0; i < materials.length; i++) {
            out.add(basic(prefix + "_" + suffix[i], materials[i], type, energy, VOLTAGE[i + 1], i + 1, 1, recipeMap));
        }
    }

    private static void kinetic(List<AddonMachine> out, String prefix, String type, String recipeMap, int[] parallel,
            String... materials) {
        String[] suffix = {"bronze", "steel", "titanium", "tungstensteel"};
        for (int i = 0; i < materials.length; i++) {
            out.add(basic(prefix + "_" + suffix[i], materials[i], type, "RU", VOLTAGE[i + 1], i + 1, parallel[i], recipeMap));
        }
    }

    private static void electric(List<AddonMachine> out, String prefix, String type, String energy, String recipeMap,
            int first, int last, int[] parallel) {
        for (int tier = first; tier <= last; tier++) {
            int count = parallel == null ? 1 : parallel[tier - first];
            out.add(basic(prefix + "_" + TIER[tier], ELECTRIC[tier], type, energy, VOLTAGE[tier], tier, count, recipeMap));
        }
    }

    private static void gas(List<AddonMachine> out, String suffix, String material, long perTick) {
        out.add(fuel("gas_turbine_" + suffix, material, "gas_turbine_" + suffix, GAS_FUEL, 3500, perTick));
    }

    private static void diesel(List<AddonMachine> out, String prefix, String suffix, String material,
            int efficiency, long perTick) {
        String id = prefix + "_" + suffix;
        out.add(fuel(id, material, id, ENGINE_FUEL, efficiency, perTick));
    }

    private static FuelGenerator fuel(String id, String material, String type, String fuelMap,
            int efficiency, long perTick) {
        return FuelGenerator.builder(MachineCasing.builder(id, material).machineType(type).build())
                .fuelMap(fuelMap)
                .efficiency(efficiency)
                .output(FuelOutput.builder("RU", perTick).front().build())
                // Vanilla MotorLiquid sizes both tanks at rate * 10. The TFRU fork uses 16 and 128;
                // this template has one capacity for the fuel tank and every exhaust tank.
                .tank(Math.toIntExact(perTick * 10))
                .single()
                .build();
    }

    private static RecipeMachine basic(String id, String material, String type, String energy, long perTick,
            int tier, int parallel, String recipeMap) {
        var energyInput = EnergyInput.builder(energy, perTick);
        var io = MachineIo.builder();
        var recipe = RecipeMachine.builder(MachineCasing.builder(id, material).machineType(type).tier(tier).build())
                .recipeMap(recipeMap)
                .basic()
                .parallel(parallel);
        // Source FlawDetector keeps NBT_USE_OUTPUT_TANK and rolls the first item
        // output's chance once: hit for output 0, otherwise output 1.
        if (id.startsWith("flaw_detector_")) {
            recipe.searchOutputTanks(true).exclusiveOutputs(true);
        }
        sides(energyInput, io, recipe, id);
        return recipe.energy(energyInput.build()).io(io.build()).build();
    }

    /**
     * Faces follow {@code tileEntityInit0} names, not the raw GT6 bit index.
     * {@code SBIT_I} and {@code SIDE_INSIDE} are no external face. {@link MachineFace#LEFT}
     * is the machine's left. A named channel with no face is closed.
     */
    private static void sides(EnergyInput.Builder energy, MachineIo.Builder io, RecipeMachine.Builder recipe, String id) {
        if (id.startsWith("fluid_heater_")) {
            io.itemInputs().itemOutputs();
            io.fluidInputs(MachineFace.BACK).fluidAutoInput(MachineFace.BACK);
            io.fluidOutputs(MachineFace.TOP).fluidAutoOutput(MachineFace.TOP);
            energy.faces(MachineFace.BOTTOM);
            recipe.cheapOverclocking(true);
            return;
        }
        if (id.startsWith("light_mixer_") || id.startsWith("electric_light_mixer_")) {
            io.itemInputs(MachineFace.LEFT, MachineFace.FRONT).itemAutoInput(MachineFace.LEFT);
            io.itemOutputs(MachineFace.RIGHT, MachineFace.BACK).itemAutoOutput(MachineFace.RIGHT);
            io.fluidInputs(MachineFace.LEFT, MachineFace.FRONT).fluidAutoInput(MachineFace.FRONT);
            io.fluidOutputs(MachineFace.RIGHT, MachineFace.BACK).fluidAutoOutput(MachineFace.BACK);
            energy.faces(MachineFace.BOTTOM);
            // Only the kinetic LightMixer checks the block above. The electric one does not.
            if (id.startsWith("light_mixer_")) recipe.minimumLightAbove(14);
            return;
        }
        if (id.startsWith("circuit_assembler_")) {
            io.itemInputs(MachineFace.LEFT).itemAutoInput(MachineFace.LEFT);
            io.itemOutputs(MachineFace.RIGHT).itemAutoOutput(MachineFace.RIGHT);
            io.fluidInputs(MachineFace.BOTTOM, MachineFace.LEFT).fluidAutoInput(MachineFace.BOTTOM);
            energy.faces(MachineFace.TOP);
            return;
        }
        if (id.startsWith("laser_cutter_")) {
            io.itemInputs(MachineFace.LEFT).itemAutoInput(MachineFace.LEFT);
            io.itemOutputs(MachineFace.RIGHT).itemAutoOutput(MachineFace.RIGHT);
            // Source auto is the top, which is not one of the input faces.
            io.fluidInputs(MachineFace.BOTTOM, MachineFace.LEFT).fluidAutoInput(MachineFace.TOP);
            energy.faces(MachineFace.TOP);
            return;
        }
        if (id.startsWith("heat_mixer_") || id.startsWith("ultrasonic_mixer_")) {
            io.itemInputs(MachineFace.LEFT, MachineFace.TOP).itemAutoInput(MachineFace.LEFT);
            io.itemOutputs(MachineFace.RIGHT, MachineFace.BACK).itemAutoOutput(MachineFace.RIGHT);
            io.fluidInputs(MachineFace.LEFT, MachineFace.TOP).fluidAutoInput(MachineFace.TOP);
            io.fluidOutputs(MachineFace.RIGHT, MachineFace.BACK).fluidAutoOutput(MachineFace.BACK);
            energy.faces(MachineFace.BOTTOM);
            return;
        }
        if (id.startsWith("electronics_designer_") || id.startsWith("flaw_detector_")) {
            io.itemInputs(MachineFace.LEFT).itemAutoInput(MachineFace.LEFT);
            io.itemOutputs(MachineFace.RIGHT).itemAutoOutput(MachineFace.RIGHT);
            io.fluidInputs(MachineFace.TOP).fluidAutoInput(MachineFace.TOP);
            energy.faces(MachineFace.BACK);
            recipe.cheapOverclocking(true);
            return;
        }
        if (id.startsWith("wafer_tester_") || id.startsWith("wafer_coater_")) {
            io.itemInputs(MachineFace.TOP, MachineFace.LEFT).itemAutoInput(MachineFace.LEFT);
            io.itemOutputs(MachineFace.RIGHT, MachineFace.BOTTOM).itemAutoOutput(MachineFace.RIGHT);
            io.fluidInputs(MachineFace.TOP, MachineFace.LEFT).fluidAutoInput(MachineFace.TOP);
            io.fluidOutputs(MachineFace.RIGHT, MachineFace.BOTTOM).fluidAutoOutput(MachineFace.BOTTOM);
            energy.faces(MachineFace.BACK);
            if (id.startsWith("wafer_tester_")) recipe.cheapOverclocking(true);
            return;
        }
        throw new IllegalStateException("No side mask for " + id);
    }

    /**
     * Source 30003, 30009, 30010, 30011. LU stays on the controller declaration.
     * UV and UV+ take that LU through the light-module cells. The energy module
     * is a condition part and is not a second recipe energy.
     */
    private static RecipeMachine maskAligner(String id, String material, int tier, long nominal, long min, long max,
            MachineFace itemIn, MachineFace itemOut, MachineFace fluidOut, boolean controllerEnergy) {
        var io = MachineIo.builder();
        if (itemIn != null) io.itemInputs(itemIn).itemAutoInput(itemIn);
        if (itemOut != null) io.itemOutputs(itemOut).itemAutoOutput(itemOut);
        if (fluidOut != null) io.fluidOutputs(fluidOut).fluidAutoOutput(fluidOut);
        var energy = EnergyInput.builder("LU", nominal).range(min, max);
        if (controllerEnergy) energy.faces(MachineFace.all());
        else energy.faces();
        return RecipeMachine.builder(MachineCasing.builder(id, material).machineType(id).tier(tier).build())
                .energy(energy.build())
                .io(io.build())
                .parallel(1)
                .recipeMap(MASK_ALIGNER)
                .largeRecipe()
                .structureKeys(structureLines(id, 4))
                .build();
    }

    /** Source 30012. EU enters the aluminium walls. The motor and cutter are casings. */
    private static RecipeMachine cncMachine() {
        return RecipeMachine.builder(MachineCasing.builder(CNC_MACHINE, "Aluminium").machineType(CNC_MACHINE).tier(2).build())
                .energy(EnergyInput.builder("EU", 128).range(64, 256).faces().build())
                .io(MachineIo.builder()
                        .itemInputs(MachineFace.TOP).itemAutoInput(MachineFace.TOP)
                        .itemOutputs(MachineFace.BOTTOM).itemAutoOutput(MachineFace.BOTTOM)
                        .build())
                .parallel(1)
                .recipeMap(CNC)
                .largeRecipe()
                .structureKeys(structureLines(CNC_MACHINE, 4))
                .build();
    }

    private static List<String> structureLines(String type, int count) {
        List<String> keys = new ArrayList<>();
        for (int line = 1; line <= count; line++) keys.add("gt6ktfruaddon.structure." + type + "." + line);
        return List.copyOf(keys);
    }

    /** Layer Y, offset -1,0,0. Positive source X is the machine's left, so right is {@code 1 - row}. */
    private static void yLayer(StructureLayout.Builder layout, String type, int up, String[] rows) {
        for (int row = 0; row < rows.length; row++) {
            String line = rows[row];
            for (int col = 0; col < line.length(); col++) {
                char symbol = line.charAt(col);
                if (symbol == ' ') continue;
                layout.cell(1 - row, up, col, CellMatch.part(maskPart(type, symbol)), maskRole(type, symbol));
            }
        }
    }

    private static StructureLayout maskAlignerUvLayout() {
        var layout = StructureLayout.builder(MASK_ALIGNER_UV);
        yLayer(layout, MASK_ALIGNER_UV, 0, new String[] {"WW", " A", "WW"});
        yLayer(layout, MASK_ALIGNER_UV, 1, new String[] {"WW", "BC", "WW"});
        return layout.build();
    }

    private static StructureLayout maskAlignerUvPlusLayout() {
        var layout = StructureLayout.builder(MASK_ALIGNER_UV_PLUS);
        yLayer(layout, MASK_ALIGNER_UV_PLUS, 0, new String[] {"WWW", " DA", "WWW"});
        yLayer(layout, MASK_ALIGNER_UV_PLUS, 1, new String[] {"WBW", "BCB", "WBW"});
        yLayer(layout, MASK_ALIGNER_UV_PLUS, 2, new String[] {"LLL", "LLL", "LLL"});
        return layout.build();
    }

    /**
     * DUV and EUV share one map. 31110 and 31120 were never registered, so those
     * two cells are absent. 0 is the controller.
     */
    private static StructureLayout maskAlignerDuvLayout(String type) {
        var layout = StructureLayout.builder(type);
        int[][][] ids = {
                {{31002, 0, 31002}, {31002, 31200, 31002}},
                {{31002, 31110, 31002}, {31002, 31120, 31002}}
        };
        for (int up = 0; up < ids.length; up++) {
            for (int back = 0; back < ids[up].length; back++) {
                for (int mapX = 0; mapX < ids[up][back].length; mapX++) {
                    int id = ids[up][back][mapX];
                    if (id == 0 || id == 31110 || id == 31120) continue;
                    String part = id == 31200 ? "catalytic_reactor_casing" : "sunlight_absorber";
                    layout.cell(1 - mapX, up, back, CellMatch.part(part), StructureRole.ITEM_FLUID_INPUT);
                }
            }
        }
        return layout.build();
    }

    private static StructureLayout cncLayout() {
        var layout = StructureLayout.builder(CNC_MACHINE);
        yLayer(layout, CNC_MACHINE, 0, new String[] {"AAA", " CC", "BBB", "BBB", "BBB"});
        yLayer(layout, CNC_MACHINE, 1, new String[] {"AAA", "CCC", "   ", "   ", "   "});
        yLayer(layout, CNC_MACHINE, 2, new String[] {" A ", " A ", " A ", " D ", "   "});
        return layout.build();
    }

    private static String maskPart(String type, char symbol) {
        if (CNC_MACHINE.equals(type)) {
            return switch (symbol) {
                case 'A' -> "aluminium_wall";
                case 'B' -> "cnc_platform";
                case 'C' -> "cnc_motor";
                case 'D' -> "cnc_cutter_head";
                default -> throw new IllegalStateException("unknown cnc symbol " + symbol);
            };
        }
        if (MASK_ALIGNER_UV_PLUS.equals(type)) {
            return switch (symbol) {
                case 'W' -> "mask_aligner_shell";
                case 'A' -> "mask_aligner_energy_uv_plus";
                case 'B' -> "mask_aligner_platform_motor";
                case 'C' -> "mask_aligner_wafer_platform";
                case 'D' -> "mask_aligner_io_uv_plus";
                case 'L' -> "light_module_uv_plus";
                default -> throw new IllegalStateException("unknown uv+ symbol " + symbol);
            };
        }
        return switch (symbol) {
            case 'W' -> "aluminium_wall";
            case 'A' -> "mask_aligner_energy_uv";
            case 'B' -> "light_module_uv";
            case 'C' -> "mask_aligner_io_uv";
            default -> throw new IllegalStateException("unknown uv symbol " + symbol);
        };
    }

    private static StructureRole maskRole(String type, char symbol) {
        if (CNC_MACHINE.equals(type)) {
            return switch (symbol) {
                case 'A' -> StructureRole.ENERGY_INPUT;
                case 'B' -> StructureRole.ITEM_FLUID_INPUT;
                default -> StructureRole.CASING;
            };
        }
        if (MASK_ALIGNER_UV_PLUS.equals(type)) {
            return switch (symbol) {
                case 'L' -> StructureRole.ENERGY_INPUT;
                case 'C' -> StructureRole.ITEM_FLUID_IO;
                case 'A' -> StructureRole.CASING;
                default -> StructureRole.ITEM_FLUID_INPUT;
            };
        }
        return switch (symbol) {
            case 'B' -> StructureRole.ENERGY_INPUT;
            case 'C' -> StructureRole.ITEM_FLUID_IO;
            case 'A' -> StructureRole.CASING;
            default -> StructureRole.ITEM_FLUID_INPUT;
        };
    }

    private static LayoutPart casing(String name, String material, float hardness) {
        return LayoutPart.builder(name, material).hardness(hardness).build();
    }

    private static LayoutPart energyModule(String name, String material, long nominal, long min, long max) {
        return LayoutPart.builder(name, material).hardness(6.0F)
                .energyInput(EnergyInput.builder("EU", nominal).range(min, max)
                        .capacity(max).faces(MachineFace.all()).build())
                .condition(nominal)
                .build();
    }

    /**
     * Sources 30004, 30005 and 30006. One 3×3 heat layer, then four, six or nine
     * 3×3 distillation layers. The controller sits in the front-middle of the
     * lowest distillation layer and is not a cell. Output hatches are the
     * back-middle column. Coordinates are the layout's right, up and back.
     * The controller accepts no item, fluid or energy; the structure roles do.
     */
    private static RecipeMachine distillationTower(String type, String recipe, long maxPacket) {
        List<String> keys = new ArrayList<>();
        for (int line = 1; line <= 6; line++) {
            keys.add("gt6ktfruaddon.structure." + type + "." + line);
        }
        return RecipeMachine.builder(MachineCasing.builder(type, "StainlessSteel").machineType(type).tier(2).build())
                .energy(EnergyInput.builder("HU", 512).range(16, maxPacket).faces().build())
                .io(MachineIo.builder().itemInputs().itemOutputs().fluidInputs().fluidOutputs().build())
                .parallel(1)
                .recipeMap(recipe)
                .largeRecipe()
                .cheapOverclocking(true)
                .structureKeys(List.copyOf(keys))
                .build();
    }

    private static StructureLayout distillationLayout(String type, int wallLayers) {
        var layout = StructureLayout.builder(type);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                towerCell(layout, row, 0, col, "heat_transmitter", StructureRole.ENERGY_INPUT);
            }
        }
        for (int layer = 1; layer <= wallLayers; layer++) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (layer == 1 && row == 1 && col == 0) continue;
                    boolean output = row == 1 && col == 2;
                    towerCell(layout, row, layer, col, "distillation_tower_part",
                            output ? StructureRole.ITEM_FLUID_OUTPUT : StructureRole.ITEM_FLUID_INPUT);
                }
            }
        }
        return layout.build();
    }

    /** Source local row grows to the machine's left, so right is {@code 1 - row}. */
    private static void towerCell(StructureLayout.Builder layout, int row, int layer, int col, String part,
            StructureRole role) {
        layout.cell(1 - row, layer - 1, col, CellMatch.part(part), role);
    }

    /**
     * Source 30041. Titanium controller, 2048 EU, packet range 1024 to 16384,
     * parallel 64. Crushed-ore rows are 128 EU. Those rows are not on this map yet.
     * Controller item and fluid outputs are closed; the output wall carries them.
     */
    private static RecipeMachine oreProcessingSystem() {
        List<String> keys = new ArrayList<>();
        for (int line = 1; line <= 4; line++) {
            keys.add("gt6ktfruaddon.structure." + ORE_PROCESS_TYPE + "." + line);
        }
        return RecipeMachine.builder(MachineCasing.builder(ORE_PROCESS_TYPE, "Titanium").machineType(ORE_PROCESS_TYPE).tier(4).build())
                .energy(EnergyInput.builder("EU", 2048).range(1024, 16384).faces(MachineFace.all()).build())
                .io(MachineIo.builder()
                        .itemInputs(MachineFace.TOP)
                        .itemAutoInput(MachineFace.TOP)
                        .itemOutputs()
                        .fluidInputs(MachineFace.TOP)
                        .fluidAutoInput(MachineFace.TOP)
                        .fluidOutputs()
                        .build())
                .parallel(64)
                .recipeMap(ORE_PROCESS)
                .largeRecipe()
                .structureKeys(List.copyOf(keys))
                .build();
    }

    /**
     * Layer rule ABC, offset 0,0,0. Local row grows to the machine's left and
     * local column grows backward, so a cell is {@code (-row, layer, col)}.
     * The controller is layer A, row 0, column 0.
     */
    private static final String[] ORE_BOTTOM = {
            "AAAAA", "AAAAA", "AAAAA", "BBBBB", "BBBBB", "NBBBB"
    };
    private static final String[] ORE_MIDDLE = {
            "CCCCC", "CCCCC", "CCCCC", "BBDDD", "BBDDD", "BBDDD"
    };
    private static final String[] ORE_TOP = {
            "     ", "     ", "     ", "EEDDD", "EEDDD", "EEDDD"
    };

    private static StructureLayout oreProcessingLayout() {
        var layout = StructureLayout.builder(ORE_PROCESS_TYPE);
        String[][] layers = {ORE_BOTTOM, ORE_MIDDLE, ORE_TOP};
        for (int layer = 0; layer < layers.length; layer++) {
            String[] rows = layers[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int col = 0; col < line.length(); col++) {
                    if (layer == 0 && row == 0 && col == 0) continue;
                    char symbol = line.charAt(col);
                    if (symbol == ' ') continue;
                    layout.cell(-row, layer, col, CellMatch.part(orePart(symbol)), oreRole(symbol));
                }
            }
        }
        return layout.build();
    }

    private static String orePart(char symbol) {
        return switch (symbol) {
            case 'A' -> "titanium_wall";
            case 'B', 'N' -> "tungstensteel_wall";
            case 'C' -> "sluice_part";
            case 'D' -> "centrifuge_part";
            case 'E' -> "shredder_blades";
            default -> throw new IllegalStateException("unknown ore structure symbol " + symbol);
        };
    }

    /**
     * Source 30100 / 30101. The 3×3×3 sits one block behind the valve and is
     * aligned to the machine: right and up run -1..1, back runs 0..2. The valve
     * itself is the near-face center, so that cell is absent. The middle cell
     * is air and must be empty. The other 25 cells are the wall named here.
     */
    private static StructureLayout gasTankLayout(String type, String wall) {
        var layout = StructureLayout.builder(type);
        for (int up = -1; up <= 1; up++) {
            for (int right = -1; right <= 1; right++) {
                for (int back = 0; back <= 2; back++) {
                    if (right == 0 && up == 0 && back == 0) continue;
                    if (right == 0 && up == 0 && back == 1) {
                        layout.cell(0, 0, 1, CellMatch.air(), StructureRole.AIR);
                    } else {
                        layout.cell(right, up, back, CellMatch.part(wall), StructureRole.CASING);
                    }
                }
            }
        }
        return layout.build();
    }

    /**
     * The valve has no energy of its own. The declared packet is the API maximum
     * so an ordinary EU cable is below the accepted window and is ignored.
     * Seven tanks sum to {@link #GAS_TANK_CAPACITY}.
     */
    private static BehaviorMachine gasTank(String id, String material) {
        int[] capacity = {
                1_024_000_000, 1_024_000_000, 1_024_000_000, 1_024_000_000,
                1_024_000_000, 1_024_000_000, 768_000_000
        };
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).tier(1).build())
                .energy(EnergyInput.builder("EU", 1_000_000_000_000L).faces(MachineFace.BOTTOM).build())
                .gui("gt6ktfruaddon:textures/gui/machines/gastank.png")
                .energyGauge(8, 8)
                .progress(148, 64);
        for (int tank = 0; tank < capacity.length; tank++) {
            builder.fluid(capacity[tank], 20 + tank * BehaviorMachine.TANK_WIDTH, 8, BehaviorMachine.SlotRole.ANY);
        }
        return builder.build();
    }

    /**
     * Source 21010–21013, 27520–27523, 9800, 10021, 10022, 10026, 10027 and
     * 15000–15055. Tier on a shared machine type selects the row.
     */
    private static void kineticBehaviors(List<AddonMachine> machines) {
        String[] kinetic = {"Wood", "Bronze", "Steel", "Titanium", "Tungstensteel", "Adamantium"};
        String[] kineticId = {"wood", "bronze", "steel", "titanium", "tungstensteel", "adamantium"};
        int[] hopperInput = {64, 32, 128, 512};
        int[] hopperRange = {4, 8, 16, 24};
        for (int i = 0; i < 4; i++) {
            machines.add(voidHopper("void_hopper_" + kineticId[i], kinetic[i], i + 1,
                    hopperInput[i], hopperRange[i]));
        }
        machines.add(miner("miner_tin_alloy", "TinAlloy", 1, "RF", 128));
        machines.add(miner("miner_lv", "SteelGalvanized", 2, "EU", 32));
        machines.add(miner("miner_mv", "Aluminium", 3, "EU", 128));
        machines.add(miner("miner_hv", "StainlessSteel", 4, "EU", 512));
        machines.add(waterMill());
        machines.add(manualMotor("manual_motor_wood", "WoodTreated", 1, "RU", 10));
        machines.add(manualMotor("manual_motor_bronze", "Bronze", 2, "RU", 22));
        machines.add(manualMotor("manual_kinetic_motor_wood", "WoodTreated", 3, "KU", 8));
        machines.add(manualMotor("manual_kinetic_motor_bronze", "Bronze", 4, "KU", 16));
        long[] vmax = {8, 32, 128, 512, 2048, 8192};
        String[] kind = {
                FLYWHEEL, LARGE_FLYWHEEL, ELECTRIC_FLYWHEEL,
                LARGE_ELECTRIC_FLYWHEEL, VACUUM_FLYWHEEL, LARGE_VACUUM_FLYWHEEL
        };
        String[] kindId = {
                "flywheel", "large_flywheel", "electric_flywheel",
                "large_electric_flywheel", "vacuum_flywheel", "large_vacuum_flywheel"
        };
        int[] slots = {1, 4, 1, 4, 1, 4};
        int[] ampere = {4, 16, 4, 16, 4, 16};
        for (int kindIndex = 0; kindIndex < kind.length; kindIndex++) {
            for (int material = 0; material < kinetic.length; material++) {
                machines.add(flywheel(kindId[kindIndex] + "_" + kineticId[material], kinetic[material],
                        kind[kindIndex], material + 1, vmax[material], slots[kindIndex], ampere[kindIndex]));
            }
        }
    }

    /**
     * Source 10200–10255. The back stands in for the inside face. The buffer
     * holds ampere packets and the front emits that packet size.
     */
    private static void transformerParts(List<AddonMachine> machines) {
        String[] material = {
                "SteelGalvanized", "Aluminium", "StainlessSteel", "Chromium", "Titanium", "Iridium"
        };
        String[] electric = {"lv", "mv", "hv", "ev", "iv", "luv"};
        String[] gear = {"t1", "t2", "t3", "t4", "t5", "t6"};
        String[] type = {
                TRANSFORMER_SMALL, TRANSFORMER_LARGE,
                FLYWHEEL_GEAR_SMALL, FLYWHEEL_GEAR_LARGE,
                LIGHT_TRANSFORMER_SMALL, LIGHT_TRANSFORMER_LARGE
        };
        String[] id = {
                "transformer_small", "transformer_large",
                "flywheel_gear_small", "flywheel_gear_large",
                "light_transformer_small", "light_transformer_large"
        };
        String[] energy = {"EU", "EU", "RU", "RU", "LU", "LU"};
        int[] ampere = {4, 16, 4, 16, 4, 16};
        for (int kind = 0; kind < type.length; kind++) {
            String[] suffix = "RU".equals(energy[kind]) ? gear : electric;
            for (int tier = 0; tier < material.length; tier++) {
                machines.add(transformerPart(id[kind] + "_" + suffix[tier], material[tier],
                        type[kind], tier + 1, energy[kind], ampere[kind]));
            }
        }
    }

    private static BehaviorMachine transformerPart(String id, String material, String type, int tier,
                                                   String energy, int ampere) {
        long packet = Gt6KtfruTransformerPart.packet(tier);
        return BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(type).tier(tier).build())
                .energy(EnergyInput.builder(energy, packet).faces(MachineFace.BACK)
                        .capacity(packet * ampere).build())
                .energyOutput(EnergyOutput.builder(energy, packet, ampere).faces(MachineFace.FRONT).build())
                .gui(KINETIC_GUI)
                .toggle("gt6ktfruaddon.transformer.stopped", 152, 8, false)
                .build();
    }

    private static boolean transformerType(String machineType) {
        return TRANSFORMER_SMALL.equals(machineType) || TRANSFORMER_LARGE.equals(machineType)
                || FLYWHEEL_GEAR_SMALL.equals(machineType) || FLYWHEEL_GEAR_LARGE.equals(machineType)
                || LIGHT_TRANSFORMER_SMALL.equals(machineType) || LIGHT_TRANSFORMER_LARGE.equals(machineType);
    }

    private static int transformerAmpere(String machineType) {
        if (TRANSFORMER_LARGE.equals(machineType) || FLYWHEEL_GEAR_LARGE.equals(machineType)
                || LIGHT_TRANSFORMER_LARGE.equals(machineType)) return 16;
        return 4;
    }

    /**
     * Source 10010 / 10011. EU leaves the top. The bottom input exists because a
     * behavior machine needs one. Fuel tanks are 8000 mB. The electrolyte tank
     * is exactly the required amount. Toggle 0 stops processing. Toggle 1
     * opens the electrolyte tank.
     */
    private static BehaviorMachine gasBattery(String id, String material, int tier) {
        long rate = Gt6KtfruFuelBattery.rate(tier);
        int electrolyte = Gt6KtfruFuelBattery.electrolyte(tier);
        return BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(GAS_BATTERY).tier(tier).build())
                .energy(EnergyInput.builder("EU", rate).faces(MachineFace.BOTTOM)
                        .capacity(Gt6KtfruFuelBattery.capacity(rate)).build())
                .energyOutput(EnergyOutput.builder("EU", rate, 1).faces(MachineFace.TOP).build())
                .gui(KINETIC_GUI)
                .progress(8, 64)
                .toggle("gt6ktfruaddon.fuel_battery.stopped", 44, 64, false)
                .toggle("gt6ktfruaddon.fuel_battery.electrolyte", 64, 64, false)
                .item(26, 8, BehaviorMachine.SlotRole.INPUT)
                .item(48, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(8000, 80, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(8000, 98, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(8000, 116, 8, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(8000, 134, 8, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(electrolyte, 152, 8, BehaviorMachine.SlotRole.INPUT)
                .recipeMaps(FUEL_BATTERY)
                .build();
    }

    private static BehaviorMachine voidHopper(String id, String material, int tier, long input, int range) {
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(VOID_HOPPER).tier(tier).build())
                .energy(EnergyInput.builder("RU", input).capacity(input * 2).build())
                .gui(KINETIC_GUI)
                .item(26, 8, BehaviorMachine.SlotRole.INPUT)
                .toggle("gt6ktfruaddon.void_hopper.blacklist", 152, 8, false)
                .toggle("gt6ktfruaddon.void_hopper.clear", 152, 28, false)
                .maxRange(range);
        for (int slot = 0; slot < 9; slot++) {
            builder.item(8 + slot * 18, 64, BehaviorMachine.SlotRole.OUTPUT);
        }
        return builder.build();
    }

    /** Horizontal square stays on the tier. The declared cube is 32 so the dig can go down. */
    private static BehaviorMachine miner(String id, String material, int tier, String energy, long input) {
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(MINER).tier(tier).build())
                .energy(EnergyInput.builder(energy, input).capacity(input * 2).build())
                .gui(KINETIC_GUI)
                .item(26, 8, BehaviorMachine.SlotRole.INPUT)
                .toggle("gt6ktfruaddon.miner.reset", 152, 8, false)
                .maxRange(32);
        for (int slot = 0; slot < 9; slot++) {
            builder.item(8 + slot * 18, 64, BehaviorMachine.SlotRole.OUTPUT);
        }
        return builder.build();
    }

    /**
     * No energy in the source row. The top face is the required input, nominal 2 RU.
     * Output is a packet of 2, one or two amps, front then back.
     */
    private static BehaviorMachine waterMill() {
        return BehaviorMachine.builder(MachineCasing.builder(WATER_MILL, "WoodTreated")
                        .machineType(WATER_MILL).tier(1).build())
                .energy(EnergyInput.builder("RU", 2).faces(MachineFace.TOP).capacity(4).build())
                .energyOutput(EnergyOutput.builder("RU", 2, 2)
                        .faces(MachineFace.FRONT, MachineFace.BACK).build())
                .gui(KINETIC_GUI)
                .build();
    }

    /** Bottom input is required by the behavior machine. The crank emits on the front. */
    private static BehaviorMachine manualMotor(String id, String material, int tier, String energy, long outputMax) {
        return BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(MANUAL_MOTOR).tier(tier).build())
                .energy(EnergyInput.builder(energy, outputMax).faces(MachineFace.BOTTOM)
                        .capacity(outputMax).build())
                .energyOutput(EnergyOutput.builder(energy, outputMax, 1).faces(MachineFace.FRONT).build())
                .gui(KINETIC_GUI)
                .build();
    }

    /** Back in, front out, at {@code vmax}. Capacity 0, so nothing is kept past the tick. */
    private static BehaviorMachine flywheel(String id, String material, String type, int tier,
            long vmax, int slots, int ampere) {
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(type).tier(tier).build())
                .energy(EnergyInput.builder("RU", vmax).faces(MachineFace.BACK).build())
                .energyOutput(EnergyOutput.builder("RU", vmax, ampere).faces(MachineFace.FRONT).build())
                .gui(KINETIC_GUI);
        int x0 = slots == 1 ? 80 : 44;
        for (int slot = 0; slot < slots; slot++) {
            builder.item(x0 + slot * 18, 8, BehaviorMachine.SlotRole.INPUT);
        }
        return builder.build();
    }

    /**
     * Source 22000–22004. Capacity stays at the behavior default, so the buffer
     * holds up to twice the rated KU and the tick spends whatever arrived.
     * The tank is an input: pipes can fill it and cannot pull from it.
     */
    private static BehaviorMachine compressor(String id, String material, int tier, long ku, int tank) {
        return BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(GAS_COMPRESSOR).tier(tier).build())
                .energy(EnergyInput.builder("KU", ku).faces(MachineFace.BOTTOM).build())
                .gui("gt6ktfruaddon:textures/gui/machines/gascompressor.png")
                .fluid(tank, 152, 8, BehaviorMachine.SlotRole.INPUT)
                .build();
    }

    private static CompressedGasTankDeclaration drum(String id, String material, int capacity,
            boolean acid, boolean plasma, boolean magic) {
        return new CompressedGasTankDeclaration(id, material, capacity, acid, plasma, magic);
    }

    /** {@code N} is the only output. Parts with no usage flag are casings. */
    private static StructureRole oreRole(char symbol) {
        return switch (symbol) {
            case 'A', 'B' -> StructureRole.ITEM_FLUID_INPUT;
            case 'N' -> StructureRole.ITEM_FLUID_OUTPUT;
            case 'C', 'D', 'E' -> StructureRole.CASING;
            default -> throw new IllegalStateException("unknown ore structure symbol " + symbol);
        };
    }
}
