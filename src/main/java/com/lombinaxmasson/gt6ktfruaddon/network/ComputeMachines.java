package com.lombinaxmasson.gt6ktfruaddon.network;

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
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;

/**
 * Live compute blocks. 1100, 1102 and 1104 stay commented out in the source.
 * 1101 is replaced by the three wireless multiblock controllers.
 */
public final class ComputeMachines implements Feature {
    static final String WIRELESS = "wireless_compute_controller";
    static final String NODE = "wireless_compute_node_aluminium";
    static final String QUANTUM_NODE = "quantum_compute_node_naquadah";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add("compute_user", () -> new Compute(Compute.Kind.PLAIN, 512, 0, ""))
            .add("dream_brain", () -> new Compute(Compute.Kind.PLAIN, 512, 0, ""))
            .add("compute_cluster", () -> new Compute(Compute.Kind.CLUSTER, 0, 0, ""))
            .add("compute_cluster_simple", () -> new Compute(Compute.Kind.CLUSTER, 0, 0, ""))
            .add(WIRELESS, () -> new Compute(Compute.Kind.WIRELESS, 0, 0, ""))
            .add("wireless_compute_controller_large", () -> new Compute(Compute.Kind.WIRELESS, 0, 0, ""))
            .add("wireless_compute_controller_huge", () -> new Compute(Compute.Kind.WIRELESS, 0, 0, ""))
            .add("biology_compute_t1", () -> arrayBehavior(262144, 65536, "Biology"))
            .add("biology_compute_t2", () -> arrayBehavior(2_097_152, 524288, "Biology"))
            .add("biology_compute_t3", () -> arrayBehavior(16_777_216, 4_194_304, "Biology"))
            .add("biology_compute_t4", () -> arrayBehavior(67_108_864, 16_777_216, "Biology"))
            .add("quantum_compute_t1", () -> arrayBehavior(16_777_216, 65536, "Quantum"))
            .add("quantum_compute_t2", () -> arrayBehavior(134_217_728, 262144, "Quantum"))
            .add("quantum_compute_t3", () -> arrayBehavior(1_073_741_824, 1_048_576, "Quantum"))
            .add("quantum_compute_t4", () -> arrayBehavior(8_589_934_592L, 16_777_216, "Quantum"))
            .add("spacetime_compute_t1", () -> arrayBehavior(2_097_152, 2000, "Spacetime"))
            .add("spacetime_compute_t2", () -> arrayBehavior(67_108_864, 200000, "Spacetime"))
            .add("quantum_compute_center", () -> arrayBehavior(0, 0, "Quantum"))
            .add(NODE, () -> new Compute(Compute.Kind.NODE, 2000, 0, "Normal"))
            .add("wireless_compute_node_stainless", () -> new Compute(Compute.Kind.NODE, 20000, 0, "Normal"))
            .add("wireless_compute_node_chromium", () -> new Compute(Compute.Kind.NODE, 200000, 0, "Normal"))
            .add("wireless_compute_node_titanium", () -> new Compute(Compute.Kind.NODE, 2000000, 0, "Normal"))
            .add(QUANTUM_NODE, () -> new Compute(Compute.Kind.NODE, 65536, 0, "Quantum"))
            .add("quantum_compute_node_naquadria", () -> new Compute(Compute.Kind.NODE, 262144, 0, "Quantum"))
            .add("quantum_compute_node_enriched", () -> new Compute(Compute.Kind.NODE, 1048576, 0, "Quantum"))
            .add("quantum_compute_node_trinaquadalloy", () -> new Compute(Compute.Kind.NODE, 16777216, 0, "Quantum"))
            .build();

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        emitter(machines, "compute_user");
        emitter(machines, "dream_brain");
        node(machines, "compute_cluster", "Chromium", 4);
        node(machines, "compute_cluster_simple", "StainlessSteel", 2);
        wireless(machines, WIRELESS);
        wireless(machines, "wireless_compute_controller_large");
        wireless(machines, "wireless_compute_controller_huge");
        array(machines, "biology_compute_t1", "StainlessSteel");
        array(machines, "biology_compute_t2", "Chromium");
        array(machines, "biology_compute_t3", "Graphene");
        array(machines, "biology_compute_t4", "Trinaquadalloy");
        array(machines, "quantum_compute_t1", "Naquadah");
        array(machines, "quantum_compute_t2", "Naquadria");
        array(machines, "quantum_compute_t3", "Naquadria");
        array(machines, "quantum_compute_t4", "Trinaquadalloy");
        array(machines, "spacetime_compute_t1", "Naquadria");
        array(machines, "spacetime_compute_t2", "Trinaquadalloy");
        array(machines, "quantum_compute_center", "Trinaquadalloy");
        computeNode(machines, NODE, "Aluminium");
        computeNode(machines, "wireless_compute_node_stainless", "StainlessSteel");
        computeNode(machines, "wireless_compute_node_chromium", "Chromium");
        computeNode(machines, "wireless_compute_node_titanium", "Titanium");
        computeNode(machines, QUANTUM_NODE, "Naquadah");
        computeNode(machines, "quantum_compute_node_naquadria", "Naquadria");
        computeNode(machines, "quantum_compute_node_enriched", "Naquadria");
        computeNode(machines, "quantum_compute_node_trinaquadalloy", "Trinaquadalloy");
        return List.copyOf(machines);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    private static MachineBehavior arrayBehavior(long required, long provided, String unit) {
        return new Compute(Compute.Kind.ARRAY, provided, required, unit);
    }

    private static void emitter(List<AddonMachine> machines, String id) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(512).build())
                .energyOutput(EnergyOutput.builder("EU", 512, 1).faces(MachineFace.FRONT).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build());
    }

    /** The source emits 512 EU. The bottom input keeps the behavior block able to form a structure. */
    private static void wireless(List<AddonMachine> machines, String id) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .energyOutput(EnergyOutput.builder("EU", 512, 1).faces(MachineFace.FRONT).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build());
    }

    private static void array(List<AddonMachine> machines, String id, String material) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build());
    }

    private static void node(List<AddonMachine> machines, String id, String material, int slots) {
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(Ktfru.PLACEHOLDER_GUI);
        for (int slot = 0; slot < slots; slot++) {
            builder.item(26 + slot * 18, 8, BehaviorMachine.SlotRole.INPUT);
        }
        machines.add(builder.build());
    }

    private static void computeNode(List<AddonMachine> machines, String id, String material) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build());
    }
}
