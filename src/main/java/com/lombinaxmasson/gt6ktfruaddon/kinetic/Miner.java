package com.lombinaxmasson.gt6ktfruaddon.kinetic;

import com.gregtech.gregtech.api.addon.BehaviorRangeRules;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Miner. One candidate square, then down. {@code breakBlock} is one block a tick
 * and collects drops with an empty hand, so the tool in slot 0 only decides
 * whether a block is eligible.
 */
public final class Miner implements MachineBehavior {
    private static final int NOT_INIT = 0;
    private static final int IDLE = 1;
    private static final int FINDING = 2;
    private static final int BREAKING = 3;
    private static final int FINISHED = 4;
    private static final int RESET = 0;
    /** Declared cube. The scan square is smaller on the lower tiers. */
    private static final int DIG_RANGE = 32;

    private static final long[] INPUT = {128, 32, 128, 512};
    private static final int[] RANGE = {4, 12, 20, 32};
    private static final int[] SPEED = {1, 4, 6, 8};
    private static final int[] HARDNESS = {8, 20, 32, 64};

    @Override
    public void tick(MachineBehaviorContext context) {
        var state = context.state();
        if (state.getInt("phase") == NOT_INIT) init(context);
        if (state.getInt("phase") == FINISHED) {
            context.working(false);
            return;
        }
        long input = input(context);
        long stored = context.energy();
        if (stored < input / 2 || context.item(0).isEmpty()) {
            state.setInt("phase", IDLE);
            context.working(false);
            return;
        }
        if (state.getInt("phase") == IDLE) state.setInt("phase", FINDING);
        long spent = Math.min(stored, input * 2);
        context.consume(spent);
        double overclock = spent / (double) (input / 2);
        if (state.getInt("phase") == FINDING) find(context);
        if (state.getInt("phase") == BREAKING) dig(context, overclock);
        int phase = state.getInt("phase");
        context.working(phase == FINDING || phase == BREAKING);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return slot == 0;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, net.neoforged.neoforge.fluids.FluidStack stack) {
        return false;
    }

    @Override
    public void onToggle(MachineBehaviorContext context, int toggle) {
        if (toggle != RESET || !context.toggle(RESET)) return;
        context.state().setInt("phase", NOT_INIT);
        context.setToggle(RESET, false);
    }

    private static void init(MachineBehaviorContext context) {
        var state = context.state();
        int range = range(context);
        state.setInt("x", -range);
        state.setInt("z", -range);
        state.setInt("y", -1);
        state.setInt("remain", 0);
        state.setInt("phase", IDLE);
    }

    private static void find(MachineBehaviorContext context) {
        var state = context.state();
        int range = range(context);
        int speed = speed(context);
        int x = state.getInt("x");
        int y = state.getInt("y");
        int z = state.getInt("z");
        ItemStack tool = context.item(0);
        var level = context.level();
        BlockPos origin = context.pos();
        for (int i = 0; i < speed; i++) {
            if (finishedAt(origin, x, y, z)) {
                state.setInt("phase", FINISHED);
                break;
            }
            BlockPos target = origin.offset(x, y, z);
            BlockState block = level.getBlockState(target);
            float hardness = block.getDestroySpeed(level, target);
            float dig = tool.getDestroySpeed(block);
            if (block.isAir() || hardness < 0 || hardness > hardness(context)
                    || dig <= 0.1F || !tool.isCorrectToolForDrops(block)) {
                int[] next = advance(origin, x, y, z, range);
                x = next[0];
                y = next[1];
                z = next[2];
                if (next[3] != 0) {
                    state.setInt("phase", FINISHED);
                    break;
                }
                continue;
            }
            state.setInt("remain", (int) (hardness * (4096F / speed) / dig));
            state.setInt("phase", BREAKING);
            break;
        }
        state.setInt("x", x);
        state.setInt("y", y);
        state.setInt("z", z);
    }

    private static void dig(MachineBehaviorContext context, double overclock) {
        var state = context.state();
        int remain = state.getInt("remain") - (int) Math.floor(speed(context) * overclock);
        if (remain > 0) {
            state.setInt("remain", remain);
            return;
        }
        BlockPos origin = context.pos();
        BlockPos target = origin.offset(state.getInt("x"), state.getInt("y"), state.getInt("z"));
        BlockState block = context.level().getBlockState(target);
        if (block.isAir() || context.breakBlock(target)) {
            int[] next = advance(origin, state.getInt("x"), state.getInt("y"), state.getInt("z"), range(context));
            state.setInt("x", next[0]);
            state.setInt("y", next[1]);
            state.setInt("z", next[2]);
            state.setInt("remain", 0);
            state.setInt("phase", next[3] != 0 ? FINISHED : FINDING);
        }
    }

    /** {@code result[3]} is 1 when the next cell is below the world or outside the cube. */
    private static int[] advance(BlockPos origin, int x, int y, int z, int range) {
        if (origin.getY() + y < 1) return new int[] {x, y, z, 1};
        x++;
        if (x >= range) {
            x = -range;
            z++;
        }
        if (z >= range) {
            x = -range;
            z = -range;
            y--;
        }
        return new int[] {x, y, z, finishedAt(origin, x, y, z) ? 1 : 0};
    }

    private static boolean finishedAt(BlockPos origin, int x, int y, int z) {
        return origin.getY() + y < 1 || !BehaviorRangeRules.inside(DIG_RANGE, x, y, z);
    }

    private static int index(MachineBehaviorContext context) {
        int index = context.tier() - 1;
        if (index < 0 || index >= INPUT.length) return 0;
        return index;
    }

    private static long input(MachineBehaviorContext context) { return INPUT[index(context)]; }
    private static int range(MachineBehaviorContext context) { return RANGE[index(context)]; }
    private static int speed(MachineBehaviorContext context) { return SPEED[index(context)]; }
    private static int hardness(MachineBehaviorContext context) { return HARDNESS[index(context)]; }
}
