package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachineContributor;
import com.gregtech.gregtech.api.addon.BehaviorMachineDeclaration.Face;
import com.gregtech.gregtech.api.addon.FuelGeneratorDeclaration;
import com.gregtech.gregtech.api.addon.MachineDeclaration;
import com.gregtech.gregtech.api.addon.MachineLayout;
import com.gregtech.gregtech.api.addon.RecipeMapDeclaration;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * Single-block machines from {@code tileEntityInit0}. Recipe machines are
 * 20000–27542. Fuel generators are the gas turbines and diesel engines from
 * 10000–10160. The large recipe machines are the three distillation towers and
 * the ore processing system.
 * The gas compressor and the gas battery are not registered.
 */
public final class Gt6KtfruMachines implements AddonMachineContributor {
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
    /** Layout name and machine type. The controller id uses the same string. */
    public static final String ORE_PROCESS_TYPE = "ore_processing_system";
    /** Built-in GT fuel map behind {@code FM.Gas}. Already contains hydrogen. */
    public static final String GAS_FUEL = "gt.recipe.fuels.gas";
    /** Built-in GT fuel map behind {@code FM.Engine}. Already contains diesel. */
    public static final String ENGINE_FUEL = "gt.recipe.fuels.engine";

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
    public List<RecipeMapDeclaration> recipeMaps() {
        List<RecipeMapDeclaration> maps = new ArrayList<>();
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
        return List.copyOf(maps);
    }

    @Override
    public List<MachineDeclaration> machines() {
        List<MachineDeclaration> machines = new ArrayList<>();
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
        machines.add(distillationTower(TINY_DISTILL_TYPE, TINY_DISTILL));
        machines.add(distillationTower(SMALL_DISTILL_TYPE, SMALL_DISTILL));
        machines.add(distillationTower(DISTILL_TYPE, DISTILL));
        machines.add(oreProcessingSystem());
        return List.copyOf(machines);
    }

    @Override
    public List<MachineLayout> layouts() {
        return List.of(
                distillationLayout(TINY_DISTILL_TYPE, 4),
                distillationLayout(SMALL_DISTILL_TYPE, 6),
                distillationLayout(DISTILL_TYPE, 9),
                oreProcessingLayout());
    }

    @Override
    public List<FuelGeneratorDeclaration> fuelGenerators() {
        List<FuelGeneratorDeclaration> generators = new ArrayList<>();
        // The fuel signature includes the output rate, so each block keeps its own machine type.
        gas(generators, "bronze", "Bronze", 16);
        gas(generators, "steel", "Steel", 32);
        gas(generators, "invar", "Invar", 64);
        gas(generators, "titanium", "Titanium", 128);
        gas(generators, "tungstensteel", "Tungstensteel", 372);
        gas(generators, "iridium", "Iridium", 768);
        gas(generators, "chromium", "Chromium", 256);
        diesel(generators, "diesel", "bronze", "Bronze", 3300, 16);
        diesel(generators, "diesel", "arsenic_copper", "ArsenicCopper", 3300, 16);
        diesel(generators, "diesel", "arsenic_bronze", "ArsenicBronze", 3300, 24);
        diesel(generators, "diesel", "steel", "Steel", 3300, 32);
        diesel(generators, "diesel", "invar", "Invar", 3300, 64);
        diesel(generators, "diesel", "titanium", "Titanium", 3300, 128);
        diesel(generators, "diesel", "tungstensteel", "Tungstensteel", 3300, 372);
        diesel(generators, "diesel", "iridium", "Iridium", 3300, 768);
        diesel(generators, "diesel", "chromium", "Chromium", 3300, 256);
        diesel(generators, "turbo_diesel", "bronze", "Bronze", 5200, 24);
        diesel(generators, "turbo_diesel", "arsenic_copper", "ArsenicCopper", 5200, 24);
        diesel(generators, "turbo_diesel", "arsenic_bronze", "ArsenicBronze", 5200, 36);
        diesel(generators, "turbo_diesel", "steel", "Steel", 5200, 48);
        diesel(generators, "turbo_diesel", "invar", "Invar", 5200, 96);
        diesel(generators, "turbo_diesel", "titanium", "Titanium", 5200, 172);
        diesel(generators, "turbo_diesel", "tungstensteel", "Tungstensteel", 5200, 558);
        diesel(generators, "turbo_diesel", "iridium", "Iridium", 5200, 1152);
        diesel(generators, "turbo_diesel", "chromium", "Chromium", 5200, 384);
        diesel(generators, "turbo_diesel", "osmium", "Osmium", 5000, 2304);
        diesel(generators, "turbo_diesel", "trinaquadalloy", "Trinaquadalloy", 4800, 4608);
        return List.copyOf(generators);
    }

