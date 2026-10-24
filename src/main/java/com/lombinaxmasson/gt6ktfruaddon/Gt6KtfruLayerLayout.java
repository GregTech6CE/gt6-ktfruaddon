package com.lombinaxmasson.gt6ktfruaddon;

import java.util.HashMap;
import java.util.Map;

import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * Axis Y layouts transcribed from kTFRU layer strings.
 * {@code right = -offsetX - row}, {@code up = offsetY + layer}, {@code back = offsetZ + column}.
 * Spaces are not cells. The controller occupies 0,0,0.
 */
public final class Gt6KtfruLayerLayout {
    private Gt6KtfruLayerLayout() {}

    public record Spec(char mark, CellMatch match, StructureRole role) {}

    public static StructureLayout build(String name, int offsetX, int offsetY, int offsetZ,
            String[][] layers, Spec... specs) {
        return builder(name, offsetX, offsetY, offsetZ, layers, specs).build();
    }

    /** The cells of {@code layers} in a builder that is not built yet, for a layout that adds more to it. */
    public static StructureLayout.Builder builder(String name, int offsetX, int offsetY, int offsetZ,
            String[][] layers, Spec... specs) {
        Map<Character, Spec> roles = new HashMap<>();
        for (Spec spec : specs) roles.put(spec.mark(), spec);
        var layout = StructureLayout.builder(name);
        for (int layer = 0; layer < layers.length; layer++) {
            String[] rows = layers[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int column = 0; column < line.length(); column++) {
                    char mark = line.charAt(column);
                    if (mark == ' ') continue;
                    int right = -offsetX - row;
                    int up = offsetY + layer;
                    int back = offsetZ + column;
                    if (right == 0 && up == 0 && back == 0) continue;
                    Spec spec = roles.get(mark);
                    if (spec == null)
                        throw new IllegalArgumentException(name + " has no cell for '" + mark + "'");
                    if (spec.match() == null) continue;
                    layout.cell(right, up, back, spec.match(), spec.role());
                }
            }
        }
        return layout;
    }

    public static Spec part(char mark, String name, StructureRole role) {
        return new Spec(mark, CellMatch.part(name), role);
    }

    public static Spec block(char mark, String id, StructureRole role) {
        return new Spec(mark, CellMatch.block(id), role);
    }

    /** Any one of {@code matches}; parts and plain blocks may be mixed. */
    public static Spec oneOf(char mark, StructureRole role, CellMatch... matches) {
        return new Spec(mark, CellMatch.oneOf(matches), role);
    }

    public static Spec air(char mark) {
        return new Spec(mark, CellMatch.air(), StructureRole.AIR);
    }

    /** A controller mark. Cells with this mark are not placed. */
    public static Spec controller(char mark) {
        return new Spec(mark, null, StructureRole.CASING);
    }
}
