package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.FuelGenerator;
import com.gregtech.gregtech.api.addon.FuelOutput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * The fuel generators, 10000–10160 upstream: seven gas turbines on the gas fuel table and the
 * diesel and turbo diesel engines on the engine fuel table. All of them output RU from the front.
 */
public final class Gt6KtfruFuelGenerators implements Feature {
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
