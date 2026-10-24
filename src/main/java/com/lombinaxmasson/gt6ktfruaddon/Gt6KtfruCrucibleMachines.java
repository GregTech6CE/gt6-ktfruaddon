package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;

/**
 * Card 19. The crucible heats a known iron charge. Item composition stays unread.
 */
public final class Gt6KtfruCrucibleMachines {
    static final String CRUCIBLE = "electromagnet_crucible";
    private static final String GUI = "gt6ktfruaddon:textures/gui/machines/gascompressor.png";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(CRUCIBLE, Gt6KtfruCrucible::new)
            .build();

    private Gt6KtfruCrucibleMachines() {}

    public static void add(List<AddonMachine> machines) {
        machines.add(BehaviorMachine.builder(MachineCasing.builder(CRUCIBLE, "Titanium").machineType(CRUCIBLE).build())
                .energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(8192).build())
                .gui(GUI)
                .toggle("gt6ktfruaddon.crucible.stopped", 152, 64, false)
                .build());
    }

    public static void addLayouts(List<StructureLayout> layouts) {
        layouts.add(Gt6KtfruCrucibleLayouts.crucible(CRUCIBLE));
    }

    public static void addParts(List<LayoutPart> parts) {
        parts.add(LayoutPart.builder("crucible_screen", "Chromium").hardness(6.0F).build());
    }

    public static Set<String> behaviorTypes() {
        return BEHAVIORS.keySet();
    }

    public static boolean owns(String machineType) {
        return BEHAVIORS.containsKey(machineType);
    }

    public static MachineBehavior create(String machineType) {
        Supplier<MachineBehavior> behavior = BEHAVIORS.get(machineType);
        if (behavior == null) throw new IllegalArgumentException(machineType);
        return behavior.get();
    }
}
