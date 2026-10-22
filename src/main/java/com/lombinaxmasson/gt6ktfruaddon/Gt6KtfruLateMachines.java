package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.MachineIo;
import com.gregtech.gregtech.api.addon.RecipeMachine;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/**
 * Late recipe machines and the behavior multiblocks that are not compute or wireless energy.
 * Laser chambers accept LU in the source pulse window. They do not store a pulse or run a cooldown.
 * Tokamaks keep EU only. The LU start charge, field and compute check are not on the recipe machine.
 * The quantum relay does not emit QU.
 */
public final class Gt6KtfruLateMachines {
    static final String LASER = "gt6ktfruaddon.recipe.laser_fusion";
    static final String CATALYTIC = "gt6ktfruaddon.recipe.catalytic_reactor";
    static final String MIRROR = "gt6ktfruaddon.recipe.mirror_gold_synthesis";
    static final String MIRROR_QUANTUM = "gt6ktfruaddon.recipe.mirror_gold_quantumization";
    static final String COSMIC = "gt6ktfruaddon.recipe.cosmic_radiation";
    static final String CRYSTAL = "gt6ktfruaddon.recipe.microgravity_crystal";
    static final String QUANTUM_GOLD = "gt6ktfruaddon.recipe.quantum_gold";
    static final String OBSERVATION = "gt6ktfruaddon.recipe.quantum_observation";
    static final String RELAY_MAP = "gt6ktfruaddon.recipe.quantum_communication";
    static final String ROOM = "centrifugal_microgravity";
    static final String FLOW = "ai_flow_control";
    static final String FILL = "ai_fill_planning";
    static final String PATTERN = "ai_pattern_recognition";
    private static final String GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";

    private Gt6KtfruLateMachines() {}

    public static void addMaps(List<RecipeTable> maps) {
        maps.add(Gt6KtfruMachines.map(LASER, "Laser Inertial Fusion", "gascompressor", 3, 3, 0, 3, 3, 0, 1));
        maps.add(Gt6KtfruMachines.map(CATALYTIC, "Catalytic Reactor", "gascompressor", 6, 3, 0, 6, 3, 0, 1));
        maps.add(Gt6KtfruMachines.map(MIRROR, "Mirror Gold Synthesis", "gascompressor", 5, 5, 0, 2, 2, 0, 1));
        maps.add(Gt6KtfruMachines.map(MIRROR_QUANTUM, "Mirror Gold Quantumization", "gascompressor", 5, 5, 0, 2, 2, 0, 1));
        maps.add(Gt6KtfruMachines.map(COSMIC, "Cosmic Radiation Research", "gascompressor", 1, 3, 0, 0, 0, 0, 1));
        maps.add(Gt6KtfruMachines.map(CRYSTAL, "Microgravity Crystal Growth", "gascompressor", 1, 1, 0, 2, 0, 0, 1));
        maps.add(Gt6KtfruMachines.map(QUANTUM_GOLD, "Quantum Gold Synthesis", "gascompressor", 4, 4, 0, 2, 2, 0, 1));
        maps.add(Gt6KtfruMachines.map(OBSERVATION, "Quantum Observation", "gascompressor", 4, 4, 0, 2, 2, 0, 1));
        maps.add(Gt6KtfruMachines.map(RELAY_MAP, "Quantum Communication", "gascompressor", 1, 1, 0, 0, 0, 0, 1));
    }

    public static void add(List<AddonMachine> machines) {
        machines.add(tokamak("fusion_tokamak_exp", 2048));
        machines.add(tokamak("fusion_tokamak_t1", 16384));
        machines.add(laser("laser_fusion_experimental", "Tungstensteel", 131072));
        machines.add(laser("laser_fusion_industrial", "Osmium", 2097152));
        machines.add(catalytic("catalytic_reactor_ev", "StainlessSteel", 2048, 256, 4096));
        machines.add(catalytic("catalytic_reactor_iv", "Tungstensteel", 8192, 1024, 16384));
        machines.add(basic("mirror_gold_synthesis", "Iridium", 8192, 8192, 8192, MIRROR, true));
        machines.add(basic("mirror_gold_quantumization", "Naquadria", 16384, 16384, 16384, MIRROR_QUANTUM, true));
        machines.add(basic("cosmic_radiation_chamber", "Tungstensteel", 8192, 2048, 16384, COSMIC, false));
        machines.add(basic("microgravity_assembler", "Tungstensteel", 8192, 2048, 16384, COSMIC, false));
        machines.add(large("quantum_gold_chamber", "Naquadria", 65536, 65536, 65536, QUANTUM_GOLD));
        machines.add(large("quantum_observation_tower", "Trinaquadalloy", 16384, 16384, 16384, OBSERVATION));
        machines.add(large("microgravity_crystal_growth", "Titanium", 8192, 1024, 16384, CRYSTAL));
        machines.add(basic("quantum_communication_relay", "Iridium", 8192, 8192, 8192, RELAY_MAP, false));
        machines.add(room());
        machines.add(ai(FLOW));
        machines.add(ai(FILL));
        machines.add(ai(PATTERN));
    }

