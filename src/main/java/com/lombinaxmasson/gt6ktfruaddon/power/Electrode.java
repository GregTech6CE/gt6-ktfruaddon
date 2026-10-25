package com.lombinaxmasson.gt6ktfruaddon.power;

import com.lombinaxmasson.gt6ktfruaddon.Ktfru;

import net.minecraft.resources.ResourceLocation;

/** Exact registered identities; legacy dotted paths remain save-compatible. */
enum Electrode {
    NICKEL("nickel"), CATIO3("catio3"), CARBON("carbon"), PLATINUM("platinum");

    private final ResourceLocation id;

    Electrode(String suffix) {
        id = ResourceLocation.fromNamespaceAndPath(Ktfru.MODID, "ktfru.item.battery.pole." + suffix);
    }

    static Electrode find(ResourceLocation id) {
        for (Electrode pole : values()) if (pole.id.equals(id)) return pole;
        return null;
    }

    static String electrolyte(Electrode left, Electrode right) {
        if (pair(left, right, NICKEL, CATIO3)) return "GenLiquid_YttriumZirconiumOxide";
        if (pair(left, right, CARBON, PLATINUM)) return "GenLiquid_SulfuricAcid";
        return "";
    }

    private static boolean pair(Electrode left, Electrode right, Electrode a, Electrode b) {
        return (left == a && right == b) || (left == b && right == a);
    }
}
