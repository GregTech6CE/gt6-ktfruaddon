package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.CompressedGasTankDeclaration;
import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.RecipeMachine;

/**
 * Adding a machine used to mean remembering a blockstate, an item model and two lang entries.
 * These tests turn that checklist into a failure.
 */
class ResourceContractTest {
    private static final String EN = "en_us";
    private static final String ZH = "zh_cn";

    @Test
    void langFilesHaveTheSameKeys() {
        Set<String> en = Resources.langKeys(EN);
        Set<String> zh = Resources.langKeys(ZH);
        assertEquals(Set.of(), difference(en, zh), "keys only in " + EN);
        assertEquals(Set.of(), difference(zh, en), "keys only in " + ZH);
    }

    @Test
    void everyMachineHasABlockstateAnItemModelAndANameInBothLanguages() {
        List<String> missing = new ArrayList<>();
        for (AddonMachine machine : Declarations.machines()) {
            requireBlockResources(missing, machine.casing().id(), true);
        }
        assertEquals(List.of(), missing);
    }

    @Test
    void everyLayoutPartAndGasDrumHasANameInBothLanguages() {
        List<String> missing = new ArrayList<>();
        for (LayoutPart part : Declarations.parts()) {
            requireBlockResources(missing, part.name(), false);
        }
        for (CompressedGasTankDeclaration tank : Declarations.gasTanks()) {
            requireBlockResources(missing, tank.id(), false);
        }
        assertEquals(List.of(), missing);
    }

    @Test
    void structureHintLinesExistInBothLanguages() {
        Set<String> en = Resources.langKeys(EN);
        Set<String> zh = Resources.langKeys(ZH);
        List<String> missing = new ArrayList<>();
        for (AddonMachine machine : Declarations.machines()) {
            if (machine instanceof RecipeMachine recipe) {
                for (String key : recipe.structureKeys()) {
                    if (!en.contains(key)) missing.add(key + " (" + EN + ")");
                    if (!zh.contains(key)) missing.add(key + " (" + ZH + ")");
                }
            }
        }
        assertEquals(List.of(), missing);
    }

    private static void requireBlockResources(List<String> missing, String id, boolean modelsToo) {
        String ns = Resources.NAMESPACE;
        if (modelsToo) {
            if (!Resources.exists("assets/" + ns + "/blockstates/" + id + ".json")) missing.add(id + ": blockstate");
            if (!Resources.exists("assets/" + ns + "/models/item/" + id + ".json")) missing.add(id + ": item model");
        }
        String key = "block." + ns + "." + id;
        if (!Resources.langKeys(EN).contains(key)) missing.add(id + ": " + EN + " name");
        if (!Resources.langKeys(ZH).contains(key)) missing.add(id + ": " + ZH + " name");
    }

    private static Set<String> difference(Set<String> left, Set<String> right) {
        Set<String> result = new TreeSet<>(left);
        result.removeAll(right);
        return result;
    }
}
