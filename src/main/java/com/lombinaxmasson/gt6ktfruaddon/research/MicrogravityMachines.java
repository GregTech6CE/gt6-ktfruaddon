package com.lombinaxmasson.gt6ktfruaddon.research;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
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
 * The microgravity room (30102) with crystal growth (30103) and the microgravity assembler (30105), which
 * check the room index, and the cosmic radiation chamber on the assembler's recipe map.
 */
public final class MicrogravityMachines implements Feature {
    static final String MICROGRAVITY_ASSEMBLER = "microgravity_assembler";
    static final String MICROGRAVITY_CRYSTAL = "microgravity_crystal_growth";
    static final String ROOM = "centrifugal_microgravity";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(MICROGRAVITY_ASSEMBLER, () -> new MicrogravityMachine(RecipeMaps.COSMIC_RADIATION, false, 0))
            .add(MICROGRAVITY_CRYSTAL, () -> new MicrogravityMachine(RecipeMaps.MICROGRAVITY_CRYSTAL, true, 2))
            .add(ROOM, () -> new MicrogravityController())
            .build();

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.COSMIC_RADIATION, "Cosmic Radiation Research", "gascompressor", 1, 3, 0, 0, 0, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.MICROGRAVITY_CRYSTAL, "Microgravity Crystal Growth", "heatmixer", 1, 1, 0, 2, 0, 0, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(cosmicRadiationChamber());
        machines.add(microgravityAssembler());
        machines.add(microgravityCrystal());
        machines.add(room());
        return List.copyOf(machines);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    private static BehaviorMachine microgravityAssembler() {
        return BehaviorMachine.builder(MachineCasing.builder(MICROGRAVITY_ASSEMBLER, "Tungstensteel")
                        .machineType(MICROGRAVITY_ASSEMBLER).build())
                .energy(EnergyInput.builder("EU", 8192).faces(MachineFace.BACK).capacity(65536).build())
                .auxiliaryEnergy(EnergyInput.builder("EU", 8192)
                        .range(2048, 16384)
                        .faces(MachineFace.BOTTOM, MachineFace.LEFT, MachineFace.RIGHT, MachineFace.TOP)
                        .capacity(65536)
                        .build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .progress(66, 26)
                .item(36, 26, BehaviorMachine.SlotRole.INPUT)
                .item(102, 26, BehaviorMachine.SlotRole.OUTPUT)
                .item(120, 26, BehaviorMachine.SlotRole.OUTPUT)
                .item(138, 26, BehaviorMachine.SlotRole.OUTPUT)
                .recipeMaps(RecipeMaps.COSMIC_RADIATION)
                .build();
    }

    private static BehaviorMachine microgravityCrystal() {
        return BehaviorMachine.builder(MachineCasing.builder(MICROGRAVITY_CRYSTAL, "Titanium")
                        .machineType(MICROGRAVITY_CRYSTAL).build())
                .energy(EnergyInput.builder("EU", 8192).faces(MachineFace.BOTTOM).capacity(65536).build())
                .auxiliaryEnergy(EnergyInput.builder("EU", 8192)
                        .range(1024, 16384)
                        .faces(MachineFace.BACK, MachineFace.LEFT, MachineFace.RIGHT, MachineFace.TOP)
                        .capacity(65536)
                        .build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .progress(92, 26)
                .item(26, 26, BehaviorMachine.SlotRole.INPUT)
                .item(124, 26, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(64_000, 48, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(64_000, 68, 8, BehaviorMachine.SlotRole.INPUT)
                .recipeMaps(RecipeMaps.MICROGRAVITY_CRYSTAL)
                .build();
    }

    private static RecipeMachine cosmicRadiationChamber() {
        var io = MachineIo.builder()
                .itemInputs(MachineFace.TOP).itemAutoInput(MachineFace.TOP)
                .itemOutputs(MachineFace.BOTTOM).itemAutoOutput(MachineFace.BOTTOM);
        return RecipeMachine.builder(MachineCasing.builder("cosmic_radiation_chamber", "Tungstensteel")
                        .machineType("cosmic_radiation_chamber").build())
                .recipeMap(RecipeMaps.COSMIC_RADIATION)
                .basic()
                .energy(EnergyInput.builder("EU", 8192).faces(MachineFace.BACK).range(2048, 16384).build())
                .io(io.build())
                .build();
    }

    /** Nominal 4096 accepts 2048–8192 EU on the bottom. */
    private static BehaviorMachine room() {
        return BehaviorMachine.builder(MachineCasing.builder(ROOM, "Tungstensteel").machineType(ROOM).build())
                .energy(EnergyInput.builder("EU", 4096).faces(MachineFace.BOTTOM).capacity(65536).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build();
    }
}
