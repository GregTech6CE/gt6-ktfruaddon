package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

/**
 * The slow loss of stored energy that batteries and flywheels have: on one tick of every 200 of the
 * world clock a share of the buffer is gone. GregTech 6 does it on tick 5 of 200. The clock is the
 * world's, so no counter has to be stored and a machine does not write its state every tick for it.
 */
public final class LossTimer {
    /** Ticks between two losses. */
    static final long PERIOD = 200;
    /** The tick of the period on which the loss happens. */
    static final long PHASE = 5;

    private LossTimer() {}

    /** Whether this tick of the world clock is the one for the loss. */
    public static boolean due(long gameTime) {
        return gameTime % PERIOD == PHASE;
    }

    /** What goes out of {@code stored} when {@code percent} of it is lost. What is kept is rounded down. */
    public static long share(long stored, double percent) {
        long kept = (long) Math.floor(stored * (1.0 - percent / 100.0));
        return Math.max(0, stored - kept);
    }

    /**
     * On the tick for it, takes {@code percent} of the stored energy out of the machine.
     *
     * @return how much was lost
     */
    public static long lose(MachineBehaviorContext context, double percent) {
        return lose(context, context.level().getGameTime(), percent);
    }

    /** The same for a caller that already has the clock, which is also how the tests choose the tick. */
    public static long lose(MachineBehaviorContext context, long gameTime, double percent) {
        if (!due(gameTime)) return 0;
        long drop = share(context.energy(), percent);
        if (drop > 0) context.consume(drop);
        return drop;
    }
}
