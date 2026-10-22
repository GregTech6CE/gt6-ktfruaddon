package com.lombinaxmasson.gt6ktfruaddon;

import java.util.LinkedHashSet;
import java.util.Set;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;

import net.minecraft.world.item.ItemStack;
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
        waferTester();
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
        // ComputerBuilding.java:418
        add("ComputerBuilding.java:418", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 500L, 7800L,
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680", 2)},
                new ItemStack[] {own("cpugt3699", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:420
        add("ComputerBuilding.java:420", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 580L, 8200L,
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v2", 2)},
                new ItemStack[] {own("cpugt3699_v2", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 36)},
                NO_FLUIDS);
        // ComputerBuilding.java:422
        add("ComputerBuilding.java:422", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 620L, 10800L,
                new ItemStack[] {circuit(1, 1), own("cpu_board_t3", 1), own("cpu_die_gt3680_v3", 2)},
                new ItemStack[] {own("cpugt3699_v3", 1)},
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
        // ComputerBuilding.java:433
        add("ComputerBuilding.java:433", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 50L, 300L,
                new ItemStack[] {circuit(1, 1), own("ram_board_t1", 1), own("ram_die2_k", 8)},
                new ItemStack[] {own("ram_bar2_k8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 72)},
                NO_FLUIDS);
        // ComputerBuilding.java:436
        add("ComputerBuilding.java:436", Gt6KtfruMachines.CIRCUIT_ASSEMBLER, 150L, 300L,
                new ItemStack[] {circuit(1, 1), own("ram_board_t1", 1), own("ram_die32_k", 8)},
                new ItemStack[] {own("ram_bar32_k8", 1)},
                new FluidStack[] {field("GenMolten_SolderingAlloy", 144)},
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
                new int[] {3500, 1200},
                null);
        // ComputerBuilding.java:371
        add("ComputerBuilding.java:371", Gt6KtfruMachines.LASER_CUTTER, 250L, 400L,
                new ItemStack[] {own("cpu_wafer_t2_checked", 1)},
                new ItemStack[] {own("cpu_die_tf3586", 12), own("cpu_die_tf3586_s", 5)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {2000, 800},
                null);
        // ComputerBuilding.java:372
        add("ComputerBuilding.java:372", Gt6KtfruMachines.LASER_CUTTER, 500L, 400L,
                new ItemStack[] {own("cpu_wafer_t3_checked", 1)},
                new ItemStack[] {own("cpu_die_gt1000", 12), own("cpu_die_gt1090", 5)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {2500, 900},
                null);
        // ComputerBuilding.java:373
        add("ComputerBuilding.java:373", Gt6KtfruMachines.LASER_CUTTER, 1000L, 1000L,
                new ItemStack[] {own("cpu_wafer_t4_checked", 1)},
                new ItemStack[] {own("cpu_die_gt2000", 12), own("cpu_die_gt2090", 5)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {1700, 650},
                null);
        // ComputerBuilding.java:374
        add("ComputerBuilding.java:374", Gt6KtfruMachines.LASER_CUTTER, 800L, 10000L,
                new ItemStack[] {own("cpu_wafer_t5_checked", 1)},
                new ItemStack[] {own("cpu_die_gt3660", 25), own("cpu_die_gt3680", 12)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {1400, 450},
                null);
        // ComputerBuilding.java:379
        add("ComputerBuilding.java:379", Gt6KtfruMachines.LASER_CUTTER, 80L, 400L,
                new ItemStack[] {own("ram_wafer_t1_checked", 1)},
                new ItemStack[] {own("ram_die2_k", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {5000},
                null);
        // ComputerBuilding.java:380
        add("ComputerBuilding.java:380", Gt6KtfruMachines.LASER_CUTTER, 210L, 400L,
                new ItemStack[] {own("ram_wafer_t2_checked", 1)},
                new ItemStack[] {own("ram_die32_k", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {3000},
                null);
        // ComputerBuilding.java:381
        add("ComputerBuilding.java:381", Gt6KtfruMachines.LASER_CUTTER, 480L, 400L,
                new ItemStack[] {own("ram_wafer_t3_checked", 1)},
                new ItemStack[] {own("ram_die256_k", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {4300},
                null);
        // ComputerBuilding.java:382
        add("ComputerBuilding.java:382", Gt6KtfruMachines.LASER_CUTTER, 940L, 1000L,
                new ItemStack[] {own("ram_wafer_t4_checked", 1)},
                new ItemStack[] {own("ram_die2_m", 32)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {3800},
                null);
        // ComputerBuilding.java:383
        add("ComputerBuilding.java:383", Gt6KtfruMachines.LASER_CUTTER, 560L, 6000L,
                new ItemStack[] {own("ram_wafer_t5_checked", 1)},
                new ItemStack[] {own("ram_die16_m", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {3400},
                null);
        // ComputerBuilding.java:384
        add("ComputerBuilding.java:384", Gt6KtfruMachines.LASER_CUTTER, 540L, 7000L,
                new ItemStack[] {own("ram_wafer_t6_checked", 1)},
                new ItemStack[] {own("ram_die128_m", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {3000},
                null);
        // ComputerBuilding.java:385
        add("ComputerBuilding.java:385", Gt6KtfruMachines.LASER_CUTTER, 500L, 8000L,
                new ItemStack[] {own("ram_wafer_t7_checked", 1)},
                new ItemStack[] {own("ram_die768_m", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {2600},
                null);
        // ComputerBuilding.java:386
        add("ComputerBuilding.java:386", Gt6KtfruMachines.LASER_CUTTER, 430L, 9000L,
                new ItemStack[] {own("ram_wafer_t8_checked", 1)},
                new ItemStack[] {own("ram_die2_g", 60)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {2100},
                null);
        // ComputerBuilding.java:396
        add("ComputerBuilding.java:396", Gt6KtfruMachines.LASER_CUTTER, 170L, 400L,
                new ItemStack[] {own("diode_wafer_t1_checked", 1)},
                new ItemStack[] {own("diode_t2_part", 64), own("diode_t2_part", 24)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {4800, 4800},
                null);
        // ComputerBuilding.java:397
        add("ComputerBuilding.java:397", Gt6KtfruMachines.LASER_CUTTER, 440L, 400L,
                new ItemStack[] {own("diode_wafer_t2_checked", 1)},
                new ItemStack[] {own("diode_t3_part", 64), own("diode_t3_part", 24)},
                NO_FLUIDS,
                NO_FLUIDS,
                new int[] {3200, 3200},
                null);
    }

    private static void heatMixer() {
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
        // Chemistry.java:137
        add("Chemistry.java:137", Gt6KtfruMachines.HEAT_MIXER, 120L, 80L,
                new ItemStack[] {form("dust", "AluminumChloride", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Benzene", 200), field("GenLiquid_Chloromethane", 100)},
                new FluidStack[] {field("GenLiquid_Toluene", 200), field("GenGas_HydrochloricAcid", 1000)},
                null,
                new int[] {0});
        // Chemistry.java:150
        add("Chemistry.java:150", Gt6KtfruMachines.HEAT_MIXER, 2141L, 80L,
                new ItemStack[] {form("dust", "Rutile", 2)},
                new ItemStack[] {form("dust", "TitanicSulfate", 3)},
                new FluidStack[] {field("GenLiquid_SulfuricAcid", 4000)},
                NO_FLUIDS);
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
        // Plastic.java:65
        add("Plastic.java:65", Gt6KtfruMachines.HEAT_MIXER, 148L, 120L,
                new ItemStack[] {form("dust", "SodiumHydroxide", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_DichloroPropanol", 100)},
                new FluidStack[] {field("GenLiquid_Epichlorohydrin", 100)});
        // Plastic.java:75
        add("Plastic.java:75", Gt6KtfruMachines.HEAT_MIXER, 83L, 100L,
                new ItemStack[] {form("dust", "PotassiumSulfate", 1)},
                new ItemStack[] {form("dust", "PotassiumPyrosulfate", 1)},
                new FluidStack[] {field("GenGas_SulfurTrioxide", 1000)},
                NO_FLUIDS,
                null,
                new int[] {0});
        // Plastic.java:80
        add("Plastic.java:80", Gt6KtfruMachines.HEAT_MIXER, 320L, 1200L,
                new ItemStack[] {own("proton_exchange_membrane", 1), form("dust", "BPA", 1), form("dust", "SodiumHydroxide", 2)},
                new ItemStack[] {form("dust", "EpoxyResin", 6)},
                new FluidStack[] {field("GenLiquid_Epichlorohydrin", 1000)},
                new FluidStack[] {field("Saltwater", 800)},
                null,
                new int[] {0});
        // Plastic.java:91
        add("Plastic.java:91", Gt6KtfruMachines.HEAT_MIXER, 500L, 600L,
                new ItemStack[] {form("dust", "Iron", 1), form("dust", "Silicon", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Chlorine", 2000)},
                new FluidStack[] {field("GenLiquid_Tetrachorosilane", 1000)},
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
                null,
                new int[] {0});
        // Plastic.java:129
        add("Plastic.java:129", Gt6KtfruMachines.HEAT_MIXER, 120L, 60L,
                new ItemStack[] {form("dust", "Chromium", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Ethylene", 200)},
                new FluidStack[] {field("GenLiquid_Isoprene", 144)},
                null,
                new int[] {0});
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
        // ComputerBuilding.java:75
        add("ComputerBuilding.java:75", Gt6KtfruMachines.HEAT_MIXER, 240L, 120L,
                new ItemStack[] {form("dust", "Silver", 1), form("dust", "Cyclohexanol", 1)},
                new ItemStack[] {form("dust", "Cyclohexanone", 1)},
                new FluidStack[] {field("Oxygen", 100)},
                NO_FLUIDS,
                null,
                new int[] {0});
        // ComputerBuilding.java:77
        add("ComputerBuilding.java:77", Gt6KtfruMachines.HEAT_MIXER, 120L, 120L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "AmmoniumNitrate", 1)},
                new FluidStack[] {field("GenLiquid_NitricAcid", 1000), field("GenGas_Ammonia", 1000)},
                NO_FLUIDS);
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
        // OilProcessing.java:97
        add("OilProcessing.java:97", Gt6KtfruMachines.HEAT_MIXER, 64L, 200L,
                new ItemStack[] {form("dust", "Iron", 1), form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Methane", 100), field("Oxygen", 100)},
                new FluidStack[] {field("GenGas_Formaldehyde", 100), field("Water", 100)},
                null,
                new int[] {0, 1});
        // OilProcessing.java:99
        add("OilProcessing.java:99", Gt6KtfruMachines.HEAT_MIXER, 44L, 20L,
                new ItemStack[] {form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Formaldehyde", 100), field("Water", 100)},
                null,
                new int[] {0});
        // OilProcessing.java:100
        add("OilProcessing.java:100", Gt6KtfruMachines.HEAT_MIXER, 44L, 20L,
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Methanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Formaldehyde", 100), field("Water", 100)},
                null,
                new int[] {0});
        // OilProcessing.java:102
        add("OilProcessing.java:102", Gt6KtfruMachines.HEAT_MIXER, 44L, 20L,
                new ItemStack[] {form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Acetaldehyde", 100), field("Water", 100)},
                null,
                new int[] {0});
        // OilProcessing.java:103
        add("OilProcessing.java:103", Gt6KtfruMachines.HEAT_MIXER, 44L, 20L,
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("BioEthanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Acetaldehyde", 100), field("Water", 100)},
                null,
                new int[] {0});
        // OilProcessing.java:105
        add("OilProcessing.java:105", Gt6KtfruMachines.HEAT_MIXER, 44L, 30L,
                new ItemStack[] {form("dust", "Molybdenum", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Propanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Propionaldehyde", 100), field("Water", 100)},
                null,
                new int[] {0});
        // OilProcessing.java:106
        add("OilProcessing.java:106", Gt6KtfruMachines.HEAT_MIXER, 44L, 30L,
                new ItemStack[] {form("dust", "Silver", 1)},
                NO_ITEMS,
                new FluidStack[] {field("GenLiquid_Propanol", 100), field("Oxygen", 50)},
                new FluidStack[] {field("GenGas_Propionaldehyde", 100), field("Water", 100)},
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
                null,
                new int[] {0, 1});
        // OilProcessing.java:161
        add("OilProcessing.java:161", Gt6KtfruMachines.HEAT_MIXER, 280L, 160L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Oxygen", 500), field("Propane", 1000)},
                new FluidStack[] {field("GenLiquid_Acetone", 500)});
        // OreProcessing.java:35
        add("OreProcessing.java:35", Gt6KtfruMachines.HEAT_MIXER, 100L, 400L,
                new ItemStack[] {form("dust", "Iron", 1)},
                NO_ITEMS,
                new FluidStack[] {field("Nitrogen", 2000), field("Hydrogen", 3000)},
                new FluidStack[] {field("GenGas_Ammonia", 2000)},
                null,
                new int[] {0});
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
                null,
                new int[] {1});
        // PlatinumGroupProcess.java:34
        add("PlatinumGroupProcess.java:34", Gt6KtfruMachines.HEAT_MIXER, 82L, 120L,
                new ItemStack[] {form("dust", "DimethylaminomethylStyrene", 1)},
                new ItemStack[] {own("alkaline_ion_exchange_membrane", 1)},
                new FluidStack[] {field("Oxygen", 1000)},
                NO_FLUIDS);
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

    private static void add(String where, String map, long euPerTick, long duration,
            ItemStack[] itemsIn, ItemStack[] itemsOut, FluidStack[] fluidsIn, FluidStack[] fluidsOut) {
        add(where, map, euPerTick, duration, itemsIn, itemsOut, fluidsIn, fluidsOut, null, null);
    }

    private static void add(String where, String map, long euPerTick, long duration,
            ItemStack[] itemsIn, ItemStack[] itemsOut, FluidStack[] fluidsIn, FluidStack[] fluidsOut,
            int[] chances, int[] kept) {
        if (broken(itemsIn) || broken(fluidsIn) || broken(itemsOut) || broken(fluidsOut)) {
            return;
        }
        AddonRecipes.Line line = chances == null && kept == null
                ? new AddonRecipes.Line(itemsIn, itemsOut, fluidsIn, fluidsOut, duration, euPerTick, 0)
                : new AddonRecipes.Line(itemsIn, itemsOut, fluidsIn, fluidsOut, duration, euPerTick, 0, chances, kept);
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
