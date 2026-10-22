package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * Wireless battery. Axis Y, offset 0,0,0. Layers AABBBCC.
 * {@code right = -row}, {@code up = layer}, {@code back = column}.
 * The corner of the first layer is the controller.
 */
public final class Gt6KtfruWirelessLayouts {
    private static final String[] A = {"TTTT", "TCCT", "TCCT", "TTTT"};
    private static final String[] B = {"OOOO", "OCCO", "OCCO", "OOOO"};
    private static final String[] C = {"WWWW", "WBBW", "WBBW", "WWWW"};
    private static final String[][] LAYERS = {A, A, B, B, B, C, C};

    private Gt6KtfruWirelessLayouts() {}

    public static StructureLayout battery(String name) {
        var layout = StructureLayout.builder(name);
        for (int layer = 0; layer < LAYERS.length; layer++) {
            String[] rows = LAYERS[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int column = 0; column < line.length(); column++) {
                    int right = -row;
                    int up = layer;
                    int back = column;
                    if (right == 0 && up == 0 && back == 0) continue;
                    char mark = line.charAt(column);
                    String part = switch (mark) {
                        case 'O' -> "large_copper_coil";
                        case 'W' -> "titanium_wall";
                        case 'B' -> "battery_cabin";
                        default -> "magnetic_conductor";
                    };
                    StructureRole role = mark == 'W' ? StructureRole.ENERGY_INPUT : StructureRole.CASING;
                    layout.cell(right, up, back, CellMatch.part(part), role);
                }
            }
        }
        return layout.build();
    }
}
