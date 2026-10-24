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
 * The transformer, flywheel-gear and light-transformer parts, sources 10200 to 10255.
 */
public final class Gt6KtfruTransformerParts implements Feature {
    public static final String TRANSFORMER_SMALL = "transformer_small";
    public static final String TRANSFORMER_LARGE = "transformer_large";
    public static final String FLYWHEEL_GEAR_SMALL = "flywheel_gear_small";
    public static final String FLYWHEEL_GEAR_LARGE = "flywheel_gear_large";
    public static final String LIGHT_TRANSFORMER_SMALL = "light_transformer_small";
    public static final String LIGHT_TRANSFORMER_LARGE = "light_transformer_large";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(TRANSFORMER_SMALL, () -> new Gt6KtfruTransformerPart(4))
            .add(TRANSFORMER_LARGE, () -> new Gt6KtfruTransformerPart(16))
            .add(FLYWHEEL_GEAR_SMALL, () -> new Gt6KtfruTransformerPart(4))
            .add(FLYWHEEL_GEAR_LARGE, () -> new Gt6KtfruTransformerPart(16))
            .add(LIGHT_TRANSFORMER_SMALL, () -> new Gt6KtfruTransformerPart(4))
            .add(LIGHT_TRANSFORMER_LARGE, () -> new Gt6KtfruTransformerPart(16))
            .build();

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        transformerParts(machines);
        return List.copyOf(machines);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    /**
     * Source 10200–10255. The back stands in for the inside face. The buffer
     * holds ampere packets and the front emits that packet size.
     */
    private static void transformerParts(List<AddonMachine> machines) {
        String[] material = {
                "SteelGalvanized", "Aluminium", "StainlessSteel", "Chromium", "Titanium", "Iridium"
        };
        String[] electric = {"lv", "mv", "hv", "ev", "iv", "luv"};
        String[] gear = {"t1", "t2", "t3", "t4", "t5", "t6"};
        String[] type = {
                TRANSFORMER_SMALL, TRANSFORMER_LARGE,
                FLYWHEEL_GEAR_SMALL, FLYWHEEL_GEAR_LARGE,
                LIGHT_TRANSFORMER_SMALL, LIGHT_TRANSFORMER_LARGE
        };
        String[] id = {
                "transformer_small", "transformer_large",
                "flywheel_gear_small", "flywheel_gear_large",
                "light_transformer_small", "light_transformer_large"
        };
        String[] energy = {"EU", "EU", "RU", "RU", "LU", "LU"};
        int[] ampere = {4, 16, 4, 16, 4, 16};
        for (int kind = 0; kind < type.length; kind++) {
            String[] suffix = "RU".equals(energy[kind]) ? gear : electric;
            for (int tier = 0; tier < material.length; tier++) {
                machines.add(transformerPart(id[kind] + "_" + suffix[tier], material[tier],
                        type[kind], tier + 1, energy[kind], ampere[kind]));
            }
        }
    }

    private static BehaviorMachine transformerPart(String id, String material, String type, int tier,
                                                   String energy, int ampere) {
        long packet = Gt6KtfruTransformerPart.packet(tier);
        return BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(type).tier(tier).build())
                .energy(EnergyInput.builder(energy, packet).faces(MachineFace.BACK)
                        .capacity(packet * ampere).build())
                .energyOutput(EnergyOutput.builder(energy, packet, ampere).faces(MachineFace.FRONT).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .toggle("gt6ktfruaddon.transformer.stopped", 152, 8, false)
                .build();
    }
}
