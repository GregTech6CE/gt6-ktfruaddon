package com.lombinaxmasson.gt6ktfruaddon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.JsonParser;

/** Reads the addon's own resources from the test classpath. */
final class Resources {
    /** The mod id as a literal, so the tests never load the {@code @Mod} class. */
    static final String NAMESPACE = "gt6ktfruaddon";

    private Resources() {}

    static boolean exists(String path) {
        return Resources.class.getResource("/" + path) != null;
    }

    static Set<String> langKeys(String locale) {
        String path = "/assets/" + NAMESPACE + "/lang/" + locale + ".json";
        try (var in = Resources.class.getResourceAsStream(path)) {
            if (in == null) throw new IllegalStateException("missing " + path);
            var json = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            return new TreeSet<>(json.keySet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
