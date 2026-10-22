package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/**
 * Research blocks from {@code tileEntityInit0}. The assembler and reactor check
 * the {@code research_monitor} two blocks behind the controller before running a recipe.
 * Microscopes unlock examined items into their bound or adjacent monitor.
 */
public final class Gt6KtfruResearchMachines {
    static final String ASSEMBLER = "research_assembler";
    static final String REACTOR = "research_reactor";
    static final String OPTICAL = "optical_microscope";
    static final String ELECTRON = "electron_microscope";
    static final String ASSEMBLER_MAP = "gt6ktfruaddon.recipe.research_assembler";
    static final String REACTOR_MAP = "gt6ktfruaddon.recipe.research_reactor";
    private static final String GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";

    private Gt6KtfruResearchMachines() {}

    public static void addMaps(List<RecipeTable> maps) {
        maps.add(Gt6KtfruMachines.map(ASSEMBLER_MAP, "Research Assembler", "researchassembler", 9, 6, 0, 3, 3, 0, 1));
        maps.add(Gt6KtfruMachines.map(REACTOR_MAP, "Research Reactor", "fusion", 9, 6, 0, 6, 6, 0, 1));
    }

    public static void add(List<AddonMachine> machines) {
        table(machines, "research_monitor");
        table(machines, "research_table_circuit");
        table(machines, "research_table_item");
        table(machines, "research_table_printer");
        table(machines, "research_database_item");
        table(machines, "research_database_online");
        table(machines, "research_table_identify");
        table(machines, "research_table_compute");
        table(machines, "research_table_current_control");
        machines.add(researchMachine(ASSEMBLER, "Aluminium", ASSEMBLER_MAP, "gt.recipe.assembler"));
        machines.add(researchMachine(REACTOR, "StainlessSteel", REACTOR_MAP));
        machines.add(microscope(OPTICAL, "Aluminium", 128, 16, 512, 16384));
        machines.add(microscope(ELECTRON, "StainlessSteel", 512, 16, 4096, 131072));
    }

    public static void addLayouts(List<StructureLayout> layouts) {
        layouts.add(Gt6KtfruResearchLayouts.assembler(ASSEMBLER));
        layouts.add(Gt6KtfruResearchLayouts.assembler(REACTOR));
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
                "research_table_compute",
                "research_table_current_control",
                ASSEMBLER,
                REACTOR,
                OPTICAL,
                ELECTRON);
    }

    public static boolean owns(String machineType) {
        return behaviorTypes().contains(machineType);
    }

    public static MachineBehavior create(String machineType) {
        if (ASSEMBLER.equals(machineType)) {
            return new Gt6KtfruResearchAssembler(ASSEMBLER_MAP, "gt.recipe.assembler");
        }
        if (REACTOR.equals(machineType)) {
            return new Gt6KtfruResearchAssembler(REACTOR_MAP);
        }
        if (OPTICAL.equals(machineType)) return new Gt6KtfruMicroscope(256);
        if (ELECTRON.equals(machineType)) return new Gt6KtfruMicroscope(4096);
        return new Gt6KtfruResearch("research_monitor".equals(machineType));
    }

    /** Bottom EU satisfies the required behavior input; front emits one 512 EU packet. */
    private static void table(List<AddonMachine> machines, String id) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(512).build())
                .energyOutput(EnergyOutput.builder("EU", 512, 1).faces(MachineFace.FRONT).build())
                .gui(GUI)
                .build());
    }

    /** EU 131072, accepted packets 64–524288 on auxiliary faces. Checks the research monitor two blocks behind. */
    private static BehaviorMachine researchMachine(String id, String material, String... maps) {
        return BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("EU", 131072).faces(MachineFace.BOTTOM).capacity(524288).build())
                .auxiliaryEnergy(EnergyInput.builder("EU", 131072)
                        .range(64, 524288)
                        .faces(MachineFace.BACK, MachineFace.LEFT, MachineFace.RIGHT, MachineFace.TOP)
                        .capacity(524288)
                        .build())
                .gui(GUI)
                .progress(100, 26)
                .item(22, 8, BehaviorMachine.SlotRole.INPUT)
                .item(40, 8, BehaviorMachine.SlotRole.INPUT)
                .item(22, 26, BehaviorMachine.SlotRole.INPUT)
                .item(40, 26, BehaviorMachine.SlotRole.INPUT)
                .item(22, 44, BehaviorMachine.SlotRole.INPUT)
                .item(40, 44, BehaviorMachine.SlotRole.INPUT)
                .item(148, 17, BehaviorMachine.SlotRole.OUTPUT)
                .item(148, 35, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(16_000, 60, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(16_000, 80, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(16_000, 126, 8, BehaviorMachine.SlotRole.OUTPUT)
                .recipeMaps(maps)
                .build();
    }

    private static BehaviorMachine microscope(String id, String material, long eu, long min, long max, long capacity) {
        return BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("EU", eu).faces(MachineFace.BOTTOM).capacity(capacity).build())
                .auxiliaryEnergy(EnergyInput.builder("EU", eu)
                        .range(min, max)
                        .faces(MachineFace.BACK, MachineFace.LEFT, MachineFace.RIGHT, MachineFace.TOP)
                        .capacity(capacity)
                        .build())
                .gui(GUI)
                .item(44, 26, BehaviorMachine.SlotRole.INPUT)
                .item(112, 26, BehaviorMachine.SlotRole.OUTPUT)
                .build();
    }
}
