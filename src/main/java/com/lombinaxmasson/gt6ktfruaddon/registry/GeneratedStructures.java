package com.lombinaxmasson.gt6ktfruaddon.registry;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.LayoutPart;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.generated.GeneratedLayouts;

/** The layouts and parts that tools/gen_card20_layouts.py transcribes from the source mod. */
public final class GeneratedStructures implements Feature {
    @Override
    public List<StructureLayout> layouts() {
        List<StructureLayout> layouts = new ArrayList<>();
        GeneratedLayouts.addLayouts(layouts);
        return List.copyOf(layouts);
    }

    @Override
    public List<LayoutPart> parts() {
        List<LayoutPart> parts = new ArrayList<>();
        GeneratedLayouts.addParts(parts);
        return List.copyOf(parts);
    }
}
