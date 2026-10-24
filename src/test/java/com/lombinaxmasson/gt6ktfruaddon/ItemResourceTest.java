package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * Item registration itself needs Minecraft's bootstrap, which only works inside FML, so the
 * registry is covered by the server boot smoke test. This checks the resources the registry leans on.
 */
class ItemResourceTest {
    private static final String ITEM_PREFIX = "item." + Resources.NAMESPACE + ".";
    private static final String BLOCK_PREFIX = "block." + Resources.NAMESPACE + ".";

    @Test
    void everyItemModelHasANameInBothLanguages() throws IOException, URISyntaxException {
        Path models = Path.of(ItemResourceTest.class
                .getResource("/assets/" + Resources.NAMESPACE + "/models/item").toURI());
        Set<String> en = Resources.langKeys("en_us");
        Set<String> zh = Resources.langKeys("zh_cn");
        List<String> missing = new ArrayList<>();
        try (Stream<Path> files = Files.list(models)) {
            for (Path file : (Iterable<Path>) files::iterator) {
                String name = file.getFileName().toString();
                if (!name.endsWith(".json")) continue;
                String id = name.substring(0, name.length() - ".json".length());
                if (!named(en, id)) missing.add(id + " (en_us)");
                if (!named(zh, id)) missing.add(id + " (zh_cn)");
            }
        }
        assertEquals(List.of(), missing);
    }

    @Test
    void everyItemNameHasAModel() {
        List<String> missing = new ArrayList<>();
        for (String key : Resources.langKeys("en_us")) {
            if (key.startsWith(ITEM_PREFIX)) {
                String id = key.substring(ITEM_PREFIX.length());
                if (!Resources.exists("assets/" + Resources.NAMESPACE + "/models/item/" + id + ".json")) {
                    missing.add(id);
                }
            }
        }
        assertEquals(List.of(), missing);
    }

    private static boolean named(Set<String> keys, String id) {
        return keys.contains(ITEM_PREFIX + id) || keys.contains(BLOCK_PREFIX + id);
    }
}
