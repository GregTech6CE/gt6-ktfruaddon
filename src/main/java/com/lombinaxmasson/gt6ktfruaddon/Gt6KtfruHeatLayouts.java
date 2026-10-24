package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.LayoutRepeat;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * Card 12 layouts. Layer strings are the source order: the string index is X and
 * the character index is Z. {@code right = -offsetX - row}, {@code up = offsetY + layer},
 * {@code back = offsetZ + column}. Mapped sun and mantle grids use that same axis order.
 */
public final class Gt6KtfruHeatLayouts {
    private Gt6KtfruHeatLayouts() {}

    public static StructureLayout sunHeater(String name) {
        // One absorb layer. The source repeats that layer until it fails, then caps with the top.
        return Gt6KtfruLayerLayout.build(name, -2, -1, 0, new String[][] {
                {"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"},
                {"WWWWW", "WWWWW", " WWWW", "WWWWW", "WWWWW"},
                {"AAAAA", "AHHHA", "AHWHA", "AHHHA", "AAAAA"},
                {"TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT"}
        },
                Gt6KtfruLayerLayout.part('W', "tank_wall", StructureRole.FLUID_INPUT),
                Gt6KtfruLayerLayout.part('A', "sunlight_absorber", StructureRole.CASING),
                Gt6KtfruLayerLayout.part('H', "heat_pipe", StructureRole.CASING),
                Gt6KtfruLayerLayout.part('T', "heat_pipe_top", StructureRole.FLUID_OUTPUT));
    }

    public static StructureLayout sunMirror(String name) {
        return Gt6KtfruLayerLayout.build(name, -1, 0, -1, new String[][] {
                {"WWW", "W W", "WWW"},
                {"   ", " W ", "   "},
                {"MMM", "MMM", "MMM"}
        },
                Gt6KtfruLayerLayout.part('W', "large_sun_mirror_frame", StructureRole.CASING),
                Gt6KtfruLayerLayout.part('M', "large_sun_mirror", StructureRole.CASING));
    }

    public static StructureLayout tidal(String name) {
        return Gt6KtfruLayerLayout.build(name, -2, -3, -1, new String[][] {
                {
                        "  LLLLLL",
                        "  LLLLLL",
                        "  LLLLLL",
                        "  LLLLLL",
                        "  LLLLLL"
                },
                {
                        "OOLLLLLL",
                        "OWWBBBLL",
                        "OWWBBBLL",
                        "OWWBBBLL",
                        "OOLLLLLL"
                },
                {
                        "OOLLLLLL",
                        "OWWWWBLL",
                        "OWAAABLL",
                        "OWWWWBLL",
                        "OOLLLLLL"
                },
                {
                        "OOLLLLLL",
                        "OWWWWBLL",
                        "O AAABLL",
                        "OWWWWBLL",
                        "OOLLLLLL"
                }
        },
                Gt6KtfruLayerLayout.part('B', "tidal_blade", StructureRole.CASING),
                Gt6KtfruLayerLayout.part('W', "tank_wall", StructureRole.CASING),
                Gt6KtfruLayerLayout.air('A'),
                Gt6KtfruLayerLayout.block('O', "minecraft:stone", StructureRole.CASING),
                Gt6KtfruLayerLayout.block('L', "minecraft:water", StructureRole.CASING));
    }

    public static StructureLayout oilMiner(String name) {
        return Gt6KtfruLayerLayout.build(name, -1, 0, 0, new String[][] {
                {"WAA", " AA", "WAA"},
                {"WOO", "OOO", "WOO"}
        },
                Gt6KtfruLayerLayout.part('A', "oil_miner_drill", StructureRole.CASING),
                Gt6KtfruLayerLayout.part('W', "tank_wall", StructureRole.ITEM_FLUID_ENERGY_INPUT),
                Gt6KtfruLayerLayout.part('O', "tank_wall", StructureRole.ITEM_FLUID_OUTPUT));
    }

    public static StructureLayout mantle(String name) {
        var layout = Gt6KtfruLayerLayout.builder(name, -2, -1, 0, new String[][] {
                {"WWWWW", "WWWWW", "WWDWW", "WWWWW", "WWWWW"},
                {"WWWWW", "WHHHW", " HHHW", "WHHHW", "WWWWW"},
                {" WWW ", "WHHHW", "WHHHW", "WHHHW", " WWW "},
                {"  W  ", " WTW ", "WTTTW", " WTW ", "  W  "}
        },
                Gt6KtfruLayerLayout.part('W', "tungsten_wall", StructureRole.ITEM_FLUID_ENERGY_INPUT),
                Gt6KtfruLayerLayout.part('H', "heat_pipe", StructureRole.CASING),
                Gt6KtfruLayerLayout.part('T', "heat_pipe_top", StructureRole.FLUID_OUTPUT),
                Gt6KtfruLayerLayout.part('D', "mantle_drill_rod", StructureRole.CASING));
        // The source keeps drilling the rod column down to bedrock; the first
        // copy below the fixed drill cell repeats until the world stops it.
        layout.repeat(LayoutRepeat.builder("drill_rod", -2, 1, -1).step(-1)
                .cell(0, 0, 2, CellMatch.part("mantle_drill_rod"), StructureRole.CASING)
                .build());
        return layout.build();
    }
}
