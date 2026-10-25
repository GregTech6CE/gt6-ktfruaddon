package com.lombinaxmasson.gt6ktfruaddon;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.OutputRolls;
class OutputRollsTest {
    @Test void certainAndImpossibleOutputsDoNotDraw() {
        assertTrue(OutputRolls.succeeds(10000,()->{throw new AssertionError();}));
        assertFalse(OutputRolls.succeeds(0,()->{throw new AssertionError();}));
    }
    @Test void everyChanceUsesTheTenThousandthsBoundary() {
        for (int chance:new int[]{2000,4000,5000,8000}) {
            assertTrue(OutputRolls.succeeds(chance,()->chance-1));
            assertFalse(OutputRolls.succeeds(chance,()->chance));
            assertFalse(OutputRolls.succeeds(chance,()->9999));
        }
    }
    @Test void eachOutputGetsItsOwnDraw() {
        int[] draw={0};
        assertTrue(OutputRolls.succeeds(5000,()->draw[0]++));
        assertTrue(OutputRolls.succeeds(5000,()->draw[0]++));
        assertEquals(2,draw[0]);
    }
}
