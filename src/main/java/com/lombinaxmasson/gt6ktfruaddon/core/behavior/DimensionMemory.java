package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.HashMap;
import java.util.Map;

/** Non-expiring messages keyed by dimension and position. Server thread only. */
public final class DimensionMemory<V> implements BehaviorLifecycle.Memory {
    private final Map<DimPos, V> values = new HashMap<>();
    public DimensionMemory() { BehaviorLifecycle.register(this); }
    public V get(DimPos key) { return values.get(key); }
    public void put(DimPos key, V value) { values.put(key, value); }
    public V remove(DimPos key) { return values.remove(key); }
    @Override public void clear() { values.clear(); }
    @Override public void clearDimension(String dimension) {
        values.keySet().removeIf(key -> key.dim().equals(dimension));
    }
}
