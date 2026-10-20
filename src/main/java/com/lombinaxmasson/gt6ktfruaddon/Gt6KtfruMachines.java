package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachineContributor;
import com.gregtech.gregtech.api.addon.FuelGeneratorDeclaration;
import com.gregtech.gregtech.api.addon.FuelMapDeclaration;
import com.gregtech.gregtech.api.addon.MachineDeclaration;
import com.gregtech.gregtech.api.addon.MachineLayout;
import com.gregtech.gregtech.api.addon.RecipeMapDeclaration;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * Single-block machines from {@code tileEntityInit0} 20000–27542. The gas
 * compressor fills a compressed-gas tank and is not registered. The placeholder
 * crystal puller is gone. The hydrogen burner stays until card 03.
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
        return List.copyOf(machines);
    }

    @Override
    public List<MachineLayout> layouts() {
        return List.of(new MachineLayout("hydrogen_burner", List.of(
                new MachineLayout.Cell(-1, 0, 1, "tank_wall", StructureRole.FLUID_INPUT),
                new MachineLayout.Cell(0, 0, 1, "tank_wall", StructureRole.FLUID_INPUT),
                new MachineLayout.Cell(1, 0, 1, "tank_wall", StructureRole.FLUID_INPUT))));
    }

    @Override
    public List<FuelMapDeclaration> fuelMaps() {
        return List.of(new FuelMapDeclaration(
                "gt6ktfruaddon.fuel.hydrogen",
                "Hydrogen Burner",
                "gt6ktfruaddon:textures/gui/machines/burner",
                0, 0, 0,
                1, 1, 1, 1,
                false, false, false, false));
    }

    @Override
    public List<FuelGeneratorDeclaration> fuelGenerators() {
        return List.of(FuelGeneratorDeclaration.builder("hydrogen_burner", "Steel")
                .machineType("hydrogen_burner")
                .fuelMap("gt6ktfruaddon.fuel.hydrogen")
                .efficiency(10_000)
                .energy("RU", 32)
                .tank(16_000)
                .outputBehind(2)
                .multi()
                .build());
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

    private static MachineDeclaration basic(String id, String material, String type, String energy, long perTick,
            int tier, int parallel, String recipeMap) {
        return MachineDeclaration.builder(id, material)
                .machineType(type)
                .energy(energy, perTick)
                .tier(tier)
                .parallel(parallel)
                .recipeMap(recipeMap)
                .basic()
                .build();
    }
}
