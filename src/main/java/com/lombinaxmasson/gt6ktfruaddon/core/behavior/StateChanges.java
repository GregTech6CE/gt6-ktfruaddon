package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.Objects;
import com.gregtech.gregtech.api.addon.platform.BehaviorState;

/** The host marks a block dirty on every setter call; write only actual changes. */
public final class StateChanges {
    private StateChanges() {}
    public static void setLong(BehaviorState state, String key, long value) {
        if (state.getLong(key) != value) state.setLong(key, value);
    }
    public static void setInt(BehaviorState state, String key, int value) {
        if (state.getInt(key) != value) state.setInt(key, value);
    }
    public static void setString(BehaviorState state, String key, String value) {
        if (!Objects.equals(state.getString(key), value)) state.setString(key, value);
    }
}
