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

    public static StructureLayout fuelDeburn(String name) {
        return Gt6KtfruLayerLayout.build(name, -6, -1, -1, new String[][] {
                layerA(), layerB(), layerC(), layerD(), layerC(), layerE(), layerF(), layerG(),
                layerH(), layerI(), layerJ(), layerK(), layerL(), layerM(), layerN(), layerO(),
                layerP(), layerQ(), layerQ()
        },
                Gt6KtfruLayerLayout.part('F', "enhanced_frame", StructureRole.ITEM_FLUID_ENERGY_INPUT),
                Gt6KtfruLayerLayout.part('G', "enhanced_frame", StructureRole.ITEM_FLUID_OUTPUT),
                Gt6KtfruLayerLayout.part('W', "tank_wall", StructureRole.ITEM_FLUID_INPUT),
                Gt6KtfruLayerLayout.part('P', "chemical_reactor_pipe", StructureRole.CASING));
    }

    private static String[] layerA() {
        return new String[] {
                " FFFFF  FGF",
                " FWWWF  FFF",
                " FWWWF  FFF",
                " FWWWF  FFF",
                " FFFFF  FFF",
                " F      FFF",
                " F      FFF",
                " FFFFFFFFFF",
                " FWWWWF FFF",
                " FWWWWF FFF",
                " FWWWWF FFF",
                " FWWWWF FFF",
                " FFFFFF FFF",
                "           "
        };
    }

    private static String[] layerB() {
        return new String[] {
                " F   F   P ",
                "  WWW      ",
                "  W W   WPW",
                "  WWW    P ",
                " F F F  WPW",
                " F       P ",
                "        WPW",
                " FWWWWF  P ",
                " W    W WPW",
                " W    W  P ",
                " W    W WPW",
                " W    W  P ",
                " FWWWWF WPW",
                "    PPPPPP "
        };
    }

    private static String[] layerC() {
        return new String[] {
                " F FFF   P ",
                " FWWW      ",
                " FW WF  W W",
                "  WWWF     ",
                " FFPFF  W W",
                "   P       ",
                "   P    W W",
                " FWWWWF    ",
                " W    W W W",
                " W    W    ",
                " W    W W W",
                " W    W    ",
                " FWWWWF W W",
                "           "
        };
    }

    private static String[] layerD() {
        return new String[] {
                " F   F   P ",
                "  WWW      ",
                "  W W   W W",
                "  WWW      ",
                " F F F  W W",
                "           ",
                "        W W",
                " FWWWWF    ",
                " W    W W W",
                " W    W    ",
                " W    W W W",
                " W    W    ",
                " FWWWWF W W",
                "           "
        };
    }

    private static String[] layerE() {
        return new String[] {
                " F   F   P ",
                "  WWW      ",
                "  W W   W W",
                "  WWW      ",
                " F F F  W W",
                "           ",
                "        W W",
                " FWWWWF    ",
                " W    W W W",
                " W    W    ",
                "PW    W W W",
                "PW    W    ",
                "PFWWWWF W W",
                "PPP P      "
        };
    }

    private static String[] layerF() {
        return new String[] {
                " F FFF   P ",
                " FWWW    P ",
                " FW WF  WPW",
                "  WWWF   P ",
                " FFPFF  WPW",
                "   P     P ",
                "   P    WPW",
                " FWWWWF  P ",
                " W    W WPW",
                " W    W  P ",
                " W    W WPW",
                " W    W  P ",
                " FWWWWF WPW",
                "    P      "
        };
    }

    private static String[] layerG() {
        return new String[] {
                " F   F     ",
                "  WWW      ",
                "  W W   FFF",
                "  WWW   FFF",
                " F F F  FFF",
                "        FFF",
                "        FFF",
                " FWWWWF FFF",
                " W    W FFF",
                " W    W FFF",
                "PW    W FFF",
                "PW    W FFF",
                "PFWWWWF FFF",
                "PPP P      "
        };
    }

    private static String[] layerH() {
        return new String[] {
                " FFFFF     ",
                " FWWWF     ",
                " FWWWF     ",
                " FWWWF     ",
                " FFFFF     ",
                "           ",
                "           ",
                " FWWWWF    ",
                " W    W    ",
                " W    W    ",
                " W    W    ",
                " W    W    ",
                " FWWWWF    ",
                "    P      "
        };
    }

    private static String[] layerI() {
        return new String[] {
                "           ",
                "    P      ",
                "           ",
                "  P        ",
                "           ",
                "           ",
                "           ",
                " FWWWWF    ",
                " W    W    ",
                " W    W    ",
                "PW    W    ",
                "PW    W    ",
                "PFWWWWF    ",
                "PPP P      "
        };
    }

    private static String[] layerJ() {
        return new String[] {
                "           ",
                "    P      ",
                "           ",
                "  P        ",
                "           ",
                "           ",
                "    PPPP   ",
                " FWWWWFP   ",
                " W    WP   ",
                " W    WP   ",
                " W    W    ",
                " W    W    ",
                " FWWWWF    ",
                "    P      "
        };
    }

    private static String[] layerK() {
        return new String[] {
                "           ",
                "    P      ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                " FWWWWF    ",
                " W    W    ",
                " W    W    ",
                "PW    W    ",
                "PW    W    ",
                "PFWWWWF    ",
                "PPP P      "
        };
    }

    private static String[] layerL() {
        return new String[] {
                "           ",
                "    P      ",
                "           ",
                "           ",
                "           ",
                "           ",
                "    PPPP   ",
                " FWWWWFP   ",
                " W    WP   ",
                " W    WP   ",
                " W    W    ",
                " W    W    ",
                " FWWWWF    ",
                "    P      "
        };
    }

    private static String[] layerM() {
        return new String[] {
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                " FWWWWF    ",
                " W    W    ",
                " W    W    ",
                " W    W    ",
                " W    W    ",
                " FWWWWF    ",
                "           "
        };
    }

    private static String[] layerN() {
        return new String[] {
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                " FFFFFF    ",
                " FWWWWF    ",
                " FWWWWF    ",
                " FWWWWF    ",
                " FWWWWF    ",
                " FFFFFF    ",
                "           "
        };
    }

    private static String[] layerO() {
        return new String[] {
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "  P        ",
                "    P      ",
                "           ",
                "     P     ",
                "           ",
                "           "
        };
    }

    private static String[] layerP() {
        return new String[] {
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "  P        ",
                "    P      ",
                "           ",
                "           ",
                "           ",
                "           "
        };
    }

    private static String[] layerQ() {
        return new String[] {
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           ",
                "  P        ",
                "           ",
                "           ",
                "           ",
                "           ",
                "           "
        };
    }
}
