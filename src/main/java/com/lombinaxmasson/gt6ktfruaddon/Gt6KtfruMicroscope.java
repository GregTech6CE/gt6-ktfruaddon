package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/** Spends a fixed EU cost and consumes one item. Research tasks are not promoted. */
public final class Gt6KtfruMicroscope implements MachineBehavior {
    private final long perItem;

    public Gt6KtfruMicroscope(long perItem) {
        this.perItem = perItem;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        boolean formed = context.structureFormed();
        ItemStack sample = context.item(0);
        boolean running = formed && !sample.isEmpty() && context.consume(perItem);
        if (running) {
            sample.shrink(1);
            context.setItem(0, sample.isEmpty() ? ItemStack.EMPTY : sample);
        }
        context.working(running);
        context.progress(running ? 1.0F : 0.0F);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return slot == 0;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return false;
    }
}
