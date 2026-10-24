package com.lombinaxmasson.gt6ktfruaddon.core.feature;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/**
 * The behavior of each machine type a feature declares, collected one type at a time so that a
 * repeated type fails where it is written.
 */
public final class Behaviors {
    private final Map<String, Supplier<MachineBehavior>> entries = new LinkedHashMap<>();

    public Behaviors add(String machineType, Supplier<MachineBehavior> behavior) {
        if (entries.putIfAbsent(machineType, behavior) != null) {
            throw new IllegalStateException("Machine type '" + machineType + "' already has a behavior");
        }
        return this;
    }

    public Map<String, Supplier<MachineBehavior>> build() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(entries));
    }
}
