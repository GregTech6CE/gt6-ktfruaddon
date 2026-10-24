package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.gregtech.gregtech.api.addon.CellMatch;
import com.gregtech.gregtech.api.addon.CompressedGasTankDeclaration;
import com.gregtech.gregtech.api.addon.EnergyInput;
import com.gregtech.gregtech.api.addon.FluidPipeDeclaration;
import com.gregtech.gregtech.api.addon.ItemPipeDeclaration;
import com.gregtech.gregtech.api.addon.MachineCasing;
import com.gregtech.gregtech.api.addon.MachineFace;
import com.gregtech.gregtech.api.addon.StructureLayout;
import com.gregtech.gregtech.api.addon.StructureRole;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Behaviors;
import com.lombinaxmasson.gt6ktfruaddon.core.feature.Feature;

/**
 * Compressed gas: the two large tank valves with their structures, the five gas compressors, the five
 * compressed-gas drums, and the Teflon fluid pipes and PVC item pipes.
 */
public final class Gt6KtfruGasMachines implements Feature {
    /** Source 30100. Layout name, machine type and block id. */
    public static final String STEEL_GAS_TANK = "steel_compressed_gas_tank";
    /** Source 30101. */
    public static final String STAINLESS_GAS_TANK = "stainless_compressed_gas_tank";
    /** Shared machine type for source 22000–22004. Block ids stay per tier. */
    public static final String GAS_COMPRESSOR = "gas_compressor";
    public static final String BRONZE_COMPRESSOR = "gas_compressor_bronze";
    public static final String STEEL_GAS_DRUM = "steel_gas_drum";
    public static final String STAINLESS_GAS_DRUM = "stainless_gas_drum";
    public static final String MANASTEEL_GAS_DRUM = "manasteel_gas_drum";
    public static final String NETHERITE_GAS_DRUM = "netherite_gas_drum";
    public static final String TUNGSTENSTEEL_GAS_DRUM = "tungstensteel_gas_drum";
    /** PipeSpec.PipeSize names, lower case. */
    public static final String[] FLUID_PIPE_SIZES = {
            "tiny", "small", "medium", "large", "huge", "quadruple", "nonuple"
    };
    /** ItemPipeSpec.ItemPipeSize names, lower case. */
    public static final String[] ITEM_PIPE_SIZES = {
            "medium", "large", "huge", "restrictive_medium", "restrictive_large", "restrictive_huge"
    };
    public static final String TEFLON_PIPE = "teflon";
    public static final String PVC_PIPE = "pvc";
    /**
     * Source {@code NBT_TANK_CAPACITY}. One behavior tank stops at
     * 1,024,000,000 mB, so the valve splits this across seven tanks.
     */
    public static final long GAS_TANK_CAPACITY = 6_912_000_000L;

    private static final Map<String, Supplier<MachineBehavior>> BEHAVIORS = new Behaviors()
            .add(STEEL_GAS_TANK, Gt6KtfruGasTank::new)
            .add(STAINLESS_GAS_TANK, Gt6KtfruGasTank::new)
            .add(GAS_COMPRESSOR, Gt6KtfruGasCompressor::new)
            .build();

    @Override
    public List<AddonMachine> machines() {
        List<AddonMachine> machines = new ArrayList<>();
        machines.add(gasTank(STEEL_GAS_TANK, "Steel"));
        machines.add(gasTank(STAINLESS_GAS_TANK, "StainlessSteel"));
        machines.add(compressor(BRONZE_COMPRESSOR, "Bronze", 1, 32, 8_000));
        machines.add(compressor("gas_compressor_steel", "Steel", 2, 128, 20_000));
        machines.add(compressor("gas_compressor_titanium", "Titanium", 3, 512, 80_000));
        machines.add(compressor("gas_compressor_tungstensteel", "Tungstensteel", 4, 2048, 200_000));
        machines.add(compressor("gas_compressor_adamantium", "Adamantium", 5, 8192, 800_000));
        return List.copyOf(machines);
    }

    @Override
    public List<StructureLayout> layouts() {
        return List.of(
                gasTankLayout(STEEL_GAS_TANK, "dense_steel_wall"),
                gasTankLayout(STAINLESS_GAS_TANK, "tank_wall_dense"));
    }

