package com.lombinaxmasson.gt6ktfruaddon.power;

import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Source {@code TransformerPart}. One energy type, one packet size, one ampere.
 * The buffer holds ampere packets. While the stop toggle is off, the front
 * emits as many of those packets as the buffer can pay.
 */
public final class Gt6KtfruTransformerPart implements MachineBehavior {
    private static final long[] PACKET = {32, 128, 512, 2048, 8192, 32768};

    private final int ampere;

    public Gt6KtfruTransformerPart(int ampere) {
        this.ampere = ampere;
    }

    public static long packet(int tier) {
        if (tier < 1 || tier > PACKET.length) return PACKET[0];
        return PACKET[tier - 1];
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (context.toggle(0)) {
            context.working(false);
            return;
        }
        long size = packet(context.tier());
        long stored = context.energy();
        if (size >= 1 && stored >= size) {
            long packets = Math.min(ampere, stored / size);
            if (packets >= 1) context.emit(size, packets);
        }
        context.working(context.energy() > 0);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return false;
    }
}
