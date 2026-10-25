package com.lombinaxmasson.gt6ktfruaddon.power;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * The two gas batteries. Their rows keep both poles and leave the electrolyte out.
 */
public final class GasBatteries implements Feature {
    public static final String GAS_BATTERY = "gas_battery";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(GAS_BATTERY, FuelBattery::new)
            .build();

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.FUEL_BATTERY, "Fuel Battery", "fuelbattery", 2, 0, 2, 3, 2, 2, 4));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(gasBattery("gas_battery_aluminium", "Aluminium", 1));
        machines.add(gasBattery("gas_battery_stainless", "StainlessSteel", 2));
        return List.copyOf(machines);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    /**
     * Source 10010 / 10011. EU leaves the top. The bottom input exists because a
     * behavior machine needs one. Fuel tanks are 8000 mB. The electrolyte tank
     * is exactly the required amount. Toggle 0 stops processing. Toggle 1
     * opens the electrolyte tank.
     */
    private static BehaviorMachine gasBattery(String id, String material, int tier) {
        long rate = FuelBattery.rate(tier);
        int electrolyte = FuelBattery.electrolyte(tier);
        return BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(GAS_BATTERY).tier(tier).build())
                .energy(EnergyInput.builder("EU", rate).faces(MachineFace.BOTTOM)
                        .capacity(FuelBattery.capacity(rate)).build())
                .energyOutput(EnergyOutput.builder("EU", rate, 1).faces(MachineFace.TOP).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .progress(8, 64)
                .toggle("gt6ktfruaddon.fuel_battery.stopped", 44, 64, false)
                .toggle("gt6ktfruaddon.fuel_battery.electrolyte", 64, 64, false)
                .item(26, 8, FuelBattery.SLOTS.itemRole(0))
                .item(48, 8, FuelBattery.SLOTS.itemRole(1))
                .fluid(8000, 80, 8, FuelBattery.SLOTS.fluidRole(0))
                .fluid(8000, 98, 8, FuelBattery.SLOTS.fluidRole(1))
                .fluid(8000, 116, 8, FuelBattery.SLOTS.fluidRole(2))
                .fluid(8000, 134, 8, FuelBattery.SLOTS.fluidRole(3))
                .fluid(electrolyte, 152, 8, FuelBattery.SLOTS.fluidRole(4))
                .recipeMaps(RecipeMaps.FUEL_BATTERY)
                .build();
    }
}
