package com.lombinaxmasson.gt6ktfruaddon.heat;

import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.LayoutRepeat;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;
import com.lombinaxmasson.gt6ktfruaddon.core.layout.LayerLayout;

/**
 * Card 12 layouts. Layer strings are the source order: the string index is X and
 * the character index is Z. {@code right = -offsetX - row}, {@code up = offsetY + layer},
 * {@code back = offsetZ + column}. Mapped sun and mantle grids use that same axis order.
 */
public final class HeatLayouts {
    private HeatLayouts() {}

    public static StructureLayout sunHeater(String name) {
        // One absorb layer. The source repeats that layer until it fails, then caps with the top.
        return LayerLayout.build(name, -2, -1, 0, new String[][] {
                {"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"},
                {"WWWWW", "WWWWW", " WWWW", "WWWWW", "WWWWW"},
                {"AAAAA", "AHHHA", "AHWHA", "AHHHA", "AAAAA"},
                {"TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT"}
        },
                LayerLayout.part('W', "tank_wall", StructureRole.FLUID_INPUT),
                LayerLayout.part('A', "sunlight_absorber", StructureRole.CASING),
                LayerLayout.part('H', "heat_pipe", StructureRole.CASING),
                LayerLayout.part('T', "heat_pipe_top", StructureRole.FLUID_OUTPUT));
    }

    public static StructureLayout sunMirror(String name) {
        return LayerLayout.build(name, -1, 0, -1, new String[][] {
                {"WWW", "W W", "WWW"},
                {"   ", " W ", "   "},
                {"MMM", "MMM", "MMM"}
        },
                LayerLayout.part('W', "large_sun_mirror_frame", StructureRole.CASING),
                LayerLayout.part('M', "large_sun_mirror", StructureRole.CASING));
    }

    public static StructureLayout tidal(String name) {
        return LayerLayout.build(name, -2, -3, -1, new String[][] {
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
                LayerLayout.part('B', "tidal_blade", StructureRole.CASING),
                LayerLayout.part('W', "tank_wall", StructureRole.CASING),
                LayerLayout.air('A'),
                LayerLayout.block('O', "minecraft:stone", StructureRole.CASING),
                LayerLayout.block('L', "minecraft:water", StructureRole.CASING));
    }

    public static StructureLayout oilMiner(String name) {
        return LayerLayout.build(name, -1, 0, 0, new String[][] {
                {"WAA", " AA", "WAA"},
                {"WOO", "OOO", "WOO"}
        },
                LayerLayout.part('A', "oil_miner_drill", StructureRole.CASING),
                LayerLayout.part('W', "tank_wall", StructureRole.ITEM_FLUID_ENERGY_INPUT),
                LayerLayout.part('O', "tank_wall", StructureRole.ITEM_FLUID_OUTPUT));
    }

    public static StructureLayout mantle(String name) {
        var layout = LayerLayout.builder(name, -2, -1, 0, new String[][] {
                {"WWWWW", "WWWWW", "WWDWW", "WWWWW", "WWWWW"},
                {"WWWWW", "WHHHW", " HHHW", "WHHHW", "WWWWW"},
                {" WWW ", "WHHHW", "WHHHW", "WHHHW", " WWW "},
                {"  W  ", " WTW ", "WTTTW", " WTW ", "  W  "}
        },
                LayerLayout.part('W', "tungsten_wall", StructureRole.ITEM_FLUID_ENERGY_INPUT),
                LayerLayout.part('H', "heat_pipe", StructureRole.CASING),
                LayerLayout.part('T', "heat_pipe_top", StructureRole.FLUID_OUTPUT),
                LayerLayout.part('D', "mantle_drill_rod", StructureRole.CASING));
        // The source keeps drilling the rod column down to bedrock; the first
        // copy below the fixed drill cell repeats until the world stops it.
        layout.repeat(LayoutRepeat.builder("drill_rod", -2, 1, -1).step(-1)
                .cell(0, 0, 2, CellMatch.part("mantle_drill_rod"), StructureRole.CASING)
                .build());
        return layout.build();
    }
}
