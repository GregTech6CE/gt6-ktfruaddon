package com.lombinaxmasson.gt6ktfruaddon.kinetic;

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
 * Kinetic machines: the void hoppers, miners, water mill, manual motors and flywheel boxes, which take
 * and give RU, KU or EU through behavior machines.
 */
public final class KineticMachines implements Feature {
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
            .add(VOID_HOPPER, VoidHopper::new)
            .add(MINER, Miner::new)
            .add(MINER_RF, Miner::new)
            .add(WATER_MILL, WaterMill::new)
            .add(MANUAL_MOTOR, ManualMotor::new)
            .add(MANUAL_KINETIC_MOTOR, ManualMotor::new)
            .add(FLYWHEEL, () -> new Flywheel(4, 0.1))
            .add(LARGE_FLYWHEEL, () -> new Flywheel(16, 0.15))
            .add(ELECTRIC_FLYWHEEL, () -> new Flywheel(4, 0.07))
            .add(LARGE_ELECTRIC_FLYWHEEL, () -> new Flywheel(16, 0.1))
            .add(VACUUM_FLYWHEEL, () -> new Flywheel(4, 0.0001))
            .add(LARGE_VACUUM_FLYWHEEL, () -> new Flywheel(16, 0.00012))
            .build();

    private static final Tier WOOD = new Tier(1, "wood", "Wood", 8);
    private static final Tier BRONZE = new Tier(2, "bronze", "Bronze", 32);
    private static final Tier STEEL = new Tier(3, "steel", "Steel", 128);
    private static final Tier TITANIUM = new Tier(4, "titanium", "Titanium", 512);
    private static final Tier TUNGSTENSTEEL = new Tier(5, "tungstensteel", "Tungstensteel", 2048);
    private static final Tier ADAMANTIUM = new Tier(6, "adamantium", "Adamantium", 8192);
    private static final List<Tier> TIERS = List.of(WOOD, BRONZE, STEEL, TITANIUM, TUNGSTENSTEEL, ADAMANTIUM);

    /** Source 21010 to 21013: wood takes 64 and bronze 32, then 128 and 512. Those are the source's numbers. */
    private static final List<Hopper> HOPPERS = List.of(
            new Hopper(WOOD, 64, 4),
            new Hopper(BRONZE, 32, 8),
            new Hopper(STEEL, 128, 16),
            new Hopper(TITANIUM, 512, 24));

    private static final List<FlywheelKind> FLYWHEELS = List.of(
            new FlywheelKind(FLYWHEEL, 1, 4),
            new FlywheelKind(LARGE_FLYWHEEL, 4, 16),
            new FlywheelKind(ELECTRIC_FLYWHEEL, 1, 4),
            new FlywheelKind(LARGE_ELECTRIC_FLYWHEEL, 4, 16),
            new FlywheelKind(VACUUM_FLYWHEEL, 1, 4),
            new FlywheelKind(LARGE_VACUUM_FLYWHEEL, 4, 16));

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
        for (Hopper hopper : HOPPERS) {
            Tier tier = hopper.tier();
            machines.add(voidHopper("void_hopper_" + tier.id(), tier.material(), tier.number(),
                    hopper.input(), hopper.range()));
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
        for (FlywheelKind kind : FLYWHEELS) {
            for (Tier tier : TIERS) {
                machines.add(flywheel(kind.type() + "_" + tier.id(), tier.material(), kind.type(), tier.number(),
                        tier.vmax(), kind.slots(), kind.ampere()));
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

    /** One tier of the kinetic ladder: its number, the suffix of its ids, its material and the packet a flywheel takes. */
    private record Tier(int number, String id, String material, long vmax) {}

    /** The tier a void hopper comes in, what it takes in per tick and how far it reaches. */
    private record Hopper(Tier tier, long input, int range) {}

    /** The type of a flywheel, how many items it holds and how many amperes it passes. */
    private record FlywheelKind(String type, int slots, int ampere) {}
}
