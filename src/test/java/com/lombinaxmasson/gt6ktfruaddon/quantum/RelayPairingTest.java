package com.lombinaxmasson.gt6ktfruaddon.quantum;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import net.minecraft.core.BlockPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.*;
class RelayPairingTest {
    final DimPos a=new DimPos("minecraft:overworld",new BlockPos(1,2,3));
    final DimPos b=new DimPos("minecraft:overworld",new BlockPos(4,5,6));
    final DimPos c=new DimPos("minecraft:overworld",new BlockPos(7,8,9));
    static class Endpoint implements RelayPairing.Endpoint {
        BlockPos partner;long revision;
        public BlockPos partner(){return partner;}public long revision(){return revision;}
        public void link(BlockPos p,long r){partner=p;revision=r;}
    }
    @Test void onlinePairingWritesBothPersistentEndpointsImmediately() {
        var links=new RelayPairing();var left=new Endpoint();var right=new Endpoint();
        links.update(a,left,1,k->true);links.update(b,right,1,k->true);
        links.pair(b,right,a,0,1,k->true);
        assertEquals(b.pos(),left.partner);assertEquals(a.pos(),right.partner);
        assertEquals(left.revision,right.revision);assertTrue(links.paired(a,1,k->true));
    }
    @Test void unloadedPartnerCompletesFromPersistedHalfAfterServerRestart() {
        var links=new RelayPairing();var left=new Endpoint();var right=new Endpoint();
        links.pair(b,right,a,0,1,k->false);assertEquals(a.pos(),right.partner);
        BehaviorLifecycle.clearAll();
        links.update(a,left,30,k->true);links.update(b,right,30,k->true);
        assertEquals(b.pos(),left.partner);assertTrue(links.paired(a,30,k->true));
    }
    @Test void newerPairingReplacesAnOldLinkWhenPartnerReloads() {
        var links=new RelayPairing();var left=new Endpoint();left.link(c.pos(),5);var right=new Endpoint();
        links.pair(b,right,a,5,10,k->false);
        links.update(a,left,11,k->true);links.update(b,right,11,k->true);
        assertEquals(b.pos(),left.partner);assertEquals(right.revision,left.revision);
    }
    @Test void staleOrUnloadedEndpointsCannotKeepTheRecipeGateOpen() {
        var links=new RelayPairing();var left=new Endpoint();var right=new Endpoint();
        links.update(a,left,1,k->true);links.update(b,right,1,k->true);links.pair(b,right,a,0,1,k->true);
        assertFalse(links.paired(a,22,k->true));
        links.update(a,left,23,k->true);links.update(b,right,23,k->true);
        assertFalse(links.paired(a,23,k->false));
    }
    @Test void sameTickRebindingsAreOrderedAndAnOldPeerCannotUndoThem() {
        var links=new RelayPairing();var left=new Endpoint();var right=new Endpoint();var third=new Endpoint();
        links.update(a,left,1,k->true);links.update(b,right,1,k->true);links.update(c,third,1,k->true);
        links.pair(b,right,a,0,1,k->true);long old=left.revision;
        links.pair(c,third,a,old,1,k->true);links.update(b,right,2,k->true);
        assertEquals(c.pos(),left.partner);assertTrue(left.revision>old);assertFalse(links.paired(b,2,k->true));
    }
    @Test void legacyHalfPairWithNoRevisionStillCompletes() {
        var links=new RelayPairing();var left=new Endpoint();var right=new Endpoint();right.link(a.pos(),0);
        links.update(a,left,1,k->true);links.update(b,right,1,k->true);
        assertEquals(b.pos(),left.partner);assertTrue(links.paired(a,1,k->true));
    }
}
