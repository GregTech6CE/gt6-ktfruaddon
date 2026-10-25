package com.lombinaxmasson.gt6ktfruaddon.research;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.EnergyBridge;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/** Crystal growth and microgravity assembler recipe behavior; requires a live microgravity room. */
public final class MicrogravityMachine extends BehaviorBase {
    private final String recipeMap;
    private final boolean multiblock;
    private final int fluidInputs;

    public MicrogravityMachine(String recipeMap, boolean multiblock, int fluidInputs) {
        this.recipeMap = recipeMap;
        this.multiblock = multiblock;
        this.fluidInputs = fluidInputs;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        EnergyBridge.pull(context, "EU");
        if ((multiblock && !context.structureFormed()) || !MicrogravityRooms.hasMicrogravity(context.level(), context.pos())) {
            context.working(false);
            context.progress(0);
            return;
        }
        AddonRecipes.Line line = find(context);
        if (line == null) {
            context.state().setLong("progress", 0);
            context.progress(0);
            context.working(false);
            return;
        }
        long duration = Math.max(1L, line.duration());
        long progress = context.state().getLong("progress");
        long eu = Math.max(1L, line.euPerTick());
        if (progress >= duration) {
            if (finish(context, line)) progress = 0;
        } else if (context.energy() >= eu && context.consume(eu)) {
            progress++;
            if (progress >= duration && finish(context, line)) progress = 0;
            context.working(true);
        } else {
            context.working(false);
        }
        context.state().setLong("progress", progress);
        context.progress(Math.min(1.0F, progress / (float) duration));
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

    private AddonRecipes.Line find(MachineBehaviorContext context) {
        List<FluidStack> fluids = fluidInputs == 0
                ? List.of()
                : List.of(context.fluid(0), context.fluid(1));
        return AddonRecipes.map(recipeMap).find(
                List.of(context.item(0)),
                fluids,
                true, 1, fluidInputs);
    }

    private boolean finish(MachineBehaviorContext context, AddonRecipes.Line line) {
        if (!pays(context, line) || !fits(context, line)) return false;
        ItemStack[] inItems = line.itemInputs();
        if (inItems != null && inItems.length > 0 && inItems[0] != null && !inItems[0].isEmpty() && !kept(line, 0)) {
            ItemStack have = context.item(0).copy();
            have.shrink(inItems[0].getCount());
            context.setItem(0, have.isEmpty() ? ItemStack.EMPTY : have);
        }
        FluidStack[] inFluids = line.fluidInputs();
        if (inFluids != null) {
            for (FluidStack need : inFluids) {
                if (need == null || need.isEmpty()) continue;
                int tank = tankHolding(context, need);
                if (tank >= 0) context.drain(tank, need.getAmount(), false);
            }
        }
        ItemStack[] outItems = line.itemOutputs();
        if (outItems != null) {
            for (ItemStack out : outItems) {
                if (out != null && !out.isEmpty()) context.insertOutput(out.copy());
            }
        }
        return true;
    }

    private boolean pays(MachineBehaviorContext context, AddonRecipes.Line line) {
        ItemStack[] inItems = line.itemInputs();
        if (inItems != null && inItems.length > 0 && inItems[0] != null && !inItems[0].isEmpty()) {
            ItemStack have = context.item(0);
            if (have.isEmpty() || !ItemStack.isSameItemSameComponents(have, inItems[0])
                    || have.getCount() < inItems[0].getCount()) {
                return false;
            }
        }
        FluidStack[] inFluids = line.fluidInputs();
        if (inFluids != null) {
            for (FluidStack need : inFluids) {
                if (need == null || need.isEmpty()) continue;
                if (tankHolding(context, need) < 0) return false;
            }
        }
        return true;
    }

    private boolean fits(MachineBehaviorContext context, AddonRecipes.Line line) {
        ItemStack[] outItems = line.itemOutputs();
        if (outItems == null) return true;
        int outSlots = context.itemSlots() - 1;
        int neededEmpty = 0;
        for (ItemStack out : outItems) {
            if (out == null || out.isEmpty()) continue;
            boolean merged = false;
            for (int s = 1; s <= outSlots; s++) {
                ItemStack have = context.item(s);
                if (!have.isEmpty() && ItemStack.isSameItemSameComponents(have, out)
                        && have.getCount() + out.getCount() <= have.getMaxStackSize()) {
                    merged = true;
                    break;
                }
            }
            if (!merged) neededEmpty++;
        }
        int empty = 0;
        for (int s = 1; s <= outSlots; s++) {
            if (context.item(s).isEmpty()) empty++;
        }
        return empty >= neededEmpty;
    }

    private int tankHolding(MachineBehaviorContext context, FluidStack need) {
        for (int t = 0; t < fluidInputs; t++) {
            FluidStack have = context.fluid(t);
            if (!have.isEmpty() && FluidStack.isSameFluidSameComponents(have, need)
                    && have.getAmount() >= need.getAmount()) {
                return t;
            }
        }
        return -1;
    }

    private static boolean kept(AddonRecipes.Line line, int index) {
        int[] kept = line.keptInputs();
        if (kept == null) return false;
        for (int k : kept) if (k == index) return true;
        return false;
    }
}
