package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.RecipeMachine;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/**
 * Research blocks from {@code tileEntityInit0}. The assembler and reactor are
 * large EU recipe machines. The microscopes keep their structures and spend EU
 * per item; they do not promote research tasks.
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
        maps.add(Gt6KtfruMachines.map(ASSEMBLER_MAP, "Research Assembler", "gascompressor", 9, 6, 0, 3, 3, 0, 1));
        maps.add(Gt6KtfruMachines.map(REACTOR_MAP, "Research Reactor", "gascompressor", 9, 6, 0, 6, 6, 0, 1));
    }

    public static void add(List<AddonMachine> machines) {
        table(machines, "research_monitor", true);
        table(machines, "research_table_circuit", true);
        table(machines, "research_table_item", true);
        table(machines, "research_table_printer", true);
        table(machines, "research_database_item", true);
        table(machines, "research_database_online", true);
        table(machines, "research_table_identify", true);
        table(machines, "research_table_compute", false);
        table(machines, "research_table_current_control", false);
        machines.add(recipe(ASSEMBLER, "Aluminium", ASSEMBLER_MAP));
        machines.add(recipe(REACTOR, "StainlessSteel", REACTOR_MAP));
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
                OPTICAL,
                ELECTRON);
    }

    public static boolean owns(String machineType) {
        return behaviorTypes().contains(machineType);
    }

    public static MachineBehavior create(String machineType) {
        if (OPTICAL.equals(machineType)) return new Gt6KtfruMicroscope(256);
        if (ELECTRON.equals(machineType)) return new Gt6KtfruMicroscope(4096);
        return new Gt6KtfruResearch("research_monitor".equals(machineType));
    }

    /** 9810–9815 accept and emit 512 EU. 9816 and 9817 only emit. */
    private static void table(List<AddonMachine> machines, String id, boolean input) {
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, "StainlessSteel").machineType(id).build())
                .gui(GUI);
        if (input) {
            builder.energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(512).build());
        }
        builder.energyOutput(EnergyOutput.builder("EU", 512, 1).faces(MachineFace.FRONT).build());
        machines.add(builder.build());
    }

    /** EU 131072, accepted packets 64–524288. The walls carry items, fluids and energy. */
    private static RecipeMachine recipe(String id, String material, String map) {
        return RecipeMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .recipeMap(map)
                .largeRecipe()
                .energy(EnergyInput.builder("EU", 131072).range(64, 524288).faces().build())
                .structureKeys(List.of("gt6ktfruaddon.structure." + id + ".1"))
                .build();
    }

    private static BehaviorMachine microscope(String id, String material, long eu, long min, long max, long capacity) {
        return BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("EU", eu).range(min, max).faces(MachineFace.BOTTOM).capacity(capacity).build())
                .gui(GUI)
                .item(44, 26, BehaviorMachine.SlotRole.INPUT)
                .build();
    }
}
