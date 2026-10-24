package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;
import java.util.Set;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.AddonMachineContributor;
import com.gregtech.gregtech.api.addon.CompressedGasTankDeclaration;
import com.gregtech.gregtech.api.addon.FluidPipeDeclaration;
import com.gregtech.gregtech.api.addon.ItemPipeDeclaration;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.BehaviorMachineContributor;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.registry.Features;

/**
 * What GT6CE finds through ServiceLoader. Every list comes from the feature registry, which builds
 * each of them once, however many times GT6CE or the addon asks.
 */
public final class Gt6KtfruMachines implements AddonMachineContributor, BehaviorMachineContributor {
    @Override
    public String addonId() {
        return Ktfru.MODID;
    }

    @Override
    public List<RecipeTable> recipeTables() {
        return Features.get().recipeTables();
    }

    @Override
    public List<AddonMachine> addonMachines() {
        return Features.get().machines();
    }

    @Override
    public List<StructureLayout> structureLayouts() {
        return Features.get().layouts();
    }

    @Override
    public List<LayoutPart> layoutParts() {
        return Features.get().parts();
    }

    @Override
    public List<FluidPipeDeclaration> fluidPipes() {
        return Features.get().fluidPipes();
    }

    @Override
    public List<ItemPipeDeclaration> itemPipes() {
        return Features.get().itemPipes();
    }

    @Override
    public List<CompressedGasTankDeclaration> compressedGasTanks() {
        return Features.get().gasTanks();
    }

    @Override
    public Set<String> machineTypes() {
        return Features.get().machineTypes();
    }

    @Override
    public MachineBehavior create(String machineType) {
        return Features.get().createBehavior(machineType);
    }
}
