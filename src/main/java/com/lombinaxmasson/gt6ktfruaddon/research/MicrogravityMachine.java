package com.lombinaxmasson.gt6ktfruaddon.research;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.EnergyBridge;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.RecipeRunner;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.Slots;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/** Crystal growth and microgravity assembler recipe behavior; requires a live microgravity room. */
public final class MicrogravityMachine extends BehaviorBase {
    static final Slots ASSEMBLER_SLOTS = new Slots(List.of(0), Slots.range(1, 3), List.of(), List.of());
    static final Slots CRYSTAL_SLOTS = new Slots(List.of(0), List.of(1), Slots.range(0, 2), List.of());
    private final RecipeRunner runner;
    private final boolean multiblock;
    private final int fluidInputs;

    public MicrogravityMachine(String recipeMap, boolean multiblock, int fluidInputs) {
        this.runner = new RecipeRunner(fluidInputs == 0 ? ASSEMBLER_SLOTS : CRYSTAL_SLOTS, List.of(recipeMap));
        this.multiblock = multiblock;
        this.fluidInputs = fluidInputs;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        EnergyBridge.pull(context, "EU");
        if ((multiblock && !context.structureFormed()) || !MicrogravityRooms.hasMicrogravity(context.level(), context.pos())) {
            runner.idle(context);
            return;
        }
        runner.tick(context);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return slot == 0 && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return tank >= 0 && tank < fluidInputs && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        boolean microgravity = MicrogravityRooms.hasMicrogravity(context.level(), context.pos());
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.microgravity.status",
                microgravity ? 1 : 0, (!multiblock || context.structureFormed()) ? 1 : 0), false);
        return true;
    }

}
