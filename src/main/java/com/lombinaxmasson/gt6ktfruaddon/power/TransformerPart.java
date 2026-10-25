package com.lombinaxmasson.gt6ktfruaddon.power;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

/**
 * Source {@code TransformerPart}. One energy type, one packet size, one ampere.
 * The buffer holds ampere packets. While the stop toggle is off, the front
 * emits as many of those packets as the buffer can pay.
 */
public final class TransformerPart extends BehaviorBase {
    private static final long[] PACKET = {32, 128, 512, 2048, 8192, 32768};

    private final int ampere;

    public TransformerPart(int ampere) {
        this.ampere = ampere;
    }

    public static long packet(int tier) {
        if (tier < 1 || tier > PACKET.length) return PACKET[0];
        return PACKET[tier - 1];
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (context.toggle(0)) {
            context.working(false);
            return;
        }
        long size = packet(context.tier());
        long stored = context.energy();
        if (size >= 1 && stored >= size) {
            long packets = Math.min(ampere, stored / size);
            if (packets >= 1) context.emit(size, packets);
        }
        context.working(context.energy() > 0);
    }
}
