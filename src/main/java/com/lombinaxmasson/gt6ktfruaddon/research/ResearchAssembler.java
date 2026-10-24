package com.lombinaxmasson.gt6ktfruaddon.research;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PosCodec;

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
public final class ResearchAssembler implements MachineBehavior {
    static final int ITEM_IN_COUNT = 6;
    static final int ITEM_OUT_START = 6;
    static final int ITEM_OUT_COUNT = 2;
    static final int FLUID_IN_COUNT = 2;
    static final int FLUID_OUT_TANK = 2;

    private final List<String> maps;

    public ResearchAssembler(String... maps) {
        this.maps = List.of(maps);
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        long aux = context.energy("EU");
        if (aux > 0) {
            long move = Math.min(aux, Math.max(0L, context.capacity() - context.energy()));
            if (move > 0 && context.consume("EU", move)) context.generate(move);
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
        DimPos pending = Research.takePendingMonitor(player.getUUID(), dim);
        if (pending != null) {
            context.state().setString("monitor", PosCodec.format(pending.pos()));
        }
        BlockPos db = databasePos(context);
        boolean online = Research.isMonitorOnline(context.level(), db);
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.research_assembler.status",
                context.structureFormed() ? 1 : 0,
                PosCodec.format(db),
                online ? 1 : 0), false);
        return true;
    }

    private AddonRecipes.Line find(MachineBehaviorContext context) {
        List<ItemStack> items = new ArrayList<>(ITEM_IN_COUNT);
        for (int i = 0; i < ITEM_IN_COUNT; i++) items.add(context.item(i));
        List<FluidStack> fluids = List.of(context.fluid(0), context.fluid(1));
        for (String map : maps) {
            AddonRecipes.Line found = AddonRecipes.map(map).find(items, fluids, true, ITEM_IN_COUNT, FLUID_IN_COUNT);
            if (found != null) return found;
        }
        return null;
    }

    private static boolean unlocked(MachineBehaviorContext context, AddonRecipes.Line line) {
        ItemStack[] outputs = line.itemOutputs();
        if (outputs == null || outputs.length == 0 || outputs[0] == null || outputs[0].isEmpty()) return true;
        var id = BuiltInRegistries.ITEM.getKey(outputs[0].getItem());
        if (id == null) return false;
        return Research.isUnlocked(context.level(), databasePos(context), id.toString());
    }

    private static BlockPos databasePos(MachineBehaviorContext context) {
        BlockPos behindTwo = context.pos().relative(context.facing().getOpposite(), 2);
        if (Research.isMonitorOnline(context.level(), behindTwo)) {
            return behindTwo;
        }
        BlockPos saved = PosCodec.parse(context.state().getString("monitor"));
        return saved != null ? saved : behindTwo;
    }

    private static boolean finish(MachineBehaviorContext context, AddonRecipes.Line line) {
        if (!pays(context, line) || !fits(context, line)) return false;
        ItemStack[] items = line.itemInputs();
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                ItemStack need = items[i];
                if (need == null || need.isEmpty() || kept(line, i)) continue;
                int slot = itemSlotHolding(context, need);
                if (slot >= 0) {
                    ItemStack have = context.item(slot).copy();
                    have.shrink(need.getCount());
                    context.setItem(slot, have.isEmpty() ? ItemStack.EMPTY : have);
                }
            }
        }
        FluidStack[] fluids = line.fluidInputs();
        if (fluids != null) {
            for (FluidStack need : fluids) {
                if (need == null || need.isEmpty()) continue;
                int tank = fluidTankHolding(context, need);
                if (tank >= 0) context.drain(tank, need.getAmount(), false);
            }
        }
        FluidStack[] outFluids = line.fluidOutputs();
        if (outFluids != null && outFluids.length > 0 && outFluids[0] != null && !outFluids[0].isEmpty()) {
            context.fill(FLUID_OUT_TANK, outFluids[0].copy(), false);
        }
        ItemStack[] outputs = line.itemOutputs();
        if (outputs != null) {
            for (ItemStack out : outputs) {
                if (out != null && !out.isEmpty()) context.insertOutput(out.copy());
            }
        }
        return true;
    }

    private static boolean kept(AddonRecipes.Line line, int index) {
        int[] indexes = line.keptInputs();
        if (indexes == null) return false;
        for (int k : indexes) if (k == index) return true;
        return false;
    }

    private static boolean pays(MachineBehaviorContext context, AddonRecipes.Line line) {
        ItemStack[] items = line.itemInputs();
        if (items != null) {
            for (ItemStack need : items) {
                if (need == null || need.isEmpty()) continue;
                if (itemSlotHolding(context, need) < 0) return false;
            }
        }
        FluidStack[] fluids = line.fluidInputs();
        if (fluids != null) {
            for (FluidStack need : fluids) {
                if (need == null || need.isEmpty()) continue;
                if (fluidTankHolding(context, need) < 0) return false;
            }
        }
        return true;
    }

    private static boolean fits(MachineBehaviorContext context, AddonRecipes.Line line) {
        FluidStack[] outFluids = line.fluidOutputs();
        if (outFluids != null && outFluids.length > 0 && outFluids[0] != null && !outFluids[0].isEmpty()) {
            FluidStack have = context.fluid(FLUID_OUT_TANK);
            if (!have.isEmpty() && (!FluidStack.isSameFluidSameComponents(have, outFluids[0])
                    || have.getAmount() + outFluids[0].getAmount() > context.capacity(FLUID_OUT_TANK))) {
                return false;
            }
        }
        ItemStack[] outputs = line.itemOutputs();
        if (outputs == null || outputs.length == 0 || outputs[0] == null || outputs[0].isEmpty()) return true;
        for (int s = ITEM_OUT_START; s < ITEM_OUT_START + ITEM_OUT_COUNT; s++) {
            ItemStack have = context.item(s);
            if (have.isEmpty()) return true;
            if (ItemStack.isSameItemSameComponents(have, outputs[0])
                    && have.getCount() + outputs[0].getCount() <= have.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    private static int itemSlotHolding(MachineBehaviorContext context, ItemStack need) {
        for (int s = 0; s < ITEM_IN_COUNT; s++) {
            ItemStack have = context.item(s);
            if (!have.isEmpty() && ItemStack.isSameItemSameComponents(have, need)
                    && have.getCount() >= need.getCount()) {
                return s;
            }
        }
        return -1;
    }

    private static int fluidTankHolding(MachineBehaviorContext context, FluidStack need) {
        for (int t = 0; t < FLUID_IN_COUNT; t++) {
            FluidStack have = context.fluid(t);
            if (!have.isEmpty() && FluidStack.isSameFluidSameComponents(have, need)
                    && have.getAmount() >= need.getAmount()) {
                return t;
            }
        }
        return -1;
    }
}
