package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PosCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Centrifugal microgravity controller (30102) and the three AI research centers (30106–30108).
 * After spinning for 600 ticks at 2048 EU/tick, the room registers its 9x9x9 interior in {@link Gt6KtfruRooms}.
 * Each AI center allocates 67,108,864 Normal compute/tick from its bound compute controller and adds
 * its points/tick (8, 4 or 2) to the bound or adjacent {@code research_monitor}.
 */
public final class Gt6KtfruLateBehavior implements MachineBehavior {
    public static final long AI_COMPUTE_PER_TICK = 67_108_864L;
    public static final String STATUS_IDLE = "idle";
    public static final String STATUS_UNBOUND = "unbound";
    public static final String STATUS_OFFLINE = "offline";
    public static final String STATUS_NO_MONITOR = "no_monitor";
    public static final String STATUS_INSUFFICIENT = "insufficient";
    public static final String STATUS_RUNNING = "running";

    private final boolean room;
    private final long points;

    public Gt6KtfruLateBehavior(boolean room, long points) {
        this.room = room;
        this.points = points;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (room) {
            tickRoom(context);
        } else {
            tickAi(context);
        }
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        if (room) {
            Gt6KtfruRooms.updateRoom(context.level(), context.pos(), false);
        }
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
        if (room) {
            int spin = context.state().getInt("spin");
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.microgravity_room.status",
                    context.structureFormed() ? 1 : 0, spin, spin >= 600 ? 1 : 0), false);
            return true;
        }
        String dim = context.level().dimension().location().toString();
        DimPos pendingController = Gt6KtfruCompute.takePendingController(player.getUUID(), dim);
        if (pendingController != null) {
            context.state().setString("controller", PosCodec.format(pendingController.pos()));
        }
        DimPos pendingMonitor = Gt6KtfruResearch.takePendingMonitor(player.getUUID(), dim);
        if (pendingMonitor != null) {
            context.state().setString("monitor", PosCodec.format(pendingMonitor.pos()));
        }
        String status = context.state().getString("status");
        if (status.isEmpty()) status = STATUS_IDLE;
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.ai.points",
                points, context.structureFormed() ? 1 : 0, status, AI_COMPUTE_PER_TICK), false);
        return true;
    }

    private static void tickRoom(MachineBehaviorContext context) {
        if (!context.structureFormed() || !context.consume(2048)) {
            context.state().setInt("spin", 0);
            context.working(false);
            context.progress(0);
            Gt6KtfruRooms.updateRoom(context.level(), context.pos(), false);
            return;
        }
        int spin = Math.min(600, context.state().getInt("spin") + 1);
        context.state().setInt("spin", spin);
        context.progress(spin / 600.0F);
        boolean active = spin >= 600;
        context.working(active);
        Gt6KtfruRooms.updateRoom(context.level(), context.pos(), active);
    }

    private void tickAi(MachineBehaviorContext context) {
        if (!context.structureFormed()) {
            context.state().setString("status", STATUS_OFFLINE);
            context.working(false);
            return;
        }
        BlockPos monitor = resolveMonitor(context);
        if (monitor == null || !Gt6KtfruResearch.isMonitorOnline(context.level(), monitor)) {
            context.state().setString("status", STATUS_NO_MONITOR);
            context.working(false);
            return;
        }
        BlockPos controller = PosCodec.parse(context.state().getString("controller"));
        if (controller == null) {
            context.state().setString("status", STATUS_UNBOUND);
            context.working(false);
            return;
        }
        if (!Gt6KtfruCompute.isControllerOnline(context.level(), controller)) {
            context.state().setString("status", STATUS_OFFLINE);
            context.working(false);
            return;
        }
        if (!Gt6KtfruCompute.allocate(context.level(), controller, "Normal", AI_COMPUTE_PER_TICK)) {
            context.state().setString("status", STATUS_INSUFFICIENT);
            context.working(false);
            return;
        }
        Gt6KtfruResearch.addPoints(context.level(), monitor, points);
        context.state().setString("status", STATUS_RUNNING);
        context.working(true);
    }

    private static BlockPos resolveMonitor(MachineBehaviorContext context) {
        BlockPos saved = PosCodec.parse(context.state().getString("monitor"));
        if (saved != null && Gt6KtfruResearch.isMonitorOnline(context.level(), saved)) {
            return saved;
        }
        BlockPos adjacent = Gt6KtfruResearch.findAdjacentMonitor(context.level(), context.pos());
        if (adjacent != null) {
            context.state().setString("monitor", PosCodec.format(adjacent));
            return adjacent;
        }
        return saved;
    }
}
