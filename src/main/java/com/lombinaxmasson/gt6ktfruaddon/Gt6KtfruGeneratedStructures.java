package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.generated.Gt6KtfruGeneratedLayouts;

/** The layouts and parts that tools/gen_card20_layouts.py transcribes from the source mod. */
public final class Gt6KtfruGeneratedStructures implements Feature {
    @Override
    public List<StructureLayout> layouts() {
        List<StructureLayout> layouts = new ArrayList<>();
        Gt6KtfruGeneratedLayouts.addLayouts(layouts);
        return List.copyOf(layouts);
    }

    @Override
    public List<LayoutPart> parts() {
        List<LayoutPart> parts = new ArrayList<>();
        Gt6KtfruGeneratedLayouts.addParts(parts);
        return List.copyOf(parts);
    }
}
