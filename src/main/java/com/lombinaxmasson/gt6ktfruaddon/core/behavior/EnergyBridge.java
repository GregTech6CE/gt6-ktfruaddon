package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

/**
 * Energy that arrives in the auxiliary buffer of a machine has to be moved into the main buffer by
 * the behavior, because the SDK keeps the two apart. This is that move, written once.
 */
public final class EnergyBridge {
    private EnergyBridge() {}

    /**
     * Moves the auxiliary buffer of {@code type} into the main buffer as far as the main buffer has
     * room. Only what the main buffer stored leaves the auxiliary buffer, so nothing is lost when the
     * main buffer is nearly full.
     *
     * @return how much moved
     */
    public static long pull(MachineBehaviorContext context, String type) {
        long available = context.energy(type);
        if (available <= 0) return 0;
        long moved = context.generate(available);
        if (moved > 0) context.consume(type, moved);
        return Math.max(0, moved);
    }
}
