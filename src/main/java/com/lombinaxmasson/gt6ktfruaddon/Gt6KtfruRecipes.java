package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * One row per map. Rows marked temporary stand in until the source outputs
 * exist. One material unit of gas or liquid is 1000 mB; {@code FL.make(n)} stays n mB.
 */
public final class Gt6KtfruRecipes {
    private static final ItemStack[] NO_ITEMS = new ItemStack[0];
    private static final FluidStack[] NO_FLUIDS = new FluidStack[0];

    private Gt6KtfruRecipes() {}

    public static void register() {
        fluidHeating();
        lightMixer();
        circuitAssembler();
        laserCutter();
        heatMixer();
        electronicsDesigner();
        flawDetector();
        waferTester();
        waferCoater();
        ultrasonicMixer();
        require(AddonRecipes.map(Gt6KtfruMachines.HEAT_MIXER).find(
                List.of(dust("Iron", 1)), List.of(), true, 6, 3), "heat mixer iron row");
        require(AddonRecipes.map(Gt6KtfruMachines.FLUID_HEATING).find(
                List.of(), List.of(fluid("DistW", 10)), true, 0, 0), "fluid heating distilled water");
    }

    /** Chemistry.java. The coolant pair is D-tier and still added. */
    private static void fluidHeating() {
        add(Gt6KtfruMachines.FLUID_HEATING, NO_ITEMS, NO_ITEMS,
                new FluidStack[] {fluid("DistW", 10)}, new FluidStack[] {fluid("Steam", 1600)}, 20, 80);
        add(Gt6KtfruMachines.FLUID_HEATING, NO_ITEMS, NO_ITEMS,
                new FluidStack[] {fluid("Coolant_IC2", 10)}, new FluidStack[] {fluid("Coolant_IC2_Hot", 10)}, 10, 20);
    }

    /** Plastic.java. The only LightMixer row. */
    private static void lightMixer() {
        add(Gt6KtfruMachines.LIGHT_MIXER, NO_ITEMS, NO_ITEMS,
                new FluidStack[] {fluid("Methane", 300), fluid("GenGas_Chlorine", 600)},
                new FluidStack[] {
                        fluid("GenLiquid_Chloromethane", 100),
                        fluid("GenLiquid_Dichloromethane", 100),
                        fluid("GenLiquid_Chloroform", 100),
                        fluid("GenGas_HydrochloricAcid", 600)},
                600, 32);
    }

    /** Temporary. Source outputs are unported circuit parts. */
    private static void circuitAssembler() {
        add(Gt6KtfruMachines.CIRCUIT_ASSEMBLER,
                new ItemStack[] {new ItemStack(Items.IRON_INGOT)},
                new ItemStack[] {new ItemStack(Items.IRON_NUGGET)},
                NO_FLUIDS, NO_FLUIDS, 40, 16);
    }

    /** Chemistry.java. 500 LU, so the HV cutter and above can run it. */
    private static void laserCutter() {
        add(Gt6KtfruMachines.LASER_CUTTER,
                new ItemStack[] {prefix("plate", "Graphene", 1)},
                new ItemStack[] {prefix("stick", "Graphene", 2)},
                NO_FLUIDS, NO_FLUIDS, 200, 500);
    }

    /**
     * HeatMixer.java sulfur rows, plus a temporary iron dust to iron ingot.
     * The LiOH row needs liquid lithium chlorate, which this GT6CE build does not have.
     */
    private static void heatMixer() {
        add(Gt6KtfruMachines.HEAT_MIXER,
                new ItemStack[] {dust("Sulfur", 1)}, NO_ITEMS,
                new FluidStack[] {fluid("Hydrogen", 2000)},
                new FluidStack[] {fluid("GenGas_HydrosulfuricAcid", 3000)},
                48, 16);
        add(Gt6KtfruMachines.HEAT_MIXER,
                NO_ITEMS, new ItemStack[] {dust("Sulfur", 1)},
                new FluidStack[] {fluid("GenGas_HydrosulfuricAcid", 2000), fluid("GenGas_SulfurDioxide", 1000)},
                new FluidStack[] {fluid("Water", 2000)},
                48, 16);
        add(Gt6KtfruMachines.HEAT_MIXER,
                new ItemStack[] {dust("Iron", 1)}, new ItemStack[] {ingot("Iron", 1)},
                NO_FLUIDS, NO_FLUIDS, 48, 16);
    }

    /** Temporary. Source rows consume photomasks and research data. */
    private static void electronicsDesigner() {
        add(Gt6KtfruMachines.ELECTRONICS_DESIGNER,
                new ItemStack[] {new ItemStack(Items.GOLD_INGOT)},
                new ItemStack[] {new ItemStack(Items.GOLD_NUGGET)},
                NO_FLUIDS, NO_FLUIDS, 40, 16);
    }

    /** Temporary. Source rows are chance splits, and Line has no chances. */
    private static void flawDetector() {
        add(Gt6KtfruMachines.FLAW_DETECTOR,
                new ItemStack[] {new ItemStack(Items.COAL)},
                new ItemStack[] {new ItemStack(Items.CHARCOAL)},
                NO_FLUIDS, NO_FLUIDS, 40, 16);
    }

    /** Temporary. Source rows consume unported wafers. */
    private static void waferTester() {
        add(Gt6KtfruMachines.WAFER_TESTER,
                new ItemStack[] {new ItemStack(Items.FLINT)},
                new ItemStack[] {new ItemStack(Items.GRAVEL)},
                NO_FLUIDS, NO_FLUIDS, 40, 16);
    }

    /** Temporary. Source rows consume unported wafers and photoresist. */
    private static void waferCoater() {
        add(Gt6KtfruMachines.WAFER_COATER,
                new ItemStack[] {new ItemStack(Items.CLAY_BALL)},
                new ItemStack[] {new ItemStack(Items.BRICK)},
                NO_FLUIDS, NO_FLUIDS, 40, 16);
    }

    /** Temporary. Source rows need custom solvents. 256 EU so the HV machine can run it. */
    private static void ultrasonicMixer() {
        add(Gt6KtfruMachines.ULTRASONIC_MIXER,
                new ItemStack[] {new ItemStack(Items.SAND)},
                new ItemStack[] {new ItemStack(Items.GLASS)},
                NO_FLUIDS, NO_FLUIDS, 40, 256);
    }

    private static void add(String map, ItemStack[] itemsIn, ItemStack[] itemsOut,
            FluidStack[] fluidsIn, FluidStack[] fluidsOut, long duration, long euPerTick) {
        require(AddonRecipes.map(map).add(new AddonRecipes.Line(
                itemsIn, itemsOut, fluidsIn, fluidsOut, duration, euPerTick, 0)), map);
    }

    private static ItemStack dust(String material, int count) {
        return prefix("dust", material, count);
    }

    private static ItemStack ingot(String material, int count) {
        return prefix("ingot", material, count);
    }

    private static ItemStack prefix(String prefix, String material, int count) {
        ItemStack stack = AddonItems.stack(prefix, material, count);
        if (stack == null || stack.isEmpty()) {
            throw new IllegalStateException("missing " + prefix + " " + material);
        }
        return stack;
    }

    private static FluidStack fluid(String field, int amount) {
        FluidStack stack = AddonFluids.still(field, amount);
        if (stack == null || stack.isEmpty()) {
            throw new IllegalStateException("missing fluid " + field);
        }
        return stack;
    }

    private static void require(Object value, String label) {
        if (value == null) {
            throw new IllegalStateException(label + " was rejected");
        }
    }
}
