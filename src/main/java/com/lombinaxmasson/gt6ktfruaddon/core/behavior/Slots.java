package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.List;
import java.util.HashSet;
import java.util.stream.IntStream;

/** Inventory indexes shared by declarations and behavior; inputs and outputs must be disjoint. */
public record Slots(List<Integer> itemsIn, List<Integer> itemsOut, List<Integer> fluidsIn, List<Integer> fluidsOut) {
    public Slots {
        itemsIn = List.copyOf(itemsIn); itemsOut = List.copyOf(itemsOut);
        fluidsIn = List.copyOf(fluidsIn); fluidsOut = List.copyOf(fluidsOut);
        validate(itemsIn, itemsOut); validate(fluidsIn, fluidsOut);
    }
    private static void validate(List<Integer> input, List<Integer> output) {
        var unique = new HashSet<Integer>();
        for (int index : java.util.stream.Stream.concat(input.stream(), output.stream()).toList()) {
            if (index < 0 || !unique.add(index)) throw new IllegalArgumentException("duplicate or negative slot: " + index);
        }
    }
    public com.gregtech.gregtech.api.addon.BehaviorMachine.SlotRole itemRole(int slot) {
        return role(itemsIn, itemsOut, slot);
    }
    public com.gregtech.gregtech.api.addon.BehaviorMachine.SlotRole fluidRole(int tank) {
        return role(fluidsIn, fluidsOut, tank);
    }
    private static com.gregtech.gregtech.api.addon.BehaviorMachine.SlotRole role(List<Integer> in, List<Integer> out, int index) {
        if (in.contains(index)) return com.gregtech.gregtech.api.addon.BehaviorMachine.SlotRole.INPUT;
        if (out.contains(index)) return com.gregtech.gregtech.api.addon.BehaviorMachine.SlotRole.OUTPUT;
        throw new IllegalArgumentException("undeclared slot: " + index);
    }
    public static List<Integer> range(int first, int count) {
        return IntStream.range(first, first + count).boxed().toList();
    }
}
