package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.gregtech.gregtech.api.addon.MaterialDeclaration;
import com.gregtech.gregtech.api.addon.PrefixDeclaration;

class MaterialContractTest {
    @Test
    void materialsBuildAndHaveUniqueNames() {
        assertFalse(Declarations.materials().isEmpty(), "no materials declared");
        Set<String> seen = new TreeSet<>();
        List<String> duplicates = new ArrayList<>();
        for (MaterialDeclaration material : Declarations.materials()) {
            if (!seen.add(material.name())) duplicates.add(material.name());
        }
        assertEquals(List.of(), duplicates);
    }

    @Test
    void prefixesHaveUniqueNames() {
        Set<String> seen = new TreeSet<>();
        List<String> duplicates = new ArrayList<>();
        for (PrefixDeclaration prefix : Declarations.prefixes()) {
            if (!seen.add(prefix.name())) duplicates.add(prefix.name());
        }
        assertEquals(List.of(), duplicates);
    }
}
