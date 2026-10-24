package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

import org.junit.jupiter.api.Test;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.CompressedGasTankDeclaration;
import com.gregtech.gregtech.api.addon.FuelGenerator;
import com.gregtech.gregtech.api.addon.LayoutCell;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.RecipeMachine;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;

/**
 * What GT6CE checks only at game start, checked here on the plain JVM: the SDK builders validate
 * in {@code build()}, and these tests cover the rules that span several declarations.
 */
class MachineContractTest {
    @Test
    void declarationsBuild() {
        assertFalse(Declarations.machines().isEmpty(), "no machines declared");
        assertFalse(Declarations.recipeTables().isEmpty(), "no recipe tables declared");
        assertFalse(Declarations.layouts().isEmpty(), "no structure layouts declared");
        assertFalse(Declarations.parts().isEmpty(), "no layout parts declared");
    }

    @Test
    void blockIdsAreUnique() {
        Map<String, String> owner = new HashMap<>();
        List<String> clashes = new ArrayList<>();
        for (AddonMachine machine : Declarations.machines()) {
            claim(owner, clashes, machine.casing().id(), "a machine");
        }
        for (LayoutPart part : Declarations.parts()) {
            claim(owner, clashes, part.name(), "a layout part");
        }
        for (CompressedGasTankDeclaration tank : Declarations.gasTanks()) {
            claim(owner, clashes, tank.id(), "a gas drum");
        }
        for (String size : Gt6KtfruMachines.FLUID_PIPE_SIZES) {
            claim(owner, clashes, Gt6KtfruMachines.fluidPipeId(size), "a fluid pipe");
        }
        for (String size : Gt6KtfruMachines.ITEM_PIPE_SIZES) {
            claim(owner, clashes, Gt6KtfruMachines.itemPipeId(size), "an item pipe");
        }
        assertEquals(List.of(), clashes);
    }

