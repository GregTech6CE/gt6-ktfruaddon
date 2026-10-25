package com.lombinaxmasson.gt6ktfruaddon.core;

import net.minecraft.resources.ResourceLocation;

/**
 * GT6CE 1d1f69fbb registry naming conventions, not public SDK lookup contracts.
 * Layout parts use their declared names; display fluid items use fluid_item_ plus the fluid path.
 * Builder tools contain builder_wand; source blocks use fluid_spring_ plus the fluid path.
 * Keep these compatibility assumptions here until the SDK provides dedicated lookups.
 */
public final class Gt6ce {
    public static final String NAMESPACE = "gt6ce";
    private static final String SPRING = "fluid_spring_";

    private Gt6ce() {}

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(NAMESPACE, path);
    }

    public static boolean owns(ResourceLocation id) {
        return id != null && NAMESPACE.equals(id.getNamespace());
    }

    public static ResourceLocation fluidItem(ResourceLocation fluid) {
        return id("fluid_item_" + fluid.getPath());
    }

    public static boolean builderWand(ResourceLocation id) {
        return owns(id) && id.getPath().contains("builder_wand");
    }

    public static String springPath(ResourceLocation id) {
        return owns(id) && id.getPath().startsWith(SPRING) ? id.getPath() : "";
    }

    public static String springFluid(String path) {
        if (!path.startsWith(SPRING)) throw new IllegalArgumentException("not a fluid spring: " + path);
        return path.substring(SPRING.length());
    }
}
