package com.lombinaxmasson.gt6ktfruaddon.processing;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.MachineIo;
import com.gregtech.gregtech.api.addon.RecipeMachine;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.core.layout.Parts;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * The ore processing system, source 30041: a large recipe machine with a three-layer structure.
 */
public final class OreProcessing implements Feature {
    /** Layout name and machine type. The controller id uses the same string. */
    public static final String ORE_PROCESS_TYPE = "ore_processing_system";
    /**
     * Layer rule ABC, offset 0,0,0. Local row grows to the machine's left and
     * local column grows backward, so a cell is {@code (-row, layer, col)}.
     * The controller is layer A, row 0, column 0.
     */
    private static final String[] ORE_BOTTOM = {
            "AAAAA", "AAAAA", "AAAAA", "BBBBB", "BBBBB", "NBBBB"
    };
    private static final String[] ORE_MIDDLE = {
            "CCCCC", "CCCCC", "CCCCC", "BBDDD", "BBDDD", "BBDDD"
    };
    private static final String[] ORE_TOP = {
            "     ", "     ", "     ", "EEDDD", "EEDDD", "EEDDD"
    };

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.ORE_PROCESS, "Ore Process System", "oreprocesssystem", 3, 9, 1, 3, 3, 0, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(oreProcessingSystem());
        return List.copyOf(machines);
    }

    @Override
    public List<StructureLayout> layouts() {
        return List.of(
                oreProcessingLayout());
    }

    /**
     * Source 30041. Titanium controller, 2048 EU, packet range 1024 to 16384,
     * parallel 64. Crushed-ore rows are 128 EU. Those rows are not on this map yet.
     * Controller item and fluid outputs are closed; the output wall carries them.
     */
    private static RecipeMachine oreProcessingSystem() {
        List<String> keys = new ArrayList<>();
        for (int line = 1; line <= 4; line++) {
            keys.add("gt6ktfruaddon.structure." + ORE_PROCESS_TYPE + "." + line);
        }
        return RecipeMachine.builder(MachineCasing.builder(ORE_PROCESS_TYPE, "Titanium").machineType(ORE_PROCESS_TYPE).tier(4).build())
                .energy(EnergyInput.builder("EU", 2048).range(1024, 16384).faces(MachineFace.all()).build())
                .io(MachineIo.builder()
                        .itemInputs(MachineFace.TOP)
                        .itemAutoInput(MachineFace.TOP)
                        .itemOutputs()
                        .fluidInputs(MachineFace.TOP)
                        .fluidAutoInput(MachineFace.TOP)
                        .fluidOutputs()
                        .build())
                .parallel(64)
                .recipeMap(RecipeMaps.ORE_PROCESS)
                .largeRecipe()
                .structureKeys(List.copyOf(keys))
                .build();
    }

    private static StructureLayout oreProcessingLayout() {
        var layout = StructureLayout.builder(ORE_PROCESS_TYPE);
        String[][] layers = {ORE_BOTTOM, ORE_MIDDLE, ORE_TOP};
        for (int layer = 0; layer < layers.length; layer++) {
            String[] rows = layers[layer];
            for (int row = 0; row < rows.length; row++) {
                String line = rows[row];
                for (int col = 0; col < line.length(); col++) {
                    if (layer == 0 && row == 0 && col == 0) continue;
                    char symbol = line.charAt(col);
                    if (symbol == ' ') continue;
                    layout.cell(-row, layer, col, CellMatch.part(orePart(symbol)), oreRole(symbol));
                }
            }
        }
        return layout.build();
    }

    private static String orePart(char symbol) {
        return switch (symbol) {
            case 'A' -> "titanium_wall";
            case 'B', 'N' -> "tungstensteel_wall";
            case 'C' -> "sluice_part";
            case 'D' -> "centrifuge_part";
            case 'E' -> "shredder_blades";
            default -> throw new IllegalStateException("unknown ore structure symbol " + symbol);
        };
    }

    /** {@code N} is the only output. Parts with no usage flag are casings. */
    private static StructureRole oreRole(char symbol) {
        return switch (symbol) {
            case 'A', 'B' -> StructureRole.ITEM_FLUID_INPUT;
            case 'N' -> StructureRole.ITEM_FLUID_OUTPUT;
            case 'C', 'D', 'E' -> StructureRole.CASING;
            default -> throw new IllegalStateException("unknown ore structure symbol " + symbol);
        };
    }
}