    private static RecipeMapDeclaration map(String name, String title, String gui,
            int inputItems, int outputItems, int minimalInputItems,
            int inputFluids, int outputFluids, int minimalInputFluids, int minimalInputs) {
        return new RecipeMapDeclaration(
                name, title, "gt6ktfruaddon:textures/gui/machines/" + gui,
                inputItems, outputItems, minimalInputItems,
                inputFluids, outputFluids, minimalInputFluids, minimalInputs,
                true, false, true, true);
    }

    private static void heat(List<MachineDeclaration> out, String prefix, String type, String energy, String recipeMap,
            String... materials) {
        String[] suffix = {"steel", "invar", "titanium", "tungsten_carbide"};
        for (int i = 0; i < materials.length; i++) {
            out.add(basic(prefix + "_" + suffix[i], materials[i], type, energy, VOLTAGE[i + 1], i + 1, 1, recipeMap));
        }
    }

    private static void kinetic(List<MachineDeclaration> out, String prefix, String type, String recipeMap, int[] parallel,
            String... materials) {
        String[] suffix = {"bronze", "steel", "titanium", "tungstensteel"};
        for (int i = 0; i < materials.length; i++) {
            out.add(basic(prefix + "_" + suffix[i], materials[i], type, "RU", VOLTAGE[i + 1], i + 1, parallel[i], recipeMap));
        }
    }

    private static void electric(List<MachineDeclaration> out, String prefix, String type, String energy, String recipeMap,
            int first, int last, int[] parallel) {
        for (int tier = first; tier <= last; tier++) {
            int count = parallel == null ? 1 : parallel[tier - first];
            out.add(basic(prefix + "_" + TIER[tier], ELECTRIC[tier], type, energy, VOLTAGE[tier], tier, count, recipeMap));
        }
    }

    private static void gas(List<FuelGeneratorDeclaration> out, String suffix, String material, long perTick) {
        out.add(fuel("gas_turbine_" + suffix, material, "gas_turbine_" + suffix, GAS_FUEL, 3500, perTick));
    }

    private static void diesel(List<FuelGeneratorDeclaration> out, String prefix, String suffix, String material,
            int efficiency, long perTick) {
        String id = prefix + "_" + suffix;
        out.add(fuel(id, material, id, ENGINE_FUEL, efficiency, perTick));
    }

    private static FuelGeneratorDeclaration fuel(String id, String material, String type, String fuelMap,
            int efficiency, long perTick) {
        return FuelGeneratorDeclaration.builder(id, material)
                .machineType(type)
                .fuelMap(fuelMap)
                .efficiency(efficiency)
                .energy("RU", perTick)
                // Vanilla MotorLiquid sizes both tanks at rate * 10. The TFRU fork uses 16 and 128;
                // this template has one capacity for the fuel tank and every exhaust tank.
                .tank(Math.toIntExact(perTick * 10))
                .outputFront()
                .single()
                .build();
    }

    private static MachineDeclaration basic(String id, String material, String type, String energy, long perTick,
            int tier, int parallel, String recipeMap) {
        var builder = MachineDeclaration.builder(id, material)
                .machineType(type)
                .energy(energy, perTick)
                .tier(tier)
                .parallel(parallel)
                .recipeMap(recipeMap)
                .basic();
        sides(builder, id);
        return builder.build();
    }

