package com.lombinaxmasson.gt6ktfruaddon;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Which package of the addon may use which. A node is a package directly below the root, and a
 * deeper package belongs to the one above it (core.layout is core). The root package holds two
 * classes, and each is a node of its own, because Ktfru is used by everyone and the entry class
 * uses everyone.
 *
 * <pre>
 * Ktfru        uses nothing
 * core         the generic pieces; uses Ktfru
 * recipe       the recipe map names; uses core
 * content      the materials; uses core
 * generated    written by the scripts; uses core and recipe
 * features     every other package (processing, gas, kinetic, power, heat, ...); use core and
 *              recipe, and research also uses network
 * registry     knows every feature and what the scripts generate
 * client       uses registry
 * event        uses registry
 * KtfruAddon   the entry class; wires registry, content and generated
 * </pre>
 *
 * Every node may use itself and Ktfru. Nothing uses KtfruAddon, client or event, and only they use
 * registry. A package that is not named above is a feature, so a new feature needs no change here.
 * A rule that has to change, because a feature really needs something it may not use, is changed
 * here, in the open.
 *
 * <p>The uses are read from the sources, not from the class files: the compiler copies a constant
 * such as a recipe map name into the class that uses it, and that leaves nothing in the bytecode.
 * A use of a class of another package needs an import or a qualified name, so both are read, in
 * code and in comments.
 */
final class PackageRules {
    static final String BASE = "com.lombinaxmasson.gt6ktfruaddon";
    static final String KTFRU = "Ktfru";
    static final String ENTRY = "KtfruAddon";

    /** The packages below the root that are not features. */
    private static final Set<String> NOT_FEATURES =
            Set.of("core", "recipe", "content", "generated", "registry", "client", "event");

    /** The features that use another feature, and which. */
    private static final Map<String, Set<String>> FEATURE_USES = Map.of("research", Set.of("network"));

    private static final Pattern PACKAGE = Pattern.compile("^package\\s+([\\w.]+);", Pattern.MULTILINE);
    private static final Pattern QUALIFIED = Pattern.compile(Pattern.quote(BASE) + "\\.([A-Za-z_][\\w.]*)");

    private PackageRules() {}

    /** One mention of a class of the addon: in which file, from which node, of which node, how it is written. */
    record Use(String file, String from, String to, String name) {}

    /** The directory of the main sources that holds the root package. */
    static Path sources() {
        Path here = Path.of("").toAbsolutePath();
        for (Path dir = here; dir != null; dir = dir.getParent()) {
            Path root = dir.resolve("src/main/java").resolve(BASE.replace('.', '/'));
            if (Files.isDirectory(root)) return root;
        }
        throw new IllegalStateException("src/main/java is not in " + here + " or above");
    }

