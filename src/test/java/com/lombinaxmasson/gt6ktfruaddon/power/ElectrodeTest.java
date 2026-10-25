package com.lombinaxmasson.gt6ktfruaddon.power;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.minecraft.resources.ResourceLocation;

class ElectrodeTest {
    @Test
    void onlyExactOwnRegistryIdsAreElectrodes() {
        assertSame(Electrode.NICKEL, Electrode.find(ResourceLocation.parse("gt6ktfruaddon:ktfru.item.battery.pole.nickel")));
        assertNull(Electrode.find(ResourceLocation.parse("other:ktfru.item.battery.pole.nickel")));
        assertNull(Electrode.find(ResourceLocation.parse("gt6ktfruaddon:ktfru.item.battery.pole.nickel_fake")));
        assertNull(Electrode.find(ResourceLocation.parse("gt6ktfruaddon:ktfru.item.battery.pole")));
        assertNull(Electrode.find(null));
    }

    @Test
    void electrolytePairingIsSymmetricAndRejectsUnknownOrMixedPoles() {
        assertEquals("GenLiquid_YttriumZirconiumOxide", Electrode.electrolyte(Electrode.NICKEL, Electrode.CATIO3));
        assertEquals("GenLiquid_YttriumZirconiumOxide", Electrode.electrolyte(Electrode.CATIO3, Electrode.NICKEL));
        assertEquals("GenLiquid_SulfuricAcid", Electrode.electrolyte(Electrode.CARBON, Electrode.PLATINUM));
        assertEquals("GenLiquid_SulfuricAcid", Electrode.electrolyte(Electrode.PLATINUM, Electrode.CARBON));
        assertEquals("", Electrode.electrolyte(Electrode.NICKEL, Electrode.PLATINUM));
        assertEquals("", Electrode.electrolyte(null, Electrode.CATIO3));
        assertEquals("", Electrode.electrolyte(Electrode.NICKEL, Electrode.NICKEL));
    }
}
