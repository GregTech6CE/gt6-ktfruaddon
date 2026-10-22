package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.FuelGenerator;
import com.gregtech.gregtech.api.addon.FuelOutput;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/**
 * Card 13. Gas turbines and expanded motors are fuel multiblocks: the fuel map
 * burns and RU leaves. Steam turbines, dynamos and the boiler are behavior machines.
 * A fuel declaration has no structure-hint key; the layout name is the machine type
 * and the description is in the language file.
 */
public final class Gt6KtfruTurbines {
    static final String BOILER = "fluid_boiler";
    static final String EXCITE = "separate_excite_dynamo";
    private static final String GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";
    private static final int AMPERE = 16;

    private Gt6KtfruTurbines() {}

    public static void add(List<AddonMachine> machines) {
        gas(machines, "large_gas_turbine_magnalium", "StainlessSteel", 1024);
        gas(machines, "large_gas_turbine_trinitanium", "Titanium", 4096);
        gas(machines, "large_gas_turbine_graphene", "Tungstensteel", 16384);
        gas(machines, "large_gas_turbine_trinaquadalloy", "Trinaquadalloy", 131072);
        gas(machines, "large_gas_turbine_vibramantium", "Adamantium", 524288);
        steam(machines, "large_steam_turbine_magnalium", "StainlessSteel", 1024);
        steam(machines, "large_steam_turbine_trinitanium", "Titanium", 4096);
        steam(machines, "large_steam_turbine_graphene", "Tungstensteel", 16384);
        steam(machines, "large_steam_turbine_trinaquadalloy", "Trinaquadalloy", 131072);
        steam(machines, "large_steam_turbine_vibramantium", "Adamantium", 524288);
        dynamo(machines, "dynamo_stainless", "StainlessSteel", 1024, 4096, 3686);
        dynamo(machines, "dynamo_titanium", "Titanium", 4096, 16384, 15400);
        dynamo(machines, "dynamo_tungstensteel", "Tungstensteel", 16384, 65536, 61603);
        dynamo(machines, "dynamo_trinaquadalloy", "Trinaquadalloy", 65536, 262144, 246415);
        dynamo(machines, "dynamo_adamantium", "Adamantium", 262144, 1048576, 985660);
        machines.add(excite());
        motor(machines, "expanded_motor_steel", "Steel", 256);
        motor(machines, "expanded_motor_stainless", "StainlessSteel", 1024);
        motor(machines, "expanded_motor_titanium", "Titanium", 4096);
        motor(machines, "expanded_motor_tungstensteel", "Tungstensteel", 16384);
        motor(machines, "expanded_motor_trinaquadalloy", "Trinaquadalloy", 131072);
        motor(machines, "expanded_motor_adamantium", "Adamantium", 524288);
        machines.add(boiler());
    }

    public static void addLayouts(List<StructureLayout> layouts) {
        layouts.add(Gt6KtfruTurbineLayouts.turbine("large_gas_turbine_magnalium", "turbine_shell_stainless"));
        layouts.add(Gt6KtfruTurbineLayouts.turbine("large_gas_turbine_trinitanium", "turbine_shell_titanium"));
        layouts.add(Gt6KtfruTurbineLayouts.turbine("large_gas_turbine_graphene", "turbine_shell_tungstensteel"));
        layouts.add(Gt6KtfruTurbineLayouts.turbine("large_gas_turbine_trinaquadalloy", "turbine_shell_trinaquadalloy"));
        layouts.add(Gt6KtfruTurbineLayouts.turbine("large_gas_turbine_vibramantium", "turbine_shell_adamantium"));
        layouts.add(Gt6KtfruTurbineLayouts.turbine("large_steam_turbine_magnalium", "turbine_shell_stainless"));
        layouts.add(Gt6KtfruTurbineLayouts.turbine("large_steam_turbine_trinitanium", "turbine_shell_titanium"));
        layouts.add(Gt6KtfruTurbineLayouts.turbine("large_steam_turbine_graphene", "turbine_shell_tungstensteel"));
        layouts.add(Gt6KtfruTurbineLayouts.turbine("large_steam_turbine_trinaquadalloy", "turbine_shell_trinaquadalloy"));
        layouts.add(Gt6KtfruTurbineLayouts.turbine("large_steam_turbine_vibramantium", "turbine_shell_adamantium"));
        layouts.add(Gt6KtfruTurbineLayouts.dynamo("dynamo_stainless", "turbine_shell_stainless"));
        layouts.add(Gt6KtfruTurbineLayouts.dynamo("dynamo_titanium", "turbine_shell_titanium"));
        layouts.add(Gt6KtfruTurbineLayouts.dynamo("dynamo_tungstensteel", "turbine_shell_tungstensteel"));
        layouts.add(Gt6KtfruTurbineLayouts.dynamo("dynamo_trinaquadalloy", "turbine_shell_trinaquadalloy"));
        layouts.add(Gt6KtfruTurbineLayouts.dynamo("dynamo_adamantium", "turbine_shell_adamantium"));
        layouts.add(Gt6KtfruTurbineLayouts.excite(EXCITE));
        layouts.add(Gt6KtfruTurbineLayouts.motor("expanded_motor_steel", "steel_wall"));
        layouts.add(Gt6KtfruTurbineLayouts.motor("expanded_motor_stainless", "tank_wall"));
        layouts.add(Gt6KtfruTurbineLayouts.motor("expanded_motor_titanium", "titanium_wall"));
        layouts.add(Gt6KtfruTurbineLayouts.motor("expanded_motor_tungstensteel", "tungstensteel_wall"));
        layouts.add(Gt6KtfruTurbineLayouts.motor("expanded_motor_trinaquadalloy", "trinaquadalloy_wall"));
        layouts.add(Gt6KtfruTurbineLayouts.motor("expanded_motor_adamantium", "adamantium_wall"));
        layouts.add(Gt6KtfruTurbineLayouts.boiler(BOILER));
    }

