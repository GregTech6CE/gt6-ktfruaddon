package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.lombinaxmasson.gt6ktfruaddon.generated.Gt6KtfruItems;
import com.lombinaxmasson.gt6ktfruaddon.generated.Gt6KtfruSourceRecipes;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * The light-mixer row stays handwritten. Every other resolved source row is in
 * {@link Gt6KtfruSourceRecipes}. Temporary vanilla rows from card 02 are gone.
 */
public final class Gt6KtfruRecipes {
    private static final ItemStack[] NO_ITEMS = new ItemStack[0];

    private Gt6KtfruRecipes() {}

    public static void register() {
        Gt6KtfruSourceRecipes.register();
        fuelBattery();
        fuelDeburn();
        lightMixer();
        distillationTower(RecipeMaps.TINY_DISTILL, 40);
        distillationTower(RecipeMaps.SMALL_DISTILL, 30);
        distillationTower(RecipeMaps.DISTILL, 20);
        fusionTokamak();
        require(AddonRecipes.map(RecipeMaps.HEAT_MIXER).find(
                List.of(dust("Sulfur", 1)), List.of(fluid("Hydrogen", 2000)), true, 6, 3),
                "heat mixer sulfur row");
        require(AddonRecipes.map(RecipeMaps.FLUID_HEATING).find(
                List.of(), List.of(fluid("DistW", 10)), true, 0, 0),
                "fluid heating distilled water");
        require(AddonRecipes.map(RecipeMaps.LIGHT_MIXER).find(
                List.of(), List.of(fluid("Methane", 300), fluid("GenGas_Chlorine", 600)), true, 6, 3),
                "light mixer chloromethane row");
        if (AddonRecipes.map(RecipeMaps.CIRCUIT_ASSEMBLER).find(
                List.of(new ItemStack(Items.IRON_INGOT)), List.of(), true, 9, 3) != null) {
            throw new IllegalStateException("temporary assembler row is still present");
        }
        if (AddonRecipes.map(RecipeMaps.FLAW_DETECTOR).find(
                List.of(new ItemStack(Items.COAL)), List.of(), true, 3, 3) != null) {
            throw new IllegalStateException("temporary flaw detector row is still present");
        }
    }

