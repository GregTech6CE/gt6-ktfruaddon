package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.LayoutPart;
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
 * The four mask aligners and the CNC machine with their structures and structure parts.
 */
public final class Gt6KtfruSemiconductorMachines implements Feature {
    public static final String MASK_ALIGNER_UV = "mask_aligner_uv";
    public static final String MASK_ALIGNER_UV_PLUS = "mask_aligner_uv_plus";
    public static final String MASK_ALIGNER_DUV = "mask_aligner_duv";
    public static final String MASK_ALIGNER_EUV = "mask_aligner_euv";
    public static final String CNC_MACHINE = "cnc_machine";

    @Override
    public List<RecipeTable> recipeTables() {
        List<RecipeTable> maps = new ArrayList<>();
        maps.add(RecipeMaps.table(RecipeMaps.MASK_ALIGNER, "Mask Aligner", "maskaligner", 3, 3, 1, 3, 0, 0, 1));
        maps.add(RecipeMaps.table(RecipeMaps.CNC, "CNC", "cnc", 4, 3, 1, 3, 0, 1, 1));
        return List.copyOf(maps);
    }

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(maskAligner(MASK_ALIGNER_UV, "Aluminium", 2, 128, 64, 256,
                MachineFace.TOP, MachineFace.BACK, null, false));
        machines.add(maskAligner(MASK_ALIGNER_UV_PLUS, "StainlessSteel", 3, 512, 256, 1024,
                null, MachineFace.TOP, MachineFace.BACK, false));
        machines.add(maskAligner(MASK_ALIGNER_DUV, "Chromium", 4, 2048, 1024, 4096,
                MachineFace.TOP, MachineFace.BACK, null, true));
        machines.add(maskAligner(MASK_ALIGNER_EUV, "Tungsten", 5, 8192, 4096, 16384,
                MachineFace.TOP, MachineFace.BACK, null, true));
        machines.add(cncMachine());
        return List.copyOf(machines);
    }

    @Override
    public List<StructureLayout> layouts() {
        return List.of(
                maskAlignerUvLayout(),
                maskAlignerUvPlusLayout(),
                maskAlignerDuvLayout(MASK_ALIGNER_DUV),
                maskAlignerDuvLayout(MASK_ALIGNER_EUV),
                cncLayout());
    }

    @Override
    public List<LayoutPart> parts() {
        return List.of(
                Parts.casing("aluminium_wall", "Aluminium", 4.0F),
                Parts.casing("sunlight_absorber", "Silver", 6.0F),
                Parts.casing("catalytic_reactor_casing", "Tungstensteel", 10.0F),
                Parts.casing("mask_aligner_platform_motor", "StainlessSteel", 6.0F),
                Parts.casing("mask_aligner_wafer_platform", "StainlessSteel", 6.0F),
                Parts.casing("cnc_platform", "Aluminium", 6.0F),
                Parts.casing("cnc_motor", "Aluminium", 6.0F),
                Parts.casing("cnc_cutter_head", "StainlessSteel", 6.0F),
                Parts.casing("light_module_uv", "Aluminium", 6.0F),
                Parts.casing("light_module_uv_plus", "StainlessSteel", 6.0F),
                Parts.casing("light_module_duv", "Chromium", 6.0F),
                Parts.casing("light_module_euv", "Tungstensteel", 6.0F),
                Parts.casing("mask_aligner_shell", "StainlessSteel", 10.0F),
                Parts.casing("mask_aligner_io_uv", "Aluminium", 6.0F),
                Parts.casing("mask_aligner_io_uv_plus", "StainlessSteel", 6.0F),
                energyModule("mask_aligner_energy_uv", "Aluminium", 192, 64, 256),
                energyModule("mask_aligner_energy_uv_plus", "StainlessSteel", 768, 256, 1024));
    }

    /**
     * Source 30003, 30009, 30010, 30011. LU stays on the controller declaration.
     * UV and UV+ take that LU through the light-module cells. The energy module
     * is a condition part and is not a second recipe energy.
     */
    private static RecipeMachine maskAligner(String id, String material, int tier, long nominal, long min, long max,
            MachineFace itemIn, MachineFace itemOut, MachineFace fluidOut, boolean controllerEnergy) {
        var io = MachineIo.builder();
        if (itemIn != null) io.itemInputs(itemIn).itemAutoInput(itemIn);
        if (itemOut != null) io.itemOutputs(itemOut).itemAutoOutput(itemOut);
        if (fluidOut != null) io.fluidOutputs(fluidOut).fluidAutoOutput(fluidOut);
        var energy = EnergyInput.builder("LU", nominal).range(min, max);
        if (controllerEnergy) energy.faces(MachineFace.all());
        else energy.faces();
        return RecipeMachine.builder(MachineCasing.builder(id, material).machineType(id).tier(tier).build())
                .energy(energy.build())
                .io(io.build())
                .parallel(1)
                .recipeMap(RecipeMaps.MASK_ALIGNER)
                .largeRecipe()
                .structureKeys(structureLines(id, 4))
                .build();
    }

    /** Source 30012. EU enters the aluminium walls. The motor and cutter are casings. */
    private static RecipeMachine cncMachine() {
        return RecipeMachine.builder(MachineCasing.builder(CNC_MACHINE, "Aluminium").machineType(CNC_MACHINE).tier(2).build())
                .energy(EnergyInput.builder("EU", 128).range(64, 256).faces().build())
                .io(MachineIo.builder()
                        .itemInputs(MachineFace.TOP).itemAutoInput(MachineFace.TOP)
                        .itemOutputs(MachineFace.BOTTOM).itemAutoOutput(MachineFace.BOTTOM)
                        .build())
                .parallel(1)
                .recipeMap(RecipeMaps.CNC)
                .largeRecipe()
                .structureKeys(structureLines(CNC_MACHINE, 4))
                .build();
    }

    private static List<String> structureLines(String type, int count) {
        List<String> keys = new ArrayList<>();
        for (int line = 1; line <= count; line++) keys.add("gt6ktfruaddon.structure." + type + "." + line);
        return List.copyOf(keys);
    }

    /** Layer Y, offset -1,0,0. Positive source X is the machine's left, so right is {@code 1 - row}. */
    private static void yLayer(StructureLayout.Builder layout, String type, int up, String[] rows) {
        for (int row = 0; row < rows.length; row++) {
            String line = rows[row];
            for (int col = 0; col < line.length(); col++) {
                char symbol = line.charAt(col);
                if (symbol == ' ') continue;
                layout.cell(1 - row, up, col, CellMatch.part(maskPart(type, symbol)), maskRole(type, symbol));
            }
        }
    }

    private static StructureLayout maskAlignerUvLayout() {
        var layout = StructureLayout.builder(MASK_ALIGNER_UV);
        yLayer(layout, MASK_ALIGNER_UV, 0, new String[] {"WW", " A", "WW"});
        yLayer(layout, MASK_ALIGNER_UV, 1, new String[] {"WW", "BC", "WW"});
        return layout.build();
    }

    private static StructureLayout maskAlignerUvPlusLayout() {
        var layout = StructureLayout.builder(MASK_ALIGNER_UV_PLUS);
        yLayer(layout, MASK_ALIGNER_UV_PLUS, 0, new String[] {"WWW", " DA", "WWW"});
        yLayer(layout, MASK_ALIGNER_UV_PLUS, 1, new String[] {"WBW", "BCB", "WBW"});
        yLayer(layout, MASK_ALIGNER_UV_PLUS, 2, new String[] {"LLL", "LLL", "LLL"});
        return layout.build();
    }

    /**
     * DUV and EUV share one map. 31110 and 31120 were never registered, so those
     * two cells are absent. 0 is the controller.
     */
    private static StructureLayout maskAlignerDuvLayout(String type) {
        var layout = StructureLayout.builder(type);
        int[][][] ids = {
                {{31002, 0, 31002}, {31002, 31200, 31002}},
                {{31002, 31110, 31002}, {31002, 31120, 31002}}
        };
        for (int up = 0; up < ids.length; up++) {
            for (int back = 0; back < ids[up].length; back++) {
                for (int mapX = 0; mapX < ids[up][back].length; mapX++) {
                    int id = ids[up][back][mapX];
                    if (id == 0 || id == 31110 || id == 31120) continue;
                    String part = id == 31200 ? "catalytic_reactor_casing" : "sunlight_absorber";
                    layout.cell(1 - mapX, up, back, CellMatch.part(part), StructureRole.ITEM_FLUID_INPUT);
                }
            }
        }
        return layout.build();
    }

    private static StructureLayout cncLayout() {
        var layout = StructureLayout.builder(CNC_MACHINE);
        yLayer(layout, CNC_MACHINE, 0, new String[] {"AAA", " CC", "BBB", "BBB", "BBB"});
        yLayer(layout, CNC_MACHINE, 1, new String[] {"AAA", "CCC", "   ", "   ", "   "});
        yLayer(layout, CNC_MACHINE, 2, new String[] {" A ", " A ", " A ", " D ", "   "});
        return layout.build();
    }

    private static String maskPart(String type, char symbol) {
        if (CNC_MACHINE.equals(type)) {
            return switch (symbol) {
                case 'A' -> "aluminium_wall";
                case 'B' -> "cnc_platform";
                case 'C' -> "cnc_motor";
                case 'D' -> "cnc_cutter_head";
                default -> throw new IllegalStateException("unknown cnc symbol " + symbol);
            };
        }
        if (MASK_ALIGNER_UV_PLUS.equals(type)) {
            return switch (symbol) {
                case 'W' -> "mask_aligner_shell";
                case 'A' -> "mask_aligner_energy_uv_plus";
                case 'B' -> "mask_aligner_platform_motor";
                case 'C' -> "mask_aligner_wafer_platform";
                case 'D' -> "mask_aligner_io_uv_plus";
                case 'L' -> "light_module_uv_plus";
                default -> throw new IllegalStateException("unknown uv+ symbol " + symbol);
            };
        }
        return switch (symbol) {
            case 'W' -> "aluminium_wall";
            case 'A' -> "mask_aligner_energy_uv";
            case 'B' -> "light_module_uv";
            case 'C' -> "mask_aligner_io_uv";
            default -> throw new IllegalStateException("unknown uv symbol " + symbol);
        };
    }

    private static StructureRole maskRole(String type, char symbol) {
        if (CNC_MACHINE.equals(type)) {
            return switch (symbol) {
                case 'A' -> StructureRole.ENERGY_INPUT;
                case 'B' -> StructureRole.ITEM_FLUID_INPUT;
                default -> StructureRole.CASING;
            };
        }
        if (MASK_ALIGNER_UV_PLUS.equals(type)) {
            return switch (symbol) {
                case 'L' -> StructureRole.ENERGY_INPUT;
                case 'C' -> StructureRole.ITEM_FLUID_IO;
                case 'A' -> StructureRole.CASING;
                default -> StructureRole.ITEM_FLUID_INPUT;
            };
        }
        return switch (symbol) {
            case 'B' -> StructureRole.ENERGY_INPUT;
            case 'C' -> StructureRole.ITEM_FLUID_IO;
            case 'A' -> StructureRole.CASING;
            default -> StructureRole.ITEM_FLUID_INPUT;
        };
    }

    private static LayoutPart energyModule(String name, String material, long nominal, long min, long max) {
        return LayoutPart.builder(name, material).hardness(6.0F)
                .energyInput(EnergyInput.builder("EU", nominal).range(min, max)
                        .capacity(max).faces(MachineFace.all()).build())
                .condition(nominal)
                .build();
    }
}
