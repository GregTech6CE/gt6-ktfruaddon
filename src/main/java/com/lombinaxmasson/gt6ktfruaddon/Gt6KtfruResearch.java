package com.lombinaxmasson.gt6ktfruaddon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Research blocks and the addon-side monitor index ({@code ResearchTreeMonitor}, 9818).
 * Microscopes unlock examined items into their bound or adjacent monitor.
 * AI centers add minigame progress points to their bound or adjacent monitor.
 * The research assembler and research reactor check the monitor two blocks behind the controller.
 */
public final class Gt6KtfruResearch implements MachineBehavior {
    static final String TREE = "minecraft:paper,minecraft:glass,minecraft:iron_ingot";
    private static final long EXPIRY_TICKS = 20L;
    private static final Direction[] HORIZONTAL = {
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    private static final Map<DimPos, MonitorEntry> MONITORS = new HashMap<>();
    private static final Map<UUID, DimPos> PENDING_MONITOR = new HashMap<>();

    private final boolean monitor;

    public Gt6KtfruResearch(boolean monitor) {
        this.monitor = monitor;
    }

    public static synchronized DimPos takePendingMonitor(UUID player, String dim) {
        DimPos key = PENDING_MONITOR.get(player);
        if (key != null && dim.equals(key.dim())) {
            PENDING_MONITOR.remove(player);
            return key;
        }
        return null;
    }

    public static synchronized BlockPos findAdjacentMonitor(Level level, BlockPos pos) {
        if (level == null || pos == null) return null;
        String dim = level.dimension().location().toString();
        long now = level.getGameTime();
        for (Direction side : HORIZONTAL) {
            BlockPos neighbor = pos.relative(side);
            MonitorEntry entry = MONITORS.get(new DimPos(dim, neighbor));
            if (entry != null && now - entry.lastTick <= EXPIRY_TICKS) {
                return neighbor;
            }
        }
        return null;
    }

    public static synchronized boolean isMonitorOnline(Level level, BlockPos monitorPos) {
        if (level == null || monitorPos == null) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos));
        return entry != null && level.getGameTime() - entry.lastTick <= EXPIRY_TICKS;
    }

    public static synchronized boolean isUnlocked(Level level, BlockPos monitorPos, String itemId) {
        if (level == null || monitorPos == null || itemId == null || itemId.isEmpty()) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos));
        if (entry == null || level.getGameTime() - entry.lastTick > EXPIRY_TICKS) return false;
        return entry.unlocked.contains(itemId);
    }

    public static synchronized boolean unlockItem(Level level, BlockPos monitorPos, String itemId) {
        if (level == null || monitorPos == null || itemId == null || itemId.isEmpty()) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos));
        if (entry == null || level.getGameTime() - entry.lastTick > EXPIRY_TICKS) return false;
        entry.unlocked.add(itemId);
        entry.dirty = true;
        return true;
    }

    public static synchronized boolean addPoints(Level level, BlockPos monitorPos, long points) {
        if (level == null || monitorPos == null || points <= 0) return false;
        String dim = level.dimension().location().toString();
        MonitorEntry entry = MONITORS.get(new DimPos(dim, monitorPos));
        if (entry == null || level.getGameTime() - entry.lastTick > EXPIRY_TICKS) return false;
        entry.points += points;
        entry.dirty = true;
        return true;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (monitor) {
            String raw = context.state().getString("tree");
            if (raw.isEmpty()) {
                raw = TREE;
                context.state().setString("tree", raw);
            }
            String dim = context.level().dimension().location().toString();
            DimPos key = new DimPos(dim, context.pos().immutable());
            long now = context.level().getGameTime();
            synchronized (Gt6KtfruResearch.class) {
                MonitorEntry entry = MONITORS.get(key);
                if (entry == null) {
                    entry = new MonitorEntry(parseSet(raw), context.state().getLong("points"), now);
                    MONITORS.put(key, entry);
                } else {
                    entry.unlocked.addAll(parseSet(raw));
                    entry.lastTick = now;
                    if (entry.dirty) {
                        context.state().setString("tree", String.join(",", entry.unlocked));
                        context.state().setLong("points", entry.points);
                        entry.dirty = false;
                    }
                }
            }
        }
        context.working(false);
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        if (!monitor) return;
        String dim = context.level().dimension().location().toString();
        synchronized (Gt6KtfruResearch.class) {
            MONITORS.remove(new DimPos(dim, context.pos().immutable()));
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
        if (!monitor || !AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        String dim = context.level().dimension().location().toString();
        BlockPos here = context.pos().immutable();
        synchronized (Gt6KtfruResearch.class) {
            PENDING_MONITOR.put(player.getUUID(), new DimPos(dim, here));
        }
        player.displayClientMessage(Component.translatable(
                "gt6ktfruaddon.research.tree", context.state().getString("tree")), false);
        return true;
    }

    private static Set<String> parseSet(String csv) {
        Set<String> set = new LinkedHashSet<>();
        if (csv == null || csv.isEmpty()) return set;
        Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(set::add);
        return set;
    }

    private static final class MonitorEntry {
        final Set<String> unlocked;
        long points;
        long lastTick;
        boolean dirty;

        MonitorEntry(Set<String> unlocked, long points, long lastTick) {
            this.unlocked = unlocked;
            this.points = points;
            this.lastTick = lastTick;
        }
    }
}