    @Override
    public List<CompressedGasTankDeclaration> gasTanks() {
        return List.of(
                drum(STEEL_GAS_DRUM, "Steel", 12_800_000, false, false, false),
                drum(STAINLESS_GAS_DRUM, "StainlessSteel", 12_800_000, true, false, false),
                drum(MANASTEEL_GAS_DRUM, "Manasteel", 12_800_000, true, false, true),
                drum(NETHERITE_GAS_DRUM, "Netherite", 25_600_000, true, true, true),
                drum(TUNGSTENSTEEL_GAS_DRUM, "Tungstensteel", 51_200_000, false, false, true));
    }

    @Override
    public List<FluidPipeDeclaration> fluidPipes() {
        return List.of(new FluidPipeDeclaration(TEFLON_PIPE, "Teflon", 800, true, true, false, true, 0, 0));
    }

    @Override
    public List<ItemPipeDeclaration> itemPipes() {
        return List.of(new ItemPipeDeclaration(PVC_PIPE, "PVC", 8192, 4));
    }

    @Override
    public List<String> derivedBlockIds() {
        List<String> ids = new ArrayList<>();
        for (String size : FLUID_PIPE_SIZES) ids.add(fluidPipeId(size));
        for (String size : ITEM_PIPE_SIZES) ids.add(itemPipeId(size));
        return List.copyOf(ids);
    }

    @Override
    public Map<String, Supplier<MachineBehavior>> behaviors() {
        return BEHAVIORS;
    }

    public static String fluidPipeId(String size) {
        return "pipe_" + size + "_" + TEFLON_PIPE;
    }

    public static String itemPipeId(String size) {
        return "item_pipe_" + size + "_" + PVC_PIPE;
    }

    /**
     * Source 30100 / 30101. The 3×3×3 sits one block behind the valve and is
     * aligned to the machine: right and up run -1..1, back runs 0..2. The valve
     * itself is the near-face center, so that cell is absent. The middle cell
     * is air and must be empty. The other 25 cells are the wall named here.
     */
    private static StructureLayout gasTankLayout(String type, String wall) {
        var layout = StructureLayout.builder(type);
        for (int up = -1; up <= 1; up++) {
            for (int right = -1; right <= 1; right++) {
                for (int back = 0; back <= 2; back++) {
                    if (right == 0 && up == 0 && back == 0) continue;
                    if (right == 0 && up == 0 && back == 1) {
                        layout.cell(0, 0, 1, CellMatch.air(), StructureRole.AIR);
                    } else {
                        layout.cell(right, up, back, CellMatch.part(wall), StructureRole.CASING);
                    }
                }
            }
        }
        return layout.build();
    }

    /**
     * The valve has no energy of its own. The declared packet is the API maximum
     * so an ordinary EU cable is below the accepted window and is ignored.
     * Seven tanks sum to {@link #GAS_TANK_CAPACITY}.
     */
    private static BehaviorMachine gasTank(String id, String material) {
        int[] capacity = {
                1_024_000_000, 1_024_000_000, 1_024_000_000, 1_024_000_000,
                1_024_000_000, 1_024_000_000, 768_000_000
        };
        var builder = BehaviorMachine.builder(MachineCasing.builder(id, material).machineType(id).tier(1).build())
                .energy(EnergyInput.builder("EU", 1_000_000_000_000L).faces(MachineFace.BOTTOM).build())
                .gui("gt6ktfruaddon:textures/gui/machines/gastank.png")
                .energyGauge(8, 8)
                .progress(148, 64);
        for (int tank = 0; tank < capacity.length; tank++) {
            builder.fluid(capacity[tank], 20 + tank * BehaviorMachine.TANK_WIDTH, 8, BehaviorMachine.SlotRole.ANY);
        }
        return builder.build();
    }

    /**
     * Source 22000–22004. Capacity stays at the behavior default, so the buffer
     * holds up to twice the rated KU and the tick spends whatever arrived.
     * The tank is an input: pipes can fill it and cannot pull from it.
     */
    private static BehaviorMachine compressor(String id, String material, int tier, long ku, int tank) {
        return BehaviorMachine.builder(MachineCasing.builder(id, material)
                        .machineType(GAS_COMPRESSOR).tier(tier).build())
                .energy(EnergyInput.builder("KU", ku).faces(MachineFace.BOTTOM).build())
                .gui(Ktfru.PLACEHOLDER_GUI)
                .fluid(tank, 152, 8, BehaviorMachine.SlotRole.INPUT)
                .build();
    }

    private static CompressedGasTankDeclaration drum(String id, String material, int capacity,
            boolean acid, boolean plasma, boolean magic) {
        return new CompressedGasTankDeclaration(id, material, capacity, acid, plasma, magic);
    }
}
