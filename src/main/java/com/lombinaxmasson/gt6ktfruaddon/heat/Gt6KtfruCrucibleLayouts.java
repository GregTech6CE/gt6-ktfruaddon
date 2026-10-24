package com.lombinaxmasson.gt6ktfruaddon.heat;

import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * Electromagnetic crucible. Axis Y, offset -2,0,0.
 * {@code right = 2 - row}, {@code up = layer}, {@code back = column}.
 * The controller is the space in the bottom layer.
 */
public final class Gt6KtfruCrucibleLayouts {
    private static final String[] A = {" PPP ", "PXXXP", " XXXP", "PXXXP", " PPP "};
    private static final String[] B = {" XXX ", "XCCCX", "XC CX", "XCCCX", " XXX "};
    private static final String[] C = {" XXX ", "XXXXX", "XXXXX", "XXXXX", " XXX "};
    private static final String[][] LAYERS = {A, B, B, B, C};

    private Gt6KtfruCrucibleLayouts() {}

    public static StructureLayout crucible(String name) {
        var layout = StructureLayout.builder(name);
        for (int layer = 0; layer < LAYERS.length; layer++) {
            String[] rows = LAYERS[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int column = 0; column < line.length(); column++) {
                    char mark = line.charAt(column);
                    if (mark == ' ') continue;
                    int right = 2 - row;
                    int up = layer;
                    int back = column;
                    if (right == 0 && up == 0 && back == 0) continue;
                    if (mark == 'P') {
                        layout.cell(right, up, back, CellMatch.part("crucible_screen"), StructureRole.CASING);
                    } else if (mark == 'C') {
                        layout.cell(right, up, back, CellMatch.part("large_niobium_titanium_coil"), StructureRole.CASING);
                    } else {
                        layout.cell(right, up, back, CellMatch.part("titanium_wall"), StructureRole.ITEM_FLUID_ENERGY_INPUT);
                    }
                }
            }
        }
        return layout.build();
    }
}
