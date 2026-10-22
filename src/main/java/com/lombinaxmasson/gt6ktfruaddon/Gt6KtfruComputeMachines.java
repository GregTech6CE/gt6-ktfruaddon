package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/**
 * Live compute blocks. 1100, 1102 and 1104 stay commented out in the source.
 * 1101 is replaced by the three wireless multiblock controllers.
 */
public final class Gt6KtfruComputeMachines {
    static final String WIRELESS = "wireless_compute_controller";
    static final String NODE = "wireless_compute_node_aluminium";
    static final String QUANTUM_NODE = "quantum_compute_node_naquadah";
    private static final String GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";

    private Gt6KtfruComputeMachines() {}

    public static void add(List<AddonMachine> machines) {
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
    }

    public static Set<String> behaviorTypes() {
        return Set.of(
                "compute_user",
                "dream_brain",
                "compute_cluster",
                "compute_cluster_simple",
                WIRELESS,
                "wireless_compute_controller_large",
                "wireless_compute_controller_huge",
                "biology_compute_t1",
                "biology_compute_t2",
                "biology_compute_t3",
                "biology_compute_t4",
                "quantum_compute_t1",
                "quantum_compute_t2",
                "quantum_compute_t3",
                "quantum_compute_t4",
                "spacetime_compute_t1",
                "spacetime_compute_t2",
                "quantum_compute_center",
                NODE,
                "wireless_compute_node_stainless",
                "wireless_compute_node_chromium",
                "wireless_compute_node_titanium",
                QUANTUM_NODE,
                "quantum_compute_node_naquadria",
                "quantum_compute_node_enriched",
                "quantum_compute_node_trinaquadalloy");
    }

    public static boolean owns(String machineType) {
        return behaviorTypes().contains(machineType);
    }

    public static MachineBehavior create(String machineType) {
        return switch (machineType) {
            case WIRELESS, "wireless_compute_controller_large", "wireless_compute_controller_huge" ->
                    new Gt6KtfruCompute(Gt6KtfruCompute.Kind.WIRELESS, 0, 0, "");
            case "biology_compute_t1" -> arrayBehavior(262144, 65536, "Biology");
            case "biology_compute_t2" -> arrayBehavior(2_097_152, 524288, "Biology");
            case "biology_compute_t3" -> arrayBehavior(16_777_216, 4_194_304, "Biology");
            case "biology_compute_t4" -> arrayBehavior(67_108_864, 16_777_216, "Biology");
            case "quantum_compute_t1" -> arrayBehavior(16_777_216, 65536, "Quantum");
            case "quantum_compute_t2" -> arrayBehavior(134_217_728, 262144, "Quantum");
            case "quantum_compute_t3" -> arrayBehavior(1_073_741_824, 1_048_576, "Quantum");
            case "quantum_compute_t4" -> arrayBehavior(8_589_934_592L, 16_777_216, "Quantum");
            case "spacetime_compute_t1" -> arrayBehavior(2_097_152, 2000, "Spacetime");
            case "spacetime_compute_t2" -> arrayBehavior(67_108_864, 200000, "Spacetime");
            case "quantum_compute_center" -> arrayBehavior(0, 0, "Quantum");
            case NODE -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.NODE, 2000, 0, "Normal");
            case "wireless_compute_node_stainless" -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.NODE, 20000, 0, "Normal");
            case "wireless_compute_node_chromium" -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.NODE, 200000, 0, "Normal");
            case "wireless_compute_node_titanium" -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.NODE, 2000000, 0, "Normal");
            case QUANTUM_NODE -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.NODE, 65536, 0, "Quantum");
            case "quantum_compute_node_naquadria" -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.NODE, 262144, 0, "Quantum");
            case "quantum_compute_node_enriched" -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.NODE, 1048576, 0, "Quantum");
            case "quantum_compute_node_trinaquadalloy" ->
                    new Gt6KtfruCompute(Gt6KtfruCompute.Kind.NODE, 16777216, 0, "Quantum");
            case "compute_cluster", "compute_cluster_simple" ->
                    new Gt6KtfruCompute(Gt6KtfruCompute.Kind.CLUSTER, 0, 0, "");
            default -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.PLAIN, 512, 0, "");
        };
    }

    private static MachineBehavior arrayBehavior(long required, long provided, String unit) {
        return new Gt6KtfruCompute(Gt6KtfruCompute.Kind.ARRAY, provided, required, unit);
    }

    private static void emitter(List<AddonMachine> machines, String id) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(512).build())
                .energyOutput(EnergyOutput.builder("EU", 512, 1).faces(MachineFace.FRONT).build())
                .gui(GUI)
                .build());
    }

    /** The source emits 512 EU. The bottom input keeps the behavior block able to form a structure. */
    private static void wireless(List<AddonMachine> machines, String id) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .energyOutput(EnergyOutput.builder("EU", 512, 1).faces(MachineFace.FRONT).build())
                .gui(GUI)
                .build());
    }

    private static void array(List<AddonMachine> machines, String id, String material) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(GUI)
                .build());
    }

    private static void node(List<AddonMachine> machines, String id, String material, int slots) {
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(GUI);
        for (int slot = 0; slot < slots; slot++) {
            builder.item(26 + slot * 18, 8, BehaviorMachine.SlotRole.INPUT);
        }
        machines.add(builder.build());
    }

    private static void computeNode(List<AddonMachine> machines, String id, String material) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(GUI)
                .build());
    }
}
