package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * Research assembler. Axis Y, offset 0,0,0. Spaces are not cells.
 * {@code right = -row}, {@code up = layer}, {@code back = column}.
 * The controller is the space in the first row.
 */
public final class Gt6KtfruResearchLayouts {
    private static final String[][] LAYERS = {
            {" W", "TW", "WW"},
            {" W", " W", " W"}
    };

    private Gt6KtfruResearchLayouts() {}

    public static StructureLayout assembler(String name) {
        var layout = StructureLayout.builder(name);
        for (int layer = 0; layer < LAYERS.length; layer++) {
            String[] rows = LAYERS[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int column = 0; column < line.length(); column++) {
                    char mark = line.charAt(column);
                    if (mark == ' ') continue;
                    int right = -row;
                    int up = layer;
                    int back = column;
                    if (right == 0 && up == 0 && back == 0) continue;
                    if (mark == 'T') {
                        layout.cell(right, up, back, CellMatch.block("minecraft:crafting_table"), StructureRole.CASING);
                    } else {
                        layout.cell(right, up, back, CellMatch.part("steel_wall"), StructureRole.ITEM_FLUID_ENERGY);
                    }
                }
            }
        }
        return layout.build();
    }
}
