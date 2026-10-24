package com.lombinaxmasson.gt6ktfruaddon.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.CompressedGasTankDeclaration;
import com.gregtech.gregtech.api.addon.FluidPipeDeclaration;
import com.gregtech.gregtech.api.addon.ItemPipeDeclaration;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruBatteries;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruCatalyticMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruComputeMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruCrucibleMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruDistillationMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruFuelGenerators;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruFusionMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruGasBatteries;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruGasMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruGeneratedStructures;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruHeatMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruKineticMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruLateMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruOreProcessing;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruResearchMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruSemiconductorMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruSingleBlockMachines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruTransformerParts;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruTurbines;
import com.lombinaxmasson.gt6ktfruaddon.Gt6KtfruWirelessMachines;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;

/**
 * Every feature of the addon, and the lists GT6CE asks for, each built once.
 *
 * <p>The order of the features is the order of every list. GT6CE numbers the structure parts by
 * their position in {@link #parts()}, so a new feature goes at the end and no feature is moved.
 */
public final class Features {
    private static Features instance;

    private final List<Feature> features;
    private List<RecipeTable> recipeTables;
    private List<AddonMachine> machines;
    private List<StructureLayout> layouts;
    private List<LayoutPart> parts;
    private List<CompressedGasTankDeclaration> gasTanks;
    private List<FluidPipeDeclaration> fluidPipes;
    private List<ItemPipeDeclaration> itemPipes;
    private List<String> derivedBlockIds;
    private Map<String, Supplier<MachineBehavior>> behaviors;

    public Features(List<Feature> features) {
        this.features = List.copyOf(features);
    }

    /** The features of this addon. */
    public static synchronized Features get() {
        if (instance == null) {
            instance = new Features(List.of(
                    new Gt6KtfruSingleBlockMachines(),
                    new Gt6KtfruDistillationMachines(),
                    new Gt6KtfruOreProcessing(),
                    new Gt6KtfruGasMachines(),
                    new Gt6KtfruKineticMachines(),
                    new Gt6KtfruGasBatteries(),
                    new Gt6KtfruTransformerParts(),
                    new Gt6KtfruSemiconductorMachines(),
                    new Gt6KtfruFuelGenerators(),
                    new Gt6KtfruHeatMachines(),
                    new Gt6KtfruTurbines(),
                    new Gt6KtfruBatteries(),
                    new Gt6KtfruResearchMachines(),
                    new Gt6KtfruComputeMachines(),
                    new Gt6KtfruWirelessMachines(),
                    new Gt6KtfruFusionMachines(),
                    new Gt6KtfruCatalyticMachines(),
                    new Gt6KtfruLateMachines(),
                    new Gt6KtfruCrucibleMachines(),
                    new Gt6KtfruGeneratedStructures()));
        }
        return instance;
    }

    /** The features, in the order of the registry. */
    public List<Feature> features() {
        return features;
    }

    public synchronized List<RecipeTable> recipeTables() {
        if (recipeTables == null) recipeTables = merged(Feature::recipeTables);
        return recipeTables;
    }

    public synchronized List<AddonMachine> machines() {
        if (machines == null) machines = merged(Feature::machines);
        return machines;
    }

    public synchronized List<StructureLayout> layouts() {
        if (layouts == null) layouts = merged(Feature::layouts);
        return layouts;
    }

    public synchronized List<LayoutPart> parts() {
        if (parts == null) parts = merged(Feature::parts);
        return parts;
    }

    public synchronized List<CompressedGasTankDeclaration> gasTanks() {
        if (gasTanks == null) gasTanks = merged(Feature::gasTanks);
        return gasTanks;
    }

    public synchronized List<FluidPipeDeclaration> fluidPipes() {
        if (fluidPipes == null) fluidPipes = merged(Feature::fluidPipes);
        return fluidPipes;
    }

    public synchronized List<ItemPipeDeclaration> itemPipes() {
        if (itemPipes == null) itemPipes = merged(Feature::itemPipes);
        return itemPipes;
    }

    /** Blocks that GT6CE builds from a declaration without the declaration naming them: the pipes. */
    public synchronized List<String> derivedBlockIds() {
        if (derivedBlockIds == null) derivedBlockIds = merged(Feature::derivedBlockIds);
        return derivedBlockIds;
    }

    /** The id of every block GT6CE registers for this addon: machines, structure parts, drums and pipes. */
    public List<String> blockIds() {
        List<String> ids = new ArrayList<>();
        for (AddonMachine machine : machines()) ids.add(machine.casing().id());
        for (LayoutPart part : parts()) ids.add(part.name());
        for (CompressedGasTankDeclaration tank : gasTanks()) ids.add(tank.id());
        ids.addAll(derivedBlockIds());
        return List.copyOf(ids);
    }

    /** The machine types that have a behavior, from all features. */
    public Set<String> machineTypes() {
        return behaviors().keySet();
    }

    public MachineBehavior createBehavior(String machineType) {
        Supplier<MachineBehavior> behavior = behaviors().get(machineType);
        if (behavior == null) throw new IllegalArgumentException(machineType);
        return behavior.get();
    }

    private synchronized Map<String, Supplier<MachineBehavior>> behaviors() {
        if (behaviors == null) {
            Map<String, Supplier<MachineBehavior>> all = new LinkedHashMap<>();
            for (Feature feature : features) {
                for (Map.Entry<String, Supplier<MachineBehavior>> entry : feature.behaviors().entrySet()) {
                    if (all.putIfAbsent(entry.getKey(), entry.getValue()) != null) {
                        throw new IllegalStateException("Machine type '" + entry.getKey()
                                + "' has a behavior in two features, the second is " + feature.getClass().getSimpleName());
                    }
                }
            }
            behaviors = Collections.unmodifiableMap(all);
        }
        return behaviors;
    }

    private <T> List<T> merged(Function<Feature, List<T>> part) {
        List<T> all = new ArrayList<>();
        for (Feature feature : features) all.addAll(part.apply(feature));
        return List.copyOf(all);
    }
}