    /**
     * Chemistry.java:61–79. The electrolyte is a 0 mB presence-only catalyst, so each
     * row names its tank fluid without draining it. Poles stay in the machine.
     */
    private static void fuelBattery() {
        ItemStack nickel = new ItemStack(Gt6KtfruItems.BATTERY_POLE_NICKEL.get());
        ItemStack catio3 = new ItemStack(Gt6KtfruItems.BATTERY_POLE_CATIO3.get());
        ItemStack carbon = new ItemStack(Gt6KtfruItems.BATTERY_POLE_CARBON.get());
        ItemStack platinum = new ItemStack(Gt6KtfruItems.BATTERY_POLE_PLATINUM.get());
        String yzo = "GenLiquid_YttriumZirconiumOxide";
        String sulfuric = "GenLiquid_SulfuricAcid";
        battery(yzo, nickel, catio3, -384, 9, two("Methane", 20, "Air", 80), two("CarbonDioxide", 20, "DistW", 40));
        battery(yzo, nickel, catio3, -166, 9, two("GenGas_CarbonMonoxide", 20, "Air", 20), one("CarbonDioxide", 20));
        battery(yzo, nickel, catio3, -16, 9, two("Hydrogen", 20, "Air", 20), one("DistW", 20));
        battery(yzo, nickel, catio3, -180, 9, two("GenLiquid_Methanol", 20, "Air", 60), two("CarbonDioxide", 20, "DistW", 40));
        battery(yzo, nickel, catio3, -288, 9, two("BioEthanol", 20, "Air", 120), two("CarbonDioxide", 40, "DistW", 60));
        battery(yzo, nickel, catio3, -384, 13, two("Methane", 20, "Oxygen", 40), two("CarbonDioxide", 20, "DistW", 40));
        battery(yzo, nickel, catio3, -166, 13, two("GenGas_CarbonMonoxide", 20, "Oxygen", 10), one("CarbonDioxide", 20));
        battery(yzo, nickel, catio3, -16, 13, two("Hydrogen", 20, "Oxygen", 10), one("DistW", 20));
        battery(yzo, nickel, catio3, -180, 13, two("GenLiquid_Methanol", 20, "Oxygen", 30), two("CarbonDioxide", 20, "DistW", 40));
        battery(yzo, nickel, catio3, -288, 13, two("BioEthanol", 20, "Oxygen", 60), two("CarbonDioxide", 40, "DistW", 60));
        battery(sulfuric, carbon, platinum, -16, 7, two("Hydrogen", 20, "Air", 10), one("DistW", 20));
        battery(sulfuric, carbon, platinum, -180, 7, two("GenLiquid_Methanol", 20, "Air", 30), two("CarbonDioxide", 20, "DistW", 40));
        battery(sulfuric, carbon, platinum, -288, 7, two("BioEthanol", 20, "Air", 60), two("CarbonDioxide", 40, "DistW", 60));
        battery(sulfuric, carbon, platinum, -16, 10, two("Hydrogen", 20, "Oxygen", 10), one("DistW", 20));
        battery(sulfuric, carbon, platinum, -180, 10, two("GenLiquid_Methanol", 20, "Oxygen", 30), two("CarbonDioxide", 20, "DistW", 40));
        battery(sulfuric, carbon, platinum, -288, 10, two("BioEthanol", 20, "Oxygen", 60), two("CarbonDioxide", 40, "DistW", 60));
        require(AddonRecipes.map(RecipeMaps.FUEL_BATTERY).find(
                List.of(nickel, catio3), List.of(fluid("Methane", 20), fluid("Air", 80), fluid(yzo, 1)), true, 2, 0),
                "fuel battery methane row");
        require(AddonRecipes.map(RecipeMaps.FUEL_BATTERY).find(
                List.of(carbon, platinum), List.of(fluid("Hydrogen", 20), fluid("Oxygen", 10), fluid(sulfuric, 1)), true, 2, 0),
                "fuel battery sulfuric row");
        require(AddonRecipes.map(RecipeMaps.HEAT_MIXER).find(
                List.of(dust("Yttrium", 4), dust("Zirconium", 4)), List.of(fluid("Oxygen", 4000)), true, 6, 3),
                "heat mixer yttrium zirconium oxide");
    }

    private static void battery(String electrolyte, ItemStack a, ItemStack b, long euPerTick, long duration,
            FluidStack[] fluidsIn, FluidStack[] fluidsOut) {
        FluidStack[] inputs = new FluidStack[fluidsIn.length + 1];
        System.arraycopy(fluidsIn, 0, inputs, 0, fluidsIn.length);
        inputs[fluidsIn.length] = catalyst(electrolyte);
        require(AddonRecipes.map(RecipeMaps.FUEL_BATTERY).add(new AddonRecipes.Line(
                new ItemStack[] {a, b}, NO_ITEMS, inputs, fluidsOut, duration, euPerTick, 0, null,
                new int[] {0, 1}, new int[] {fluidsIn.length})),
                RecipeMaps.FUEL_BATTERY);
    }

    /** A 1 mB kept input: the tank must hold the fluid, nothing is drained. */
    private static FluidStack catalyst(String field) {
        return fluid(field, 1);
    }

    private static FluidStack[] one(String field, int amount) {
        return new FluidStack[] {fluid(field, amount)};
    }

    private static FluidStack[] two(String a, int aAmount, String b, int bAmount) {
        return new FluidStack[] {fluid(a, aAmount), fluid(b, bAmount)};
    }

