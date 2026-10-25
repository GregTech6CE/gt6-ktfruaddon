package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

/** What a behavior gets from the base without writing it. */
class BehaviorBaseTest {
    private static final BehaviorBase NOTHING = new BehaviorBase() {
        @Override
        public void tick(MachineBehaviorContext context) {}
    };

    @Test
    void takesNoItem() {
        assertFalse(NOTHING.canInsert(null, 0, null));
    }

    @Test
    void takesNoFluid() {
        assertFalse(NOTHING.canFill(null, 0, null));
    }
}
