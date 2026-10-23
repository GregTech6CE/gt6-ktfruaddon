package com.lombinaxmasson.gt6ktfruaddon;

import java.util.LinkedHashSet;
import java.util.Set;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;

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
 * GregTech divides them. The light mixer row stays in {@link Gt6KtfruRecipes}.
 */
public final class Gt6KtfruSourceRecipes {
    private static final ItemStack[] NO_ITEMS = new ItemStack[0];
    private static final FluidStack[] NO_FLUIDS = new FluidStack[0];
    private static final Set<String> PROBLEMS = new LinkedHashSet<>();

    private Gt6KtfruSourceRecipes() {}

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
        add("Chemistry.java:28", Gt6KtfruMachines.FLUID_HEATING, 80L, 20L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("DistW", 10)},
                new FluidStack[] {field("Steam", 1600)});
        // Chemistry.java:30 D-tier Coolant_IC2.
        add("Chemistry.java:30", Gt6KtfruMachines.FLUID_HEATING, 20L, 10L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Coolant_IC2", 10)},
                new FluidStack[] {field("Coolant_IC2_Hot", 10)});
        // Chemistry.java:38
        add("Chemistry.java:38", Gt6KtfruMachines.FLUID_HEATING, 20L, 10L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("CarbonDioxide", 10)},
                new FluidStack[] {field("Hot_Carbon_Dioxide", 10)});
        // Chemistry.java:39
        add("Chemistry.java:39", Gt6KtfruMachines.FLUID_HEATING, 30L, 10L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Helium", 10)},
                new FluidStack[] {field("Hot_Helium", 10)});
    }

    private static void assembler() {
        // Circuits.java:36
        add("Circuits.java:36", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 120L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Constantan", 10), form("plate", "Ceramic", 1), form("foil", "Rubber", 6)},
                new ItemStack[] {own("resistance_t1", 16)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:37
        add("Circuits.java:37", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 180L, 90L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Nichrome", 10), form("plate", "Ceramic", 1), form("foil", "Rubber", 8)},
                new ItemStack[] {own("resistance_t2", 24)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:38
        add("Circuits.java:38", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1000L, 60L,
                new ItemStack[] {form("plate", "Copper", 1), form("plateTiny", "Ruthenium", 1), form("plate", "Ceramic", 1), form("dust", "Teflon", 4)},
                new ItemStack[] {own("resistance_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 14)},
                NO_FLUIDS);
        // Circuits.java:39
        add("Circuits.java:39", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 50L,
                new ItemStack[] {form("plate", "Gold", 1), form("plateTiny", "Ruthenium", 1), form("plate", "EpoxyResin", 1), form("dust", "Teflon", 4)},
                new ItemStack[] {own("resistance_t4", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 14)},
                NO_FLUIDS);
        // Circuits.java:40
        add("Circuits.java:40", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 50L,
                new ItemStack[] {form("plate", "Gold", 1), form("plateTiny", "Ruthenium", 1), form("plate", "EpoxyResin", 1), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("resistance_t4", 64), own("resistance_t4", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 14)},
                NO_FLUIDS);
        // Circuits.java:44
        add("Circuits.java:44", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 120L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Constantan", 10), form("plate", "Ceramic", 1), form("foil", "Plastic", 6)},
                new ItemStack[] {own("resistance_t1", 16)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:45
        add("Circuits.java:45", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 80L, 90L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Nichrome", 10), form("plate", "Ceramic", 1), form("foil", "Plastic", 8)},
                new ItemStack[] {own("resistance_t2", 24)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:49
        add("Circuits.java:49", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 180L,
                new ItemStack[] {form("plate", "Copper", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Copper", 32), form("foil", "Rubber", 8)},
                new ItemStack[] {own("coil_t1", 20)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:50
        add("Circuits.java:50", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 80L, 140L,
                new ItemStack[] {form("plate", "Copper", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Gold", 32), form("foil", "Rubber", 8)},
                new ItemStack[] {own("coil_t2", 20)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:51
        add("Circuits.java:51", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1000L, 100L,
                new ItemStack[] {form("plate", "Copper", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Platinum", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("coil_t3", 32)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:52
        add("Circuits.java:52", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 80L,
                new ItemStack[] {form("plate", "Gold", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Graphene", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("coil_t4", 32)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:53
        add("Circuits.java:53", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 80L,
                new ItemStack[] {form("plate", "Gold", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Graphene", 32), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("coil_t4", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:57
        add("Circuits.java:57", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 180L,
                new ItemStack[] {form("plate", "Copper", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Copper", 32), form("foil", "Plastic", 8)},
                new ItemStack[] {own("coil_t1", 20)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:58
        add("Circuits.java:58", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 80L, 140L,
                new ItemStack[] {form("plate", "Copper", 1), form("stick", "ElectricalSteel", 4), form("wireFine", "Gold", 32), form("foil", "Plastic", 8)},
                new ItemStack[] {own("coil_t2", 20)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:63
        add("Circuits.java:63", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 120L,
                new ItemStack[] {form("foil", "Copper", 16), form("foil", "Rubber", 8), form("plate", "Paper", 4), form("wireFine", "Copper", 8)},
                new ItemStack[] {own("capacitor_t1", 32)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Circuits.java:64
        add("Circuits.java:64", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 140L, 100L,
                new ItemStack[] {form("foil", "Aluminium", 16), form("foil", "Rubber", 8), form("dust", "Alumina", 16), form("wireFine", "Silver", 8)},
                new ItemStack[] {own("capacitor_t2", 40)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Circuits.java:65
        add("Circuits.java:65", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1000L, 80L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Platinum", 4), form("foil", "Niobium", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:66
        add("Circuits.java:66", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1000L, 80L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Platinum", 4), form("foil", "Tantalum", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:67
        add("Circuits.java:67", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1000L, 80L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Graphene", 4), form("foil", "Niobium", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:68
        add("Circuits.java:68", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1000L, 80L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Graphene", 4), form("foil", "Tantalum", 32), form("dust", "Teflon", 4)},
                new ItemStack[] {own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:69
        add("Circuits.java:69", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1000L, 80L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Platinum", 4), form("foil", "Niobium", 32), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("capacitor_t3", 64), own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:70
        add("Circuits.java:70", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1000L, 80L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Platinum", 4), form("foil", "Tantalum", 32), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("capacitor_t3", 64), own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:71
        add("Circuits.java:71", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1000L, 80L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Graphene", 4), form("foil", "Niobium", 32), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("capacitor_t3", 64), own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:72
        add("Circuits.java:72", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1000L, 80L,
                new ItemStack[] {form("plate", "Copper", 1), form("wireFine", "Graphene", 4), form("foil", "Tantalum", 32), form("dust", "NanoBariumTitanate", 4)},
                new ItemStack[] {own("capacitor_t3", 64), own("capacitor_t3", 64)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:74
        add("Circuits.java:74", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 256L, 60L,
                new ItemStack[] {form("plateGem", "Silicon", 1), form("dust", "Boron", 2), form("dust", "Phosphor", 4), form("foil", "Silver", 4), form("dust", "Teflon", 1)},
                new ItemStack[] {own("diode_t1", 32)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Circuits.java:75
        add("Circuits.java:75", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1024L, 10L,
                new ItemStack[] {own("diode_t2_part", 4), form("wireFine", "Silver", 1), form("dustTiny", "Teflon", 1)},
                new ItemStack[] {own("diode_t2", 4)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 1)},
                NO_FLUIDS);
        // Circuits.java:76
        add("Circuits.java:76", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 10L,
                new ItemStack[] {own("diode_t3_part", 4), form("wireFine", "Graphene", 1), form("dustTiny", "Teflon", 1)},
                new ItemStack[] {own("diode_t3", 4)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 1)},
                NO_FLUIDS);
        // Circuits.java:77
        add("Circuits.java:77", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 10L,
                new ItemStack[] {own("diode_t3_part", 4), form("wireFine", "Graphene", 1), form("dustTiny", "NanoBariumTitanate", 1)},
                new ItemStack[] {own("diode_t3", 8)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 1)},
                NO_FLUIDS);
        // Circuits.java:79
        add("Circuits.java:79", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 60L,
                new ItemStack[] {form("plateTiny", "Gallium", 4), form("dust", "Arsenic", 2), form("dust", "Aluminium", 1), form("foil", "Silver", 4)},
                new ItemStack[] {own("led_set", 2)},
                new FluidStack[] {field("GenLiquid_MoltenTeflon", 144)},
                NO_FLUIDS);
        // Circuits.java:84
        add("Circuits.java:84", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 80L,
                new ItemStack[] {form("plate", "Wood", 1), form("dust", "SiliconDioxide", 1)},
                new ItemStack[] {gt("circuit_plate", 1)},
                new FluidStack[] {field("Glue", 288)},
                NO_FLUIDS);
        // Circuits.java:86
        add("Circuits.java:86", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 80L,
                new ItemStack[] {form("foil", "Bakelite", 4)},
                new ItemStack[] {own("circuit_board_empty_t2", 1)},
                new FluidStack[] {field("Glue", 288)},
                NO_FLUIDS);
        // Circuits.java:88
        add("Circuits.java:88", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 80L,
                new ItemStack[] {form("foil", "Bakelite", 6), form("foil", "EpoxyResin", 6)},
                new ItemStack[] {own("circuit_board_empty_t3", 1)},
                new FluidStack[] {field("Glass", 576)},
                NO_FLUIDS);
        // Circuits.java:106
        add("Circuits.java:106", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 100L,
                new ItemStack[] {gt("circuit_part_basic", 2), gt("circuit_plate_copper", 1), own("coil_t1", 1), own("capacitor_t1", 2), own("resistance_t1", 4)},
                new ItemStack[] {gt("circuit_board_basic", 1)},
                new FluidStack[] {field("Glue", 72)},
                NO_FLUIDS);
        // Circuits.java:107
        add("Circuits.java:107", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 24L, 200L,
                new ItemStack[] {gt("circuit_part_good", 2), gt("circuit_plate_copper", 1), own("coil_t1", 2), own("capacitor_t1", 8), own("resistance_t1", 8)},
                new ItemStack[] {gt("circuit_board_good", 1)},
                new FluidStack[] {field("Glue", 72)},
                NO_FLUIDS);
        // Circuits.java:108
        add("Circuits.java:108", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 120L,
                new ItemStack[] {gt("circuit_part_advanced", 4), gt("circuit_plate_gold", 1), own("coil_t2", 2), own("capacitor_t2", 6), own("resistance_t2", 12)},
                new ItemStack[] {gt("circuit_board_advanced", 1)},
                new FluidStack[] {field("GenLiquid_MoltenBakelite", 72)},
                NO_FLUIDS);
        // Circuits.java:109
        add("Circuits.java:109", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 384L, 120L,
                new ItemStack[] {gt("circuit_part_elite", 4), gt("circuit_plate_gold", 1), own("coil_t2", 4), own("capacitor_t2", 8), own("resistance_t2", 20), own("diode_t1", 2)},
                new ItemStack[] {gt("circuit_board_elite", 1)},
                new FluidStack[] {field("GenLiquid_MoltenBakelite", 72)},
                NO_FLUIDS);
        // Circuits.java:110
        add("Circuits.java:110", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 874L, 120L,
                new ItemStack[] {gt("circuit_part_master", 6), gt("circuit_plate_platinum", 1), own("coil_t3", 8), own("capacitor_t3", 24), own("resistance_t3", 64), own("diode_t2", 8)},
                new ItemStack[] {gt("circuit_board_master", 1)},
                new FluidStack[] {field("GenLiquid_MoltenTeflon", 144)},
                NO_FLUIDS);
        // CompactItem.java:159
        add("CompactItem.java:159", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 240L, 64L,
                new ItemStack[] {own("piezoelectric_ceramic_plate", 1), form("wireFine", "Silver", 1)},
                new ItemStack[] {own("ultrasonic_generator", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 15)},
                NO_FLUIDS);
        // ComputerBuilding.java:403
        add("ComputerBuilding.java:403", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 90L, 280L,
                new ItemStack[] {own("cpu_board_t1", 1), own("cpu_die_tf3386", 1)},
                new ItemStack[] {own("cputf3386", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:404
        add("ComputerBuilding.java:404", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 280L,
                new ItemStack[] {own("cpu_board_t1", 1), own("cpu_die_tf3386_s", 1)},
                new ItemStack[] {own("cputf3386_s", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:405
        add("ComputerBuilding.java:405", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 200L, 280L,
                new ItemStack[] {own("cpu_board_t1", 1), own("cpu_die_tf3586", 1)},
                new ItemStack[] {own("cputf3586", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:406
        add("ComputerBuilding.java:406", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 240L, 280L,
                new ItemStack[] {own("cpu_board_t1", 1), own("cpu_die_tf3586_s", 1)},
                new ItemStack[] {own("cputf3586_s", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:407
        add("ComputerBuilding.java:407", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 260L, 280L,
                new ItemStack[] {own("cpu_board_t2", 1), own("cpu_die_gt1000", 1)},
                new ItemStack[] {own("cpugt1000", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:408
        add("ComputerBuilding.java:408", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 280L, 280L,
                new ItemStack[] {own("cpu_board_t2", 1), own("cpu_die_gt1090", 1)},
                new ItemStack[] {own("cpugt1090", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:409
        add("ComputerBuilding.java:409", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 360L, 280L,
                new ItemStack[] {own("cpu_board_t2", 1), own("cpu_die_gt2000", 1)},
                new ItemStack[] {own("cpugt2000", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:410
        add("ComputerBuilding.java:410", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 420L, 280L,
                new ItemStack[] {own("cpu_board_t2", 1), own("cpu_die_gt2090", 1)},
                new ItemStack[] {own("cpugt2090", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:412
        add("ComputerBuilding.java:412", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 420L, 3200L,
                new ItemStack[] {own("cpu_board_t3", 1), own("cpu_die_gt3660", 1)},
                new ItemStack[] {own("cpugt3660", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:413
        add("ComputerBuilding.java:413", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 520L, 3700L,
                new ItemStack[] {own("cpu_board_t3", 1), own("cpu_die_gt3660_v2", 1)},
                new ItemStack[] {own("cpugt3660_v2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:414
        add("ComputerBuilding.java:414", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 600L, 4500L,
                new ItemStack[] {own("cpu_board_t3", 1), own("cpu_die_gt3660_v3", 1)},
                new ItemStack[] {own("cpugt3660_v3", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:415
        add("ComputerBuilding.java:415", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 420L, 7200L,
                new ItemStack[] {own("cpu_board_t3", 1), own("cpu_die_gt3660_v4", 1), own("inter_layer_t1", 1)},
                new ItemStack[] {own("cpugt3660_v4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:417
        add("ComputerBuilding.java:417", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 460L, 3600L,
                new ItemStack[] {circuit(0, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680", 1)},
                new ItemStack[] {own("cpugt3680", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:418
        add("ComputerBuilding.java:418", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 500L, 7800L,
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680", 2)},
                new ItemStack[] {own("cpugt3699", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:419
        add("ComputerBuilding.java:419", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 500L, 4000L,
                new ItemStack[] {circuit(0, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v2", 1)},
                new ItemStack[] {own("cpugt3680_v2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:420
        add("ComputerBuilding.java:420", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 580L, 8200L,
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v2", 2)},
                new ItemStack[] {own("cpugt3699_v2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:421
        add("ComputerBuilding.java:421", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 560L, 5200L,
                new ItemStack[] {circuit(0, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v3", 1)},
                new ItemStack[] {own("cpugt3680_v3", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // ComputerBuilding.java:422
        add("ComputerBuilding.java:422", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 620L, 10800L,
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v3", 2)},
                new ItemStack[] {own("cpugt3699_v3", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:423
        add("ComputerBuilding.java:423", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 700L, 7800L,
                new ItemStack[] {circuit(0, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v4", 1), own("inter_layer_t1", 1)},
                new ItemStack[] {own("cpugt3680_v4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:424
        add("ComputerBuilding.java:424", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 780L, 16000L,
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v4", 2), own("inter_layer_t1", 1)},
                new ItemStack[] {own("cpugt3699_v4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:426
        add("ComputerBuilding.java:426", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 700L, 12000L,
                new ItemStack[] {circuit(10, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v3", 1), own("inter_layer_t1", 1)},
                new ItemStack[] {own("cpugt3680_v3_e", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:427
        add("ComputerBuilding.java:427", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 780L, 13000L,
                new ItemStack[] {circuit(11, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v3", 2), own("inter_layer_t1", 1)},
                new ItemStack[] {own("cpugt3699_v3_e", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:428
        add("ComputerBuilding.java:428", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 700L, 18000L,
                new ItemStack[] {circuit(10, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v4", 1), own("inter_layer_t2", 1)},
                new ItemStack[] {own("cpugt3680_v4_e", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:429
        add("ComputerBuilding.java:429", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 780L, 20000L,
                new ItemStack[] {circuit(11, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v4", 2), own("inter_layer_t2", 1)},
                new ItemStack[] {own("cpugt3699_v4_e", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:432
        add("ComputerBuilding.java:432", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 42L, 80L,
                new ItemStack[] {circuit(0, 1), own("ram_board_t1", 1), own("ram_die2_k", 4)},
                new ItemStack[] {own("ram_bar2_k4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:433
        add("ComputerBuilding.java:433", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 50L, 300L,
                new ItemStack[] {circuit(1, 1), own("ram_board_t1", 1), own("ram_die2_k", 8)},
                new ItemStack[] {own("ram_bar2_k8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:435
        add("ComputerBuilding.java:435", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 80L,
                new ItemStack[] {circuit(0, 1), own("ram_board_t1", 1), own("ram_die32_k", 4)},
                new ItemStack[] {own("ram_bar32_k4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:436
        add("ComputerBuilding.java:436", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 150L, 300L,
                new ItemStack[] {circuit(1, 1), own("ram_board_t1", 1), own("ram_die32_k", 8)},
                new ItemStack[] {own("ram_bar32_k8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:438
        add("ComputerBuilding.java:438", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 180L, 80L,
                new ItemStack[] {circuit(0, 1), own("ram_board_t2", 1), own("ram_die256_k", 4)},
                new ItemStack[] {own("ram_bar256_k4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:439
        add("ComputerBuilding.java:439", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 220L, 300L,
                new ItemStack[] {circuit(1, 1), own("ram_board_t2", 1), own("ram_die256_k", 8)},
                new ItemStack[] {own("ram_bar256_k8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:440
        add("ComputerBuilding.java:440", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 220L, 900L,
                new ItemStack[] {circuit(2, 1), own("ram_board_t2", 1), own("ram_die256_k", 16)},
                new ItemStack[] {own("ram_bar256_k16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:442
        add("ComputerBuilding.java:442", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 260L, 80L,
                new ItemStack[] {circuit(0, 1), own("ram_board_t2", 1), own("ram_die2_m", 4)},
                new ItemStack[] {own("ram_bar2_m4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:443
        add("ComputerBuilding.java:443", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 280L, 300L,
                new ItemStack[] {circuit(1, 1), own("ram_board_t2", 1), own("ram_die2_m", 8)},
                new ItemStack[] {own("ram_bar2_m8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:444
        add("ComputerBuilding.java:444", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 280L, 900L,
                new ItemStack[] {circuit(2, 1), own("ram_board_t2", 1), own("ram_die2_m", 16)},
                new ItemStack[] {own("ram_bar2_m16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:446
        add("ComputerBuilding.java:446", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 500L, 80L,
                new ItemStack[] {circuit(0, 1), own("ram_board_t2", 1), own("ram_die16_m", 4)},
                new ItemStack[] {own("ram_bar16_m4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:447
        add("ComputerBuilding.java:447", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 540L, 300L,
                new ItemStack[] {circuit(1, 1), own("ram_board_t2", 1), own("ram_die16_m", 8)},
                new ItemStack[] {own("ram_bar16_m8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:448
        add("ComputerBuilding.java:448", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 580L, 900L,
                new ItemStack[] {circuit(2, 1), own("ram_board_t2", 1), own("ram_die16_m", 16)},
                new ItemStack[] {own("ram_bar16_m16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:449
        add("ComputerBuilding.java:449", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 560L, 3200L,
                new ItemStack[] {circuit(3, 1), own("ram_board_t2", 1), own("ram_die16_m", 32)},
                new ItemStack[] {own("ram_bar16_m32", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // ComputerBuilding.java:451
        add("ComputerBuilding.java:451", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 900L, 80L,
                new ItemStack[] {circuit(0, 1), own("ram_board_t2", 1), own("ram_die128_m", 4)},
                new ItemStack[] {own("ram_bar128_m4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:452
        add("ComputerBuilding.java:452", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 950L, 300L,
                new ItemStack[] {circuit(1, 1), own("ram_board_t2", 1), own("ram_die128_m", 8)},
                new ItemStack[] {own("ram_bar128_m8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:453
        add("ComputerBuilding.java:453", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 990L, 900L,
                new ItemStack[] {circuit(2, 1), own("ram_board_t2", 1), own("ram_die128_m", 16)},
                new ItemStack[] {own("ram_bar128_m16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:454
        add("ComputerBuilding.java:454", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 970L, 3200L,
                new ItemStack[] {circuit(3, 1), own("ram_board_t2", 1), own("ram_die128_m", 32)},
                new ItemStack[] {own("ram_bar128_m32", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // ComputerBuilding.java:456
        add("ComputerBuilding.java:456", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 2000L, 80L,
                new ItemStack[] {circuit(0, 1), own("ram_board_t3", 1), own("ram_die768_m", 4)},
                new ItemStack[] {own("ram_bar768_m4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:457
        add("ComputerBuilding.java:457", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 2300L, 300L,
                new ItemStack[] {circuit(1, 1), own("ram_board_t3", 1), own("ram_die768_m", 8)},
                new ItemStack[] {own("ram_bar768_m8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:458
        add("ComputerBuilding.java:458", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 2700L, 900L,
                new ItemStack[] {circuit(2, 1), own("ram_board_t3", 1), own("ram_die768_m", 16)},
                new ItemStack[] {own("ram_bar768_m16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:459
        add("ComputerBuilding.java:459", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 2500L, 3200L,
                new ItemStack[] {circuit(3, 1), own("ram_board_t3", 1), own("ram_die768_m", 32)},
                new ItemStack[] {own("ram_bar768_m32", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // ComputerBuilding.java:461
        add("ComputerBuilding.java:461", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4000L, 80L,
                new ItemStack[] {circuit(0, 1), own("ram_board_t3", 1), own("ram_die2_g", 4)},
                new ItemStack[] {own("ram_bar2_g4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:462
        add("ComputerBuilding.java:462", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4400L, 300L,
                new ItemStack[] {circuit(1, 1), own("ram_board_t3", 1), own("ram_die2_g", 8)},
                new ItemStack[] {own("ram_bar2_g8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:463
        add("ComputerBuilding.java:463", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4900L, 900L,
                new ItemStack[] {circuit(2, 1), own("ram_board_t3", 1), own("ram_die2_g", 16)},
                new ItemStack[] {own("ram_bar2_g16", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:464
        add("ComputerBuilding.java:464", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 5100L, 3200L,
                new ItemStack[] {circuit(3, 1), own("ram_board_t3", 1), own("ram_die2_g", 32)},
                new ItemStack[] {own("ram_bar2_g32", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // Fusion.java:89
        add("Fusion.java:89", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 600L,
                new ItemStack[] {own("metastable_tantalum", 2), form("wireFine", "Platinum", 16), own("computer_ultimate_circuits", 1)},
                new ItemStack[] {own("quantum_control_element", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Fusion.java:94
        add("Fusion.java:94", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32768L, 1200L,
                new ItemStack[] {own("superheavy_nuclide_precursor", 1), own("nuclear_target_substrate", 1), own("metastable_tantalum", 1)},
                new ItemStack[] {own("naquadria_precursor", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:107
        add("Fusion.java:107", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 2048L, 200L,
                new ItemStack[] {form("dustAnalyticalPure", "Titanium", 2), form("dustAnalyticalPure", "Deuterium", 1), form("dustAnalyticalPure", "Tritium", 1)},
                new ItemStack[] {own("laser_target_dt", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Fusion.java:112
        add("Fusion.java:112", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 2048L, 200L,
                new ItemStack[] {form("dustAnalyticalPure", "Lithium", 2), form("dustAnalyticalPure", "Deuterium", 1)},
                new ItemStack[] {own("laser_target_li6", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Fusion.java:116
        add("Fusion.java:116", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 300L,
                new ItemStack[] {form("dustAnalyticalPure", "Lead", 2), form("dustAnalyticalPure", "Deuterium", 1)},
                new ItemStack[] {own("laser_target_lead", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Fusion.java:120
        add("Fusion.java:120", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 300L,
                new ItemStack[] {form("dustAnalyticalPure", "Tantalum", 2), form("dustAnalyticalPure", "Deuterium", 1)},
                new ItemStack[] {own("laser_target_tantalum", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Fusion.java:124
        add("Fusion.java:124", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 300L,
                new ItemStack[] {form("dustAnalyticalPure", "Carbon", 4), form("dustAnalyticalPure", "Deuterium", 1)},
                new ItemStack[] {own("laser_target_graphite", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Fusion.java:128
        add("Fusion.java:128", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 600L,
                new ItemStack[] {form("dustAnalyticalPure", "Bismuth", 2), form("dustAnalyticalPure", "Deuterium", 2)},
                new ItemStack[] {own("laser_target_bismuth", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Fusion.java:133
        add("Fusion.java:133", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 600L,
                new ItemStack[] {form("plateDense", "Tungstensteel", 4), form("plate", "Glass", 8), form("wireFine", "Platinum", 16)},
                new ItemStack[] {block("laser_focusing_array", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Fusion.java:135
        add("Fusion.java:135", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 600L,
                new ItemStack[] {gt("casing_machine_titanium", 2), gt("pipe_small_titanium", 4), form("plate", "Tantalum", 2)},
                new ItemStack[] {block("laser_target_injector", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Fusion.java:137
        add("Fusion.java:137", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32768L, 1200L,
                new ItemStack[] {gt("casing_machine_osmiumelemental", 2), form("plateDense", "OsmiumElemental", 2), form("wireFine", "Platinum", 32)},
                new ItemStack[] {block("laser_pulse_storage", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:139
        add("Fusion.java:139", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 600L,
                new ItemStack[] {form("plateDense", "Lead", 4), form("plate", "Tungstensteel", 2), form("foil", "Lithium", 8)},
                new ItemStack[] {block("laser_product_collector", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Fusion.java:148
        add("Fusion.java:148", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16384L, 1200L,
                new ItemStack[] {form("plateDense", "Titanium", 4), gt("wire_04_copper", 8), form("plate", "StainlessSteel", 2), gt("circuit_elite", 1)},
                new ItemStack[] {block("tokamak_magnetic_conductor_exp", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // Fusion.java:150
        add("Fusion.java:150", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32768L, 1600L,
                new ItemStack[] {gt("wire_08_copper", 16), form("plateDense", "StainlessSteel", 4), form("plate", "PEEK", 2), gt("circuit_elite", 1)},
                new ItemStack[] {block("tokamak_magnetic_coil_exp", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:152
        add("Fusion.java:152", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32768L, 1600L,
                new ItemStack[] {form("plateDense", "StainlessSteel", 4), form("plate", "Titanium", 4), form("plate", "PEEK", 2), gt("circuit_elite", 1)},
                new ItemStack[] {block("tokamak_magnetic_wall_exp", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:157
        add("Fusion.java:157", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 65536L, 1800L,
                new ItemStack[] {form("plateDense", "Tungstensteel", 4), gt("wire_04_graphene", 8), form("plate", "PEEK", 2), gt("circuit_master", 1)},
                new ItemStack[] {block("tokamak_magnetic_conductor_t1", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // Fusion.java:159
        add("Fusion.java:159", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 131072L, 2400L,
                new ItemStack[] {gt("wire_08_graphene", 16), form("plateDense", "Tungstensteel", 4), form("plate", "PEEK", 4), gt("circuit_master", 1)},
                new ItemStack[] {block("tokamak_magnetic_coil_t1", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 9)},
                NO_FLUIDS);
        // Fusion.java:161
        add("Fusion.java:161", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 131072L, 2400L,
                new ItemStack[] {form("plateDense", "Tungstensteel", 4), form("plate", "Trinaquadalloy", 4), form("plate", "PEEK", 4), gt("circuit_master", 1)},
                new ItemStack[] {block("tokamak_magnetic_wall_t1", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 9)},
                NO_FLUIDS);
        // QuantumEntry.java:57
        add("QuantumEntry.java:57", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Platinum", 4), form("foil", "MirrorGold", 4), form("wireFine", "Naquadah", 16), gt("circuit_elite", 1)},
                new ItemStack[] {block("quantum_stabilization_substrate", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // QuantumEntry.java:73
        add("QuantumEntry.java:73", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 2400L,
                new ItemStack[] {form("plate", "MirrorGold", 1), form("dustAbsolutelyPure", "intellite", 4), gt("compact_signal_emitter_luv", 1), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("entanglement_channel_module", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // QuantumEntry.java:79
        add("QuantumEntry.java:79", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 1600L,
                new ItemStack[] {form("foil", "MirrorGold", 2), form("dustAnalyticalPure", "intellite", 2), form("wireFine", "Naquadah", 16), gt("circuit_elite", 1)},
                new ItemStack[] {block("quantum_readout_coil", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ResearchRecipes.java:140
        add("ResearchRecipes.java:140", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 128L,
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_carbondioxide", 1)},
                new FluidStack[] {field("CarbonDioxide", 1000)},
                NO_FLUIDS);
        // tileEntityInit0.java:213
        add("tileEntityInit0.java:213", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 4), form("plateDense", "ElectricalSteel", 1), gt("wire_01_tin", 4), gt("cable_01_tin", 2), gt("circuit_basic", 1)},
                new ItemStack[] {block("transformer_small_lv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:214
        add("tileEntityInit0.java:214", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 4), form("plateDense", "ElectricalSteel", 1), gt("wire_01_copper", 8), gt("cable_01_copper", 2), gt("circuit_good", 1)},
                new ItemStack[] {block("transformer_small_mv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:215
        add("tileEntityInit0.java:215", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 4), form("plateDense", "ElectricalSteel", 1), gt("wire_01_gold", 16), gt("cable_01_gold", 2), gt("circuit_advanced", 1)},
                new ItemStack[] {block("transformer_small_hv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:216
        add("tileEntityInit0.java:216", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 4), form("plateDense", "ElectricalSteel", 1), gt("wire_01_aluminium", 32), gt("cable_01_aluminium", 2), gt("circuit_elite", 1)},
                new ItemStack[] {block("transformer_small_ev", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:217
        add("tileEntityInit0.java:217", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 4), form("plateDense", "ElectricalSteel", 2), gt("wire_01_platinum", 64), gt("cable_01_platinum", 2), gt("circuit_master", 1)},
                new ItemStack[] {block("transformer_small_iv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:218
        add("tileEntityInit0.java:218", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 4), form("plateDense", "ElectricalSteel", 2), gt("wire_01_graphene", 64), gt("wire_01_graphene", 64), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("transformer_small_luv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:220
        add("tileEntityInit0.java:220", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 4), form("plateDense", "ElectricalSteel", 4), gt("wire_08_tin", 4), gt("cable_04_tin", 2), gt("circuit_basic", 2)},
                new ItemStack[] {block("transformer_large_lv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:221
        add("tileEntityInit0.java:221", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 4), form("plateDense", "ElectricalSteel", 4), gt("wire_08_copper", 8), gt("cable_04_copper", 2), gt("circuit_good", 2)},
                new ItemStack[] {block("transformer_large_mv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:222
        add("tileEntityInit0.java:222", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 4), form("plateDense", "ElectricalSteel", 4), gt("wire_08_gold", 16), gt("cable_04_gold", 2), gt("circuit_advanced", 2)},
                new ItemStack[] {block("transformer_large_hv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:223
        add("tileEntityInit0.java:223", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 4), form("plateDense", "ElectricalSteel", 4), gt("wire_08_aluminium", 32), gt("cable_04_aluminium", 2), gt("circuit_elite", 2)},
                new ItemStack[] {block("transformer_large_ev", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:224
        add("tileEntityInit0.java:224", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 4), form("plateDense", "ElectricalSteel", 8), gt("wire_08_platinum", 64), gt("cable_04_platinum", 2), gt("circuit_master", 2)},
                new ItemStack[] {block("transformer_large_iv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:225
        add("tileEntityInit0.java:225", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 4), form("plateDense", "ElectricalSteel", 8), gt("wire_08_graphene", 64), gt("wire_04_graphene", 2), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("transformer_large_luv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:227
        add("tileEntityInit0.java:227", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 4), form("gearGtSmall", "SteelGalvanized", 4), gt("circuit_basic", 1)},
                new ItemStack[] {block("flywheel_gear_small_t1", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:228
        add("tileEntityInit0.java:228", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 4), form("gearGtSmall", "Aluminium", 4), gt("circuit_good", 1)},
                new ItemStack[] {block("flywheel_gear_small_t2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:229
        add("tileEntityInit0.java:229", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 4), form("gearGtSmall", "StainlessSteel", 4), gt("circuit_advanced", 1)},
                new ItemStack[] {block("flywheel_gear_small_t3", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:230
        add("tileEntityInit0.java:230", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 4), form("gearGtSmall", "Chromium", 4), gt("circuit_elite", 1)},
                new ItemStack[] {block("flywheel_gear_small_t4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:231
        add("tileEntityInit0.java:231", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 4), form("gearGtSmall", "Titanium", 4), gt("circuit_master", 1)},
                new ItemStack[] {block("flywheel_gear_small_t5", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:232
        add("tileEntityInit0.java:232", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 4), form("gearGtSmall", "Iridium", 4), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("flywheel_gear_small_t6", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:234
        add("tileEntityInit0.java:234", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 16), form("gearGt", "SteelGalvanized", 4), gt("circuit_basic", 2)},
                new ItemStack[] {block("flywheel_gear_large_t1", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:235
        add("tileEntityInit0.java:235", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 16), form("gearGt", "Aluminium", 4), gt("circuit_good", 2)},
                new ItemStack[] {block("flywheel_gear_large_t2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:236
        add("tileEntityInit0.java:236", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 16), form("gearGt", "StainlessSteel", 4), gt("circuit_advanced", 2)},
                new ItemStack[] {block("flywheel_gear_large_t3", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:237
        add("tileEntityInit0.java:237", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 16), form("gearGt", "Chromium", 4), gt("circuit_elite", 2)},
                new ItemStack[] {block("flywheel_gear_large_t4", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:238
        add("tileEntityInit0.java:238", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 16), form("gearGt", "Titanium", 4), gt("circuit_master", 2)},
                new ItemStack[] {block("flywheel_gear_large_t5", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:239
        add("tileEntityInit0.java:239", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 16), form("gearGt", "Iridium", 4), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("flywheel_gear_large_t6", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:241
        add("tileEntityInit0.java:241", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 4), gt("compact_force_field_emitter_lv", 2), gt("wire_01_tin", 8), gt("cable_01_tin", 2), gt("circuit_basic", 1)},
                new ItemStack[] {block("light_transformer_small_lv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:242
        add("tileEntityInit0.java:242", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 4), gt("compact_force_field_emitter_mv", 2), gt("wire_01_copper", 8), gt("cable_01_copper", 2), gt("circuit_good", 1)},
                new ItemStack[] {block("light_transformer_small_mv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:243
        add("tileEntityInit0.java:243", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 4), gt("compact_force_field_emitter_hv", 2), gt("wire_01_gold", 8), gt("cable_01_gold", 2), gt("circuit_advanced", 1)},
                new ItemStack[] {block("light_transformer_small_hv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:244
        add("tileEntityInit0.java:244", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 4), gt("compact_force_field_emitter_ev", 2), gt("wire_01_aluminium", 8), gt("cable_01_aluminium", 2), gt("circuit_elite", 1)},
                new ItemStack[] {block("light_transformer_small_ev", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:245
        add("tileEntityInit0.java:245", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 4), gt("compact_force_field_emitter_iv", 2), gt("wire_01_platinum", 8), gt("cable_01_platinum", 2), gt("circuit_master", 1)},
                new ItemStack[] {block("light_transformer_small_iv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:246
        add("tileEntityInit0.java:246", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 4), gt("compact_force_field_emitter_luv", 2), gt("wire_01_graphene", 8), gt("wire_01_graphene", 2), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("light_transformer_small_luv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:248
        add("tileEntityInit0.java:248", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_steelgalvanized", 1), form("stickLong", "SteelGalvanized", 16), gt("compact_force_field_emitter_lv", 8), gt("wire_08_tin", 8), gt("cable_04_tin", 2), gt("circuit_basic", 2)},
                new ItemStack[] {block("light_transformer_large_lv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:249
        add("tileEntityInit0.java:249", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("stickLong", "Aluminium", 16), gt("compact_force_field_emitter_mv", 8), gt("wire_08_copper", 8), gt("cable_04_copper", 2), gt("circuit_good", 2)},
                new ItemStack[] {block("light_transformer_large_mv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:250
        add("tileEntityInit0.java:250", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("stickLong", "StainlessSteel", 16), gt("compact_force_field_emitter_hv", 8), gt("wire_08_gold", 8), gt("cable_04_gold", 2), gt("circuit_advanced", 2)},
                new ItemStack[] {block("light_transformer_large_hv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:251
        add("tileEntityInit0.java:251", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_chromium", 1), form("stickLong", "Chromium", 16), gt("compact_force_field_emitter_ev", 8), gt("wire_08_aluminium", 8), gt("cable_04_aluminium", 2), gt("circuit_elite", 2)},
                new ItemStack[] {block("light_transformer_large_ev", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:252
        add("tileEntityInit0.java:252", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_titanium", 1), form("stickLong", "Titanium", 16), gt("compact_force_field_emitter_iv", 8), gt("wire_08_platinum", 8), gt("cable_04_platinum", 2), gt("circuit_master", 2)},
                new ItemStack[] {block("light_transformer_large_iv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:253
        add("tileEntityInit0.java:253", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_iridium", 1), form("stickLong", "Iridium", 16), gt("compact_force_field_emitter_luv", 8), gt("wire_08_graphene", 8), gt("wire_04_graphene", 2), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("light_transformer_large_luv", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 288)},
                NO_FLUIDS);
        // tileEntityInit0.java:462
        add("tileEntityInit0.java:462", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 400L, 800L,
                new ItemStack[] {gt("casing_machine_dense_stainlesssteel", 1), form("rotor", "StainlessSteel", 16), gt("axle_steel_4", 1), gt("circuit_good", 2)},
                new ItemStack[] {block("liquid_battery_stainless", 1)},
                new FluidStack[] {field("GenMolten_StainlessSteel", 576)},
                NO_FLUIDS);
        // tileEntityInit0.java:463
        add("tileEntityInit0.java:463", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 800L, 800L,
                new ItemStack[] {gt("casing_machine_dense_titanium", 1), form("rotor", "Titanium", 16), gt("axle_titanium_4", 1), gt("circuit_advanced", 2)},
                new ItemStack[] {block("liquid_battery_titanium", 1)},
                new FluidStack[] {field("GenMolten_Titanium", 576)},
                NO_FLUIDS);
        // tileEntityInit0.java:464
        add("tileEntityInit0.java:464", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 1400L, 800L,
                new ItemStack[] {gt("casing_machine_dense_tungstensteel", 1), form("rotor", "Tungstensteel", 16), gt("axle_tungstensteel_4", 1), gt("circuit_elite", 2)},
                new ItemStack[] {block("liquid_battery_tungstensteel", 1)},
                new FluidStack[] {field("GenMolten_Tungstensteel", 576)},
                NO_FLUIDS);
        // tileEntityInit0.java:536
        add("tileEntityInit0.java:536", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32768L, 4800L,
                new ItemStack[] {form("plateDouble", "Tungstensteel", 4), form("plateDouble", "Titanium", 4), form("dustAnalyticalPure", "Lead", 8), form("dustAnalyticalPure", "Tungsten", 4), form("dustAnalyticalPure", "Titanium", 4), gt("compact_sensor_hv", 1), gt("compact_signal_emitter_hv", 1), gt("circuit_elite", 2)},
                new ItemStack[] {block("centrifugal_microgravity", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:537
        add("tileEntityInit0.java:537", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32768L, 4800L,
                new ItemStack[] {gt("casing_machine_titanium", 1), form("plateDouble", "Titanium", 4), form("dustAnalyticalPure", "Molybdenum", 8), gt("circuit_elite", 2)},
                new ItemStack[] {block("microgravity_crystal_growth", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:555
        add("tileEntityInit0.java:555", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 200L,
                new ItemStack[] {form("dust", "PotassiumSodium", 5), gt("casing_machine_double_silver", 1), form("dust", "Carbon", 1), form("plateCurved", "Silver", 9)},
                new ItemStack[] {block("sunlight_absorber", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:556
        add("tileEntityInit0.java:556", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 200L,
                new ItemStack[] {form("dust", "PotassiumSodium", 5), gt("casing_machine_silver", 1), form("plateCurved", "Silver", 12), circuit(0, 1)},
                new ItemStack[] {block("heat_pipe", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:557
        add("tileEntityInit0.java:557", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16L, 200L,
                new ItemStack[] {form("dust", "PotassiumSodium", 5), gt("casing_machine_silver", 1), form("plateCurved", "Silver", 6), form("plateDense", "Silver", 1)},
                new ItemStack[] {block("sun_heater_top_layer", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:598
        add("tileEntityInit0.java:598", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 200L, 800L,
                new ItemStack[] {circuit(7, 1), form("ingotDouble", "VanadiumSteel", 6)},
                new ItemStack[] {block("enhanced_frame", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // tileEntityInit0.java:599
        add("tileEntityInit0.java:599", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 200L, 800L,
                new ItemStack[] {circuit(7, 1), form("plateDouble", "StainlessSteel", 4)},
                new ItemStack[] {block("chemical_reactor_pipe", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // tileEntityInit0.java:601
        add("tileEntityInit0.java:601", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 200L, 800L,
                new ItemStack[] {circuit(0, 1), form("plateDense", "ElectricalSteel", 4)},
                new ItemStack[] {block("magnetic_conductor", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // tileEntityInit0.java:658
        add("tileEntityInit0.java:658", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 1200L,
                new ItemStack[] {form("plateDouble", "Tungstensteel", 4), form("plate", "PEEK", 1), form("wireFine", "Graphene", 2), form("dustAnalyticalPure", "Silicon", 1), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("ai_compute_frame", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:659
        add("tileEntityInit0.java:659", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16384L, 1800L,
                new ItemStack[] {own("ram_bar2_g32", 2), form("dustAnalyticalPure", "Silicon", 1), form("wireFine", "Graphene", 2), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("ai_memory_bank", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:660
        add("tileEntityInit0.java:660", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16384L, 1800L,
                new ItemStack[] {own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "Platinum", 1), form("wireFine", "Graphene", 4), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("ai_routing_matrix", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:661
        add("tileEntityInit0.java:661", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16384L, 1800L,
                new ItemStack[] {own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "Carbon", 1), own("ram_bar2_g32", 2), form("foil", "PEEK", 2)},
                new ItemStack[] {block("ai_packing_planner", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:662
        add("tileEntityInit0.java:662", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 16384L, 1800L,
                new ItemStack[] {own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "intellite", 1), gt("compact_signal_emitter_luv", 1), form("wireFine", "Graphene", 4), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("ai_recognition_matrix", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:673
        add("tileEntityInit0.java:673", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 1200L,
                new ItemStack[] {gt("casing_machine_double_tungstensteel", 1), form("dustAnalyticalPure", "Copper", 8), form("dustAnalyticalPure", "Silver", 4), form("plate", "PEEK", 4), form("wireFine", "Graphene", 16), gt("circuit_master", 1)},
                new ItemStack[] {block("wireless_receiver_inductive", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // tileEntityInit0.java:675
        add("tileEntityInit0.java:675", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32768L, 2400L,
                new ItemStack[] {gt("casing_machine_double_naquadria", 1), form("plate", "MirrorGold", 2), form("dustAbsolutelyPure", "intellite", 4), form("dustAbsolutelyPure", "Naquadria", 2), block("entanglement_channel_module", 2), block("quantum_readout_coil", 1), gt("compact_signal_emitter_luv", 2), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("wireless_receiver_quantum", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:687
        add("tileEntityInit0.java:687", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 1600L,
                new ItemStack[] {form("plateDouble", "Lead", 4), form("plateDouble", "Tungstensteel", 2), form("dustCommercialPure", "Lead", 1)},
                new ItemStack[] {block("high_shield_laminated_wall", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // tileEntityInit0.java:688
        add("tileEntityInit0.java:688", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 2400L,
                new ItemStack[] {form("plateDouble", "Titanium", 4), form("dustAnalyticalPure", "Titanium", 2), gt("compact_electric_motor_hv", 1)},
                new ItemStack[] {block("counter_rotating_mass_ring", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // tileEntityInit0.java:689
        add("tileEntityInit0.java:689", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 12288L, 3200L,
                new ItemStack[] {form("plateDouble", "Tungstensteel", 4), form("dustAnalyticalPure", "Tungsten", 2), gt("compact_signal_emitter_hv", 1)},
                new ItemStack[] {block("gyro_stabilization_anchor", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 24)},
                NO_FLUIDS);
        // tileEntityInit0.java:690
        add("tileEntityInit0.java:690", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 4096L, 1600L,
                new ItemStack[] {form("plateDouble", "Titanium", 4), gt("compact_electric_motor_hv", 1)},
                new ItemStack[] {block("sealed_access_hatch", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:691
        add("tileEntityInit0.java:691", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 2000L,
                new ItemStack[] {form("plateDouble", "StainlessSteel", 4), gt("wire_04_tungsten", 4), form("dustAnalyticalPure", "Tungsten", 1)},
                new ItemStack[] {block("room_energy_port", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:711
        add("tileEntityInit0.java:711", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32L, 400L,
                new ItemStack[] {gt("casing_machine_aluminium", 1), form("plate", "ElectricalSteel", 4), gt("compact_force_field_emitter_mv", 1), gt("circuit_good", 2)},
                new ItemStack[] {block("wireless_compute_node_aluminium", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:712
        add("tileEntityInit0.java:712", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_stainlesssteel", 1), form("plate", "ElectricalSteel", 4), gt("compact_force_field_emitter_hv", 1), gt("circuit_advanced", 2)},
                new ItemStack[] {block("wireless_compute_node_stainless", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:713
        add("tileEntityInit0.java:713", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 128L, 400L,
                new ItemStack[] {gt("casing_machine_chromium", 1), form("plate", "ElectricalSteel", 4), gt("compact_force_field_emitter_ev", 1), gt("circuit_elite", 2)},
                new ItemStack[] {block("wireless_compute_node_chromium", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:714
        add("tileEntityInit0.java:714", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 512L, 400L,
                new ItemStack[] {gt("casing_machine_titanium", 1), form("plate", "ElectricalSteel", 8), gt("compact_force_field_emitter_iv", 1), gt("circuit_master", 2)},
                new ItemStack[] {block("wireless_compute_node_titanium", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // tileEntityInit0.java:718
        add("tileEntityInit0.java:718", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 8192L, 1200L,
                new ItemStack[] {form("plate", "Naquadah", 1), block("quantum_coherence_casing", 1), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("quantum_compute_node_naquadah", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // tileEntityInit0.java:720
        add("tileEntityInit0.java:720", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 32768L, 2400L,
                new ItemStack[] {form("plate", "Naquadria", 1), block("quantum_cryogenic_manifold", 1), block("quantum_gold_drive_module", 1), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("quantum_compute_node_naquadria", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:722
        add("tileEntityInit0.java:722", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 131072L, 3600L,
                new ItemStack[] {form("plate", "Naquadria", 1), block("quantum_phase_isolation_frame", 1), block("quantum_gold_lattice_stabilizer", 1), gt("circuit_ultimate", 4)},
                new ItemStack[] {block("quantum_compute_node_enriched", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 9)},
                NO_FLUIDS);
        // tileEntityInit0.java:724
        add("tileEntityInit0.java:724", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 524288L, 4800L,
                new ItemStack[] {form("plate", "Trinaquadalloy", 1), block("deep_coherence_containment_wall", 1), block("quantum_gold_lattice_stabilizer", 1), gt("circuit_ultimate", 8)},
                new ItemStack[] {block("quantum_compute_node_trinaquadalloy", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 4)},
                NO_FLUIDS);
    }

    private static void laserCutter() {
        // Chemistry.java:54
        add("Chemistry.java:54", Gt6KtfruMachines.LASER_CUTTER, 300L, 480L,
                new ItemStack[] {form("plate", "Nickel", 8)},
                new ItemStack[] {own("ktfru.item.battery.pole.nickel", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Chemistry.java:56
        add("Chemistry.java:56", Gt6KtfruMachines.LASER_CUTTER, 300L, 480L,
                new ItemStack[] {form("plate", "Platinum", 8)},
                new ItemStack[] {own("ktfru.item.battery.pole.platinum", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Chemistry.java:133
        add("Chemistry.java:133", Gt6KtfruMachines.LASER_CUTTER, 500L, 200L,
                new ItemStack[] {form("plate", "Graphene", 1)},
                new ItemStack[] {form("stick", "Graphene", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:56
        add("ComputerBuilding.java:56", Gt6KtfruMachines.LASER_CUTTER, 64L, 140L,
                new ItemStack[] {circuit(1, 1), gt("circuit_plate_copper", 1)},
                new ItemStack[] {own("ram_board_t1", 14)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:57
        add("ComputerBuilding.java:57", Gt6KtfruMachines.LASER_CUTTER, 64L, 400L,
                new ItemStack[] {circuit(1, 1), gt("circuit_plate_gold", 1)},
                new ItemStack[] {own("ram_board_t2", 14)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:58
        add("ComputerBuilding.java:58", Gt6KtfruMachines.LASER_CUTTER, 64L, 1000L,
                new ItemStack[] {circuit(1, 1), gt("circuit_plate_platinum", 1)},
                new ItemStack[] {own("ram_board_t3", 14)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:60
        add("ComputerBuilding.java:60", Gt6KtfruMachines.LASER_CUTTER, 64L, 140L,
                new ItemStack[] {circuit(0, 1), gt("circuit_plate_copper", 1)},
                new ItemStack[] {own("cpu_board_t1", 25)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:61
        add("ComputerBuilding.java:61", Gt6KtfruMachines.LASER_CUTTER, 64L, 400L,
                new ItemStack[] {circuit(0, 1), gt("circuit_plate_gold", 1)},
                new ItemStack[] {own("cpu_board_t2", 25)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:62
        add("ComputerBuilding.java:62", Gt6KtfruMachines.LASER_CUTTER, 64L, 1000L,
                new ItemStack[] {circuit(0, 1), gt("circuit_plate_platinum", 1)},
                new ItemStack[] {own("cpu_board_t3", 25)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:163
        add("ComputerBuilding.java:163", Gt6KtfruMachines.LASER_CUTTER, 96L, 2000L,
                new ItemStack[] {form("bouleGt", "Silicon", 1)},
                new ItemStack[] {own("silicon_plate_t1", 11)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:164
        add("ComputerBuilding.java:164", Gt6KtfruMachines.LASER_CUTTER, 96L, 6000L,
                new ItemStack[] {own("silicon_boule_pure", 1)},
                new ItemStack[] {own("silicon_plate_t2", 9)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:166
        add("ComputerBuilding.java:166", Gt6KtfruMachines.LASER_CUTTER, 2048L, 2000L,
                new ItemStack[] {own("mo_o2_boule", 1)},
                new ItemStack[] {own("mo_o2_plate_t1", 9)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:167
        add("ComputerBuilding.java:167", Gt6KtfruMachines.LASER_CUTTER, 2048L, 6000L,
                new ItemStack[] {own("mo_o2_boule_pure", 1)},
                new ItemStack[] {own("mo_o2_plate_t2", 9)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:370
        add("ComputerBuilding.java:370", Gt6KtfruMachines.LASER_CUTTER, 120L, 400L,
                new ItemStack[] {own("cpu_wafer_t1_checked", 1)},
                new ItemStack[] {own("cpu_die_tf3386", 10), own("cpu_die_tf3386_s", 7)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3500, 1200},
                null);
        // ComputerBuilding.java:371
        add("ComputerBuilding.java:371", Gt6KtfruMachines.LASER_CUTTER, 250L, 400L,
                new ItemStack[] {own("cpu_wafer_t2_checked", 1)},
                new ItemStack[] {own("cpu_die_tf3586", 12), own("cpu_die_tf3586_s", 5)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2000, 800},
                null);
        // ComputerBuilding.java:372
        add("ComputerBuilding.java:372", Gt6KtfruMachines.LASER_CUTTER, 500L, 400L,
                new ItemStack[] {own("cpu_wafer_t3_checked", 1)},
                new ItemStack[] {own("cpu_die_gt1000", 12), own("cpu_die_gt1090", 5)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2500, 900},
                null);
        // ComputerBuilding.java:373
        add("ComputerBuilding.java:373", Gt6KtfruMachines.LASER_CUTTER, 1000L, 1000L,
                new ItemStack[] {own("cpu_wafer_t4_checked", 1)},
                new ItemStack[] {own("cpu_die_gt2000", 12), own("cpu_die_gt2090", 5)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {1700, 650},
                null);
        // ComputerBuilding.java:374
        add("ComputerBuilding.java:374", Gt6KtfruMachines.LASER_CUTTER, 800L, 10000L,
                new ItemStack[] {own("cpu_wafer_t5_checked", 1)},
                new ItemStack[] {own("cpu_die_gt3660", 25), own("cpu_die_gt3680", 12)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {1400, 450},
                null);
        // ComputerBuilding.java:375
        add("ComputerBuilding.java:375", Gt6KtfruMachines.LASER_CUTTER, 800L, 24000L,
                new ItemStack[] {own("cpu_wafer_t6_checked", 1)},
                new ItemStack[] {own("cpu_die_gt3660_v2", 25), own("cpu_die_gt3680_v2", 12)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {1000, 300},
                null);
        // ComputerBuilding.java:376
        add("ComputerBuilding.java:376", Gt6KtfruMachines.LASER_CUTTER, 780L, 56000L,
                new ItemStack[] {own("cpu_wafer_t7_checked", 1)},
                new ItemStack[] {own("cpu_die_gt3660_v3", 25), own("cpu_die_gt3680_v3", 12)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {700, 150},
                null);
        // ComputerBuilding.java:377
        add("ComputerBuilding.java:377", Gt6KtfruMachines.LASER_CUTTER, 760L, 72000L,
                new ItemStack[] {own("cpu_wafer_t8_checked", 1)},
                new ItemStack[] {own("cpu_die_gt3660_v4", 25), own("cpu_die_gt3680_v4", 12)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {400, 100},
                null);
        // ComputerBuilding.java:379
        add("ComputerBuilding.java:379", Gt6KtfruMachines.LASER_CUTTER, 80L, 400L,
                new ItemStack[] {own("ram_wafer_t1_checked", 1)},
                new ItemStack[] {own("ram_die2_k", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {5000},
                null);
        // ComputerBuilding.java:380
        add("ComputerBuilding.java:380", Gt6KtfruMachines.LASER_CUTTER, 210L, 400L,
                new ItemStack[] {own("ram_wafer_t2_checked", 1)},
                new ItemStack[] {own("ram_die32_k", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3000},
                null);
        // ComputerBuilding.java:381
        add("ComputerBuilding.java:381", Gt6KtfruMachines.LASER_CUTTER, 480L, 400L,
                new ItemStack[] {own("ram_wafer_t3_checked", 1)},
                new ItemStack[] {own("ram_die256_k", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {4300},
                null);
        // ComputerBuilding.java:382
        add("ComputerBuilding.java:382", Gt6KtfruMachines.LASER_CUTTER, 940L, 1000L,
                new ItemStack[] {own("ram_wafer_t4_checked", 1)},
                new ItemStack[] {own("ram_die2_m", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3800},
                null);
        // ComputerBuilding.java:383
        add("ComputerBuilding.java:383", Gt6KtfruMachines.LASER_CUTTER, 560L, 6000L,
                new ItemStack[] {own("ram_wafer_t5_checked", 1)},
                new ItemStack[] {own("ram_die16_m", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3400},
                null);
        // ComputerBuilding.java:384
        add("ComputerBuilding.java:384", Gt6KtfruMachines.LASER_CUTTER, 540L, 7000L,
                new ItemStack[] {own("ram_wafer_t6_checked", 1)},
                new ItemStack[] {own("ram_die128_m", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3000},
                null);
        // ComputerBuilding.java:385
        add("ComputerBuilding.java:385", Gt6KtfruMachines.LASER_CUTTER, 500L, 8000L,
                new ItemStack[] {own("ram_wafer_t7_checked", 1)},
                new ItemStack[] {own("ram_die768_m", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2600},
                null);
        // ComputerBuilding.java:386
        add("ComputerBuilding.java:386", Gt6KtfruMachines.LASER_CUTTER, 430L, 9000L,
                new ItemStack[] {own("ram_wafer_t8_checked", 1)},
                new ItemStack[] {own("ram_die2_g", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2100},
                null);
        // ComputerBuilding.java:388
        add("ComputerBuilding.java:388", Gt6KtfruMachines.LASER_CUTTER, 64L, 400L,
                new ItemStack[] {own("circuit_part_wafer_t3_checked", 1)},
                new ItemStack[] {gt("circuit_part_advanced", 40), gt("circuit_part_good", 10)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {4000, 1000},
                null);
        // ComputerBuilding.java:389
        add("ComputerBuilding.java:389", Gt6KtfruMachines.LASER_CUTTER, 170L, 400L,
                new ItemStack[] {own("circuit_part_wafer_t4_checked", 1)},
                new ItemStack[] {gt("circuit_part_elite", 40), gt("circuit_part_advanced", 10)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2400, 1000},
                null);
        // ComputerBuilding.java:390
        add("ComputerBuilding.java:390", Gt6KtfruMachines.LASER_CUTTER, 440L, 400L,
                new ItemStack[] {own("circuit_part_wafer_t5_checked", 1)},
                new ItemStack[] {gt("circuit_part_master", 40), gt("circuit_part_elite", 10)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3200, 1500},
                null);
        // ComputerBuilding.java:391
        add("ComputerBuilding.java:391", Gt6KtfruMachines.LASER_CUTTER, 860L, 1000L,
                new ItemStack[] {own("circuit_part_wafer_t6_checked", 1)},
                new ItemStack[] {gt("circuit_part_ultimate", 40), gt("circuit_part_master", 10)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {2600, 1900},
                null);
        // ComputerBuilding.java:393
        add("ComputerBuilding.java:393", Gt6KtfruMachines.LASER_CUTTER, 170L, 4000L,
                new ItemStack[] {own("inter_layer_wafer_t7_checked", 1)},
                new ItemStack[] {own("inter_layer_t1", 21)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {4800},
                null);
        // ComputerBuilding.java:394
        add("ComputerBuilding.java:394", Gt6KtfruMachines.LASER_CUTTER, 170L, 5000L,
                new ItemStack[] {own("inter_layer_wafer_t8_checked", 1)},
                new ItemStack[] {own("inter_layer_t2", 21)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {4800},
                null);
        // ComputerBuilding.java:396
        add("ComputerBuilding.java:396", Gt6KtfruMachines.LASER_CUTTER, 170L, 400L,
                new ItemStack[] {own("diode_wafer_t1_checked", 1)},
                new ItemStack[] {own("diode_t2_part", 64), own("diode_t2_part", 24)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {4800, 4800},
                null);
        // ComputerBuilding.java:397
        add("ComputerBuilding.java:397", Gt6KtfruMachines.LASER_CUTTER, 440L, 400L,
                new ItemStack[] {own("diode_wafer_t2_checked", 1)},
                new ItemStack[] {own("diode_t3_part", 64), own("diode_t3_part", 24)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {3200, 3200},
                null);
    }

    private static void heatMixer() {
        // Chemistry.java:43
        add("Chemistry.java:43", Gt6KtfruMachines.HEAT_MIXER, 32L, 20L,
                new ItemStack[] {form("dust", "Carbon", 1)},
                new ItemStack[] {form("dustTiny", "Sugar", 1)},
                new FluidStack[] {field("Vinegar_Rice", 100)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 30), field("Water", 60)},
                0L,
                null,
                new int[] {0});
        // Chemistry.java:44
        add("Chemistry.java:44", Gt6KtfruMachines.HEAT_MIXER, 32L, 20L,
                new ItemStack[] {form("dust", "Carbon", 1)},
                new ItemStack[] {form("dustTiny", "Sugar", 1)},
                new FluidStack[] {field("Vinegar_Apple", 100)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 20), field("Water", 70)},
                0L,
                null,
                new int[] {0});
        // Chemistry.java:45
        add("Chemistry.java:45", Gt6KtfruMachines.HEAT_MIXER, 32L, 20L,
                new ItemStack[] {form("dust", "Carbon", 1)},
                new ItemStack[] {form("dustTiny", "Sugar", 2)},
                new FluidStack[] {field("Vinegar_Cane", 100)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 20), field("Water", 70)},
                0L,
                null,
                new int[] {0});
        // Chemistry.java:46
        add("Chemistry.java:46", Gt6KtfruMachines.HEAT_MIXER, 32L, 20L,
                new ItemStack[] {form("dust", "Carbon", 1)},
                new ItemStack[] {form("dustTiny", "Sugar", 1)},
                new FluidStack[] {field("Vinegar_Grape", 100)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 20), field("Water", 70)},
                0L,
                null,
                new int[] {0});
        // Chemistry.java:48
        add("Chemistry.java:48", Gt6KtfruMachines.HEAT_MIXER, 120L, 40L,
                new ItemStack[] {form("dust", "Sodium", 1), form("dust", "Potassium", 1)},
                new ItemStack[] {form("dustTiny", "PotassiumSodium", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Chemistry.java:58
        add("Chemistry.java:58", Gt6KtfruMachines.HEAT_MIXER, 500L, 800L,
                new ItemStack[] {form("dust", "Yttrium", 4), form("dust", "Zirconium", 4)},
                NO_ITEMS,
                new FluidStack[] {field("Oxygen", 4000)},
                new FluidStack[] {field("GenLiquid_YttriumZirconiumOxide", 1000)});
        // Chemistry.java:123
        add("Chemistry.java:123", Gt6KtfruMachines.HEAT_MIXER, 120L, 80L,
                new ItemStack[] {form("dust", "Palladium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Butynediol", 1000), field("Hydrogen", 1000)},
                new FluidStack[] {field("GenLiquid_Butanediol", 1000)},
                0L,
                null,
                new int[] {0});
        // Chemistry.java:125
        add("Chemistry.java:125", Gt6KtfruMachines.HEAT_MIXER, 120L, 80L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methylamine", 1000), field("GenLiquid_Butyrolactone", 1000)},
                new FluidStack[] {field("GenLiquid_Methylpyrrolidone", 1000), field("DistW", 1000)});
        // Chemistry.java:137
        add("Chemistry.java:137", Gt6KtfruMachines.HEAT_MIXER, 120L, 80L,
                new ItemStack[] {form("dust", "AluminumChloride", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Benzene", 200), field("GenLiquid_Chloromethane", 100)},
                new FluidStack[] {field("GenLiquid_Toluene", 200), field("GenGas_HydrochloricAcid", 1000)},
                0L,
                null,
                new int[] {0});
        // Chemistry.java:141
        add("Chemistry.java:141", Gt6KtfruMachines.HEAT_MIXER, 480L, 800L,
                new ItemStack[] {form("dust", "SodiumAcetate", 2)},
                new ItemStack[] {form("dust", "SodiumPersulfate", 1)},
                new FluidStack[] {field("GenLiquid_BariumSulfate", 2000)},
                new FluidStack[] {field("GenLiquid_BariumAcetate", 2000)});
        // Chemistry.java:143
        add("Chemistry.java:143", Gt6KtfruMachines.HEAT_MIXER, 1040L, 80L,
                new ItemStack[] {circuit(0, 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Acetone", 1000)},
                new FluidStack[] {field("GenLiquid_Ketene", 400), field("GenLiquid_Diketene", 600), field("Methane", 1000)});
        // Chemistry.java:144
        add("Chemistry.java:144", Gt6KtfruMachines.HEAT_MIXER, 200L, 200L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Diketene", 1000)},
                new FluidStack[] {field("GenLiquid_Ketene", 600), field("GenLiquid_Diketene", 400)});
        // Chemistry.java:148
        add("Chemistry.java:148", Gt6KtfruMachines.HEAT_MIXER, 440L, 200L,
                new ItemStack[] {form("dust", "PhosphorusPentoxide", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 1000)},
                new FluidStack[] {field("GenLiquid_Ketene", 900), field("GenLiquid_Diketene", 100), field("DistW", 1000)},
                0L,
                null,
                new int[] {0});
        // Chemistry.java:150
        add("Chemistry.java:150", Gt6KtfruMachines.HEAT_MIXER, 2141L, 80L,
                new ItemStack[] {form("dust", "Rutile", 2)},
                new ItemStack[] {form("dust", "TitanicSulfate", 3)},
                new FluidStack[] {field("GenLiquid_SulfuricAcid", 4000)},
                NO_FLUIDS);
        // Chemistry.java:154
        add("Chemistry.java:154", Gt6KtfruMachines.HEAT_MIXER, 450L, 80L,
                new ItemStack[] {form("dust", "SodiumHydroxide", 2)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_ImpureTitaniumAcylate", 1000)},
                new FluidStack[] {field("GenLiquid_PurifyTitaniumAcylate", 1000)});
        // Chemistry.java:156
        add("Chemistry.java:156", Gt6KtfruMachines.HEAT_MIXER, 250L, 120L,
                new ItemStack[] {circuit(0, 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_PurifyTitaniumAcylate", 1000), field("GenLiquid_SulfuricAcid", 2000)},
                new FluidStack[] {field("GenLiquid_TitaniumAcylate", 1000)});
        // Chemistry.java:158
        add("Chemistry.java:158", Gt6KtfruMachines.HEAT_MIXER, 480L, 800L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "NanoBariumTitanate", 1)},
                new FluidStack[] {field("GenLiquid_TitaniumAcylate", 1000), field("GenLiquid_BariumAcetate", 1000)},
                NO_FLUIDS);
        // Plastic.java:45
        add("Plastic.java:45", Gt6KtfruMachines.HEAT_MIXER, 64L, 80L,
                new ItemStack[] {form("dust", "Calcite", 1)},
                new ItemStack[] {form("dust", "CalciumAcetate", 1)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 100)},
                new FluidStack[] {field("CarbonDioxide", 1000)});
        // Plastic.java:46
        add("Plastic.java:46", Gt6KtfruMachines.HEAT_MIXER, 64L, 80L,
                new ItemStack[] {form("dust", "SodiumCarbonate", 1)},
                new ItemStack[] {form("dust", "SodiumAcetate", 2)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 100)},
                new FluidStack[] {field("CarbonDioxide", 1000)});
        // Plastic.java:52
        add("Plastic.java:52", Gt6KtfruMachines.HEAT_MIXER, 100L, 110L,
                new ItemStack[] {form("dust", "DiphenylCarbonate", 1), form("dust", "BPA", 1)},
                new ItemStack[] {form("dust", "HardPlastic", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
        // Plastic.java:53
        add("Plastic.java:53", Gt6KtfruMachines.HEAT_MIXER, 64L, 82L,
                new ItemStack[] {form("dust", "Aluminium", 1)},
                new ItemStack[] {form("dust", "TriethylAluminium", 1)},
                new FluidStack[] {field("GenLiquid_Ethane", 1500)},
                NO_FLUIDS);
        // Plastic.java:57
        add("Plastic.java:57", Gt6KtfruMachines.HEAT_MIXER, 240L, 100L,
                new ItemStack[] {form("dust", "CalciumAcetate", 1)},
                new ItemStack[] {form("dust", "Calcite", 1)},
                NO_FLUIDS,
                new FluidStack[] {field("GenLiquid_Acetone", 500)});
        // Plastic.java:61
        add("Plastic.java:61", Gt6KtfruMachines.HEAT_MIXER, 256L, 80L,
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Propylene", 100), field("Oxygen", 100), field("Hydrogen", 100)},
                new FluidStack[] {field("GenLiquid_AllylAlcohol", 100)},
                0L,
                null,
                new int[] {0});
        // Plastic.java:65
        add("Plastic.java:65", Gt6KtfruMachines.HEAT_MIXER, 148L, 120L,
                new ItemStack[] {form("dust", "SodiumHydroxide", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_DichloroPropanol", 100)},
                new FluidStack[] {field("GenLiquid_Epichlorohydrin", 100)});
        // Plastic.java:69
        add("Plastic.java:69", Gt6KtfruMachines.HEAT_MIXER, 110L, 1100L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "HardPlastic", 2)},
                new FluidStack[] {field("GenLiquid_SolutionBPASodium", 1000), field("GenGas_Phosgene", 200), field("GenLiquid_Dichloromethane", 200)},
                NO_FLUIDS);
        // Plastic.java:71
        add("Plastic.java:71", Gt6KtfruMachines.HEAT_MIXER, 142L, 1200L,
                new ItemStack[] {form("dust", "Zinc", 1)},
                new ItemStack[] {form("dust", "ZincChloride", 3)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 1600), field("GenLiquid_Chloroform", 400)},
                new FluidStack[] {field("GenGas_Tetrafluoroethylene", 400), field("GenGas_HydrochloricAcid", 2000)});
        // Plastic.java:75
        add("Plastic.java:75", Gt6KtfruMachines.HEAT_MIXER, 83L, 100L,
                new ItemStack[] {form("dust", "PotassiumSulfate", 1)},
                new ItemStack[] {form("dust", "PotassiumPyrosulfate", 1)},
                new FluidStack[] {field("GenGas_SulfurTrioxide", 1000)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0});
        // Plastic.java:80
        add("Plastic.java:80", Gt6KtfruMachines.HEAT_MIXER, 320L, 1200L,
                new ItemStack[] {own("proton_exchange_membrane", 1), form("dust", "BPA", 1), form("dust", "SodiumHydroxide", 2)},
                new ItemStack[] {form("dust", "EpoxyResin", 6)},
                new FluidStack[] {field("GenLiquid_Epichlorohydrin", 1000)},
                new FluidStack[] {field("Saltwater", 800)},
                0L,
                null,
                new int[] {0});
        // Plastic.java:85
        add("Plastic.java:85", Gt6KtfruMachines.HEAT_MIXER, 180L, 1200L,
                new ItemStack[] {form("dust", "SodiumHydroxide", 1)},
                new ItemStack[] {form("dust", "Bakelite", 8)},
                new FluidStack[] {field("GenLiquid_Phenol", 1000), field("GenGas_Formaldehyde", 1000)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0});
        // Plastic.java:91
        add("Plastic.java:91", Gt6KtfruMachines.HEAT_MIXER, 500L, 600L,
                new ItemStack[] {form("dust", "Iron", 1), form("dust", "Silicon", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Chlorine", 2000)},
                new FluidStack[] {field("GenLiquid_Tetrachorosilane", 1000)},
                0L,
                null,
                new int[] {0});
        // Plastic.java:98
        add("Plastic.java:98", Gt6KtfruMachines.HEAT_MIXER, 168L, 200L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "MagnesiumChloride", 1)},
                new FluidStack[] {field("GenLiquid_Methyltrichlorosilane", 100), field("GenLiquid_MethylmagnesiumChloride", 100)},
                new FluidStack[] {field("GenLiquid_Dichlorodimethylsilane", 100)});
        // Plastic.java:99
        add("Plastic.java:99", Gt6KtfruMachines.HEAT_MIXER, 256L, 2080L,
                new ItemStack[] {form("dust", "Silicon", 1), form("dust", "Brass", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Chloromethane", 200)},
                new FluidStack[] {field("GenLiquid_Dichlorodimethylsilane", 100)},
                0L,
                null,
                new int[] {1});
        // Plastic.java:107
        add("Plastic.java:107", Gt6KtfruMachines.HEAT_MIXER, 24L, 120L,
                new ItemStack[] {form("dust", "Mercury", 1)},
                new ItemStack[] {form("dust", "MercuryIIChloride", 1)},
                new FluidStack[] {field("GenGas_Chlorine", 2000)},
                NO_FLUIDS);
        // Plastic.java:115
        add("Plastic.java:115", Gt6KtfruMachines.HEAT_MIXER, 144L, 320L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_DichloroEthane", 100)},
                new FluidStack[] {field("GenLiquid_VinylChloride", 100), field("GenGas_HydrochloricAcid", 100)});
        // Plastic.java:119
        add("Plastic.java:119", Gt6KtfruMachines.HEAT_MIXER, 156L, 80L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Benzene", 100), field("Ethylene", 100)},
                new FluidStack[] {field("GenLiquid_Ethylbenzene", 100)});
        // Plastic.java:123
        add("Plastic.java:123", Gt6KtfruMachines.HEAT_MIXER, 412L, 40L,
                new ItemStack[] {form("dust", "Chromium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Ethylbenzene", 100), field("Water", 50)},
                new FluidStack[] {field("GenLiquid_Styrene", 100), field("Hydrogen", 50)},
                0L,
                null,
                new int[] {0});
        // Plastic.java:129
        add("Plastic.java:129", Gt6KtfruMachines.HEAT_MIXER, 120L, 60L,
                new ItemStack[] {form("dust", "Chromium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Ethylene", 200)},
                new FluidStack[] {field("GenLiquid_Isoprene", 144)},
                0L,
                null,
                new int[] {0});
        // Plastic.java:133
        add("Plastic.java:133", Gt6KtfruMachines.HEAT_MIXER, 210L, 100L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Tetrafluoroethylene", 100), field("GenGas_SulfurTrioxide", 100)},
                new FluidStack[] {field("GenLiquid_TFES", 100)});
        // Plastic.java:135
        add("Plastic.java:135", Gt6KtfruMachines.HEAT_MIXER, 256L, 80L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Tetrafluoroethylene", 600), field("GenGas_Acetylene", 200)},
                new FluidStack[] {field("GenGas_HPF", 200), field("GenGas_perfluorocyclobutane", 200)});
        // Plastic.java:143
        add("Plastic.java:143", Gt6KtfruMachines.HEAT_MIXER, 210L, 100L,
                new ItemStack[] {form("dust", "SodiumHydroxide", 1)},
                new ItemStack[] {form("dust", "SodiumHydrogencarbonate", 1)},
                new FluidStack[] {field("GenLiquid_PrecursorPSVE", 100)},
                new FluidStack[] {field("GenLiquid_PSVE", 100)});
        // Plastic.java:145
        add("Plastic.java:145", Gt6KtfruMachines.HEAT_MIXER, 210L, 100L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "PSVE", 1)},
                new FluidStack[] {field("GenLiquid_PSVE", 100), field("GenGas_HPF", 100), field("GenGas_Tetrafluoroethylene", 100)},
                NO_FLUIDS);
        // Plastic.java:151
        add("Plastic.java:151", Gt6KtfruMachines.HEAT_MIXER, 310L, 120L,
                new ItemStack[] {form("dust", "Gold", 1), form("dust", "Lead", 1)},
                new ItemStack[] {form("dust", "VinylAcetate", 1)},
                new FluidStack[] {field("Ethylene", 100), field("GenLiquid_GlacialAceticAcid", 100), field("Oxygen", 100)},
                new FluidStack[] {field("CarbonDioxide", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0, 1});
        // Plastic.java:153
        add("Plastic.java:153", Gt6KtfruMachines.HEAT_MIXER, 262L, 140L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Acetylene", 100), field("GenLiquid_GlacialAceticAcid", 100)},
                new FluidStack[] {field("GenLiquid_VinylAcetate", 100)});
        // Plastic.java:161
        add("Plastic.java:161", Gt6KtfruMachines.HEAT_MIXER, 120L, 200L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Propylene", 1000), field("GenGas_Chlorine", 1000)},
                new FluidStack[] {field("GenLiquid_Dichloromethane", 1000)});
        // CompactItem.java:157
        add("CompactItem.java:157", Gt6KtfruMachines.HEAT_MIXER, 240L, 64L,
                new ItemStack[] {form("dust", "Barium", 1), form("dust", "Rutile", 1)},
                new ItemStack[] {form("dust", "BariumTitanate", 1)},
                new FluidStack[] {field("CarbonDioxide", 1000)},
                NO_FLUIDS);
        // ComputerBuilding.java:66
        add("ComputerBuilding.java:66", Gt6KtfruMachines.HEAT_MIXER, 32L, 120L,
                new ItemStack[] {form("dust", "Naphthalene", 1)},
                new ItemStack[] {form("dust", "Naphthalenol", 1)},
                new FluidStack[] {field("GenGas_Chlorine", 200), field("Water", 200)},
                new FluidStack[] {field("GenGas_HydrochloricAcid", 200)});
        // ComputerBuilding.java:70
        add("ComputerBuilding.java:70", Gt6KtfruMachines.HEAT_MIXER, 60L, 70L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_MethoxyPropanol", 100), field("GenLiquid_GlacialAceticAcid", 100)},
                new FluidStack[] {field("GenLiquid_PGMEA", 100)});
        // ComputerBuilding.java:72
        add("ComputerBuilding.java:72", Gt6KtfruMachines.HEAT_MIXER, 160L, 90L,
                new ItemStack[] {form("dust", "Bakelite", 8), form("dust", "DiazoNaphthol", 8)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_PGMEA", 1000)},
                new FluidStack[] {field("GenLiquid_DNQPhotoresist", 1000)});
        // ComputerBuilding.java:74
        add("ComputerBuilding.java:74", Gt6KtfruMachines.HEAT_MIXER, 240L, 120L,
                new ItemStack[] {form("dust", "Palladium", 1)},
                new ItemStack[] {form("dust", "Cyclohexanol", 1)},
                new FluidStack[] {field("GenLiquid_Phenol", 100), field("Hydrogen", 100)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0});
        // ComputerBuilding.java:75
        add("ComputerBuilding.java:75", Gt6KtfruMachines.HEAT_MIXER, 240L, 120L,
                new ItemStack[] {form("dust", "Silver", 1), form("dust", "Cyclohexanol", 1)},
                new ItemStack[] {form("dust", "Cyclohexanone", 1)},
                new FluidStack[] {field("Oxygen", 100)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0});
        // ComputerBuilding.java:77
        add("ComputerBuilding.java:77", Gt6KtfruMachines.HEAT_MIXER, 120L, 120L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "AmmoniumNitrate", 1)},
                new FluidStack[] {field("GenLiquid_NitricAcid", 1000), field("GenGas_Ammonia", 1000)},
                NO_FLUIDS);
        // ComputerBuilding.java:81
        add("ComputerBuilding.java:81", Gt6KtfruMachines.HEAT_MIXER, 1440L, 120L,
                new ItemStack[] {form("dust", "Nitroacenaphthene", 8), form("dust", "PolymethylMethacrylate", 6)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Cyclohexanone", 1000)},
                new FluidStack[] {field("GenLiquid_PMMAPhotoresist", 1000)});
        // ComputerBuilding.java:124
        add("ComputerBuilding.java:124", Gt6KtfruMachines.HEAT_MIXER, 80L, 10L,
                new ItemStack[] {form("dust", "Silicon", 1), form("dust", "Magnesium", 2)},
                new ItemStack[] {form("dust", "MagnesiumSilicide", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:125
        add("ComputerBuilding.java:125", Gt6KtfruMachines.HEAT_MIXER, 120L, 60L,
                new ItemStack[] {form("dust", "Salt", 1)},
                new ItemStack[] {form("dust", "SodiumHydroxide", 1), form("dust", "AmmoniumChloride", 1)},
                new FluidStack[] {field("GenGas_Ammonia", 1000), field("Water", 1000)},
                NO_FLUIDS);
        // ComputerBuilding.java:128
        add("ComputerBuilding.java:128", Gt6KtfruMachines.HEAT_MIXER, 290L, 10L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_HexafluorosilicicAcid", 3000)},
                new FluidStack[] {field("GenGas_SiliconTetrafluoride", 1000), field("GenGas_HydrogenFluoride", 2000)});
        // ComputerBuilding.java:130
        add("ComputerBuilding.java:130", Gt6KtfruMachines.HEAT_MIXER, 410L, 40L,
                new ItemStack[] {form("dust", "SodiumAluminiumHydride", 1)},
                new ItemStack[] {form("dust", "Cryolite", 1)},
                new FluidStack[] {field("GenGas_SiliconTetrafluoride", 1000)},
                new FluidStack[] {field("GenGas_Silane", 1000)});
        // Fusion.java:100
        add("Fusion.java:100", Gt6KtfruMachines.HEAT_MIXER, 32768L, 2400L,
                new ItemStack[] {own("naquadria_precursor", 1), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustTiny", "Naquadria", 1)},
                new FluidStack[] {field("Helium", 4000)},
                NO_FLUIDS);
        // HeatMixer.java:28
        add("HeatMixer.java:28", Gt6KtfruMachines.HEAT_MIXER, 16L, 48L,
                new ItemStack[] {form("dust", "Sulfur", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 2000)},
                new FluidStack[] {field("GenGas_HydrosulfuricAcid", 3000)});
        // HeatMixer.java:29
        add("HeatMixer.java:29", Gt6KtfruMachines.HEAT_MIXER, 16L, 48L,
                new ItemStack[] {form("dustTiny", "Blaze", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 2000)},
                new FluidStack[] {field("GenGas_HydrosulfuricAcid", 3000)});
        // HeatMixer.java:30
        add("HeatMixer.java:30", Gt6KtfruMachines.HEAT_MIXER, 16L, 48L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "Sulfur", 1)},
                new FluidStack[] {field("GenGas_HydrosulfuricAcid", 2000), field("GenGas_SulfurDioxide", 1000)},
                new FluidStack[] {field("Water", 2000)});
        // OilProcessing.java:32
        add("OilProcessing.java:32", Gt6KtfruMachines.HEAT_MIXER, 32L, 100L,
                new ItemStack[] {form("dust", "Copper", 1), form("dustTiny", "Zinc", 1), form("dustTiny", "Chromium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 200), field("GenGas_CarbonMonoxide", 100)},
                new FluidStack[] {field("GenLiquid_Methanol", 120)},
                0L,
                null,
                new int[] {0, 1, 2});
        // OilProcessing.java:48
        add("OilProcessing.java:48", Gt6KtfruMachines.HEAT_MIXER, 80L, 14L,
                new ItemStack[] {form("dust", "Nickel", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 40), field("GenLiquid_OilDesulfurizationer", 4), field("GenLiquid_DesaltOilLight", 100)},
                new FluidStack[] {field("GenLiquid_CleanedOilLight", 100), field("GenLiquid_SulfuredOilDesulfurizationer", 4)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:49
        add("OilProcessing.java:49", Gt6KtfruMachines.HEAT_MIXER, 80L, 20L,
                new ItemStack[] {form("dust", "Nickel", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 40), field("GenLiquid_OilDesulfurizationer", 6), field("GenLiquid_DesaltOilNormal", 100)},
                new FluidStack[] {field("GenLiquid_CleanedOilNormal", 100), field("GenLiquid_SulfuredOilDesulfurizationer", 6)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:50
        add("OilProcessing.java:50", Gt6KtfruMachines.HEAT_MIXER, 80L, 26L,
                new ItemStack[] {form("dust", "Nickel", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 40), field("GenLiquid_OilDesulfurizationer", 8), field("GenLiquid_DesaltOilMedium", 100)},
                new FluidStack[] {field("GenLiquid_CleanedOilMedium", 100), field("GenLiquid_SulfuredOilDesulfurizationer", 8)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:51
        add("OilProcessing.java:51", Gt6KtfruMachines.HEAT_MIXER, 80L, 32L,
                new ItemStack[] {form("dust", "Nickel", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 40), field("GenLiquid_OilDesulfurizationer", 10), field("GenLiquid_DesaltOilHeavy", 100)},
                new FluidStack[] {field("GenLiquid_CleanedOilHeavy", 100), field("GenLiquid_SulfuredOilDesulfurizationer", 10)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:52
        add("OilProcessing.java:52", Gt6KtfruMachines.HEAT_MIXER, 80L, 38L,
                new ItemStack[] {form("dust", "Nickel", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Hydrogen", 40), field("GenLiquid_OilDesulfurizationer", 12), field("GenLiquid_DesaltOilExtraHeavy", 100)},
                new FluidStack[] {field("GenLiquid_CleanedOilExtraHeavy", 100), field("GenLiquid_SulfuredOilDesulfurizationer", 12)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:54
        add("OilProcessing.java:54", Gt6KtfruMachines.HEAT_MIXER, 32L, 2400L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_SulfuredOilDesulfurizationer", 1000), field("Hydrogen", 200)},
                new FluidStack[] {field("GenLiquid_OilDesulfurizationer", 1000), field("GenGas_HydrosulfuricAcid", 100)});
        // OilProcessing.java:97
        add("OilProcessing.java:97", Gt6KtfruMachines.HEAT_MIXER, 64L, 200L,
                new ItemStack[] {form("dust", "Iron", 1), form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Methane", 100), field("Oxygen", 100)},
                new FluidStack[] {field("GenGas_Formaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0, 1});
        // OilProcessing.java:99
        add("OilProcessing.java:99", Gt6KtfruMachines.HEAT_MIXER, 44L, 20L,
                new ItemStack[] {form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Formaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:100
        add("OilProcessing.java:100", Gt6KtfruMachines.HEAT_MIXER, 44L, 20L,
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Formaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:102
        add("OilProcessing.java:102", Gt6KtfruMachines.HEAT_MIXER, 44L, 20L,
                new ItemStack[] {form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Acetaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:103
        add("OilProcessing.java:103", Gt6KtfruMachines.HEAT_MIXER, 44L, 20L,
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Acetaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:105
        add("OilProcessing.java:105", Gt6KtfruMachines.HEAT_MIXER, 44L, 30L,
                new ItemStack[] {form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Propanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Propionaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:106
        add("OilProcessing.java:106", Gt6KtfruMachines.HEAT_MIXER, 44L, 30L,
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Propanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Propionaldehyde", 100), field("Water", 100)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:108
        add("OilProcessing.java:108", Gt6KtfruMachines.HEAT_MIXER, 47L, 30L,
                new ItemStack[] {form("dust", "Cobalt", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("GenGas_CarbonMonoxide", 50), field("Hydrogen", 50)},
                new FluidStack[] {field("GenGas_Propionaldehyde", 100)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:109
        add("OilProcessing.java:109", Gt6KtfruMachines.HEAT_MIXER, 47L, 30L,
                new ItemStack[] {form("dust", "Rhodium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("GenGas_CarbonMonoxide", 50), field("Hydrogen", 50)},
                new FluidStack[] {field("GenGas_Propionaldehyde", 100)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:111
        add("OilProcessing.java:111", Gt6KtfruMachines.HEAT_MIXER, 217L, 15L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Propionaldehyde", 100), field("Hydrogen", 100)},
                new FluidStack[] {field("GenLiquid_Propanol", 100)});
        // OilProcessing.java:117
        add("OilProcessing.java:117", Gt6KtfruMachines.HEAT_MIXER, 137L, 55L,
                new ItemStack[] {form("dust", "Silver", 1), form("dust", "Rhodium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("Water", 100)},
                new FluidStack[] {field("GenLiquid_Propanol", 100)},
                0L,
                null,
                new int[] {0, 1});
        // OilProcessing.java:130
        add("OilProcessing.java:130", Gt6KtfruMachines.HEAT_MIXER, 32L, 40L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_OilGas", 100), field("GenLiquid_Benzene", 5), field("Hydrogen", 80)},
                new FluidStack[] {field("Diesel", 10), field("GenGas_CrackedOilGas", 100)});
        // OilProcessing.java:161
        add("OilProcessing.java:161", Gt6KtfruMachines.HEAT_MIXER, 280L, 160L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Oxygen", 500), field("Propane", 1000)},
                new FluidStack[] {field("GenLiquid_Acetone", 500)});
        // OilProcessing.java:162
        add("OilProcessing.java:162", Gt6KtfruMachines.HEAT_MIXER, 80L, 400L,
                new ItemStack[] {form("dust", "Manganese", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Oxygen", 500), field("GenLiquid_VinylAcetate", 1000)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 1000)},
                0L,
                null,
                new int[] {0});
        // OilProcessing.java:164
        add("OilProcessing.java:164", Gt6KtfruMachines.HEAT_MIXER, 170L, 80L,
                new ItemStack[] {form("dust", "Rhodium", 1), form("dust", "Iodine", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 1000), field("GenGas_CarbonMonoxide", 1000)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 1000)},
                0L,
                null,
                new int[] {0, 1});
        // OilProcessing.java:165
        add("OilProcessing.java:165", Gt6KtfruMachines.HEAT_MIXER, 170L, 100L,
                new ItemStack[] {form("dust", "Iridium", 1), form("dust", "Iodine", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 1000), field("GenGas_CarbonMonoxide", 1000)},
                new FluidStack[] {field("GenLiquid_GlacialAceticAcid", 1000)},
                0L,
                null,
                new int[] {0, 1});
        // OilProcessing.java:167
        add("OilProcessing.java:167", Gt6KtfruMachines.HEAT_MIXER, 40L, 160L,
                new ItemStack[] {circuit(0, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Naphtha", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedNaphthaLow", 1000)},
                0L,
                null,
                new int[] {1});
        // OilProcessing.java:168
        add("OilProcessing.java:168", Gt6KtfruMachines.HEAT_MIXER, 120L, 150L,
                new ItemStack[] {circuit(1, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Naphtha", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedNaphthaMedium", 1000)},
                0L,
                null,
                new int[] {1});
        // OilProcessing.java:169
        add("OilProcessing.java:169", Gt6KtfruMachines.HEAT_MIXER, 420L, 140L,
                new ItemStack[] {circuit(2, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Naphtha", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedNaphthaHigh", 1000)},
                0L,
                null,
                new int[] {1});
        // OilProcessing.java:177
        add("OilProcessing.java:177", Gt6KtfruMachines.HEAT_MIXER, 40L, 160L,
                new ItemStack[] {circuit(0, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Petrol", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedPetrolLow", 1000)},
                0L,
                null,
                new int[] {1});
        // OilProcessing.java:178
        add("OilProcessing.java:178", Gt6KtfruMachines.HEAT_MIXER, 120L, 150L,
                new ItemStack[] {circuit(1, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Petrol", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedPetrolMedium", 1000)},
                0L,
                null,
                new int[] {1});
        // OilProcessing.java:179
        add("OilProcessing.java:179", Gt6KtfruMachines.HEAT_MIXER, 420L, 140L,
                new ItemStack[] {circuit(2, 1), form("dust", "Platinum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Petrol", 1000), field("Hydrogen", 800)},
                new FluidStack[] {field("GenLiquid_CrackedPetrolHigh", 1000)},
                0L,
                null,
                new int[] {1});
        // OreProcessing.java:35
        add("OreProcessing.java:35", Gt6KtfruMachines.HEAT_MIXER, 100L, 400L,
                new ItemStack[] {form("dust", "Iron", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Nitrogen", 2000), field("Hydrogen", 3000)},
                new FluidStack[] {field("GenGas_Ammonia", 2000)},
                0L,
                null,
                new int[] {0});
        // OreProcessing.java:40
        add("OreProcessing.java:40", Gt6KtfruMachines.HEAT_MIXER, 320L, 160L,
                new ItemStack[] {form("dust", "AmmoniumDichromate", 1)},
                new ItemStack[] {form("dust", "ChromiumDioxide", 2)},
                new FluidStack[] {field("Oxygen", 4000)},
                new FluidStack[] {field("Nitrogen", 250)});
        // OreProcessing.java:45
        add("OreProcessing.java:45", Gt6KtfruMachines.HEAT_MIXER, 120L, 80L,
                new ItemStack[] {form("dust", "Calcite", 1), form("dust", "SodiumCarbonate", 2), form("dust", "Bauxite", 1)},
                new ItemStack[] {form("dust", "CookedBauxide", 2)},
                NO_FLUIDS,
                new FluidStack[] {field("CarbonDioxide", 3000)});
        // OreProcessing.java:68
        add("OreProcessing.java:68", Gt6KtfruMachines.HEAT_MIXER, 320L, 160L,
                new ItemStack[] {form("dust", "TungstenTrioxide", 4)},
                new ItemStack[] {form("dust", "Tungsten", 1)},
                new FluidStack[] {field("Hydrogen", 6000)},
                new FluidStack[] {field("Water", 9000)});
        // OreProcessing.java:73
        add("OreProcessing.java:73", Gt6KtfruMachines.HEAT_MIXER, 96L, 60L,
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "MagnesiumChloride", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Helium", 1), field("GenMolten_Magnesium", 288)},
                NO_FLUIDS);
        // OreProcessing.java:74
        add("OreProcessing.java:74", Gt6KtfruMachines.HEAT_MIXER, 128L, 60L,
                new ItemStack[] {form("dust", "Magnesium", 2)},
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "MagnesiumChloride", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Helium", 1)},
                NO_FLUIDS);
        // OreProcessing.java:75
        add("OreProcessing.java:75", Gt6KtfruMachines.HEAT_MIXER, 96L, 60L,
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "Salt", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Helium", 1), field("GenMolten_Sodium", 288)},
                NO_FLUIDS);
        // OreProcessing.java:76
        add("OreProcessing.java:76", Gt6KtfruMachines.HEAT_MIXER, 128L, 60L,
                new ItemStack[] {form("dust", "Sodium", 2)},
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "Salt", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Helium", 1)},
                NO_FLUIDS);
        // OreProcessing.java:77
        add("OreProcessing.java:77", Gt6KtfruMachines.HEAT_MIXER, 96L, 60L,
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "MagnesiumChloride", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Argon", 1), field("GenMolten_Magnesium", 288)},
                NO_FLUIDS);
        // OreProcessing.java:78
        add("OreProcessing.java:78", Gt6KtfruMachines.HEAT_MIXER, 128L, 60L,
                new ItemStack[] {form("dust", "Magnesium", 2)},
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "MagnesiumChloride", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Argon", 1)},
                NO_FLUIDS);
        // OreProcessing.java:79
        add("OreProcessing.java:79", Gt6KtfruMachines.HEAT_MIXER, 96L, 60L,
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "Salt", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Argon", 1), field("GenMolten_Sodium", 288)},
                NO_FLUIDS);
        // OreProcessing.java:80
        add("OreProcessing.java:80", Gt6KtfruMachines.HEAT_MIXER, 128L, 60L,
                new ItemStack[] {form("dust", "Sodium", 2)},
                new ItemStack[] {form("dustSmall", "Titanium", 6), form("dust", "Salt", 6)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 5000), field("Argon", 1)},
                NO_FLUIDS);
        // OreProcessing.java:100
        add("OreProcessing.java:100", Gt6KtfruMachines.HEAT_MIXER, 1428L, 20L,
                new ItemStack[] {form("dust", "UraniumNitrateHexahydrate", 1)},
                new ItemStack[] {form("dust", "UraniumTetrafluoride", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 4000)},
                NO_FLUIDS);
        // OreProcessing.java:102
        add("OreProcessing.java:102", Gt6KtfruMachines.HEAT_MIXER, 386L, 300L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Propane", 1000), field("GenGas_Acetaldehyde", 3000)},
                new FluidStack[] {field("GenLiquid_Butanol", 1000), field("GenLiquid_Ethane", 1000)});
        // OreProcessing.java:103
        add("OreProcessing.java:103", Gt6KtfruMachines.HEAT_MIXER, 468L, 80L,
                new ItemStack[] {form("dust", "Phosphate", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Butanol", 3000)},
                new FluidStack[] {field("GenLiquid_TributylPhosphate", 1000), field("Water", 1000)});
        // OreProcessing.java:105
        add("OreProcessing.java:105", Gt6KtfruMachines.HEAT_MIXER, 465L, 100L,
                new ItemStack[] {form("dust", "WaxParaffin", 1), form("dust", "Pyrolusite", 1)},
                new ItemStack[] {form("dust", "OleicAcid", 1)},
                new FluidStack[] {field("Oxygen", 1000)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // PlatinumGroupProcess.java:32
        add("PlatinumGroupProcess.java:32", Gt6KtfruMachines.HEAT_MIXER, 212L, 120L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Styrene", 1000), field("GenLiquid_Dichloromethane", 1000)},
                new FluidStack[] {field("GenLiquid_VinylbenzylChloride", 500)});
        // PlatinumGroupProcess.java:33
        add("PlatinumGroupProcess.java:33", Gt6KtfruMachines.HEAT_MIXER, 212L, 320L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "DimethylaminomethylStyrene", 4)},
                new FluidStack[] {field("GenLiquid_VinylbenzylChloride", 1000), field("GenLiquid_Dimethylamine", 1000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:34
        add("PlatinumGroupProcess.java:34", Gt6KtfruMachines.HEAT_MIXER, 82L, 120L,
                new ItemStack[] {form("dust", "DimethylaminomethylStyrene", 1)},
                new ItemStack[] {own("alkaline_ion_exchange_membrane", 1)},
                new FluidStack[] {field("Oxygen", 1000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:37
        add("PlatinumGroupProcess.java:37", Gt6KtfruMachines.HEAT_MIXER, 350L, 16L,
                new ItemStack[] {form("dust", "Sulfur", 2)},
                NO_ITEMS,
                new FluidStack[] {field("Butane", 2000), field("Ethylene", 1000)},
                new FluidStack[] {field("GenLiquid_DinhexylSulfide", 1000)});
        // PlatinumGroupProcess.java:40
        add("PlatinumGroupProcess.java:40", Gt6KtfruMachines.HEAT_MIXER, 180L, 90L,
                new ItemStack[] {form("dust", "Alumina", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 2000), field("GenGas_Ammonia", 1000)},
                new FluidStack[] {field("GenLiquid_Methylamine", 400), field("GenLiquid_Dimethylamine", 300), field("GenLiquid_Trimethylamine", 300)},
                0L,
                null,
                new int[] {0});
        // PlatinumGroupProcess.java:42
        add("PlatinumGroupProcess.java:42", Gt6KtfruMachines.HEAT_MIXER, 180L, 90L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_EthylChloroacetate", 1000), field("GenLiquid_Dimethylamine", 1000)},
                new FluidStack[] {field("GenLiquid_Dimethylglycine", 1000)});
        // PlatinumGroupProcess.java:47
        add("PlatinumGroupProcess.java:47", Gt6KtfruMachines.HEAT_MIXER, 180L, 90L,
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "Gold", 2)},
                new FluidStack[] {field("GenLiquid_RoughPlatinumGroupSolution", 1000), field("GenGas_SulfurDioxide", 1000)},
                new FluidStack[] {field("GenLiquid_SulphurPlatinumGroupSolution", 1500)});
        // PlatinumGroupProcess.java:54
        add("PlatinumGroupProcess.java:54", Gt6KtfruMachines.HEAT_MIXER, 480L, 16L,
                new ItemStack[] {form("dust", "AmmoniumChloride", 1), form("dust", "Salt", 1)},
                new ItemStack[] {form("dust", "SodiumNitrate", 2), form("dust", "AmmoniumHexachlororhodate", 6)},
                new FluidStack[] {field("GenLiquid_ElectrolyzedNitricPlatinumGroupSolution", 1000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:56
        add("PlatinumGroupProcess.java:56", Gt6KtfruMachines.HEAT_MIXER, 180L, 16L,
                new ItemStack[] {form("dust", "AmmoniumChloride", 1)},
                new ItemStack[] {form("dust", "AmmoniumHexachloroiridate", 7)},
                new FluidStack[] {field("GenLiquid_IridiumPlatinumGroupSolution", 1000), field("GenGas_Chlorine", 2000)},
                new FluidStack[] {field("GenLiquid_SulfuricAcid", 1000)});
        // PlatinumGroupProcess.java:57
        add("PlatinumGroupProcess.java:57", Gt6KtfruMachines.HEAT_MIXER, 180L, 16L,
                new ItemStack[] {form("dust", "SodiumHydroxide", 3)},
                new ItemStack[] {form("dust", "Rhodium", 3)},
                new FluidStack[] {field("GenLiquid_NitricPlatinumGroupSolutionUp", 1000)},
                new FluidStack[] {field("GenLiquid_DeprecatedNitricPlatinumGroupSolutionUp", 1000)});
        // PlatinumGroupProcess.java:58
        add("PlatinumGroupProcess.java:58", Gt6KtfruMachines.HEAT_MIXER, 180L, 16L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "Salt", 1)},
                new FluidStack[] {field("GenLiquid_DeprecatedNitricPlatinumGroupSolutionUp", 2000), field("GenGas_HydrochloricAcid", 1000)},
                new FluidStack[] {field("GenLiquid_Trimethylamine", 500)});
        // PlatinumGroupProcess.java:61
        add("PlatinumGroupProcess.java:61", Gt6KtfruMachines.HEAT_MIXER, 180L, 16L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_ExtractedPlatinumGroupSolutionUp", 1500), field("GenGas_HydrochloricAcid", 12000)},
                new FluidStack[] {field("GenLiquid_Dimethylglycine", 1000), field("GenLiquid_AcidPlatinumGroupSolutionUp", 500)});
        // PlatinumGroupProcess.java:62
        add("PlatinumGroupProcess.java:62", Gt6KtfruMachines.HEAT_MIXER, 180L, 16L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "Platinum", 1)},
                new FluidStack[] {field("GenLiquid_DinhexylSulfide", 1000), field("GenLiquid_AcidPlatinumGroupSolutionUp", 500)},
                new FluidStack[] {field("GenLiquid_DinhexylSulfidePlatinumGroupSolution", 1000), field("GenLiquid_ChloroplatinicAcid", 9000)});
        // PlatinumGroupProcess.java:63
        add("PlatinumGroupProcess.java:63", Gt6KtfruMachines.HEAT_MIXER, 180L, 16L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "AmmoniumHexachloropalladate", 4)},
                new FluidStack[] {field("GenLiquid_DinhexylSulfidePlatinumGroupSolution", 1000), field("GenGas_Ammonia", 4000)},
                new FluidStack[] {field("GenLiquid_DinhexylSulfide", 1000)});
        // PlatinumGroupProcess.java:65
        add("PlatinumGroupProcess.java:65", Gt6KtfruMachines.HEAT_MIXER, 780L, 2000L,
                new ItemStack[] {form("dust", "Rhodium", 2)},
                new ItemStack[] {form("dust", "Rhodiumoxide", 9)},
                new FluidStack[] {field("Oxygen", 3000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:66
        add("PlatinumGroupProcess.java:66", Gt6KtfruMachines.HEAT_MIXER, 200L, 1200L,
                new ItemStack[] {form("dust", "Palladium", 1)},
                new ItemStack[] {form("dust", "Palladiumoxide", 3)},
                new FluidStack[] {field("Oxygen", 1000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:67
        add("PlatinumGroupProcess.java:67", Gt6KtfruMachines.HEAT_MIXER, 420L, 800L,
                new ItemStack[] {form("dust", "Iridium", 1)},
                new ItemStack[] {form("dust", "Iridiumoxide", 5)},
                new FluidStack[] {field("Oxygen", 2000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:68
        add("PlatinumGroupProcess.java:68", Gt6KtfruMachines.HEAT_MIXER, 820L, 200L,
                new ItemStack[] {form("dust", "Rhodiumoxide", 3)},
                new ItemStack[] {form("dust", "Rhodium", 1)},
                new FluidStack[] {field("Hydrogen", 4000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:69
        add("PlatinumGroupProcess.java:69", Gt6KtfruMachines.HEAT_MIXER, 700L, 200L,
                new ItemStack[] {form("dust", "Palladiumoxide", 3)},
                new ItemStack[] {form("dust", "Palladium", 2)},
                new FluidStack[] {field("Hydrogen", 4000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:70
        add("PlatinumGroupProcess.java:70", Gt6KtfruMachines.HEAT_MIXER, 730L, 200L,
                new ItemStack[] {form("dust", "Iridiumoxide", 5)},
                new ItemStack[] {form("dust", "Iridium", 2)},
                new FluidStack[] {field("Hydrogen", 8000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:71
        add("PlatinumGroupProcess.java:71", Gt6KtfruMachines.HEAT_MIXER, 730L, 200L,
                new ItemStack[] {form("dust", "OsmiumTetraoxide", 1)},
                new ItemStack[] {form("dustSmall", "OsmiumElemental", 6)},
                new FluidStack[] {field("Hydrogen", 3000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:73
        add("PlatinumGroupProcess.java:73", Gt6KtfruMachines.HEAT_MIXER, 630L, 600L,
                new ItemStack[] {form("dust", "AmmoniumHexachlororhodate", 2)},
                new ItemStack[] {form("dust", "Rhodiumoxide", 1), form("dust", "AmmoniumChloride", 1)},
                new FluidStack[] {field("Oxygen", 4000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:74
        add("PlatinumGroupProcess.java:74", Gt6KtfruMachines.HEAT_MIXER, 430L, 600L,
                new ItemStack[] {form("dust", "AmmoniumHexachloroiridate", 7)},
                new ItemStack[] {form("dust", "Palladiumoxide", 3), form("dust", "AmmoniumChloride", 4)},
                new FluidStack[] {field("Oxygen", 12000)},
                NO_FLUIDS);
        // PlatinumGroupProcess.java:75
        add("PlatinumGroupProcess.java:75", Gt6KtfruMachines.HEAT_MIXER, 510L, 600L,
                new ItemStack[] {form("dust", "AmmoniumHexachloropalladate", 4)},
                new ItemStack[] {form("dust", "Iridiumoxide", 5), form("dust", "AmmoniumChloride", 5)},
                new FluidStack[] {field("Oxygen", 12000)},
                NO_FLUIDS);
        // PurityRefining.java:73
        add("PurityRefining.java:73", Gt6KtfruMachines.HEAT_MIXER, 1024L, 1200L,
                new ItemStack[] {form("dust", "Tungsten", 4)},
                new ItemStack[] {form("dustCommercialPure", "Tungsten", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:73
        add("PurityRefining.java:73", Gt6KtfruMachines.HEAT_MIXER, 1024L, 1200L,
                new ItemStack[] {form("dust", "Molybdenum", 4)},
                new ItemStack[] {form("dustCommercialPure", "Molybdenum", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 1024L, 1200L,
                new ItemStack[] {form("dustCommercialPure", "Aluminium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Aluminium", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 1024L, 1200L,
                new ItemStack[] {form("dustCommercialPure", "Titanium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Titanium", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 1024L, 1200L,
                new ItemStack[] {form("dustCommercialPure", "Zirconium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Zirconium", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 1024L, 1200L,
                new ItemStack[] {form("dustCommercialPure", "Niobium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Niobium", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 1024L, 1200L,
                new ItemStack[] {form("dustCommercialPure", "Tantalum", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Tantalum", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 1024L, 1200L,
                new ItemStack[] {form("dustCommercialPure", "Chromium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Chromium", 3)},
                new FluidStack[] {field("GenGas_Chlorine", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "Gold", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Gold", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "Platinum", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Platinum", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "Palladium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Palladium", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "Rhodium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Rhodium", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "Iridium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Iridium", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "OsmiumElemental", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "OsmiumElemental", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "intellite", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "intellite", 3)},
                new FluidStack[] {field("Hydrogen", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 4096L, 2400L,
                new ItemStack[] {form("dustCommercialPure", "Tungsten", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Tungsten", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 4096L, 2400L,
                new ItemStack[] {form("dustCommercialPure", "Molybdenum", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Molybdenum", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "Carbon", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Carbon", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "Silicon", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Silicon", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "Germanium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Germanium", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "Gallium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Gallium", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
        // PurityRefining.java:120
        add("PurityRefining.java:120", Gt6KtfruMachines.HEAT_MIXER, 2048L, 1600L,
                new ItemStack[] {form("dustCommercialPure", "Indium", 4)},
                new ItemStack[] {form("dustAnalyticalPure", "Indium", 3)},
                new FluidStack[] {field("Argon", 250)},
                new FluidStack[] {field("Water", 1000)});
    }

    private static void eDA() {
        // ComputerBuilding.java:88
        add("ComputerBuilding.java:88", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 3600L,
                new ItemStack[] {gt("circuit_good", 1), own("tech_research_data19", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t1", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3});
        // ComputerBuilding.java:89
        add("ComputerBuilding.java:89", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 1800L,
                new ItemStack[] {gt("circuit_good", 1), own("tech_research_data19", 1), gt("circuit_part_good", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("circuit_part_photomask_t3", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2});
        // ComputerBuilding.java:91
        add("ComputerBuilding.java:91", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 1800L,
                new ItemStack[] {gt("circuit_good", 1), own("tech_research_data19", 1), form("dust", "Silicon", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("diode_photomask_t1", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2});
        // ComputerBuilding.java:93
        add("ComputerBuilding.java:93", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 7200L,
                new ItemStack[] {gt("circuit_advanced", 1), own("tech_research_data25", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t2", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3});
        // ComputerBuilding.java:94
        add("ComputerBuilding.java:94", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 3600L,
                new ItemStack[] {gt("circuit_advanced", 1), own("tech_research_data25", 1), gt("circuit_part_good", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("circuit_part_photomask_t4", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2});
        // ComputerBuilding.java:96
        add("ComputerBuilding.java:96", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 3600L,
                new ItemStack[] {gt("circuit_advanced", 1), own("tech_research_data25", 1), own("silicon_boule_pure", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("diode_photomask_t2", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2});
        // ComputerBuilding.java:98
        add("ComputerBuilding.java:98", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 14400L,
                new ItemStack[] {gt("circuit_elite", 1), own("tech_research_data29", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t3", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3});
        // ComputerBuilding.java:99
        add("ComputerBuilding.java:99", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 7200L,
                new ItemStack[] {gt("circuit_elite", 1), own("tech_research_data29", 1), gt("circuit_part_good", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("circuit_part_photomask_t5", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2});
        // ComputerBuilding.java:102
        add("ComputerBuilding.java:102", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 28800L,
                new ItemStack[] {gt("circuit_master", 1), own("tech_research_data33", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t4", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3});
        // ComputerBuilding.java:103
        add("ComputerBuilding.java:103", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 14400L,
                new ItemStack[] {gt("circuit_master", 1), own("tech_research_data33", 1), gt("circuit_part_good", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("circuit_part_photomask_t6", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2});
        // ComputerBuilding.java:106
        add("ComputerBuilding.java:106", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 28800L,
                new ItemStack[] {own("computer_gt2090", 1), own("tech_research_data33", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t5", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3});
        // ComputerBuilding.java:109
        add("ComputerBuilding.java:109", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 28800L,
                new ItemStack[] {own("computer_gt3699", 1), own("tech_research_data36", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t6", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3});
        // ComputerBuilding.java:112
        add("ComputerBuilding.java:112", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 28800L,
                new ItemStack[] {own("computer_gt3699_v2", 1), own("tech_research_data40", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t7", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3});
        // ComputerBuilding.java:115
        add("ComputerBuilding.java:115", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 28800L,
                new ItemStack[] {own("computer_gt3699_v3", 1), own("tech_research_data43", 1), gt("co2_laser_mv", 1), gt("electric_dynamo_mv", 1), own("tech_research_data40", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("cpu_photomask_t8", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2, 3, 4});
        // ComputerBuilding.java:118
        add("ComputerBuilding.java:118", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 28800L,
                new ItemStack[] {own("computer_gt3699_v3", 1), own("tech_research_data40", 1), form("wireFine", "Platinum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("inter_layer_photomask_t1", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2});
        // ComputerBuilding.java:119
        add("ComputerBuilding.java:119", Gt6KtfruMachines.ELECTRONICS_DESIGNER, 16L, 21600L,
                new ItemStack[] {own("computer_gt3699_v4", 1), own("tech_research_data43", 1), form("wireFine", "Platinum", 1), form("paneGlass", "Black", 1)},
                new ItemStack[] {own("inter_layer_photomask_t2", 1)},
                new FluidStack[] {field("GenGas_HydrogenFluoride", 10)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0, 1, 2});
    }

    private static void flawDetector() {
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "Magnalium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Magnalium", 1), form("turbineLargeSteamDamaged", "Magnalium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "Trinitanium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Trinitanium", 1), form("turbineLargeSteamDamaged", "Trinitanium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "Graphene", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Graphene", 1), form("turbineLargeSteamDamaged", "Graphene", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "Vibramantium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Vibramantium", 1), form("turbineLargeSteamDamaged", "Vibramantium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "Trinaquadalloy", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Trinaquadalloy", 1), form("turbineLargeSteamDamaged", "Trinaquadalloy", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "Steel", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Steel", 1), form("turbineLargeSteamDamaged", "Steel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "StainlessSteel", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "StainlessSteel", 1), form("turbineLargeSteamDamaged", "StainlessSteel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "Titanium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Titanium", 1), form("turbineLargeSteamDamaged", "Titanium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "Tungstensteel", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Tungstensteel", 1), form("turbineLargeSteamDamaged", "Tungstensteel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "Adamantium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Adamantium", 1), form("turbineLargeSteamDamaged", "Adamantium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "Iridium", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "Iridium", 1), form("turbineLargeSteamDamaged", "Iridium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:52
        add("recipePrefixItems.java:52", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 12000L,
                new ItemStack[] {form("turbineLargeSteam", "OsmiumElemental", 1)},
                new ItemStack[] {form("turbineLargeSteamChecked", "OsmiumElemental", 1), form("turbineLargeSteamDamaged", "OsmiumElemental", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "Magnalium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Magnalium", 1), form("turbineLargeGasDamaged", "Magnalium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "Trinitanium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Trinitanium", 1), form("turbineLargeGasDamaged", "Trinitanium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "Graphene", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Graphene", 1), form("turbineLargeGasDamaged", "Graphene", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "Vibramantium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Vibramantium", 1), form("turbineLargeGasDamaged", "Vibramantium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "Trinaquadalloy", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Trinaquadalloy", 1), form("turbineLargeGasDamaged", "Trinaquadalloy", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "Steel", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Steel", 1), form("turbineLargeGasDamaged", "Steel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "StainlessSteel", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "StainlessSteel", 1), form("turbineLargeGasDamaged", "StainlessSteel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "Titanium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Titanium", 1), form("turbineLargeGasDamaged", "Titanium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "Tungstensteel", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Tungstensteel", 1), form("turbineLargeGasDamaged", "Tungstensteel", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "Adamantium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Adamantium", 1), form("turbineLargeGasDamaged", "Adamantium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "Iridium", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "Iridium", 1), form("turbineLargeGasDamaged", "Iridium", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
        // recipePrefixItems.java:53
        add("recipePrefixItems.java:53", Gt6KtfruMachines.FLAW_DETECTOR, 16L, 18000L,
                new ItemStack[] {form("turbineLargeGas", "OsmiumElemental", 1)},
                new ItemStack[] {form("turbineLargeGasChecked", "OsmiumElemental", 1), form("turbineLargeGasDamaged", "OsmiumElemental", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {6000, 4000},
                null);
    }

    private static void waferTester() {
        // ComputerBuilding.java:337
        add("ComputerBuilding.java:337", Gt6KtfruMachines.WAFER_TESTER, 60L, 400L,
                new ItemStack[] {own("cpu_wafer_t1_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t1_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:338
        add("ComputerBuilding.java:338", Gt6KtfruMachines.WAFER_TESTER, 150L, 400L,
                new ItemStack[] {own("cpu_wafer_t2_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t2_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:339
        add("ComputerBuilding.java:339", Gt6KtfruMachines.WAFER_TESTER, 250L, 400L,
                new ItemStack[] {own("cpu_wafer_t3_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t3_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:340
        add("ComputerBuilding.java:340", Gt6KtfruMachines.WAFER_TESTER, 800L, 800L,
                new ItemStack[] {own("cpu_wafer_t4_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t4_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:341
        add("ComputerBuilding.java:341", Gt6KtfruMachines.WAFER_TESTER, 3600L, 9500L,
                new ItemStack[] {own("cpu_wafer_t5_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t5_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:342
        add("ComputerBuilding.java:342", Gt6KtfruMachines.WAFER_TESTER, 3600L, 13200L,
                new ItemStack[] {own("cpu_wafer_t6_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t6_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:343
        add("ComputerBuilding.java:343", Gt6KtfruMachines.WAFER_TESTER, 3600L, 16700L,
                new ItemStack[] {own("cpu_wafer_t7_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t7_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:344
        add("ComputerBuilding.java:344", Gt6KtfruMachines.WAFER_TESTER, 3600L, 18800L,
                new ItemStack[] {own("cpu_wafer_t8_doped", 1)},
                new ItemStack[] {own("cpu_wafer_t8_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:346
        add("ComputerBuilding.java:346", Gt6KtfruMachines.WAFER_TESTER, 52L, 400L,
                new ItemStack[] {own("ram_wafer_t1_doped", 1)},
                new ItemStack[] {own("ram_wafer_t1_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:347
        add("ComputerBuilding.java:347", Gt6KtfruMachines.WAFER_TESTER, 140L, 400L,
                new ItemStack[] {own("ram_wafer_t2_doped", 1)},
                new ItemStack[] {own("ram_wafer_t2_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:348
        add("ComputerBuilding.java:348", Gt6KtfruMachines.WAFER_TESTER, 210L, 400L,
                new ItemStack[] {own("ram_wafer_t3_doped", 1)},
                new ItemStack[] {own("ram_wafer_t3_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:349
        add("ComputerBuilding.java:349", Gt6KtfruMachines.WAFER_TESTER, 670L, 500L,
                new ItemStack[] {own("ram_wafer_t4_doped", 1)},
                new ItemStack[] {own("ram_wafer_t4_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:350
        add("ComputerBuilding.java:350", Gt6KtfruMachines.WAFER_TESTER, 1500L, 1500L,
                new ItemStack[] {own("ram_wafer_t5_doped", 1)},
                new ItemStack[] {own("ram_wafer_t5_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:351
        add("ComputerBuilding.java:351", Gt6KtfruMachines.WAFER_TESTER, 1500L, 2000L,
                new ItemStack[] {own("ram_wafer_t6_doped", 1)},
                new ItemStack[] {own("ram_wafer_t6_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:352
        add("ComputerBuilding.java:352", Gt6KtfruMachines.WAFER_TESTER, 1500L, 2500L,
                new ItemStack[] {own("ram_wafer_t7_doped", 1)},
                new ItemStack[] {own("ram_wafer_t7_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:353
        add("ComputerBuilding.java:353", Gt6KtfruMachines.WAFER_TESTER, 1500L, 3000L,
                new ItemStack[] {own("ram_wafer_t8_doped", 1)},
                new ItemStack[] {own("ram_wafer_t8_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:355
        add("ComputerBuilding.java:355", Gt6KtfruMachines.WAFER_TESTER, 46L, 400L,
                new ItemStack[] {own("circuit_part_wafer_t3_doped", 1)},
                new ItemStack[] {own("circuit_part_wafer_t3_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:356
        add("ComputerBuilding.java:356", Gt6KtfruMachines.WAFER_TESTER, 120L, 400L,
                new ItemStack[] {own("circuit_part_wafer_t4_doped", 1)},
                new ItemStack[] {own("circuit_part_wafer_t4_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:357
        add("ComputerBuilding.java:357", Gt6KtfruMachines.WAFER_TESTER, 200L, 400L,
                new ItemStack[] {own("circuit_part_wafer_t5_doped", 1)},
                new ItemStack[] {own("circuit_part_wafer_t5_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:358
        add("ComputerBuilding.java:358", Gt6KtfruMachines.WAFER_TESTER, 660L, 1000L,
                new ItemStack[] {own("circuit_part_wafer_t6_doped", 1)},
                new ItemStack[] {own("circuit_part_wafer_t6_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:360
        add("ComputerBuilding.java:360", Gt6KtfruMachines.WAFER_TESTER, 900L, 4000L,
                new ItemStack[] {own("inter_layer_wafer_t7_doped", 1)},
                new ItemStack[] {own("inter_layer_wafer_t7_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:361
        add("ComputerBuilding.java:361", Gt6KtfruMachines.WAFER_TESTER, 900L, 5000L,
                new ItemStack[] {own("inter_layer_wafer_t8_doped", 1)},
                new ItemStack[] {own("inter_layer_wafer_t8_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:363
        add("ComputerBuilding.java:363", Gt6KtfruMachines.WAFER_TESTER, 120L, 400L,
                new ItemStack[] {own("diode_wafer_t1_doped", 1)},
                new ItemStack[] {own("diode_wafer_t1_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // ComputerBuilding.java:364
        add("ComputerBuilding.java:364", Gt6KtfruMachines.WAFER_TESTER, 200L, 400L,
                new ItemStack[] {own("diode_wafer_t2_doped", 1)},
                new ItemStack[] {own("diode_wafer_t2_checked", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void waferCoater() {
        // ComputerBuilding.java:198
        add("ComputerBuilding.java:198", Gt6KtfruMachines.WAFER_COATER, 30L, 200L,
                new ItemStack[] {own("silicon_plate_t1", 1)},
                new ItemStack[] {own("silicon_plate_coated_t1", 1)},
                new FluidStack[] {field("GenLiquid_DNQPhotoresist", 100)},
                NO_FLUIDS);
        // ComputerBuilding.java:199
        add("ComputerBuilding.java:199", Gt6KtfruMachines.WAFER_COATER, 260L, 200L,
                new ItemStack[] {own("silicon_plate_oxidized_t2", 1)},
                new ItemStack[] {own("silicon_plate_coated_t2", 1)},
                new FluidStack[] {field("GenLiquid_DNQPhotoresist", 100)},
                NO_FLUIDS);
        // ComputerBuilding.java:201
        add("ComputerBuilding.java:201", Gt6KtfruMachines.WAFER_COATER, 2050L, 200L,
                new ItemStack[] {own("mo_o2_plate_oxidized_t1", 1)},
                new ItemStack[] {own("mo_o2_plate_coated_t1", 1)},
                new FluidStack[] {field("GenLiquid_PMMAPhotoresist", 100)},
                NO_FLUIDS);
        // ComputerBuilding.java:202
        add("ComputerBuilding.java:202", Gt6KtfruMachines.WAFER_COATER, 8200L, 200L,
                new ItemStack[] {own("mo_o2_plate_oxidized_t2", 1)},
                new ItemStack[] {own("mo_o2_plate_coated_t2", 1)},
                new FluidStack[] {field("GenLiquid_PMMAPhotoresist", 100)},
                NO_FLUIDS);
    }

    private static void ultrasonicMixer() {
        // Chemistry.java:127
        add("Chemistry.java:127", Gt6KtfruMachines.ULTRASONIC_MIXER, 800L, 3600L,
                new ItemStack[] {circuit(0, 1)},
                new ItemStack[] {form("dust", "Graphite", 6), form("foil", "Graphene", 4)},
                new FluidStack[] {field("GenLiquid_Graphite_Methylpyrrolidone", 4000)},
                new FluidStack[] {field("GenLiquid_Methylpyrrolidone", 4000)});
        // Chemistry.java:128
        add("Chemistry.java:128", Gt6KtfruMachines.ULTRASONIC_MIXER, 600L, 18000L,
                new ItemStack[] {circuit(1, 1)},
                new ItemStack[] {form("dust", "Graphite", 24), form("plate", "Graphene", 4)},
                new FluidStack[] {field("GenLiquid_Graphite_Methylpyrrolidone", 16000)},
                new FluidStack[] {field("GenLiquid_Methylpyrrolidone", 16000)});
        // Chemistry.java:139
        add("Chemistry.java:139", Gt6KtfruMachines.ULTRASONIC_MIXER, 800L, 80L,
                new ItemStack[] {form("dust", "Barite", 4)},
                new ItemStack[] {form("dust", "Barite", 3)},
                new FluidStack[] {field("DistW", 1000)},
                new FluidStack[] {field("GenLiquid_BariumSulfate", 1000)});
    }

    private static void tinyDistillTower() {
        // OilProcessing.java:56
        add("OilProcessing.java:56", Gt6KtfruMachines.TINY_DISTILL, 128L, 100L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilLight", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 400), field("GenLiquid_Naphtha", 300), field("GenGas_OilGas", 200)});
        // OilProcessing.java:57
        add("OilProcessing.java:57", Gt6KtfruMachines.TINY_DISTILL, 128L, 100L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilMedium", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 425), field("GenLiquid_Naphtha", 325), field("GenGas_OilGas", 175)});
        // OilProcessing.java:58
        add("OilProcessing.java:58", Gt6KtfruMachines.TINY_DISTILL, 128L, 100L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilNormal", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 450), field("GenLiquid_Naphtha", 350), field("GenGas_OilGas", 150)});
        // OilProcessing.java:59
        add("OilProcessing.java:59", Gt6KtfruMachines.TINY_DISTILL, 128L, 100L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 475), field("GenLiquid_Naphtha", 375), field("GenGas_OilGas", 125)});
        // OilProcessing.java:60
        add("OilProcessing.java:60", Gt6KtfruMachines.TINY_DISTILL, 128L, 100L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilExtraHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 500), field("GenLiquid_Naphtha", 400), field("GenGas_OilGas", 100)});
    }

    private static void smallDistillTower() {
        // OilProcessing.java:62
        add("OilProcessing.java:62", Gt6KtfruMachines.SMALL_DISTILL, 128L, 80L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilLight", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 400), field("GenLiquid_Naphtha", 300), field("GenGas_OilGas", 200)});
        // OilProcessing.java:63
        add("OilProcessing.java:63", Gt6KtfruMachines.SMALL_DISTILL, 128L, 80L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilMedium", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 425), field("GenLiquid_Naphtha", 325), field("GenGas_OilGas", 175)});
        // OilProcessing.java:64
        add("OilProcessing.java:64", Gt6KtfruMachines.SMALL_DISTILL, 128L, 80L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilNormal", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 450), field("GenLiquid_Naphtha", 350), field("GenGas_OilGas", 150)});
        // OilProcessing.java:65
        add("OilProcessing.java:65", Gt6KtfruMachines.SMALL_DISTILL, 128L, 80L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 475), field("GenLiquid_Naphtha", 375), field("GenGas_OilGas", 125)});
        // OilProcessing.java:66
        add("OilProcessing.java:66", Gt6KtfruMachines.SMALL_DISTILL, 128L, 80L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilExtraHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 500), field("GenLiquid_Naphtha", 400), field("GenGas_OilGas", 100)});
        // OilProcessing.java:74
        add("OilProcessing.java:74", Gt6KtfruMachines.SMALL_DISTILL, 196L, 120L,
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "OilScarp", 1)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 400)},
                new FluidStack[] {field("Diesel", 120), field("Kerosine", 200), field("GenLiquid_Naphtha", 80), field("Petrol", 320), field("GenGas_OilGas", 100)});
        // OilProcessing.java:132
        add("OilProcessing.java:132", Gt6KtfruMachines.SMALL_DISTILL, 256L, 60L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_OilGas", 1000)},
                new FluidStack[] {field("Methane", 372), field("GenLiquid_Ethane", 262), field("Propane", 188), field("Butane", 138), field("CarbonDioxide", 40)});
        // OilProcessing.java:170
        add("OilProcessing.java:170", Gt6KtfruMachines.SMALL_DISTILL, 120L, 20L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaLow", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 121), field("GenLiquid_Toluene", 30), field("Petrol", 600), field("Methane", 212), field("GenLiquid_Ethane", 182), field("Hydrogen", 882)});
        // OilProcessing.java:171
        add("OilProcessing.java:171", Gt6KtfruMachines.SMALL_DISTILL, 120L, 20L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaMedium", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 151), field("GenLiquid_Toluene", 43), field("Petrol", 500), field("Methane", 342), field("GenLiquid_Ethane", 282), field("Hydrogen", 1282)});
        // OilProcessing.java:172
        add("OilProcessing.java:172", Gt6KtfruMachines.SMALL_DISTILL, 120L, 20L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaHigh", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 191), field("GenLiquid_Toluene", 50), field("Petrol", 300), field("Methane", 602), field("GenLiquid_Ethane", 352), field("Hydrogen", 1682)});
        // OilProcessing.java:180
        add("OilProcessing.java:180", Gt6KtfruMachines.SMALL_DISTILL, 120L, 20L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolLow", 1000)},
                new FluidStack[] {field("Lubricant", 218), field("Petrol", 542), field("GenGas_OilGas", 118), field("GenGas_CrackedOilGas", 242)});
        // OilProcessing.java:181
        add("OilProcessing.java:181", Gt6KtfruMachines.SMALL_DISTILL, 120L, 20L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolMedium", 1000)},
                new FluidStack[] {field("Lubricant", 162), field("Petrol", 423), field("GenGas_OilGas", 247), field("GenGas_CrackedOilGas", 392)});
        // OilProcessing.java:182
        add("OilProcessing.java:182", Gt6KtfruMachines.SMALL_DISTILL, 120L, 20L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolHigh", 1000)},
                new FluidStack[] {field("Lubricant", 92), field("Petrol", 321), field("GenGas_OilGas", 301), field("GenGas_CrackedOilGas", 542)});
    }

    private static void distillTower() {
        // OilProcessing.java:68
        add("OilProcessing.java:68", Gt6KtfruMachines.DISTILL, 128L, 65L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilLight", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 400), field("GenLiquid_Naphtha", 300), field("GenGas_OilGas", 200)});
        // OilProcessing.java:69
        add("OilProcessing.java:69", Gt6KtfruMachines.DISTILL, 128L, 65L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilMedium", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 425), field("GenLiquid_Naphtha", 325), field("GenGas_OilGas", 175)});
        // OilProcessing.java:70
        add("OilProcessing.java:70", Gt6KtfruMachines.DISTILL, 128L, 65L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilNormal", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 450), field("GenLiquid_Naphtha", 350), field("GenGas_OilGas", 150)});
        // OilProcessing.java:71
        add("OilProcessing.java:71", Gt6KtfruMachines.DISTILL, 128L, 65L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 475), field("GenLiquid_Naphtha", 375), field("GenGas_OilGas", 125)});
        // OilProcessing.java:72
        add("OilProcessing.java:72", Gt6KtfruMachines.DISTILL, 128L, 65L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CleanedOilExtraHeavy", 800)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 500), field("GenLiquid_Naphtha", 400), field("GenGas_OilGas", 100)});
        // OilProcessing.java:75
        add("OilProcessing.java:75", Gt6KtfruMachines.DISTILL, 196L, 95L,
                NO_ITEMS,
                new ItemStack[] {form("dustSmall", "OilScarp", 1)},
                new FluidStack[] {field("GenLiquid_InitalBottomOil", 400)},
                new FluidStack[] {field("Diesel", 120), field("Kerosine", 200), field("GenLiquid_Naphtha", 80), field("Petrol", 320), field("GenGas_OilGas", 100)});
        // OilProcessing.java:80
        add("OilProcessing.java:80", Gt6KtfruMachines.DISTILL, 64L, 16L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Biomass", 80)},
                new FluidStack[] {field("Reikanol", 20), field("GenLiquid_Glycerol", 20), field("Methane", 4), field("DistW", 50)});
        // OilProcessing.java:83
        add("OilProcessing.java:83", Gt6KtfruMachines.DISTILL, 64L, 16L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("BiomassIC2", 80)},
                new FluidStack[] {field("Reikanol", 20), field("GenLiquid_Glycerol", 20), field("Methane", 4), field("DistW", 50)});
        // OilProcessing.java:133
        add("OilProcessing.java:133", Gt6KtfruMachines.DISTILL, 256L, 40L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_OilGas", 1000)},
                new FluidStack[] {field("Methane", 372), field("GenLiquid_Ethane", 262), field("Propane", 188), field("Butane", 138), field("CarbonDioxide", 40)});
        // OilProcessing.java:135
        add("OilProcessing.java:135", Gt6KtfruMachines.DISTILL, 256L, 40L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_CrackedOilGas", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 42), field("GenLiquid_Toluene", 8), field("GenLiquid_Phenol", 11), field("Methane", 183), field("GenLiquid_Ethane", 152), field("Propane", 133), field("Butane", 128), field("GenGas_Butadiene", 134), field("CarbonDioxide", 134)});
        // OilProcessing.java:173
        add("OilProcessing.java:173", Gt6KtfruMachines.DISTILL, 120L, 10L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaLow", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 121), field("GenLiquid_Toluene", 30), field("Petrol", 600), field("Methane", 212), field("GenLiquid_Ethane", 182), field("Hydrogen", 882)});
        // OilProcessing.java:174
        add("OilProcessing.java:174", Gt6KtfruMachines.DISTILL, 120L, 10L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaMedium", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 151), field("GenLiquid_Toluene", 43), field("Petrol", 500), field("Methane", 342), field("GenLiquid_Ethane", 282), field("Hydrogen", 1282)});
        // OilProcessing.java:175
        add("OilProcessing.java:175", Gt6KtfruMachines.DISTILL, 120L, 10L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedNaphthaHigh", 1000)},
                new FluidStack[] {field("GenLiquid_Benzene", 191), field("GenLiquid_Toluene", 50), field("Petrol", 300), field("Methane", 602), field("GenLiquid_Ethane", 352), field("Hydrogen", 1682)});
        // OilProcessing.java:183
        add("OilProcessing.java:183", Gt6KtfruMachines.DISTILL, 120L, 10L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolLow", 1000)},
                new FluidStack[] {field("Lubricant", 218), field("Petrol", 542), field("GenGas_OilGas", 118), field("GenGas_CrackedOilGas", 242)});
        // OilProcessing.java:184
        add("OilProcessing.java:184", Gt6KtfruMachines.DISTILL, 120L, 10L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolMedium", 1000)},
                new FluidStack[] {field("Lubricant", 162), field("Petrol", 423), field("GenGas_OilGas", 247), field("GenGas_CrackedOilGas", 392)});
        // OilProcessing.java:185
        add("OilProcessing.java:185", Gt6KtfruMachines.DISTILL, 120L, 10L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_CrackedPetrolHigh", 1000)},
                new FluidStack[] {field("Lubricant", 92), field("Petrol", 321), field("GenGas_OilGas", 301), field("GenGas_CrackedOilGas", 542)});
    }

    private static void maskAligner() {
        // ComputerBuilding.java:213
        add("ComputerBuilding.java:213", Gt6KtfruMachines.MASK_ALIGNER, 128L, 3000L,
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("cpu_photomask_t1", 1)},
                new ItemStack[] {own("cpu_wafer_t1", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:214
        add("ComputerBuilding.java:214", Gt6KtfruMachines.MASK_ALIGNER, 256L, 3000L,
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("cpu_photomask_t2", 1)},
                new ItemStack[] {own("cpu_wafer_t2", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:215
        add("ComputerBuilding.java:215", Gt6KtfruMachines.MASK_ALIGNER, 256L, 16000L,
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("cpu_photomask_t3", 1)},
                new ItemStack[] {own("cpu_wafer_t3", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:216
        add("ComputerBuilding.java:216", Gt6KtfruMachines.MASK_ALIGNER, 512L, 16000L,
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("cpu_photomask_t4", 1)},
                new ItemStack[] {own("cpu_wafer_t4", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:217
        add("ComputerBuilding.java:217", Gt6KtfruMachines.MASK_ALIGNER, 1024L, 24000L,
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("cpu_photomask_t5", 1)},
                new ItemStack[] {own("cpu_wafer_t5", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:218
        add("ComputerBuilding.java:218", Gt6KtfruMachines.MASK_ALIGNER, 2048L, 48000L,
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("cpu_photomask_t6", 1)},
                new ItemStack[] {own("cpu_wafer_t6", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:219
        add("ComputerBuilding.java:219", Gt6KtfruMachines.MASK_ALIGNER, 4096L, 48000L,
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("cpu_photomask_t7", 1)},
                new ItemStack[] {own("cpu_wafer_t7", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:220
        add("ComputerBuilding.java:220", Gt6KtfruMachines.MASK_ALIGNER, 8192L, 64000L,
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("cpu_photomask_t8", 1)},
                new ItemStack[] {own("cpu_wafer_t8", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:222
        add("ComputerBuilding.java:222", Gt6KtfruMachines.MASK_ALIGNER, 100L, 2000L,
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("ram_photomask_t1", 1)},
                new ItemStack[] {own("ram_wafer_t1", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:223
        add("ComputerBuilding.java:223", Gt6KtfruMachines.MASK_ALIGNER, 240L, 2000L,
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("ram_photomask_t2", 1)},
                new ItemStack[] {own("ram_wafer_t2", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:224
        add("ComputerBuilding.java:224", Gt6KtfruMachines.MASK_ALIGNER, 242L, 10000L,
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("ram_photomask_t3", 1)},
                new ItemStack[] {own("ram_wafer_t3", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:225
        add("ComputerBuilding.java:225", Gt6KtfruMachines.MASK_ALIGNER, 500L, 10000L,
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("ram_photomask_t4", 1)},
                new ItemStack[] {own("ram_wafer_t4", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:226
        add("ComputerBuilding.java:226", Gt6KtfruMachines.MASK_ALIGNER, 980L, 14000L,
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("ram_photomask_t5", 1)},
                new ItemStack[] {own("ram_wafer_t5", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:227
        add("ComputerBuilding.java:227", Gt6KtfruMachines.MASK_ALIGNER, 1510L, 24000L,
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("ram_photomask_t6", 1)},
                new ItemStack[] {own("ram_wafer_t6", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:228
        add("ComputerBuilding.java:228", Gt6KtfruMachines.MASK_ALIGNER, 3070L, 24000L,
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("ram_photomask_t7", 1)},
                new ItemStack[] {own("ram_wafer_t7", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:229
        add("ComputerBuilding.java:229", Gt6KtfruMachines.MASK_ALIGNER, 7092L, 32000L,
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("ram_photomask_t8", 1)},
                new ItemStack[] {own("ram_wafer_t8", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:231
        add("ComputerBuilding.java:231", Gt6KtfruMachines.MASK_ALIGNER, 120L, 2000L,
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("circuit_part_photomask_t3", 1)},
                new ItemStack[] {own("circuit_part_wafer_t3", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:232
        add("ComputerBuilding.java:232", Gt6KtfruMachines.MASK_ALIGNER, 180L, 2000L,
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("circuit_part_photomask_t4", 1)},
                new ItemStack[] {own("circuit_part_wafer_t4", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:233
        add("ComputerBuilding.java:233", Gt6KtfruMachines.MASK_ALIGNER, 546L, 9000L,
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("circuit_part_photomask_t5", 1)},
                new ItemStack[] {own("circuit_part_wafer_t5", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:234
        add("ComputerBuilding.java:234", Gt6KtfruMachines.MASK_ALIGNER, 842L, 9000L,
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("circuit_part_photomask_t6", 1)},
                new ItemStack[] {own("circuit_part_wafer_t6", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:236
        add("ComputerBuilding.java:236", Gt6KtfruMachines.MASK_ALIGNER, 4096L, 12000L,
                new ItemStack[] {own("mo_o2_plate_soft_baked_t1", 1), own("inter_layer_photomask_t1", 1)},
                new ItemStack[] {own("inter_layer_wafer_t7", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:237
        add("ComputerBuilding.java:237", Gt6KtfruMachines.MASK_ALIGNER, 6144L, 12000L,
                new ItemStack[] {own("mo_o2_plate_soft_baked_t2", 1), own("inter_layer_photomask_t2", 1)},
                new ItemStack[] {own("inter_layer_wafer_t8", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:239
        add("ComputerBuilding.java:239", Gt6KtfruMachines.MASK_ALIGNER, 180L, 2000L,
                new ItemStack[] {own("silicon_plate_coated_t1", 1), own("diode_photomask_t1", 1)},
                new ItemStack[] {own("diode_wafer_t1", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
        // ComputerBuilding.java:240
        add("ComputerBuilding.java:240", Gt6KtfruMachines.MASK_ALIGNER, 842L, 9000L,
                new ItemStack[] {own("silicon_plate_soft_baked_t2", 1), own("diode_photomask_t2", 1)},
                new ItemStack[] {own("diode_wafer_t2", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                null,
                new int[] {1});
    }

    private static void cNC() {
        // CompactItem.java:64
        add("CompactItem.java:64", Gt6KtfruMachines.CNC, 65L, 600L,
                new ItemStack[] {form("stickLong", "Bronze", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft1", 1), form("dust", "Bronze", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:65
        add("CompactItem.java:65", Gt6KtfruMachines.CNC, 65L, 200L,
                new ItemStack[] {form("ingotDouble", "Bronze", 1), form("plateCurved", "Bronze", 1), form("stick", "Bronze", 1)},
                new ItemStack[] {own("engine_cylinder1", 1), form("dustSmall", "Bronze", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:66
        add("CompactItem.java:66", Gt6KtfruMachines.CNC, 65L, 200L,
                new ItemStack[] {form("rotor", "Bronze", 1), form("plateCurved", "Bronze", 2), form("plate", "Bronze", 1)},
                new ItemStack[] {own("engine_turbo1", 1), form("dustSmall", "Bronze", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:73
        add("CompactItem.java:73", Gt6KtfruMachines.CNC, 65L, 600L,
                new ItemStack[] {form("stickLong", "ArsenicCopper", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft2", 1), form("dust", "ArsenicCopper", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:74
        add("CompactItem.java:74", Gt6KtfruMachines.CNC, 65L, 200L,
                new ItemStack[] {form("ingotDouble", "ArsenicCopper", 1), form("plateCurved", "ArsenicCopper", 1), form("stick", "ArsenicCopper", 1)},
                new ItemStack[] {own("engine_cylinder2", 1), form("dustSmall", "ArsenicCopper", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:75
        add("CompactItem.java:75", Gt6KtfruMachines.CNC, 65L, 200L,
                new ItemStack[] {form("rotor", "ArsenicCopper", 1), form("plateCurved", "ArsenicCopper", 2), form("plate", "ArsenicCopper", 1)},
                new ItemStack[] {own("engine_turbo2", 1), form("dustSmall", "ArsenicCopper", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:82
        add("CompactItem.java:82", Gt6KtfruMachines.CNC, 65L, 600L,
                new ItemStack[] {form("stickLong", "ArsenicBronze", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft3", 1), form("dust", "ArsenicBronze", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:83
        add("CompactItem.java:83", Gt6KtfruMachines.CNC, 65L, 200L,
                new ItemStack[] {form("ingotDouble", "ArsenicBronze", 1), form("plateCurved", "ArsenicBronze", 1), form("stick", "ArsenicBronze", 1)},
                new ItemStack[] {own("engine_cylinder3", 1), form("dustSmall", "ArsenicBronze", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:84
        add("CompactItem.java:84", Gt6KtfruMachines.CNC, 65L, 200L,
                new ItemStack[] {form("rotor", "ArsenicBronze", 1), form("plateCurved", "ArsenicBronze", 2), form("plate", "ArsenicBronze", 1)},
                new ItemStack[] {own("engine_turbo3", 1), form("dustSmall", "ArsenicBronze", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:91
        add("CompactItem.java:91", Gt6KtfruMachines.CNC, 96L, 600L,
                new ItemStack[] {form("stickLong", "Steel", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft4", 1), form("dust", "Steel", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:92
        add("CompactItem.java:92", Gt6KtfruMachines.CNC, 96L, 200L,
                new ItemStack[] {form("ingotDouble", "Steel", 1), form("plateCurved", "Steel", 1), form("stick", "Steel", 1)},
                new ItemStack[] {own("engine_cylinder4", 1), form("dustSmall", "Steel", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:93
        add("CompactItem.java:93", Gt6KtfruMachines.CNC, 96L, 200L,
                new ItemStack[] {form("rotor", "Steel", 1), form("plateCurved", "Steel", 2), form("plate", "Steel", 1)},
                new ItemStack[] {own("engine_turbo4", 1), form("dustSmall", "Steel", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:100
        add("CompactItem.java:100", Gt6KtfruMachines.CNC, 128L, 600L,
                new ItemStack[] {form("stickLong", "Invar", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft5", 1), form("dust", "Invar", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:101
        add("CompactItem.java:101", Gt6KtfruMachines.CNC, 128L, 200L,
                new ItemStack[] {form("ingotDouble", "Invar", 1), form("plateCurved", "Invar", 1), form("stick", "Invar", 1)},
                new ItemStack[] {own("engine_cylinder5", 1), form("dustSmall", "Invar", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:102
        add("CompactItem.java:102", Gt6KtfruMachines.CNC, 128L, 200L,
                new ItemStack[] {form("rotor", "Invar", 1), form("plateCurved", "Invar", 2), form("plate", "Invar", 1)},
                new ItemStack[] {own("engine_turbo5", 1), form("dustSmall", "Invar", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:110
        add("CompactItem.java:110", Gt6KtfruMachines.CNC, 172L, 600L,
                new ItemStack[] {form("stickLong", "Titanium", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft6", 1), form("dust", "Titanium", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:111
        add("CompactItem.java:111", Gt6KtfruMachines.CNC, 172L, 200L,
                new ItemStack[] {form("ingotDouble", "Titanium", 1), form("plateCurved", "Titanium", 1), form("stick", "Titanium", 1)},
                new ItemStack[] {own("engine_cylinder6", 1), form("dustSmall", "Titanium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:112
        add("CompactItem.java:112", Gt6KtfruMachines.CNC, 172L, 200L,
                new ItemStack[] {form("rotor", "Titanium", 1), form("plateCurved", "Titanium", 2), form("plate", "Titanium", 1)},
                new ItemStack[] {own("engine_turbo6", 1), form("dustSmall", "Titanium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:119
        add("CompactItem.java:119", Gt6KtfruMachines.CNC, 256L, 600L,
                new ItemStack[] {form("stickLong", "Tungstensteel", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft7", 1), form("dust", "Tungstensteel", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:120
        add("CompactItem.java:120", Gt6KtfruMachines.CNC, 256L, 200L,
                new ItemStack[] {form("ingotDouble", "Tungstensteel", 1), form("plateCurved", "Tungstensteel", 1), form("stick", "Tungstensteel", 1)},
                new ItemStack[] {own("engine_cylinder7", 1), form("dustSmall", "Tungstensteel", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:121
        add("CompactItem.java:121", Gt6KtfruMachines.CNC, 256L, 200L,
                new ItemStack[] {form("rotor", "Tungstensteel", 1), form("plateCurved", "Tungstensteel", 2), form("plate", "Tungstensteel", 1)},
                new ItemStack[] {own("engine_turbo7", 1), form("dustSmall", "Tungstensteel", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:128
        add("CompactItem.java:128", Gt6KtfruMachines.CNC, 256L, 600L,
                new ItemStack[] {form("stickLong", "Iridium", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft8", 1), form("dust", "Iridium", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:129
        add("CompactItem.java:129", Gt6KtfruMachines.CNC, 256L, 200L,
                new ItemStack[] {form("ingotDouble", "Iridium", 1), form("plateCurved", "Iridium", 1), form("stick", "Iridium", 1)},
                new ItemStack[] {own("engine_cylinder8", 1), form("dustSmall", "Iridium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:130
        add("CompactItem.java:130", Gt6KtfruMachines.CNC, 256L, 200L,
                new ItemStack[] {form("rotor", "Iridium", 1), form("plateCurved", "Iridium", 2), form("plate", "Iridium", 1)},
                new ItemStack[] {own("engine_turbo8", 1), form("dustSmall", "Iridium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:137
        add("CompactItem.java:137", Gt6KtfruMachines.CNC, 256L, 600L,
                new ItemStack[] {form("stickLong", "Chromium", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft_cr", 1), form("dust", "Chromium", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:138
        add("CompactItem.java:138", Gt6KtfruMachines.CNC, 256L, 200L,
                new ItemStack[] {form("ingotDouble", "Chromium", 1), form("plateCurved", "Chromium", 1), form("stick", "Chromium", 1)},
                new ItemStack[] {own("engine_cylinder_cr", 1), form("dustSmall", "Chromium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:139
        add("CompactItem.java:139", Gt6KtfruMachines.CNC, 256L, 200L,
                new ItemStack[] {form("rotor", "Chromium", 1), form("plateCurved", "Chromium", 2), form("plate", "Chromium", 1)},
                new ItemStack[] {own("engine_turbo_cr", 1), form("dustSmall", "Chromium", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:145
        add("CompactItem.java:145", Gt6KtfruMachines.CNC, 256L, 600L,
                new ItemStack[] {form("stickLong", "OsmiumElemental", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft_os", 1), form("dust", "OsmiumElemental", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:146
        add("CompactItem.java:146", Gt6KtfruMachines.CNC, 256L, 200L,
                new ItemStack[] {form("ingotDouble", "OsmiumElemental", 1), form("plateCurved", "OsmiumElemental", 1), form("stick", "OsmiumElemental", 1)},
                new ItemStack[] {own("engine_cylinder_os", 1), form("dustSmall", "OsmiumElemental", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:147
        add("CompactItem.java:147", Gt6KtfruMachines.CNC, 256L, 200L,
                new ItemStack[] {form("rotor", "OsmiumElemental", 1), form("plateCurved", "OsmiumElemental", 2), form("plate", "OsmiumElemental", 1)},
                new ItemStack[] {own("engine_turbo_os", 1), form("dustSmall", "OsmiumElemental", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:150
        add("CompactItem.java:150", Gt6KtfruMachines.CNC, 256L, 600L,
                new ItemStack[] {form("stickLong", "Trinaquadalloy", 3), circuit(0, 1)},
                new ItemStack[] {own("engine_crank_shaft9", 1), form("dust", "Trinaquadalloy", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:151
        add("CompactItem.java:151", Gt6KtfruMachines.CNC, 256L, 200L,
                new ItemStack[] {form("ingotDouble", "Trinaquadalloy", 1), form("plateCurved", "Trinaquadalloy", 1), form("stick", "Trinaquadalloy", 1)},
                new ItemStack[] {own("engine_cylinder9", 1), form("dustSmall", "Trinaquadalloy", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // CompactItem.java:152
        add("CompactItem.java:152", Gt6KtfruMachines.CNC, 256L, 200L,
                new ItemStack[] {form("rotor", "Trinaquadalloy", 1), form("plateCurved", "Trinaquadalloy", 2), form("plate", "Trinaquadalloy", 1)},
                new ItemStack[] {own("engine_turbo9", 1), form("dustSmall", "Trinaquadalloy", 4)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // QuantumEntry.java:66
        add("QuantumEntry.java:66", Gt6KtfruMachines.CNC, 8192L, 1600L,
                new ItemStack[] {form("plate", "MirrorGold", 4), block("quantum_stabilization_substrate", 1)},
                new ItemStack[] {block("quantum_coherence_casing_iridium", 1)},
                new FluidStack[] {field("DistW", 1000)},
                NO_FLUIDS);
        // tileEntityInit0.java:591
        add("tileEntityInit0.java:591", Gt6KtfruMachines.CNC, 200L, 8000L,
                new ItemStack[] {circuit(0, 1), gt("titanium_wall", 1)},
                new ItemStack[] {block("titanium_turbine_shell", 1)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // tileEntityInit0.java:592
        add("tileEntityInit0.java:592", Gt6KtfruMachines.CNC, 200L, 8000L,
                new ItemStack[] {circuit(0, 1), gt("tungstensteel_wall", 1)},
                new ItemStack[] {block("tungstensteel_turbine_shell", 1)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // tileEntityInit0.java:593
        add("tileEntityInit0.java:593", Gt6KtfruMachines.CNC, 200L, 8000L,
                new ItemStack[] {circuit(0, 1), gt("adamantium_wall", 1)},
                new ItemStack[] {block("adamantium_turbine_shell", 1)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
        // tileEntityInit0.java:610
        add("tileEntityInit0.java:610", Gt6KtfruMachines.CNC, 200L, 8000L,
                new ItemStack[] {circuit(0, 1), block("trinaquadalloy_wall", 1)},
                new ItemStack[] {block("trinaquadalloy_turbine_shell", 1)},
                new FluidStack[] {field("DistW", 2000)},
                NO_FLUIDS);
    }

    private static void fusionTokamak() {
        // Fusion.java:43
        add("Fusion.java:43", Gt6KtfruMachines.FUSION_TOKAMAK, 8192L, 400L,
                NO_ITEMS,
                new ItemStack[] {own("neutron", 2)},
                new FluidStack[] {field("Deuterium", 4000), field("Tritium", 4000)},
                new FluidStack[] {field("Helium", 2000)});
        // Fusion.java:46
        add("Fusion.java:46", Gt6KtfruMachines.FUSION_TOKAMAK, 16384L, 600L,
                NO_ITEMS,
                new ItemStack[] {own("neutron", 1)},
                new FluidStack[] {field("Deuterium", 2000), field("GenGas_Helium3", 1000)},
                new FluidStack[] {field("Helium", 2000)});
    }

    private static void fusionTokamakExperimental() {
        // Fusion.java:50
        add("Fusion.java:50", Gt6KtfruMachines.FUSION_TOKAMAK, 8192L, 400L,
                NO_ITEMS,
                new ItemStack[] {own("tokamak_neutron_data", 1)},
                new FluidStack[] {field("Deuterium", 4000), field("Tritium", 4000)},
                new FluidStack[] {field("Helium", 2000)},
                0L,
                new int[] {5000},
                null);
        // Fusion.java:53
        add("Fusion.java:53", Gt6KtfruMachines.FUSION_TOKAMAK, 16384L, 600L,
                NO_ITEMS,
                new ItemStack[] {own("tokamak_plasma_data", 1)},
                new FluidStack[] {field("Deuterium", 2000), field("GenGas_Helium3", 1000)},
                new FluidStack[] {field("Helium", 2000)},
                0L,
                new int[] {7500},
                null);
    }

    private static void laserFusion() {
        // Fusion.java:59
        add("Fusion.java:59", Gt6KtfruLateMachines.LASER, 2048L, 1L,
                new ItemStack[] {own("laser_target_dt", 1)},
                new ItemStack[] {own("neutron", 4)},
                new FluidStack[] {field("Deuterium", 4000)},
                new FluidStack[] {field("Helium", 2000), field("Tritium", 10)});
        // Fusion.java:61
        add("Fusion.java:61", Gt6KtfruLateMachines.LASER, 4096L, 1L,
                new ItemStack[] {own("laser_target_li6", 1)},
                new ItemStack[] {own("neutron", 1)},
                new FluidStack[] {field("Deuterium", 2000)},
                new FluidStack[] {field("Tritium", 2000), field("Helium", 10)});
        // Fusion.java:72
        add("Fusion.java:72", Gt6KtfruLateMachines.LASER, 8192L, 1L,
                new ItemStack[] {own("laser_target_lead", 1)},
                new ItemStack[] {own("neutron_rich_bismuth", 2)},
                new FluidStack[] {field("Deuterium", 4000)},
                new FluidStack[] {field("Helium", 10)});
        // Fusion.java:74
        add("Fusion.java:74", Gt6KtfruLateMachines.LASER, 8192L, 1L,
                new ItemStack[] {own("laser_target_tantalum", 1)},
                new ItemStack[] {own("metastable_tantalum", 2)},
                new FluidStack[] {field("Deuterium", 4000)},
                new FluidStack[] {field("Helium", 10)});
        // Fusion.java:76
        add("Fusion.java:76", Gt6KtfruLateMachines.LASER, 8192L, 1L,
                new ItemStack[] {own("laser_target_graphite", 1)},
                new ItemStack[] {own("dense_graphene_precursor", 1)},
                new FluidStack[] {field("Deuterium", 4000)},
                new FluidStack[] {field("Helium", 10)});
        // Fusion.java:82
        add("Fusion.java:82", Gt6KtfruLateMachines.LASER, 32768L, 1L,
                new ItemStack[] {own("laser_target_bismuth", 1), own("alpha_particle", 1)},
                new ItemStack[] {own("superheavy_nuclide_precursor", 1)},
                new FluidStack[] {field("Deuterium", 8000)},
                new FluidStack[] {field("Helium", 10)});
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Lithium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Lithium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Iron", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Iron", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Nickel", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Nickel", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Cobalt", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Cobalt", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Copper", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Copper", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Zinc", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Zinc", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Silver", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Silver", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Tin", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Tin", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Lead", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Lead", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Bismuth", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Bismuth", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Aluminium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Aluminium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Titanium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Titanium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Zirconium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Zirconium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Niobium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Niobium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Tantalum", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Tantalum", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Chromium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Chromium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Gold", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Gold", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Platinum", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Platinum", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Palladium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Palladium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Rhodium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Rhodium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Iridium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Iridium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "OsmiumElemental", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "OsmiumElemental", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "intellite", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "intellite", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Tungsten", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Tungsten", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Carbon", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Carbon", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Silicon", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Silicon", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Germanium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Germanium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Gallium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Gallium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Indium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Indium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Deuterium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Deuterium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Tritium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Tritium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Uranium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Uranium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Thorium", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Thorium", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
        // PurityRefining.java:130
        add("PurityRefining.java:130", Gt6KtfruLateMachines.LASER, 32768L, 1200L,
                new ItemStack[] {form("dustAnalyticalPure", "Naquadria", 4), own("quantum_control_element", 1)},
                new ItemStack[] {form("dustAbsolutelyPure", "Naquadria", 1)},
                new FluidStack[] {field("GenGas_Xenon", 250)},
                NO_FLUIDS,
                10000000L,
                null,
                null);
    }

    private static void laserFusionExperimental() {
        // Fusion.java:63
        add("Fusion.java:63", Gt6KtfruLateMachines.LASER, 2048L, 1L,
                new ItemStack[] {own("laser_target_dt", 1)},
                new ItemStack[] {own("laser_fusion_compression_data", 1)},
                new FluidStack[] {field("Deuterium", 4000)},
                new FluidStack[] {field("Helium", 2000), field("Tritium", 10)},
                0L,
                new int[] {5000},
                null);
        // Fusion.java:66
        add("Fusion.java:66", Gt6KtfruLateMachines.LASER, 4096L, 1L,
                new ItemStack[] {own("laser_target_li6", 1)},
                new ItemStack[] {own("laser_fusion_neutron_data", 1)},
                new FluidStack[] {field("Deuterium", 2000)},
                new FluidStack[] {field("Tritium", 2000), field("Helium", 10)},
                0L,
                new int[] {5000},
                null);
    }

    private static void catalyticReactor() {
        // CatalyticChemistry.java:38
        add("CatalyticChemistry.java:38", Gt6KtfruLateMachines.CATALYTIC, 1024L, 320L,
                new ItemStack[] {own("nickel_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "Plastic", 4)},
                new FluidStack[] {field("Methane", 3000), field("Oxygen", 1500)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:44
        add("CatalyticChemistry.java:44", Gt6KtfruLateMachines.CATALYTIC, 1024L, 360L,
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "PVC", 3)},
                new FluidStack[] {field("Methane", 3000), field("GenGas_Chlorine", 2000)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:50
        add("CatalyticChemistry.java:50", Gt6KtfruLateMachines.CATALYTIC, 1536L, 480L,
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "SBR", 2)},
                new FluidStack[] {field("GenLiquid_Benzene", 1000), field("Methane", 2500), field("Oxygen", 1500)},
                new FluidStack[] {field("Water", 1500)},
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:56
        add("CatalyticChemistry.java:56", Gt6KtfruLateMachines.CATALYTIC, 1536L, 520L,
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "EpoxyResin", 4)},
                new FluidStack[] {field("GenLiquid_Benzene", 1000), field("Propylene", 1500), field("Oxygen", 1800)},
                new FluidStack[] {field("Water", 1200)},
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:62
        add("CatalyticChemistry.java:62", Gt6KtfruLateMachines.CATALYTIC, 1280L, 420L,
                new ItemStack[] {own("ziegler_natta_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "PolymethylMethacrylate", 2)},
                new FluidStack[] {field("Methane", 2500), field("Oxygen", 1200), field("GenLiquid_Methanol", 1000)},
                new FluidStack[] {field("Water", 1000)},
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:68
        add("CatalyticChemistry.java:68", Gt6KtfruLateMachines.CATALYTIC, 1536L, 500L,
                new ItemStack[] {own("platinum_palladium_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "HardPlastic", 3)},
                new FluidStack[] {field("GenLiquid_Benzene", 1000), field("GenGas_Phosgene", 750), field("Oxygen", 1000)},
                new FluidStack[] {field("Hydrogen", 240)},
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:75
        add("CatalyticChemistry.java:75", Gt6KtfruLateMachines.CATALYTIC, 256L, 240L,
                new ItemStack[] {form("dust", "Nickel", 4), form("dust", "Alumina", 4), form("dust", "SiliconDioxide", 2)},
                new ItemStack[] {own("nickel_catalyst_cluster", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // CatalyticChemistry.java:79
        add("CatalyticChemistry.java:79", Gt6KtfruLateMachines.CATALYTIC, 512L, 300L,
                new ItemStack[] {form("dustTiny", "Platinum", 4), form("dustTiny", "Palladium", 4), form("dust", "Carbon", 4), form("dust", "Alumina", 2)},
                new ItemStack[] {own("platinum_palladium_catalyst_cluster", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // CatalyticChemistry.java:83
        add("CatalyticChemistry.java:83", Gt6KtfruLateMachines.CATALYTIC, 512L, 360L,
                new ItemStack[] {form("dust", "SiliconDioxide", 4), form("dust", "Alumina", 2), form("dust", "Sodium", 1)},
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // CatalyticChemistry.java:87
        add("CatalyticChemistry.java:87", Gt6KtfruLateMachines.CATALYTIC, 1024L, 400L,
                new ItemStack[] {form("dust", "TriethylAluminium", 1), form("dust", "MagnesiumChloride", 4)},
                new ItemStack[] {own("ziegler_natta_catalyst_cluster", 1)},
                new FluidStack[] {field("GenLiquid_TitaniumTetrachloride", 1000)},
                NO_FLUIDS);
        // CatalyticChemistry.java:97
        add("CatalyticChemistry.java:97", Gt6KtfruLateMachines.CATALYTIC, 256L, 80L,
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Benzene", 1000), field("GenGas_Fluorine", 1000)},
                new FluidStack[] {field("GenLiquid_Fluorobenzene", 1000)},
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:102
        add("CatalyticChemistry.java:102", Gt6KtfruLateMachines.CATALYTIC, 256L, 80L,
                new ItemStack[] {own("zeolite_catalyst_cluster", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Toluene", 1000), field("GenGas_Fluorine", 1000)},
                new FluidStack[] {field("GenLiquid_Fluorotoluene", 1000)},
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:107
        add("CatalyticChemistry.java:107", Gt6KtfruLateMachines.CATALYTIC, 384L, 140L,
                new ItemStack[] {own("nickel_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "FluorobenzoicAcid", 1)},
                new FluidStack[] {field("GenLiquid_Fluorotoluene", 1000), field("Oxygen", 1000)},
                NO_FLUIDS,
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:112
        add("CatalyticChemistry.java:112", Gt6KtfruLateMachines.CATALYTIC, 512L, 160L,
                new ItemStack[] {own("platinum_palladium_catalyst_cluster", 1), form("dust", "FluorobenzoicAcid", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Phosgene", 1000)},
                new FluidStack[] {field("GenLiquid_FluorobenzoylChloride", 1000), field("CarbonDioxide", 1000), field("GenGas_HydrochloricAcid", 1000)},
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:117
        add("CatalyticChemistry.java:117", Gt6KtfruLateMachines.CATALYTIC, 768L, 240L,
                new ItemStack[] {own("platinum_palladium_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "Difluorobenzophenone", 1)},
                new FluidStack[] {field("GenLiquid_Fluorobenzene", 1000), field("GenLiquid_FluorobenzoylChloride", 1000)},
                new FluidStack[] {field("GenGas_HydrochloricAcid", 1000)},
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:122
        add("CatalyticChemistry.java:122", Gt6KtfruLateMachines.CATALYTIC, 512L, 180L,
                new ItemStack[] {own("nickel_catalyst_cluster", 1)},
                new ItemStack[] {form("dust", "Hydroquinone", 1)},
                new FluidStack[] {field("GenLiquid_Phenol", 1000), field("GenLiquid_HydrogenPeroxide", 1000)},
                new FluidStack[] {field("Water", 500)},
                0L,
                null,
                new int[] {0});
        // CatalyticChemistry.java:127
        add("CatalyticChemistry.java:127", Gt6KtfruLateMachines.CATALYTIC, 8192L, 1200L,
                new ItemStack[] {own("polycondensation_catalyst_cluster", 1), form("dust", "Difluorobenzophenone", 1), form("dust", "Hydroquinone", 1), form("dust", "PotassiumCarbonate", 2)},
                new ItemStack[] {form("dust", "PEEK", 2)},
                NO_FLUIDS,
                new FluidStack[] {field("Water", 1000)},
                0L,
                null,
                new int[] {0});
    }

    private static void mirrorGoldSynthesis() {
        // QuantumEntry.java:38
        add("QuantumEntry.java:38", Gt6KtfruLateMachines.MIRROR, 8192L, 2400L,
                new ItemStack[] {form("ingot", "MirrorGold", 1), form("dustAnalyticalPure", "intellite", 2), form("dustAnalyticalPure", "Naquadria", 2), form("nugget", "intellite", 4)},
                new ItemStack[] {form("ingot", "MirrorGold", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
        // QuantumEntry.java:47
        add("QuantumEntry.java:47", Gt6KtfruLateMachines.MIRROR, 8192L, 4800L,
                new ItemStack[] {form("dustAbsolutelyPure", "intellite", 8), form("dustAbsolutelyPure", "Naquadria", 4), form("dustAbsolutelyPure", "Platinum", 4), form("nugget", "intellite", 16), form("ingot", "MirrorGold", 1)},
                new ItemStack[] {form("plate", "MirrorGold", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void mirrorGoldQuantumization() {
        // QuantumEntry.java:89
        add("QuantumEntry.java:89", Gt6KtfruLateMachines.MIRROR_QUANTUM, 16384L, 2400L,
                new ItemStack[] {form("plate", "MirrorGold", 1), form("dustAbsolutelyPure", "intellite", 8), form("dustAbsolutelyPure", "Naquadria", 4), gt("compact_signal_emitter_luv", 2), gt("circuit_ultimate", 1)},
                new ItemStack[] {block("quantum_gold_drive_module", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
    }

    private static void cosmicRadiationResearch() {
        // ComputerBuilding.java:176
        add("ComputerBuilding.java:176", Gt6KtfruLateMachines.COSMIC, 8192L, 2400L,
                new ItemStack[] {form("dustAnalyticalPure", "Gold", 1)},
                new ItemStack[] {own("cosmic_radiation_background_data", 1), own("cosmic_radiation_spectrum_data", 1), own("high_energy_cosmic_particle_data", 1)},
                NO_FLUIDS,
                NO_FLUIDS,
                0L,
                new int[] {8000, 4000, 2000},
                null);
    }

    private static void microgravityCrystalGrowth() {
        // ComputerBuilding.java:171
        add("ComputerBuilding.java:171", Gt6KtfruLateMachines.CRYSTAL, 8192L, 144000L,
                new ItemStack[] {form("dustAnalyticalPure", "Molybdenum", 8)},
                new ItemStack[] {own("mo_o2_boule_pure", 1)},
                new FluidStack[] {field("GenGas_Krypton", 40000), field("Oxygen", 4000)},
                NO_FLUIDS);
        // ComputerBuilding.java:172
        add("ComputerBuilding.java:172", Gt6KtfruLateMachines.CRYSTAL, 8192L, 144000L,
                new ItemStack[] {form("dustAnalyticalPure", "Molybdenum", 8)},
                new ItemStack[] {own("mo_o2_boule_pure", 1)},
                new FluidStack[] {field("GenGas_Xenon", 20000), field("Oxygen", 4000)},
                NO_FLUIDS);
        // ComputerBuilding.java:173
        add("ComputerBuilding.java:173", Gt6KtfruLateMachines.CRYSTAL, 8192L, 144000L,
                new ItemStack[] {form("dustAnalyticalPure", "Molybdenum", 8)},
                new ItemStack[] {own("mo_o2_boule_pure", 1)},
                new FluidStack[] {field("GenGas_Radon", 20000), field("Oxygen", 4000)},
                NO_FLUIDS);
    }

    private static void quantumGoldSynthesis() {
        // QuantumEntry.java:107
        add("QuantumEntry.java:107", Gt6KtfruLateMachines.QUANTUM_GOLD, 65536L, 4800L,
                new ItemStack[] {form("plate", "MirrorGold", 1), form("dustTiny", "Naquadria", 8), form("nugget", "intellite", 16), form("dustAbsolutelyPure", "intellite", 8)},
                new ItemStack[] {form("ingot", "QuantumGold", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void quantumObservation() {
        // QuantumObservation.java:29
        add("QuantumObservation.java:29", Gt6KtfruLateMachines.OBSERVATION, 16384L, 4800L,
                new ItemStack[] {form("plate", "QuantumGold", 1), form("foil", "MirrorGold", 2), form("dustAbsolutelyPure", "intellite", 4), form("dustAbsolutelyPure", "Naquadria", 4)},
                new ItemStack[] {own("quantum_observation_data", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // QuantumObservation.java:37
        add("QuantumObservation.java:37", Gt6KtfruLateMachines.OBSERVATION, 8192L, 2400L,
                new ItemStack[] {form("foil", "QuantumGold", 1), block("quantum_readout_coil", 2), form("dustAnalyticalPure", "Platinum", 4)},
                new ItemStack[] {own("quantum_observation_data", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void quantumCommunication() {
        // QuantumEntry.java:31
        add("QuantumEntry.java:31", Gt6KtfruLateMachines.RELAY_MAP, 8192L, 3600L,
                new ItemStack[] {form("dustAnalyticalPure", "intellite", 4)},
                new ItemStack[] {form("ingot", "MirrorGold", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void researchAssembler() {
        // CompactItem.java:41
        add("CompactItem.java:41", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 372L, 80L,
                new ItemStack[] {form("plateTiny", "intellite", 1), form("wireFine", "Titanium", 4)},
                new ItemStack[] {own("intelligent_core", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // CompactItem.java:42
        add("CompactItem.java:42", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 1574L, 80L,
                new ItemStack[] {own("cpugt3660", 1), form("dust", "Titanium", 1)},
                new ItemStack[] {own("intelligent_core", 2)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // CompactItem.java:54
        add("CompactItem.java:54", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 80L,
                new ItemStack[] {circuit(0, 1), own("basic_circuit_part_core", 1)},
                new ItemStack[] {gt("circuit_part_basic", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
        // CompactItem.java:55
        add("CompactItem.java:55", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32L, 80L,
                new ItemStack[] {circuit(1, 1), form("plateGemTiny", "Silicon", 2), form("foil", "Copper", 1)},
                new ItemStack[] {own("good_circuit_part_core", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
        // CompactItem.java:155
        add("CompactItem.java:155", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 320L, 200L,
                new ItemStack[] {form("bolt", "Cobalt60", 1), form("plateDense", "Lead", 2)},
                new ItemStack[] {own("co60_flaw_detection_core", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
                NO_FLUIDS);
        // ComputerBuilding.java:471
        add("ComputerBuilding.java:471", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 80L,
                new ItemStack[] {form("plate", "SteelGalvanized", 4), form("screw", "Steel", 16), gt("circuit_basic", 8)},
                new ItemStack[] {own("computer_basic_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:472
        add("ComputerBuilding.java:472", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 160L,
                new ItemStack[] {form("plate", "SteelGalvanized", 4), form("screw", "Steel", 16), gt("circuit_good", 8)},
                new ItemStack[] {own("computer_good_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:473
        add("ComputerBuilding.java:473", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 320L,
                new ItemStack[] {form("plate", "SteelGalvanized", 4), form("screw", "Steel", 32), gt("circuit_advanced", 8)},
                new ItemStack[] {own("computer_advanced_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:474
        add("ComputerBuilding.java:474", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 640L,
                new ItemStack[] {form("plate", "StainlessSteel", 4), form("screw", "Steel", 32), gt("circuit_elite", 8)},
                new ItemStack[] {own("computer_elite_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:475
        add("ComputerBuilding.java:475", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32L, 960L,
                new ItemStack[] {form("plate", "StainlessSteel", 4), form("screw", "StainlessSteel", 64), gt("circuit_master", 8)},
                new ItemStack[] {own("computer_master_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:476
        add("ComputerBuilding.java:476", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32L, 1280L,
                new ItemStack[] {form("plate", "Tungstensteel", 4), form("screw", "StainlessSteel", 64), gt("circuit_ultimate", 8)},
                new ItemStack[] {own("computer_ultimate_circuits", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // Fusion.java:141
        add("Fusion.java:141", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32768L, 2400L,
                new ItemStack[] {block("laser_focusing_array", 8), block("laser_target_injector", 1), block("laser_pulse_storage", 2)},
                new ItemStack[] {block("laser_fusion_experimental", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 9)},
                NO_FLUIDS);
        // Fusion.java:143
        add("Fusion.java:143", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 131072L, 4800L,
                new ItemStack[] {block("laser_focusing_array", 24), block("laser_pulse_storage", 8), block("laser_product_collector", 4)},
                new ItemStack[] {block("laser_fusion_industrial", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 2)},
                NO_FLUIDS);
        // Fusion.java:154
        add("Fusion.java:154", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 65536L, 3200L,
                new ItemStack[] {gt("casing_machine_double_stainlesssteel", 1), block("tokamak_magnetic_conductor_exp", 8), block("tokamak_magnetic_coil_exp", 4), block("tokamak_magnetic_wall_exp", 4), gt("circuit_elite", 2), gt("compact_sensor_ev", 2), own("computer_gt3660", 1)},
                new ItemStack[] {block("fusion_tokamak_exp", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 9)},
                NO_FLUIDS);
        // Fusion.java:163
        add("Fusion.java:163", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 262144L, 4800L,
                new ItemStack[] {gt("casing_machine_double_tungstensteel", 1), block("tokamak_magnetic_conductor_t1", 16), block("tokamak_magnetic_coil_t1", 8), block("tokamak_magnetic_wall_t1", 8), gt("circuit_master", 4), gt("compact_sensor_iv", 2), own("computer_gt3699", 2)},
                new ItemStack[] {block("fusion_tokamak_t1", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 4)},
                NO_FLUIDS);
        // ResearchRecipes.java:133
        add("ResearchRecipes.java:133", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 128L,
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_helium", 1)},
                new FluidStack[] {field("Helium", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:134
        add("ResearchRecipes.java:134", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 128L,
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_neon", 1)},
                new FluidStack[] {field("Neon", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:135
        add("ResearchRecipes.java:135", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 128L,
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_argon", 1)},
                new FluidStack[] {field("Argon", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:136
        add("ResearchRecipes.java:136", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 128L,
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_krypton", 1)},
                new FluidStack[] {field("GenGas_Krypton", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:137
        add("ResearchRecipes.java:137", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 128L,
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_xenon", 1)},
                new FluidStack[] {field("GenGas_Xenon", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:138
        add("ResearchRecipes.java:138", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 128L,
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_heliumneon", 1)},
                new FluidStack[] {field("GenGas_HeliumNeon", 1000)},
                NO_FLUIDS);
        // ResearchRecipes.java:139
        add("ResearchRecipes.java:139", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 16L, 128L,
                new ItemStack[] {gt("laser_emitter_emptygas", 1)},
                new ItemStack[] {gt("laser_emitter_carbonmonoxide", 1)},
                new FluidStack[] {field("GenGas_CarbonMonoxide", 1000)},
                NO_FLUIDS);
        // tileEntityInit0.java:528
        add("tileEntityInit0.java:528", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32768L, 2400L,
                new ItemStack[] {form("plate", "MirrorGold", 2), gt("casing_machine_naquadria", 1), form("dustAbsolutelyPure", "intellite", 4), gt("compact_signal_emitter_luv", 4), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("mirror_gold_quantumization", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:529
        add("tileEntityInit0.java:529", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32768L, 2400L,
                new ItemStack[] {form("plate", "MirrorGold", 2), gt("casing_machine_naquadria", 1), form("dustAbsolutelyPure", "intellite", 4), form("dustAbsolutelyPure", "Naquadria", 4), form("dustAnalyticalPure", "Platinum", 4), gt("compact_signal_emitter_luv", 4), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("quantum_gold_chamber", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:541
        add("tileEntityInit0.java:541", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32768L, 2400L,
                new ItemStack[] {block("ai_routing_matrix", 1), block("ai_compute_frame", 4), block("ai_memory_bank", 4), own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "Silicon", 1), form("plate", "PEEK", 1), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("ai_flow_control", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:542
        add("tileEntityInit0.java:542", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32768L, 2400L,
                new ItemStack[] {block("ai_packing_planner", 1), block("ai_compute_frame", 4), block("ai_memory_bank", 4), own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "Silicon", 1), form("plate", "PEEK", 1), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("ai_fill_planning", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:543
        add("tileEntityInit0.java:543", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32768L, 2400L,
                new ItemStack[] {block("ai_recognition_matrix", 1), block("ai_compute_frame", 4), block("ai_memory_bank", 4), own("computer_gt3699_v4", 1), form("dustAnalyticalPure", "Silicon", 1), form("plate", "PEEK", 1), gt("circuit_ultimate", 2)},
                new ItemStack[] {block("ai_pattern_recognition", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:678
        add("tileEntityInit0.java:678", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 8192L, 1200L,
                new ItemStack[] {gt("casing_machine_double_tungstensteel", 1), block("magnetic_conductor", 8), form("dustAnalyticalPure", "Copper", 16), form("dustAnalyticalPure", "Silver", 8), form("plate", "PEEK", 8), form("wireFine", "Graphene", 32), gt("circuit_master", 2)},
                new ItemStack[] {block("wireless_battery_inductive", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:680
        add("tileEntityInit0.java:680", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32768L, 2400L,
                new ItemStack[] {gt("casing_machine_double_naquadria", 1), form("plate", "MirrorGold", 4), form("dustAbsolutelyPure", "intellite", 8), form("dustAbsolutelyPure", "Naquadria", 4), block("entanglement_channel_module", 4), block("quantum_readout_coil", 2), gt("compact_signal_emitter_luv", 4), gt("circuit_ultimate", 4)},
                new ItemStack[] {block("wireless_battery_quantum", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
        // tileEntityInit0.java:683
        add("tileEntityInit0.java:683", Gt6KtfruResearchMachines.ASSEMBLER_MAP, 32768L, 2400L,
                new ItemStack[] {form("plate", "QuantumGold", 4), form("dustAbsolutelyPure", "intellite", 4), form("dustAbsolutelyPure", "Naquadria", 4), block("quantum_readout_coil", 2), gt("compact_signal_emitter_luv", 4), gt("circuit_ultimate", 2), gt("casing_machine_trinaquadalloy", 1)},
                new ItemStack[] {block("quantum_observation_tower", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 18)},
                NO_FLUIDS);
    }

    private static void researchReactor() {
        // CatalyticChemistry.java:91
        add("CatalyticChemistry.java:91", Gt6KtfruResearchMachines.REACTOR_MAP, 2048L, 600L,
                new ItemStack[] {form("dust", "PotassiumCarbonate", 4), form("dust", "Rutile", 2), form("dust", "SiliconDioxide", 2)},
                new ItemStack[] {own("polycondensation_catalyst_cluster", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void add(String where, String map, long euPerTick, long duration,
            ItemStack[] itemsIn, ItemStack[] itemsOut, FluidStack[] fluidsIn, FluidStack[] fluidsOut) {
        add(where, map, euPerTick, duration, itemsIn, itemsOut, fluidsIn, fluidsOut, 0L, null, null);
    }

    private static void add(String where, String map, long euPerTick, long duration,
            ItemStack[] itemsIn, ItemStack[] itemsOut, FluidStack[] fluidsIn, FluidStack[] fluidsOut,
            long special, int[] chances, int[] kept) {
        if (broken(itemsIn) || broken(fluidsIn) || broken(itemsOut) || broken(fluidsOut)) {
            return;
        }
        AddonRecipes.Line line = chances == null && kept == null
                ? new AddonRecipes.Line(itemsIn, itemsOut, fluidsIn, fluidsOut, duration, euPerTick, special)
                : new AddonRecipes.Line(itemsIn, itemsOut, fluidsIn, fluidsOut, duration, euPerTick, special, chances, kept);
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

    private static ItemStack form(String prefix, String material, int count) {
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
        ItemStack stack = Gt6KtfruItems.stack(path, count);
        if (stack == null || stack.isEmpty()) {
            PROBLEMS.add("item " + path);
            return ItemStack.EMPTY;
        }
        return stack;
    }

    private static ItemStack block(String path, int count) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(Gt6KtfruAddon.MODID, path));
        if (item == Items.AIR) {
            PROBLEMS.add("block " + path);
            return ItemStack.EMPTY;
        }
        return new ItemStack(item, count);
    }

    private static ItemStack gt(String path, int count) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("gt6ce", path));
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
        FluidStack stack = AddonFluids.still(name, amount);
        if (stack == null || stack.isEmpty()) {
            PROBLEMS.add("fluid " + name);
            return FluidStack.EMPTY;
        }
        return stack;
    }
}
