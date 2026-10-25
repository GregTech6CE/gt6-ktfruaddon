package com.lombinaxmasson.gt6ktfruaddon.kinetic;

import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

/**
 * Water mill. Torque is the sum of neighbor flow crossed with the facing.
 * Packets are positive and try the front, then the back.
 */
public final class WaterMill extends BehaviorBase {
    private static final int ROT_LIMIT = 9600;
    private static final int PACKET = 2;

    @Override
    public void tick(MachineBehaviorContext context) {
        var state = context.state();
        int rot = state.getInt("rot");
        long time = context.level().getGameTime();
        int milli = state.getInt("torque");
        if (time % 10 == 0) {
            milli = rot >= ROT_LIMIT ? 0 : (int) Math.round(torque(context) * 1000.0);
            state.setInt("torque", milli);
        }
        double torque = milli / 1000.0;
        if (rot >= ROT_LIMIT || Math.abs(torque) < 0.1) {
            context.working(false);
            return;
        }
        if (time % 20 == 0) state.setInt("rot", rot + context.level().getRandom().nextInt(2));
        long amount = Math.abs(torque) > 2 ? 2 : 1;
        long need = PACKET * amount;
        if (context.energy() < need) context.generate(need - context.energy());
        if (context.energy() >= need) context.emit(PACKET, amount);
        context.working(true);
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        int rot = context.state().getInt("rot");
        if (held.isEmpty()) {
            player.displayClientMessage(Component.translatable(rot >= ROT_LIMIT
                    ? "gt6ktfruaddon.water_mill.rotten"
                    : "gt6ktfruaddon.water_mill.torque",
                    context.state().getInt("torque") / 100), false);
            return true;
        }
        if (!bronzeRing(held)) return false;
        if (rot < ROT_LIMIT * 0.75) {
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.water_mill.early"), false);
            return true;
        }
        if (!player.getAbilities().instabuild) held.shrink(1);
        context.state().setInt("rot", 0);
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.water_mill.repaired"), false);
        return true;
    }

    /**
     * Skips the facing axis. A non-liquid occluding neighbor zeroes the whole sum.
     * Flow along the facing is dropped before the cross product.
     */
    private static double torque(MachineBehaviorContext context) {
        Direction facing = context.facing();
        Vec3 front = new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ());
        double sum = 0;
        for (Direction side : Direction.values()) {
            if (side.getAxis() == facing.getAxis()) continue;
            var at = context.pos().relative(side);
            BlockState block = context.level().getBlockState(at);
            FluidState fluid = block.getFluidState();
            if (fluid.isEmpty()) {
                if (block.canOcclude()) return 0;
                continue;
            }
            Vec3 flow = fluid.getFlow(context.level(), at);
            Vec3 flowPlane = new Vec3(
                    facing.getStepX() == 0 ? flow.x : 0,
                    facing.getStepY() == 0 ? flow.y : 0,
                    facing.getStepZ() == 0 ? flow.z : 0);
            if (flowPlane.cross(front).lengthSqr() == 0) continue;
            Vec3 origin = new Vec3(side.getStepX(), side.getStepY(), side.getStepZ());
            Vec3 crossed = origin.cross(flowPlane);
            sum += crossed.x + crossed.y + crossed.z;
        }
        return sum;
    }

    private static boolean bronzeRing(ItemStack stack) {
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id == null) return false;
        String path = id.getPath();
        return path.contains("ring") && path.contains("bronze");
    }
}
