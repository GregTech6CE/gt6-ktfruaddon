package com.lombinaxmasson.gt6ktfruaddon;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gregtech.gregtech.api.addon.platform.BehaviorState;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

/**
 * The part of a machine that energy, progress and state code touches, for tests on the plain JVM. A
 * call to anything else throws, so a test fails loudly when the code under test reaches further than
 * the test thought. Items and fluids are not here: a plain JVM cannot create stacks.
 */
final class FakeMachine {
    /** Stored energy in the main buffer. */
    long energy;
    /** Capacity of the main buffer. */
    long capacity = 1_000_000;
    /** Auxiliary buffers by energy type. */
    final Map<String, Long> auxiliary = new HashMap<>();
    /** What {@code state()} holds. */
    final Map<String, Long> longs = new HashMap<>();
    /** Every {@code consume} call, as "type:amount", the main buffer as "main:amount". */
    final List<String> consumed = new ArrayList<>();
    /** The last value given to {@code progress(float)}, or -1 before any. */
    float progress = -1;
    /** The last value given to {@code working(boolean)}, or null before any. */
    Boolean working;

    final Map<String, String> strings = new HashMap<>();
    int stateWrites;
    final MachineBehaviorContext context;

    FakeMachine() {
        BehaviorState state = proxy(BehaviorState.class, (self, method, args) -> switch (method.getName()) {
            case "getLong" -> longs.getOrDefault((String) args[0], 0L);
            case "setLong" -> {
                stateWrites++;
                longs.put((String) args[0], (Long) args[1]);
                yield null;
            }
            case "getInt" -> longs.getOrDefault((String) args[0], 0L).intValue();
            case "setInt" -> { stateWrites++; longs.put((String) args[0], ((Integer)args[1]).longValue()); yield null; }
            case "getString" -> strings.getOrDefault((String) args[0], "");
            case "setString" -> { stateWrites++; strings.put((String) args[0], (String)args[1]); yield null; }
            default -> throw new UnsupportedOperationException("state." + method.getName());
        });
        context = proxy(MachineBehaviorContext.class, (self, method, args) -> {
            boolean none = args == null || args.length == 0;
            switch (method.getName()) {
                case "energy":
                    return none ? energy : auxiliary.getOrDefault((String) args[0], 0L);
                case "setCapacity":
                    capacity = (Long) args[0];
                    return null;
                case "capacity":
                    return capacity;
                case "generate": {
                    long stored = Math.max(0, Math.min((Long) args[0], capacity - energy));
                    energy += stored;
                    return stored;
                }
                case "consume": {
                    if (args.length == 1) {
                        long amount = (Long) args[0];
                        consumed.add("main:" + amount);
                        if (amount > energy) return false;
                        energy -= amount;
                        return true;
                    }
                    String type = (String) args[0];
                    long amount = (Long) args[1];
                    consumed.add(type + ":" + amount);
                    long have = auxiliary.getOrDefault(type, 0L);
                    if (amount > have) return false;
                    auxiliary.put(type, have - amount);
                    return true;
                }
                case "progress":
                    if (!none) progress = (Float) args[0];
                    return none ? progress : null;
                case "working":
                    working = (Boolean) args[0];
                    return null;
                case "state":
                    return state;
                default:
                    throw new UnsupportedOperationException(method.getName());
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, (self, method, args) -> {
            if (method.getDeclaringClass() == Object.class) {
                return switch (method.getName()) {
                    case "toString" -> "fake " + type.getSimpleName();
                    case "hashCode" -> System.identityHashCode(self);
                    default -> self == args[0];
                };
            }
            return handler.invoke(self, method, args);
        });
    }
}
