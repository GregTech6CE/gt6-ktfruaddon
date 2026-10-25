package com.lombinaxmasson.gt6ktfruaddon.recipe;

/** Names the duration and signed power at the call site so adjacent longs cannot be swapped. */
public final class RecipeTiming {
    private final long duration;
    private final long euPerTick;

    private RecipeTiming(long duration, long euPerTick) {
        this.duration = duration;
        this.euPerTick = euPerTick;
    }

    public long duration() { return duration; }
    public long euPerTick() { return euPerTick; }

    public static Duration ticks(long ticks) { return new Duration(ticks); }

    public record Duration(long ticks) {
        public Duration {
            if (ticks < 1) throw new IllegalArgumentException("recipe duration must be positive: " + ticks);
        }

        public RecipeTiming euPerTick(long power) { return new RecipeTiming(ticks, power); }
    }
}