    /** GT6CE rejects a machine type whose blocks disagree; its signature includes the main energy type. */
    @Test
    void machinesOfOneTypeAgreeOnTheMainEnergyType() {
        Map<String, Set<String>> energyByType = new TreeMap<>();
        for (AddonMachine machine : Declarations.machines()) {
            String energy = mainEnergyType(machine);
            if (energy != null) {
                energyByType.computeIfAbsent(typeOf(machine), type -> new TreeSet<>()).add(energy);
            }
        }
        List<String> mixed = energyByType.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> entry.getKey() + " mixes " + entry.getValue())
                .toList();
        assertEquals(List.of(), mixed);
    }

    @Test
    void behaviorMachineTypesAndTheFactoryAgree() {
        Set<String> used = Declarations.machines().stream()
                .filter(machine -> machine instanceof BehaviorMachine)
                .map(MachineContractTest::typeOf)
                .collect(Collectors.toCollection(TreeSet::new));
        Set<String> listed = new TreeSet<>(Declarations.machineContributor().machineTypes());
        assertEquals(Set.of(), difference(used, listed), "behavior machine types the factory does not list");
        assertEquals(Set.of(), difference(listed, used), "factory types that no behavior machine uses");
    }

    @Test
    void factoryBuildsABehaviorForEveryListedType() {
        List<String> failures = new ArrayList<>();
        for (String type : new TreeSet<>(Declarations.machineContributor().machineTypes())) {
            try {
                assertNotNull(Declarations.machineContributor().create(type), "null behavior");
            } catch (Throwable problem) {
                failures.add(type + ": " + problem);
            }
        }
        assertEquals(List.of(), failures);
    }

    @Test
    void factoryRejectsUnknownTypes() {
        assertThrows(IllegalArgumentException.class,
                () -> Declarations.machineContributor().create("no_such_machine"));
    }

    @Test
    void recipeTableNamesAreUnique() {
        Set<String> seen = new TreeSet<>();
        List<String> duplicates = new ArrayList<>();
        for (RecipeTable table : Declarations.recipeTables()) {
            if (!seen.add(table.name())) duplicates.add(table.name());
        }
        assertEquals(List.of(), duplicates);
    }

    @Test
    void machinesUseDeclaredOrBuiltInRecipeMaps() {
        Set<String> declared = Declarations.recipeTables().stream()
                .map(RecipeTable::name)
                .collect(Collectors.toCollection(TreeSet::new));
        List<String> unknown = new ArrayList<>();
        for (AddonMachine machine : Declarations.machines()) {
            String id = machine.casing().id();
            switch (machine) {
                case RecipeMachine recipe -> checkMap(unknown, declared, id, recipe.recipeMap());
                case FuelGenerator fuel -> checkMap(unknown, declared, id, fuel.fuelMap());
                case BehaviorMachine behavior ->
                        behavior.recipeMaps().forEach(map -> checkMap(unknown, declared, id, map));
            }
        }
        assertEquals(List.of(), unknown);
    }

    @Test
    void largeRecipeMachinesHaveALayoutNamedAfterTheirType() {
        Set<String> layoutNames = layoutNames();
        List<String> missing = Declarations.machines().stream()
                .filter(machine -> machine instanceof RecipeMachine recipe && recipe.largeRecipe())
                .map(MachineContractTest::typeOf)
                .filter(type -> !layoutNames.contains(type))
                .distinct()
                .sorted()
                .toList();
        assertEquals(List.of(), missing);
    }

    @Test
    void layoutNamesAreUniqueAndBelongToAMachineType() {
        List<String> names = Declarations.layouts().stream().map(StructureLayout::name).toList();
        List<String> duplicates = new ArrayList<>();
        Set<String> seen = new TreeSet<>();
        for (String name : names) {
            if (!seen.add(name)) duplicates.add(name);
        }
        assertEquals(List.of(), duplicates, "duplicate layout names");

        Set<String> types = Declarations.machines().stream()
                .map(MachineContractTest::typeOf)
                .collect(Collectors.toCollection(TreeSet::new));
        List<String> stale = names.stream().filter(name -> !types.contains(name)).sorted().toList();
        assertEquals(List.of(), stale, "layouts that no machine type uses");
    }

    /** GT6CE reports an unknown part only when the game registers the layout. */
    @Test
    void layoutsOnlyReferenceKnownParts() throws IOException {
        Set<String> known = builtInPartNames();
        Declarations.parts().forEach(part -> known.add(part.name()));
        Set<String> referenced = new TreeSet<>();
        for (StructureLayout layout : Declarations.layouts()) {
            for (LayoutCell cell : layout.cells()) {
                collectParts(cell.match(), referenced);
            }
        }
        assertEquals(Set.of(), difference(referenced, known), "layout cells naming a part nobody declares");
    }

    static String typeOf(AddonMachine machine) {
        String type = machine.casing().machineType();
        return type != null ? type : machine.casing().id();
    }

    private static String mainEnergyType(AddonMachine machine) {
        return switch (machine) {
            case RecipeMachine recipe -> recipe.energy().type();
            case BehaviorMachine behavior -> behavior.energy() == null ? null : behavior.energy().type();
            case FuelGenerator fuel -> fuel.output().type();
        };
    }

    private static void claim(Map<String, String> owner, List<String> clashes, String id, String what) {
        String previous = owner.putIfAbsent(id, what);
        if (previous != null) clashes.add(id + " is declared as " + previous + " and as " + what);
    }

    private static void checkMap(List<String> unknown, Set<String> declared, String machine, String map) {
        if (map != null && !declared.contains(map) && !map.startsWith("gt.recipe.")) {
            unknown.add(machine + " -> " + map);
        }
    }

    private static Set<String> layoutNames() {
        return Declarations.layouts().stream()
                .map(StructureLayout::name)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private static Set<String> difference(Set<String> left, Set<String> right) {
        Set<String> result = new TreeSet<>(left);
        result.removeAll(right);
        return result;
    }

    private static void collectParts(CellMatch match, Set<String> out) {
        if (match instanceof CellMatch.Part part) {
            out.add(part.name());
        } else if (match instanceof CellMatch.OneOf oneOf) {
            oneOf.members().forEach(member -> collectParts(member, out));
        }
    }

    /** GT6CE's own part blocks, taken from the item models in the runtime jar that tools/build-gt6ce.ps1 builds. */
    private static Set<String> builtInPartNames() throws IOException {
        String prefix = "assets/gt6ce/models/item/";
        Set<String> names = new TreeSet<>();
        try (ZipFile jar = new ZipFile(Path.of("libs", "gt6ce", "gt6ce-1.21.1-neoforge.jar").toFile())) {
            jar.stream()
                    .map(entry -> entry.getName())
                    .filter(name -> name.startsWith(prefix) && name.endsWith(".json"))
                    .map(name -> name.substring(prefix.length(), name.length() - ".json".length()))
                    .forEach(names::add);
        }
        return names;
    }
}
