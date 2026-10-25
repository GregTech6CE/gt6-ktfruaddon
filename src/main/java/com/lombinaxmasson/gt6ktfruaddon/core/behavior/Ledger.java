package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.ToIntBiFunction;

/** Plans on copies; a failed transaction never changes the supplied inventory. */
public final class Ledger<S> {
    public interface Stack<S> {
        int amount(S stack);
        S amount(S stack, int amount);
        boolean same(S a, S b);
    }
    public record Need<S>(S stack, boolean kept) {}
    private final Stack<S> stacks;
    public Ledger(Stack<S> stacks) { this.stacks = stacks; }

    public List<S> consume(List<S> inventory, List<Need<S>> needs, BiPredicate<S, S> matches) {
        List<S> remaining = new ArrayList<>(inventory);
        int[] retained = new int[inventory.size()];
        for (Need<S> need : needs) {
            int left = stacks.amount(need.stack());
            for (int i = 0; i < remaining.size() && left > 0; i++) {
                S have = remaining.get(i);
                if (stacks.amount(have) == 0 || !matches.test(need.stack(), have)) continue;
                int take = Math.min(left, stacks.amount(have));
                remaining.set(i, stacks.amount(have, stacks.amount(have) - take));
                if (need.kept()) retained[i] += take;
                left -= take;
            }
            if (left > 0) return null;
        }
        for (int i = 0; i < remaining.size(); i++) {
            if (retained[i] > 0) remaining.set(i, stacks.amount(inventory.get(i),
                    stacks.amount(remaining.get(i)) + retained[i]));
        }
        return remaining;
    }

    public List<S> produce(List<S> inventory, List<S> outputs, ToIntBiFunction<Integer, S> capacity) {
        List<S> result = new ArrayList<>(inventory);
        for (S output : outputs) {
            int left = stacks.amount(output);
            // Fill matching occupied slots before claiming empty ones.
            for (int pass = 0; pass < 2 && left > 0; pass++) {
                for (int i = 0; i < result.size() && left > 0; i++) {
                    S have = result.get(i);
                    int count = stacks.amount(have);
                    if (pass == 0 ? count == 0 || !stacks.same(have, output) : count != 0) continue;
                    int room = Math.max(0, capacity.applyAsInt(i, output) - count);
                    int put = Math.min(left, room);
                    if (put > 0) result.set(i, stacks.amount(output, count + put));
                    left -= put;
                }
            }
            if (left > 0) return null;
        }
        return result;
    }
}
