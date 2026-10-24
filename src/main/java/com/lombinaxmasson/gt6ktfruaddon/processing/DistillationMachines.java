package com.lombinaxmasson.gt6ktfruaddon.processing;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineIo;
import com.gregtech.gregtech.api.addon.RecipeMachine;
import com.gregtech.gregtech.api.addon.RecipeTable;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

/**
 * The tiny, small and full distillation towers, sources 30004, 30005 and 30006: large recipe machines whose
 * layout is a heat layer under four, six or nine distillation layers.
 */
public final class DistillationMachines implements Feature {
    /** Layout name and machine type. The controller id uses the same string. */
    public static final String TINY_DISTILL_TYPE = "tiny_distillation_tower";
    public static final String SMALL_DISTILL_TYPE = "small_distillation_tower";
    public static final String DISTILL_TYPE = "distillation_tower";

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.TINY_DISTILL, "Tiny Distillation Tower", "tinydistillationtower", 1, 3, 0, 1, 3, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.SMALL_DISTILL, "Small Distillation Tower", "smalldistillationtower", 1, 3, 0, 1, 6, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.DISTILL, "Distillation Tower", "distillationtower", 1, 3, 0, 1, 9, 0, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(distillationTower(TINY_DISTILL_TYPE, RecipeMaps.TINY_DISTILL, 1024));
        machines.add(distillationTower(SMALL_DISTILL_TYPE, RecipeMaps.SMALL_DISTILL, 2048));
        machines.add(distillationTower(DISTILL_TYPE, RecipeMaps.DISTILL, 4096));
        return List.copyOf(machines);
    }

    @Override
    public List<StructureLayout> layouts() {
        return List.of(
                distillationLayout(TINY_DISTILL_TYPE, 4),
                distillationLayout(SMALL_DISTILL_TYPE, 6),
                distillationLayout(DISTILL_TYPE, 9));
    }

    /**
     * Sources 30004, 30005 and 30006. One 3×3 heat layer, then four, six or nine
     * 3×3 distillation layers. The controller sits in the front-middle of the
     * lowest distillation layer and is not a cell. Output hatches are the
     * back-middle column. Coordinates are the layout's right, up and back.
     * The controller accepts no item, fluid or energy; the structure roles do.
     */
    private static RecipeMachine distillationTower(String type, String recipe, long maxPacket) {
        List<String> keys = new ArrayList<>();
        for (int line = 1; line <= 6; line++) {
            keys.add("gt6ktfruaddon.structure." + type + "." + line);
        }
        return RecipeMachine.builder(MachineCasing.builder(type, "StainlessSteel").machineType(type).tier(2).build())
                .energy(EnergyInput.builder("HU", 512).range(16, maxPacket).faces().build())
                .io(MachineIo.builder().itemInputs().itemOutputs().fluidInputs().fluidOutputs().build())
                .parallel(1)
                .recipeMap(recipe)
                .largeRecipe()
                .cheapOverclocking(true)
                .structureKeys(List.copyOf(keys))
                .build();
    }

    private static StructureLayout distillationLayout(String type, int wallLayers) {
        var layout = StructureLayout.builder(type);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                towerCell(layout, row, 0, col, "heat_transmitter", StructureRole.ENERGY_INPUT);
            }
        }
        for (int layer = 1; layer <= wallLayers; layer++) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (layer == 1 && row == 1 && col == 0) continue;
                    boolean output = row == 1 && col == 2;
                    towerCell(layout, row, layer, col, "distillation_tower_part",
                            output ? StructureRole.ITEM_FLUID_OUTPUT : StructureRole.ITEM_FLUID_INPUT);
                }
            }
        }
        return layout.build();
    }

    /** Source local row grows to the machine's left, so right is {@code 1 - row}. */
    private static void towerCell(StructureLayout.Builder layout, int row, int layer, int col, String part,
            StructureRole role) {
        layout.cell(1 - row, layer - 1, col, CellMatch.part(part), role);
    }
}
