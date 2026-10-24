package com.lombinaxmasson.gt6ktfruaddon.kinetic;

import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Hand crank. A click adds 8 ticks, up to the tier cap, and exhausts the player.
 * While time remains, one positive packet leaves the front. KU sign alternation
 * is not available on {@code emit}.
 */
public final class ManualMotor implements MachineBehavior {
    private static final long GAIN = 8;
    /** output min, output max, max time. Tiers: wood RU, bronze RU, wood KU, bronze KU. */
    private static final long[][] TIER = {
            {4, 10, 400},
            {14, 22, 800},
            {3, 8, 200},
            {6, 16, 400}
    };

    @Override
    public void tick(MachineBehaviorContext context) {
        long[] tier = tier(context);
        long time = context.state().getLong("time");
        long before = rate(time, tier[2], tier[0], tier[1]);
        if (time > 0) {
            if (before > 0) context.generate(before);
            time--;
            context.state().setLong("time", time);
        }
        long after = rate(time, tier[2], tier[0], tier[1]);
        if (after >= 1 && context.energy() >= after) {
            context.emit(after, 1);
            if (context.energy() >= after) context.consume(after);
        }
        context.working(time > 0);
        context.progress(tier[2] <= 0 ? 0 : time / (float) tier[2]);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, net.neoforged.neoforge.fluids.FluidStack stack) {
        return false;
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        long[] tier = tier(context);
        long time = context.state().getLong("time");
        if (player.isAlive() && time <= tier[2]) {
            context.state().setLong("time", time + GAIN);
            player.causeFoodExhaustion(0.5F);
        }
        return true;
    }

    /** Same steps as the source {@code getRate}: the top fifth is the maximum. */
    private static long rate(long time, long maxTime, long min, long max) {
        if (time <= 0 || maxTime <= 0) return 0;
        float fraction = time / (float) maxTime;
        long span = max - min;
        if (fraction > 0.8F) return max;
        if (fraction > 0.6F) return (long) (max - span * 0.25);
        if (fraction > 0.4F) return (long) (max - span * 0.5);
        if (fraction > 0.2F) return (long) (max - span * 0.75);
        return min;
    }

    private static long[] tier(MachineBehaviorContext context) {
        int index = context.tier() - 1;
        if (index < 0 || index >= TIER.length) return TIER[0];
        return TIER[index];
    }
}
