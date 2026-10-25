package com.lombinaxmasson.gt6ktfruaddon.research;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PosCodec;
import com.lombinaxmasson.gt6ktfruaddon.network.ComputeNetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/** Allocates Normal compute and adds research points to its bound or adjacent monitor. */
public final class AiCenter extends BehaviorBase {
    public static final long AI_COMPUTE_PER_TICK = 67_108_864L;
    public static final String STATUS_IDLE = "idle";
    public static final String STATUS_UNBOUND = "unbound";
    public static final String STATUS_OFFLINE = "offline";
    public static final String STATUS_NO_MONITOR = "no_monitor";
    public static final String STATUS_INSUFFICIENT = "insufficient";
    public static final String STATUS_RUNNING = "running";

    private final long points;

    public AiCenter(long points) {
        this.points = points;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        tickAi(context);
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        String dim = context.level().dimension().location().toString();
        DimPos pendingController = ComputeNetwork.takePendingController(player.getUUID(), dim);
        if (pendingController != null) {
            context.state().setString("controller", PosCodec.format(pendingController.pos()));
        }
        DimPos pendingMonitor = ResearchNetwork.takePendingMonitor(player.getUUID(), dim);
        if (pendingMonitor != null) {
            context.state().setString("monitor", PosCodec.format(pendingMonitor.pos()));
        }
        String status = context.state().getString("status");
        if (status.isEmpty()) status = STATUS_IDLE;
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.ai.points",
                points, context.structureFormed() ? 1 : 0, status, AI_COMPUTE_PER_TICK), false);
        return true;
    }

    private void tickAi(MachineBehaviorContext context) {
        if (!context.structureFormed()) {
            context.state().setString("status", STATUS_OFFLINE);
            context.working(false);
            return;
        }
        BlockPos monitor = resolveMonitor(context);
        if (monitor == null || !ResearchNetwork.isMonitorOnline(context.level(), monitor)) {
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
        if (!ComputeNetwork.isControllerOnline(context.level(), controller)) {
            context.state().setString("status", STATUS_OFFLINE);
            context.working(false);
            return;
        }
        if (!ComputeNetwork.allocate(context.level(), controller, "Normal", AI_COMPUTE_PER_TICK)) {
            context.state().setString("status", STATUS_INSUFFICIENT);
            context.working(false);
            return;
        }
        ResearchNetwork.addPoints(context.level(), monitor, points);
        context.state().setString("status", STATUS_RUNNING);
        context.working(true);
    }

    private static BlockPos resolveMonitor(MachineBehaviorContext context) {
        BlockPos saved = PosCodec.parse(context.state().getString("monitor"));
        if (saved != null && ResearchNetwork.isMonitorOnline(context.level(), saved)) {
            return saved;
        }
        BlockPos adjacent = ResearchNetwork.findAdjacentMonitor(context.level(), context.pos());
        if (adjacent != null) {
            context.state().setString("monitor", PosCodec.format(adjacent));
            return adjacent;
        }
        return saved;
    }
}
