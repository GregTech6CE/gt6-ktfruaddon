package com.lombinaxmasson.gt6ktfruaddon.network;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.minecraft.core.BlockPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.*;
class WirelessInboxTest {
    final DimPos key = new DimPos("minecraft:overworld",new BlockPos(1,2,3));
    @BeforeEach void clear() { BehaviorLifecycle.clearAll(); }
    @Test void twoTowersCannotSpendTheSameRoom() {
        WirelessInbox.setRoom(key,100,1);long[] paid={0};
        assertEquals(3,WirelessInbox.send(key,20,3,1,n->{paid[0]+=n;return true;}));
        assertEquals(2,WirelessInbox.send(key,20,3,1,n->{paid[0]+=n;return true;}));
        assertEquals(5,paid[0]);assertEquals(100,WirelessInbox.waiting(key));
    }
    @Test void failedPaymentReturnsTheReservation() {
        WirelessInbox.setRoom(key,100,1);
        assertEquals(0,WirelessInbox.send(key,20,5,1,n->false));
        assertEquals(5,WirelessInbox.send(key,20,5,1,n->true));
    }
    @Test void staleAdvertisementsDoNotChargeAndSendingDoesNotRefreshTheirLife() {
        WirelessInbox.setRoom(key,100,1);
        assertEquals(1,WirelessInbox.send(key,20,1,21,n->true));
        assertEquals(0,WirelessInbox.send(key,20,1,22,n->{throw new AssertionError();}));
    }
    @Test void refreshingAccountsForEnergyAlreadyInFlight() {
        WirelessInbox.setRoom(key,100,1);WirelessInbox.send(key,20,3,1,n->true);
        WirelessInbox.setRoom(key,100,2);
        assertEquals(2,WirelessInbox.send(key,20,5,2,n->true));
    }
    @Test void partiallyFullBuffersKeepTheUnstoredRemainder() {
        WirelessInbox.setRoom(key,100,1);WirelessInbox.send(key,20,5,1,n->true);
        assertEquals(30,WirelessInbox.receive(key,n->30));assertEquals(70,WirelessInbox.waiting(key));
        assertEquals(70,WirelessInbox.receive(key,n->n));assertEquals(0,WirelessInbox.waiting(key));
    }
    @Test void dimensionUnloadAndBreakingRemoveCreditAndQueuedEnergy() {
        WirelessInbox.setRoom(key,100,1);WirelessInbox.send(key,20,1,1,n->true);
        BehaviorLifecycle.clearDimension(key.dim());
        assertEquals(0,WirelessInbox.waiting(key));assertEquals(0,WirelessInbox.send(key,20,1,1,n->true));
        WirelessInbox.setRoom(key,100,1);WirelessInbox.remove(key);
        assertEquals(0,WirelessInbox.send(key,20,1,1,n->true));
    }
}
