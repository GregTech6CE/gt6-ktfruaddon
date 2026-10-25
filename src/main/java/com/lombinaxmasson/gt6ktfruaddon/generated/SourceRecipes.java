/*
 * GENERATED FILE - DO NOT EDIT. 勿手改。
 * Upstream: https://github.com/kuzuanpa/kTFRUAddon @ 6404ab6979de9fce832ce0c41171000decf00e69
 * GT6CE: 1d1f69fbb267ad31548b1fcb1ec1168435b67bf3
 * Generate from repository root: python tools/extract_machine_recipes.py
 * Adaptations from tools/mappings.json (aliases and substitutions):
 * block_aliases: adamantium_turbine_shell -> turbine_shell_adamantium
 * block_aliases: ai_memory_bank -> ai_high_bandwidth_memory_bank
 * block_aliases: laser_focusing_array -> laser_fusion_focusing_array
 * block_aliases: laser_product_collector -> laser_fusion_neutron_product_collector
 * block_aliases: laser_pulse_storage -> laser_fusion_pulse_storage_module
 * block_aliases: laser_target_injector -> laser_fusion_target_injector
 * block_aliases: quantum_coherence_casing -> quantum_coherence_chamber_wall
 * block_aliases: quantum_coherence_casing_iridium -> quantum_coherence_chamber_wall_31111
 * block_aliases: room_energy_port -> room_energy_and_status_port
 * block_aliases: stainless_turbine_shell -> turbine_shell_stainless
 * block_aliases: sun_heater_top_layer -> heat_pipe_top
 * block_aliases: titanium_turbine_shell -> turbine_shell_titanium
 * block_aliases: trinaquadalloy_turbine_shell -> turbine_shell_trinaquadalloy
 * block_aliases: tungstensteel_turbine_shell -> turbine_shell_tungstensteel
 * gt_substitutions: casing_machine_double_naquadria -> casing_machine_double_tungstensteel
 * gt_substitutions: casing_machine_naquadria -> casing_machine_trinaquadalloy
 * form_substitutions: paneGlass Black -> minecraft:black_stained_glass_pane
 * form_substitutions: plate Glass -> minecraft:glass
 * form_substitutions: plate Paper -> minecraft:paper
 */
package com.lombinaxmasson.gt6ktfruaddon.generated;

import java.util.LinkedHashSet;
import java.util.Set;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeTiming;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Source rows from kTFRUAddon {@code 6404ab69} whose inputs and outputs resolve
 * to items and fluids already in this build. Amounts follow gregapi
 * {@code UT.Code.units}: one material unit of gas or chemical liquid is 1000 mB,
 * one unit of molten metal is 144 mB, and {@code FL.make} stays millibuckets.
 * A leftover that does not fill one millibucket rounds up only when the source
 * call asks for it. Rows with the source optimize flag are divided the way
 * GregTech divides them. The light mixer row stays in {@code Recipes}.
 */
public final class SourceRecipes {
    private static final ItemStack[] NO_ITEMS = new ItemStack[0];
    private static final FluidStack[] NO_FLUIDS = new FluidStack[0];
    private static final Set<String> PROBLEMS = new LinkedHashSet<>();

    private SourceRecipes() {}

    public static void register() {
        PROBLEMS.clear();
        fluidHeating();
        assembler();
        laserCutter();
        heatMixer();
        eDA();
        flawDetector();
        waferTester();
        waferCoater();
        ultrasonicMixer();
        tinyDistillTower();
        smallDistillTower();
        distillTower();
        maskAligner();
        cNC();
        fusionTokamak();
        fusionTokamakExperimental();
        laserFusion();
        laserFusionExperimental();
        catalyticReactor();
        mirrorGoldSynthesis();
        mirrorGoldQuantumization();
        cosmicRadiationResearch();
        microgravityCrystalGrowth();
        quantumGoldSynthesis();
        quantumObservation();
        quantumCommunication();
        researchAssembler();
        researchReactor();
        if (!PROBLEMS.isEmpty()) {
            throw new IllegalStateException(PROBLEMS.size() + " source stacks did not resolve: "
                    + String.join("; ", PROBLEMS));
        }
    }