    public static void addParts(List<LayoutPart> parts) {
        parts.add(casing("turbine_shell_stainless", "StainlessSteel", 6.0F));
        parts.add(casing("turbine_shell_titanium", "Titanium", 6.0F));
        parts.add(casing("turbine_shell_tungstensteel", "Tungstensteel", 6.0F));
        parts.add(casing("turbine_shell_adamantium", "Adamantium", 6.0F));
        parts.add(casing("turbine_shell_trinaquadalloy", "Trinaquadalloy", 10.0F));
        parts.add(casing("trinaquadalloy_wall", "Trinaquadalloy", 10.0F));
        parts.add(casing("boiler_heat_pipe", "Invar", 10.0F));
        parts.add(casing("pressure_pipe", "StainlessSteel", 10.0F));
        parts.add(casing("high_pressure_exchanger", "Invar", 10.0F));
    }

    public static Set<String> behaviorTypes() {
        return Set.of(
                "large_steam_turbine_magnalium",
                "large_steam_turbine_trinitanium",
                "large_steam_turbine_graphene",
                "large_steam_turbine_trinaquadalloy",
                "large_steam_turbine_vibramantium",
                "dynamo_stainless",
                "dynamo_titanium",
                "dynamo_tungstensteel",
                "dynamo_trinaquadalloy",
                "dynamo_adamantium",
                EXCITE,
                BOILER);
    }

    public static boolean owns(String machineType) {
        return behaviorTypes().contains(machineType);
    }

    public static MachineBehavior create(String machineType) {
        return switch (machineType) {
            case "large_steam_turbine_magnalium" -> new Gt6KtfruSteamTurbine(1024);
            case "large_steam_turbine_trinitanium" -> new Gt6KtfruSteamTurbine(4096);
            case "large_steam_turbine_graphene" -> new Gt6KtfruSteamTurbine(16384);
            case "large_steam_turbine_trinaquadalloy" -> new Gt6KtfruSteamTurbine(131072);
            case "large_steam_turbine_vibramantium" -> new Gt6KtfruSteamTurbine(524288);
            case "dynamo_stainless" -> new Gt6KtfruDynamo(4096, 3686, AMPERE, 0);
            case "dynamo_titanium" -> new Gt6KtfruDynamo(16384, 15400, AMPERE, 0);
            case "dynamo_tungstensteel" -> new Gt6KtfruDynamo(65536, 61603, AMPERE, 0);
            case "dynamo_trinaquadalloy" -> new Gt6KtfruDynamo(262144, 246415, AMPERE, 0);
            case "dynamo_adamantium" -> new Gt6KtfruDynamo(1048576, 985660, AMPERE, 0);
            case EXCITE -> new Gt6KtfruDynamo(1024, 870, AMPERE, 5);
            case BOILER -> new Gt6KtfruFluidBoiler();
            default -> throw new IllegalArgumentException(machineType);
        };
    }

