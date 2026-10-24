package com.lombinaxmasson.gt6ktfruaddon.power;

import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * Card 13 layouts. Axis Y keeps the card 12 mapping: row is X, column is Z,
 * {@code right = -offsetX - row}, {@code up = offsetY + layer}, {@code back = offsetZ + column}.
 * The fluid boiler expands on Z, so column is X and the layer index is Z:
 * {@code right = -offsetX - column}, {@code up = offsetY + row}, {@code back = offsetZ + layer}.
 * The controller occupies the cell that lands on 0,0,0 and is not placed.
 */
public final class TurbineLayouts {
    private static final String[][] TURBINE = {
            {"AAAA", "AAAC", "AAAA"},
            {"AAAA", "AAAB", "AAAA"},
            {"AAAA", "AAAA", "AAAA"}
    };

    private TurbineLayouts() {}

    /** 3×3×4 shell. Offset -1,-1,0. B is the back-center energy hatch, C the bottom output. */
    public static StructureLayout turbine(String name, String shell) {
        var layout = StructureLayout.builder(name);
        for (int layer = 0; layer < TURBINE.length; layer++) {
            String[] rows = TURBINE[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int column = 0; column < line.length(); column++) {
                    int right = 1 - row;
                    int up = layer - 1;
                    int back = column;
                    if (right == 0 && up == 0 && back == 0) continue;
                    char mark = line.charAt(column);
                    StructureRole role = switch (mark) {
                        case 'B' -> StructureRole.ENERGY_OUTPUT;
                        case 'C' -> StructureRole.ITEM_FLUID_OUTPUT;
                        default -> StructureRole.ITEM_FLUID_IO;
                    };
                    layout.cell(right, up, back, CellMatch.part(shell), role);
                }
            }
        }
        return layout.build();
    }

    /** Two 2×3 layers of one wall. Offset 0,0,0. The corner is the controller. */
    public static StructureLayout motor(String name, String wall) {
        var layout = StructureLayout.builder(name);
        for (int layer = 0; layer < 2; layer++) {
            for (int row = 0; row < 2; row++) {
                for (int column = 0; column < 3; column++) {
                    int right = -row;
                    int up = layer;
                    int back = column;
                    if (right == 0 && up == 0 && back == 0) continue;
                    layout.cell(right, up, back, CellMatch.part(wall), StructureRole.ITEM_FLUID_ENERGY);
                }
            }
        }
        return layout.build();
    }

    /**
     * Two 3×3 walls with two 3×3 coil slices between them.
     * The back-center wall is the energy hatch.
     */
    public static StructureLayout dynamo(String name, String shell) {
        var layout = StructureLayout.builder(name);
        for (int back = 0; back < 4; back++) {
            for (int up = -1; up <= 1; up++) {
                for (int right = -1; right <= 1; right++) {
                    if (right == 0 && up == 0 && back == 0) continue;
                    if (back == 1 || back == 2) {
                        layout.cell(right, up, back, CellMatch.part("large_copper_coil"), StructureRole.CASING);
                    } else if (back == 3 && right == 0 && up == 0) {
                        layout.cell(right, up, back, CellMatch.part(shell), StructureRole.ENERGY_OUTPUT);
                    } else {
                        layout.cell(right, up, back, CellMatch.part(shell), StructureRole.CASING);
                    }
                }
            }
        }
        return layout.build();
    }

    /**
     * Five 5×5 layers, offset -2,-2,0. Spaces in the middle layer are not cells.
     * The controller is the first space of {@code " WWWO"}.
     */
    public static StructureLayout excite(String name) {
        var layout = StructureLayout.builder(name);
        String[] wall = {"WCCCW", "WCCCW", "WCCCW", "WCCCW", "WCCCW"};
        String[] middle = {"WCCCW", "WCCCW", " WWWO", "WCCCW", "WCCCW"};
        String[][] layers = {wall, wall, middle, wall, wall};
        for (int layer = 0; layer < layers.length; layer++) {
            String[] rows = layers[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int column = 0; column < line.length(); column++) {
                    char mark = line.charAt(column);
                    if (mark == ' ') continue;
                    int right = 2 - row;
                    int up = layer - 2;
                    int back = column;
                    if (right == 0 && up == 0 && back == 0) continue;
                    String part = mark == 'C' ? "large_copper_coil" : "dense_steel_wall";
                    StructureRole role = mark == 'O' ? StructureRole.ENERGY_OUTPUT : StructureRole.CASING;
                    layout.cell(right, up, back, CellMatch.part(part), role);
                }
            }
        }
        return layout.build();
    }

    /**
     * One middle slice. The source repeats that slice up to 8 times.
     * Axis Z, offset -2,0,0. The controller replaces the front-middle F.
     */
    public static StructureLayout boiler(String name) {
        var layout = StructureLayout.builder(name);
        String[][] layers = {
                {"DFFFE", "FFFFF", "FFFFF", "DFFFE"},
                {"ACCCA", "BCCCB", "BCCCB", "BCCCB"},
                {"ACCCA", "BCCCB", "ACCCA", "BCCCB"},
                {"ACCCA", "ACCCA", "ACCCA", "BCCCB"}
        };
        for (int layer = 0; layer < layers.length; layer++) {
            String[] rows = layers[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int column = 0; column < line.length(); column++) {
                    char mark = line.charAt(column);
                    if (mark == ' ') continue;
                    int right = 2 - column;
                    int up = row;
                    int back = layer;
                    if (right == 0 && up == 0 && back == 0) continue;
                    layout.cell(right, up, back, CellMatch.part(boilerPart(mark)), boilerRole(mark));
                }
            }
        }
        return layout.build();
    }

    private static String boilerPart(char mark) {
        return switch (mark) {
            case 'A' -> "boiler_heat_pipe";
            case 'B' -> "pressure_pipe";
            case 'C' -> "high_pressure_exchanger";
            default -> "aluminium_wall";
        };
    }

    private static StructureRole boilerRole(char mark) {
        return switch (mark) {
            case 'D' -> StructureRole.ITEM_FLUID_INPUT;
            case 'E' -> StructureRole.ITEM_FLUID_OUTPUT;
            default -> StructureRole.CASING;
        };
    }
}
