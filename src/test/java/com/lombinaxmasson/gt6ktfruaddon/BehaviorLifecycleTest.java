package com.lombinaxmasson.gt6ktfruaddon;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.*;

class BehaviorLifecycleTest {
    private static DimPos pos(String dim) { return new DimPos(dim, new BlockPos(1, 2, 3)); }
    @Test void ttlIncludesBoundaryAndRemovesStaleEntries() {
        var registry = new TtlRegistry<String>(); var key = pos("a");
        registry.put(key, "value", 100);
        assertEquals("value", registry.get(key, 120));
        assertNull(registry.get(key, 121));
        assertNull(registry.get(key, 100));
    }
    @Test void clocksAreIndependentAndTimeReversalExpires() {
        var registry = new TtlRegistry<String>();
        registry.put(pos("a"), "a", 1000); registry.put(pos("b"), "b", 2);
        assertEquals("a", registry.get(pos("a"), 1001));
        assertEquals("b", registry.get(pos("b"), 3));
        assertNull(registry.get(pos("a"), 999));
    }
    @Test void dimensionUnloadClearsLiveMessagesAndSelectionsOnlyInThatDimension() {
        var live = new TtlRegistry<String>(); var messages = new DimensionMemory<String>();
        var players = new PlayerMemory<DimPos>(DimPos::dim); var a = UUID.randomUUID(); var b = UUID.randomUUID();
        for (String dim : new String[]{"a","b"}) { live.put(pos(dim),dim,1); messages.put(pos(dim),dim); }
        players.put(a,pos("a")); players.put(b,pos("b"));
        BehaviorLifecycle.clearDimension("a");
        assertNull(live.get(pos("a"),1)); assertNull(messages.get(pos("a"))); assertNull(players.get(a));
        assertEquals("b",live.get(pos("b"),1)); assertEquals("b",messages.get(pos("b"))); assertNotNull(players.get(b));
    }
    @Test void logoutClearsOnlyThatPlayersSelection() {
        var players = new PlayerMemory<DimPos>(DimPos::dim); var a = UUID.randomUUID(); var b = UUID.randomUUID();
        players.put(a,pos("a")); players.put(b,pos("b")); BehaviorLifecycle.clearPlayer(a);
        assertNull(players.get(a)); assertEquals(pos("b"),players.get(b));
    }
    @Test void serverStopClearsAllKindsOfMemory() {
        var live = new TtlRegistry<String>(); var messages = new DimensionMemory<String>();
        var players = new PlayerMemory<DimPos>(DimPos::dim); var a = UUID.randomUUID();
        live.put(pos("a"),"a",1); messages.put(pos("a"),"a"); players.put(a,pos("a"));
        assertTrue(BehaviorLifecycle.clearAll() >= 3);
        assertNull(live.get(pos("a"),1)); assertNull(messages.get(pos("a"))); assertNull(players.get(a));
    }
    @Test void refreshingSweepsExpiredUnloadedPositions() {
        var live = new TtlRegistry<String>(); var other = new DimPos("a",new BlockPos(4,5,6));
        live.put(pos("a"),"old",1); live.put(other,"new",100);
        assertEquals(java.util.List.of(other),live.keys("a",100));
    }
    @Test void serverStopListenerRunsThroughTheEventBus() {
        var messages=new DimensionMemory<String>();messages.put(pos("a"),"old world");
        var bus=net.neoforged.bus.api.BusBuilder.builder().build();
        bus.register(BehaviorLifecycle.class);
        bus.post(new net.neoforged.neoforge.event.server.ServerStoppedEvent(null));
        assertNull(messages.get(pos("a")));
    }
    @Test void mutablePositionsCannotChangeARegistryKey() {
        var mutable=new BlockPos.MutableBlockPos(1,2,3);
        var key=new DimPos("a",mutable);var registry=new TtlRegistry<String>();registry.put(key,"value",1);
        mutable.set(9,9,9);
        assertEquals(new BlockPos(1,2,3),key.pos());
        assertEquals("value",registry.get(pos("a"),1));
    }
}
