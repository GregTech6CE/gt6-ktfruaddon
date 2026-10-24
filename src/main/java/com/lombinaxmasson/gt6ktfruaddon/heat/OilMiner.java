package com.lombinaxmasson.gt6ktfruaddon.heat;

import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * Oil miner. Water plus an oil spring becomes aqueous oil. Air plus a gas spring
 * copies that gas. Other liquids are copied. The six drills are the structure's
 * {@code A} cells; the spring is the block under each drill.
 */
public final class OilMiner implements MachineBehavior {
    private static final int IN = 0;
    private static final int OUT = 1;
    private static final int[][] DRILLS = {
            {1, 1}, {1, 2}, {0, 1}, {0, 2}, {-1, 1}, {-1, 2}
    };

    @Override
    public void tick(MachineBehaviorContext context) {
        if (!context.structureFormed() || context.energy() <= 16) {
            context.working(false);
            return;
        }
        if (!context.fluid(IN).isEmpty()) pump(context);
        pushUp(context);
        context.consume(context.energy());
        context.working(true);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return tank == IN && stack != null && !stack.isEmpty();
    }

    private static void pump(MachineBehaviorContext context) {
        int budget = (int) Math.min(Integer.MAX_VALUE, context.energy() / 16);
        if (budget < 1) return;
        for (int[] drill : DRILLS) {
            FluidStack input = context.fluid(IN);
            if (input.isEmpty() || budget < 1) return;
            BlockPos under = at(context, drill[0], -1, drill[1]);
            FluidStack produced = produced(input, springPath(context, under));
            if (produced.isEmpty()) continue;
            int amount = Math.min(budget, input.getAmount());
            int room = context.fill(OUT, produced.copyWithAmount(amount), true);
            if (room < 1) continue;
            FluidStack drained = context.drain(IN, room, false);
            if (drained.isEmpty()) continue;
            context.fill(OUT, produced.copyWithAmount(drained.getAmount()), false);
        }
    }

    private static void pushUp(MachineBehaviorContext context) {
        IFluidHandler handler = context.level().getCapability(
                Capabilities.FluidHandler.BLOCK, context.pos().above(2), Direction.DOWN);
        if (handler == null) return;
        FluidStack fluid = context.fluid(OUT);
        if (fluid.isEmpty()) return;
        int moved = handler.fill(fluid.copy(), IFluidHandler.FluidAction.EXECUTE);
        if (moved > 0) context.drain(OUT, moved, false);
    }

    private static String springPath(MachineBehaviorContext context, BlockPos pos) {
        if (!context.level().isLoaded(pos)) return "";
        BlockState state = context.level().getBlockState(pos);
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (id == null || !"gt6ce".equals(id.getNamespace())) return "";
        String path = id.getPath();
        return path.startsWith("fluid_spring_") ? path : "";
    }

    private static FluidStack produced(FluidStack input, String springPath) {
        if (springPath.isEmpty()) return FluidStack.EMPTY;
        String fluidPath = springPath.substring("fluid_spring_".length());
        boolean gas = fluidPath.startsWith("gas_");
        boolean lava = "lava".equals(fluidPath);
        if (same(input, "Water")) {
            if (lava || gas) return FluidStack.EMPTY;
            String aqueous = switch (fluidPath) {
                case "liquid_extra_heavy_oil" -> "GenLiquid_AqueousOilExtraHeavy";
                case "liquid_heavy_oil" -> "GenLiquid_AqueousOilHeavy";
                case "liquid_medium_oil" -> "GenLiquid_AqueousOilMedium";
                case "liquid_light_oil" -> "GenLiquid_AqueousOilLight";
                default -> "";
            };
            if (!aqueous.isEmpty()) {
                FluidStack stack = AddonFluids.still(aqueous, 1);
                return stack == null ? FluidStack.EMPTY : stack;
            }
            return fluid(fluidPath);
        }
        if (same(input, "Air")) return gas ? fluid(fluidPath) : FluidStack.EMPTY;
        return FluidStack.EMPTY;
    }

    private static FluidStack fluid(String path) {
        ResourceLocation id = "lava".equals(path)
                ? ResourceLocation.parse("minecraft:lava")
                : ResourceLocation.fromNamespaceAndPath("gt6ce", path);
        var fluid = BuiltInRegistries.FLUID.get(id);
        if (fluid == null || fluid == net.minecraft.world.level.material.Fluids.EMPTY) return FluidStack.EMPTY;
        return new FluidStack(fluid, 1);
    }

    private static boolean same(FluidStack stack, String field) {
        FluidStack want = AddonFluids.still(field, 1);
        return want != null && !want.isEmpty() && FluidStack.isSameFluidSameComponents(stack, want);
    }

    private static BlockPos at(MachineBehaviorContext context, int right, int up, int back) {
        Direction facing = context.facing();
        return context.pos().relative(facing.getClockWise(), right).above(up).relative(facing.getOpposite(), back);
    }
}
