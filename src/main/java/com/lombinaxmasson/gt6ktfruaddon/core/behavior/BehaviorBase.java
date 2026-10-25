package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * The base of every behavior of this addon. It takes no items and no fluids: most behaviors have no
 * inventory that a pipe or a player may fill, and the SDK leaves both checks abstract. A behavior that
 * has slots or tanks overrides the check for them.
 */
public abstract class BehaviorBase implements MachineBehavior {
    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return false;
    }
}