    /**
     * Chemistry.java:82–86. The circuit is a real input and the factory consumes it when the bar finishes.
     * Recycled-fuel fluids are the five source liquids. Exhaust rows stay on the engine and centrifuge maps.
     */
    private static void fuelDeburn() {
        deburn(0, 24, 3000, 638, new FluidStack[] {fluid("Water", 1000), fluid("CarbonDioxide", 2000)}, "GenLiquid_RecycledFuel0");
        deburn(1, 80, 5000, 922, new FluidStack[] {fluid("Water", 2000), fluid("CarbonDioxide", 4000)}, "GenLiquid_RecycledFuel1");
        deburn(2, 240, 5000, 1788, new FluidStack[] {fluid("Water", 4000), fluid("CarbonDioxide", 8000)}, "GenLiquid_RecycledFuel2");
        deburn(3, 800, 12800, 1464, new FluidStack[] {fluid("Water", 6000), fluid("CarbonDioxide", 20000), fluid("Nitrogen", 4000)}, "GenLiquid_RecycledFuel3");
        deburn(4, 1800, 51200, 2192, new FluidStack[] {fluid("Water", 16000), fluid("CarbonDioxide", 30000), fluid("Nitrogen", 8000)}, "GenLiquid_RecycledFuel4");
        require(AddonFluids.still("GenLiquid_AqueousOilHeavy", 1), "aqueous heavy oil");
        require(AddonRecipes.map(RecipeMaps.FUEL_DEBURN).find(
                List.of(AddonItems.selectorTag(0)),
                List.of(fluid("Water", 1000), fluid("CarbonDioxide", 2000)),
                true, 1, 0),
                "fuel deburner circuit 0");
    }

    private static void deburn(int circuit, long eu, long duration, long special, FluidStack[] inputs, String output) {
        ItemStack tag = AddonItems.selectorTag(circuit);
        if (tag == null || tag.isEmpty()) throw new IllegalStateException("missing selector circuit " + circuit);
        require(AddonRecipes.map(RecipeMaps.FUEL_DEBURN).add(new AddonRecipes.Line(
                new ItemStack[] {tag}, NO_ITEMS, inputs,
                new FluidStack[] {fluid(output, 100)},
                duration, eu, special)), "fuel deburner circuit " + circuit);
    }

    /** Plastic.java. The only LightMixer row. */
    private static void lightMixer() {
        add(RecipeMaps.LIGHT_MIXER, NO_ITEMS, NO_ITEMS,
                new FluidStack[] {fluid("Methane", 300), fluid("GenGas_Chlorine", 600)},
                new FluidStack[] {
                        fluid("GenLiquid_Chloromethane", 100),
                        fluid("GenLiquid_Dichloromethane", 100),
                        fluid("GenLiquid_Chloroform", 100),
                        fluid("GenGas_HydrochloricAcid", 600)},
                600, 32);
    }

    /**
     * Chemistry.java:107–109. Ocean water into bittern, distilled water and a small salt dust.
     * The three towers use the same row at 20, 30 and 40 ticks.
     */
    private static void distillationTower(String map, long duration) {
        add(map, NO_ITEMS,
                new ItemStack[] {prefix("dustSmall", "Salt", 1)},
                new FluidStack[] {fluid("Ocean", 1000)},
                new FluidStack[] {fluid("GenLiquid_Bittern", 100), fluid("DistW", 500)},
                duration, 64);
        require(AddonRecipes.map(map).find(
                List.of(), List.of(fluid("Ocean", 1000)), true, 1, 3),
                map + " ocean row");
    }

    private static void fusionTokamak() {
        require(AddonRecipes.map(RecipeMaps.FUSION_TOKAMAK).add(new AddonRecipes.Line(
                NO_ITEMS, NO_ITEMS,
                new FluidStack[] {fluid("Deuterium", 144), fluid("Tritium", 144)},
                new FluidStack[] {fluid("Helium", 144)},
                400, 2048, 1_048_576L)), RecipeMaps.FUSION_TOKAMAK);
    }

    private static void add(String map, ItemStack[] itemsIn, ItemStack[] itemsOut,
            FluidStack[] fluidsIn, FluidStack[] fluidsOut, long duration, long euPerTick) {
        require(AddonRecipes.map(map).add(new AddonRecipes.Line(
                itemsIn, itemsOut, fluidsIn, fluidsOut, duration, euPerTick, 0)), map);
    }

    private static ItemStack dust(String material, int count) {
        return prefix("dust", material, count);
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
