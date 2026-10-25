package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.function.IntSupplier;

/** Recipe chances are ten-thousandths. Draw only for an uncertain output of a completing recipe. */
public final class OutputRolls {
    private OutputRolls() {}
    public static boolean succeeds(int chance, IntSupplier draw) {
        if (chance <= 0) return false;
        return chance >= 10000 || draw.getAsInt() < chance;
    }
}