    /**
     * Faces follow {@code tileEntityInit0} names, not the raw GT6 bit index.
     * {@code SBIT_I} and {@code SIDE_INSIDE} are no external face. The port's
     * {@link Face#LEFT} is the machine's left.
     */
    private static void sides(MachineDeclaration.Builder builder, String id) {
        if (id.startsWith("fluid_heater_")) {
            builder.itemInputs().itemOutputs();
            builder.fluidInputs(Face.BACK).fluidAutoInput(Face.BACK);
            builder.fluidOutputs(Face.TOP).fluidAutoOutput(Face.TOP);
            builder.energyInputs(Face.BOTTOM).cheapOverclocking(true);
            return;
        }
        if (id.startsWith("light_mixer_") || id.startsWith("electric_light_mixer_")) {
            builder.itemInputs(Face.LEFT, Face.FRONT).itemAutoInput(Face.LEFT);
            builder.itemOutputs(Face.RIGHT, Face.BACK).itemAutoOutput(Face.RIGHT);
            builder.fluidInputs(Face.LEFT, Face.FRONT).fluidAutoInput(Face.FRONT);
            builder.fluidOutputs(Face.RIGHT, Face.BACK).fluidAutoOutput(Face.BACK);
            builder.energyInputs(Face.BOTTOM);
            // Only the kinetic LightMixer checks the block above. The electric one does not.
            if (id.startsWith("light_mixer_")) builder.minimumLightAbove(14);
            return;
        }
        if (id.startsWith("circuit_assembler_")) {
            builder.itemInputs(Face.LEFT).itemAutoInput(Face.LEFT);
            builder.itemOutputs(Face.RIGHT).itemAutoOutput(Face.RIGHT);
            builder.fluidInputs(Face.BOTTOM, Face.LEFT).fluidAutoInput(Face.BOTTOM);
            builder.energyInputs(Face.TOP);
            return;
        }
        if (id.startsWith("laser_cutter_")) {
            builder.itemInputs(Face.LEFT).itemAutoInput(Face.LEFT);
            builder.itemOutputs(Face.RIGHT).itemAutoOutput(Face.RIGHT);
            // Source auto is the top, which is not one of the input faces.
            builder.fluidInputs(Face.BOTTOM, Face.LEFT).fluidAutoInput(Face.TOP);
            builder.energyInputs(Face.TOP);
            return;
        }
        if (id.startsWith("heat_mixer_") || id.startsWith("ultrasonic_mixer_")) {
            builder.itemInputs(Face.LEFT, Face.TOP).itemAutoInput(Face.LEFT);
            builder.itemOutputs(Face.RIGHT, Face.BACK).itemAutoOutput(Face.RIGHT);
            builder.fluidInputs(Face.LEFT, Face.TOP).fluidAutoInput(Face.TOP);
            builder.fluidOutputs(Face.RIGHT, Face.BACK).fluidAutoOutput(Face.BACK);
            builder.energyInputs(Face.BOTTOM);
            return;
        }
        if (id.startsWith("electronics_designer_") || id.startsWith("flaw_detector_")) {
            builder.itemInputs(Face.LEFT).itemAutoInput(Face.LEFT);
            builder.itemOutputs(Face.RIGHT).itemAutoOutput(Face.RIGHT);
            builder.fluidInputs(Face.TOP).fluidAutoInput(Face.TOP);
            builder.energyInputs(Face.BACK).cheapOverclocking(true);
            return;
        }
        if (id.startsWith("wafer_tester_") || id.startsWith("wafer_coater_")) {
            builder.itemInputs(Face.TOP, Face.LEFT).itemAutoInput(Face.LEFT);
            builder.itemOutputs(Face.RIGHT, Face.BOTTOM).itemAutoOutput(Face.RIGHT);
            builder.fluidInputs(Face.TOP, Face.LEFT).fluidAutoInput(Face.TOP);
            builder.fluidOutputs(Face.RIGHT, Face.BOTTOM).fluidAutoOutput(Face.BOTTOM);
            builder.energyInputs(Face.BACK);
            if (id.startsWith("wafer_tester_")) builder.cheapOverclocking(true);
            return;
        }
        throw new IllegalStateException("No side mask for " + id);
    }

