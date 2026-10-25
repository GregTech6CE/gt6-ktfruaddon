package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/** Live values expire after 20 ticks; dimensions use independent clocks. Server thread only. */
public final class TtlRegistry<V> implements BehaviorLifecycle.Memory {
    public static final long LIFETIME = 20L;
    private record Entry<V>(V value, long tick) {}
    private final Map<DimPos, Entry<V>> values = new HashMap<>();

    private final Map<String, Long> swept = new HashMap<>();

    public TtlRegistry() { BehaviorLifecycle.register(this); }
    public void put(DimPos key, V value, long tick) {
        values.put(key, new Entry<>(value, tick));
        // Sweep this dimension while it is active, including machines whose chunks unloaded.
        Long last = swept.get(key.dim());
        if (last == null || tick < last || tick - last > LIFETIME) {
            values.entrySet().removeIf(entry -> entry.getKey().dim().equals(key.dim())
                    && expired(entry.getValue().tick(), tick));
            swept.put(key.dim(), tick);
        }
    }
    public V get(DimPos key, long tick) {
        Entry<V> entry = values.get(key);
        if (entry == null) return null;
        if (expired(entry.tick(), tick)) { values.remove(key); return null; }
        return entry.value();
    }
    public V remove(DimPos key) {
        Entry<V> entry = values.remove(key);
        return entry == null ? null : entry.value();
    }
    public List<DimPos> keys(String dimension, long tick) {
        List<DimPos> result = new ArrayList<>();
        for (DimPos key : List.copyOf(values.keySet())) {
            if (key.dim().equals(dimension) && get(key, tick) != null) result.add(key);
        }
        return result;
    }
    private static boolean expired(long then, long now) { return now < then || now - then > LIFETIME; }
    @Override public void clear() { values.clear(); swept.clear(); }
    @Override public void clearDimension(String dimension) {
        swept.remove(dimension);
        values.keySet().removeIf(key -> key.dim().equals(dimension));
    }
}
