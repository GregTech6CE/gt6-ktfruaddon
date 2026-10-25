package com.lombinaxmasson.gt6ktfruaddon.quantum;

import java.util.function.Predicate;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.RevisionClock;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.TtlRegistry;

import net.minecraft.core.BlockPos;

/** Live endpoints are a view of persisted links, never the only copy of a handshake. Server thread only. */
final class RelayPairing {
    interface Endpoint {
        BlockPos partner();
        long revision();
        void link(BlockPos partner, long revision);
    }
    private final TtlRegistry<Endpoint> live = new TtlRegistry<>();
    private final RevisionClock clock = new RevisionClock();

    void update(DimPos self, Endpoint endpoint, long now, Predicate<DimPos> loaded) {
        live.put(self, endpoint, now);
        clock.observe(endpoint.revision());
        // A newly loaded endpoint may still carry an older link. The newer peer wins.
        for (DimPos key : live.keys(self.dim(), now)) {
            if (key.equals(self) || !loaded.test(key)) continue;
            Endpoint incoming = live.get(key, now);
            if (self.pos().equals(incoming.partner()) && (incoming.revision() > endpoint.revision()
                    || endpoint.partner() == null && incoming.revision() == endpoint.revision())) {
                endpoint.link(key.pos(), incoming.revision());
            }
        }
        BlockPos partner = endpoint.partner();
        if (partner == null) return;
        DimPos target = new DimPos(self.dim(), partner);
        Endpoint peer = loaded.test(target) ? live.get(target, now) : null;
        if (peer != null && (endpoint.revision() > peer.revision()
                || peer.partner() == null && endpoint.revision() == peer.revision())) {
            peer.link(self.pos(), endpoint.revision());
        }
    }

    void pair(DimPos self, Endpoint endpoint, DimPos target, long previousRevision, long now, Predicate<DimPos> loaded) {
        if (!self.dim().equals(target.dim()) || self.equals(target)) return;
        Endpoint peer = loaded.test(target) ? live.get(target, now) : null;
        long revision = clock.next(Math.max(now, Math.max(previousRevision,
                Math.max(endpoint.revision(), peer == null ? 0 : peer.revision()))));
        endpoint.link(target.pos(), revision);
        if (peer != null) peer.link(self.pos(), revision);
        live.put(self, endpoint, now);
    }

    boolean paired(DimPos self, long now, Predicate<DimPos> loaded) {
        Endpoint endpoint = live.get(self, now);
        if (endpoint == null || endpoint.partner() == null) return false;
        DimPos target = new DimPos(self.dim(), endpoint.partner());
        Endpoint peer = loaded.test(target) ? live.get(target, now) : null;
        return peer != null && self.pos().equals(peer.partner());
    }
    void remove(DimPos self) { live.remove(self); }
}
