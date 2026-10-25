package com.lombinaxmasson.gt6ktfruaddon.network;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.world.item.ItemStack;

/** Compute cluster inventory; all slots accept items as in the source port. */
public final class ComputeCluster extends ComputeUser {
    public ComputeCluster() {
        super(0);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return true;
    }
}
