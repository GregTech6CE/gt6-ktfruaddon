package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/** Owns transient machine indexes. All access is on the server thread. */
@EventBusSubscriber(modid = Ktfru.MODID)
public final class BehaviorLifecycle {
    interface Memory {
        void clear();
        default void clearDimension(String dimension) {}
        default void clearPlayer(UUID player) {}
    }

    private static final List<Memory> MEMORIES = new ArrayList<>();
    private BehaviorLifecycle() {}

    static void register(Memory memory) { MEMORIES.add(memory); }

    public static int clearAll() {
        MEMORIES.forEach(Memory::clear);
        return MEMORIES.size();
    }

    public static void clearDimension(String dimension) {
        MEMORIES.forEach(memory -> memory.clearDimension(dimension));
    }

    public static void clearPlayer(UUID player) {
        MEMORIES.forEach(memory -> memory.clearPlayer(player));
    }

    @SubscribeEvent
    public static void stopped(ServerStoppedEvent event) {
        LogUtils.getLogger().info("Behavior lifecycle: cleared {} registries", clearAll());
    }

    @SubscribeEvent
    public static void unloaded(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level) {
            clearDimension(level.dimension().location().toString());
        }
    }

    @SubscribeEvent
    public static void loggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().level().isClientSide()) clearPlayer(event.getEntity().getUUID());
    }
}
