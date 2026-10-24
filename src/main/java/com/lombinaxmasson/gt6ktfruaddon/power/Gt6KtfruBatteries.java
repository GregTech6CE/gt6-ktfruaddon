package com.lombinaxmasson.gt6ktfruaddon.power;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.EnergyOutput;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.core.layout.Parts;

/**
 * Card 14. Liquid batteries store RU from the fluid in a one-cell sink.
 * Transform batteries store EU, RU or LU. The registered input span is wider
 * than the main half-to-double window, so that input is the auxiliary buffer.
 * Bottom HU of 1 only satisfies the behavior machine.
 */
public final class Gt6KtfruBatteries implements Feature {

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add("liquid_battery_stainless", () -> new Gt6KtfruLiquidBattery(512, 1024, 64, 0.1))
            .add("liquid_battery_titanium", () -> new Gt6KtfruLiquidBattery(2048, 4096, 128, 0.1))
            .add("liquid_battery_tungstensteel", () -> new Gt6KtfruLiquidBattery(8192, 16384, 256, 0.1))
            .add("transform_battery_eu", () -> new Gt6KtfruTransformBattery("EU", 0.1, false))
            .add("transform_battery_ru", () -> new Gt6KtfruTransformBattery("RU", 0.04, false))
            .add("transform_battery_lu", () -> new Gt6KtfruTransformBattery("LU", 0.0002, true))
            .build();

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        liquid(machines, "liquid_battery_stainless", "StainlessSteel", 1024, 512, 1024, 64);
        liquid(machines, "liquid_battery_titanium", "Titanium", 4096, 2048, 4096, 128);
        liquid(machines, "liquid_battery_tungstensteel", "Tungstensteel", 16384, 8192, 16384, 256);
        machines.add(transform("transform_battery_eu", "Titanium", "EU", 16384, 128, false));
        machines.add(transform("transform_battery_ru", "Tungstensteel", "RU", 65536, 256, false));
        machines.add(transform("transform_battery_lu", "Titanium", "LU", 262144, 1024, true));
        return List.copyOf(machines);
    }

    @Override
    public List<StructureLayout> layouts() {
        List<StructureLayout> layouts = new ArrayList<>();
        layouts.add(Gt6KtfruBatteryLayouts.liquid("liquid_battery_stainless", "tank_wall_dense"));
        layouts.add(Gt6KtfruBatteryLayouts.liquid("liquid_battery_titanium", "dense_titanium_wall"));
        layouts.add(Gt6KtfruBatteryLayouts.liquid("liquid_battery_tungstensteel", "implosion_compressor_wall"));
        layouts.add(Gt6KtfruBatteryLayouts.transform("transform_battery_eu",
                "titanium_wall", "large_niobium_titanium_coil", "magnetic_conductor", "battery_cabin"));
        layouts.add(Gt6KtfruBatteryLayouts.transform("transform_battery_ru",
                "tungstensteel_wall", "tungsten_wall", "transform_battery_gear", "flywheel_cabin"));
        layouts.add(Gt6KtfruBatteryLayouts.transform("transform_battery_lu",
                "titanium_wall", "titanium_wall", "energy_crystal", "energy_crystal"));
        return List.copyOf(layouts);
    }

    @Override
    public List<LayoutPart> parts() {
        List<LayoutPart> parts = new ArrayList<>();
        parts.add(Parts.casing("liquid_battery_wall_bronze", "Bronze", 1.0F));
        parts.add(Parts.casing("liquid_battery_wall_steel", "Steel", 1.0F));
        parts.add(Parts.casing("liquid_battery_wall_plastic", "Plastic", 1.0F));
        parts.add(Parts.casing("magnetic_conductor", "ElectricalSteel", 6.0F));
        parts.add(Parts.casing("battery_cabin", "Chromium", 6.0F));
        parts.add(Parts.casing("flywheel_cabin", "TitaniumIridium", 6.0F));
        parts.add(Parts.casing("transform_battery_gear", "Tungstensteel", 6.0F));
        parts.add(Parts.casing("energy_crystal", "EnergiumRed", 6.0F));
        return List.copyOf(parts);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    private static void liquid(List<AddonMachine> machines, String id, String material,
            long inputMax, long outputMin, long outputMax, long ampere) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("HU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .auxiliaryEnergy(EnergyInput.builder("RU", 64).range(64, inputMax)
                        .faces(MachineFace.LEFT, MachineFace.RIGHT, MachineFace.BACK, MachineFace.TOP)
                        .capacity(inputMax * ampere)
                        .build())
                .energyOutput(EnergyOutput.builder("RU", outputMax, ampere).faces(MachineFace.FRONT).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .fluid(Gt6KtfruLiquidBattery.TANK, 28, 8, BehaviorMachine.SlotRole.ANY)
                .build());
    }

    private static BehaviorMachine transform(String id, String material, String energy,
            long inputMax, long ampere, boolean light) {
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).build())
                .energy(EnergyInput.builder("HU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .auxiliaryEnergy(EnergyInput.builder(energy, 64).range(64, inputMax)
                        .faces(MachineFace.LEFT, MachineFace.RIGHT, MachineFace.BACK, MachineFace.TOP)
                        .capacity(inputMax * ampere)
                        .build())
                .gui(Ktfru.PLACEHOLDER_GUI);
        if (!light) {
            for (int slot = 0; slot < 8; slot++) {
                builder.item(26 + slot * 18, 8, BehaviorMachine.SlotRole.ANY);
            }
            builder.toggle("gt6ktfruaddon.transform_battery.sealed", 152, 64, false);
        }
        return builder.build();
    }

}
