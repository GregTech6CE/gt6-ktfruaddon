package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Research assembler. The source checks an adjacent research database and
 * refuses a row whose first output is locked. This block cannot read that
 * neighbor, so the same vanilla tree the monitor stores is the unlock list.
 */
public final class Gt6KtfruResearchAssembler implements MachineBehavior {
    private static final String MAP = "gt.recipe.assembler";

    @Override
    public void tick(MachineBehaviorContext context) {
        if (context.state().getString("tree").isEmpty()) {
            context.state().setString("tree", Gt6KtfruResearch.TREE);
        }
        if (!context.structureFormed()) {
            context.working(false);
            context.progress(0);
            return;
        }
        AddonRecipes.Line line = find(context);
        if (line == null || !unlocked(context, line)) {
            context.state().setLong("progress", 0);
            context.progress(0);
            context.working(false);
            return;
        }
        long duration = Math.max(1, line.duration());
        long progress = context.state().getLong("progress");
        long eu = Math.max(1, line.euPerTick());
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
        return slot < 2 && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return tank == 0 && stack != null && !stack.isEmpty();
    }

    private static AddonRecipes.Line find(MachineBehaviorContext context) {
        return AddonRecipes.map(MAP).find(
                List.of(context.item(0), context.item(1)),
                List.of(context.fluid(0)),
                true, 2, 1);
    }

    private static boolean unlocked(MachineBehaviorContext context, AddonRecipes.Line line) {
        ItemStack[] outputs = line.itemOutputs();
        if (outputs == null || outputs.length == 0 || outputs[0] == null || outputs[0].isEmpty()) return true;
        var id = BuiltInRegistries.ITEM.getKey(outputs[0].getItem());
        if (id == null) return false;
        return context.state().getString("tree").contains(id.toString());
    }

    private static boolean finish(MachineBehaviorContext context, AddonRecipes.Line line) {
        if (!pays(context, line) || !fits(context, line)) return false;
        ItemStack[] items = line.itemInputs();
        if (items != null) {
            for (int slot = 0; slot < items.length && slot < 2; slot++) {
                ItemStack need = items[slot];
                if (need == null || need.isEmpty() || kept(line, slot)) continue;
                ItemStack have = context.item(slot).copy();
                have.shrink(need.getCount());
                context.setItem(slot, have.isEmpty() ? ItemStack.EMPTY : have);
            }
        }
        FluidStack[] fluids = line.fluidInputs();
        if (fluids != null && fluids.length > 0 && fluids[0] != null && !fluids[0].isEmpty()) {
            context.drain(0, fluids[0].getAmount(), false);
        }
        ItemStack[] outputs = line.itemOutputs();
        if (outputs != null && outputs.length > 0 && outputs[0] != null && !outputs[0].isEmpty()) {
            ItemStack out = outputs[0].copy();
            ItemStack have = context.item(2);
            if (!have.isEmpty()) out.grow(have.getCount());
            context.setItem(2, out);
        }
        return true;
    }

    private static boolean kept(AddonRecipes.Line line, int slot) {
        int[] indexes = line.keptInputs();
        if (indexes == null) return false;
        for (int index : indexes) if (index == slot) return true;
        return false;
    }

    private static boolean pays(MachineBehaviorContext context, AddonRecipes.Line line) {
        ItemStack[] items = line.itemInputs();
        if (items != null) {
            for (int slot = 0; slot < items.length && slot < 2; slot++) {
                ItemStack need = items[slot];
                if (need == null || need.isEmpty()) continue;
                ItemStack have = context.item(slot);
                if (have.isEmpty() || !ItemStack.isSameItemSameComponents(have, need) || have.getCount() < need.getCount()) {
                    return false;
                }
            }
        }
        FluidStack[] fluids = line.fluidInputs();
        if (fluids != null && fluids.length > 0 && fluids[0] != null && !fluids[0].isEmpty()) {
            FluidStack here = context.fluid(0);
            if (here.isEmpty() || !FluidStack.isSameFluidSameComponents(here, fluids[0])
                    || here.getAmount() < fluids[0].getAmount()) return false;
        }
        return true;
    }

    private static boolean fits(MachineBehaviorContext context, AddonRecipes.Line line) {
        ItemStack[] outputs = line.itemOutputs();
        if (outputs == null || outputs.length == 0 || outputs[0] == null || outputs[0].isEmpty()) return true;
        ItemStack have = context.item(2);
        if (have.isEmpty()) return true;
        return ItemStack.isSameItemSameComponents(have, outputs[0])
                && have.getCount() + outputs[0].getCount() <= have.getMaxStackSize();
    }
}
