package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

/** One-based machine tiers must name a real row; configuration errors cannot select tier one. */
public final class Tiers {
    private Tiers() {}

    public static int index(String machine, int tier, int count) {
        if (tier < 1 || tier > count)
            throw new IllegalArgumentException(machine + " tier " + tier + " outside 1.." + count);
        return tier - 1;
    }
}
