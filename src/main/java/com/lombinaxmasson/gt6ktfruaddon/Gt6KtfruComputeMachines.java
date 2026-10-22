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
 * Card 16. The live compute blocks are 1100–1105, 32005 and 32006.
 * Controllers after 30058, and nodes 32010–32043, are not in this checkout.
 */
public final class Gt6KtfruComputeMachines {
    private static final String GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";

    private Gt6KtfruComputeMachines() {}

    public static void add(List<AddonMachine> machines) {
        emitter(machines, "electric_controller");
        emitter(machines, "wireless_controller");
        emitter(machines, "wired_controller");
        emitter(machines, "compute_user");
        emitter(machines, "network_cable");
        emitter(machines, "dream_brain");
        node(machines, "compute_cluster", "Chromium", 4);
        node(machines, "compute_cluster_simple", "StainlessSteel", 2);
    }

    public static Set<String> behaviorTypes() {
        return Set.of(
                "electric_controller",
                "wireless_controller",
                "wired_controller",
                "compute_user",
                "network_cable",
                "dream_brain",
                "compute_cluster",
                "compute_cluster_simple");
    }

    public static boolean owns(String machineType) {
        return behaviorTypes().contains(machineType);
    }

    public static MachineBehavior create(String machineType) {
        return switch (machineType) {
            case "wireless_controller" -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.WIRELESS);
            case "electric_controller" -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.ELECTRIC);
            case "wired_controller" -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.WIRED);
            case "compute_cluster", "compute_cluster_simple" -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.NODE);
            default -> new Gt6KtfruCompute(Gt6KtfruCompute.Kind.PLAIN);
        };
    }

    private static void emitter(List<AddonMachine> machines, String id) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(512).build())
                .energyOutput(EnergyOutput.builder("EU", 512, 1).faces(MachineFace.FRONT).build())
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
}
