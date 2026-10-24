package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.MachineIo;
import com.gregtech.gregtech.api.addon.RecipeMachine;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * Single-block recipe machines from {@code tileEntityInit0}, sources 20000 to 27542: the fluid heaters,
 * light mixers, circuit assemblers, laser cutters, heat and ultrasonic mixers, electronics designers, flaw
 * detectors and wafer testers and coaters, each in its material tiers.
 */
public final class Gt6KtfruSingleBlockMachines implements Feature {
    private static final String[] ELECTRIC = {
            "", "SteelGalvanized", "Aluminium", "StainlessSteel", "Chromium", "Titanium"
    };
    private static final long[] VOLTAGE = {0, 32, 128, 512, 2048, 8192};
    private static final String[] TIER = {"", "lv", "mv", "hv", "ev", "iv"};

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.FLUID_HEATING, "Fluid Heating", "fluidheating", 0, 0, 0, 1, 1, 1, 1));
        maps.add(RecipeMaps.table(RecipeMaps.LIGHT_MIXER, "Light Mixer", "lightmixer", 6, 3, 0, 6, 6, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.CIRCUIT_ASSEMBLER, "Circuit Assembler", "circuitassembler", 9, 3, 1, 1, 0, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.LASER_CUTTER, "Laser Cutter", "lasercutter", 2, 3, 1, 1, 0, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.HEAT_MIXER, "Heat Mixer", "heatmixer", 6, 3, 0, 6, 3, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.ELECTRONICS_DESIGNER, "Electronics Designer", "electronicsdesigner", 6, 3, 1, 3, 0, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.FLAW_DETECTOR, "Flaw Detector", "flawdetector", 3, 3, 1, 0, 0, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.WAFER_TESTER, "Wafer Tester", "wafertester", 3, 3, 0, 0, 0, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.WAFER_COATER, "Wafer Coater", "wafercoater", 3, 3, 0, 3, 0, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.ULTRASONIC_MIXER, "Ultrasonic Mixer", "ultrasonicmixer", 6, 3, 0, 6, 2, 0, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        heat(machines, "fluid_heater", "fluidheater", "HU", RecipeMaps.FLUID_HEATING,
                "Steel", "Invar", "Titanium", "TungstenCarbide");
        kinetic(machines, "light_mixer", "lightmixer", RecipeMaps.LIGHT_MIXER, new int[] {4, 8, 16, 32},
                "Bronze", "Steel", "Titanium", "Tungstensteel");
        electric(machines, "circuit_assembler", "circuitassembler", "LU", RecipeMaps.CIRCUIT_ASSEMBLER, 1, 5, null);
        electric(machines, "laser_cutter", "lasercutter", "LU", RecipeMaps.LASER_CUTTER, 1, 5, null);
        electric(machines, "heat_mixer", "heatmixer", "EU", RecipeMaps.HEAT_MIXER, 1, 5, null);
        electric(machines, "electronics_designer", "eda", "EU", RecipeMaps.ELECTRONICS_DESIGNER, 1, 5, null);
        electric(machines, "flaw_detector", "flawdetector", "EU", RecipeMaps.FLAW_DETECTOR, 1, 5, null);
        electric(machines, "electric_light_mixer", "lightmixerelectric", "EU", RecipeMaps.LIGHT_MIXER, 1, 5,
                new int[] {4, 8, 16, 32, 64});
        electric(machines, "wafer_tester", "wafertester", "EU", RecipeMaps.WAFER_TESTER, 1, 5, null);
        electric(machines, "wafer_coater", "wafercoater", "EU", RecipeMaps.WAFER_COATER, 1, 5, null);
        electric(machines, "ultrasonic_mixer", "ultrasonicmixer", "EU", RecipeMaps.ULTRASONIC_MIXER, 3, 5, null);
        return List.copyOf(machines);
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
}
