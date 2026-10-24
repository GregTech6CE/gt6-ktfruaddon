package com.lombinaxmasson.gt6ktfruaddon.recipe;

import com.gregtech.gregtech.api.addon.RecipeTable;

/**
 * The names of the recipe maps this addon declares, and the two GT fuel maps its generators burn
 * from. A class that adds machines or recipes names a map through here, so none of them has to
 * depend on the machine declarations for a string.
 */
public final class RecipeMaps {
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
    public static final String ORE_PROCESS = "gt6ktfruaddon.recipe.ore_processing_system";
    public static final String MASK_ALIGNER = "gt6ktfruaddon.recipe.mask_aligner";
    public static final String CNC = "gt6ktfruaddon.recipe.cnc";
    public static final String FUEL_DEBURN = "gt6ktfruaddon.recipe.fuel_deburner";
    public static final String FUSION_TOKAMAK = "gt6ktfruaddon.recipe.fusion_tokamak";
    public static final String FUSION_TOKAMAK_EXPERIMENTAL = "gt6ktfruaddon.recipe.fusion_tokamak_experimental";
    /** Source {@code ktfru.recipe.fuelbattery}. No rows are added. */
    public static final String FUEL_BATTERY = "gt6ktfruaddon.recipe.fuel_battery";
    public static final String LASER_FUSION = "gt6ktfruaddon.recipe.laser_fusion";
    public static final String LASER_FUSION_EXPERIMENTAL = "gt6ktfruaddon.recipe.laser_fusion_experimental";
    public static final String CATALYTIC_REACTOR = "gt6ktfruaddon.recipe.catalytic_reactor";
    public static final String MIRROR_GOLD_SYNTHESIS = "gt6ktfruaddon.recipe.mirror_gold_synthesis";
    public static final String MIRROR_GOLD_QUANTUMIZATION = "gt6ktfruaddon.recipe.mirror_gold_quantumization";
    public static final String COSMIC_RADIATION = "gt6ktfruaddon.recipe.cosmic_radiation";
    public static final String MICROGRAVITY_CRYSTAL = "gt6ktfruaddon.recipe.microgravity_crystal";
    public static final String QUANTUM_GOLD = "gt6ktfruaddon.recipe.quantum_gold";
    public static final String QUANTUM_OBSERVATION = "gt6ktfruaddon.recipe.quantum_observation";
    public static final String QUANTUM_COMMUNICATION = "gt6ktfruaddon.recipe.quantum_communication";
    public static final String RESEARCH_ASSEMBLER = "gt6ktfruaddon.recipe.research_assembler";
    public static final String RESEARCH_REACTOR = "gt6ktfruaddon.recipe.research_reactor";
    /** Built-in GT fuel map behind {@code FM.Gas}. Already contains hydrogen. */
    public static final String GAS_FUEL = "gt.recipe.fuels.gas";
    /** Built-in GT fuel map behind {@code FM.Engine}. Already contains diesel. */
    public static final String ENGINE_FUEL = "gt.recipe.fuels.engine";

    private RecipeMaps() {}

    /** A machine recipe map with the slot counts of the source map and the flags every one of them shares. */
    public static RecipeTable table(String name, String title, String gui,
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
}