    /** The packages directly below the root that are features. */
    static Set<String> featuresIn(Path root) {
        try (Stream<Path> children = Files.list(root)) {
            Set<String> features = new TreeSet<>();
            children.filter(Files::isDirectory).forEach(path -> features.add(path.getFileName().toString()));
            features.removeAll(NOT_FEATURES);
            return features;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** The classes that sit in the root package itself. */
    static Set<String> rootClasses(Path root) {
        try (Stream<Path> children = Files.list(root)) {
            Set<String> classes = new TreeSet<>();
            children.map(path -> path.getFileName().toString()).filter(name -> name.endsWith(".java"))
                    .forEach(name -> classes.add(name.substring(0, name.length() - ".java".length())));
            return classes;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Every use in every source file below the root. */
    static List<Use> usesInSources(Path root) {
        try (Stream<Path> files = Files.walk(root)) {
            List<Use> uses = new ArrayList<>();
            for (Path file : files.filter(path -> path.toString().endsWith(".java")).sorted().toList()) {
                String name = root.relativize(file).toString().replace('\\', '/');
                uses.addAll(usesIn(name, Files.readString(file)));
            }
            return uses;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * The uses in one source file, in the order they are written. {@code file} is the path below the
     * root package, which tells the class when the file is in the root package.
     */
    static List<Use> usesIn(String file, String source) {
        Matcher declared = PACKAGE.matcher(source);
        if (!declared.find()) throw new IllegalArgumentException(file + " has no package line");
        String pkg = declared.group(1);
        String from;
        if (pkg.equals(BASE)) {
            from = file.substring(file.lastIndexOf('/') + 1, file.length() - ".java".length());
        } else if (pkg.startsWith(BASE + ".")) {
            from = firstSegment(pkg.substring(BASE.length() + 1));
        } else {
            throw new IllegalArgumentException(file + " is in package " + pkg + ", outside " + BASE);
        }
        // the file's own package line is not a use
        String rest = source.substring(0, declared.start()) + source.substring(declared.end());
        List<Use> uses = new ArrayList<>();
        Matcher mention = QUALIFIED.matcher(rest);
        while (mention.find()) {
            uses.add(new Use(file, from, firstSegment(mention.group(1)), BASE + "." + mention.group(1)));
        }
        return uses;
    }

    private static String firstSegment(String dotted) {
        int dot = dotted.indexOf('.');
        return dot < 0 ? dotted : dotted.substring(0, dot);
    }

    /** What a node may use besides itself and Ktfru. {@code features} are all the feature packages. */
    static Set<String> mayUse(String node, Set<String> features) {
        return switch (node) {
            case KTFRU -> Set.of();
            case "core" -> Set.of();
            case "recipe", "content" -> Set.of("core");
            case "generated" -> Set.of("core", "recipe");
            case "registry" -> {
                Set<String> all = new TreeSet<>(features);
                all.addAll(Set.of("core", "recipe", "generated", "content"));
                yield all;
            }
            case "client", "event" -> Set.of("core", "registry");
            case ENTRY -> Set.of("core", "recipe", "generated", "content", "registry");
            default -> {
                Set<String> feature = new TreeSet<>(Set.of("core", "recipe"));
                feature.addAll(FEATURE_USES.getOrDefault(node, Set.of()));
                yield feature;
            }
        };
    }

    /** The uses the rules do not allow, one line each. */
    static List<String> violations(Collection<Use> uses, Set<String> features) {
        List<String> found = new ArrayList<>();
        for (Use use : uses) {
            if (use.from().equals(use.to()) || use.to().equals(KTFRU)) continue;
            Set<String> allowed = mayUse(use.from(), features);
            if (!allowed.contains(use.to())) {
                Set<String> shown = new TreeSet<>(allowed);
                shown.add(KTFRU);
                found.add(use.file() + ": " + use.from() + " may not use " + use.to() + " (" + use.name()
                        + "), it may use " + shown);
            }
        }
        return found;
    }

    /** The groups of nodes that use each other in a circle, from a map of node to the nodes it uses. */
    static List<List<String>> cycles(Map<String, Set<String>> uses) {
        Map<String, Set<String>> reach = new TreeMap<>();
        uses.forEach((node, targets) -> reach.computeIfAbsent(node, key -> new TreeSet<>()).addAll(targets));
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Set<String> targets : reach.values()) {
                for (String target : List.copyOf(targets)) {
                    changed |= targets.addAll(reach.getOrDefault(target, Set.of()));
                }
            }
        }
        List<List<String>> circles = new ArrayList<>();
        Set<String> grouped = new TreeSet<>();
        for (Map.Entry<String, Set<String>> entry : reach.entrySet()) {
            String node = entry.getKey();
            if (!entry.getValue().contains(node) || grouped.contains(node)) continue;
            List<String> circle = new ArrayList<>();
            for (String other : entry.getValue()) {
                if (reach.getOrDefault(other, Set.of()).contains(node)) circle.add(other);
            }
            grouped.addAll(circle);
            circles.add(circle);
        }
        return circles;
    }
}
