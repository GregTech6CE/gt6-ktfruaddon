package com.lombinaxmasson.gt6ktfruaddon;

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
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * The two gas batteries. Their rows keep both poles and leave the electrolyte out.
 */
public final class Gt6KtfruGasBatteries implements Feature {
    public static final String GAS_BATTERY = "gas_battery";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(GAS_BATTERY, Gt6KtfruFuelBattery::new)
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
        long rate = Gt6KtfruFuelBattery.rate(tier);
        int electrolyte = Gt6KtfruFuelBattery.electrolyte(tier);
        return BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(GAS_BATTERY).tier(tier).build())
                .energy(EnergyInput.builder("EU", rate).faces(MachineFace.BOTTOM)
                        .capacity(Gt6KtfruFuelBattery.capacity(rate)).build())
                .energyOutput(EnergyOutput.builder("EU", rate, 1).faces(MachineFace.TOP).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .progress(8, 64)
                .toggle("gt6ktfruaddon.fuel_battery.stopped", 44, 64, false)
                .toggle("gt6ktfruaddon.fuel_battery.electrolyte", 64, 64, false)
                .item(26, 8, BehaviorMachine.SlotRole.INPUT)
                .item(48, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(8000, 80, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(8000, 98, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(8000, 116, 8, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(8000, 134, 8, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(electrolyte, 152, 8, BehaviorMachine.SlotRole.INPUT)
                .recipeMaps(RecipeMaps.FUEL_BATTERY)
                .build();
    }
}
