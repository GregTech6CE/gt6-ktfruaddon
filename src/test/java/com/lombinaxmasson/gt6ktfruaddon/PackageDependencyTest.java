package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.lombinaxmasson.gt6ktfruaddon.PackageRules.Use;

/**
 * The directions in which the packages of the addon use each other, as {@link PackageRules} lays
 * them down. The first three tests apply the rules to the real sources. The others try the rules
 * and the reading of the sources on made-up input, so a rule that stops working turns red here
 * instead of letting everything through.
 */
class PackageDependencyTest {
    private static final Set<String> FEATURES = Set.of("processing", "gas", "heat", "research", "network", "ai");

    @Test
    void packagesUseEachOtherOnlyInTheAllowedDirections() {
        Path root = PackageRules.sources();
        assertEquals(List.of(),
                PackageRules.violations(PackageRules.usesInSources(root), PackageRules.featuresIn(root)));
    }

    @Test
    void packagesDoNotUseEachOtherInACircle() {
        Map<String, Set<String>> graph = new TreeMap<>();
        for (Use use : PackageRules.usesInSources(PackageRules.sources())) {
            if (!use.from().equals(use.to())) {
                graph.computeIfAbsent(use.from(), key -> new TreeSet<>()).add(use.to());
            }
        }
        assertEquals(List.of(), PackageRules.cycles(graph));
    }

    @Test
    void theRootPackageHoldsOnlyTheEntryClassAndKtfru() {
        assertEquals(Set.of(PackageRules.ENTRY, PackageRules.KTFRU), PackageRules.rootClasses(PackageRules.sources()));
    }

    @Test
    void everyoneMayUseKtfruAndItself() {
        assertEquals(List.of(), violations(use("core", "Ktfru"), use("heat", "Ktfru"), use("registry", "Ktfru"),
                use("KtfruAddon", "Ktfru"), use("heat", "heat"), use("core", "core")));
    }

    @Test
    void ktfruUsesNothingElse() {
        for (String other : List.of("core", "recipe", "heat", "registry", "KtfruAddon")) {
            assertEquals(1, violations(use("Ktfru", other)).size(), "Ktfru uses " + other);
        }
    }

    @Test
    void aFeatureUsesCoreAndRecipeButNothingElse() {
        assertEquals(List.of(), violations(use("heat", "core"), use("heat", "recipe")));
        for (String other : List.of("gas", "generated", "content", "registry", "client", "event", "KtfruAddon")) {
            assertEquals(1, violations(use("heat", other)).size(), "heat uses " + other);
        }
    }

    @Test
    void researchMayUseNetworkAndNetworkMayNotUseResearch() {
        assertEquals(List.of(), violations(use("research", "network")));
        assertEquals(1, violations(use("network", "research")).size());
        assertEquals(1, violations(use("research", "heat")).size());
    }

    @Test
    void aPackageThatIsNotNamedIsAFeature() {
        assertEquals(List.of(), violations(use("ai", "core"), use("ai", "recipe"), use("registry", "ai")));
        assertEquals(1, violations(use("ai", "heat")).size());
        assertEquals(1, violations(use("heat", "ai")).size());
    }

    @Test
    void coreUsesNothingButKtfru() {
        for (String other : List.of("recipe", "content", "generated", "registry", "heat", "client", "KtfruAddon")) {
            assertEquals(1, violations(use("core", other)).size(), "core uses " + other);
        }
    }

    @Test
    void generatedCodeUsesCoreAndRecipeOnly() {
        assertEquals(List.of(), violations(use("generated", "core"), use("generated", "recipe")));
        for (String other : List.of("content", "registry", "heat", "client", "KtfruAddon")) {
            assertEquals(1, violations(use("generated", other)).size(), "generated uses " + other);
        }
    }

    @Test
    void nothingUsesTheEntryClassTheClientOrTheEvents() {
        for (String node : List.of("KtfruAddon", "client", "event")) {
            for (String user : List.of("core", "recipe", "generated", "content", "registry", "heat", "client",
                    "event", "KtfruAddon")) {
                if (!user.equals(node)) {
                    assertEquals(1, violations(use(user, node)).size(), user + " uses " + node);
                }
            }
        }
    }

    @Test
    void onlyTheWiringUsesTheRegistry() {
        assertEquals(List.of(), violations(use("KtfruAddon", "registry"), use("client", "registry"),
                use("event", "registry")));
        for (String user : List.of("core", "recipe", "generated", "content", "heat", "research")) {
            assertEquals(1, violations(use(user, "registry")).size(), user + " uses registry");
        }
    }

