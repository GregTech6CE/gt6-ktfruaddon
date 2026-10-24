package com.lombinaxmasson.gt6ktfruaddon.power;

import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * RU to EU. Efficiency is {@code outputMax * 10000 / inputMax}, the same fallback
 * {@code MultiTransformerBase} uses when the registration has no efficiency key.
 * A separate-excite dynamo spends {@code floor(inputMin * 0.02)} EU before that conversion.
 * Waste is off, so RU left in the buffer stays.
 */
public final class Dynamo implements MachineBehavior {
    private final long outputMax;
    private final long efficiency;
    private final int ampere;
    private final long excite;

    Dynamo(long inputMax, long outputMax, int ampere, long excite) {
        this.outputMax = outputMax;
        this.efficiency = outputMax * 10_000L / inputMax;
        this.ampere = ampere;
        this.excite = excite;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (!context.structureFormed() || !excited(context)) {
            context.working(false);
            return;
        }
        long stored = context.energy();
        long usable = stored * efficiency / 10_000L;
        if (usable < 1) {
            context.working(false);
            return;
        }
        long packets = Math.min(ampere, (usable + outputMax - 1) / outputMax);
        long voltage = Math.min(outputMax, usable / packets);
        if (voltage < 1) {
            context.working(false);
            return;
        }
        long sent = context.emit(voltage, packets);
        if (sent < 1) {
            context.working(false);
            return;
        }
        long spent = voltage * sent * 10_000L / efficiency;
        long extra = spent - voltage * sent;
        if (extra > 0) context.consume(Math.min(extra, context.energy()));
        context.working(true);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return false;
    }

    private boolean excited(MachineBehaviorContext context) {
        if (excite < 1) return true;
        long have = context.energy("EU");
        if (have < excite) {
            if (have > 0) context.consume("EU", have);
            return false;
        }
        return context.consume("EU", excite);
    }
}
