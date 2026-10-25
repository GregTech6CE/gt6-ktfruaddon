package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/** Machine indexes must live behind the lifecycle, never in an unmanaged static collection. */
class BehaviorMemoryContractTest {
    @Test void featuresHaveNoUnmanagedStaticCollections() throws Exception {
        Pattern field = Pattern.compile("static\\s+(?:final\\s+)?(?:[\\w.]+\\s*<[^;={}()]+>)\\s+\\w+\\s*=\\s*new\\s+(?:[\\w]+\\.)*(?:HashMap|HashSet|ArrayList|LinkedHashMap|ConcurrentHashMap)\\b");
        List<String> violations = new ArrayList<>();
        try (var paths = Files.walk(PackageRules.sources())) {
            for (var path : paths.filter(p -> p.toString().endsWith(".java")).toList()) {
                String relative = PackageRules.sources().relativize(path).toString().replace('\\','/');
                if (!relative.contains("/") || relative.startsWith("core/") || relative.startsWith("registry/")
                        || relative.startsWith("generated/") || relative.startsWith("content/")
                        || relative.startsWith("event/") || relative.startsWith("client/")) continue;
                if (field.matcher(Files.readString(path)).find()) violations.add(relative);
            }
        }
        assertEquals(List.of(), violations, "use a lifecycle-managed registry for transient state");
    }
}
