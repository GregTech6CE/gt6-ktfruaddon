package com.lombinaxmasson.gt6ktfruaddon.processing;

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
public final class SingleBlockMachines implements Feature {
    /** The voltage of tiers 1 to 5, from the first entry. */
    private static final long[] VOLTAGE = {32, 128, 512, 2048, 8192};

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

    /** The tiers most electric families run in. */
    private static final List<Tier> ELECTRIC = List.of(
            new Tier(1, "lv", "SteelGalvanized"),
            new Tier(2, "mv", "Aluminium"),
            new Tier(3, "hv", "StainlessSteel"),
            new Tier(4, "ev", "Chromium"),
            new Tier(5, "iv", "Titanium"));

    /** The families in the order their machines are declared. */
    private static final List<Family> FAMILIES = List.of(
            new Family("fluid_heater", "fluidheater", "HU", RecipeMaps.FLUID_HEATING, FLUID_HEATER, List.of(
                    new Tier(1, "steel", "Steel"),
                    new Tier(2, "invar", "Invar"),
                    new Tier(3, "titanium", "Titanium"),
                    new Tier(4, "tungsten_carbide", "TungstenCarbide"))),
            new Family("light_mixer", "lightmixer", "RU", RecipeMaps.LIGHT_MIXER, KINETIC_LIGHT_MIXER, List.of(
                    new Tier(1, "bronze", "Bronze", 4),
                    new Tier(2, "steel", "Steel", 8),
                    new Tier(3, "titanium", "Titanium", 16),
                    new Tier(4, "tungstensteel", "Tungstensteel", 32))),
            new Family("circuit_assembler", "circuitassembler", "LU", RecipeMaps.CIRCUIT_ASSEMBLER,
                    CIRCUIT_ASSEMBLER, ELECTRIC),
            new Family("laser_cutter", "lasercutter", "LU", RecipeMaps.LASER_CUTTER, LASER_CUTTER, ELECTRIC),
            new Family("heat_mixer", "heatmixer", "EU", RecipeMaps.HEAT_MIXER, MIXER, ELECTRIC),
            new Family("electronics_designer", "eda", "EU", RecipeMaps.ELECTRONICS_DESIGNER, DESIGNER, ELECTRIC),
            new Family("flaw_detector", "flawdetector", "EU", RecipeMaps.FLAW_DETECTOR, FLAW_DETECTOR, ELECTRIC),
            new Family("electric_light_mixer", "lightmixerelectric", "EU", RecipeMaps.LIGHT_MIXER, LIGHT_MIXER, List.of(
                    new Tier(1, "lv", "SteelGalvanized", 4),
                    new Tier(2, "mv", "Aluminium", 8),
                    new Tier(3, "hv", "StainlessSteel", 16),
                    new Tier(4, "ev", "Chromium", 32),
                    new Tier(5, "iv", "Titanium", 64))),
            new Family("wafer_tester", "wafertester", "EU", RecipeMaps.WAFER_TESTER, WAFER_TESTER, ELECTRIC),
            new Family("wafer_coater", "wafercoater", "EU", RecipeMaps.WAFER_COATER, WAFER, ELECTRIC),
            new Family("ultrasonic_mixer", "ultrasonicmixer", "EU", RecipeMaps.ULTRASONIC_MIXER, MIXER,
                    ELECTRIC.subList(2, 5)));

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
        for (Family family : FAMILIES) {
            for (Tier tier : family.tiers()) machines.add(basic(family, tier));
        }
        return List.copyOf(machines);
    }

    private static RecipeMachine basic(Family family, Tier tier) {
        var energyInput = EnergyInput.builder(family.energy(), voltage(tier.number()));
        var io = MachineIo.builder();
        var recipe = RecipeMachine.builder(MachineCasing.builder(family.prefix() + "_" + tier.suffix(), tier.material())
                        .machineType(family.type()).tier(tier.number()).build())
                .recipeMap(family.recipeMap())
                .basic()
                .parallel(tier.parallel());
        family.sides().apply(energyInput, io, recipe);
        return recipe.energy(energyInput.build()).io(io.build()).build();
    }

    private static long voltage(int tier) {
        return VOLTAGE[tier - 1];
    }

    private static Channel on(MachineFace... faces) {
        return new Channel(List.of(faces), null);
    }

    /** One machine of a family: its tier, the suffix of its id, its material and how many recipes it runs at once. */
    private record Tier(int number, String suffix, String material, int parallel) {
        Tier(int number, String suffix, String material) {
            this(number, suffix, material, 1);
        }
    }

    /** The machines of one kind on one recipe map, one per tier, with the faces they share. */
    private record Family(String prefix, String type, String energy, String recipeMap, SideProfile sides,
            List<Tier> tiers) {}

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
