package com.lombinaxmasson.gt6ktfruaddon.heat;

import com.gregtech.gregtech.api.addon.GregTechMaterials;
import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Electromagnetic crucible heat. A known iron charge can melt.
 * Arbitrary item contents stay unread.
 */
public final class Gt6KtfruCrucible implements MachineBehavior {
    /** {@code CrucibleSpec.KG_PER_ENERGY} in this GT6CE checkout. */
    static final long KG_PER_ENERGY = 100;
    static final long MASS = 3200;
    static final long BASE = 100;
    static final long AMBIENT = 293_000;
    static final long MAX = 32_768_000;
    static final long COOL = 200;

    @Override
    public void tick(MachineBehaviorContext context) {
        var state = context.state();
        long temp = state.getLong("temp");
        long old = state.getBoolean("seen") ? state.getLong("old") : temp;
        long energy = context.energy();
        boolean stopped = context.toggle(0);
        if (!stopped && context.structureFormed() && energy > BASE && !state.getBoolean("iron") && !state.getBoolean("spent")) {
            state.setBoolean("iron", true);
        }
        boolean iron = state.getBoolean("iron");
        if (!stopped && energy < BASE && iron) {
            state.setBoolean("iron", false);
            state.setBoolean("molten", false);
            state.setBoolean("spent", true);
            temp = AMBIENT;
        } else if (!stopped && energy > BASE && temp <= MAX) {
            double added = (energy - BASE) / (1 + MASS * 1.66 / KG_PER_ENERGY);
            temp += Math.round(added * 1000);
        }
        if (energy > 0) context.consume(energy);
        int melting = melting();
        if (iron && melting < temp / 1000 && melting > old / 1000) state.setBoolean("molten", true);
        if (iron && melting > temp / 1000 && melting < old / 1000) state.setBoolean("molten", false);
        state.setLong("old", temp);
        state.setBoolean("seen", true);
        temp = Math.max(AMBIENT, temp - COOL);
        state.setLong("temp", temp);
        context.working(iron && state.getBoolean("molten"));
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return false;
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.crucible.temperature",
                context.state().getLong("temp") / 1000.0,
                Component.translatable(context.state().getBoolean("molten")
                        ? "gt6ktfruaddon.crucible.molten" : "gt6ktfruaddon.crucible.solid")), false);
        return true;
    }

    private static int melting() {
        var iron = GregTechMaterials.find("Iron");
        return iron.valid() ? iron.meltingPoint() : Integer.MAX_VALUE;
    }
}