    public static Set<String> behaviorTypes() {
        return Set.of(ROOM, FLOW, FILL, PATTERN);
    }

    public static boolean owns(String machineType) {
        return behaviorTypes().contains(machineType);
    }

    public static MachineBehavior create(String machineType) {
        if (ROOM.equals(machineType)) return new Gt6KtfruLateBehavior(true, 0);
        long points = FLOW.equals(machineType) ? 8 : FILL.equals(machineType) ? 4 : 2;
        return new Gt6KtfruLateBehavior(false, points);
    }

    private static RecipeMachine tokamak(String id, long eu) {
        return RecipeMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .recipeMap(Gt6KtfruMachines.FUSION_TOKAMAK)
                .largeRecipe()
                .constantEnergy(false)
                .energy(EnergyInput.builder("EU", eu).faces().build())
                .structureKeys(List.of("gt6ktfruaddon.structure." + id + ".1"))
                .build();
    }

    private static RecipeMachine laser(String id, String material, long lu) {
        return RecipeMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .recipeMap(LASER)
                .largeRecipe()
                .constantEnergy(false)
                .energy(EnergyInput.builder("LU", lu).range(1, lu).faces().build())
                .structureKeys(List.of("gt6ktfruaddon.structure." + id + ".1"))
                .build();
    }

    private static RecipeMachine catalytic(String id, String material, long eu, long min, long max) {
        return RecipeMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .recipeMap(CATALYTIC)
                .largeRecipe()
                .cheapOverclocking(true)
                .energy(EnergyInput.builder("EU", eu).range(min, max).faces().build())
                .structureKeys(List.of("gt6ktfruaddon.structure." + id + ".1"))
                .build();
    }

    private static RecipeMachine large(String id, String material, long eu, long min, long max, String map) {
        var energy = EnergyInput.builder("EU", eu);
        if (min != max) energy.range(min, max);
        return RecipeMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .recipeMap(map)
                .largeRecipe()
                .energy(energy.faces().build())
                .structureKeys(List.of("gt6ktfruaddon.structure." + id + ".1"))
                .build();
    }

    private static RecipeMachine basic(String id, String material, long eu, long min, long max, String map, boolean fluids) {
        var energy = EnergyInput.builder("EU", eu).faces(MachineFace.BACK);
        if (min != max) energy.range(min, max);
        var io = MachineIo.builder()
                .itemInputs(MachineFace.TOP).itemAutoInput(MachineFace.TOP)
                .itemOutputs(MachineFace.BOTTOM).itemAutoOutput(MachineFace.BOTTOM);
        if (fluids) {
            io.fluidInputs(MachineFace.LEFT).fluidAutoInput(MachineFace.LEFT)
                    .fluidOutputs(MachineFace.RIGHT).fluidAutoOutput(MachineFace.RIGHT);
        }
        return RecipeMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .recipeMap(map)
                .basic()
                .energy(energy.build())
                .io(io.build())
                .build();
    }

    private static BehaviorMachine room() {
        return BehaviorMachine.builder(MachineCasing.builder(ROOM, "Tungstensteel").machineType(ROOM).build())
                .energy(EnergyInput.builder("EU", 2048).range(2048, 8192).faces(MachineFace.BOTTOM).capacity(65536).build())
                .gui(GUI)
                .build();
    }

    private static BehaviorMachine ai(String id) {
        return BehaviorMachine.builder(MachineCasing.builder(id, "Tungstensteel").machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(GUI)
                .build();
    }
}