    /**
     * Sources 30004, 30005 and 30006. One 3×3 heat layer, then four, six or nine
     * 3×3 distillation layers. The controller sits in the front-middle of the
     * lowest distillation layer and is not a cell. Output hatches are the
     * back-middle column. Coordinates are the layout's right, up and back.
     */
    private static MachineDeclaration distillationTower(String type, String recipe) {
        List<String> keys = new ArrayList<>();
        for (int line = 1; line <= 6; line++) {
            keys.add("gt6ktfruaddon.structure." + type + "." + line);
        }
        return MachineDeclaration.builder(type, "StainlessSteel")
                .machineType(type)
                // Source nominal is 512 HU. The declaration only exposes half to
                // double, so 128 keeps the 64 HU ocean row inside.
                .energy("HU", 128)
                .tier(2)
                .parallel(1)
                .recipeMap(recipe)
                .largeRecipe()
                .cheapOverclocking(true)
                .itemInputs()
                .itemOutputs()
                .fluidInputs()
                .fluidOutputs()
                .energyInputs()
                .structureKeys(List.copyOf(keys))
                .build();
    }

    private static MachineLayout distillationLayout(String type, int wallLayers) {
        List<MachineLayout.Cell> cells = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cells.add(towerCell(row, 0, col, "heat_transmitter", StructureRole.ENERGY_INPUT));
            }
        }
        for (int layer = 1; layer <= wallLayers; layer++) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (layer == 1 && row == 1 && col == 0) continue;
                    boolean output = row == 1 && col == 2;
                    cells.add(towerCell(row, layer, col, "distillation_tower_part",
                            output ? StructureRole.ITEM_FLUID_OUTPUT : StructureRole.ITEM_FLUID_INPUT));
                }
            }
        }
        return new MachineLayout(type, List.copyOf(cells));
    }

    /** Source local row grows to the machine's left, so right is {@code 1 - row}. */
    private static MachineLayout.Cell towerCell(int row, int layer, int col, String part, StructureRole role) {
        return new MachineLayout.Cell(1 - row, layer - 1, col, part, role);
    }

    /**
     * Source 30041. Titanium controller, 2048 EU, parallel 64. The declaration
     * window is half to double, so the upper bound is 4096 rather than 16384.
     * Crushed-ore rows are 128 EU and need each ore's byproducts, which the
     * material API does not expose, so this map has no rows yet.
     */
    private static MachineDeclaration oreProcessingSystem() {
        List<String> keys = new ArrayList<>();
        for (int line = 1; line <= 4; line++) {
            keys.add("gt6ktfruaddon.structure." + ORE_PROCESS_TYPE + "." + line);
        }
        return MachineDeclaration.builder(ORE_PROCESS_TYPE, "Titanium")
                .machineType(ORE_PROCESS_TYPE)
                .energy("EU", 2048)
                .tier(4)
                .parallel(64)
                .recipeMap(ORE_PROCESS)
                .largeRecipe()
                .itemInputs(Face.TOP)
                .itemAutoInput(Face.TOP)
                .itemOutputs()
                .fluidInputs(Face.TOP)
                .fluidAutoInput(Face.TOP)
                .fluidOutputs()
                .energyInputs(Face.BOTTOM, Face.TOP, Face.FRONT, Face.BACK, Face.LEFT, Face.RIGHT)
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

    private static MachineLayout oreProcessingLayout() {
        List<MachineLayout.Cell> cells = new ArrayList<>();
        String[][] layers = {ORE_BOTTOM, ORE_MIDDLE, ORE_TOP};
        for (int layer = 0; layer < layers.length; layer++) {
            String[] rows = layers[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int col = 0; col < line.length(); col++) {
                    if (layer == 0 && row == 0 && col == 0) continue;
                    char symbol = line.charAt(col);
                    if (symbol == ' ') continue;
                    cells.add(new MachineLayout.Cell(-row, layer, col, orePart(symbol), oreRole(symbol)));
                }
            }
        }
        return new MachineLayout(ORE_PROCESS_TYPE, List.copyOf(cells));
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
