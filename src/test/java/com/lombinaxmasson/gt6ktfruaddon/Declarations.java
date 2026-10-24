package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.CompressedGasTankDeclaration;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.MaterialDeclaration;
import com.gregtech.gregtech.api.addon.PrefixDeclaration;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.lombinaxmasson.gt6ktfruaddon.content.Materials;
import com.lombinaxmasson.gt6ktfruaddon.registry.Machines;

/**
 * What the addon declares to GT6CE, built once per test JVM. The SDK builders validate in
 * {@code build()}, so a bad declaration fails in the first test that asks for the list, with the
 * SDK's own message.
 */
final class Declarations {
    private static final Machines MACHINE_CONTRIBUTOR = new Machines();
    private static final Materials MATERIAL_CONTRIBUTOR = new Materials();

    private static List<AddonMachine> machines;
    private static List<RecipeTable> recipeTables;
    private static List<StructureLayout> layouts;
    private static List<LayoutPart> parts;
    private static List<CompressedGasTankDeclaration> gasTanks;
    private static List<MaterialDeclaration> materials;
    private static List<PrefixDeclaration> prefixes;

    private Declarations() {}

    static Machines machineContributor() {
        return MACHINE_CONTRIBUTOR;
    }

    static synchronized List<AddonMachine> machines() {
        if (machines == null) machines = MACHINE_CONTRIBUTOR.addonMachines();
        return machines;
    }

    static synchronized List<RecipeTable> recipeTables() {
        if (recipeTables == null) recipeTables = MACHINE_CONTRIBUTOR.recipeTables();
        return recipeTables;
    }

    static synchronized List<StructureLayout> layouts() {
        if (layouts == null) layouts = MACHINE_CONTRIBUTOR.structureLayouts();
        return layouts;
    }

    static synchronized List<LayoutPart> parts() {
        if (parts == null) parts = MACHINE_CONTRIBUTOR.layoutParts();
        return parts;
    }

    static synchronized List<CompressedGasTankDeclaration> gasTanks() {
        if (gasTanks == null) gasTanks = MACHINE_CONTRIBUTOR.compressedGasTanks();
        return gasTanks;
    }

    static synchronized List<MaterialDeclaration> materials() {
        if (materials == null) materials = MATERIAL_CONTRIBUTOR.materials();
        return materials;
    }

    static synchronized List<PrefixDeclaration> prefixes() {
        if (prefixes == null) prefixes = MATERIAL_CONTRIBUTOR.prefixes();
        return prefixes;
    }
}
