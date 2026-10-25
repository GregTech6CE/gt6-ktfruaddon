package com.lombinaxmasson.gt6ktfruaddon.fusion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.RecipeMachine;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * The fusion machines: the experimental and tier 1 tokamaks, large recipe machines that charge each
 * recipe's start-up energy from an LU buffer, and the two laser fusion controllers (30071, 30072) with
 * discrete LU pulse storage, a peak check and mandatory cooling.
 */
public final class FusionMachines implements Feature {
    static final String LASER_EXP = "laser_fusion_experimental";
    static final String LASER_IND = "laser_fusion_industrial";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(LASER_EXP, LaserFusion::experimental)
            .add(LASER_IND, LaserFusion::industrial)
            .build();

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.FUSION_TOKAMAK, "Fusion Tokamak", "fusiontokamak", 3, 3, 0, 6, 6, 1, 1));
        maps.add(RecipeMaps.table(RecipeMaps.FUSION_TOKAMAK_EXPERIMENTAL, "Experimental Fusion Tokamak", "fusiontokamak", 3, 3, 0, 6, 6, 1, 1));
        maps.add(RecipeMaps.table(RecipeMaps.LASER_FUSION, "Laser Inertial Fusion", "gascompressor", 3, 3, 0, 3, 3, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.LASER_FUSION_EXPERIMENTAL, "Experimental Laser Fusion", "gascompressor", 3, 3, 0, 3, 3, 0, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(tokamak("fusion_tokamak_exp", 2048, RecipeMaps.FUSION_TOKAMAK_EXPERIMENTAL));
        machines.add(tokamak("fusion_tokamak_t1", 16384, RecipeMaps.FUSION_TOKAMAK));
        machines.add(laser(LASER_EXP, RecipeMaps.LASER_FUSION_EXPERIMENTAL, "Tungstensteel", 131_072L, 2L * 8L * 1024L * 1024L));
        machines.add(laser(LASER_IND, RecipeMaps.LASER_FUSION, "Osmium", 2_097_152L, 8L * 134_217_728L));
        return List.copyOf(machines);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    private static RecipeMachine tokamak(String id, long eu, String map) {
        // Source: NBT_ENERGY_ACCEPTED_2 is LU with NBT_SPECIAL_IS_START_ENERGY.
        // Each recipe's special value is charged from that buffer before progress.
        return RecipeMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .recipeMap(map)
                .largeRecipe()
                .constantEnergy(false)
                .energy(EnergyInput.builder("EU", eu).faces().build())
                .auxiliaryEnergy(EnergyInput.builder("LU", eu)
                        .anyPacket()
                        .faces(MachineFace.all())
                        .capacity(4_194_304L)
                        .build())
                .startupEnergy(true)
                .structureKeys(List.of("gt6ktfruaddon.structure." + id + ".1"))
                .build();
    }

    private static BehaviorMachine laser(String id, String map, String material, long maxPacket, long pulseCapacity) {
        return BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("LU", maxPacket / 2)
                        .faces(MachineFace.TOP)
                        .capacity(pulseCapacity)
                        .build())
                .auxiliaryEnergy(EnergyInput.builder("LU", maxPacket)
                        .range(1, maxPacket)
                        .faces(MachineFace.BOTTOM, MachineFace.BACK, MachineFace.LEFT, MachineFace.RIGHT)
                        .capacity(pulseCapacity)
                        .build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .energyGauge(84, 8)
                .progress(84, 64)
                .item(8, 26, LaserFusion.SLOTS.itemRole(0))
                .item(26, 26, LaserFusion.SLOTS.itemRole(1))
                .item(156, 26, LaserFusion.SLOTS.itemRole(2))
                .fluid(8_000, 46, 8, LaserFusion.SLOTS.fluidRole(0))
                .fluid(8_000, 66, 8, LaserFusion.SLOTS.fluidRole(1))
                .fluid(8_000, 116, 8, LaserFusion.SLOTS.fluidRole(2))
                .fluid(8_000, 136, 8, LaserFusion.SLOTS.fluidRole(3))
                .recipeMaps(map)
                .build();
    }
}
