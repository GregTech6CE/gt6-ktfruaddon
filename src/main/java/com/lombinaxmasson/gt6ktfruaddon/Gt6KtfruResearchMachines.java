package com.lombinaxmasson.gt6ktfruaddon;

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
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * Research blocks from {@code tileEntityInit0}. The assembler and reactor check
 * the {@code research_monitor} two blocks behind the controller before running a recipe.
 * Microscopes unlock examined items into their bound or adjacent monitor.
 */
public final class Gt6KtfruResearchMachines implements Feature {
    static final String ASSEMBLER = "research_assembler";
    static final String REACTOR = "research_reactor";
    static final String OPTICAL = "optical_microscope";
    static final String ELECTRON = "electron_microscope";

    private static final String[] TABLES = {
            "research_monitor",
            "research_table_circuit",
            "research_table_item",
            "research_table_printer",
            "research_database_item",
            "research_database_online",
            "research_table_identify",
            "research_table_compute",
            "research_table_current_control"
    };
    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = declareBehaviors();

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.RESEARCH_ASSEMBLER, "Research Assembler", "researchassembler", 9, 6, 0, 3, 3, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.RESEARCH_REACTOR, "Research Reactor", "fusion", 9, 6, 0, 6, 6, 0, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        for (String id : TABLES) table(machines, id);
        machines.add(researchMachine(ASSEMBLER, "Aluminium", RecipeMaps.RESEARCH_ASSEMBLER, "gt.recipe.assembler"));
        machines.add(researchMachine(REACTOR, "StainlessSteel", RecipeMaps.RESEARCH_REACTOR));
        machines.add(microscope(OPTICAL, "Aluminium", 128, 16, 512, 16384));
        machines.add(microscope(ELECTRON, "StainlessSteel", 512, 16, 4096, 131072));
        return List.copyOf(machines);
    }

    @Override
    public List<StructureLayout> layouts() {
        List<StructureLayout> layouts = new ArrayList<>();
        layouts.add(Gt6KtfruResearchLayouts.assembler(ASSEMBLER));
        layouts.add(Gt6KtfruResearchLayouts.assembler(REACTOR));
        return List.copyOf(layouts);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    private static Map<String, Supplier<MachineBehavior>> declareBehaviors() {
        Behaviors behaviors = new Behaviors();
        for (String id : TABLES) behaviors.add(id, () -> new Gt6KtfruResearch("research_monitor".equals(id)));
        return behaviors
                .add(ASSEMBLER, () -> new Gt6KtfruResearchAssembler(RecipeMaps.RESEARCH_ASSEMBLER, "gt.recipe.assembler"))
                .add(REACTOR, () -> new Gt6KtfruResearchAssembler(RecipeMaps.RESEARCH_REACTOR))
                .add(OPTICAL, () -> new Gt6KtfruMicroscope(256))
                .add(ELECTRON, () -> new Gt6KtfruMicroscope(4096))
                .build();
    }

    /** Bottom EU satisfies the required behavior input; front emits one 512 EU packet. */
    private static void table(List<AddonMachine> machines, String id) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(512).build())
                .energyOutput(EnergyOutput.builder("EU", 512, 1).faces(MachineFace.FRONT).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
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
                .gui(Ktfru.PLACEHOLDER_GUI)
                .energyGauge(6, 8)
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
                .gui(Ktfru.PLACEHOLDER_GUI)
                .item(44, 26, BehaviorMachine.SlotRole.INPUT)
                .item(112, 26, BehaviorMachine.SlotRole.OUTPUT)
                .build();
    }
}
