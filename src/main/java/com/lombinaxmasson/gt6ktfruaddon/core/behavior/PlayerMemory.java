package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/** Pending pairing selections; cleared on logout and dimension unload. Server thread only. */
public final class PlayerMemory<V> implements BehaviorLifecycle.Memory {
    private final Map<UUID, V> values = new HashMap<>();
    private final Function<V, String> dimension;
    public PlayerMemory(Function<V, String> dimension) {
        this.dimension = dimension;
        BehaviorLifecycle.register(this);
    }
    public V get(UUID player) { return values.get(player); }
    public void put(UUID player, V value) { values.put(player, value); }
    public V remove(UUID player) { return values.remove(player); }
    @Override public void clear() { values.clear(); }
    @Override public void clearPlayer(UUID player) { values.remove(player); }
    @Override public void clearDimension(String dim) {
        values.values().removeIf(value -> dim.equals(dimension.apply(value)));
    }
}
