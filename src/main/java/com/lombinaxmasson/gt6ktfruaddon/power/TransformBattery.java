package com.lombinaxmasson.gt6ktfruaddon.power;

import java.util.Map;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.Gt6ce;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.EnergyBridge;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.LossTimer;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

/**
 * Transform battery. EU capacity is the filled cells in the eight slots.
 * RU capacity stays 0: a flywheel's maximum needs mass, tool quality and
 * tool speed, and the material catalog has none of those. LU is the fixed
 * crystal capacity and is always sealed. A sealed machine does not take
 * items. Nothing here can push that buffer into a card 10 transformer part.
 */
public final class TransformBattery extends BehaviorBase {
    static final long LIGHT_CAPACITY = 367_001_600_000L;
    private static final Map<String, Long> CELLS = Map.of(
            "lead_acid_cell_filled", 512_000L,
            "alkaline_button_cell_filled", 2_048_000L,
            "nickel_cadmium_cell_filled", 2_048_000L,
            "lithium_cobalt_cell_filled", 8_192_000L,
            "lithium_manganese_cell_filled", 32_768_000L);

    private final String energy;
    private final double lossPercent;
    private final boolean alwaysSealed;

    public TransformBattery(String energy, double lossPercent, boolean alwaysSealed) {
        this.energy = energy;
        this.lossPercent = lossPercent;
        this.alwaysSealed = alwaysSealed;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        long computed = capacity(context);
        long stored = context.energy();
        context.setCapacity(computed > 0 ? computed : Math.max(stored, 1));
        EnergyBridge.pull(context, energy);
        stored = context.energy();
        if (stored > 0) LossTimer.lose(context, lossPercent);
        long cap = context.capacity();
        context.progress(cap <= 0 ? 0 : Math.min(1.0F, context.energy() / (float) cap));
        context.working(context.structureFormed() && context.energy() > 0);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        if (alwaysSealed) return false;
        return !context.toggle(0);
    }

    private long capacity(MachineBehaviorContext context) {
        if (alwaysSealed) return LIGHT_CAPACITY;
        long cap = 0;
        for (int slot = 0; slot < context.itemSlots(); slot++) {
            ItemStack stack = context.item(slot);
            if (stack.isEmpty()) continue;
            if ("EU".equals(energy)) cap += cell(stack) * stack.getCount();
            if ("RU".equals(energy) && flywheel(stack)) cap += flywheelStorage() * stack.getCount();
        }
        return cap;
    }

    private static long cell(ItemStack stack) {
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (!Gt6ce.owns(id)) return 0;
        Long each = CELLS.get(id.getPath());
        return each == null ? 0 : each;
    }

    private static boolean flywheel(ItemStack stack) {
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id != null && id.getPath().contains("flywheel");
    }

    /** The catalog cannot answer mass, tool quality or tool speed. */
    private static long flywheelStorage() {
        return 0;
    }
}
