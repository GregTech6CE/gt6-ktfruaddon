package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.FuelGenerator;
import com.gregtech.gregtech.api.addon.FuelOutput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * What is not a feature of its own yet; WP3 step 3 cuts it up.
 * Single-block machines from {@code tileEntityInit0}. Recipe machines are
 * 20000–27542. Fuel generators are the gas turbines and diesel engines from
 * 10000–10160. The large recipe machines are the three distillation towers and
 * the ore processing system. The two large compressed-gas valves and the five
 * gas compressors are behavior machines. Teflon fluid pipes, PVC item pipes
 * and the five compressed-gas drums are separate declarations. Card 08 adds the
 * void hoppers, miners, water mill, manual motors and flywheel boxes.
 * Card 09 adds the two gas batteries. Rows keep both poles and leave the electrolyte out.
 * Card 10 adds the transformer, flywheel-gear and light-transformer parts.
 * Card 11 adds the mask aligners, the CNC machine and their structure parts.
 * Card 12 adds the sun heater, large mirror, tidal generator, oil miner,
 * mantle heater and fuel deburn factory.
 * Card 13 adds the large gas and steam turbines, dynamos, expanded motors and fluid boiler.
 * Card 14 adds the liquid batteries and transform batteries.
 * Card 15 adds the research tables, microscopes, assembler and reactor.
 * Card 16 adds the wireless, biology, quantum and spacetime controllers, plus the compute nodes.
 * Card 17 adds the inductive tower, the quantum hub and both receivers.
 * Card 18 adds the late recipe machines, tokamak layouts, catalytic reactors, microgravity and AI centers.
 * Card 19 adds the electromagnetic crucible and its screen.
 */
public final class Gt6KtfruBaseMachines implements Feature {
    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.FUEL_DEBURN, "Fuel Deburner", "fueldeburner", 3, 3, 0, 6, 6, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.FUSION_TOKAMAK, "Fusion Tokamak", "fusiontokamak", 3, 3, 0, 6, 6, 1, 1));
        maps.add(RecipeMaps.table(RecipeMaps.FUSION_TOKAMAK_EXPERIMENTAL, "Experimental Fusion Tokamak", "fusiontokamak", 3, 3, 0, 6, 6, 1, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        gas(machines, "bronze", "Bronze", 16);
        gas(machines, "steel", "Steel", 32);
        gas(machines, "invar", "Invar", 64);
        gas(machines, "titanium", "Titanium", 128);
        gas(machines, "tungstensteel", "Tungstensteel", 372);
        gas(machines, "iridium", "Iridium", 768);
        gas(machines, "chromium", "Chromium", 256);
        diesel(machines, "diesel", "bronze", "Bronze", 3300, 16);
        diesel(machines, "diesel", "arsenic_copper", "ArsenicCopper", 3300, 16);
        diesel(machines, "diesel", "arsenic_bronze", "ArsenicBronze", 3300, 24);
        diesel(machines, "diesel", "steel", "Steel", 3300, 32);
        diesel(machines, "diesel", "invar", "Invar", 3300, 64);
        diesel(machines, "diesel", "titanium", "Titanium", 3300, 128);
        diesel(machines, "diesel", "tungstensteel", "Tungstensteel", 3300, 372);
        diesel(machines, "diesel", "iridium", "Iridium", 3300, 768);
        diesel(machines, "diesel", "chromium", "Chromium", 3300, 256);
        diesel(machines, "turbo_diesel", "bronze", "Bronze", 5200, 24);
        diesel(machines, "turbo_diesel", "arsenic_copper", "ArsenicCopper", 5200, 24);
        diesel(machines, "turbo_diesel", "arsenic_bronze", "ArsenicBronze", 5200, 36);
        diesel(machines, "turbo_diesel", "steel", "Steel", 5200, 48);
        diesel(machines, "turbo_diesel", "invar", "Invar", 5200, 96);
        diesel(machines, "turbo_diesel", "titanium", "Titanium", 5200, 172);
        diesel(machines, "turbo_diesel", "tungstensteel", "Tungstensteel", 5200, 558);
        diesel(machines, "turbo_diesel", "iridium", "Iridium", 5200, 1152);
        diesel(machines, "turbo_diesel", "chromium", "Chromium", 5200, 384);
        diesel(machines, "turbo_diesel", "osmium", "Osmium", 5000, 2304);
        diesel(machines, "turbo_diesel", "trinaquadalloy", "Trinaquadalloy", 4800, 4608);
        return List.copyOf(machines);
    }

    private static void gas(List<AddonMachine> out, String suffix, String material, long perTick) {
        out.add(fuel("gas_turbine_" + suffix, material, "gas_turbine_" + suffix, RecipeMaps.GAS_FUEL, 3500, perTick));
    }

    private static void diesel(List<AddonMachine> out, String prefix, String suffix, String material,
            int efficiency, long perTick) {
        String id = prefix + "_" + suffix;
        out.add(fuel(id, material, id, RecipeMaps.ENGINE_FUEL, efficiency, perTick));
    }

    private static FuelGenerator fuel(String id, String material, String type, String fuelMap,
            int efficiency, long perTick) {
        return FuelGenerator.builder(MachineCasing.builder(id, material).machineType(type).build())
                .fuelMap(fuelMap)
                .efficiency(efficiency)
                .output(FuelOutput.builder("RU", perTick).front().build())
                // Vanilla MotorLiquid sizes both tanks at rate * 10. The TFRU fork uses 16 and 128;
                // this template has one capacity for the fuel tank and every exhaust tank.
                .tank(Math.toIntExact(perTick * 10))
                .single()
                .build();
    }

}
