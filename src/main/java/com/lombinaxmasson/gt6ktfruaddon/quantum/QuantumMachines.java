package com.lombinaxmasson.gt6ktfruaddon.quantum;

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
import com.gregtech.gregtech.api.addon.MachineIo;
import com.gregtech.gregtech.api.addon.RecipeMachine;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * The quantum chain, whose recipes the source keeps in QuantumEntry and QuantumObservation: the two mirror
 * gold machines, the quantum gold chamber, the quantum observation tower and the quantum communication
 * relay (30074), a paired EU-to-QU recipe runner that emits 128 QU/tick from the top face.
 */
public final class QuantumMachines implements Feature {
    static final String QUANTUM_RELAY = "quantum_communication_relay";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(QUANTUM_RELAY, QuantumRelay::new)
            .build();

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.MIRROR_GOLD_SYNTHESIS, "Mirror Gold Synthesis", "gascompressor", 5, 5, 0, 2, 2, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.MIRROR_GOLD_QUANTUMIZATION, "Mirror Gold Quantumization", "gascompressor", 5, 5, 0, 2, 2, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.QUANTUM_GOLD, "Quantum Gold Synthesis", "gascompressor", 4, 4, 0, 2, 2, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.QUANTUM_OBSERVATION, "Quantum Observation", "gascompressor", 4, 4, 0, 2, 2, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.QUANTUM_COMMUNICATION, "Quantum Communication", "gascompressor", 1, 1, 0, 0, 0, 0, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(mirrorGold("mirror_gold_synthesis", "Iridium", 8192, RecipeMaps.MIRROR_GOLD_SYNTHESIS));
        machines.add(mirrorGold("mirror_gold_quantumization", "Naquadria", 16384, RecipeMaps.MIRROR_GOLD_QUANTUMIZATION));
        machines.add(large("quantum_gold_chamber", "Naquadria", 65536, RecipeMaps.QUANTUM_GOLD));
        machines.add(large("quantum_observation_tower", "Trinaquadalloy", 16384, RecipeMaps.QUANTUM_OBSERVATION));
        machines.add(quantumRelay());
        return List.copyOf(machines);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    private static RecipeMachine mirrorGold(String id, String material, long eu, String map) {
        var io = MachineIo.builder()
                .itemInputs(MachineFace.TOP).itemAutoInput(MachineFace.TOP)
                .itemOutputs(MachineFace.BOTTOM).itemAutoOutput(MachineFace.BOTTOM)
                .fluidInputs(MachineFace.LEFT).fluidAutoInput(MachineFace.LEFT)
                .fluidOutputs(MachineFace.RIGHT).fluidAutoOutput(MachineFace.RIGHT);
        return RecipeMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .recipeMap(map)
                .basic()
                .energy(EnergyInput.builder("EU", eu).faces(MachineFace.BACK).build())
                .io(io.build())
                .build();
    }

    private static RecipeMachine large(String id, String material, long eu, String map) {
        return RecipeMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .recipeMap(map)
                .largeRecipe()
                .energy(EnergyInput.builder("EU", eu).faces().build())
                .structureKeys(List.of("gt6ktfruaddon.structure." + id + ".1"))
                .build();
    }

    private static BehaviorMachine quantumRelay() {
        return BehaviorMachine.builder(MachineCasing.builder(QUANTUM_RELAY, "Iridium")
                        .machineType(QUANTUM_RELAY).build())
                .energy(EnergyInput.builder("QU", QuantumRelay.QU_PER_TICK)
                        .faces(MachineFace.BOTTOM)
                        .capacity(QuantumRelay.ENERGY_CAPACITY)
                        .build())
                .auxiliaryEnergy(EnergyInput.builder("EU", 8192)
                        .range(2048, 32768)
                        .faces(MachineFace.BACK, MachineFace.LEFT, MachineFace.RIGHT)
                        .capacity(QuantumRelay.ENERGY_CAPACITY)
                        .build())
                .energyOutput(EnergyOutput.builder("QU", QuantumRelay.QU_PER_TICK, 1)
                        .faces(MachineFace.TOP)
                        .build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .progress(76, 26)
                .item(44, 26, BehaviorMachine.SlotRole.INPUT)
                .item(112, 26, BehaviorMachine.SlotRole.OUTPUT)
                .recipeMaps(RecipeMaps.QUANTUM_COMMUNICATION)
                .build();
    }
}
