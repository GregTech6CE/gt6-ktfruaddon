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
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;

/**
 * Kinetic machines: the void hoppers, miners, water mill, manual motors and flywheel boxes, which take
 * and give RU, KU or EU through behavior machines.
 */
public final class Gt6KtfruKineticMachines implements Feature {
    public static final String VOID_HOPPER = "void_hopper";
    public static final String MINER = "miner";
    /** The source tin-alloy miner takes RF; the other three take EU. */
    public static final String MINER_RF = "miner_rf";
    public static final String WATER_MILL = "water_mill";
    public static final String MANUAL_MOTOR = "manual_motor";
    /** The source kinetic crank takes KU; the manual motor takes RU. */
    public static final String MANUAL_KINETIC_MOTOR = "manual_kinetic_motor";
    public static final String FLYWHEEL = "flywheel";
    public static final String LARGE_FLYWHEEL = "large_flywheel";
    public static final String ELECTRIC_FLYWHEEL = "electric_flywheel";
    public static final String LARGE_ELECTRIC_FLYWHEEL = "large_electric_flywheel";
    public static final String VACUUM_FLYWHEEL = "vacuum_flywheel";
    public static final String LARGE_VACUUM_FLYWHEEL = "large_vacuum_flywheel";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(VOID_HOPPER, Gt6KtfruVoidHopper::new)
            .add(MINER, Gt6KtfruMiner::new)
            .add(MINER_RF, Gt6KtfruMiner::new)
            .add(WATER_MILL, Gt6KtfruWaterMill::new)
            .add(MANUAL_MOTOR, Gt6KtfruManualMotor::new)
            .add(MANUAL_KINETIC_MOTOR, Gt6KtfruManualMotor::new)
            .add(FLYWHEEL, () -> new Gt6KtfruFlywheel(4, 0.1))
            .add(LARGE_FLYWHEEL, () -> new Gt6KtfruFlywheel(16, 0.15))
            .add(ELECTRIC_FLYWHEEL, () -> new Gt6KtfruFlywheel(4, 0.07))
            .add(LARGE_ELECTRIC_FLYWHEEL, () -> new Gt6KtfruFlywheel(16, 0.1))
            .add(VACUUM_FLYWHEEL, () -> new Gt6KtfruFlywheel(4, 0.0001))
            .add(LARGE_VACUUM_FLYWHEEL, () -> new Gt6KtfruFlywheel(16, 0.00012))
            .build();

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        kineticBehaviors(machines);
        return List.copyOf(machines);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    /**
     * Source 21010–21013, 27520–27523, 9800, 10021, 10022, 10026, 10027 and
     * 15000–15055. Tier on a shared machine type selects the row.
     */
    private static void kineticBehaviors(List<AddonMachine> machines) {
        String[] kinetic = {"Wood", "Bronze", "Steel", "Titanium", "Tungstensteel", "Adamantium"};
        String[] kineticId = {"wood", "bronze", "steel", "titanium", "tungstensteel", "adamantium"};
        int[] hopperInput = {64, 32, 128, 512};
        int[] hopperRange = {4, 8, 16, 24};
        for (int i = 0; i < 4; i++) {
            machines.add(voidHopper("void_hopper_" + kineticId[i], kinetic[i], i + 1,
                    hopperInput[i], hopperRange[i]));
        }
        machines.add(miner("miner_tin_alloy", "TinAlloy", 1, "RF", 128));
        machines.add(miner("miner_lv", "SteelGalvanized", 2, "EU", 32));
        machines.add(miner("miner_mv", "Aluminium", 3, "EU", 128));
        machines.add(miner("miner_hv", "StainlessSteel", 4, "EU", 512));
        machines.add(waterMill());
        machines.add(manualMotor("manual_motor_wood", "WoodTreated", 1, "RU", 10));
        machines.add(manualMotor("manual_motor_bronze", "Bronze", 2, "RU", 22));
        machines.add(manualMotor("manual_kinetic_motor_wood", "WoodTreated", 3, "KU", 8));
        machines.add(manualMotor("manual_kinetic_motor_bronze", "Bronze", 4, "KU", 16));
        long[] vmax = {8, 32, 128, 512, 2048, 8192};
        String[] kind = {
                FLYWHEEL, LARGE_FLYWHEEL, ELECTRIC_FLYWHEEL,
                LARGE_ELECTRIC_FLYWHEEL, VACUUM_FLYWHEEL, LARGE_VACUUM_FLYWHEEL
        };
        String[] kindId = {
                "flywheel", "large_flywheel", "electric_flywheel",
                "large_electric_flywheel", "vacuum_flywheel", "large_vacuum_flywheel"
        };
        int[] slots = {1, 4, 1, 4, 1, 4};
        int[] ampere = {4, 16, 4, 16, 4, 16};
        for (int kindIndex = 0; kindIndex < kind.length; kindIndex++) {
            for (int material = 0; material < kinetic.length; material++) {
                machines.add(flywheel(kindId[kindIndex] + "_" + kineticId[material], kinetic[material],
                        kind[kindIndex], material + 1, vmax[material], slots[kindIndex], ampere[kindIndex]));
            }
        }
    }

