package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.RecipeMachine;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * The two catalytic reactors: large recipe machines with cheap overclocking.
 */
public final class Gt6KtfruCatalyticMachines implements Feature {
    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.CATALYTIC_REACTOR, "Catalytic Reactor", "mixer", 6, 3, 0, 6, 3, 0, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(catalytic("catalytic_reactor_ev", "StainlessSteel", 2048, 256, 4096));
        machines.add(catalytic("catalytic_reactor_iv", "Tungstensteel", 8192, 1024, 16384));
        return List.copyOf(machines);
    }

    private static RecipeMachine catalytic(String id, String material, long eu, long min, long max) {
        return RecipeMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .recipeMap(RecipeMaps.CATALYTIC_REACTOR)
                .largeRecipe()
                .cheapOverclocking(true)
                .energy(EnergyInput.builder("EU", eu).range(min, max).faces().build())
                .structureKeys(List.of("gt6ktfruaddon.structure." + id + ".1"))
                .build();
    }
}