    @Test
    void theRegistryKnowsEveryFeatureAndTheGeneratedClasses() {
        List<Use> all = new ArrayList<>();
        for (String target : FEATURES) all.add(use("registry", target));
        all.addAll(List.of(use("registry", "core"), use("registry", "recipe"), use("registry", "generated"),
                use("registry", "content")));
        assertEquals(List.of(), PackageRules.violations(all, FEATURES));
        assertEquals(1, violations(use("registry", "client")).size());
        assertEquals(1, violations(use("registry", "KtfruAddon")).size());
    }

    @Test
    void aViolationNamesTheFileTheUseAndWhatIsAllowed() {
        List<String> found = PackageRules.violations(
                List.of(new Use("gas/GasTank.java", "gas", "heat", PackageRules.BASE + ".heat.SunHeater")), FEATURES);
        assertEquals(1, found.size());
        assertTrue(found.get(0).contains("gas/GasTank.java"), found.get(0));
        assertTrue(found.get(0).contains("gas may not use heat"), found.get(0));
        assertTrue(found.get(0).contains(PackageRules.BASE + ".heat.SunHeater"), found.get(0));
        assertTrue(found.get(0).contains("[Ktfru, core, recipe]"), found.get(0));
    }

    @Test
    void aCircleIsFoundAndAChainIsNot() {
        assertEquals(List.of(), PackageRules.cycles(Map.of("a", Set.of("b"), "b", Set.of("c"))));
        assertEquals(List.of(List.of("a", "b", "c")),
                PackageRules.cycles(Map.of("a", Set.of("b"), "b", Set.of("c"), "c", Set.of("a"), "d", Set.of("a"))));
        assertEquals(List.of(List.of("a", "b"), List.of("c", "d")),
                PackageRules.cycles(Map.of("a", Set.of("b"), "b", Set.of("a"), "c", Set.of("d"), "d", Set.of("c"))));
    }

    @Test
    void importsStaticImportsAndQualifiedNamesAreAllUses() {
        String source = """
                package com.lombinaxmasson.gt6ktfruaddon.heat;

                import com.lombinaxmasson.gt6ktfruaddon.Ktfru;
                import com.lombinaxmasson.gt6ktfruaddon.core.layout.LayerLayout;
                import static com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps.HEAT_MIXER;

                /** See {@link com.lombinaxmasson.gt6ktfruaddon.power.Turbines}. */
                class X {
                    Object o = com.lombinaxmasson.gt6ktfruaddon.gas.GasTank.class;
                }
                """;
        List<Use> uses = PackageRules.usesIn("heat/X.java", source);
        assertEquals(List.of("Ktfru", "core", "recipe", "power", "gas"), uses.stream().map(Use::to).toList());
        assertEquals(Set.of("heat"), Set.copyOf(uses.stream().map(Use::from).toList()));
        assertEquals(PackageRules.BASE + ".recipe.RecipeMaps.HEAT_MIXER", uses.get(2).name());
    }

    @Test
    void thePackageLineIsNotAUseAndARootFileIsItsOwnNode() {
        assertEquals(List.of(), PackageRules.usesIn("heat/Y.java", "package com.lombinaxmasson.gt6ktfruaddon.heat;\n"));
        String entry = "package com.lombinaxmasson.gt6ktfruaddon;\n"
                + "import com.lombinaxmasson.gt6ktfruaddon.registry.Features;\n";
        assertEquals(List.of(new Use("KtfruAddon.java", "KtfruAddon", "registry",
                PackageRules.BASE + ".registry.Features")), PackageRules.usesIn("KtfruAddon.java", entry));
    }

    @Test
    void aSourceOutsideTheRootPackageIsRejected() {
        IllegalArgumentException problem = assertThrows(IllegalArgumentException.class,
                () -> PackageRules.usesIn("Z.java", "package com.example.other;\n"));
        assertTrue(problem.getMessage().contains("outside"), problem.getMessage());
    }

    private static Use use(String from, String to) {
        return new Use(from + "/Some.java", from, to, PackageRules.BASE + "." + to + ".Other");
    }

    private static List<String> violations(Use... uses) {
        return PackageRules.violations(List.of(uses), FEATURES);
    }
}
