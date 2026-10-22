package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Set;

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

/**
 * Late recipe machines and the addon-side subsystem behavior machines:
 * - Laser fusion (30071, 30072): discrete LU pulse storage, peak check and mandatory cooling.
 * - Microgravity room (30102), crystal growth (30103) and microgravity assembler (30105): room index check.
 * - Quantum communication relay (30074): paired EU-to-QU recipe runner emitting 128 QU/tick from the top face.
 * - AI research centers (30106–30108): 67,108,864 Normal compute/tick allocation and monitor point progress.
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
    static final String LASER_EXP = "laser_fusion_experimental";
    static final String LASER_IND = "laser_fusion_industrial";
    static final String MICROGRAVITY_ASSEMBLER = "microgravity_assembler";
    static final String MICROGRAVITY_CRYSTAL = "microgravity_crystal_growth";
    static final String QUANTUM_RELAY = "quantum_communication_relay";
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
        maps.add(Gt6KtfruMachines.map(CRYSTAL, "Microgravity Crystal Growth", "heatmixer", 1, 1, 0, 2, 0, 0, 1));
        maps.add(Gt6KtfruMachines.map(QUANTUM_GOLD, "Quantum Gold Synthesis", "gascompressor", 4, 4, 0, 2, 2, 0, 1));
        maps.add(Gt6KtfruMachines.map(OBSERVATION, "Quantum Observation", "gascompressor", 4, 4, 0, 2, 2, 0, 1));
        maps.add(Gt6KtfruMachines.map(RELAY_MAP, "Quantum Communication", "gascompressor", 1, 1, 0, 0, 0, 0, 1));
    }

    public static void add(List<AddonMachine> machines) {
        machines.add(tokamak("fusion_tokamak_exp", 2048));
        machines.add(tokamak("fusion_tokamak_t1", 16384));
        machines.add(laser(LASER_EXP, "Tungstensteel", 131_072L, 2L * 8L * 1024L * 1024L));
        machines.add(laser(LASER_IND, "Osmium", 2_097_152L, 8L * 134_217_728L));
        machines.add(catalytic("catalytic_reactor_ev", "StainlessSteel", 2048, 256, 4096));
        machines.add(catalytic("catalytic_reactor_iv", "Tungstensteel", 8192, 1024, 16384));
        machines.add(basic("mirror_gold_synthesis", "Iridium", 8192, 8192, 8192, MIRROR, true));
        machines.add(basic("mirror_gold_quantumization", "Naquadria", 16384, 16384, 16384, MIRROR_QUANTUM, true));
        machines.add(basic("cosmic_radiation_chamber", "Tungstensteel", 8192, 2048, 16384, COSMIC, false));
        machines.add(microgravityAssembler());
        machines.add(large("quantum_gold_chamber", "Naquadria", 65536, 65536, 65536, QUANTUM_GOLD));
        machines.add(large("quantum_observation_tower", "Trinaquadalloy", 16384, 16384, 16384, OBSERVATION));
        machines.add(microgravityCrystal());
        machines.add(quantumRelay());
        machines.add(room());
        machines.add(ai(FLOW));
        machines.add(ai(FILL));
        machines.add(ai(PATTERN));
    }

    public static Set<String> behaviorTypes() {
        return Set.of(
                LASER_EXP,
                LASER_IND,
                MICROGRAVITY_ASSEMBLER,
                MICROGRAVITY_CRYSTAL,
                QUANTUM_RELAY,
                ROOM,
                FLOW,
                FILL,
                PATTERN);
    }

    public static boolean owns(String machineType) {
        return behaviorTypes().contains(machineType);
    }

    public static MachineBehavior create(String machineType) {
        return switch (machineType) {
            case LASER_EXP -> Gt6KtfruLaserFusion.experimental();
            case LASER_IND -> Gt6KtfruLaserFusion.industrial();
            case MICROGRAVITY_ASSEMBLER -> new Gt6KtfruRooms(COSMIC, false, 0);
            case MICROGRAVITY_CRYSTAL -> new Gt6KtfruRooms(CRYSTAL, true, 2);
            case QUANTUM_RELAY -> new Gt6KtfruQuantumRelay();
            case ROOM -> new Gt6KtfruLateBehavior(true, 0);
            case FLOW -> new Gt6KtfruLateBehavior(false, 8);
            case FILL -> new Gt6KtfruLateBehavior(false, 4);
            default -> new Gt6KtfruLateBehavior(false, 2);
        };
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

    private static BehaviorMachine laser(String id, String material, long maxPacket, long pulseCapacity) {
        return BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("LU", maxPacket / 2)
                        .faces(MachineFace.TOP)
                        .capacity(pulseCapacity)
                        .build())
                .auxiliaryEnergy(EnergyInput.builder("LU", maxPacket)
                        .range(1, maxPacket)
                        .faces(MachineFace.BOTTOM, MachineFace.BACK, MachineFace.LEFT, MachineFace.RIGHT)
                        .capacity(pulseCapacity)
                        .build())
                .gui(GUI)
                .progress(88, 26)
                .item(26, 26, BehaviorMachine.SlotRole.INPUT)
                .item(156, 26, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(8_000, 46, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(8_000, 66, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(8_000, 116, 8, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(8_000, 136, 8, BehaviorMachine.SlotRole.OUTPUT)
                .recipeMaps(LASER)
                .build();
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
                .gui(GUI)
                .progress(66, 26)
                .item(36, 26, BehaviorMachine.SlotRole.INPUT)
                .item(102, 26, BehaviorMachine.SlotRole.OUTPUT)
                .item(120, 26, BehaviorMachine.SlotRole.OUTPUT)
                .item(138, 26, BehaviorMachine.SlotRole.OUTPUT)
                .recipeMaps(COSMIC)
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
                .gui(GUI)
                .progress(92, 26)
                .item(26, 26, BehaviorMachine.SlotRole.INPUT)
                .item(124, 26, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(16_000, 48, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(16_000, 68, 8, BehaviorMachine.SlotRole.INPUT)
                .recipeMaps(CRYSTAL)
                .build();
    }

    private static BehaviorMachine quantumRelay() {
        return BehaviorMachine.builder(MachineCasing.builder(QUANTUM_RELAY, "Iridium")
                        .machineType(QUANTUM_RELAY).build())
                .energy(EnergyInput.builder("QU", Gt6KtfruQuantumRelay.QU_PER_TICK)
                        .faces(MachineFace.BOTTOM)
                        .capacity(Gt6KtfruQuantumRelay.ENERGY_CAPACITY)
                        .build())
                .auxiliaryEnergy(EnergyInput.builder("EU", 8192)
                        .range(2048, 32768)
                        .faces(MachineFace.BACK, MachineFace.LEFT, MachineFace.RIGHT)
                        .capacity(Gt6KtfruQuantumRelay.ENERGY_CAPACITY)
                        .build())
                .energyOutput(EnergyOutput.builder("QU", Gt6KtfruQuantumRelay.QU_PER_TICK, 1)
                        .faces(MachineFace.TOP)
                        .build())
                .gui(GUI)
                .progress(76, 26)
                .item(44, 26, BehaviorMachine.SlotRole.INPUT)
                .item(112, 26, BehaviorMachine.SlotRole.OUTPUT)
                .recipeMaps(RELAY_MAP)
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

    /** Nominal 4096 accepts 2048–8192 EU on the bottom. */
    private static BehaviorMachine room() {
        return BehaviorMachine.builder(MachineCasing.builder(ROOM, "Tungstensteel").machineType(ROOM).build())
                .energy(EnergyInput.builder("EU", 4096).faces(MachineFace.BOTTOM).capacity(65536).build())
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
