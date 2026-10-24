package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;

/**
 * Card 19. The crucible heats a known iron charge. Item composition stays unread.
 */
public final class Gt6KtfruCrucibleMachines implements Feature {
    static final String CRUCIBLE = "electromagnet_crucible";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(CRUCIBLE, Gt6KtfruCrucible::new)
            .build();

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(BehaviorMachine.builder(MachineCasing.builder(CRUCIBLE, "Titanium").machineType(CRUCIBLE).build())
                .energy(EnergyInput.builder("EU", 512).faces(MachineFace.BOTTOM).capacity(8192).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .toggle("gt6ktfruaddon.crucible.stopped", 152, 64, false)
                .build());
        return List.copyOf(machines);
    }

    @Override
    public List<StructureLayout> layouts() {
        List<StructureLayout> layouts = new ArrayList<>();
        layouts.add(Gt6KtfruCrucibleLayouts.crucible(CRUCIBLE));
        return List.copyOf(layouts);
    }

    @Override
    public List<LayoutPart> parts() {
        List<LayoutPart> parts = new ArrayList<>();
        parts.add(LayoutPart.builder("crucible_screen", "Chromium").hardness(6.0F).build());
        return List.copyOf(parts);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }
}
