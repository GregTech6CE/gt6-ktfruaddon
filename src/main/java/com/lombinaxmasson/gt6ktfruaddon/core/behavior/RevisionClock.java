package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

/** Orders persisted pairing changes, including several player actions in one tick. Server thread only. */
public final class RevisionClock implements BehaviorLifecycle.Memory {
    private long value;
    public RevisionClock() { BehaviorLifecycle.register(this); }
    public void observe(long revision) { value = Math.max(value, revision); }
    public long next(long minimum) { value = Math.max(value, minimum) + 1; return value; }
    @Override public void clear() { value = 0; }
}
