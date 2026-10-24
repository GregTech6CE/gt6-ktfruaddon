package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;

/**
 * The fuel deburn factory: 19 layers of 14 rows and 11 columns, in the axis order of
 * {@link Gt6KtfruHeatLayouts}. Layers C and Q each appear twice.
 */
public final class Gt6KtfruFuelDeburnLayout {
    private Gt6KtfruFuelDeburnLayout() {}

    public static StructureLayout layout(String name) {
        return Gt6KtfruLayerLayout.build(name, -6, -1, -1, new String[][] {
                LAYER_A, LAYER_B, LAYER_C, LAYER_D, LAYER_C, LAYER_E, LAYER_F, LAYER_G,
                LAYER_H, LAYER_I, LAYER_J, LAYER_K, LAYER_L, LAYER_M, LAYER_N, LAYER_O,
                LAYER_P, LAYER_Q, LAYER_Q
        },
                Gt6KtfruLayerLayout.part('F', "enhanced_frame", StructureRole.ITEM_FLUID_ENERGY_INPUT),
                Gt6KtfruLayerLayout.part('G', "enhanced_frame", StructureRole.ITEM_FLUID_OUTPUT),
                Gt6KtfruLayerLayout.part('W', "tank_wall", StructureRole.ITEM_FLUID_INPUT),
                Gt6KtfruLayerLayout.part('P', "chemical_reactor_pipe", StructureRole.CASING));
    }

    private static final String[] LAYER_A = {
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

    private static final String[] LAYER_B = {
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

    private static final String[] LAYER_C = {
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

    private static final String[] LAYER_D = {
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

    private static final String[] LAYER_E = {
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

    private static final String[] LAYER_F = {
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

    private static final String[] LAYER_G = {
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

    private static final String[] LAYER_H = {
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

    private static final String[] LAYER_I = {
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

    private static final String[] LAYER_J = {
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

    private static final String[] LAYER_K = {
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

    private static final String[] LAYER_L = {
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

    private static final String[] LAYER_M = {
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

    private static final String[] LAYER_N = {
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

    private static final String[] LAYER_O = {
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

    private static final String[] LAYER_P = {
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

    private static final String[] LAYER_Q = {
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
