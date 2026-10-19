package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachineContributor;
import com.gregtech.gregtech.api.addon.FuelGeneratorDeclaration;
import com.gregtech.gregtech.api.addon.FuelMapDeclaration;
import com.gregtech.gregtech.api.addon.MachineDeclaration;
import com.gregtech.gregtech.api.addon.MachineLayout;
import com.gregtech.gregtech.api.addon.RecipeMapDeclaration;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * A basic crystal puller and a tank-wall hydrogen burner. Both use casings GregTech already has.
 */
public final class Gt6KtfruMachines implements AddonMachineContributor {
    @Override
    public String addonId() {
        return Gt6KtfruAddon.MODID;
    }

    @Override
    public List<RecipeMapDeclaration> recipeMaps() {
        return List.of(new RecipeMapDeclaration(
                "gt6ktfruaddon.recipe.crystal_puller",
                "Crystal Puller",
                "gt6ktfruaddon:textures/gui/machines/puller",
                1, 1, 1,
                0, 0, 0, 1,
                true, false, false, false));
    }

    @Override
    public List<MachineDeclaration> machines() {
        return List.of(MachineDeclaration.builder("crystal_puller", "Steel")
                .machineType("crystal_puller")
                .energy("EU", 128)
                .tier(1)
                .recipeMap("gt6ktfruaddon.recipe.crystal_puller")
                .basic()
                .build());
    }

    @Override
    public List<MachineLayout> layouts() {
        return List.of(new MachineLayout("hydrogen_burner", List.of(
                new MachineLayout.Cell(-1, 0, 1, "tank_wall", StructureRole.FLUID_INPUT),
                new MachineLayout.Cell(0, 0, 1, "tank_wall", StructureRole.FLUID_INPUT),
                new MachineLayout.Cell(1, 0, 1, "tank_wall", StructureRole.FLUID_INPUT))));
    }

    @Override
    public List<FuelMapDeclaration> fuelMaps() {
        return List.of(new FuelMapDeclaration(
                "gt6ktfruaddon.fuel.hydrogen",
                "Hydrogen Burner",
                "gt6ktfruaddon:textures/gui/machines/burner",
                0, 0, 0,
                1, 1, 1, 1,
                false, false, false, false));
    }

    @Override
    public List<FuelGeneratorDeclaration> fuelGenerators() {
        return List.of(FuelGeneratorDeclaration.builder("hydrogen_burner", "Steel")
                .machineType("hydrogen_burner")
                .fuelMap("gt6ktfruaddon.fuel.hydrogen")
                .efficiency(10_000)
                .energy("RU", 32)
                .tank(16_000)
                .outputBehind(2)
                .multi()
                .build());
    }
}
