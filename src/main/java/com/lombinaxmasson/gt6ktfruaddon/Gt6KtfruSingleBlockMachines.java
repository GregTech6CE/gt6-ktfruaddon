package com.lombinaxmasson.gt6ktfruaddon;

import static com.gregtech.gregtech.api.addon.MachineFace.BACK;
import static com.gregtech.gregtech.api.addon.MachineFace.BOTTOM;
import static com.gregtech.gregtech.api.addon.MachineFace.FRONT;
import static com.gregtech.gregtech.api.addon.MachineFace.LEFT;
import static com.gregtech.gregtech.api.addon.MachineFace.RIGHT;
import static com.gregtech.gregtech.api.addon.MachineFace.TOP;

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

    // The faces of each family. They follow the tileEntityInit0 names, not the raw GT6 bit index: SBIT_I and
    // SIDE_INSIDE are no external face, LEFT is the machine's left, and a channel with no face is closed.
    private static final Channel CLOSED = new Channel(List.of(), null);
    private static final SideProfile FLUID_HEATER = new SideProfile(CLOSED, CLOSED,
            on(BACK).auto(BACK), on(TOP).auto(TOP), BOTTOM).cheap();
    private static final SideProfile LIGHT_MIXER = new SideProfile(
            on(LEFT, FRONT).auto(LEFT), on(RIGHT, BACK).auto(RIGHT),
            on(LEFT, FRONT).auto(FRONT), on(RIGHT, BACK).auto(BACK), BOTTOM);
    // Only the kinetic LightMixer checks the block above. The electric one does not.
    private static final SideProfile KINETIC_LIGHT_MIXER = LIGHT_MIXER.lightAbove(14);
    private static final SideProfile CIRCUIT_ASSEMBLER = new SideProfile(
            on(LEFT).auto(LEFT), on(RIGHT).auto(RIGHT), on(BOTTOM, LEFT).auto(BOTTOM), CLOSED, TOP);
    // Source auto is the top, which is not one of the input faces.
    private static final SideProfile LASER_CUTTER = new SideProfile(
            on(LEFT).auto(LEFT), on(RIGHT).auto(RIGHT), on(BOTTOM, LEFT).auto(TOP), CLOSED, TOP);
    private static final SideProfile MIXER = new SideProfile(
            on(LEFT, TOP).auto(LEFT), on(RIGHT, BACK).auto(RIGHT),
            on(LEFT, TOP).auto(TOP), on(RIGHT, BACK).auto(BACK), BOTTOM);
    private static final SideProfile DESIGNER = new SideProfile(
            on(LEFT).auto(LEFT), on(RIGHT).auto(RIGHT), on(TOP).auto(TOP), CLOSED, BACK).cheap();
    // Source FlawDetector keeps NBT_USE_OUTPUT_TANK and rolls the first item
    // output's chance once: hit for output 0, otherwise output 1.
    private static final SideProfile FLAW_DETECTOR = DESIGNER.exclusive();
    private static final SideProfile WAFER = new SideProfile(
            on(TOP, LEFT).auto(LEFT), on(RIGHT, BOTTOM).auto(RIGHT),
            on(TOP, LEFT).auto(TOP), on(RIGHT, BOTTOM).auto(BOTTOM), BACK);
    private static final SideProfile WAFER_TESTER = WAFER.cheap();

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
        heat(machines, "fluid_heater", "fluidheater", "HU", RecipeMaps.FLUID_HEATING, FLUID_HEATER,
                "Steel", "Invar", "Titanium", "TungstenCarbide");
        kinetic(machines, "light_mixer", "lightmixer", RecipeMaps.LIGHT_MIXER, KINETIC_LIGHT_MIXER, new int[] {4, 8, 16, 32},
                "Bronze", "Steel", "Titanium", "Tungstensteel");
        electric(machines, "circuit_assembler", "circuitassembler", "LU", RecipeMaps.CIRCUIT_ASSEMBLER,
                CIRCUIT_ASSEMBLER, 1, 5, null);
        electric(machines, "laser_cutter", "lasercutter", "LU", RecipeMaps.LASER_CUTTER, LASER_CUTTER, 1, 5, null);
        electric(machines, "heat_mixer", "heatmixer", "EU", RecipeMaps.HEAT_MIXER, MIXER, 1, 5, null);
        electric(machines, "electronics_designer", "eda", "EU", RecipeMaps.ELECTRONICS_DESIGNER, DESIGNER, 1, 5, null);
        electric(machines, "flaw_detector", "flawdetector", "EU", RecipeMaps.FLAW_DETECTOR, FLAW_DETECTOR, 1, 5, null);
        electric(machines, "electric_light_mixer", "lightmixerelectric", "EU", RecipeMaps.LIGHT_MIXER, LIGHT_MIXER, 1, 5,
                new int[] {4, 8, 16, 32, 64});
        electric(machines, "wafer_tester", "wafertester", "EU", RecipeMaps.WAFER_TESTER, WAFER_TESTER, 1, 5, null);
        electric(machines, "wafer_coater", "wafercoater", "EU", RecipeMaps.WAFER_COATER, WAFER, 1, 5, null);
        electric(machines, "ultrasonic_mixer", "ultrasonicmixer", "EU", RecipeMaps.ULTRASONIC_MIXER, MIXER, 3, 5, null);
        return List.copyOf(machines);
    }

    private static void heat(List<AddonMachine> out, String prefix, String type, String energy, String recipeMap,
            SideProfile sides, String... materials) {
        String[] suffix = {"steel", "invar", "titanium", "tungsten_carbide"};
        for (int i = 0; i < materials.length; i++) {
            out.add(basic(prefix + "_" + suffix[i], materials[i], type, energy, VOLTAGE[i + 1], i + 1, 1, recipeMap, sides));
        }
    }

    private static void kinetic(List<AddonMachine> out, String prefix, String type, String recipeMap, SideProfile sides,
            int[] parallel, String... materials) {
        String[] suffix = {"bronze", "steel", "titanium", "tungstensteel"};
        for (int i = 0; i < materials.length; i++) {
            out.add(basic(prefix + "_" + suffix[i], materials[i], type, "RU", VOLTAGE[i + 1], i + 1, parallel[i], recipeMap,
                    sides));
        }
    }

    private static void electric(List<AddonMachine> out, String prefix, String type, String energy, String recipeMap,
            SideProfile sides, int first, int last, int[] parallel) {
        for (int tier = first; tier <= last; tier++) {
            int count = parallel == null ? 1 : parallel[tier - first];
            out.add(basic(prefix + "_" + TIER[tier], ELECTRIC[tier], type, energy, VOLTAGE[tier], tier, count, recipeMap,
                    sides));
        }
    }

    private static RecipeMachine basic(String id, String material, String type, String energy, long perTick,
            int tier, int parallel, String recipeMap, SideProfile sides) {
        var energyInput = EnergyInput.builder(energy, perTick);
        var io = MachineIo.builder();
        var recipe = RecipeMachine.builder(MachineCasing.builder(id, material).machineType(type).tier(tier).build())
                .recipeMap(recipeMap)
                .basic()
                .parallel(parallel);
        sides.apply(energyInput, io, recipe);
        return recipe.energy(energyInput.build()).io(io.build()).build();
    }

    private static Channel on(MachineFace... faces) {
        return new Channel(List.of(faces), null);
    }

    /** The faces one channel is open on, and the face it moves items or fluid through by itself, if any. */
    private record Channel(List<MachineFace> faces, MachineFace automatic) {
        Channel auto(MachineFace face) {
            return new Channel(faces, face);
        }

        MachineFace[] faceArray() {
            return faces.toArray(MachineFace[]::new);
        }
    }

    /**
     * What one family of machines declares about its faces: the four item and fluid channels, the energy face and
     * the recipe flags that differ between families. A MachineIo channel that is not named is an empty face list,
     * the same as one named with no face, so every profile names all four.
     */
    private record SideProfile(Channel itemIn, Channel itemOut, Channel fluidIn, Channel fluidOut, MachineFace energyFace,
            boolean cheapOverclocking, int minimumLightAbove, boolean exclusiveOutputs) {
        SideProfile(Channel itemIn, Channel itemOut, Channel fluidIn, Channel fluidOut, MachineFace energyFace) {
            this(itemIn, itemOut, fluidIn, fluidOut, energyFace, false, 0, false);
        }

        SideProfile cheap() {
            return new SideProfile(itemIn, itemOut, fluidIn, fluidOut, energyFace, true, minimumLightAbove, exclusiveOutputs);
        }

        SideProfile lightAbove(int level) {
            return new SideProfile(itemIn, itemOut, fluidIn, fluidOut, energyFace, cheapOverclocking, level, exclusiveOutputs);
        }

        /** The machine searches its output tanks and rolls one output or the other, not both. */
        SideProfile exclusive() {
            return new SideProfile(itemIn, itemOut, fluidIn, fluidOut, energyFace, cheapOverclocking, minimumLightAbove, true);
        }

        void apply(EnergyInput.Builder energy, MachineIo.Builder io, RecipeMachine.Builder recipe) {
            io.itemInputs(itemIn.faceArray());
            if (itemIn.automatic() != null) io.itemAutoInput(itemIn.automatic());
            io.itemOutputs(itemOut.faceArray());
            if (itemOut.automatic() != null) io.itemAutoOutput(itemOut.automatic());
            io.fluidInputs(fluidIn.faceArray());
            if (fluidIn.automatic() != null) io.fluidAutoInput(fluidIn.automatic());
            io.fluidOutputs(fluidOut.faceArray());
            if (fluidOut.automatic() != null) io.fluidAutoOutput(fluidOut.automatic());
            energy.faces(energyFace);
            if (cheapOverclocking) recipe.cheapOverclocking(true);
            if (minimumLightAbove > 0) recipe.minimumLightAbove(minimumLightAbove);
            if (exclusiveOutputs) recipe.searchOutputTanks(true).exclusiveOutputs(true);
        }
    }
}
