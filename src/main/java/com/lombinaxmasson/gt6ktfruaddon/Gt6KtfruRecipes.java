package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;

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
        lightMixer();
        require(AddonRecipes.map(Gt6KtfruMachines.HEAT_MIXER).find(
                List.of(dust("Sulfur", 1)), List.of(fluid("Hydrogen", 2000)), true, 6, 3),
                "heat mixer sulfur row");
        require(AddonRecipes.map(Gt6KtfruMachines.FLUID_HEATING).find(
                List.of(), List.of(fluid("DistW", 10)), true, 0, 0),
                "fluid heating distilled water");
        require(AddonRecipes.map(Gt6KtfruMachines.LIGHT_MIXER).find(
                List.of(), List.of(fluid("Methane", 300), fluid("GenGas_Chlorine", 600)), true, 6, 3),
                "light mixer chloromethane row");
        if (AddonRecipes.map(Gt6KtfruMachines.CIRCUIT_ASSEMBLER).find(
                List.of(new ItemStack(Items.IRON_INGOT)), List.of(), true, 9, 3) != null) {
            throw new IllegalStateException("temporary assembler row is still present");
        }
        if (AddonRecipes.map(Gt6KtfruMachines.FLAW_DETECTOR).find(
                List.of(new ItemStack(Items.COAL)), List.of(), true, 3, 3) != null) {
            throw new IllegalStateException("temporary flaw detector row is still present");
        }
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
