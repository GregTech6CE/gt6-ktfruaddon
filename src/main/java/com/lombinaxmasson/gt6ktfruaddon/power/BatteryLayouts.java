package com.lombinaxmasson.gt6ktfruaddon.power;

import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * Card 14 layouts. Axis Y uses the card 12 mapping:
 * {@code right = -row}, {@code up = layer}, {@code back = column}.
 * The controller occupies 0,0,0 and is not placed.
 */
public final class BatteryLayouts {
    private static final String[] LAYER_A = {"TTTT", "TCCT", "TCCT", "TTTT"};
    private static final String[] LAYER_B = {"OOOO", "OCCO", "OCCO", "OOOO"};
    private static final String[] LAYER_C = {"WWWW", "WBBW", "WBBW", "WWWW"};
    private static final String[][] TRANSFORM = {
            LAYER_A, LAYER_A, LAYER_B, LAYER_B, LAYER_B, LAYER_C, LAYER_C
    };

    private BatteryLayouts() {}

    /**
     * Front 3×2 shell, controller in the bottom center, plus one air cell
     * two blocks behind. The source flood-fills a sink up to the registered
     * height and width; this layout is that sink at one cell.
     */
    public static StructureLayout liquid(String name, String shell) {
        var layout = StructureLayout.builder(name);
        layout.cell(1, 0, 0, CellMatch.part(shell), StructureRole.FLUID_INPUT);
        layout.cell(-1, 0, 0, CellMatch.part(shell), StructureRole.FLUID_INPUT);
        layout.cell(1, 1, 0, CellMatch.part(shell), StructureRole.ENERGY_INPUT);
        layout.cell(0, 1, 0, CellMatch.part(shell), StructureRole.ENERGY_INPUT);
        layout.cell(-1, 1, 0, CellMatch.part(shell), StructureRole.ENERGY_INPUT);
        layout.cell(0, 0, 2, CellMatch.air(), StructureRole.AIR);
        CellMatch poolWall = CellMatch.oneOf(
                CellMatch.part("liquid_battery_wall_bronze"),
                CellMatch.part("liquid_battery_wall_steel"),
                CellMatch.part("liquid_battery_wall_plastic"));
        layout.cell(0, -1, 2, poolWall, StructureRole.FLUID_INPUT);
        layout.cell(-1, 0, 2, poolWall, StructureRole.FLUID_INPUT);
        layout.cell(1, 0, 2, poolWall, StructureRole.FLUID_INPUT);
        layout.cell(0, 0, 1, poolWall, StructureRole.FLUID_INPUT);
        layout.cell(0, 0, 3, poolWall, StructureRole.FLUID_INPUT);
        return layout.build();
    }

    /**
     * Seven 4×4 layers, {@code AABBBCC}, controller at the bottom corner.
     * {@code T} is the energy wall. The source also accepts any transformer
     * part in that cell; one cell matches one part.
     */
    public static StructureLayout transform(String name, String wall, String coil,
            String conductor, String cabin) {
        var layout = StructureLayout.builder(name);
        for (int layer = 0; layer < TRANSFORM.length; layer++) {
            String[] rows = TRANSFORM[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int column = 0; column < line.length(); column++) {
                    int right = -row;
                    int up = layer;
                    int back = column;
                    if (right == 0 && up == 0 && back == 0) continue;
                    char mark = line.charAt(column);
                    String part = switch (mark) {
                        case 'O' -> coil;
                        case 'C' -> conductor;
                        case 'B' -> cabin;
                        default -> wall;
                    };
                    StructureRole role = mark == 'T' || mark == 'W'
                            ? StructureRole.ENERGY_INPUT : StructureRole.CASING;
                    layout.cell(right, up, back, CellMatch.part(part), role);
                }
            }
        }
        return layout.build();
    }
}
