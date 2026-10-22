package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * Card 12 layouts. Layer strings are the source order: the string index is X and
 * the character index is Z. {@code right = -offsetX - row}, {@code up = offsetY + layer},
 * {@code back = offsetZ + column}. Mapped sun and mantle grids use that same axis order.
 */
public final class Gt6KtfruHeatLayouts {
    private Gt6KtfruHeatLayouts() {}

    public static StructureLayout sunHeater() {
        var layout = StructureLayout.builder(Gt6KtfruMachines.SUN_HEATER);
        // One absorb layer. The source repeats that layer until it fails, then caps with the top.
        String[][] grids = {
                {"WWWWW", "WWWWW", "WWWWW", "WWWWW", "WWWWW"},
                {"WWWWW", "WWWWW", " WWWW", "WWWWW", "WWWWW"},
                {"AAAAA", "AHHHA", "AHWHA", "AHHHA", "AAAAA"},
                {"TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT"}
        };
        layers(layout, grids, -2, -1, 0, Gt6KtfruHeatLayouts::sun);
        return layout.build();
    }

    public static StructureLayout sunMirror() {
        var layout = StructureLayout.builder(Gt6KtfruMachines.SUN_MIRROR);
        String[][] grids = {
                {"WWW", "W W", "WWW"},
                {"   ", " W ", "   "},
                {"MMM", "MMM", "MMM"}
        };
        layers(layout, grids, -1, 0, -1, Gt6KtfruHeatLayouts::mirror);
        return layout.build();
    }

    public static StructureLayout tidal() {
        var layout = StructureLayout.builder(Gt6KtfruMachines.TIDAL);
        String[][] grids = {
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
        };
        layers(layout, grids, -2, -3, -1, Gt6KtfruHeatLayouts::tidalCell);
        return layout.build();
    }

    public static StructureLayout oilMiner() {
        var layout = StructureLayout.builder(Gt6KtfruMachines.OIL_MINER);
        String[][] grids = {
                {"WAA", " AA", "WAA"},
                {"WOO", "OOO", "WOO"}
        };
        layers(layout, grids, -1, 0, 0, Gt6KtfruHeatLayouts::oil);
        return layout.build();
    }

    public static StructureLayout mantle() {
        var layout = StructureLayout.builder(Gt6KtfruMachines.MANTLE);
        String[][] grids = {
                {"WWWWW", "WWWWW", "WWDWW", "WWWWW", "WWWWW"},
                {"WWWWW", "WHHHW", " HHHW", "WHHHW", "WWWWW"},
                {" WWW ", "WHHHW", "WHHHW", "WHHHW", " WWW "},
                {"  W  ", " WTW ", "WTTTW", " WTW ", "  W  "}
        };
        layers(layout, grids, -2, -1, 0, Gt6KtfruHeatLayouts::mantleCell);
        return layout.build();
    }

    public static StructureLayout fuelDeburn() {
        var layout = StructureLayout.builder(Gt6KtfruMachines.FUEL_DEBURN);
        String[][] grids = {
                layerA(), layerB(), layerC(), layerD(), layerC(), layerE(), layerF(), layerG(),
                layerH(), layerI(), layerJ(), layerK(), layerL(), layerM(), layerN(), layerO(),
                layerP(), layerQ(), layerQ()
        };
        layers(layout, grids, -6, -1, -1, Gt6KtfruHeatLayouts::deburn);
        return layout.build();
    }

