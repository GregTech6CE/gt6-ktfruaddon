package com.lombinaxmasson.gt6ktfruaddon.research;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

/** Research table block without monitor behavior. */
public final class ResearchTable extends BehaviorBase {
    @Override
    public void tick(MachineBehaviorContext context) {
        context.working(false);
    }
}
