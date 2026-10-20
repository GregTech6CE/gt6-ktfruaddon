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
        laserCutter();
        heatMixer();
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

    private static void laserCutter() {
        // Chemistry.java:133
        add("Chemistry.java:133", Gt6KtfruMachines.LASER_CUTTER, 500L, 200L,
                new ItemStack[] {form("plate", "Graphene", 1)},
                new ItemStack[] {form("stick", "Graphene", 2)},
                NO_FLUIDS,
                NO_FLUIDS);
    }

    private static void heatMixer() {
        // Chemistry.java:48
        add("Chemistry.java:48", Gt6KtfruMachines.HEAT_MIXER, 120L, 40L,
                new ItemStack[] {form("dust", "Sodium", 1), form("dust", "Potassium", 1)},
                new ItemStack[] {form("dustTiny", "PotassiumSodium", 1)},
                NO_FLUIDS,
                NO_FLUIDS);
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
        // Plastic.java:98
        add("Plastic.java:98", Gt6KtfruMachines.HEAT_MIXER, 168L, 200L,
                NO_ITEMS,
                new ItemStack[] {form("dust", "MagnesiumChloride", 1)},
                new FluidStack[] {field("GenLiquid_Methyltrichlorosilane", 100), field("GenLiquid_MethylmagnesiumChloride", 100)},
                new FluidStack[] {field("GenLiquid_Dichlorodimethylsilane", 100)});
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
        // OilProcessing.java:111
        add("OilProcessing.java:111", Gt6KtfruMachines.HEAT_MIXER, 217L, 15L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("GenGas_Propionaldehyde", 100), field("Hydrogen", 100)},
                new FluidStack[] {field("GenLiquid_Propanol", 100)});
        // OilProcessing.java:161
        add("OilProcessing.java:161", Gt6KtfruMachines.HEAT_MIXER, 280L, 160L,
                NO_ITEMS,
                NO_ITEMS,
                new FluidStack[] {field("Oxygen", 500), field("Propane", 1000)},
                new FluidStack[] {field("GenLiquid_Acetone", 500)});
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
    }

    private static void add(String where, String map, long euPerTick, long duration,
            ItemStack[] itemsIn, ItemStack[] itemsOut, FluidStack[] fluidsIn, FluidStack[] fluidsOut) {
        if (broken(itemsIn) || broken(fluidsIn) || broken(itemsOut) || broken(fluidsOut)) {
            return;
        }
        if (AddonRecipes.map(map).add(new AddonRecipes.Line(
                itemsIn, itemsOut, fluidsIn, fluidsOut, duration, euPerTick, 0)) == null) {
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

    private static FluidStack field(String name, int amount) {
        FluidStack stack = AddonFluids.still(name, amount);
        if (stack == null || stack.isEmpty()) {
            PROBLEMS.add("fluid " + name);
            return FluidStack.EMPTY;
        }
        return stack;
    }
}