    private static void layers(StructureLayout.Builder layout, String[][] grids,
            int offsetX, int offsetY, int offsetZ, Cell cell) {
        for (int layer = 0; layer < grids.length; layer++) {
            String[] rows = grids[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int col = 0; col < line.length(); col++) {
                    char mark = line.charAt(col);
                    if (mark == ' ') continue;
                    int right = -offsetX - row;
                    int up = offsetY + layer;
                    int back = offsetZ + col;
                    if (right == 0 && up == 0 && back == 0) {
                        throw new IllegalStateException("structure cell sits on the controller");
                    }
                    cell.place(layout, mark, right, up, back);
                }
            }
        }
    }

    private static void part(StructureLayout.Builder layout, int right, int up, int back,
            String name, StructureRole role) {
        layout.cell(right, up, back, CellMatch.part(name), role);
    }

    private static void block(StructureLayout.Builder layout, int right, int up, int back, String id) {
        layout.cell(right, up, back, CellMatch.block(id), StructureRole.CASING);
    }

    private static void sun(StructureLayout.Builder layout, char mark, int right, int up, int back) {
        switch (mark) {
            case 'W' -> part(layout, right, up, back, "tank_wall", StructureRole.FLUID_INPUT);
            case 'A' -> part(layout, right, up, back, "sunlight_absorber", StructureRole.CASING);
            case 'H' -> part(layout, right, up, back, "heat_pipe", StructureRole.CASING);
            case 'T' -> part(layout, right, up, back, "heat_pipe_top", StructureRole.FLUID_OUTPUT);
            default -> throw new IllegalStateException("sun heater symbol " + mark);
        }
    }

    private static void mirror(StructureLayout.Builder layout, char mark, int right, int up, int back) {
        switch (mark) {
            case 'W' -> part(layout, right, up, back, "large_sun_mirror_frame", StructureRole.CASING);
            case 'M' -> part(layout, right, up, back, "large_sun_mirror", StructureRole.CASING);
            default -> throw new IllegalStateException("sun mirror symbol " + mark);
        }
    }

    private static void tidalCell(StructureLayout.Builder layout, char mark, int right, int up, int back) {
        switch (mark) {
            case 'B' -> part(layout, right, up, back, "tidal_blade", StructureRole.CASING);
            case 'W' -> part(layout, right, up, back, "tank_wall", StructureRole.CASING);
            case 'A' -> layout.cell(right, up, back, CellMatch.air(), StructureRole.AIR);
            case 'O' -> block(layout, right, up, back, "minecraft:stone");
            case 'L' -> block(layout, right, up, back, "minecraft:water");
            default -> throw new IllegalStateException("tidal symbol " + mark);
        }
    }

    private static void oil(StructureLayout.Builder layout, char mark, int right, int up, int back) {
        switch (mark) {
            case 'A' -> part(layout, right, up, back, "oil_miner_drill", StructureRole.CASING);
            case 'W' -> part(layout, right, up, back, "tank_wall", StructureRole.ITEM_FLUID_ENERGY_INPUT);
            case 'O' -> part(layout, right, up, back, "tank_wall", StructureRole.ITEM_FLUID_OUTPUT);
            default -> throw new IllegalStateException("oil miner symbol " + mark);
        }
    }

    private static void mantleCell(StructureLayout.Builder layout, char mark, int right, int up, int back) {
        switch (mark) {
            case 'W' -> part(layout, right, up, back, "tungsten_wall", StructureRole.ITEM_FLUID_ENERGY_INPUT);
            case 'H' -> part(layout, right, up, back, "heat_pipe", StructureRole.CASING);
            case 'T' -> part(layout, right, up, back, "heat_pipe_top", StructureRole.FLUID_OUTPUT);
            case 'D' -> part(layout, right, up, back, "mantle_drill_rod", StructureRole.CASING);
            default -> throw new IllegalStateException("mantle symbol " + mark);
        }
    }

    private static void deburn(StructureLayout.Builder layout, char mark, int right, int up, int back) {
        switch (mark) {
            case 'F' -> part(layout, right, up, back, "enhanced_frame", StructureRole.ITEM_FLUID_ENERGY_INPUT);
            case 'G' -> part(layout, right, up, back, "enhanced_frame", StructureRole.ITEM_FLUID_OUTPUT);
            case 'W' -> part(layout, right, up, back, "tank_wall", StructureRole.ITEM_FLUID_INPUT);
            case 'P' -> part(layout, right, up, back, "chemical_reactor_pipe", StructureRole.CASING);
            default -> throw new IllegalStateException("deburn symbol " + mark);
        }
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

    @FunctionalInterface
    private interface Cell {
        void place(StructureLayout.Builder layout, char mark, int right, int up, int back);
    }
}