    private static void fluidHeating() {
        // Chemistry.java:28
        add("Chemistry.java:28", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(20L).euPerTick(80L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("DistW", 10)},
                new FluidStack[] {field("Steam", 1600)});
        // Chemistry.java:30 D-tier Coolant_IC2.
        add("Chemistry.java:30", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(10L).euPerTick(20L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Coolant_IC2", 10)},
                new FluidStack[] {field("Coolant_IC2_Hot", 10)});
        // Chemistry.java:31
        add("Chemistry.java:31", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(10L).euPerTick(72L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenMolten_PotassiumSodium", 10)},
                new FluidStack[] {field("GenLiquid_HotPotassiumSodium", 10)});
        // Chemistry.java:32
        add("Chemistry.java:32", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(10L).euPerTick(30L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenMolten_Sodium", 10)},
                new FluidStack[] {field("Hot_Molten_Sodium", 10)});
        // Chemistry.java:33
        add("Chemistry.java:33", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(10L).euPerTick(40L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenMolten_Tin", 10)},
                new FluidStack[] {field("Hot_Molten_Tin", 10)});
        // Chemistry.java:34
        add("Chemistry.java:34", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(10L).euPerTick(15L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_LithiumChloride", 69)},
                new FluidStack[] {field("Hot_Molten_LiCl", 10)});
        // Chemistry.java:35
        add("Chemistry.java:35", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(10L).euPerTick(50L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_HeavyWater", 10)},
                new FluidStack[] {field("Hot_Heavy_Water", 10)});
        // Chemistry.java:36
        add("Chemistry.java:36", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(10L).euPerTick(40L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_SemiheavyWater", 10)},
                new FluidStack[] {field("Hot_Semi_Heavy_Water", 10)});
        // Chemistry.java:37
        add("Chemistry.java:37", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(10L).euPerTick(60L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_TritiatedWater", 10)},
                new FluidStack[] {field("Hot_Tritiated_Water", 10)});
        // Chemistry.java:38
        add("Chemistry.java:38", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(10L).euPerTick(20L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("CarbonDioxide", 10)},
                new FluidStack[] {field("Hot_Carbon_Dioxide", 10)});
        // Chemistry.java:39
        add("Chemistry.java:39", RecipeMaps.FLUID_HEATING, RecipeTiming.ticks(10L).euPerTick(30L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Helium", 10)},
                new FluidStack[] {field("Hot_Helium", 10)});
    }

    private static void assembler() {
        // tileEntityInit0.java:536
        add("tileEntityInit0.java:536", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(4800L).euPerTick(32768L),
                new ItemStack[] {form("plateDouble", "Tungstensteel", 4), form("plateDouble", "Titanium", 4), form("dustAnalyticalPure", "Lead", 8), form("dustAnalyticalPure", "Tungsten", 4), form("dustAnalyticalPure", "Titanium", 4), gt("compact_sensor_hv", 1), gt("compact_signal_emitter_hv", 1), gt("circuit_elite", 2)},
                new ItemStack[] {block("centrifugal_microgravity", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:675
        add("tileEntityInit0.java:675", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {gt("casing_machine_double_naquadria", 1), form("plate", "MirrorGold", 2), form("dustAbsolutelyPure", "intellite", 4), form("dustAbsolutelyPure", "Naquadria", 2), block("entanglement_channel_module", 2), block("quantum_readout_coil", 1), gt("compact_signal_emitter_luv", 2), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("wireless_receiver_quantum", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Circuits.java:111
        add("Circuits.java:111", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(120L).euPerTick(1482L),
                new ItemStack[] {gt("circuit_part_ultimate", 8), gt("circuit_plate_platinum", 1), own("coil_t4", 8), own("capacitor_t3", 64), own("resistance_t4", 99), own("resistance_t4", 29), own("diode_t3", 16)},
                new ItemStack[] {gt("circuit_board_ultimate", 1)},
                new FluidStack[] {field("GenLiquid_MoltenTeflon", 144)},
                NO_FLUIDS);
        // Circuits.java:109
        add("Circuits.java:109", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(120L).euPerTick(384L),
                new ItemStack[] {gt("circuit_part_elite", 4), gt("circuit_plate_gold", 1), own("coil_t2", 4), own("capacitor_t2", 8), own("resistance_t2", 20), own("diode_t1", 2)},
                new ItemStack[] {gt("circuit_board_elite", 1)},
                new FluidStack[] {field("GenLiquid_MoltenBakelite", 72)},
                NO_FLUIDS);
        // Circuits.java:110
        add("Circuits.java:110", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(120L).euPerTick(874L),
                new ItemStack[] {gt("circuit_part_master", 6), gt("circuit_plate_platinum", 1), own("coil_t3", 8), own("capacitor_t3", 24), own("resistance_t3", 64), own("diode_t2", 8)},
                new ItemStack[] {gt("circuit_board_master", 1)},
                new FluidStack[] {field("GenLiquid_MoltenTeflon", 144)},
                NO_FLUIDS);
        // tileEntityInit0.java:213
        add("tileEntityInit0.java:213", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 4), form("plateDense", "ElectricalSteel", 1), gt("wire_01_tin", 4), gt("cable_01_tin", 2), gt("circuit_basic", 1)},
                new ItemStack[] {block("transformer_small_lv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:214
        add("tileEntityInit0.java:214", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 4), form("plateDense", "ElectricalSteel", 1), gt("wire_01_copper", 8), gt("cable_01_copper", 2), gt("circuit_good", 1)},
                new ItemStack[] {block("transformer_small_mv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:215
        add("tileEntityInit0.java:215", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 4), form("plateDense", "ElectricalSteel", 1), gt("wire_01_gold", 16), gt("cable_01_gold", 2), gt("circuit_advanced", 1)},
                new ItemStack[] {block("transformer_small_hv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:216
        add("tileEntityInit0.java:216", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 4), form("plateDense", "ElectricalSteel", 1), gt("wire_01_aluminium", 32), gt("cable_01_aluminium", 2), gt("circuit_elite", 1)},
                new ItemStack[] {block("transformer_small_ev", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:217
        add("tileEntityInit0.java:217", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 4), form("plateDense", "ElectricalSteel", 2), gt("wire_01_platinum", 64), gt("cable_01_platinum", 2), gt("circuit_master", 1)},
                new ItemStack[] {block("transformer_small_iv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:218
        add("tileEntityInit0.java:218", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 4), form("plateDense", "ElectricalSteel", 2), gt("wire_01_graphene", 64), gt("wire_01_graphene", 64), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("transformer_small_luv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:220
        add("tileEntityInit0.java:220", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 4), form("plateDense", "ElectricalSteel", 4), gt("wire_08_tin", 4), gt("cable_04_tin", 2), gt("circuit_basic", 2)},
                new ItemStack[] {block("transformer_large_lv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:221
        add("tileEntityInit0.java:221", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 4), form("plateDense", "ElectricalSteel", 4), gt("wire_08_copper", 8), gt("cable_04_copper", 2), gt("circuit_good", 2)},
                new ItemStack[] {block("transformer_large_mv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:222
        add("tileEntityInit0.java:222", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 4), form("plateDense", "ElectricalSteel", 4), gt("wire_08_gold", 16), gt("cable_04_gold", 2), gt("circuit_advanced", 2)},
                new ItemStack[] {block("transformer_large_hv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:223
        add("tileEntityInit0.java:223", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 4), form("plateDense", "ElectricalSteel", 4), gt("wire_08_aluminium", 32), gt("cable_04_aluminium", 2), gt("circuit_elite", 2)},
                new ItemStack[] {block("transformer_large_ev", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:224
        add("tileEntityInit0.java:224", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 4), form("plateDense", "ElectricalSteel", 8), gt("wire_08_platinum", 64), gt("cable_04_platinum", 2), gt("circuit_master", 2)},
                new ItemStack[] {block("transformer_large_iv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:225
        add("tileEntityInit0.java:225", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 4), form("plateDense", "ElectricalSteel", 8), gt("wire_08_graphene", 64), gt("wire_04_graphene", 2), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("transformer_large_luv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:241
        add("tileEntityInit0.java:241", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 4), gt("compact_force_field_emitter_lv", 2), gt("wire_01_tin", 8), gt("cable_01_tin", 2), gt("circuit_basic", 1)},
                new ItemStack[] {block("light_transformer_small_lv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:242
        add("tileEntityInit0.java:242", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 4), gt("compact_force_field_emitter_mv", 2), gt("wire_01_copper", 8), gt("cable_01_copper", 2), gt("circuit_good", 1)},
                new ItemStack[] {block("light_transformer_small_mv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:243
        add("tileEntityInit0.java:243", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 4), gt("compact_force_field_emitter_hv", 2), gt("wire_01_gold", 8), gt("cable_01_gold", 2), gt("circuit_advanced", 1)},
                new ItemStack[] {block("light_transformer_small_hv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:244
        add("tileEntityInit0.java:244", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 4), gt("compact_force_field_emitter_ev", 2), gt("wire_01_aluminium", 8), gt("cable_01_aluminium", 2), gt("circuit_elite", 1)},
                new ItemStack[] {block("light_transformer_small_ev", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:245
        add("tileEntityInit0.java:245", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 4), gt("compact_force_field_emitter_iv", 2), gt("wire_01_platinum", 8), gt("cable_01_platinum", 2), gt("circuit_master", 1)},
                new ItemStack[] {block("light_transformer_small_iv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:246
        add("tileEntityInit0.java:246", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 4), gt("compact_force_field_emitter_luv", 2), gt("wire_01_graphene", 8), gt("wire_01_graphene", 2), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("light_transformer_small_luv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:248
        add("tileEntityInit0.java:248", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 16), gt("compact_force_field_emitter_lv", 8), gt("wire_08_tin", 8), gt("cable_04_tin", 2), gt("circuit_basic", 2)},
                new ItemStack[] {block("light_transformer_large_lv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:249
        add("tileEntityInit0.java:249", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 16), gt("compact_force_field_emitter_mv", 8), gt("wire_08_copper", 8), gt("cable_04_copper", 2), gt("circuit_good", 2)},
                new ItemStack[] {block("light_transformer_large_mv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:250
        add("tileEntityInit0.java:250", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 16), gt("compact_force_field_emitter_hv", 8), gt("wire_08_gold", 8), gt("cable_04_gold", 2), gt("circuit_advanced", 2)},
                new ItemStack[] {block("light_transformer_large_hv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:251
        add("tileEntityInit0.java:251", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 16), gt("compact_force_field_emitter_ev", 8), gt("wire_08_aluminium", 8), gt("cable_04_aluminium", 2), gt("circuit_elite", 2)},
                new ItemStack[] {block("light_transformer_large_ev", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:252
        add("tileEntityInit0.java:252", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 16), gt("compact_force_field_emitter_iv", 8), gt("wire_08_platinum", 8), gt("cable_04_platinum", 2), gt("circuit_master", 2)},
                new ItemStack[] {block("light_transformer_large_iv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:253
        add("tileEntityInit0.java:253", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 16), gt("compact_force_field_emitter_luv", 8), gt("wire_08_graphene", 8), gt("wire_04_graphene", 2), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("light_transformer_large_luv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:673
        add("tileEntityInit0.java:673", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1200L).euPerTick(8192L),
                new ItemStack[] {gt("casing_machine_double_tungstensteel", 1), form("dustAnalyticalPure", "Copper", 8), form("dustAnalyticalPure", "Silver", 4), form("plate", "PEEK", 4), form("wireFine", "Graphene", 16), gt("circuit_master", 1)},
                new ItemStack[] {block("wireless_receiver_inductive", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:74
        add("Circuits.java:74", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(60L).euPerTick(256L),
                new ItemStack[] {form("plateGem", "Silicon", 1), form("dust", "Boron", 2), form("dust", "Phosphor", 4), form("foil", "Silver", 4), form("dust", "Teflon", 1)},
                new ItemStack[] {own("diode_t1", 32)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:106
        add("Circuits.java:106", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(100L).euPerTick(16L),
                new ItemStack[] {gt("circuit_part_basic", 2), gt("circuit_plate_copper", 1), own("coil_t1", 1), own("capacitor_t1", 2), own("resistance_t1", 4)},
                new ItemStack[] {gt("circuit_board_basic", 1)},
                new FluidStack[] {field("Glue", 72)},
                NO_FLUIDS);
        // Circuits.java:107
        add("Circuits.java:107", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(200L).euPerTick(24L),
                new ItemStack[] {gt("circuit_part_good", 2), gt("circuit_plate_copper", 1), own("coil_t1", 2), own("capacitor_t1", 8), own("resistance_t1", 8)},
                new ItemStack[] {gt("circuit_board_good", 1)},
                new FluidStack[] {field("Glue", 72)},
                NO_FLUIDS);
        // Circuits.java:108
        add("Circuits.java:108", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(120L).euPerTick(128L),
                new ItemStack[] {gt("circuit_part_advanced", 4), gt("circuit_plate_gold", 1), own("coil_t2", 2), own("capacitor_t2", 6), own("resistance_t2", 12)},
                new ItemStack[] {gt("circuit_board_advanced", 1)},
                new FluidStack[] {field("GenLiquid_MoltenBakelite", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:167
        add("tileEntityInit0.java:167", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(20L).euPerTick(64L),
                new ItemStack[] {gt("casing_machine_aluminium", 1), own("proton_exchange_membrane", 2), block("pipe_medium_teflon", 1), gt("cable_02_platinum", 1), form("plate", "Glass", 1), form("plate", "Teflon", 8)},
                new ItemStack[] {block("gas_battery_aluminium", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // tileEntityInit0.java:168
        add("tileEntityInit0.java:168", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(20L).euPerTick(64L),
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), own("proton_exchange_membrane", 8), block("pipe_medium_teflon", 1), gt("cable_02_platinum", 4), form("plate", "Glass", 4), form("plate", "Teflon", 24)},
                new ItemStack[] {block("gas_battery_stainless", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // tileEntityInit0.java:658
        add("tileEntityInit0.java:658", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1200L).euPerTick(8192L),
                new ItemStack[] {form("plateDouble", "Tungstensteel", 4), form("plate", "PEEK", 1), form("wireFine", "Graphene", 2), form("dustAnalyticalPure", "Silicon", 1), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("ai_compute_frame", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:662
        add("tileEntityInit0.java:662", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1800L).euPerTick(16384L),
                new ItemStack[] {own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "intellite", 1), gt("compact_signal_emitter_luv", 1), form("wireFine", "Graphene", 4), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("ai_recognition_matrix", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // Circuits.java:36
        add("Circuits.java:36", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(120L).euPerTick(16L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Constantan", 10), form("plate", "Ceramic", 1), form("foil", "Rubber", 6)},
                new ItemStack[] {own("resistance_t1", 16)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:37
        add("Circuits.java:37", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(90L).euPerTick(180L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Nichrome", 10), form("plate", "Ceramic", 1), form("foil", "Rubber", 8)},
                new ItemStack[] {own("resistance_t2", 24)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:38
        add("Circuits.java:38", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(60L).euPerTick(1000L),
                new ItemStack[] {form("plate", "Copper", 1), form("plateTiny", "Ruthenium", 1), form("plate", "Ceramic", 1), form("dust", "Teflon", 4)},
                new ItemStack[] {own("resistance_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 14)},
                NO_FLUIDS);
        // Circuits.java:39
        add("Circuits.java:39", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(50L).euPerTick(4096L),
                new ItemStack[] {form("plate", "Gold", 1), form("plateTiny", "Ruthenium", 1), form("plate", "EpoxyResin", 1), form("dust", "Teflon", 4)},
                new ItemStack[] {own("resistance_t4", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 14)},
                NO_FLUIDS);
        // Circuits.java:40
        add("Circuits.java:40", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(50L).euPerTick(4096L),
                new ItemStack[] {form("plate", "Gold", 1), form("plateTiny", "Ruthenium", 1), form("plate", "EpoxyResin", 1), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("resistance_t4", 64), own("resistance_t4", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 14)},
                NO_FLUIDS);
        // Circuits.java:44
        add("Circuits.java:44", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(120L).euPerTick(16L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Constantan", 10), form("plate", "Ceramic", 1), form("foil", "Plastic", 6)},
                new ItemStack[] {own("resistance_t1", 16)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:45
        add("Circuits.java:45", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(90L).euPerTick(80L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Nichrome", 10), form("plate", "Ceramic", 1), form("foil", "Plastic", 8)},
                new ItemStack[] {own("resistance_t2", 24)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:49
        add("Circuits.java:49", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(180L).euPerTick(16L),
                new ItemStack[] {form("plate", "Copper", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Copper", 32), form("foil", "Rubber", 8)},
                new ItemStack[] {own("coil_t1", 20)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:50
        add("Circuits.java:50", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(140L).euPerTick(80L),
                new ItemStack[] {form("plate", "Copper", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Gold", 32), form("foil", "Rubber", 8)},
                new ItemStack[] {own("coil_t2", 20)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:51
        add("Circuits.java:51", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(100L).euPerTick(1000L),
                new ItemStack[] {form("plate", "Copper", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Platinum", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("coil_t3", 32)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:52
        add("Circuits.java:52", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(4096L),
                new ItemStack[] {form("plate", "Gold", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Graphene", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("coil_t4", 32)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:53
        add("Circuits.java:53", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(4096L),
                new ItemStack[] {form("plate", "Gold", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Graphene", 32), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("coil_t4", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:57
        add("Circuits.java:57", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(180L).euPerTick(16L),
                new ItemStack[] {form("plate", "Copper", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Copper", 32), form("foil", "Plastic", 8)},
                new ItemStack[] {own("coil_t1", 20)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:58
        add("Circuits.java:58", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(140L).euPerTick(80L),
                new ItemStack[] {form("plate", "Copper", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Gold", 32), form("foil", "Plastic", 8)},
                new ItemStack[] {own("coil_t2", 20)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:63
        add("Circuits.java:63", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(120L).euPerTick(16L),
                new ItemStack[] {form("foil", "Copper", 16), form("foil", "Rubber", 8), form("plate", "Paper", 4), form("wireFine", "Copper", 8)},
                new ItemStack[] {own("capacitor_t1", 32)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Circuits.java:64
        add("Circuits.java:64", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(100L).euPerTick(140L),
                new ItemStack[] {form("foil", "Aluminium", 16), form("foil", "Rubber", 8), form("dust", "Alumina", 16), form("wireFine", "Silver", 8)},
                new ItemStack[] {own("capacitor_t2", 40)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Circuits.java:65
        add("Circuits.java:65", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(1000L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Platinum", 4), form("foil", "Niobium", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:66
        add("Circuits.java:66", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(1000L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Platinum", 4), form("foil", "Tantalum", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:67
        add("Circuits.java:67", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(1000L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Graphene", 4), form("foil", "Niobium", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:68
        add("Circuits.java:68", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(1000L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Graphene", 4), form("foil", "Tantalum", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:69
        add("Circuits.java:69", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(1000L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Platinum", 4), form("foil", "Niobium", 32), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("capacitor_t3", 64), own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:70
        add("Circuits.java:70", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(1000L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Platinum", 4), form("foil", "Tantalum", 32), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("capacitor_t3", 64), own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:71
        add("Circuits.java:71", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(1000L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Graphene", 4), form("foil", "Niobium", 32), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("capacitor_t3", 64), own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:72
        add("Circuits.java:72", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(1000L),
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Graphene", 4), form("foil", "Tantalum", 32), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("capacitor_t3", 64), own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:79
        add("Circuits.java:79", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(60L).euPerTick(128L),
                new ItemStack[] {form("plateTiny", "Gallium", 4), form("dust", "Arsenic", 2), form("dust", "Aluminium", 1), form("foil", "Silver", 4)},
                new ItemStack[] {own("led_set", 2)},
                new FluidStack[] {field("GenLiquid_MoltenTeflon", 144)},
                NO_FLUIDS);
        // CompactItem.java:44
        add("CompactItem.java:44", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(16384L).euPerTick(16384L),
                new ItemStack[] {form("nugget", "DraconiumAwakened", 2), form("plateTiny", "Adamantium", 2), form("wireFine", "Terrasteel", 64), form("plate", "VibraniumSteel", 1), form("plateGem", "NetherStar", 1)},
                new ItemStack[] {vanilla(Items.PLAYER_HEAD, 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:423
        add("ComputerBuilding.java:423", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(7800L).euPerTick(700L),
                new ItemStack[] {circuit(0, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v4", 1), own("inter_layer_t1", 1)},
                new ItemStack[] {own("cpugt3680_v4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:424
        add("ComputerBuilding.java:424", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(16000L).euPerTick(780L),
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v4", 2), own("inter_layer_t1", 1)},
                new ItemStack[] {own("cpugt3699_v4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:426
        add("ComputerBuilding.java:426", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(12000L).euPerTick(700L),
                new ItemStack[] {circuit(10, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v3", 1), own("inter_layer_t1", 1)},
                new ItemStack[] {own("cpugt3680_v3_e", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:427
        add("ComputerBuilding.java:427", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(13000L).euPerTick(780L),
                new ItemStack[] {circuit(11, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v3", 2), own("inter_layer_t1", 1)},
                new ItemStack[] {own("cpugt3699_v3_e", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:428
        add("ComputerBuilding.java:428", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(18000L).euPerTick(700L),
                new ItemStack[] {circuit(10, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v4", 1), own("inter_layer_t2", 1)},
                new ItemStack[] {own("cpugt3680_v4_e", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:429
        add("ComputerBuilding.java:429", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(20000L).euPerTick(780L),
                new ItemStack[] {circuit(11, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v4", 2), own("inter_layer_t2", 1)},
                new ItemStack[] {own("cpugt3699_v4_e", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // Fusion.java:148
        add("Fusion.java:148", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1200L).euPerTick(16384L),
                new ItemStack[] {form("plateDense", "Titanium", 4), gt("wire_04_copper", 8), form("plate", "StainlessSteel", 2), gt("circuit_elite", 1)},
                new ItemStack[] {block("tokamak_magnetic_conductor_exp", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Fusion.java:150
        add("Fusion.java:150", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1600L).euPerTick(32768L),
                new ItemStack[] {gt("wire_08_copper", 16), form("plateDense", "StainlessSteel", 4), form("plate", "PEEK", 2), gt("circuit_elite", 1)},
                new ItemStack[] {block("tokamak_magnetic_coil_exp", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:152
        add("Fusion.java:152", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1600L).euPerTick(32768L),
                new ItemStack[] {form("plateDense", "StainlessSteel", 4), form("plate", "Titanium", 4), form("plate", "PEEK", 2), gt("circuit_elite", 1)},
                new ItemStack[] {block("tokamak_magnetic_wall_exp", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:157
        add("Fusion.java:157", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1800L).euPerTick(65536L),
                new ItemStack[] {form("plateDense", "Tungstensteel", 4), gt("wire_04_graphene", 8), form("plate", "PEEK", 2), gt("circuit_master", 1)},
                new ItemStack[] {block("tokamak_magnetic_conductor_t1", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:159
        add("Fusion.java:159", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(131072L),
                new ItemStack[] {gt("wire_08_graphene", 16), form("plateDense", "Tungstensteel", 4), form("plate", "PEEK", 4), gt("circuit_master", 1)},
                new ItemStack[] {block("tokamak_magnetic_coil_t1", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 9)},
                NO_FLUIDS);
        // Fusion.java:161
        add("Fusion.java:161", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(131072L),
                new ItemStack[] {form("plateDense", "Tungstensteel", 4), form("plate", "Trinaquadalloy", 4), form("plate", "PEEK", 4), gt("circuit_master", 1)},
                new ItemStack[] {block("tokamak_magnetic_wall_t1", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 9)},
                NO_FLUIDS);
        // QuantumEntry.java:57
        add("QuantumEntry.java:57", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1200L).euPerTick(4096L),
                new ItemStack[] {form("dustAnalyticalPure", "Platinum", 4), form("foil", "MirrorGold", 4), form("wireFine", "Naquadah", 16), gt("circuit_elite", 1)},
                new ItemStack[] {block("quantum_stabilization_substrate", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // QuantumEntry.java:73
        add("QuantumEntry.java:73", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(8192L),
                new ItemStack[] {form("plate", "MirrorGold", 1), form("dustAbsolutelyPure", "intellite", 4), gt("compact_signal_emitter_luv", 1), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("entanglement_channel_module", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // QuantumEntry.java:79
        add("QuantumEntry.java:79", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1600L).euPerTick(4096L),
                new ItemStack[] {form("foil", "MirrorGold", 2), form("dustAnalyticalPure", "intellite", 2), form("wireFine", "Naquadah", 16), gt("circuit_elite", 1)},
                new ItemStack[] {block("quantum_readout_coil", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:227
        add("tileEntityInit0.java:227", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 4), form("gearGtSmall", "SteelGalvanized", 4), gt("circuit_basic", 1)},
                new ItemStack[] {block("flywheel_gear_small_t1", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:228
        add("tileEntityInit0.java:228", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 4), form("gearGtSmall", "Aluminium", 4), gt("circuit_good", 1)},
                new ItemStack[] {block("flywheel_gear_small_t2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:229
        add("tileEntityInit0.java:229", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 4), form("gearGtSmall", "StainlessSteel", 4), gt("circuit_advanced", 1)},
                new ItemStack[] {block("flywheel_gear_small_t3", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:230
        add("tileEntityInit0.java:230", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 4), form("gearGtSmall", "Chromium", 4), gt("circuit_elite", 1)},
                new ItemStack[] {block("flywheel_gear_small_t4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:231
        add("tileEntityInit0.java:231", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 4), form("gearGtSmall", "Titanium", 4), gt("circuit_master", 1)},
                new ItemStack[] {block("flywheel_gear_small_t5", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:232
        add("tileEntityInit0.java:232", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 4), form("gearGtSmall", "Iridium", 4), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("flywheel_gear_small_t6", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:234
        add("tileEntityInit0.java:234", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 16), form("gearGt", "SteelGalvanized", 4), gt("circuit_basic", 2)},
                new ItemStack[] {block("flywheel_gear_large_t1", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:235
        add("tileEntityInit0.java:235", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 16), form("gearGt", "Aluminium", 4), gt("circuit_good", 2)},
                new ItemStack[] {block("flywheel_gear_large_t2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:236
        add("tileEntityInit0.java:236", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 16), form("gearGt", "StainlessSteel", 4), gt("circuit_advanced", 2)},
                new ItemStack[] {block("flywheel_gear_large_t3", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:237
        add("tileEntityInit0.java:237", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 16), form("gearGt", "Chromium", 4), gt("circuit_elite", 2)},
                new ItemStack[] {block("flywheel_gear_large_t4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:238
        add("tileEntityInit0.java:238", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 16), form("gearGt", "Titanium", 4), gt("circuit_master", 2)},
                new ItemStack[] {block("flywheel_gear_large_t5", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:239
        add("tileEntityInit0.java:239", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 16), form("gearGt", "Iridium", 4), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("flywheel_gear_large_t6", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:462
        add("tileEntityInit0.java:462", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(800L).euPerTick(400L),
                new ItemStack[] {gt("casing_machine_dense_stainlesssteel", 1), form("rotor", "StainlessSteel", 16), gt("axle_steel_4", 1), gt("circuit_good", 2)},
                new ItemStack[] {block("liquid_battery_stainless", 1)},
                new FluidStack[] {field("GenMolten_StainlessSteel", 576)},
                NO_FLUIDS);
        // tileEntityInit0.java:463
        add("tileEntityInit0.java:463", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(800L).euPerTick(800L),
                new ItemStack[] {gt("casing_machine_dense_titanium", 1), form("rotor", "Titanium", 16), gt("axle_titanium_4", 1), gt("circuit_advanced", 2)},
                new ItemStack[] {block("liquid_battery_titanium", 1)},
                new FluidStack[] {field("GenMolten_Titanium", 576)},
                NO_FLUIDS);
        // tileEntityInit0.java:464
        add("tileEntityInit0.java:464", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(800L).euPerTick(1400L),
                new ItemStack[] {gt("casing_machine_dense_tungstensteel", 1), form("rotor", "Tungstensteel", 16), gt("axle_tungstensteel_4", 1), gt("circuit_elite", 2)},
                new ItemStack[] {block("liquid_battery_tungstensteel", 1)},
                new FluidStack[] {field("GenMolten_Tungstensteel", 576)},
                NO_FLUIDS);
        // tileEntityInit0.java:537
        add("tileEntityInit0.java:537", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(4800L).euPerTick(32768L),
                new ItemStack[] {gt("casing_machine_titanium", 1), form("plateDouble", "Titanium", 4), form("dustAnalyticalPure", "Molybdenum", 8), gt("circuit_elite", 2)},
                new ItemStack[] {block("microgravity_crystal_growth", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:555
        add("tileEntityInit0.java:555", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(200L).euPerTick(16L),
                new ItemStack[] {form("dust", "PotassiumSodium", 5), gt("casing_machine_double_silver", 1), form("dust", "Carbon", 1), form("plateCurved", "Silver", 9)},
                new ItemStack[] {block("sunlight_absorber", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:556
        add("tileEntityInit0.java:556", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(200L).euPerTick(16L),
                new ItemStack[] {form("dust", "PotassiumSodium", 5), gt("casing_machine_silver", 1), form("plateCurved", "Silver", 12), circuit(0, 1)},
                new ItemStack[] {block("heat_pipe", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:557
        add("tileEntityInit0.java:557", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(200L).euPerTick(16L),
                new ItemStack[] {form("dust", "PotassiumSodium", 5), gt("casing_machine_silver", 1), form("plateCurved", "Silver", 6), form("plateDense", "Silver", 1)},
                new ItemStack[] {block("sun_heater_top_layer", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:659
        add("tileEntityInit0.java:659", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1800L).euPerTick(16384L),
                new ItemStack[] {own("ram_bar2_g32", 2), form("dustAnalyticalPure", "Silicon", 1), form("wireFine", "Graphene", 2), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("ai_memory_bank", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:660
        add("tileEntityInit0.java:660", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1800L).euPerTick(16384L),
                new ItemStack[] {own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "Platinum", 1), form("wireFine", "Graphene", 4), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("ai_routing_matrix", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:661
        add("tileEntityInit0.java:661", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1800L).euPerTick(16384L),
                new ItemStack[] {own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "Carbon", 1), own("ram_bar2_g32", 2), form("foil", "PEEK", 2)},
                new ItemStack[] {block("ai_packing_planner", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:711
        add("tileEntityInit0.java:711", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(32L),
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("plate", "ElectricalSteel", 4), gt("compact_force_field_emitter_mv", 1), gt("circuit_good", 2)},
                new ItemStack[] {block("wireless_compute_node_aluminium", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:712
        add("tileEntityInit0.java:712", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("plate", "ElectricalSteel", 4), gt("compact_force_field_emitter_hv", 1), gt("circuit_advanced", 2)},
                new ItemStack[] {block("wireless_compute_node_stainless", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:713
        add("tileEntityInit0.java:713", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(128L),
                new ItemStack[] {gt("casing_machine_chromium", 1), form("plate", "ElectricalSteel", 4), gt("compact_force_field_emitter_ev", 1), gt("circuit_elite", 2)},
                new ItemStack[] {block("wireless_compute_node_chromium", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:714
        add("tileEntityInit0.java:714", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(400L).euPerTick(512L),
                new ItemStack[] {gt("casing_machine_titanium", 1), form("plate", "ElectricalSteel", 8), gt("compact_force_field_emitter_iv", 1), gt("circuit_master", 2)},
                new ItemStack[] {block("wireless_compute_node_titanium", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:720
        add("tileEntityInit0.java:720", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {form("plate", "Naquadria", 1), block("quantum_cryogenic_manifold", 1), block("quantum_gold_drive_module", 1), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("quantum_compute_node_naquadria", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:722
        add("tileEntityInit0.java:722", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(3600L).euPerTick(131072L),
                new ItemStack[] {form("plate", "Naquadria", 1), block("quantum_phase_isolation_frame", 1), block("quantum_gold_lattice_stabilizer", 1), gt("circuit_ultimate", 4)},
                new ItemStack[] {block("quantum_compute_node_enriched", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 9)},
                NO_FLUIDS);
        // tileEntityInit0.java:724
        add("tileEntityInit0.java:724", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(4800L).euPerTick(524288L),
                new ItemStack[] {form("plate", "Trinaquadalloy", 1), block("deep_coherence_containment_wall", 1), block("quantum_gold_lattice_stabilizer", 1), gt("circuit_ultimate", 8)},
                new ItemStack[] {block("quantum_compute_node_trinaquadalloy", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 4)},
                NO_FLUIDS);
        // Circuits.java:75
        add("Circuits.java:75", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(10L).euPerTick(1024L),
                new ItemStack[] {own("diode_t2_part", 4), form("wireFine", "Silver", 1), form("dustTiny", "Teflon", 1)},
                new ItemStack[] {own("diode_t2", 4)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 1)},
                NO_FLUIDS);
        // Circuits.java:76
        add("Circuits.java:76", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(10L).euPerTick(4096L),
                new ItemStack[] {own("diode_t3_part", 4), form("wireFine", "Graphene", 1), form("dustTiny", "Teflon", 1)},
                new ItemStack[] {own("diode_t3", 4)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 1)},
                NO_FLUIDS);
        // Circuits.java:77
        add("Circuits.java:77", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(10L).euPerTick(4096L),
                new ItemStack[] {own("diode_t3_part", 4), form("wireFine", "Graphene", 1), form("dustTiny", "NanoBariumTitanate", 1)},
                new ItemStack[] {own("diode_t3", 8)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 1)},
                NO_FLUIDS);
        // ComputerBuilding.java:415
        add("ComputerBuilding.java:415", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(7200L).euPerTick(420L),
                new ItemStack[] {own("cpu_board_t3", 1), own("cpu_die_gt3660_v4", 1), own("inter_layer_t1", 1)},
                new ItemStack[] {own("cpugt3660_v4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:417
        add("ComputerBuilding.java:417", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(3600L).euPerTick(460L),
                new ItemStack[] {circuit(0, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680", 1)},
                new ItemStack[] {own("cpugt3680", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:418
        add("ComputerBuilding.java:418", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(7800L).euPerTick(500L),
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680", 2)},
                new ItemStack[] {own("cpugt3699", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:419
        add("ComputerBuilding.java:419", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(4000L).euPerTick(500L),
                new ItemStack[] {circuit(0, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v2", 1)},
                new ItemStack[] {own("cpugt3680_v2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:420
        add("ComputerBuilding.java:420", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(8200L).euPerTick(580L),
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v2", 2)},
                new ItemStack[] {own("cpugt3699_v2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:421
        add("ComputerBuilding.java:421", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(5200L).euPerTick(560L),
                new ItemStack[] {circuit(0, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v3", 1)},
                new ItemStack[] {own("cpugt3680_v3", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:422
        add("ComputerBuilding.java:422", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(10800L).euPerTick(620L),
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v3", 2)},
                new ItemStack[] {own("cpugt3699_v3", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:432
        add("ComputerBuilding.java:432", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(42L),
                new ItemStack[] {circuit(0, 1), own("ram_board_t1", 1), own("ram_die2_k", 4)},
                new ItemStack[] {own("ram_bar2_k4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:433
        add("ComputerBuilding.java:433", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(50L),
                new ItemStack[] {circuit(1, 1), own("ram_board_t1", 1), own("ram_die2_k", 8)},
                new ItemStack[] {own("ram_bar2_k8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:435
        add("ComputerBuilding.java:435", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(128L),
                new ItemStack[] {circuit(0, 1), own("ram_board_t1", 1), own("ram_die32_k", 4)},
                new ItemStack[] {own("ram_bar32_k4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:436
        add("ComputerBuilding.java:436", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(150L),
                new ItemStack[] {circuit(1, 1), own("ram_board_t1", 1), own("ram_die32_k", 8)},
                new ItemStack[] {own("ram_bar32_k8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:438
        add("ComputerBuilding.java:438", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(180L),
                new ItemStack[] {circuit(0, 1), own("ram_board_t2", 1), own("ram_die256_k", 4)},
                new ItemStack[] {own("ram_bar256_k4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:439
        add("ComputerBuilding.java:439", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(220L),
                new ItemStack[] {circuit(1, 1), own("ram_board_t2", 1), own("ram_die256_k", 8)},
                new ItemStack[] {own("ram_bar256_k8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:440
        add("ComputerBuilding.java:440", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(900L).euPerTick(220L),
                new ItemStack[] {circuit(2, 1), own("ram_board_t2", 1), own("ram_die256_k", 16)},
                new ItemStack[] {own("ram_bar256_k16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:442
        add("ComputerBuilding.java:442", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(260L),
                new ItemStack[] {circuit(0, 1), own("ram_board_t2", 1), own("ram_die2_m", 4)},
                new ItemStack[] {own("ram_bar2_m4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:443
        add("ComputerBuilding.java:443", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(280L),
                new ItemStack[] {circuit(1, 1), own("ram_board_t2", 1), own("ram_die2_m", 8)},
                new ItemStack[] {own("ram_bar2_m8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:444
        add("ComputerBuilding.java:444", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(900L).euPerTick(280L),
                new ItemStack[] {circuit(2, 1), own("ram_board_t2", 1), own("ram_die2_m", 16)},
                new ItemStack[] {own("ram_bar2_m16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:446
        add("ComputerBuilding.java:446", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(500L),
                new ItemStack[] {circuit(0, 1), own("ram_board_t2", 1), own("ram_die16_m", 4)},
                new ItemStack[] {own("ram_bar16_m4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:447
        add("ComputerBuilding.java:447", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(540L),
                new ItemStack[] {circuit(1, 1), own("ram_board_t2", 1), own("ram_die16_m", 8)},
                new ItemStack[] {own("ram_bar16_m8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:448
        add("ComputerBuilding.java:448", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(900L).euPerTick(580L),
                new ItemStack[] {circuit(2, 1), own("ram_board_t2", 1), own("ram_die16_m", 16)},
                new ItemStack[] {own("ram_bar16_m16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:449
        add("ComputerBuilding.java:449", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(3200L).euPerTick(560L),
                new ItemStack[] {circuit(3, 1), own("ram_board_t2", 1), own("ram_die16_m", 32)},
                new ItemStack[] {own("ram_bar16_m32", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // ComputerBuilding.java:451
        add("ComputerBuilding.java:451", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(900L),
                new ItemStack[] {circuit(0, 1), own("ram_board_t2", 1), own("ram_die128_m", 4)},
                new ItemStack[] {own("ram_bar128_m4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:452
        add("ComputerBuilding.java:452", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(950L),
                new ItemStack[] {circuit(1, 1), own("ram_board_t2", 1), own("ram_die128_m", 8)},
                new ItemStack[] {own("ram_bar128_m8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:453
        add("ComputerBuilding.java:453", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(900L).euPerTick(990L),
                new ItemStack[] {circuit(2, 1), own("ram_board_t2", 1), own("ram_die128_m", 16)},
                new ItemStack[] {own("ram_bar128_m16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:454
        add("ComputerBuilding.java:454", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(3200L).euPerTick(970L),
                new ItemStack[] {circuit(3, 1), own("ram_board_t2", 1), own("ram_die128_m", 32)},
                new ItemStack[] {own("ram_bar128_m32", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // ComputerBuilding.java:456
        add("ComputerBuilding.java:456", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(2000L),
                new ItemStack[] {circuit(0, 1), own("ram_board_t3", 1), own("ram_die768_m", 4)},
                new ItemStack[] {own("ram_bar768_m4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:457
        add("ComputerBuilding.java:457", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(2300L),
                new ItemStack[] {circuit(1, 1), own("ram_board_t3", 1), own("ram_die768_m", 8)},
                new ItemStack[] {own("ram_bar768_m8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:458
        add("ComputerBuilding.java:458", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(900L).euPerTick(2700L),
                new ItemStack[] {circuit(2, 1), own("ram_board_t3", 1), own("ram_die768_m", 16)},
                new ItemStack[] {own("ram_bar768_m16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:459
        add("ComputerBuilding.java:459", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(3200L).euPerTick(2500L),
                new ItemStack[] {circuit(3, 1), own("ram_board_t3", 1), own("ram_die768_m", 32)},
                new ItemStack[] {own("ram_bar768_m32", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // ComputerBuilding.java:461
        add("ComputerBuilding.java:461", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(4000L),
                new ItemStack[] {circuit(0, 1), own("ram_board_t3", 1), own("ram_die2_g", 4)},
                new ItemStack[] {own("ram_bar2_g4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:462
        add("ComputerBuilding.java:462", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(4400L),
                new ItemStack[] {circuit(1, 1), own("ram_board_t3", 1), own("ram_die2_g", 8)},
                new ItemStack[] {own("ram_bar2_g8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:463
        add("ComputerBuilding.java:463", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(900L).euPerTick(4900L),
                new ItemStack[] {circuit(2, 1), own("ram_board_t3", 1), own("ram_die2_g", 16)},
                new ItemStack[] {own("ram_bar2_g16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:464
        add("ComputerBuilding.java:464", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(3200L).euPerTick(5100L),
                new ItemStack[] {circuit(3, 1), own("ram_board_t3", 1), own("ram_die2_g", 32)},
                new ItemStack[] {own("ram_bar2_g32", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // Fusion.java:89
        add("Fusion.java:89", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(600L).euPerTick(8192L),
                new ItemStack[] {own("metastable_tantalum", 2), form("wireFine", "Platinum", 16), own("computer_ultimate_circuits", 1)},
                new ItemStack[] {own("quantum_control_element", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Fusion.java:94
        add("Fusion.java:94", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {own("superheavy_nuclide_precursor", 1), own("nuclear_target_substrate", 1), own("metastable_tantalum", 1)},
                new ItemStack[] {own("naquadria_precursor", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:133
        add("Fusion.java:133", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(600L).euPerTick(8192L),
                new ItemStack[] {form("plateDense", "Tungstensteel", 4), form("plate", "Glass", 8), form("wireFine", "Platinum", 16)},
                new ItemStack[] {block("laser_focusing_array", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Fusion.java:135
        add("Fusion.java:135", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(600L).euPerTick(8192L),
                new ItemStack[] {gt("casing_machine_titanium", 2), gt("pipe_small_titanium", 4), form("plate", "Tantalum", 2)},
                new ItemStack[] {block("laser_target_injector", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Fusion.java:137
        add("Fusion.java:137", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {gt("casing_machine_osmiumelemental", 2), form("plateDense", "OsmiumElemental", 2), form("wireFine", "Platinum", 32)},
                new ItemStack[] {block("laser_pulse_storage", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:139
        add("Fusion.java:139", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(600L).euPerTick(8192L),
                new ItemStack[] {form("plateDense", "Lead", 4), form("plate", "Tungstensteel", 2), form("foil", "Lithium", 8)},
                new ItemStack[] {block("laser_product_collector", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // tileEntityInit0.java:687
        add("tileEntityInit0.java:687", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1600L).euPerTick(4096L),
                new ItemStack[] {form("plateDouble", "Lead", 4), form("plateDouble", "Tungstensteel", 2), form("dustCommercialPure", "Lead", 1)},
                new ItemStack[] {block("high_shield_laminated_wall", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // tileEntityInit0.java:688
        add("tileEntityInit0.java:688", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(8192L),
                new ItemStack[] {form("plateDouble", "Titanium", 4), form("dustAnalyticalPure", "Titanium", 2), gt("compact_electric_motor_hv", 1)},
                new ItemStack[] {block("counter_rotating_mass_ring", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // tileEntityInit0.java:689
        add("tileEntityInit0.java:689", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(3200L).euPerTick(12288L),
                new ItemStack[] {form("plateDouble", "Tungstensteel", 4), form("dustAnalyticalPure", "Tungsten", 2), gt("compact_signal_emitter_hv", 1)},
                new ItemStack[] {block("gyro_stabilization_anchor", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 24)},
                NO_FLUIDS);
        // tileEntityInit0.java:691
        add("tileEntityInit0.java:691", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(2000L).euPerTick(8192L),
                new ItemStack[] {form("plateDouble", "StainlessSteel", 4), gt("wire_04_tungsten", 4), form("dustAnalyticalPure", "Tungsten", 1)},
                new ItemStack[] {block("room_energy_port", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:718
        add("tileEntityInit0.java:718", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1200L).euPerTick(8192L),
                new ItemStack[] {form("plate", "Naquadah", 1), block("quantum_coherence_casing", 1), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("quantum_compute_node_naquadah", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:84
        add("Circuits.java:84", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(16L),
                new ItemStack[] {form("plate", "Wood", 1), form("dust", "SiliconDioxide", 1)},
                new ItemStack[] {gt("circuit_plate", 1)},
                new FluidStack[] {field("Glue", 288)},
                NO_FLUIDS);
        // Circuits.java:88
        add("Circuits.java:88", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(16L),
                new ItemStack[] {form("foil", "Bakelite", 6), form("foil", "EpoxyResin", 6)},
                new ItemStack[] {own("circuit_board_empty_t3", 1)},
                new FluidStack[] {field("Glass", 576)},
                NO_FLUIDS);
        // CompactItem.java:159
        add("CompactItem.java:159", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(64L).euPerTick(240L),
                new ItemStack[] {own("piezoelectric_ceramic_plate", 1), form("wireFine", "Silver", 1)},
                new ItemStack[] {own("ultrasonic_generator", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 15)},
                NO_FLUIDS);
        // ComputerBuilding.java:403
        add("ComputerBuilding.java:403", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(280L).euPerTick(90L),
                new ItemStack[] {own("cpu_board_t1", 1), own("cpu_die_tf3386", 1)},
                new ItemStack[] {own("cputf3386", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:404
        add("ComputerBuilding.java:404", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(280L).euPerTick(128L),
                new ItemStack[] {own("cpu_board_t1", 1), own("cpu_die_tf3386_s", 1)},
                new ItemStack[] {own("cputf3386_s", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:405
        add("ComputerBuilding.java:405", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(280L).euPerTick(200L),
                new ItemStack[] {own("cpu_board_t1", 1), own("cpu_die_tf3586", 1)},
                new ItemStack[] {own("cputf3586", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:406
        add("ComputerBuilding.java:406", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(280L).euPerTick(240L),
                new ItemStack[] {own("cpu_board_t1", 1), own("cpu_die_tf3586_s", 1)},
                new ItemStack[] {own("cputf3586_s", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:407
        add("ComputerBuilding.java:407", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(280L).euPerTick(260L),
                new ItemStack[] {own("cpu_board_t2", 1), own("cpu_die_gt1000", 1)},
                new ItemStack[] {own("cpugt1000", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:408
        add("ComputerBuilding.java:408", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(280L).euPerTick(280L),
                new ItemStack[] {own("cpu_board_t2", 1), own("cpu_die_gt1090", 1)},
                new ItemStack[] {own("cpugt1090", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:409
        add("ComputerBuilding.java:409", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(280L).euPerTick(360L),
                new ItemStack[] {own("cpu_board_t2", 1), own("cpu_die_gt2000", 1)},
                new ItemStack[] {own("cpugt2000", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:410
        add("ComputerBuilding.java:410", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(280L).euPerTick(420L),
                new ItemStack[] {own("cpu_board_t2", 1), own("cpu_die_gt2090", 1)},
                new ItemStack[] {own("cpugt2090", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:412
        add("ComputerBuilding.java:412", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(3200L).euPerTick(420L),
                new ItemStack[] {own("cpu_board_t3", 1), own("cpu_die_gt3660", 1)},
                new ItemStack[] {own("cpugt3660", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:413
        add("ComputerBuilding.java:413", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(3700L).euPerTick(520L),
                new ItemStack[] {own("cpu_board_t3", 1), own("cpu_die_gt3660_v2", 1)},
                new ItemStack[] {own("cpugt3660_v2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:414
        add("ComputerBuilding.java:414", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(4500L).euPerTick(600L),
                new ItemStack[] {own("cpu_board_t3", 1), own("cpu_die_gt3660_v3", 1)},
                new ItemStack[] {own("cpugt3660_v3", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:107
        add("Fusion.java:107", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(200L).euPerTick(2048L),
                new ItemStack[] {form("dustAnalyticalPure", "Titanium", 2), form("dustAnalyticalPure", "Deuterium", 1), form("dustAnalyticalPure", "Tritium", 1)},
                new ItemStack[] {own("laser_target_dt", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // tileEntityInit0.java:601
        add("tileEntityInit0.java:601", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(800L).euPerTick(200L),
                new ItemStack[] {circuit(0, 1), form("plateDense", "ElectricalSteel", 4)},
                new ItemStack[] {block("magnetic_conductor", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // tileEntityInit0.java:690
        add("tileEntityInit0.java:690", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(1600L).euPerTick(4096L),
                new ItemStack[] {form("plateDouble", "Titanium", 4), gt("compact_electric_motor_hv", 1)},
                new ItemStack[] {block("sealed_access_hatch", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // Circuits.java:86
        add("Circuits.java:86", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(16L),
                new ItemStack[] {form("foil", "Bakelite", 4)},
                new ItemStack[] {own("circuit_board_empty_t2", 1)},
                new FluidStack[] {field("Glue", 288)},
                NO_FLUIDS);
        // Fusion.java:112
        add("Fusion.java:112", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(200L).euPerTick(2048L),
                new ItemStack[] {form("dustAnalyticalPure", "Lithium", 2), form("dustAnalyticalPure", "Deuterium", 1)},
                new ItemStack[] {own("laser_target_li6", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Fusion.java:116
        add("Fusion.java:116", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(4096L),
                new ItemStack[] {form("dustAnalyticalPure", "Lead", 2), form("dustAnalyticalPure", "Deuterium", 1)},
                new ItemStack[] {own("laser_target_lead", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Fusion.java:120
        add("Fusion.java:120", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(4096L),
                new ItemStack[] {form("dustAnalyticalPure", "Tantalum", 2), form("dustAnalyticalPure", "Deuterium", 1)},
                new ItemStack[] {own("laser_target_tantalum", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Fusion.java:124
        add("Fusion.java:124", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(300L).euPerTick(4096L),
                new ItemStack[] {form("dustAnalyticalPure", "Carbon", 4), form("dustAnalyticalPure", "Deuterium", 1)},
                new ItemStack[] {own("laser_target_graphite", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Fusion.java:128
        add("Fusion.java:128", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(600L).euPerTick(8192L),
                new ItemStack[] {form("dustAnalyticalPure", "Bismuth", 2), form("dustAnalyticalPure", "Deuterium", 2)},
                new ItemStack[] {own("laser_target_bismuth", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ResearchRecipes.java:140
        add("ResearchRecipes.java:140", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(128L).euPerTick(16L),
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_carbondioxide", 1)},
                new FluidStack[] {field("CarbonDioxide", 1000)},
                NO_FLUIDS);
        // tileEntityInit0.java:598
        add("tileEntityInit0.java:598", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(800L).euPerTick(200L),
                new ItemStack[] {circuit(7, 1), form("ingotDouble", "VanadiumSteel", 6)},
                new ItemStack[] {block("enhanced_frame", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // tileEntityInit0.java:599
        add("tileEntityInit0.java:599", RecipeMaps.CIRCUIT_ASSEMBLER, RecipeTiming.ticks(800L).euPerTick(200L),
                new ItemStack[] {circuit(7, 1), form("plateDouble", "StainlessSteel", 4)},
                new ItemStack[] {block("chemical_reactor_pipe", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void laserCutter() {
        // ComputerBuilding.java:56
        add("ComputerBuilding.java:56", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(140L).euPerTick(64L),
                new ItemStack[] {circuit(1, 1), gt("circuit_plate_copper", 1)},
                new ItemStack[] {own("ram_board_t1", 14)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:57
        add("ComputerBuilding.java:57", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(64L),
                new ItemStack[] {circuit(1, 1), gt("circuit_plate_gold", 1)},
                new ItemStack[] {own("ram_board_t2", 14)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:58
        add("ComputerBuilding.java:58", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(1000L).euPerTick(64L),
                new ItemStack[] {circuit(1, 1), gt("circuit_plate_platinum", 1)},
                new ItemStack[] {own("ram_board_t3", 14)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:60
        add("ComputerBuilding.java:60", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(140L).euPerTick(64L),
                new ItemStack[] {circuit(0, 1), gt("circuit_plate_copper", 1)},
                new ItemStack[] {own("cpu_board_t1", 25)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:61
        add("ComputerBuilding.java:61", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(64L),
                new ItemStack[] {circuit(0, 1), gt("circuit_plate_gold", 1)},
                new ItemStack[] {own("cpu_board_t2", 25)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:62
        add("ComputerBuilding.java:62", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(1000L).euPerTick(64L),
                new ItemStack[] {circuit(0, 1), gt("circuit_plate_platinum", 1)},
                new ItemStack[] {own("cpu_board_t3", 25)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Chemistry.java:54
        add("Chemistry.java:54", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(480L).euPerTick(300L),
                new ItemStack[] {form("plate", "Nickel", 8)},
                new ItemStack[] {own("ktfru.item.battery.pole.nickel", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Chemistry.java:56
        add("Chemistry.java:56", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(480L).euPerTick(300L),
                new ItemStack[] {form("plate", "Platinum", 8)},
                new ItemStack[] {own("ktfru.item.battery.pole.platinum", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Chemistry.java:133
        add("Chemistry.java:133", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(200L).euPerTick(500L),
                new ItemStack[] {form("plate", "Graphene", 1)},
                new ItemStack[] {form("stick", "Graphene", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:163
        add("ComputerBuilding.java:163", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(2000L).euPerTick(96L),
                new ItemStack[] {form("bouleGt", "Silicon", 1)},
                new ItemStack[] {own("silicon_plate_t1", 11)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:164
        add("ComputerBuilding.java:164", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(6000L).euPerTick(96L),
                new ItemStack[] {own("silicon_boule_pure", 1)},
                new ItemStack[] {own("silicon_plate_t2", 9)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:166
        add("ComputerBuilding.java:166", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(2000L).euPerTick(2048L),
                new ItemStack[] {own("mo_o2_boule", 1)},
                new ItemStack[] {own("mo_o2_plate_t1", 9)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:167
        add("ComputerBuilding.java:167", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(6000L).euPerTick(2048L),
                new ItemStack[] {own("mo_o2_boule_pure", 1)},
                new ItemStack[] {own("mo_o2_plate_t2", 9)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:370
        add("ComputerBuilding.java:370", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(120L),
                new ItemStack[] {own("cpu_wafer_t1_checked", 1)},
                new ItemStack[] {own("cpu_die_tf3386", 10), own("cpu_die_tf3386_s", 7)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3500, 1200},
                null,
                null);
        // ComputerBuilding.java:371
        add("ComputerBuilding.java:371", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(250L),
                new ItemStack[] {own("cpu_wafer_t2_checked", 1)},
                new ItemStack[] {own("cpu_die_tf3586", 12), own("cpu_die_tf3586_s", 5)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2000, 800},
                null,
                null);
        // ComputerBuilding.java:372
        add("ComputerBuilding.java:372", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(500L),
                new ItemStack[] {own("cpu_wafer_t3_checked", 1)},
                new ItemStack[] {own("cpu_die_gt1000", 12), own("cpu_die_gt1090", 5)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2500, 900},
                null,
                null);
        // ComputerBuilding.java:373
        add("ComputerBuilding.java:373", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(1000L).euPerTick(1000L),
                new ItemStack[] {own("cpu_wafer_t4_checked", 1)},
                new ItemStack[] {own("cpu_die_gt2000", 12), own("cpu_die_gt2090", 5)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {1700, 650},
                null,
                null);
        // ComputerBuilding.java:374
        add("ComputerBuilding.java:374", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(10000L).euPerTick(800L),
                new ItemStack[] {own("cpu_wafer_t5_checked", 1)},
                new ItemStack[] {own("cpu_die_gt3660", 25), own("cpu_die_gt3680", 12)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {1400, 450},
                null,
                null);
        // ComputerBuilding.java:375
        add("ComputerBuilding.java:375", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(24000L).euPerTick(800L),
                new ItemStack[] {own("cpu_wafer_t6_checked", 1)},
                new ItemStack[] {own("cpu_die_gt3660_v2", 25), own("cpu_die_gt3680_v2", 12)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {1000, 300},
                null,
                null);
        // ComputerBuilding.java:376
        add("ComputerBuilding.java:376", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(56000L).euPerTick(780L),
                new ItemStack[] {own("cpu_wafer_t7_checked", 1)},
                new ItemStack[] {own("cpu_die_gt3660_v3", 25), own("cpu_die_gt3680_v3", 12)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {700, 150},
                null,
                null);
        // ComputerBuilding.java:377
        add("ComputerBuilding.java:377", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(72000L).euPerTick(760L),
                new ItemStack[] {own("cpu_wafer_t8_checked", 1)},
                new ItemStack[] {own("cpu_die_gt3660_v4", 25), own("cpu_die_gt3680_v4", 12)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {400, 100},
                null,
                null);
        // ComputerBuilding.java:379
        add("ComputerBuilding.java:379", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(80L),
                new ItemStack[] {own("ram_wafer_t1_checked", 1)},
                new ItemStack[] {own("ram_die2_k", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {5000},
                null,
                null);
        // ComputerBuilding.java:380
        add("ComputerBuilding.java:380", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(210L),
                new ItemStack[] {own("ram_wafer_t2_checked", 1)},
                new ItemStack[] {own("ram_die32_k", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3000},
                null,
                null);
        // ComputerBuilding.java:381
        add("ComputerBuilding.java:381", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(480L),
                new ItemStack[] {own("ram_wafer_t3_checked", 1)},
                new ItemStack[] {own("ram_die256_k", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {4300},
                null,
                null);
        // ComputerBuilding.java:382
        add("ComputerBuilding.java:382", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(1000L).euPerTick(940L),
                new ItemStack[] {own("ram_wafer_t4_checked", 1)},
                new ItemStack[] {own("ram_die2_m", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3800},
                null,
                null);
        // ComputerBuilding.java:383
        add("ComputerBuilding.java:383", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(6000L).euPerTick(560L),
                new ItemStack[] {own("ram_wafer_t5_checked", 1)},
                new ItemStack[] {own("ram_die16_m", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3400},
                null,
                null);
        // ComputerBuilding.java:384
        add("ComputerBuilding.java:384", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(7000L).euPerTick(540L),
                new ItemStack[] {own("ram_wafer_t6_checked", 1)},
                new ItemStack[] {own("ram_die128_m", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3000},
                null,
                null);
        // ComputerBuilding.java:385
        add("ComputerBuilding.java:385", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(8000L).euPerTick(500L),
                new ItemStack[] {own("ram_wafer_t7_checked", 1)},
                new ItemStack[] {own("ram_die768_m", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2600},
                null,
                null);
        // ComputerBuilding.java:386
        add("ComputerBuilding.java:386", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(9000L).euPerTick(430L),
                new ItemStack[] {own("ram_wafer_t8_checked", 1)},
                new ItemStack[] {own("ram_die2_g", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2100},
                null,
                null);
        // ComputerBuilding.java:388
        add("ComputerBuilding.java:388", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(64L),
                new ItemStack[] {own("circuit_part_wafer_t3_checked", 1)},
                new ItemStack[] {gt("circuit_part_advanced", 40), gt("circuit_part_good", 10)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {4000, 1000},
                null,
                null);
        // ComputerBuilding.java:389
        add("ComputerBuilding.java:389", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(170L),
                new ItemStack[] {own("circuit_part_wafer_t4_checked", 1)},
                new ItemStack[] {gt("circuit_part_elite", 40), gt("circuit_part_advanced", 10)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2400, 1000},
                null,
                null);
        // ComputerBuilding.java:390
        add("ComputerBuilding.java:390", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(440L),
                new ItemStack[] {own("circuit_part_wafer_t5_checked", 1)},
                new ItemStack[] {gt("circuit_part_master", 40), gt("circuit_part_elite", 10)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3200, 1500},
                null,
                null);
        // ComputerBuilding.java:391
        add("ComputerBuilding.java:391", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(1000L).euPerTick(860L),
                new ItemStack[] {own("circuit_part_wafer_t6_checked", 1)},
                new ItemStack[] {gt("circuit_part_ultimate", 40), gt("circuit_part_master", 10)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2600, 1900},
                null,
                null);
        // ComputerBuilding.java:393
        add("ComputerBuilding.java:393", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(4000L).euPerTick(170L),
                new ItemStack[] {own("inter_layer_wafer_t7_checked", 1)},
                new ItemStack[] {own("inter_layer_t1", 21)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {4800},
                null,
                null);
        // ComputerBuilding.java:394
        add("ComputerBuilding.java:394", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(5000L).euPerTick(170L),
                new ItemStack[] {own("inter_layer_wafer_t8_checked", 1)},
                new ItemStack[] {own("inter_layer_t2", 21)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {4800},
                null,
                null);
        // ComputerBuilding.java:396
        add("ComputerBuilding.java:396", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(170L),
                new ItemStack[] {own("diode_wafer_t1_checked", 1)},
                new ItemStack[] {own("diode_t2_part", 64), own("diode_t2_part", 24)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {4800, 4800},
                null,
                null);
        // ComputerBuilding.java:397
        add("ComputerBuilding.java:397", RecipeMaps.LASER_CUTTER, RecipeTiming.ticks(400L).euPerTick(440L),
                new ItemStack[] {own("diode_wafer_t2_checked", 1)},
                new ItemStack[] {own("diode_t3_part", 64), own("diode_t3_part", 24)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3200, 3200},
                null,
                null);
    }

    private static void heatMixer() {
        // Plastic.java:151
        add("Plastic.java:151", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(310L),
                new ItemStack[] {form("dust", "Gold", 1), form("dust", "Lead", 1)},
                new ItemStack[] {form("dust", "VinylAcetate", 1)},
                new FluidStack[] {field("Ethylene", 100), field("GenLiquid_GlacialAceticAcid", 100), field("Oxygen", 100)},
                new FluidStack[] {field("CarbonDioxide", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0, 1},
                null);
        // OilProcessing.java:32
        add("OilProcessing.java:32", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(100L).euPerTick(32L),
                new ItemStack[] {form("dust", "Copper", 1), form("dustTiny", "Zinc", 1), form("dustTiny", "Chromium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 200), field("GenGas_CarbonMonoxide", 100)},
                new FluidStack[] {field("GenLiquid_Methanol", 120)},
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // Plastic.java:61
        add("Plastic.java:61", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(256L),
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Propylene", 100), field("Oxygen", 100), field("Hydrogen", 100)},
                new FluidStack[] {field("GenLiquid_AllylAlcohol", 100)},
                0L,
                null,
                new int[] {0},
                null);
        // Plastic.java:80
        add("Plastic.java:80", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(320L),
                new ItemStack[] {own("proton_exchange_membrane", 1), form("dust", "BPA", 1), form("dust", "SodiumHydroxide", 2)},
                new ItemStack[] {form("dust", "EpoxyResin", 6)},
                new FluidStack[] {field("GenLiquid_Epichlorohydrin", 1000)},
                new FluidStack[] {field("Saltwater", 800)},
                0L,
                null,
                new int[] {0},
                null);
        // Plastic.java:141
        add("Plastic.java:141", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(100L).euPerTick(210L),
                new ItemStack[] {form("dust", "SodiumCarbonate", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_TFES", 100), field("GenGas_HFPO", 100), field("GenLiquid_Methoxymethane", 1)},
                new FluidStack[] {field("GenLiquid_PrecursorPSVE", 100)},
                0L,
                null,
                null,
                new int[] {2});
        // OilProcessing.java:48
        add("OilProcessing.java:48", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(14L).euPerTick(80L),
                new ItemStack[] {form("dust", "Nickel", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 40), field("GenLiquid_OilDesulfurizationer", 4), field("GenLiquid_DesaltOilLight", 100)},
                new FluidStack[] {field("GenLiquid_CleanedOilLight", 100), field("GenLiquid_SulfuredOilDesulfurizationer", 4)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:49
        add("OilProcessing.java:49", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(20L).euPerTick(80L),
                new ItemStack[] {form("dust", "Nickel", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 40), field("GenLiquid_OilDesulfurizationer", 6), field("GenLiquid_DesaltOilNormal", 100)},
                new FluidStack[] {field("GenLiquid_CleanedOilNormal", 100), field("GenLiquid_SulfuredOilDesulfurizationer", 6)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:50
        add("OilProcessing.java:50", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(26L).euPerTick(80L),
                new ItemStack[] {form("dust", "Nickel", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 40), field("GenLiquid_OilDesulfurizationer", 8), field("GenLiquid_DesaltOilMedium", 100)},
                new FluidStack[] {field("GenLiquid_CleanedOilMedium", 100), field("GenLiquid_SulfuredOilDesulfurizationer", 8)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:51
        add("OilProcessing.java:51", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(32L).euPerTick(80L),
                new ItemStack[] {form("dust", "Nickel", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 40), field("GenLiquid_OilDesulfurizationer", 10), field("GenLiquid_DesaltOilHeavy", 100)},
                new FluidStack[] {field("GenLiquid_CleanedOilHeavy", 100), field("GenLiquid_SulfuredOilDesulfurizationer", 10)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:52
        add("OilProcessing.java:52", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(38L).euPerTick(80L),
                new ItemStack[] {form("dust", "Nickel", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 40), field("GenLiquid_OilDesulfurizationer", 12), field("GenLiquid_DesaltOilExtraHeavy", 100)},
                new FluidStack[] {field("GenLiquid_CleanedOilExtraHeavy", 100), field("GenLiquid_SulfuredOilDesulfurizationer", 12)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:97
        add("OilProcessing.java:97", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(200L).euPerTick(64L),
                new ItemStack[] {form("dust", "Iron", 1), form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Methane", 100), field("Oxygen", 100)},
                new FluidStack[] {field("GenGas_Formaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0, 1},
                null);
        // OilProcessing.java:108
        add("OilProcessing.java:108", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(30L).euPerTick(47L),
                new ItemStack[] {form("dust", "Cobalt", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("GenGas_CarbonMonoxide", 50), field("Hydrogen", 50)},
                new FluidStack[] {field("GenGas_Propionaldehyde", 100)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:109
        add("OilProcessing.java:109", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(30L).euPerTick(47L),
                new ItemStack[] {form("dust", "Rhodium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("GenGas_CarbonMonoxide", 50), field("Hydrogen", 50)},
                new FluidStack[] {field("GenGas_Propionaldehyde", 100)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:117
        add("OilProcessing.java:117", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(55L).euPerTick(137L),
                new ItemStack[] {form("dust", "Silver", 1), form("dust", "Rhodium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("Water", 100)},
                new FluidStack[] {field("GenLiquid_Propanol", 100)},
                0L,
                null,
                new int[] {0, 1},
                null);
        // OilProcessing.java:164
        add("OilProcessing.java:164", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(170L),
                new ItemStack[] {form("dust", "Rhodium", 1), form("dust", "Iodine", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 1000), field("GenGas_CarbonMonoxide", 1000)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 1000)},
                0L,
                null,
                new int[] {0, 1},
                null);
        // OilProcessing.java:165
        add("OilProcessing.java:165", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(100L).euPerTick(170L),
                new ItemStack[] {form("dust", "Iridium", 1), form("dust", "Iodine", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 1000), field("GenGas_CarbonMonoxide", 1000)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 1000)},
                0L,
                null,
                new int[] {0, 1},
                null);
        // OilProcessing.java:167
        add("OilProcessing.java:167", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(160L).euPerTick(40L),
                new ItemStack[] {circuit(0, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Naphtha", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedNaphthaLow", 1000)},
                0L,
                null,
                new int[] {1},
                null);
        // OilProcessing.java:168
        add("OilProcessing.java:168", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(150L).euPerTick(120L),
                new ItemStack[] {circuit(1, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Naphtha", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedNaphthaMedium", 1000)},
                0L,
                null,
                new int[] {1},
                null);
        // OilProcessing.java:169
        add("OilProcessing.java:169", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(140L).euPerTick(420L),
                new ItemStack[] {circuit(2, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Naphtha", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedNaphthaHigh", 1000)},
                0L,
                null,
                new int[] {1},
                null);
        // OilProcessing.java:177
        add("OilProcessing.java:177", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(160L).euPerTick(40L),
                new ItemStack[] {circuit(0, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Petrol", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedPetrolLow", 1000)},
                0L,
                null,
                new int[] {1},
                null);
        // OilProcessing.java:178
        add("OilProcessing.java:178", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(150L).euPerTick(120L),
                new ItemStack[] {circuit(1, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Petrol", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedPetrolMedium", 1000)},
                0L,
                null,
                new int[] {1},
                null);
        // OilProcessing.java:179
        add("OilProcessing.java:179", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(140L).euPerTick(420L),
                new ItemStack[] {circuit(2, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Petrol", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedPetrolHigh", 1000)},
                0L,
                null,
                new int[] {1},
                null);
        // PlatinumGroupProcess.java:41
        add("PlatinumGroupProcess.java:41", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(90L).euPerTick(180L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 1000), field("GenLiquid_GlacialAceticAcid", 1000), field("GenGas_Chlorine", 1000), field("GenLiquid_SulfuricAcid", 1)},
                new FluidStack[] {field("GenLiquid_EthylChloroacetate", 1000)},
                0L,
                null,
                null,
                new int[] {3});
        // Chemistry.java:58
        add("Chemistry.java:58", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(800L).euPerTick(500L),
                new ItemStack[] {form("dust", "Yttrium", 4), form("dust", "Zirconium", 4)},
                NO_ITEMS,
                new FluidStack[] {field("Oxygen", 4000)},
                new FluidStack[] {field("GenLiquid_YttriumZirconiumOxide", 1000)});
        // Chemistry.java:123
        add("Chemistry.java:123", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(120L),
                new ItemStack[] {form("dust", "Palladium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Butynediol", 1000), field("Hydrogen", 1000)},
                new FluidStack[] {field("GenLiquid_Butanediol", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // Chemistry.java:137
        add("Chemistry.java:137", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(120L),
                new ItemStack[] {form("dust", "AluminumChloride", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Benzene", 200), field("GenLiquid_Chloromethane", 100)},
                new FluidStack[] {field("GenLiquid_Toluene", 200), field("GenGas_HydrochloricAcid", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // Chemistry.java:156
        add("Chemistry.java:156", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(250L),
                new ItemStack[] {circuit(0, 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_PurifyTitaniumAcylate", 1000), field("GenLiquid_SulfuricAcid", 2000)},
                new FluidStack[] {field("GenLiquid_TitaniumAcylate", 1000)});
        // Plastic.java:66
        add("Plastic.java:66", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(110L).euPerTick(110L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_CarbonMonoxide", 100), field("Propylene", 100), field("GenGas_HydrogenFluoride", 1)},
                new FluidStack[] {field("GenLiquid_MethacrylicAcid", 100)},
                0L,
                null,
                null,
                new int[] {2});
        // Plastic.java:69
        add("Plastic.java:69", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1100L).euPerTick(110L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "HardPlastic", 2)},
                new FluidStack[] {field("GenLiquid_SolutionBPASodium", 1000), field("GenGas_Phosgene", 200), field("GenLiquid_Dichloromethane", 200)},
                NO_FLUIDS);
        // Plastic.java:71
        add("Plastic.java:71", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(142L),
                new ItemStack[] {form("dust", "Zinc", 1)},
                new ItemStack[] {form("dust", "ZincChloride", 3)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 1600), field("GenLiquid_Chloroform", 400)},
                new FluidStack[] {field("GenGas_Tetrafluoroethylene", 400), field("GenGas_HydrochloricAcid", 2000)});
        // Plastic.java:85
        add("Plastic.java:85", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(180L),
                new ItemStack[] {form("dust", "SodiumHydroxide", 1)},
                new ItemStack[] {form("dust", "Bakelite", 8)},
                new FluidStack[] {field("GenLiquid_Phenol", 1000), field("GenGas_Formaldehyde", 1000)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0},
                null);
        // Plastic.java:87
        add("Plastic.java:87", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1000L).euPerTick(210L),
                new ItemStack[] {form("dust", "Magnesium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Chloromethane", 300), field("GenLiquid_Methoxymethane", 1)},
                new FluidStack[] {field("GenLiquid_MethylmagnesiumChloride", 300)},
                0L,
                null,
                null,
                new int[] {1});
        // Plastic.java:91
        add("Plastic.java:91", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(600L).euPerTick(500L),
                new ItemStack[] {form("dust", "Iron", 1), form("dust", "Silicon", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Chlorine", 2000)},
                new FluidStack[] {field("GenLiquid_Tetrachorosilane", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // Plastic.java:99
        add("Plastic.java:99", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(2080L).euPerTick(256L),
                new ItemStack[] {form("dust", "Silicon", 1), form("dust", "Brass", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Chloromethane", 200)},
                new FluidStack[] {field("GenLiquid_Dichlorodimethylsilane", 100)},
                0L,
                null,
                new int[] {1},
                null);
        // Plastic.java:101
        add("Plastic.java:101", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(420L).euPerTick(1446L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Dichlorodimethylsilane", 100), field("BioEthanol", 100), field("GenLiquid_SulfuricAcid", 1)},
                new FluidStack[] {field("GenLiquid_SiliconeRubber", 100)},
                0L,
                null,
                null,
                new int[] {2});
        // Plastic.java:123
        add("Plastic.java:123", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(40L).euPerTick(412L),
                new ItemStack[] {form("dust", "Chromium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Ethylbenzene", 100), field("Water", 50)},
                new FluidStack[] {field("GenLiquid_Styrene", 100), field("Hydrogen", 50)},
                0L,
                null,
                new int[] {0},
                null);
        // Plastic.java:131
        add("Plastic.java:131", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(120L),
                new ItemStack[] {form("dust", "TriethylAluminium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 1), field("GenLiquid_Isoprene", 144)},
                new FluidStack[] {field("Latex", 720)},
                0L,
                null,
                new int[] {0},
                new int[] {0});
        // Plastic.java:139
        add("Plastic.java:139", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(160L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_HPF", 100), field("Oxygen", 100), field("GenLiquid_Benzene", 1)},
                new FluidStack[] {field("GenGas_HFPO", 100)},
                0L,
                null,
                null,
                new int[] {2});
        // Plastic.java:145
        add("Plastic.java:145", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(100L).euPerTick(210L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "PSVE", 1)},
                new FluidStack[] {field("GenLiquid_PSVE", 100), field("GenGas_HPF", 100), field("GenGas_Tetrafluoroethylene", 100)},
                NO_FLUIDS);
        // Plastic.java:155
        add("Plastic.java:155", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(240L).euPerTick(262L),
                new ItemStack[] {form("dust", "VinylAcetate", 1)},
                new ItemStack[] {form("dust", "PolyVinylAcetate", 1)},
                new FluidStack[] {field("GenLiquid_HydrogenPeroxide", 50), field("GenLiquid_Methanol", 1)},
                new FluidStack[] {field("Water", 50)},
                0L,
                null,
                null,
                new int[] {1});
        // CompactItem.java:157
        add("CompactItem.java:157", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(64L).euPerTick(240L),
                new ItemStack[] {form("dust", "Barium", 1), form("dust", "Rutile", 1)},
                new ItemStack[] {form("dust", "BariumTitanate", 1)},
                new FluidStack[] {field("CarbonDioxide", 1000)},
                NO_FLUIDS);
        // ComputerBuilding.java:66
        add("ComputerBuilding.java:66", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(32L),
                new ItemStack[] {form("dust", "Naphthalene", 1)},
                new ItemStack[] {form("dust", "Naphthalenol", 1)},
                new FluidStack[] {field("GenGas_Chlorine", 200), field("Water", 200)},
                new FluidStack[] {field("GenGas_HydrochloricAcid", 200)});
        // ComputerBuilding.java:72
        add("ComputerBuilding.java:72", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(90L).euPerTick(160L),
                new ItemStack[] {form("dust", "Bakelite", 8), form("dust", "DiazoNaphthol", 8)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_PGMEA", 1000)},
                new FluidStack[] {field("GenLiquid_DNQPhotoresist", 1000)});
        // ComputerBuilding.java:74
        add("ComputerBuilding.java:74", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(240L),
                new ItemStack[] {form("dust", "Palladium", 1)},
                new ItemStack[] {form("dust", "Cyclohexanol", 1)},
                new FluidStack[] {field("GenLiquid_Phenol", 100), field("Hydrogen", 100)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0},
                null);
        // ComputerBuilding.java:75
        add("ComputerBuilding.java:75", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(240L),
                new ItemStack[] {form("dust", "Silver", 1), form("dust", "Cyclohexanol", 1)},
                new ItemStack[] {form("dust", "Cyclohexanone", 1)},
                new FluidStack[] {field("Oxygen", 100)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0},
                null);
        // ComputerBuilding.java:81
        add("ComputerBuilding.java:81", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(1440L),
                new ItemStack[] {form("dust", "Nitroacenaphthene", 8), form("dust", "PolymethylMethacrylate", 6)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Cyclohexanone", 1000)},
                new FluidStack[] {field("GenLiquid_PMMAPhotoresist", 1000)});
        // ComputerBuilding.java:125
        add("ComputerBuilding.java:125", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(120L),
                new ItemStack[] {form("dust", "Salt", 1)},
                new ItemStack[] {form("dust", "SodiumHydroxide", 1), form("dust", "AmmoniumChloride", 1)},
                new FluidStack[] {field("GenGas_Ammonia", 1000), field("Water", 1000)},
                NO_FLUIDS);
        // Fusion.java:100
        add("Fusion.java:100", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {own("naquadria_precursor", 1), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustTiny", "Naquadria", 1)},
                new FluidStack[] {field("Helium", 4000)},
                NO_FLUIDS);
        // OilProcessing.java:99
        add("OilProcessing.java:99", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(20L).euPerTick(44L),
                new ItemStack[] {form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Formaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:100
        add("OilProcessing.java:100", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(20L).euPerTick(44L),
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Formaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:102
        add("OilProcessing.java:102", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(20L).euPerTick(44L),
                new ItemStack[] {form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Acetaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:103
        add("OilProcessing.java:103", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(20L).euPerTick(44L),
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Acetaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:105
        add("OilProcessing.java:105", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(30L).euPerTick(44L),
                new ItemStack[] {form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Propanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Propionaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:106
        add("OilProcessing.java:106", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(30L).euPerTick(44L),
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Propanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Propionaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0},
                null);
        // OilProcessing.java:130
        add("OilProcessing.java:130", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(40L).euPerTick(32L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_OilGas", 100), field("GenLiquid_Benzene", 5), field("Hydrogen", 80)},
                new FluidStack[] {field("Diesel", 10), field("GenGas_CrackedOilGas", 100)});
        // OilProcessing.java:162
        add("OilProcessing.java:162", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(400L).euPerTick(80L),
                new ItemStack[] {form("dust", "Manganese", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Oxygen", 500), field("GenLiquid_VinylAcetate", 1000)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // OreProcessing.java:35
        add("OreProcessing.java:35", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(400L).euPerTick(100L),
                new ItemStack[] {form("dust", "Iron", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Nitrogen", 2000), field("Hydrogen", 3000)},
                new FluidStack[] {field("GenGas_Ammonia", 2000)},
                0L,
                null,
                new int[] {0},
                null);
        // OreProcessing.java:45
        add("OreProcessing.java:45", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(120L),
                new ItemStack[] {form("dust", "Calcite", 1), form("dust", "SodiumCarbonate", 2), form("dust", "Bauxite", 1)},
                new ItemStack[] {form("dust", "CookedBauxide", 2)},
                NO_FLUIDS,
                new FluidStack[] {field("CarbonDioxide", 3000)});
        // OreProcessing.java:73
        add("OreProcessing.java:73", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(96L),
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "MagnesiumChloride", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Helium", 1), field("GenMolten_Magnesium", 288)},
                NO_FLUIDS);
        // OreProcessing.java:74
        add("OreProcessing.java:74", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(128L),
                new ItemStack[] {form("dust", "Magnesium", 2)},
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "MagnesiumChloride", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Helium", 1)},
                NO_FLUIDS);
        // OreProcessing.java:75
        add("OreProcessing.java:75", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(96L),
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "Salt", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Helium", 1), field("GenMolten_Sodium", 288)},
                NO_FLUIDS);
        // OreProcessing.java:76
        add("OreProcessing.java:76", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(128L),
                new ItemStack[] {form("dust", "Sodium", 2)},
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "Salt", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Helium", 1)},
                NO_FLUIDS);
        // OreProcessing.java:77
        add("OreProcessing.java:77", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(96L),
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "MagnesiumChloride", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Argon", 1), field("GenMolten_Magnesium", 288)},
                NO_FLUIDS);
        // OreProcessing.java:78
        add("OreProcessing.java:78", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(128L),
                new ItemStack[] {form("dust", "Magnesium", 2)},
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "MagnesiumChloride", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Argon", 1)},
                NO_FLUIDS);
        // OreProcessing.java:79
        add("OreProcessing.java:79", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(96L),
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "Salt", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Argon", 1), field("GenMolten_Sodium", 288)},
                NO_FLUIDS);
        // OreProcessing.java:80
        add("OreProcessing.java:80", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(128L),
                new ItemStack[] {form("dust", "Sodium", 2)},
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "Salt", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Argon", 1)},
                NO_FLUIDS);
        // OreProcessing.java:105
        add("OreProcessing.java:105", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(100L).euPerTick(465L),
                new ItemStack[] {form("dust", "WaxParaffin", 1), form("dust", "Pyrolusite", 1)},
                new ItemStack[] {form("dust", "OleicAcid", 1)},
                new FluidStack[] {field("Oxygen", 1000)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // PlatinumGroupProcess.java:37
        add("PlatinumGroupProcess.java:37", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(16L).euPerTick(350L),
                new ItemStack[] {form("dust", "Sulfur", 2)},
                NO_ITEMS,
                new FluidStack[] {field("Butane", 2000), field("Ethylene", 1000)},
                new FluidStack[] {field("GenLiquid_DinhexylSulfide", 1000)});
        // PlatinumGroupProcess.java:40
        add("PlatinumGroupProcess.java:40", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(90L).euPerTick(180L),
                new ItemStack[] {form("dust", "Alumina", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 2000), field("GenGas_Ammonia", 1000)},
                new FluidStack[] {field("GenLiquid_Methylamine", 400), field("GenLiquid_Dimethylamine", 300), field("GenLiquid_Trimethylamine", 300)},
                0L,
                null,
                new int[] {0},
                null);
        // PlatinumGroupProcess.java:54
        add("PlatinumGroupProcess.java:54", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(16L).euPerTick(480L),
                new ItemStack[] {form("dust", "AmmoniumChloride", 1), form("dust", "Salt", 1)},
                new ItemStack[] {form("dust", "SodiumNitrate", 2), form("dust", "AmmoniumHexachlororhodate", 6)},
                new FluidStack[] {field("GenLiquid_ElectrolyzedNitricPlatinumGroupSolution", 1000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:56
        add("PlatinumGroupProcess.java:56", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(16L).euPerTick(180L),
                new ItemStack[] {form("dust", "AmmoniumChloride", 1)},
                new ItemStack[] {form("dust", "AmmoniumHexachloroiridate", 7)},
                new FluidStack[] {field("GenLiquid_IridiumPlatinumGroupSolution", 1000), field("GenGas_Chlorine", 2000)},
                new FluidStack[] {field("GenLiquid_SulfuricAcid", 1000)});
        // Chemistry.java:43
        add("Chemistry.java:43", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(20L).euPerTick(32L),
                new ItemStack[] {form("dust", "Carbon", 1)},
                new ItemStack[] {form("dustTiny", "Sugar", 1)},
                new FluidStack[] {field("Vinegar_Rice", 100)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 30), field("Water", 60)},
                0L,
                null,
                new int[] {0},
                null);
        // Chemistry.java:44
        add("Chemistry.java:44", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(20L).euPerTick(32L),
                new ItemStack[] {form("dust", "Carbon", 1)},
                new ItemStack[] {form("dustTiny", "Sugar", 1)},
                new FluidStack[] {field("Vinegar_Apple", 100)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 20), field("Water", 70)},
                0L,
                null,
                new int[] {0},
                null);
        // Chemistry.java:45
        add("Chemistry.java:45", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(20L).euPerTick(32L),
                new ItemStack[] {form("dust", "Carbon", 1)},
                new ItemStack[] {form("dustTiny", "Sugar", 2)},
                new FluidStack[] {field("Vinegar_Cane", 100)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 20), field("Water", 70)},
                0L,
                null,
                new int[] {0},
                null);
        // Chemistry.java:46
        add("Chemistry.java:46", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(20L).euPerTick(32L),
                new ItemStack[] {form("dust", "Carbon", 1)},
                new ItemStack[] {form("dustTiny", "Sugar", 1)},
                new FluidStack[] {field("Vinegar_Grape", 100)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 20), field("Water", 70)},
                0L,
                null,
                new int[] {0},
                null);
        // Chemistry.java:48
        add("Chemistry.java:48", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(40L).euPerTick(120L),
                new ItemStack[] {form("dust", "Sodium", 1), form("dust", "Potassium", 1)},
                new ItemStack[] {form("dustTiny", "PotassiumSodium", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Chemistry.java:125
        add("Chemistry.java:125", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methylamine", 1000), field("GenLiquid_Butyrolactone", 1000)},
                new FluidStack[] {field("GenLiquid_Methylpyrrolidone", 1000), field("DistW", 1000)});
        // Chemistry.java:141
        add("Chemistry.java:141", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(800L).euPerTick(480L),
                new ItemStack[] {form("dust", "SodiumAcetate", 2)},
                new ItemStack[] {form("dust", "SodiumPersulfate", 1)},
                new FluidStack[] {field("GenLiquid_BariumSulfate", 2000)},
                new FluidStack[] {field("GenLiquid_BariumAcetate", 2000)});
        // Chemistry.java:143
        add("Chemistry.java:143", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(1040L),
                new ItemStack[] {circuit(0, 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Acetone", 1000)},
                new FluidStack[] {field("GenLiquid_Ketene", 400), field("GenLiquid_Diketene", 600), field("Methane", 1000)});
        // Chemistry.java:148
        add("Chemistry.java:148", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(200L).euPerTick(440L),
                new ItemStack[] {form("dust", "PhosphorusPentoxide", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 1000)},
                new FluidStack[] {field("GenLiquid_Ketene", 900), field("GenLiquid_Diketene", 100), field("DistW", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // Chemistry.java:150
        add("Chemistry.java:150", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(2141L),
                new ItemStack[] {form("dust", "Rutile", 2)},
                new ItemStack[] {form("dust", "TitanicSulfate", 3)},
                new FluidStack[] {field("GenLiquid_SulfuricAcid", 4000)},
                NO_FLUIDS);
        // Chemistry.java:154
        add("Chemistry.java:154", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(450L),
                new ItemStack[] {form("dust", "SodiumHydroxide", 2)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_ImpureTitaniumAcylate", 1000)},
                new FluidStack[] {field("GenLiquid_PurifyTitaniumAcylate", 1000)});
        // Chemistry.java:158
        add("Chemistry.java:158", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(800L).euPerTick(480L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "NanoBariumTitanate", 1)},
                new FluidStack[] {field("GenLiquid_TitaniumAcylate", 1000), field("GenLiquid_BariumAcetate", 1000)},
                NO_FLUIDS);
        // Plastic.java:45
        add("Plastic.java:45", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(64L),
                new ItemStack[] {form("dust", "Calcite", 1)},
                new ItemStack[] {form("dust", "CalciumAcetate", 1)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 100)},
                new FluidStack[] {field("CarbonDioxide", 1000)});
        // Plastic.java:46
        add("Plastic.java:46", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(64L),
                new ItemStack[] {form("dust", "SodiumCarbonate", 1)},
                new ItemStack[] {form("dust", "SodiumAcetate", 2)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 100)},
                new FluidStack[] {field("CarbonDioxide", 1000)});
        // Plastic.java:48
        add("Plastic.java:48", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(64L),
                new ItemStack[] {form("dust", "Zinc", 1)},
                new ItemStack[] {form("dust", "ZincOxide", 1)},
                new FluidStack[] {field("Oxygen", 1000)},
                NO_FLUIDS);
        // Plastic.java:52
        add("Plastic.java:52", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(110L).euPerTick(100L),
                new ItemStack[] {form("dust", "DiphenylCarbonate", 1), form("dust", "BPA", 1)},
                new ItemStack[] {form("dust", "HardPlastic", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Plastic.java:53
        add("Plastic.java:53", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(82L).euPerTick(64L),
                new ItemStack[] {form("dust", "Aluminium", 1)},
                new ItemStack[] {form("dust", "TriethylAluminium", 1)},
                new FluidStack[] {field("GenLiquid_Ethane", 1500)},
                NO_FLUIDS);
        // Plastic.java:65
        add("Plastic.java:65", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(148L),
                new ItemStack[] {form("dust", "SodiumHydroxide", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_DichloroPropanol", 100)},
                new FluidStack[] {field("GenLiquid_Epichlorohydrin", 100)});
        // Plastic.java:75
        add("Plastic.java:75", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(100L).euPerTick(83L),
                new ItemStack[] {form("dust", "PotassiumSulfate", 1)},
                new ItemStack[] {form("dust", "PotassiumPyrosulfate", 1)},
                new FluidStack[] {field("GenGas_SulfurTrioxide", 1000)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0},
                null);
        // Plastic.java:98
        add("Plastic.java:98", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(200L).euPerTick(168L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "MagnesiumChloride", 1)},
                new FluidStack[] {field("GenLiquid_Methyltrichlorosilane", 100), field("GenLiquid_MethylmagnesiumChloride", 100)},
                new FluidStack[] {field("GenLiquid_Dichlorodimethylsilane", 100)});
        // Plastic.java:107
        add("Plastic.java:107", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(24L),
                new ItemStack[] {form("dust", "Mercury", 1)},
                new ItemStack[] {form("dust", "MercuryIIChloride", 1)},
                new FluidStack[] {field("GenGas_Chlorine", 2000)},
                NO_FLUIDS);
        // Plastic.java:117
        add("Plastic.java:117", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(146L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "PVC", 1)},
                new FluidStack[] {field("GenLiquid_VinylChloride", 100), field("Water", 1)},
                NO_FLUIDS,
                0L,
                null,
                null,
                new int[] {1});
        // Plastic.java:119
        add("Plastic.java:119", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(156L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Benzene", 100), field("Ethylene", 100)},
                new FluidStack[] {field("GenLiquid_Ethylbenzene", 100)});
        // Plastic.java:129
        add("Plastic.java:129", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(60L).euPerTick(120L),
                new ItemStack[] {form("dust", "Chromium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Ethylene", 200)},
                new FluidStack[] {field("GenLiquid_Isoprene", 144)},
                0L,
                null,
                new int[] {0},
                null);
        // Plastic.java:133
        add("Plastic.java:133", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(100L).euPerTick(210L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Tetrafluoroethylene", 100), field("GenGas_SulfurTrioxide", 100)},
                new FluidStack[] {field("GenLiquid_TFES", 100)});
        // Plastic.java:135
        add("Plastic.java:135", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(256L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Tetrafluoroethylene", 600), field("GenGas_Acetylene", 200)},
                new FluidStack[] {field("GenGas_HPF", 200), field("GenGas_perfluorocyclobutane", 200)});
        // Plastic.java:143
        add("Plastic.java:143", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(100L).euPerTick(210L),
                new ItemStack[] {form("dust", "SodiumHydroxide", 1)},
                new ItemStack[] {form("dust", "SodiumHydrogencarbonate", 1)},
                new FluidStack[] {field("GenLiquid_PrecursorPSVE", 100)},
                new FluidStack[] {field("GenLiquid_PSVE", 100)});
        // Plastic.java:153
        add("Plastic.java:153", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(140L).euPerTick(262L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Acetylene", 100), field("GenLiquid_GlacialAceticAcid", 100)},
                new FluidStack[] {field("GenLiquid_VinylAcetate", 100)});
        // Plastic.java:161
        add("Plastic.java:161", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(200L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Propylene", 1000), field("GenGas_Chlorine", 1000)},
                new FluidStack[] {field("GenLiquid_Dichloromethane", 1000)});
        // ComputerBuilding.java:70
        add("ComputerBuilding.java:70", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(70L).euPerTick(60L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_MethoxyPropanol", 100), field("GenLiquid_GlacialAceticAcid", 100)},
                new FluidStack[] {field("GenLiquid_PGMEA", 100)});
        // ComputerBuilding.java:77
        add("ComputerBuilding.java:77", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(120L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "AmmoniumNitrate", 1)},
                new FluidStack[] {field("GenLiquid_NitricAcid", 1000), field("GenGas_Ammonia", 1000)},
                NO_FLUIDS);
        // ComputerBuilding.java:124
        add("ComputerBuilding.java:124", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(10L).euPerTick(80L),
                new ItemStack[] {form("dust", "Silicon", 1), form("dust", "Magnesium", 2)},
                new ItemStack[] {form("dust", "MagnesiumSilicide", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:130
        add("ComputerBuilding.java:130", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(40L).euPerTick(410L),
                new ItemStack[] {form("dust", "SodiumAluminiumHydride", 1)},
                new ItemStack[] {form("dust", "Cryolite", 1)},
                new FluidStack[] {field("GenGas_SiliconTetrafluoride", 1000)},
                new FluidStack[] {field("GenGas_Silane", 1000)});
        // HeatMixer.java:27
        add("HeatMixer.java:27", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(177L).euPerTick(16L),
                new ItemStack[] {form("dust", "LithiumHydroxide", 9)},
                new ItemStack[] {form("dust", "LithiumChloride", 5)},
                new FluidStack[] {field("GenGas_Chlorine", 3000)},
                new FluidStack[] {field("Water", 4500), field("GenLiquid_LithiumChlorate", 2500)});
        // HeatMixer.java:28
        add("HeatMixer.java:28", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(48L).euPerTick(16L),
                new ItemStack[] {form("dust", "Sulfur", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 2000)},
                new FluidStack[] {field("GenGas_HydrosulfuricAcid", 3000)});
        // HeatMixer.java:29
        add("HeatMixer.java:29", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(48L).euPerTick(16L),
                new ItemStack[] {form("dustTiny", "Blaze", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 2000)},
                new FluidStack[] {field("GenGas_HydrosulfuricAcid", 3000)});
        // HeatMixer.java:30
        add("HeatMixer.java:30", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(48L).euPerTick(16L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "Sulfur", 1)},
                new FluidStack[] {field("GenGas_HydrosulfuricAcid", 2000), field("GenGas_SulfurDioxide", 1000)},
                new FluidStack[] {field("Water", 2000)});
        // OilProcessing.java:54
        add("OilProcessing.java:54", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(2400L).euPerTick(32L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_SulfuredOilDesulfurizationer", 1000), field("Hydrogen", 200)},
                new FluidStack[] {field("GenLiquid_OilDesulfurizationer", 1000), field("GenGas_HydrosulfuricAcid", 100)});
        // OilProcessing.java:111
        add("OilProcessing.java:111", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(15L).euPerTick(217L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Propionaldehyde", 100), field("Hydrogen", 100)},
                new FluidStack[] {field("GenLiquid_Propanol", 100)});
        // OilProcessing.java:161
        add("OilProcessing.java:161", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(160L).euPerTick(280L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Oxygen", 500), field("Propane", 1000)},
                new FluidStack[] {field("GenLiquid_Acetone", 500)});
        // OreProcessing.java:40
        add("OreProcessing.java:40", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(160L).euPerTick(320L),
                new ItemStack[] {form("dust", "AmmoniumDichromate", 1)},
                new ItemStack[] {form("dust", "ChromiumDioxide", 2)},
                new FluidStack[] {field("Oxygen", 4000)},
                new FluidStack[] {field("Nitrogen", 250)});
        // OreProcessing.java:68
        add("OreProcessing.java:68", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(160L).euPerTick(320L),
                new ItemStack[] {form("dust", "TungstenTrioxide", 4)},
                new ItemStack[] {form("dust", "Tungsten", 1)},
                new FluidStack[] {field("Hydrogen", 6000)},
                new FluidStack[] {field("Water", 9000)});
        // OreProcessing.java:100
        add("OreProcessing.java:100", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(20L).euPerTick(1428L),
                new ItemStack[] {form("dust", "UraniumNitrateHexahydrate", 1)},
                new ItemStack[] {form("dust", "UraniumTetrafluoride", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 4000)},
                NO_FLUIDS);
        // OreProcessing.java:102
        add("OreProcessing.java:102", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(300L).euPerTick(386L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Propane", 1000), field("GenGas_Acetaldehyde", 3000)},
                new FluidStack[] {field("GenLiquid_Butanol", 1000), field("GenLiquid_Ethane", 1000)});
        // OreProcessing.java:103
        add("OreProcessing.java:103", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(80L).euPerTick(468L),
                new ItemStack[] {form("dust", "Phosphate", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Butanol", 3000)},
                new FluidStack[] {field("GenLiquid_TributylPhosphate", 1000), field("Water", 1000)});
        // OreProcessing.java:106
        add("OreProcessing.java:106", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(600L).euPerTick(120L),
                new ItemStack[] {form("ingot", "Butter", 2)},
                new ItemStack[] {form("dust", "OleicAcid", 3)},
                new FluidStack[] {field("GenGas_HydrochloricAcid", 1)},
                new FluidStack[] {field("GenLiquid_Glycerol", 1000)},
                0L,
                null,
                null,
                new int[] {0});
        // OreProcessing.java:107
        add("OreProcessing.java:107", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(600L).euPerTick(120L),
                new ItemStack[] {form("ingot", "Butter", 2)},
                new ItemStack[] {form("dust", "OleicAcid", 3)},
                new FluidStack[] {field("GenLiquid_SulfuricAcid", 1)},
                new FluidStack[] {field("GenLiquid_Glycerol", 1000)},
                0L,
                null,
                null,
                new int[] {0});
        // PlatinumGroupProcess.java:32
        add("PlatinumGroupProcess.java:32", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(212L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Styrene", 1000), field("GenLiquid_Dichloromethane", 1000)},
                new FluidStack[] {field("GenLiquid_VinylbenzylChloride", 500)});
        // PlatinumGroupProcess.java:33
        add("PlatinumGroupProcess.java:33", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(320L).euPerTick(212L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "DimethylaminomethylStyrene", 4)},
                new FluidStack[] {field("GenLiquid_VinylbenzylChloride", 1000), field("GenLiquid_Dimethylamine", 1000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:34
        add("PlatinumGroupProcess.java:34", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(120L).euPerTick(82L),
                new ItemStack[] {form("dust", "DimethylaminomethylStyrene", 1)},
                new ItemStack[] {own("alkaline_ion_exchange_membrane", 1)},
                new FluidStack[] {field("Oxygen", 1000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:42
        add("PlatinumGroupProcess.java:42", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(90L).euPerTick(180L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_EthylChloroacetate", 1000), field("GenLiquid_Dimethylamine", 1000)},
                new FluidStack[] {field("GenLiquid_Dimethylglycine", 1000)});
        // PlatinumGroupProcess.java:47
        add("PlatinumGroupProcess.java:47", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(90L).euPerTick(180L),
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "Gold", 2)},
                new FluidStack[] {field("GenLiquid_RoughPlatinumGroupSolution", 1000), field("GenGas_SulfurDioxide", 1000)},
                new FluidStack[] {field("GenLiquid_SulphurPlatinumGroupSolution", 1500)});
        // PlatinumGroupProcess.java:57
        add("PlatinumGroupProcess.java:57", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(16L).euPerTick(180L),
                new ItemStack[] {form("dust", "SodiumHydroxide", 3)},
                new ItemStack[] {form("dust", "Rhodium", 3)},
                new FluidStack[] {field("GenLiquid_NitricPlatinumGroupSolutionUp", 1000)},
                new FluidStack[] {field("GenLiquid_DeprecatedNitricPlatinumGroupSolutionUp", 1000)});
        // PlatinumGroupProcess.java:58
        add("PlatinumGroupProcess.java:58", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(16L).euPerTick(180L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "Salt", 1)},
                new FluidStack[] {field("GenLiquid_DeprecatedNitricPlatinumGroupSolutionUp", 2000), field("GenGas_HydrochloricAcid", 1000)},
                new FluidStack[] {field("GenLiquid_Trimethylamine", 500)});
        // PlatinumGroupProcess.java:61
        add("PlatinumGroupProcess.java:61", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(16L).euPerTick(180L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_ExtractedPlatinumGroupSolutionUp", 1500), field("GenGas_HydrochloricAcid", 12000)},
                new FluidStack[] {field("GenLiquid_Dimethylglycine", 1000), field("GenLiquid_AcidPlatinumGroupSolutionUp", 500)});
        // PlatinumGroupProcess.java:62
        add("PlatinumGroupProcess.java:62", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(16L).euPerTick(180L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "Platinum", 1)},
                new FluidStack[] {field("GenLiquid_DinhexylSulfide", 1000), field("GenLiquid_AcidPlatinumGroupSolutionUp", 500)},
                new FluidStack[] {field("GenLiquid_DinhexylSulfidePlatinumGroupSolution", 1000), field("GenLiquid_ChloroplatinicAcid", 9000)});
        // PlatinumGroupProcess.java:63
        add("PlatinumGroupProcess.java:63", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(16L).euPerTick(180L),
                NO_ITEMS,
                new ItemStack[] {form("dust", "AmmoniumHexachloropalladate", 4)},
                new FluidStack[] {field("GenLiquid_DinhexylSulfidePlatinumGroupSolution", 1000), field("GenGas_Ammonia", 4000)},
                new FluidStack[] {field("GenLiquid_DinhexylSulfide", 1000)});
        // PlatinumGroupProcess.java:65
        add("PlatinumGroupProcess.java:65", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(2000L).euPerTick(780L),
                new ItemStack[] {form("dust", "Rhodium", 2)},
                new ItemStack[] {form("dust", "Rhodiumoxide", 9)},
                new FluidStack[] {field("Oxygen", 3000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:66
        add("PlatinumGroupProcess.java:66", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(200L),
                new ItemStack[] {form("dust", "Palladium", 1)},
                new ItemStack[] {form("dust", "Palladiumoxide", 3)},
                new FluidStack[] {field("Oxygen", 1000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:67
        add("PlatinumGroupProcess.java:67", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(800L).euPerTick(420L),
                new ItemStack[] {form("dust", "Iridium", 1)},
                new ItemStack[] {form("dust", "Iridiumoxide", 5)},
                new FluidStack[] {field("Oxygen", 2000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:68
        add("PlatinumGroupProcess.java:68", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(200L).euPerTick(820L),
                new ItemStack[] {form("dust", "Rhodiumoxide", 3)},
                new ItemStack[] {form("dust", "Rhodium", 1)},
                new FluidStack[] {field("Hydrogen", 4000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:69
        add("PlatinumGroupProcess.java:69", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(200L).euPerTick(700L),
                new ItemStack[] {form("dust", "Palladiumoxide", 3)},
                new ItemStack[] {form("dust", "Palladium", 2)},
                new FluidStack[] {field("Hydrogen", 4000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:70
        add("PlatinumGroupProcess.java:70", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(200L).euPerTick(730L),
                new ItemStack[] {form("dust", "Iridiumoxide", 5)},
                new ItemStack[] {form("dust", "Iridium", 2)},
                new FluidStack[] {field("Hydrogen", 8000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:71
        add("PlatinumGroupProcess.java:71", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(200L).euPerTick(730L),
                new ItemStack[] {form("dust", "OsmiumTetraoxide", 1)},
                new ItemStack[] {form("dustSmall", "OsmiumElemental", 6)},
                new FluidStack[] {field("Hydrogen", 3000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:73
        add("PlatinumGroupProcess.java:73", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(600L).euPerTick(630L),
                new ItemStack[] {form("dust", "AmmoniumHexachlororhodate", 2)},
                new ItemStack[] {form("dust", "Rhodiumoxide", 1), form("dust", "AmmoniumChloride", 1)},
                new FluidStack[] {field("Oxygen", 4000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:74
        add("PlatinumGroupProcess.java:74", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(600L).euPerTick(430L),
                new ItemStack[] {form("dust", "AmmoniumHexachloroiridate", 7)},
                new ItemStack[] {form("dust", "Palladiumoxide", 3), form("dust", "AmmoniumChloride", 4)},
                new FluidStack[] {field("Oxygen", 12000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:75
        add("PlatinumGroupProcess.java:75", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(600L).euPerTick(510L),
                new ItemStack[] {form("dust", "AmmoniumHexachloropalladate", 4)},
                new ItemStack[] {form("dust", "Iridiumoxide", 5), form("dust", "AmmoniumChloride", 5)},
                new FluidStack[] {field("Oxygen", 12000)},
                NO_FLUIDS);
        // PurityRefining.java:73
        add("PurityRefining.java:73", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(1024L),
                new ItemStack[] {form("dust", "Tungsten", 4)},
                new ItemStack[] {form("dustCommercialPure", "Tungsten", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:73
        add("PurityRefining.java:73", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(1024L),
                new ItemStack[] {form("dust", "Molybdenum", 4)},
                new ItemStack[] {form("dustCommercialPure", "Molybdenum", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(1024L),
                new ItemStack[] {form("dustCommercialPure", "Aluminium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Aluminium", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(1024L),
                new ItemStack[] {form("dustCommercialPure", "Titanium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Titanium", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(1024L),
                new ItemStack[] {form("dustCommercialPure", "Zirconium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Zirconium", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(1024L),
                new ItemStack[] {form("dustCommercialPure", "Niobium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Niobium", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(1024L),
                new ItemStack[] {form("dustCommercialPure", "Tantalum", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Tantalum", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1200L).euPerTick(1024L),
                new ItemStack[] {form("dustCommercialPure", "Chromium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Chromium", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "Gold", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Gold", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "Platinum", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Platinum", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "Palladium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Palladium", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "Rhodium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Rhodium", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "Iridium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Iridium", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "OsmiumElemental", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "OsmiumElemental", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "intellite", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "intellite", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(2400L).euPerTick(4096L),
                new ItemStack[] {form("dustCommercialPure", "Tungsten", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Tungsten", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(2400L).euPerTick(4096L),
                new ItemStack[] {form("dustCommercialPure", "Molybdenum", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Molybdenum", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "Carbon", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Carbon", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "Silicon", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Silicon", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "Germanium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Germanium", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "Gallium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Gallium", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(1600L).euPerTick(2048L),
                new ItemStack[] {form("dustCommercialPure", "Indium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Indium", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // Chemistry.java:144
        add("Chemistry.java:144", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(200L).euPerTick(200L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Diketene", 1000)},
                new FluidStack[] {field("GenLiquid_Ketene", 600), field("GenLiquid_Diketene", 400)});
        // Plastic.java:57
        add("Plastic.java:57", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(100L).euPerTick(240L),
                new ItemStack[] {form("dust", "CalciumAcetate", 1)},
                new ItemStack[] {form("dust", "Calcite", 1)},
                NO_FLUIDS,
                new FluidStack[] {field("GenLiquid_Acetone", 500)});
        // Plastic.java:115
        add("Plastic.java:115", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(320L).euPerTick(144L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_DichloroEthane", 100)},
                new FluidStack[] {field("GenLiquid_VinylChloride", 100), field("GenGas_HydrochloricAcid", 100)});
        // ComputerBuilding.java:128
        add("ComputerBuilding.java:128", RecipeMaps.HEAT_MIXER, RecipeTiming.ticks(10L).euPerTick(290L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_HexafluorosilicicAcid", 3000)},
                new FluidStack[] {field("GenGas_SiliconTetrafluoride", 1000), field("GenGas_HydrogenFluoride", 2000)});
    }

    private static void eDA() {
        // ComputerBuilding.java:115
        add("ComputerBuilding.java:115", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(28800L).euPerTick(16L),
                new ItemStack[] {own("computer_gt3699_v3", 1), own("tech_research_data43", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), own("tech_research_data40", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t8", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3, 4},
                null);
        // ComputerBuilding.java:88
        add("ComputerBuilding.java:88", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(3600L).euPerTick(16L),
                new ItemStack[] {gt("circuit_good", 1), own("tech_research_data19", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t1", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3},
                null);
        // ComputerBuilding.java:93
        add("ComputerBuilding.java:93", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(7200L).euPerTick(16L),
                new ItemStack[] {gt("circuit_advanced", 1), own("tech_research_data25", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t2", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3},
                null);
        // ComputerBuilding.java:98
        add("ComputerBuilding.java:98", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(14400L).euPerTick(16L),
                new ItemStack[] {gt("circuit_elite", 1), own("tech_research_data29", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t3", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3},
                null);
        // ComputerBuilding.java:102
        add("ComputerBuilding.java:102", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(28800L).euPerTick(16L),
                new ItemStack[] {gt("circuit_master", 1), own("tech_research_data33", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t4", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3},
                null);
        // ComputerBuilding.java:106
        add("ComputerBuilding.java:106", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(28800L).euPerTick(16L),
                new ItemStack[] {own("computer_gt2090", 1), own("tech_research_data33", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t5", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3},
                null);
        // ComputerBuilding.java:109
        add("ComputerBuilding.java:109", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(28800L).euPerTick(16L),
                new ItemStack[] {own("computer_gt3699", 1), own("tech_research_data36", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t6", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3},
                null);
        // ComputerBuilding.java:112
        add("ComputerBuilding.java:112", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(28800L).euPerTick(16L),
                new ItemStack[] {own("computer_gt3699_v2", 1), own("tech_research_data40", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t7", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3},
                null);
        // ComputerBuilding.java:89
        add("ComputerBuilding.java:89", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(1800L).euPerTick(16L),
                new ItemStack[] {gt("circuit_good", 1), own("tech_research_data19", 1), gt("circuit_part_good", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("circuit_part_photomask_t3", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:90
        add("ComputerBuilding.java:90", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(2700L).euPerTick(16L),
                new ItemStack[] {gt("circuit_good", 1), own("tech_research_data19", 1), gt("logistics_mass_storage_electrum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("ram_photomask_t1", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:91
        add("ComputerBuilding.java:91", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(1800L).euPerTick(16L),
                new ItemStack[] {gt("circuit_good", 1), own("tech_research_data19", 1), form("dust", "Silicon", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("diode_photomask_t1", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:94
        add("ComputerBuilding.java:94", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(3600L).euPerTick(16L),
                new ItemStack[] {gt("circuit_advanced", 1), own("tech_research_data25", 1), gt("circuit_part_good", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("circuit_part_photomask_t4", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:95
        add("ComputerBuilding.java:95", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(5400L).euPerTick(16L),
                new ItemStack[] {gt("circuit_advanced", 1), own("tech_research_data25", 1), gt("logistics_mass_storage_electrum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("ram_photomask_t2", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:96
        add("ComputerBuilding.java:96", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(3600L).euPerTick(16L),
                new ItemStack[] {gt("circuit_advanced", 1), own("tech_research_data25", 1), own("silicon_boule_pure", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("diode_photomask_t2", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:99
        add("ComputerBuilding.java:99", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(7200L).euPerTick(16L),
                new ItemStack[] {gt("circuit_elite", 1), own("tech_research_data29", 1), gt("circuit_part_good", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("circuit_part_photomask_t5", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:100
        add("ComputerBuilding.java:100", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(10800L).euPerTick(16L),
                new ItemStack[] {gt("circuit_elite", 1), own("tech_research_data29", 1), gt("logistics_mass_storage_electrum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("ram_photomask_t3", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:103
        add("ComputerBuilding.java:103", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(14400L).euPerTick(16L),
                new ItemStack[] {gt("circuit_master", 1), own("tech_research_data33", 1), gt("circuit_part_good", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("circuit_part_photomask_t6", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:104
        add("ComputerBuilding.java:104", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(21600L).euPerTick(16L),
                new ItemStack[] {gt("circuit_master", 1), own("tech_research_data33", 1), gt("logistics_mass_storage_electrum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("ram_photomask_t4", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:107
        add("ComputerBuilding.java:107", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(21600L).euPerTick(16L),
                new ItemStack[] {own("computer_gt2090", 1), own("tech_research_data33", 1), gt("logistics_mass_storage_electrum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("ram_photomask_t5", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:110
        add("ComputerBuilding.java:110", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(21600L).euPerTick(16L),
                new ItemStack[] {own("computer_gt3699", 1), own("tech_research_data36", 1), gt("logistics_mass_storage_electrum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("ram_photomask_t6", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:113
        add("ComputerBuilding.java:113", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(21600L).euPerTick(16L),
                new ItemStack[] {own("computer_gt3699_v2", 1), own("tech_research_data40", 1), gt("logistics_mass_storage_electrum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("ram_photomask_t7", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:116
        add("ComputerBuilding.java:116", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(21600L).euPerTick(16L),
                new ItemStack[] {own("computer_gt3699_v3", 1), own("tech_research_data43", 1), gt("logistics_mass_storage_electrum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("ram_photomask_t8", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:118
        add("ComputerBuilding.java:118", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(28800L).euPerTick(16L),
                new ItemStack[] {own("computer_gt3699_v3", 1), own("tech_research_data40", 1), form("wireFine", "Platinum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("inter_layer_photomask_t1", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
        // ComputerBuilding.java:119
        add("ComputerBuilding.java:119", RecipeMaps.ELECTRONICS_DESIGNER, RecipeTiming.ticks(21600L).euPerTick(16L),
                new ItemStack[] {own("computer_gt3699_v4", 1), own("tech_research_data43", 1), form("wireFine", "Platinum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("inter_layer_photomask_t2", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2},
                null);
    }

    private static void flawDetector() {
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "Magnalium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Magnalium", 1), form("turbineLargeSteamDamaged", "Magnalium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "Trinitanium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Trinitanium", 1), form("turbineLargeSteamDamaged", "Trinitanium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "Graphene", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Graphene", 1), form("turbineLargeSteamDamaged", "Graphene", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "Vibramantium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Vibramantium", 1), form("turbineLargeSteamDamaged", "Vibramantium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "Trinaquadalloy", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Trinaquadalloy", 1), form("turbineLargeSteamDamaged", "Trinaquadalloy", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "Steel", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Steel", 1), form("turbineLargeSteamDamaged", "Steel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "StainlessSteel", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "StainlessSteel", 1), form("turbineLargeSteamDamaged", "StainlessSteel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "Titanium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Titanium", 1), form("turbineLargeSteamDamaged", "Titanium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "Tungstensteel", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Tungstensteel", 1), form("turbineLargeSteamDamaged", "Tungstensteel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "Adamantium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Adamantium", 1), form("turbineLargeSteamDamaged", "Adamantium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "Iridium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Iridium", 1), form("turbineLargeSteamDamaged", "Iridium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(12000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeSteam", "OsmiumElemental", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "OsmiumElemental", 1), form("turbineLargeSteamDamaged", "OsmiumElemental", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "Magnalium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Magnalium", 1), form("turbineLargeGasDamaged", "Magnalium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "Trinitanium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Trinitanium", 1), form("turbineLargeGasDamaged", "Trinitanium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "Graphene", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Graphene", 1), form("turbineLargeGasDamaged", "Graphene", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "Vibramantium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Vibramantium", 1), form("turbineLargeGasDamaged", "Vibramantium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "Trinaquadalloy", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Trinaquadalloy", 1), form("turbineLargeGasDamaged", "Trinaquadalloy", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "Steel", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Steel", 1), form("turbineLargeGasDamaged", "Steel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "StainlessSteel", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "StainlessSteel", 1), form("turbineLargeGasDamaged", "StainlessSteel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "Titanium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Titanium", 1), form("turbineLargeGasDamaged", "Titanium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "Tungstensteel", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Tungstensteel", 1), form("turbineLargeGasDamaged", "Tungstensteel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "Adamantium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Adamantium", 1), form("turbineLargeGasDamaged", "Adamantium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "Iridium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Iridium", 1), form("turbineLargeGasDamaged", "Iridium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", RecipeMaps.FLAW_DETECTOR, RecipeTiming.ticks(18000L).euPerTick(16L),
                new ItemStack[] {form("turbineLargeGas", "OsmiumElemental", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "OsmiumElemental", 1), form("turbineLargeGasDamaged", "OsmiumElemental", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null,
                null);
    }

    private static void waferTester() {
        // ComputerBuilding.java:337
        add("ComputerBuilding.java:337", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(60L),
                new ItemStack[] {own("cpu_wafer_t1_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t1_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:338
        add("ComputerBuilding.java:338", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(150L),
                new ItemStack[] {own("cpu_wafer_t2_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t2_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:339
        add("ComputerBuilding.java:339", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(250L),
                new ItemStack[] {own("cpu_wafer_t3_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t3_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:340
        add("ComputerBuilding.java:340", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(800L).euPerTick(800L),
                new ItemStack[] {own("cpu_wafer_t4_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t4_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:341
        add("ComputerBuilding.java:341", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(9500L).euPerTick(3600L),
                new ItemStack[] {own("cpu_wafer_t5_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t5_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:342
        add("ComputerBuilding.java:342", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(13200L).euPerTick(3600L),
                new ItemStack[] {own("cpu_wafer_t6_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t6_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:343
        add("ComputerBuilding.java:343", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(16700L).euPerTick(3600L),
                new ItemStack[] {own("cpu_wafer_t7_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t7_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:344
        add("ComputerBuilding.java:344", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(18800L).euPerTick(3600L),
                new ItemStack[] {own("cpu_wafer_t8_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t8_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:346
        add("ComputerBuilding.java:346", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(52L),
                new ItemStack[] {own("ram_wafer_t1_doped", 1)},
                new ItemStack[] {own("ram_wafer_t1_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:347
        add("ComputerBuilding.java:347", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(140L),
                new ItemStack[] {own("ram_wafer_t2_doped", 1)},
                new ItemStack[] {own("ram_wafer_t2_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:348
        add("ComputerBuilding.java:348", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(210L),
                new ItemStack[] {own("ram_wafer_t3_doped", 1)},
                new ItemStack[] {own("ram_wafer_t3_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:349
        add("ComputerBuilding.java:349", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(500L).euPerTick(670L),
                new ItemStack[] {own("ram_wafer_t4_doped", 1)},
                new ItemStack[] {own("ram_wafer_t4_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:350
        add("ComputerBuilding.java:350", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(1500L).euPerTick(1500L),
                new ItemStack[] {own("ram_wafer_t5_doped", 1)},
                new ItemStack[] {own("ram_wafer_t5_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:351
        add("ComputerBuilding.java:351", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(2000L).euPerTick(1500L),
                new ItemStack[] {own("ram_wafer_t6_doped", 1)},
                new ItemStack[] {own("ram_wafer_t6_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:352
        add("ComputerBuilding.java:352", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(2500L).euPerTick(1500L),
                new ItemStack[] {own("ram_wafer_t7_doped", 1)},
                new ItemStack[] {own("ram_wafer_t7_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:353
        add("ComputerBuilding.java:353", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(3000L).euPerTick(1500L),
                new ItemStack[] {own("ram_wafer_t8_doped", 1)},
                new ItemStack[] {own("ram_wafer_t8_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:355
        add("ComputerBuilding.java:355", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(46L),
                new ItemStack[] {own("circuit_part_wafer_t3_doped", 1)},
                new ItemStack[] {own("circuit_part_wafer_t3_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:356
        add("ComputerBuilding.java:356", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(120L),
                new ItemStack[] {own("circuit_part_wafer_t4_doped", 1)},
                new ItemStack[] {own("circuit_part_wafer_t4_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:357
        add("ComputerBuilding.java:357", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(200L),
                new ItemStack[] {own("circuit_part_wafer_t5_doped", 1)},
                new ItemStack[] {own("circuit_part_wafer_t5_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:358
        add("ComputerBuilding.java:358", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(1000L).euPerTick(660L),
                new ItemStack[] {own("circuit_part_wafer_t6_doped", 1)},
                new ItemStack[] {own("circuit_part_wafer_t6_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:360
        add("ComputerBuilding.java:360", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(4000L).euPerTick(900L),
                new ItemStack[] {own("inter_layer_wafer_t7_doped", 1)},
                new ItemStack[] {own("inter_layer_wafer_t7_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:361
        add("ComputerBuilding.java:361", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(5000L).euPerTick(900L),
                new ItemStack[] {own("inter_layer_wafer_t8_doped", 1)},
                new ItemStack[] {own("inter_layer_wafer_t8_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:363
        add("ComputerBuilding.java:363", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(120L),
                new ItemStack[] {own("diode_wafer_t1_doped", 1)},
                new ItemStack[] {own("diode_wafer_t1_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:364
        add("ComputerBuilding.java:364", RecipeMaps.WAFER_TESTER, RecipeTiming.ticks(400L).euPerTick(200L),
                new ItemStack[] {own("diode_wafer_t2_doped", 1)},
                new ItemStack[] {own("diode_wafer_t2_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void waferCoater() {
        // ComputerBuilding.java:198
        add("ComputerBuilding.java:198", RecipeMaps.WAFER_COATER, RecipeTiming.ticks(200L).euPerTick(30L),
                new ItemStack[] {own("silicon_plate_t1", 1)},
                new ItemStack[] {own("silicon_plate_coated_t1", 1)},
                new FluidStack[] {field("GenLiquid_DNQPhotoresist", 100)},
                NO_FLUIDS);
        // ComputerBuilding.java:199
        add("ComputerBuilding.java:199", RecipeMaps.WAFER_COATER, RecipeTiming.ticks(200L).euPerTick(260L),
                new ItemStack[] {own("silicon_plate_oxidized_t2", 1)},
                new ItemStack[] {own("silicon_plate_coated_t2", 1)},
                new FluidStack[] {field("GenLiquid_DNQPhotoresist", 100)},
                NO_FLUIDS);
        // ComputerBuilding.java:201
        add("ComputerBuilding.java:201", RecipeMaps.WAFER_COATER, RecipeTiming.ticks(200L).euPerTick(2050L),
                new ItemStack[] {own("mo_o2_plate_oxidized_t1", 1)},
                new ItemStack[] {own("mo_o2_plate_coated_t1", 1)},
                new FluidStack[] {field("GenLiquid_PMMAPhotoresist", 100)},
                NO_FLUIDS);
        // ComputerBuilding.java:202
        add("ComputerBuilding.java:202", RecipeMaps.WAFER_COATER, RecipeTiming.ticks(200L).euPerTick(8200L),
                new ItemStack[] {own("mo_o2_plate_oxidized_t2", 1)},
                new ItemStack[] {own("mo_o2_plate_coated_t2", 1)},
                new FluidStack[] {field("GenLiquid_PMMAPhotoresist", 100)},
                NO_FLUIDS);
    }

    private static void ultrasonicMixer() {
        // Chemistry.java:127
        add("Chemistry.java:127", RecipeMaps.ULTRASONIC_MIXER, RecipeTiming.ticks(3600L).euPerTick(800L),
                new ItemStack[] {circuit(0, 1)},
                new ItemStack[] {form("dust", "Graphite", 6), form("foil", "Graphene", 4)},
                new FluidStack[] {field("GenLiquid_Graphite_Methylpyrrolidone", 4000)},
                new FluidStack[] {field("GenLiquid_Methylpyrrolidone", 4000)});
        // Chemistry.java:128
        add("Chemistry.java:128", RecipeMaps.ULTRASONIC_MIXER, RecipeTiming.ticks(18000L).euPerTick(600L),
                new ItemStack[] {circuit(1, 1)},
                new ItemStack[] {form("dust", "Graphite", 24), form("plate", "Graphene", 4)},
                new FluidStack[] {field("GenLiquid_Graphite_Methylpyrrolidone", 16000)},
                new FluidStack[] {field("GenLiquid_Methylpyrrolidone", 16000)});
        // Chemistry.java:139
        add("Chemistry.java:139", RecipeMaps.ULTRASONIC_MIXER, RecipeTiming.ticks(80L).euPerTick(800L),
                new ItemStack[] {form("dust", "Barite", 4)},
                new ItemStack[] {form("dust", "Barite", 3)},
                new FluidStack[] {field("DistW", 1000)},
                new FluidStack[] {field("GenLiquid_BariumSulfate", 1000)});
    }

    private static void tinyDistillTower() {
        // OilProcessing.java:56
        add("OilProcessing.java:56", RecipeMaps.TINY_DISTILL, RecipeTiming.ticks(100L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilLight", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 400), field("GenLiquid_Naphtha", 300), field("GenGas_OilGas", 200)});
        // OilProcessing.java:57
        add("OilProcessing.java:57", RecipeMaps.TINY_DISTILL, RecipeTiming.ticks(100L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilMedium", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 425), field("GenLiquid_Naphtha", 325), field("GenGas_OilGas", 175)});
        // OilProcessing.java:58
        add("OilProcessing.java:58", RecipeMaps.TINY_DISTILL, RecipeTiming.ticks(100L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilNormal", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 450), field("GenLiquid_Naphtha", 350), field("GenGas_OilGas", 150)});
        // OilProcessing.java:59
        add("OilProcessing.java:59", RecipeMaps.TINY_DISTILL, RecipeTiming.ticks(100L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 475), field("GenLiquid_Naphtha", 375), field("GenGas_OilGas", 125)});
        // OilProcessing.java:60
        add("OilProcessing.java:60", RecipeMaps.TINY_DISTILL, RecipeTiming.ticks(100L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilExtraHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 500), field("GenLiquid_Naphtha", 400), field("GenGas_OilGas", 100)});
    }

    private static void smallDistillTower() {
        // OilProcessing.java:62
        add("OilProcessing.java:62", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(80L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilLight", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 400), field("GenLiquid_Naphtha", 300), field("GenGas_OilGas", 200)});
        // OilProcessing.java:63
        add("OilProcessing.java:63", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(80L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilMedium", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 425), field("GenLiquid_Naphtha", 325), field("GenGas_OilGas", 175)});
        // OilProcessing.java:64
        add("OilProcessing.java:64", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(80L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilNormal", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 450), field("GenLiquid_Naphtha", 350), field("GenGas_OilGas", 150)});
        // OilProcessing.java:65
        add("OilProcessing.java:65", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(80L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 475), field("GenLiquid_Naphtha", 375), field("GenGas_OilGas", 125)});
        // OilProcessing.java:66
        add("OilProcessing.java:66", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(80L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilExtraHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 500), field("GenLiquid_Naphtha", 400), field("GenGas_OilGas", 100)});
        // OilProcessing.java:74
        add("OilProcessing.java:74", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(120L).euPerTick(196L),
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "OilScarp", 1)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 400)},
                new FluidStack[] {field("Diesel", 120), field("Kerosine", 200), field("GenLiquid_Naphtha", 80), field("Petrol", 320), field("GenGas_OilGas", 100)});
        // OilProcessing.java:132
        add("OilProcessing.java:132", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(60L).euPerTick(256L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_OilGas", 1000)},
                new FluidStack[] {field("Methane", 372), field("GenLiquid_Ethane", 262), field("Propane", 188), field("Butane", 138), field("CarbonDioxide", 40)});
        // OilProcessing.java:170
        add("OilProcessing.java:170", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(20L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaLow", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 121), field("GenLiquid_Toluene", 30), field("Petrol", 600), field("Methane", 212), field("GenLiquid_Ethane", 182), field("Hydrogen", 882)});
        // OilProcessing.java:171
        add("OilProcessing.java:171", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(20L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaMedium", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 151), field("GenLiquid_Toluene", 43), field("Petrol", 500), field("Methane", 342), field("GenLiquid_Ethane", 282), field("Hydrogen", 1282)});
        // OilProcessing.java:172
        add("OilProcessing.java:172", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(20L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaHigh", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 191), field("GenLiquid_Toluene", 50), field("Petrol", 300), field("Methane", 602), field("GenLiquid_Ethane", 352), field("Hydrogen", 1682)});
        // OilProcessing.java:180
        add("OilProcessing.java:180", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(20L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolLow", 1000)},
                new FluidStack[] {field("Lubricant", 218), field("Petrol", 542), field("GenGas_OilGas", 118), field("GenGas_CrackedOilGas", 242)});
        // OilProcessing.java:181
        add("OilProcessing.java:181", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(20L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolMedium", 1000)},
                new FluidStack[] {field("Lubricant", 162), field("Petrol", 423), field("GenGas_OilGas", 247), field("GenGas_CrackedOilGas", 392)});
        // OilProcessing.java:182
        add("OilProcessing.java:182", RecipeMaps.SMALL_DISTILL, RecipeTiming.ticks(20L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolHigh", 1000)},
                new FluidStack[] {field("Lubricant", 92), field("Petrol", 321), field("GenGas_OilGas", 301), field("GenGas_CrackedOilGas", 542)});
    }

    private static void distillTower() {
        // OilProcessing.java:68
        add("OilProcessing.java:68", RecipeMaps.DISTILL, RecipeTiming.ticks(65L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilLight", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 400), field("GenLiquid_Naphtha", 300), field("GenGas_OilGas", 200)});
        // OilProcessing.java:69
        add("OilProcessing.java:69", RecipeMaps.DISTILL, RecipeTiming.ticks(65L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilMedium", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 425), field("GenLiquid_Naphtha", 325), field("GenGas_OilGas", 175)});
        // OilProcessing.java:70
        add("OilProcessing.java:70", RecipeMaps.DISTILL, RecipeTiming.ticks(65L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilNormal", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 450), field("GenLiquid_Naphtha", 350), field("GenGas_OilGas", 150)});
        // OilProcessing.java:71
        add("OilProcessing.java:71", RecipeMaps.DISTILL, RecipeTiming.ticks(65L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 475), field("GenLiquid_Naphtha", 375), field("GenGas_OilGas", 125)});
        // OilProcessing.java:72
        add("OilProcessing.java:72", RecipeMaps.DISTILL, RecipeTiming.ticks(65L).euPerTick(128L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilExtraHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 500), field("GenLiquid_Naphtha", 400), field("GenGas_OilGas", 100)});
        // OilProcessing.java:75
        add("OilProcessing.java:75", RecipeMaps.DISTILL, RecipeTiming.ticks(95L).euPerTick(196L),
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "OilScarp", 1)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 400)},
                new FluidStack[] {field("Diesel", 120), field("Kerosine", 200), field("GenLiquid_Naphtha", 80), field("Petrol", 320), field("GenGas_OilGas", 100)});
        // OilProcessing.java:80
        add("OilProcessing.java:80", RecipeMaps.DISTILL, RecipeTiming.ticks(16L).euPerTick(64L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Biomass", 80)},
                new FluidStack[] {field("Reikanol", 20), field("GenLiquid_Glycerol", 20), field("Methane", 4), field("DistW", 50)});
        // OilProcessing.java:83
        add("OilProcessing.java:83", RecipeMaps.DISTILL, RecipeTiming.ticks(16L).euPerTick(64L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("BiomassIC2", 80)},
                new FluidStack[] {field("Reikanol", 20), field("GenLiquid_Glycerol", 20), field("Methane", 4), field("DistW", 50)});
        // OilProcessing.java:84
        add("OilProcessing.java:84", RecipeMaps.DISTILL, RecipeTiming.ticks(64L).euPerTick(64L),
                NO_ITEMS,
                new ItemStack[] {form("dustTiny", "WaxParaffin", 1), form("dustTiny", "Asphalt", 1), form("dustTiny", "PetroleumCoke", 1)},
                new FluidStack[] {field("Oil_Soulsand", 25)},
                new FluidStack[] {field("Diesel", 5), field("Kerosine", 5), field("Petrol", 5), field("Propane", 5), field("Butane", 5), field("Lubricant", 40)},
                0L,
                new int[] {1000, 1000, 1000},
                null,
                null);
        // OilProcessing.java:133
        add("OilProcessing.java:133", RecipeMaps.DISTILL, RecipeTiming.ticks(40L).euPerTick(256L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_OilGas", 1000)},
                new FluidStack[] {field("Methane", 372), field("GenLiquid_Ethane", 262), field("Propane", 188), field("Butane", 138), field("CarbonDioxide", 40)});
        // OilProcessing.java:135
        add("OilProcessing.java:135", RecipeMaps.DISTILL, RecipeTiming.ticks(40L).euPerTick(256L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_CrackedOilGas", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 42), field("GenLiquid_Toluene", 8), field("GenLiquid_Phenol", 11), field("Methane", 183), field("GenLiquid_Ethane", 152), field("Propane", 133), field("Butane", 128), field("GenGas_Butadiene", 134), field("CarbonDioxide", 134)});
        // OilProcessing.java:173
        add("OilProcessing.java:173", RecipeMaps.DISTILL, RecipeTiming.ticks(10L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaLow", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 121), field("GenLiquid_Toluene", 30), field("Petrol", 600), field("Methane", 212), field("GenLiquid_Ethane", 182), field("Hydrogen", 882)});
        // OilProcessing.java:174
        add("OilProcessing.java:174", RecipeMaps.DISTILL, RecipeTiming.ticks(10L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaMedium", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 151), field("GenLiquid_Toluene", 43), field("Petrol", 500), field("Methane", 342), field("GenLiquid_Ethane", 282), field("Hydrogen", 1282)});
        // OilProcessing.java:175
        add("OilProcessing.java:175", RecipeMaps.DISTILL, RecipeTiming.ticks(10L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaHigh", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 191), field("GenLiquid_Toluene", 50), field("Petrol", 300), field("Methane", 602), field("GenLiquid_Ethane", 352), field("Hydrogen", 1682)});
        // OilProcessing.java:183
        add("OilProcessing.java:183", RecipeMaps.DISTILL, RecipeTiming.ticks(10L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolLow", 1000)},
                new FluidStack[] {field("Lubricant", 218), field("Petrol", 542), field("GenGas_OilGas", 118), field("GenGas_CrackedOilGas", 242)});
        // OilProcessing.java:184
        add("OilProcessing.java:184", RecipeMaps.DISTILL, RecipeTiming.ticks(10L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolMedium", 1000)},
                new FluidStack[] {field("Lubricant", 162), field("Petrol", 423), field("GenGas_OilGas", 247), field("GenGas_CrackedOilGas", 392)});
        // OilProcessing.java:185
        add("OilProcessing.java:185", RecipeMaps.DISTILL, RecipeTiming.ticks(10L).euPerTick(120L),
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolHigh", 1000)},
                new FluidStack[] {field("Lubricant", 92), field("Petrol", 321), field("GenGas_OilGas", 301), field("GenGas_CrackedOilGas", 542)});
    }

    private static void maskAligner() {
        // ComputerBuilding.java:213
        add("ComputerBuilding.java:213", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(3000L).euPerTick(128L),
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("cpu_photomask_t1", 1)},
                new ItemStack[] {own("cpu_wafer_t1", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:214
        add("ComputerBuilding.java:214", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(3000L).euPerTick(256L),
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("cpu_photomask_t2", 1)},
                new ItemStack[] {own("cpu_wafer_t2", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:215
        add("ComputerBuilding.java:215", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(16000L).euPerTick(256L),
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("cpu_photomask_t3", 1)},
                new ItemStack[] {own("cpu_wafer_t3", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:216
        add("ComputerBuilding.java:216", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(16000L).euPerTick(512L),
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("cpu_photomask_t4", 1)},
                new ItemStack[] {own("cpu_wafer_t4", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:217
        add("ComputerBuilding.java:217", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(24000L).euPerTick(1024L),
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("cpu_photomask_t5", 1)},
                new ItemStack[] {own("cpu_wafer_t5", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:218
        add("ComputerBuilding.java:218", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(48000L).euPerTick(2048L),
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("cpu_photomask_t6", 1)},
                new ItemStack[] {own("cpu_wafer_t6", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:219
        add("ComputerBuilding.java:219", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(48000L).euPerTick(4096L),
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("cpu_photomask_t7", 1)},
                new ItemStack[] {own("cpu_wafer_t7", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:220
        add("ComputerBuilding.java:220", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(64000L).euPerTick(8192L),
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("cpu_photomask_t8", 1)},
                new ItemStack[] {own("cpu_wafer_t8", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:222
        add("ComputerBuilding.java:222", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(2000L).euPerTick(100L),
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("ram_photomask_t1", 1)},
                new ItemStack[] {own("ram_wafer_t1", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:223
        add("ComputerBuilding.java:223", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(2000L).euPerTick(240L),
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("ram_photomask_t2", 1)},
                new ItemStack[] {own("ram_wafer_t2", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:224
        add("ComputerBuilding.java:224", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(10000L).euPerTick(242L),
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("ram_photomask_t3", 1)},
                new ItemStack[] {own("ram_wafer_t3", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:225
        add("ComputerBuilding.java:225", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(10000L).euPerTick(500L),
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("ram_photomask_t4", 1)},
                new ItemStack[] {own("ram_wafer_t4", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:226
        add("ComputerBuilding.java:226", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(14000L).euPerTick(980L),
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("ram_photomask_t5", 1)},
                new ItemStack[] {own("ram_wafer_t5", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:227
        add("ComputerBuilding.java:227", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(24000L).euPerTick(1510L),
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("ram_photomask_t6", 1)},
                new ItemStack[] {own("ram_wafer_t6", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:228
        add("ComputerBuilding.java:228", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(24000L).euPerTick(3070L),
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("ram_photomask_t7", 1)},
                new ItemStack[] {own("ram_wafer_t7", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:229
        add("ComputerBuilding.java:229", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(32000L).euPerTick(7092L),
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("ram_photomask_t8", 1)},
                new ItemStack[] {own("ram_wafer_t8", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:231
        add("ComputerBuilding.java:231", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(2000L).euPerTick(120L),
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("circuit_part_photomask_t3", 1)},
                new ItemStack[] {own("circuit_part_wafer_t3", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:232
        add("ComputerBuilding.java:232", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(2000L).euPerTick(180L),
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("circuit_part_photomask_t4", 1)},
                new ItemStack[] {own("circuit_part_wafer_t4", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:233
        add("ComputerBuilding.java:233", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(9000L).euPerTick(546L),
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("circuit_part_photomask_t5", 1)},
                new ItemStack[] {own("circuit_part_wafer_t5", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:234
        add("ComputerBuilding.java:234", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(9000L).euPerTick(842L),
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("circuit_part_photomask_t6", 1)},
                new ItemStack[] {own("circuit_part_wafer_t6", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:236
        add("ComputerBuilding.java:236", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(12000L).euPerTick(4096L),
                new ItemStack[] {own("mo_o2_plate_soft_baked_t1", 1), own("inter_layer_photomask_t1", 1)},
                new ItemStack[] {own("inter_layer_wafer_t7", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:237
        add("ComputerBuilding.java:237", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(12000L).euPerTick(6144L),
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("inter_layer_photomask_t2", 1)},
                new ItemStack[] {own("inter_layer_wafer_t8", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:239
        add("ComputerBuilding.java:239", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(2000L).euPerTick(180L),
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("diode_photomask_t1", 1)},
                new ItemStack[] {own("diode_wafer_t1", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
        // ComputerBuilding.java:240
        add("ComputerBuilding.java:240", RecipeMaps.MASK_ALIGNER, RecipeTiming.ticks(9000L).euPerTick(842L),
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("diode_photomask_t2", 1)},
                new ItemStack[] {own("diode_wafer_t2", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1},
                null);
    }

    private static void cNC() {
        // CompactItem.java:65
        add("CompactItem.java:65", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(65L),
                new ItemStack[] {form("ingotDouble", "Bronze", 1), form("plateCurved", "Bronze", 1), form("stick", "Bronze", 1)},
                new ItemStack[] {own("engine_cylinder1", 1), form("dustSmall", "Bronze", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:66
        add("CompactItem.java:66", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(65L),
                new ItemStack[] {form("rotor", "Bronze", 1), form("plateCurved", "Bronze", 2), form("plate", "Bronze", 1)},
                new ItemStack[] {own("engine_turbo1", 1), form("dustSmall", "Bronze", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:74
        add("CompactItem.java:74", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(65L),
                new ItemStack[] {form("ingotDouble", "ArsenicCopper", 1), form("plateCurved", "ArsenicCopper", 1), form("stick", "ArsenicCopper", 1)},
                new ItemStack[] {own("engine_cylinder2", 1), form("dustSmall", "ArsenicCopper", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:75
        add("CompactItem.java:75", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(65L),
                new ItemStack[] {form("rotor", "ArsenicCopper", 1), form("plateCurved", "ArsenicCopper", 2), form("plate", "ArsenicCopper", 1)},
                new ItemStack[] {own("engine_turbo2", 1), form("dustSmall", "ArsenicCopper", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:83
        add("CompactItem.java:83", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(65L),
                new ItemStack[] {form("ingotDouble", "ArsenicBronze", 1), form("plateCurved", "ArsenicBronze", 1), form("stick", "ArsenicBronze", 1)},
                new ItemStack[] {own("engine_cylinder3", 1), form("dustSmall", "ArsenicBronze", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:84
        add("CompactItem.java:84", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(65L),
                new ItemStack[] {form("rotor", "ArsenicBronze", 1), form("plateCurved", "ArsenicBronze", 2), form("plate", "ArsenicBronze", 1)},
                new ItemStack[] {own("engine_turbo3", 1), form("dustSmall", "ArsenicBronze", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:92
        add("CompactItem.java:92", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(96L),
                new ItemStack[] {form("ingotDouble", "Steel", 1), form("plateCurved", "Steel", 1), form("stick", "Steel", 1)},
                new ItemStack[] {own("engine_cylinder4", 1), form("dustSmall", "Steel", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:93
        add("CompactItem.java:93", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(96L),
                new ItemStack[] {form("rotor", "Steel", 1), form("plateCurved", "Steel", 2), form("plate", "Steel", 1)},
                new ItemStack[] {own("engine_turbo4", 1), form("dustSmall", "Steel", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:101
        add("CompactItem.java:101", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(128L),
                new ItemStack[] {form("ingotDouble", "Invar", 1), form("plateCurved", "Invar", 1), form("stick", "Invar", 1)},
                new ItemStack[] {own("engine_cylinder5", 1), form("dustSmall", "Invar", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:102
        add("CompactItem.java:102", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(128L),
                new ItemStack[] {form("rotor", "Invar", 1), form("plateCurved", "Invar", 2), form("plate", "Invar", 1)},
                new ItemStack[] {own("engine_turbo5", 1), form("dustSmall", "Invar", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:111
        add("CompactItem.java:111", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(172L),
                new ItemStack[] {form("ingotDouble", "Titanium", 1), form("plateCurved", "Titanium", 1), form("stick", "Titanium", 1)},
                new ItemStack[] {own("engine_cylinder6", 1), form("dustSmall", "Titanium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:112
        add("CompactItem.java:112", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(172L),
                new ItemStack[] {form("rotor", "Titanium", 1), form("plateCurved", "Titanium", 2), form("plate", "Titanium", 1)},
                new ItemStack[] {own("engine_turbo6", 1), form("dustSmall", "Titanium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:120
        add("CompactItem.java:120", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(256L),
                new ItemStack[] {form("ingotDouble", "Tungstensteel", 1), form("plateCurved", "Tungstensteel", 1), form("stick", "Tungstensteel", 1)},
                new ItemStack[] {own("engine_cylinder7", 1), form("dustSmall", "Tungstensteel", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:121
        add("CompactItem.java:121", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(256L),
                new ItemStack[] {form("rotor", "Tungstensteel", 1), form("plateCurved", "Tungstensteel", 2), form("plate", "Tungstensteel", 1)},
                new ItemStack[] {own("engine_turbo7", 1), form("dustSmall", "Tungstensteel", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:129
        add("CompactItem.java:129", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(256L),
                new ItemStack[] {form("ingotDouble", "Iridium", 1), form("plateCurved", "Iridium", 1), form("stick", "Iridium", 1)},
                new ItemStack[] {own("engine_cylinder8", 1), form("dustSmall", "Iridium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:130
        add("CompactItem.java:130", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(256L),
                new ItemStack[] {form("rotor", "Iridium", 1), form("plateCurved", "Iridium", 2), form("plate", "Iridium", 1)},
                new ItemStack[] {own("engine_turbo8", 1), form("dustSmall", "Iridium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:138
        add("CompactItem.java:138", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(256L),
                new ItemStack[] {form("ingotDouble", "Chromium", 1), form("plateCurved", "Chromium", 1), form("stick", "Chromium", 1)},
                new ItemStack[] {own("engine_cylinder_cr", 1), form("dustSmall", "Chromium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:139
        add("CompactItem.java:139", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(256L),
                new ItemStack[] {form("rotor", "Chromium", 1), form("plateCurved", "Chromium", 2), form("plate", "Chromium", 1)},
                new ItemStack[] {own("engine_turbo_cr", 1), form("dustSmall", "Chromium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:146
        add("CompactItem.java:146", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(256L),
                new ItemStack[] {form("ingotDouble", "OsmiumElemental", 1), form("plateCurved", "OsmiumElemental", 1), form("stick", "OsmiumElemental", 1)},
                new ItemStack[] {own("engine_cylinder_os", 1), form("dustSmall", "OsmiumElemental", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:147
        add("CompactItem.java:147", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(256L),
                new ItemStack[] {form("rotor", "OsmiumElemental", 1), form("plateCurved", "OsmiumElemental", 2), form("plate", "OsmiumElemental", 1)},
                new ItemStack[] {own("engine_turbo_os", 1), form("dustSmall", "OsmiumElemental", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:151
        add("CompactItem.java:151", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(256L),
                new ItemStack[] {form("ingotDouble", "Trinaquadalloy", 1), form("plateCurved", "Trinaquadalloy", 1), form("stick", "Trinaquadalloy", 1)},
                new ItemStack[] {own("engine_cylinder9", 1), form("dustSmall", "Trinaquadalloy", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:152
        add("CompactItem.java:152", RecipeMaps.CNC, RecipeTiming.ticks(200L).euPerTick(256L),
                new ItemStack[] {form("rotor", "Trinaquadalloy", 1), form("plateCurved", "Trinaquadalloy", 2), form("plate", "Trinaquadalloy", 1)},
                new ItemStack[] {own("engine_turbo9", 1), form("dustSmall", "Trinaquadalloy", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:64
        add("CompactItem.java:64", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(65L),
                new ItemStack[] {form("stickLong", "Bronze", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft1", 1), form("dust", "Bronze", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:73
        add("CompactItem.java:73", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(65L),
                new ItemStack[] {form("stickLong", "ArsenicCopper", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft2", 1), form("dust", "ArsenicCopper", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:82
        add("CompactItem.java:82", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(65L),
                new ItemStack[] {form("stickLong", "ArsenicBronze", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft3", 1), form("dust", "ArsenicBronze", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:91
        add("CompactItem.java:91", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(96L),
                new ItemStack[] {form("stickLong", "Steel", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft4", 1), form("dust", "Steel", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:100
        add("CompactItem.java:100", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(128L),
                new ItemStack[] {form("stickLong", "Invar", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft5", 1), form("dust", "Invar", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:110
        add("CompactItem.java:110", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(172L),
                new ItemStack[] {form("stickLong", "Titanium", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft6", 1), form("dust", "Titanium", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:119
        add("CompactItem.java:119", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(256L),
                new ItemStack[] {form("stickLong", "Tungstensteel", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft7", 1), form("dust", "Tungstensteel", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:128
        add("CompactItem.java:128", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(256L),
                new ItemStack[] {form("stickLong", "Iridium", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft8", 1), form("dust", "Iridium", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:137
        add("CompactItem.java:137", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(256L),
                new ItemStack[] {form("stickLong", "Chromium", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft_cr", 1), form("dust", "Chromium", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:145
        add("CompactItem.java:145", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(256L),
                new ItemStack[] {form("stickLong", "OsmiumElemental", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft_os", 1), form("dust", "OsmiumElemental", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:150
        add("CompactItem.java:150", RecipeMaps.CNC, RecipeTiming.ticks(600L).euPerTick(256L),
                new ItemStack[] {form("stickLong", "Trinaquadalloy", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft9", 1), form("dust", "Trinaquadalloy", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // QuantumEntry.java:66
        add("QuantumEntry.java:66", RecipeMaps.CNC, RecipeTiming.ticks(1600L).euPerTick(8192L),
                new ItemStack[] {form("plate", "MirrorGold", 4), block("quantum_stabilization_substrate", 1)},
                new ItemStack[] {block("quantum_coherence_casing_iridium", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(256L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "Magnalium", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "Magnalium", 1), form("dust", "Magnalium", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(10240L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "Trinitanium", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "Trinitanium", 1), form("dust", "Trinitanium", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(38400L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "Vibramantium", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "Vibramantium", 1), form("dust", "Vibramantium", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(51200L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "Trinaquadalloy", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "Trinaquadalloy", 1), form("dust", "Trinaquadalloy", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(512L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "Steel", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "Steel", 1), form("dust", "Steel", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(480L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "StainlessSteel", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "StainlessSteel", 1), form("dust", "StainlessSteel", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(3840L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "Titanium", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "Titanium", 1), form("dust", "Titanium", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(10240L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "Tungstensteel", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "Tungstensteel", 1), form("dust", "Tungstensteel", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(12800L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "Adamantium", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "Adamantium", 1), form("dust", "Adamantium", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(10240L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "Iridium", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "Iridium", 1), form("dust", "Iridium", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // recipePrefixItems.java:47
        add("recipePrefixItems.java:47", RecipeMaps.CNC, RecipeTiming.ticks(2560L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("ingot", "OsmiumElemental", 1)},
                new ItemStack[] {form("bladeLargeTurbine", "OsmiumElemental", 1), form("dust", "OsmiumElemental", 2)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // tileEntityInit0.java:590
        add("tileEntityInit0.java:590", RecipeMaps.CNC, RecipeTiming.ticks(8000L).euPerTick(200L),
                new ItemStack[] {circuit(0, 1), gt("tank_wall", 1)},
                new ItemStack[] {block("stainless_turbine_shell", 1)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // tileEntityInit0.java:591
        add("tileEntityInit0.java:591", RecipeMaps.CNC, RecipeTiming.ticks(8000L).euPerTick(200L),
                new ItemStack[] {circuit(0, 1), gt("titanium_wall", 1)},
                new ItemStack[] {block("titanium_turbine_shell", 1)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // tileEntityInit0.java:592
        add("tileEntityInit0.java:592", RecipeMaps.CNC, RecipeTiming.ticks(8000L).euPerTick(200L),
                new ItemStack[] {circuit(0, 1), gt("tungstensteel_wall", 1)},
                new ItemStack[] {block("tungstensteel_turbine_shell", 1)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // tileEntityInit0.java:593
        add("tileEntityInit0.java:593", RecipeMaps.CNC, RecipeTiming.ticks(8000L).euPerTick(200L),
                new ItemStack[] {circuit(0, 1), gt("adamantium_wall", 1)},
                new ItemStack[] {block("adamantium_turbine_shell", 1)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // tileEntityInit0.java:610
        add("tileEntityInit0.java:610", RecipeMaps.CNC, RecipeTiming.ticks(8000L).euPerTick(200L),
                new ItemStack[] {circuit(0, 1), block("trinaquadalloy_wall", 1)},
                new ItemStack[] {block("trinaquadalloy_turbine_shell", 1)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
    }

    private static void fusionTokamak() {
        // Fusion.java:34
        add("Fusion.java:34", RecipeMaps.FUSION_TOKAMAK, RecipeTiming.ticks(400L).euPerTick(2048L),
                new ItemStack[] {circuit(0, 1)},
                NO_ITEMS,
                new FluidStack[] {field("Deuterium", 1000), field("Tritium", 1000)},
                new FluidStack[] {field("Helium", 230), field("MatterNeutral", 64864800)},
                1048576L,
                null,
                null,
                null);
        // Fusion.java:43
        add("Fusion.java:43", RecipeMaps.FUSION_TOKAMAK, RecipeTiming.ticks(400L).euPerTick(8192L),
                NO_ITEMS,
                new ItemStack[] {own("neutron", 2)},
                new FluidStack[] {field("Deuterium", 4000), field("Tritium", 4000)},
                new FluidStack[] {field("Helium", 2000)},
                1048576L,
                null,
                null,
                null);
        // Fusion.java:46
        add("Fusion.java:46", RecipeMaps.FUSION_TOKAMAK, RecipeTiming.ticks(600L).euPerTick(16384L),
                NO_ITEMS,
                new ItemStack[] {own("neutron", 1)},
                new FluidStack[] {field("Deuterium", 2000), field("GenGas_Helium3", 1000)},
                new FluidStack[] {field("Helium", 2000)},
                4194304L,
                null,
                null,
                null);
    }

    private static void fusionTokamakExperimental() {
        // Fusion.java:35
        add("Fusion.java:35", RecipeMaps.FUSION_TOKAMAK_EXPERIMENTAL, RecipeTiming.ticks(400L).euPerTick(2048L),
                new ItemStack[] {circuit(0, 1)},
                new ItemStack[] {own("tokamak_neutron_data", 1)},
                new FluidStack[] {field("Deuterium", 1000), field("Tritium", 1000)},
                new FluidStack[] {field("Helium", 230), field("MatterNeutral", 64864800)},
                1048576L,
                new int[] {5000},
                null,
                null);
        // Fusion.java:38
        add("Fusion.java:38", RecipeMaps.FUSION_TOKAMAK_EXPERIMENTAL, RecipeTiming.ticks(400L).euPerTick(2048L),
                new ItemStack[] {circuit(0, 1)},
                new ItemStack[] {own("tokamak_plasma_data", 1)},
                new FluidStack[] {field("Deuterium", 2000)},
                new FluidStack[] {field("GenGas_Helium3", 10), field("MatterNeutral", 6486480)},
                1048576L,
                new int[] {5000},
                null,
                null);
        // Fusion.java:50
        add("Fusion.java:50", RecipeMaps.FUSION_TOKAMAK_EXPERIMENTAL, RecipeTiming.ticks(400L).euPerTick(8192L),
                NO_ITEMS,
                new ItemStack[] {own("tokamak_neutron_data", 1)},
                new FluidStack[] {field("Deuterium", 4000), field("Tritium", 4000)},
                new FluidStack[] {field("Helium", 2000)},
                1048576L,
                new int[] {5000},
                null,
                null);
        // Fusion.java:53
        add("Fusion.java:53", RecipeMaps.FUSION_TOKAMAK_EXPERIMENTAL, RecipeTiming.ticks(600L).euPerTick(16384L),
                NO_ITEMS,
                new ItemStack[] {own("tokamak_plasma_data", 1)},
                new FluidStack[] {field("Deuterium", 2000), field("GenGas_Helium3", 1000)},
                new FluidStack[] {field("Helium", 2000)},
                4194304L,
                new int[] {7500},
                null,
                null);
    }

    private static void laserFusion() {
        // Fusion.java:82
        add("Fusion.java:82", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1L).euPerTick(32768L),
                new ItemStack[] {own("laser_target_bismuth", 1), own("alpha_particle", 1)},
                new ItemStack[] {own("superheavy_nuclide_precursor", 1)},
                new FluidStack[] {field("Deuterium", 8000)},
                new FluidStack[] {field("Helium", 10)},
                900000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Lithium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Lithium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Iron", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Iron", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Nickel", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Nickel", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Cobalt", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Cobalt", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Copper", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Copper", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Zinc", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Zinc", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Silver", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Silver", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Tin", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Tin", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Lead", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Lead", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Bismuth", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Bismuth", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Aluminium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Aluminium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Titanium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Titanium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Zirconium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Zirconium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Niobium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Niobium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Tantalum", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Tantalum", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Chromium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Chromium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Gold", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Gold", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Platinum", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Platinum", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Palladium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Palladium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Rhodium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Rhodium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Iridium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Iridium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "OsmiumElemental", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "OsmiumElemental", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "intellite", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "intellite", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Tungsten", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Tungsten", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Carbon", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Carbon", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Silicon", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Silicon", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Germanium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Germanium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Gallium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Gallium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Indium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Indium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Deuterium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Deuterium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Tritium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Tritium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Uranium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Uranium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Thorium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Thorium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1200L).euPerTick(32768L),
                new ItemStack[] {form("dustAnalyticalPure", "Naquadria", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Naquadria", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null,
                null);
        // Fusion.java:59
        add("Fusion.java:59", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1L).euPerTick(2048L),
                new ItemStack[] {own("laser_target_dt", 1)},
                new ItemStack[] {own("neutron", 4)},
                new FluidStack[] {field("Deuterium", 4000)},
                new FluidStack[] {field("Helium", 2000), field("Tritium", 10)},
                10000000L,
                null,
                null,
                null);
        // Fusion.java:61
        add("Fusion.java:61", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1L).euPerTick(4096L),
                new ItemStack[] {own("laser_target_li6", 1)},
                new ItemStack[] {own("neutron", 1)},
                new FluidStack[] {field("Deuterium", 2000)},
                new FluidStack[] {field("Tritium", 2000), field("Helium", 10)},
                12000000L,
                null,
                null,
                null);
        // Fusion.java:72
        add("Fusion.java:72", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1L).euPerTick(8192L),
                new ItemStack[] {own("laser_target_lead", 1)},
                new ItemStack[] {own("neutron_rich_bismuth", 2)},
                new FluidStack[] {field("Deuterium", 4000)},
                new FluidStack[] {field("Helium", 10)},
                100000000L,
                null,
                null,
                null);
        // Fusion.java:74
        add("Fusion.java:74", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1L).euPerTick(8192L),
                new ItemStack[] {own("laser_target_tantalum", 1)},
                new ItemStack[] {own("metastable_tantalum", 2)},
                new FluidStack[] {field("Deuterium", 4000)},
                new FluidStack[] {field("Helium", 10)},
                120000000L,
                null,
                null,
                null);
        // Fusion.java:76
        add("Fusion.java:76", RecipeMaps.LASER_FUSION, RecipeTiming.ticks(1L).euPerTick(8192L),
                new ItemStack[] {own("laser_target_graphite", 1)},
                new ItemStack[] {own("dense_graphene_precursor", 1)},
                new FluidStack[] {field("Deuterium", 4000)},
                new FluidStack[] {field("Helium", 10)},
                150000000L,
                null,
                null,
                null);
    }

    private static void laserFusionExperimental() {
        // Fusion.java:63
        add("Fusion.java:63", RecipeMaps.LASER_FUSION_EXPERIMENTAL, RecipeTiming.ticks(1L).euPerTick(2048L),
                new ItemStack[] {own("laser_target_dt", 1)},
                new ItemStack[] {own("laser_fusion_compression_data", 1)},
                new FluidStack[] {field("Deuterium", 4000)},
                new FluidStack[] {field("Helium", 2000), field("Tritium", 10)},
                10000000L,
                new int[] {5000},
                null,
                null);
        // Fusion.java:66
        add("Fusion.java:66", RecipeMaps.LASER_FUSION_EXPERIMENTAL, RecipeTiming.ticks(1L).euPerTick(4096L),
                new ItemStack[] {own("laser_target_li6", 1)},
                new ItemStack[] {own("laser_fusion_neutron_data", 1)},
                new FluidStack[] {field("Deuterium", 2000)},
                new FluidStack[] {field("Tritium", 2000), field("Helium", 10)},
                12000000L,
                new int[] {5000},
                null,
                null);
    }

    private static void catalyticReactor() {
        // CatalyticChemistry.java:50
        add("CatalyticChemistry.java:50", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(480L).euPerTick(1536L),
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "SBR", 2)},
                new FluidStack[] {field("GenLiquid_Benzene", 1000), field("Methane", 2500), field("Oxygen", 1500)},
                new FluidStack[] {field("Water", 1500)},
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:56
        add("CatalyticChemistry.java:56", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(520L).euPerTick(1536L),
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "EpoxyResin", 4)},
                new FluidStack[] {field("GenLiquid_Benzene", 1000), field("Propylene", 1500), field("Oxygen", 1800)},
                new FluidStack[] {field("Water", 1200)},
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:62
        add("CatalyticChemistry.java:62", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(420L).euPerTick(1280L),
                new ItemStack[] {own("ziegler_natta_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "PolymethylMethacrylate", 2)},
                new FluidStack[] {field("Methane", 2500), field("Oxygen", 1200), field("GenLiquid_Methanol", 1000)},
                new FluidStack[] {field("Water", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:68
        add("CatalyticChemistry.java:68", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(500L).euPerTick(1536L),
                new ItemStack[] {own("platinum_palladium_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "HardPlastic", 3)},
                new FluidStack[] {field("GenLiquid_Benzene", 1000), field("GenGas_Phosgene", 750), field("Oxygen", 1000)},
                new FluidStack[] {field("Hydrogen", 240)},
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:79
        add("CatalyticChemistry.java:79", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(300L).euPerTick(512L),
                new ItemStack[] {form("dustTiny", "Platinum", 4), form("dustTiny", "Palladium", 4), form("dust", "Carbon", 4), form("dust", "Alumina", 2)},
                new ItemStack[] {own("platinum_palladium_catalyst_cluster", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // CatalyticChemistry.java:127
        add("CatalyticChemistry.java:127", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(1200L).euPerTick(8192L),
                new ItemStack[] {own("polycondensation_catalyst_cluster", 1), form("dust", "Difluorobenzophenone", 1), form("dust", "Hydroquinone", 1), form("dust", "PotassiumCarbonate", 2)},
                new ItemStack[] {form("dust", "PEEK", 2)},
                NO_FLUIDS,
                new FluidStack[] {field("Water", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:38
        add("CatalyticChemistry.java:38", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(320L).euPerTick(1024L),
                new ItemStack[] {own("nickel_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "Plastic", 4)},
                new FluidStack[] {field("Methane", 3000), field("Oxygen", 1500)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:44
        add("CatalyticChemistry.java:44", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(360L).euPerTick(1024L),
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "PVC", 3)},
                new FluidStack[] {field("Methane", 3000), field("GenGas_Chlorine", 2000)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:75
        add("CatalyticChemistry.java:75", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(240L).euPerTick(256L),
                new ItemStack[] {form("dust", "Nickel", 4), form("dust", "Alumina", 4), form("dust", "SiliconDioxide", 2)},
                new ItemStack[] {own("nickel_catalyst_cluster", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // CatalyticChemistry.java:83
        add("CatalyticChemistry.java:83", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(360L).euPerTick(512L),
                new ItemStack[] {form("dust", "SiliconDioxide", 4), form("dust", "Alumina", 2), form("dust", "Sodium", 1)},
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // CatalyticChemistry.java:87
        add("CatalyticChemistry.java:87", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(400L).euPerTick(1024L),
                new ItemStack[] {form("dust", "TriethylAluminium", 1), form("dust", "MagnesiumChloride", 4)},
                new ItemStack[] {own("ziegler_natta_catalyst_cluster", 1)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 1000)},
                NO_FLUIDS);
        // CatalyticChemistry.java:97
        add("CatalyticChemistry.java:97", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(80L).euPerTick(256L),
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Benzene", 1000), field("GenGas_Fluorine", 1000)},
                new FluidStack[] {field("GenLiquid_Fluorobenzene", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:102
        add("CatalyticChemistry.java:102", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(80L).euPerTick(256L),
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Toluene", 1000), field("GenGas_Fluorine", 1000)},
                new FluidStack[] {field("GenLiquid_Fluorotoluene", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:107
        add("CatalyticChemistry.java:107", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(140L).euPerTick(384L),
                new ItemStack[] {own("nickel_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "FluorobenzoicAcid", 1)},
                new FluidStack[] {field("GenLiquid_Fluorotoluene", 1000), field("Oxygen", 1000)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:112
        add("CatalyticChemistry.java:112", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(160L).euPerTick(512L),
                new ItemStack[] {own("platinum_palladium_catalyst_cluster", 1), form("dust", "FluorobenzoicAcid", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Phosgene", 1000)},
                new FluidStack[] {field("GenLiquid_FluorobenzoylChloride", 1000), field("CarbonDioxide", 1000), field("GenGas_HydrochloricAcid", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:117
        add("CatalyticChemistry.java:117", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(240L).euPerTick(768L),
                new ItemStack[] {own("platinum_palladium_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "Difluorobenzophenone", 1)},
                new FluidStack[] {field("GenLiquid_Fluorobenzene", 1000), field("GenLiquid_FluorobenzoylChloride", 1000)},
                new FluidStack[] {field("GenGas_HydrochloricAcid", 1000)},
                0L,
                null,
                new int[] {0},
                null);
        // CatalyticChemistry.java:122
        add("CatalyticChemistry.java:122", RecipeMaps.CATALYTIC_REACTOR, RecipeTiming.ticks(180L).euPerTick(512L),
                new ItemStack[] {own("nickel_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "Hydroquinone", 1)},
                new FluidStack[] {field("GenLiquid_Phenol", 1000), field("GenLiquid_HydrogenPeroxide", 1000)},
                new FluidStack[] {field("Water", 500)},
                0L,
                null,
                new int[] {0},
                null);
    }

    private static void mirrorGoldSynthesis() {
        // QuantumEntry.java:47
        add("QuantumEntry.java:47", RecipeMaps.MIRROR_GOLD_SYNTHESIS, RecipeTiming.ticks(4800L).euPerTick(8192L),
                new ItemStack[] {form("dustAbsolutelyPure", "intellite", 8), form("dustAbsolutelyPure", "Naquadria", 4), form("dustAbsolutelyPure", "Platinum", 4), form("nugget", "intellite", 16), form("ingot", "MirrorGold", 1)},
                new ItemStack[] {form("plate", "MirrorGold", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
        // QuantumEntry.java:38
        add("QuantumEntry.java:38", RecipeMaps.MIRROR_GOLD_SYNTHESIS, RecipeTiming.ticks(2400L).euPerTick(8192L),
                new ItemStack[] {form("ingot", "MirrorGold", 1), form("dustAnalyticalPure", "intellite", 2), form("dustAnalyticalPure", "Naquadria", 2), form("nugget", "intellite", 4)},
                new ItemStack[] {form("ingot", "MirrorGold", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void mirrorGoldQuantumization() {
        // QuantumEntry.java:89
        add("QuantumEntry.java:89", RecipeMaps.MIRROR_GOLD_QUANTUMIZATION, RecipeTiming.ticks(2400L).euPerTick(16384L),
                new ItemStack[] {form("plate", "MirrorGold", 1), form("dustAbsolutelyPure", "intellite", 8), form("dustAbsolutelyPure", "Naquadria", 4), gt("compact_signal_emitter_luv", 2), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("quantum_gold_drive_module", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
    }

    private static void cosmicRadiationResearch() {
        // ComputerBuilding.java:176
        add("ComputerBuilding.java:176", RecipeMaps.COSMIC_RADIATION, RecipeTiming.ticks(2400L).euPerTick(8192L),
                new ItemStack[] {form("dustAnalyticalPure", "Gold", 1)},
                new ItemStack[] {own("cosmic_radiation_background_data", 1), own("cosmic_radiation_spectrum_data", 1), own("high_energy_cosmic_particle_data", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {8000, 4000, 2000},
                null,
                null);
    }

    private static void microgravityCrystalGrowth() {
        // ComputerBuilding.java:171
        add("ComputerBuilding.java:171", RecipeMaps.MICROGRAVITY_CRYSTAL, RecipeTiming.ticks(144000L).euPerTick(8192L),
                new ItemStack[] {form("dustAnalyticalPure", "Molybdenum", 8)},
                new ItemStack[] {own("mo_o2_boule_pure", 1)},
                new FluidStack[] {field("GenGas_Krypton", 40000), field("Oxygen", 4000)},
                NO_FLUIDS);
        // ComputerBuilding.java:172
        add("ComputerBuilding.java:172", RecipeMaps.MICROGRAVITY_CRYSTAL, RecipeTiming.ticks(144000L).euPerTick(8192L),
                new ItemStack[] {form("dustAnalyticalPure", "Molybdenum", 8)},
                new ItemStack[] {own("mo_o2_boule_pure", 1)},
                new FluidStack[] {field("GenGas_Xenon", 20000), field("Oxygen", 4000)},
                NO_FLUIDS);
        // ComputerBuilding.java:173
        add("ComputerBuilding.java:173", RecipeMaps.MICROGRAVITY_CRYSTAL, RecipeTiming.ticks(144000L).euPerTick(8192L),
                new ItemStack[] {form("dustAnalyticalPure", "Molybdenum", 8)},
                new ItemStack[] {own("mo_o2_boule_pure", 1)},
                new FluidStack[] {field("GenGas_Radon", 20000), field("Oxygen", 4000)},
                NO_FLUIDS);
    }

    private static void quantumGoldSynthesis() {
        // QuantumEntry.java:107
        add("QuantumEntry.java:107", RecipeMaps.QUANTUM_GOLD, RecipeTiming.ticks(4800L).euPerTick(65536L),
                new ItemStack[] {form("plate", "MirrorGold", 1), form("dustTiny", "Naquadria", 8), form("nugget", "intellite", 16), form("dustAbsolutelyPure", "intellite", 8)},
                new ItemStack[] {form("ingot", "QuantumGold", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void quantumObservation() {
        // QuantumObservation.java:29
        add("QuantumObservation.java:29", RecipeMaps.QUANTUM_OBSERVATION, RecipeTiming.ticks(4800L).euPerTick(16384L),
                new ItemStack[] {form("plate", "QuantumGold", 1), form("foil", "MirrorGold", 2), form("dustAbsolutelyPure", "intellite", 4), form("dustAbsolutelyPure", "Naquadria", 4)},
                new ItemStack[] {own("quantum_observation_data", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // QuantumObservation.java:37
        add("QuantumObservation.java:37", RecipeMaps.QUANTUM_OBSERVATION, RecipeTiming.ticks(2400L).euPerTick(8192L),
                new ItemStack[] {form("foil", "QuantumGold", 1), block("quantum_readout_coil", 2), form("dustAnalyticalPure", "Platinum", 4)},
                new ItemStack[] {own("quantum_observation_data", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void quantumCommunication() {
        // QuantumEntry.java:31
        add("QuantumEntry.java:31", RecipeMaps.QUANTUM_COMMUNICATION, RecipeTiming.ticks(3600L).euPerTick(8192L),
                new ItemStack[] {form("dustAnalyticalPure", "intellite", 4)},
                new ItemStack[] {form("ingot", "MirrorGold", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void researchAssembler() {
        // tileEntityInit0.java:680
        add("tileEntityInit0.java:680", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {gt("casing_machine_double_naquadria", 1), form("plate", "MirrorGold", 4), form("dustAbsolutelyPure", "intellite", 8), form("dustAbsolutelyPure", "Naquadria", 4), block("entanglement_channel_module", 4), block("quantum_readout_coil", 2), gt("compact_signal_emitter_luv", 4), gt("circuit_ultimate", 4)},
                new ItemStack[] {block("wireless_battery_quantum", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:154
        add("Fusion.java:154", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(3200L).euPerTick(65536L),
                new ItemStack[] {gt("casing_machine_double_stainlesssteel", 1), block("tokamak_magnetic_conductor_exp", 8), block("tokamak_magnetic_coil_exp", 4), block("tokamak_magnetic_wall_exp", 4), gt("circuit_elite", 2), gt("compact_sensor_ev", 2), own("computer_gt3660", 1)},
                new ItemStack[] {block("fusion_tokamak_exp", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 9)},
                NO_FLUIDS);
        // Fusion.java:163
        add("Fusion.java:163", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(4800L).euPerTick(262144L),
                new ItemStack[] {gt("casing_machine_double_tungstensteel", 1), block("tokamak_magnetic_conductor_t1", 16), block("tokamak_magnetic_coil_t1", 8), block("tokamak_magnetic_wall_t1", 8), gt("circuit_master", 4), gt("compact_sensor_iv", 2), own("computer_gt3699", 2)},
                new ItemStack[] {block("fusion_tokamak_t1", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 4)},
                NO_FLUIDS);
        // tileEntityInit0.java:529
        add("tileEntityInit0.java:529", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {form("plate", "MirrorGold", 2), gt("casing_machine_naquadria", 1), form("dustAbsolutelyPure", "intellite", 4), form("dustAbsolutelyPure", "Naquadria", 4), form("dustAnalyticalPure", "Platinum", 4), gt("compact_signal_emitter_luv", 4), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("quantum_gold_chamber", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:541
        add("tileEntityInit0.java:541", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {block("ai_routing_matrix", 1), block("ai_compute_frame", 4), block("ai_memory_bank", 4), own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "Silicon", 1), form("plate", "PEEK", 1), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("ai_flow_control", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:542
        add("tileEntityInit0.java:542", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {block("ai_packing_planner", 1), block("ai_compute_frame", 4), block("ai_memory_bank", 4), own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "Silicon", 1), form("plate", "PEEK", 1), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("ai_fill_planning", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:543
        add("tileEntityInit0.java:543", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {block("ai_recognition_matrix", 1), block("ai_compute_frame", 4), block("ai_memory_bank", 4), own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "Silicon", 1), form("plate", "PEEK", 1), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("ai_pattern_recognition", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:678
        add("tileEntityInit0.java:678", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(1200L).euPerTick(8192L),
                new ItemStack[] {gt("casing_machine_double_tungstensteel", 1), block("magnetic_conductor", 8), form("dustAnalyticalPure", "Copper", 16), form("dustAnalyticalPure", "Silver", 8), form("plate", "PEEK", 8), form("wireFine", "Graphene", 32), gt("circuit_master", 2)},
                new ItemStack[] {block("wireless_battery_inductive", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:683
        add("tileEntityInit0.java:683", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {form("plate", "QuantumGold", 4), form("dustAbsolutelyPure", "intellite", 4), form("dustAbsolutelyPure", "Naquadria", 4), block("quantum_readout_coil", 2), gt("compact_signal_emitter_luv", 4), gt("circuit_ultimate", 2), gt("casing_machine_trinaquadalloy", 1)},
                new ItemStack[] {block("quantum_observation_tower", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:528
        add("tileEntityInit0.java:528", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {form("plate", "MirrorGold", 2), gt("casing_machine_naquadria", 1), form("dustAbsolutelyPure", "intellite", 4), gt("compact_signal_emitter_luv", 4), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("mirror_gold_quantumization", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:471
        add("ComputerBuilding.java:471", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(16L),
                new ItemStack[] {form("plate", "SteelGalvanized", 4), form("screw", "Steel", 16), gt("circuit_basic", 8)},
                new ItemStack[] {own("computer_basic_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:472
        add("ComputerBuilding.java:472", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(160L).euPerTick(16L),
                new ItemStack[] {form("plate", "SteelGalvanized", 4), form("screw", "Steel", 16), gt("circuit_good", 8)},
                new ItemStack[] {own("computer_good_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:473
        add("ComputerBuilding.java:473", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(320L).euPerTick(16L),
                new ItemStack[] {form("plate", "SteelGalvanized", 4), form("screw", "Steel", 32), gt("circuit_advanced", 8)},
                new ItemStack[] {own("computer_advanced_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:474
        add("ComputerBuilding.java:474", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(640L).euPerTick(16L),
                new ItemStack[] {form("plate", "StainlessSteel", 4), form("screw", "Steel", 32), gt("circuit_elite", 8)},
                new ItemStack[] {own("computer_elite_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:475
        add("ComputerBuilding.java:475", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(960L).euPerTick(32L),
                new ItemStack[] {form("plate", "StainlessSteel", 4), form("screw", "StainlessSteel", 64), gt("circuit_master", 8)},
                new ItemStack[] {own("computer_master_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:476
        add("ComputerBuilding.java:476", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(1280L).euPerTick(32L),
                new ItemStack[] {form("plate", "Tungstensteel", 4), form("screw", "StainlessSteel", 64), gt("circuit_ultimate", 8)},
                new ItemStack[] {own("computer_ultimate_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // Fusion.java:141
        add("Fusion.java:141", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(2400L).euPerTick(32768L),
                new ItemStack[] {block("laser_focusing_array", 8), block("laser_target_injector", 1), block("laser_pulse_storage", 2)},
                new ItemStack[] {block("laser_fusion_experimental", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 9)},
                NO_FLUIDS);
        // Fusion.java:143
        add("Fusion.java:143", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(4800L).euPerTick(131072L),
                new ItemStack[] {block("laser_focusing_array", 24), block("laser_pulse_storage", 8), block("laser_product_collector", 4)},
                new ItemStack[] {block("laser_fusion_industrial", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 2)},
                NO_FLUIDS);
        // CompactItem.java:41
        add("CompactItem.java:41", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(372L),
                new ItemStack[] {form("plateTiny", "intellite", 1), form("wireFine", "Titanium", 4)},
                new ItemStack[] {own("intelligent_core", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // CompactItem.java:42
        add("CompactItem.java:42", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(1574L),
                new ItemStack[] {own("cpugt3660", 1), form("dust", "Titanium", 1)},
                new ItemStack[] {own("intelligent_core", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // CompactItem.java:55
        add("CompactItem.java:55", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(32L),
                new ItemStack[] {circuit(1, 1), form("plateGemTiny", "Silicon", 2), form("foil", "Copper", 1)},
                new ItemStack[] {own("good_circuit_part_core", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
        // CompactItem.java:155
        add("CompactItem.java:155", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(200L).euPerTick(320L),
                new ItemStack[] {form("bolt", "Cobalt60", 1), form("plateDense", "Lead", 2)},
                new ItemStack[] {own("co60_flaw_detection_core", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // CompactItem.java:54
        add("CompactItem.java:54", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(80L).euPerTick(16L),
                new ItemStack[] {circuit(0, 1), own("basic_circuit_part_core", 1)},
                new ItemStack[] {gt("circuit_part_basic", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ResearchRecipes.java:133
        add("ResearchRecipes.java:133", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(128L).euPerTick(16L),
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_helium", 1)},
                new FluidStack[] {field("Helium", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:134
        add("ResearchRecipes.java:134", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(128L).euPerTick(16L),
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_neon", 1)},
                new FluidStack[] {field("Neon", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:135
        add("ResearchRecipes.java:135", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(128L).euPerTick(16L),
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_argon", 1)},
                new FluidStack[] {field("Argon", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:136
        add("ResearchRecipes.java:136", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(128L).euPerTick(16L),
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_krypton", 1)},
                new FluidStack[] {field("GenGas_Krypton", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:137
        add("ResearchRecipes.java:137", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(128L).euPerTick(16L),
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_xenon", 1)},
                new FluidStack[] {field("GenGas_Xenon", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:138
        add("ResearchRecipes.java:138", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(128L).euPerTick(16L),
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_heliumneon", 1)},
                new FluidStack[] {field("GenGas_HeliumNeon", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:139
        add("ResearchRecipes.java:139", RecipeMaps.RESEARCH_ASSEMBLER, RecipeTiming.ticks(128L).euPerTick(16L),
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_carbonmonoxide", 1)},
                new FluidStack[] {field("GenGas_CarbonMonoxide", 1000)},
                NO_FLUIDS);
    }

    private static void researchReactor() {
        // CatalyticChemistry.java:91
        add("CatalyticChemistry.java:91", RecipeMaps.RESEARCH_REACTOR, RecipeTiming.ticks(600L).euPerTick(2048L),
                new ItemStack[] {form("dust", "PotassiumCarbonate", 4), form("dust", "Rutile", 2), form("dust", "SiliconDioxide", 2)},
                new ItemStack[] {own("polycondensation_catalyst_cluster", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void add(String where, String map, RecipeTiming timing,
            ItemStack[] itemsIn, ItemStack[] itemsOut, FluidStack[] fluidsIn, FluidStack[] fluidsOut) {
        add(where, map, timing, itemsIn, itemsOut, fluidsIn, fluidsOut, 0L, null, null, null);
    }

    private static void add(String where, String map, RecipeTiming timing,
            ItemStack[] itemsIn, ItemStack[] itemsOut, FluidStack[] fluidsIn, FluidStack[] fluidsOut,
            long special, int[] chances, int[] kept, int[] keptFluids) {
        if (broken(itemsIn) || brokenFluidInputs(fluidsIn) || broken(itemsOut) || broken(fluidsOut)) {
            return;
        }
        AddonRecipes.Line line = chances == null && kept == null && keptFluids == null
                ? new AddonRecipes.Line(itemsIn, itemsOut, fluidsIn, fluidsOut, timing.duration(), timing.euPerTick(), special)
                : new AddonRecipes.Line(itemsIn, itemsOut, fluidsIn, fluidsOut, timing.duration(), timing.euPerTick(), special,
                        chances, kept, keptFluids);
        if (AddonRecipes.map(map).add(line) == null) {
            PROBLEMS.add(where + " was rejected");
        }
    }

    private static boolean broken(ItemStack[] stacks) {
        for (ItemStack stack : stacks) {
            if (stack == null || stack.isEmpty()) return true;
        }
        return false;
    }

    private static boolean broken(FluidStack[] stacks) {
        for (FluidStack stack : stacks) {
            if (stack == null || stack.isEmpty()) return true;
        }
        return false;
    }

    /** An input keeps a 0 mB presence-only catalyst: its fluid must exist, its amount may be zero. */
    private static boolean brokenFluidInputs(FluidStack[] stacks) {
        for (FluidStack stack : stacks) {
            if (stack == null || stack.getFluid() == net.minecraft.world.level.material.Fluids.EMPTY) return true;
        }
        return false;
    }

    private static ItemStack form(String prefix, String material, int count) {
        if ("paneGlass".equals(prefix) && "Black".equals(material)) return new ItemStack(Items.BLACK_STAINED_GLASS_PANE, count);
        if ("plate".equals(prefix) && "Glass".equals(material)) return new ItemStack(Items.GLASS, count);
        if ("plate".equals(prefix) && "Paper".equals(material)) return new ItemStack(Items.PAPER, count);
        ItemStack stack = AddonItems.stack(prefix, material, count);
        if (stack == null || stack.isEmpty()) {
            PROBLEMS.add(prefix + " " + material);
            return ItemStack.EMPTY;
        }
        return stack;
    }

    private static ItemStack vanilla(ItemLike item, int count) {
        return new ItemStack(item, count);
    }

    private static ItemStack own(String path, int count) {
        ItemStack stack = KtfruItems.stack(path, count);
        if (stack == null || stack.isEmpty()) {
            PROBLEMS.add("item " + path);
            return ItemStack.EMPTY;
        }
        return stack;
    }

    private static ItemStack block(String path, int count) {
        String id = switch (path) {
            case "adamantium_turbine_shell" -> "turbine_shell_adamantium";
            case "ai_memory_bank" -> "ai_high_bandwidth_memory_bank";
            case "laser_focusing_array" -> "laser_fusion_focusing_array";
            case "laser_product_collector" -> "laser_fusion_neutron_product_collector";
            case "laser_pulse_storage" -> "laser_fusion_pulse_storage_module";
            case "laser_target_injector" -> "laser_fusion_target_injector";
            case "quantum_coherence_casing" -> "quantum_coherence_chamber_wall";
            case "quantum_coherence_casing_iridium" -> "quantum_coherence_chamber_wall_31111";
            case "room_energy_port" -> "room_energy_and_status_port";
            case "stainless_turbine_shell" -> "turbine_shell_stainless";
            case "sun_heater_top_layer" -> "heat_pipe_top";
            case "titanium_turbine_shell" -> "turbine_shell_titanium";
            case "trinaquadalloy_turbine_shell" -> "turbine_shell_trinaquadalloy";
            case "tungstensteel_turbine_shell" -> "turbine_shell_tungstensteel";
            default -> path;
        };
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(Ktfru.MODID, id));
        if (item == Items.AIR) {
            PROBLEMS.add("block " + path);
            return ItemStack.EMPTY;
        }
        return new ItemStack(item, count);
    }

    private static ItemStack gt(String path, int count) {
        String id = switch (path) {
            case "casing_machine_double_naquadria" -> "casing_machine_double_tungstensteel";
            case "casing_machine_naquadria" -> "casing_machine_trinaquadalloy";
            default -> path;
        };
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("gt6ce", id));
        if (item == null || item == Items.AIR) {
            PROBLEMS.add("gt6ce " + path);
            return ItemStack.EMPTY;
        }
        return new ItemStack(item, count);
    }

    private static ItemStack circuit(int index, int count) {
        ItemStack stack = AddonItems.selectorTag(index);
        if (stack == null || stack.isEmpty()) {
            PROBLEMS.add("selector " + index);
            return ItemStack.EMPTY;
        }
        return stack.copyWithCount(count);
    }

    private static FluidStack field(String name, int amount) {
        if (amount <= 0) {
            FluidStack sample = AddonFluids.still(name, 1);
            if (sample == null || sample.isEmpty()) {
                PROBLEMS.add("fluid " + name);
                return FluidStack.EMPTY;
            }
            return new FluidStack(sample.getFluid(), 0);
        }
        FluidStack stack = AddonFluids.still(name, amount);
        if (stack == null || stack.isEmpty()) {
            PROBLEMS.add("fluid " + name);
            return FluidStack.EMPTY;
        }
        return stack;
    }
}
