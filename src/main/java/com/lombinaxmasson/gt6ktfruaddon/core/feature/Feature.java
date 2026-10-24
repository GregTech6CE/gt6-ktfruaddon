package com.lombinaxmasson.gt6ktfruaddon.core.feature;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.CompressedGasTankDeclaration;
import com.gregtech.gregtech.api.addon.FluidPipeDeclaration;
import com.gregtech.gregtech.api.addon.ItemPipeDeclaration;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;

/**
 * What one part of the addon declares to GT6CE: its recipe tables, machines, structure layouts and
 * parts, the compressed gas drums and pipes, and the behavior of each machine type it declares as
 * a behavior machine. A feature lists only what it has; the rest stays empty.
 *
 * <p>The features are collected in a registry, and the lists GT6CE gets are the features' lists in
 * the order of the registry.
 */
public interface Feature {
    default List<RecipeTable> recipeTables() {
        return List.of();
    }

    default List<AddonMachine> machines() {
        return List.of();
    }

    default List<StructureLayout> layouts() {
        return List.of();
    }

    default List<LayoutPart> parts() {
        return List.of();
    }

    default List<CompressedGasTankDeclaration> gasTanks() {
        return List.of();
    }

    default List<FluidPipeDeclaration> fluidPipes() {
        return List.of();
    }

    default List<ItemPipeDeclaration> itemPipes() {
        return List.of();
    }

    /** One behavior per machine type of the behavior machines this feature declares. */
    default Map<String, Supplier<MachineBehavior>> behaviors() {
        return Map.of();
    }

    /** Ids of blocks that GT6CE builds from this feature's declarations without a declaration naming them. */
    default List<String> derivedBlockIds() {
        return List.of();
    }
}
