package com.lombinaxmasson.gt6ktfruaddon;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.lombinaxmasson.gt6ktfruaddon.registry.Features;

/** Exports actual declaration ids for the offline documentation report; no game bootstrap. */
public final class DocumentationInventory {
    private DocumentationInventory() {}

    public static void main(String[] args) throws Exception {
        Path target = Path.of(args[0]);
        Files.createDirectories(target.getParent());
        Files.write(target, Features.get().blockIds().stream().sorted().toList(), StandardCharsets.UTF_8);
    }
}
