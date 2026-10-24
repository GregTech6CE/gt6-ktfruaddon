package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.registry.Features;

/** The registry's own rules, on features made up here, and then on the addon's real ones. */
class FeaturesTest {
    @Test
    void everyListFollowsTheOrderOfTheFeatures() {
        Features features = new Features(List.of(partsOf("a", "b"), partsOf("c"), partsOf("d")));
        assertEquals(List.of("a", "b", "c", "d"), features.parts().stream().map(LayoutPart::name).toList());
    }

    @Test
    void aListIsBuiltOnce() {
        int[] calls = {0};
        Feature counting = new Feature() {
            @Override
            public List<LayoutPart> parts() {
                calls[0]++;
                return List.of(part("a"));
            }
        };
        Features features = new Features(List.of(counting));
        assertSame(features.parts(), features.parts());
        assertEquals(1, calls[0]);
    }

    @Test
    void machineTypesAreTheBehaviorKeysOfAllFeatures() {
        Features features = new Features(List.of(behaviorsOf("alpha", "beta"), behaviorsOf("gamma")));
        assertEquals(Set.of("alpha", "beta", "gamma"), features.machineTypes());
    }

    @Test
    void aBehaviorIsCreatedByTheFeatureThatDeclaresItsType() {
        MachineBehavior expected = behavior();
        Feature feature = new Feature() {
            @Override
            public Map<String, Supplier<MachineBehavior>> behaviors() {
                return new Behaviors().add("alpha", () -> expected).build();
            }
        };
        assertSame(expected, new Features(List.of(feature)).createBehavior("alpha"));
    }

    @Test
    void aMachineTypeTwoFeaturesImplementIsRejected() {
        Features features = new Features(List.of(behaviorsOf("alpha"), behaviorsOf("alpha")));
        IllegalStateException problem = assertThrows(IllegalStateException.class, features::machineTypes);
        assertTrue(problem.getMessage().contains("alpha"), problem.getMessage());
    }

    @Test
    void aMachineTypeNobodyDeclaresIsRejected() {
        Features features = new Features(List.of(behaviorsOf("alpha")));
        assertThrows(IllegalArgumentException.class, () -> features.createBehavior("beta"));
    }

    @Test
    void aTypeAddedTwiceToOneBehaviorsTableIsRejected() {
        Behaviors table = new Behaviors().add("alpha", FeaturesTest::behavior);
        assertThrows(IllegalStateException.class, () -> table.add("alpha", FeaturesTest::behavior));
    }

    @Test
    void theAddonsFeaturesBuildOnceAndCoverEveryKindOfDeclaration() {
        Features features = Features.get();
        assertFalse(features.recipeTables().isEmpty());
        assertFalse(features.machines().isEmpty());
        assertFalse(features.layouts().isEmpty());
        assertFalse(features.parts().isEmpty());
        assertFalse(features.gasTanks().isEmpty());
        assertFalse(features.fluidPipes().isEmpty());
        assertFalse(features.itemPipes().isEmpty());
        assertFalse(features.machineTypes().isEmpty());
        assertSame(features.machines(), Features.get().machines());
    }

    /** A feature that is written but not listed in the registry would silently declare nothing. */
    @Test
    void everyFeatureClassOfTheAddonIsRegistered() throws Exception {
        Path classes = Path.of(Features.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        Set<String> found = new TreeSet<>();
        try (Stream<Path> files = Files.walk(classes)) {
            for (Path file : files.filter(path -> path.toString().endsWith(".class")).toList()) {
                String name = classes.relativize(file).toString().replace(File.separatorChar, '.')
                        .replaceAll("\\.class$", "");
                Class<?> type = Class.forName(name, false, Features.class.getClassLoader());
                if (Feature.class.isAssignableFrom(type) && !type.isInterface()
                        && !Modifier.isAbstract(type.getModifiers())) {
                    found.add(type.getName());
                }
            }
        }
        Set<String> registered = new TreeSet<>();
        for (Feature feature : Features.get().features()) registered.add(feature.getClass().getName());
        assertEquals(registered, found, "feature classes the registry does not list");
    }

    @Test
    void everyBlockIdIsListedOnce() {
        Features features = Features.get();
        List<String> ids = features.blockIds();
        assertEquals(features.machines().size() + features.parts().size() + features.gasTanks().size()
                + features.derivedBlockIds().size(), ids.size());
        assertEquals(ids.size(), Set.copyOf(ids).size(), "block ids that are listed twice");
    }

    private static LayoutPart part(String name) {
        return LayoutPart.builder(name, "Iron").hardness(1.0F).build();
    }

    private static Feature partsOf(String... names) {
        return new Feature() {
            @Override
            public List<LayoutPart> parts() {
                return java.util.Arrays.stream(names).map(FeaturesTest::part).toList();
            }
        };
    }

    private static Feature behaviorsOf(String... types) {
        return new Feature() {
            @Override
            public Map<String, Supplier<MachineBehavior>> behaviors() {
                Behaviors table = new Behaviors();
                for (String type : types) table.add(type, FeaturesTest::behavior);
                return table.build();
            }
        };
    }

    /** A behavior that is never called; the tests only look at which one comes back. */
    private static MachineBehavior behavior() {
        return (MachineBehavior) Proxy.newProxyInstance(MachineBehavior.class.getClassLoader(),
                new Class<?>[] {MachineBehavior.class}, (proxy, method, arguments) -> null);
    }
}