    private static void gas(List<AddonMachine> machines, String id, String material, long rate) {
        machines.add(fuel(id, material, Gt6KtfruMachines.GAS_FUEL, rate, true));
    }

    private static void motor(List<AddonMachine> machines, String id, String material, long rate) {
        machines.add(fuel(id, material, Gt6KtfruMachines.ENGINE_FUEL, rate, false));
    }

    /**
     * Waste off is efficiency 10000. The generator stores at most two packets,
     * which is the limit-consumption cap. Gas leaves four blocks behind the front.
     * An expanded motor leaves the front. The declared ampere of 4 is one packet here.
     */
    private static FuelGenerator fuel(String id, String material, String fuelMap, long rate, boolean gas) {
        FuelOutput output = gas
                ? FuelOutput.builder("RU", rate).behind(4).build()
                : FuelOutput.builder("RU", rate).front().build();
        return FuelGenerator.builder(MachineCasing.builder(id, material).machineType(id).build())
                .fuelMap(fuelMap)
                .efficiency(10_000)
                .output(output)
                .tank(Math.toIntExact(rate * 16))
                .multi()
                .build();
    }

    /**
     * Nominal RU is twice the minimum, so the fixed half-to-double window is the
     * registered minimum and maximum. EU leaves the back face.
     */
    private static void dynamo(List<AddonMachine> machines, String id, String material,
            long inputMin, long inputMax, long outputMax) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("RU", inputMin * 2)
                        .faces(MachineFace.FRONT)
                        .capacity(inputMax * AMPERE * 2)
                        .build())
                .energyOutput(EnergyOutput.builder("EU", outputMax, AMPERE).faces(MachineFace.BACK).build())
                .gui(GUI)
                .build());
    }

    /**
     * RU on the front, EU out the back. The 5 EU excite input cannot share the front,
     * so it sits on the top.
     */
    private static BehaviorMachine excite() {
        return BehaviorMachine.builder(MachineCasing.builder(EXCITE, "Steel").machineType(EXCITE).build())
                .energy(EnergyInput.builder("RU", 512).faces(MachineFace.FRONT).capacity(1024 * AMPERE * 2).build())
                .auxiliaryEnergy(EnergyInput.builder("EU", 5).anyPacket()
                        .faces(MachineFace.TOP).capacity(1024).build())
                .energyOutput(EnergyOutput.builder("EU", 870, AMPERE).faces(MachineFace.BACK).build())
                .gui(GUI)
                .build();
    }

    /** Bottom RU is the required input. Steam enters the first tank and RU leaves the front. */
    private static void steam(List<AddonMachine> machines, String id, String material, long rate) {
        int steamTank = Math.toIntExact(Math.min(1_024_000_000L, rate * 200));
        int waterTank = Math.toIntExact(Math.min(1_024_000_000L, rate * 4));
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("RU", 1).faces(MachineFace.BOTTOM).capacity(rate).build())
                .energyOutput(EnergyOutput.builder("RU", rate, 1).faces(MachineFace.FRONT).build())
                .gui(GUI)
                .fluid(steamTank, 32, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(waterTank, 56, 8, BehaviorMachine.SlotRole.OUTPUT)
                .build());
    }

    /**
     * No energy type in the registration. Bottom HU of 1 only satisfies the behavior machine.
     * Tanks are hot fuel, distilled water, steam and one exhaust.
     */
    private static BehaviorMachine boiler() {
        long rate = Gt6KtfruFluidBoiler.RATE;
        return BehaviorMachine.builder(MachineCasing.builder(BOILER, "Invar").machineType(BOILER).build())
                .energy(EnergyInput.builder("HU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(GUI)
                .progress(78, 8)
                .fluid(Math.toIntExact(rate * 4), 28, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(Math.toIntExact(rate * 16), 50, 8, BehaviorMachine.SlotRole.INPUT)
                .fluid(Math.toIntExact(rate * 640), 108, 8, BehaviorMachine.SlotRole.OUTPUT)
                .fluid(Math.toIntExact(rate * 16), 130, 8, BehaviorMachine.SlotRole.OUTPUT)
                .build();
    }

    private static LayoutPart casing(String name, String material, float hardness) {
        return LayoutPart.builder(name, material).hardness(hardness).build();
    }
}
