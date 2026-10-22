package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/**
 * Card 15. Research blocks that this source actually registers, plus the
 * research assembler. Microscopes, the research reactor, 9816 and 9817 are
 * not in the checkout.
 */
public final class Gt6KtfruResearchMachines {
    static final String ASSEMBLER = "research_assembler";
    private static final String GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";

    private Gt6KtfruResearchMachines() {}

    public static void add(List<AddonMachine> machines) {
        table(machines, "research_monitor");
        table(machines, "research_table_circuit");
        table(machines, "research_table_item");
        table(machines, "research_table_printer");
        table(machines, "research_database_item");
        table(machines, "research_database_online");
        table(machines, "research_table_identify");
        machines.add(assembler());
    }

    public static void addLayouts(List<StructureLayout> layouts) {
        layouts.add(Gt6KtfruResearchLayouts.assembler(ASSEMBLER));
    }

    public static Set<String> behaviorTypes() {
        return Set.of(
                "research_monitor",
                "research_table_circuit",
                "research_table_item",
                "research_table_printer",
                "research_database_item",
                "research_database_online",
                "research_table_identify",
                ASSEMBLER);
    }

    public static boolean owns(String machineType) {
        return behaviorTypes().contains(machineType);
    }

    public static MachineBehavior create(String machineType) {
        if (ASSEMBLER.equals(machineType)) return new Gt6KtfruResearchAssembler();
        return new Gt6KtfruResearch("research_monitor".equals(machineType));
    }

    /** Bottom EU is the required input. The front can emit one 512 EU packet. */
    private static void table(List<AddonMachine> machines, String id) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(512).build())
                .energyOutput(EnergyOutput.builder("EU", 512, 1).faces(MachineFace.FRONT).build())
                .gui(GUI)
                .build());
    }

    /**
     * Registered LU window is 64–256, so the nominal 128 sits in the middle.
     * The recipe map is the built-in assembler.
     */
    private static BehaviorMachine assembler() {
        return BehaviorMachine.builder(MachineCasing.builder(ASSEMBLER, "Aluminium").machineType(ASSEMBLER).build())
                .energy(EnergyInput.builder("LU", 128).capacity(8192).build())
                .gui(GUI)
                .progress(80, 26)
                .item(26, 26, BehaviorMachine.SlotRole.INPUT)
                .item(44, 26, BehaviorMachine.SlotRole.INPUT)
                .item(116, 26, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(16_000, 152, 8, BehaviorMachine.SlotRole.INPUT)
                .build();
    }
}
