package com.lombinaxmasson.gt6ktfruaddon.research;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.EnergyBridge;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PosCodec;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.RecipeRunner;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.Slots;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.StateChanges;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Research assembler (30042) and research reactor (30043).
 * Matches {@code MultiResearchRequiredBasicMachine}: checks the {@code IResearchDatabase}
 * two blocks behind the controller ({@code pos.relative(facing.getOpposite(), 2)}) or a bound
 * monitor, and only runs a recipe when its first item output is unlocked in that database.
 */
public final class ResearchAssembler extends BehaviorBase {
    static final int ITEM_IN_COUNT = 6;
    static final int ITEM_OUT_START = 6;
    static final int ITEM_OUT_COUNT = 2;
    static final int FLUID_IN_COUNT = 2;
    static final int FLUID_OUT_TANK = 2;

    static final Slots SLOTS = new Slots(Slots.range(0, ITEM_IN_COUNT), Slots.range(ITEM_OUT_START, ITEM_OUT_COUNT),
            Slots.range(0, FLUID_IN_COUNT), List.of(FLUID_OUT_TANK));
    private final RecipeRunner runner;

    public ResearchAssembler(String... maps) {
        this.runner = new RecipeRunner(SLOTS, List.of(maps)).requires(ResearchAssembler::unlocked);
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        EnergyBridge.pull(context, "EU");
        if (!context.structureFormed()) { runner.idle(context); return; }
        runner.tick(context);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return slot >= 0 && slot < ITEM_IN_COUNT && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return tank >= 0 && tank < FLUID_IN_COUNT && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        String dim = context.level().dimension().location().toString();
        DimPos pending = ResearchNetwork.takePendingMonitor(player.getUUID(), dim);
        if (pending != null) {
            StateChanges.setString(context.state(), "monitor", PosCodec.format(pending.pos()));
        }
        BlockPos db = databasePos(context);
        boolean online = ResearchNetwork.isMonitorOnline(context.level(), db);
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.research_assembler.status",
                context.structureFormed() ? 1 : 0,
                PosCodec.format(db),
                online ? 1 : 0), false);
        return true;
    }



    private static boolean unlocked(MachineBehaviorContext context, AddonRecipes.Line line) {
        ItemStack[] outputs = line.itemOutputs();
        if (outputs == null || outputs.length == 0 || outputs[0] == null || outputs[0].isEmpty()) return true;
        var id = BuiltInRegistries.ITEM.getKey(outputs[0].getItem());
        if (id == null) return false;
        return ResearchNetwork.isUnlocked(context.level(), databasePos(context), id.toString());
    }

    private static BlockPos databasePos(MachineBehaviorContext context) {
        BlockPos behindTwo = context.pos().relative(context.facing().getOpposite(), 2);
        if (ResearchNetwork.isMonitorOnline(context.level(), behindTwo)) {
            return behindTwo;
        }
        BlockPos saved = PosCodec.parse(context.state().getString("monitor"));
        return saved != null ? saved : behindTwo;
    }

}