    private static BehaviorMachine voidHopper(String id, String material, int tier, long input, int range) {
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(VOID_HOPPER).tier(tier).build())
                .energy(EnergyInput.builder("RU", input).capacity(input * 2).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .item(26, 8, BehaviorMachine.SlotRole.INPUT)
                .toggle("gt6ktfruaddon.void_hopper.blacklist", 152, 8, false)
                .toggle("gt6ktfruaddon.void_hopper.clear", 152, 28, false)
                .maxRange(range);
        for (int slot = 0; slot < 9; slot++) {
            builder.item(8 + slot * 18, 64, BehaviorMachine.SlotRole.OUTPUT);
        }
        return builder.build();
    }

    /** Horizontal square stays on the tier. The declared cube is 32 so the dig can go down. */
    private static BehaviorMachine miner(String id, String material, int tier, String energy, long input) {
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType("RF".equals(energy) ? MINER_RF : MINER).tier(tier).build())
                .energy(EnergyInput.builder(energy, input).capacity(input * 2).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .item(26, 8, BehaviorMachine.SlotRole.INPUT)
                .toggle("gt6ktfruaddon.miner.reset", 152, 8, false)
                .maxRange(32);
        for (int slot = 0; slot < 9; slot++) {
            builder.item(8 + slot * 18, 64, BehaviorMachine.SlotRole.OUTPUT);
        }
        return builder.build();
    }

    /**
     * No energy in the source row. The top face is the required input, nominal 2 RU.
     * Output is a packet of 2, one or two amps, front then back.
     */
    private static BehaviorMachine waterMill() {
        return BehaviorMachine.builder(MachineCasing.builder(WATER_MILL, "WoodTreated")
                        .machineType(WATER_MILL).tier(1).build())
                .energy(EnergyInput.builder("RU", 2).faces(MachineFace.TOP).capacity(4).build())
                .energyOutput(EnergyOutput.builder("RU", 2, 2)
                        .faces(MachineFace.FRONT, MachineFace.BACK).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build();
    }

    /** Bottom input is required by the behavior machine. The crank emits on the front. */
    private static BehaviorMachine manualMotor(String id, String material, int tier, String energy, long outputMax) {
        return BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType("KU".equals(energy) ? MANUAL_KINETIC_MOTOR : MANUAL_MOTOR).tier(tier).build())
                .energy(EnergyInput.builder(energy, outputMax).faces(MachineFace.BOTTOM)
                        .capacity(outputMax).build())
                .energyOutput(EnergyOutput.builder(energy, outputMax, 1).faces(MachineFace.FRONT).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build();
    }

    /** Back in, front out, at {@code vmax}. Capacity 0, so nothing is kept past the tick. */
    private static BehaviorMachine flywheel(String id, String material, String type, int tier,
            long vmax, int slots, int ampere) {
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(type).tier(tier).build())
                .energy(EnergyInput.builder("RU", vmax).faces(MachineFace.BACK).build())
                .energyOutput(EnergyOutput.builder("RU", vmax, ampere).faces(MachineFace.FRONT).build())
                .gui(Ktfru.PLACEHOLDER_GUI);
        int x0 = slots == 1 ? 80 : 44;
        for (int slot = 0; slot < slots; slot++) {
            builder.item(x0 + slot * 18, 8, BehaviorMachine.SlotRole.INPUT);
        }
        return builder.build();
    }
}
