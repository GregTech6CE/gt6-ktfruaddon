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
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;

/**
 * The three AI research centers (30106–30108): each allocates 67,108,864 Normal compute per tick from
 * its bound compute controller and adds its points per tick (8, 4 or 2) to the bound or adjacent
 * research monitor.
 */
public final class AiCenters implements Feature {
    static final String FLOW = "ai_flow_control";
    static final String FILL = "ai_fill_planning";
    static final String PATTERN = "ai_pattern_recognition";

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(FLOW, () -> new LateBehavior(false, 8))
            .add(FILL, () -> new LateBehavior(false, 4))
            .add(PATTERN, () -> new LateBehavior(false, 2))
            .build();

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(ai(FLOW));
        machines.add(ai(FILL));
        machines.add(ai(PATTERN));
        return List.copyOf(machines);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    private static BehaviorMachine ai(String id) {
        return BehaviorMachine.builder(MachineCasing.builder(id, "Tungstensteel").machineType(id).build())
                .energy(EnergyInput.builder("EU", 1).faces(MachineFace.BOTTOM).capacity(1).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .build();
    }
}
