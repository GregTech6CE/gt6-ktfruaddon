package com.lombinaxmasson.gt6ktfruaddon.power;

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

    private static final List<Tier> TIERS = List.of(
            new Tier(1, "SteelGalvanized", "lv", "t1"),
            new Tier(2, "Aluminium", "mv", "t2"),
            new Tier(3, "StainlessSteel", "hv", "t3"),
            new Tier(4, "Chromium", "ev", "t4"),
            new Tier(5, "Titanium", "iv", "t5"),
            new Tier(6, "Iridium", "luv", "t6"));

    private static final List<Kind> KINDS = List.of(
            new Kind(TRANSFORMER_SMALL, "EU", 4, false),
            new Kind(TRANSFORMER_LARGE, "EU", 16, false),
            new Kind(FLYWHEEL_GEAR_SMALL, "RU", 4, true),
            new Kind(FLYWHEEL_GEAR_LARGE, "RU", 16, true),
            new Kind(LIGHT_TRANSFORMER_SMALL, "LU", 4, false),
            new Kind(LIGHT_TRANSFORMER_LARGE, "LU", 16, false));

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
        for (Kind kind : KINDS) {
            for (Tier tier : TIERS) {
                String suffix = kind.gear() ? tier.gear() : tier.electric();
                machines.add(transformerPart(kind.type() + "_" + suffix, tier.material(), kind.type(), tier.number(),
                        kind.energy(), kind.ampere()));
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

    /** One tier: its number, its material, and the id suffix of the electric parts and of the flywheel gears. */
    private record Tier(int number, String material, String electric, String gear) {}

    /** The type of a part, its energy type and ampere, and whether its ids end in a gear tier, not a voltage. */
    private record Kind(String type, String energy, int ampere, boolean gear) {}
}
