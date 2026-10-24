package com.lombinaxmasson.gt6ktfruaddon.core.layout;

import com.gregtech.gregtech.api.addon.LayoutPart;

/** Structure parts that several features declare the same way. */
public final class Parts {
    private Parts() {}

    /** A part that only has a material and a hardness. */
    public static LayoutPart casing(String name, String material, float hardness) {
        return LayoutPart.builder(name, material).hardness(hardness).build();
    }
}
