package com.lombinaxmasson.gt6ktfruaddon;

import org.slf4j.Logger;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.GregTechAddon;
import com.gregtech.gregtech.api.addon.GregTechAddons;
import com.gregtech.gregtech.api.addon.GregTechMaterials;
import com.gregtech.gregtech.api.addon.MaterialDeclaration;
import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Probe entry. Materials and machines arrive through ServiceLoader. Recipes are added once GT
 * calls {@code onRecipesReady}.
 */
@Mod(Gt6KtfruAddon.MODID)
public final class Gt6KtfruAddon {
    public static final String MODID = "gt6ktfruaddon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB =
            CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gt6ktfruaddon"))
                    .withTabsBefore(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                    .icon(() -> stack("heat_mixer_lv"))
                    .withSearchBar()
                    .displayItems((parameters, output) -> {
                        Gt6KtfruMachines machines = new Gt6KtfruMachines();
                        for (AddonMachine machine : machines.addonMachines())
                            accept(output, machine.casing().id());
                        for (String drum : Gt6KtfruMachines.GAS_DRUMS) accept(output, drum);
                        for (String size : Gt6KtfruMachines.FLUID_PIPE_SIZES)
                            accept(output, Gt6KtfruMachines.fluidPipeId(size));
                        for (String size : Gt6KtfruMachines.ITEM_PIPE_SIZES)
                            accept(output, Gt6KtfruMachines.itemPipeId(size));
                        for (var item : Gt6KtfruItems.ALL) output.accept(item.get());
                        for (MaterialDeclaration material : new Gt6KtfruMaterials().materials()) {
                            if (material.dust() || material.metal())
                                acceptStack(output, AddonItems.stack("dust", material.name(), 1));
                            if (material.metal())
                                acceptStack(output, AddonItems.stack("ingot", material.name(), 1));
                            if (material.liquid() || material.gas())
                                acceptFluid(output, material);
                        }
                    })
                    .build());

    public Gt6KtfruAddon(IEventBus modEventBus, ModContainer modContainer) {
        CREATIVE_MODE_TABS.register(modEventBus);
        Gt6KtfruItems.ITEMS.register(modEventBus);
        GregTechAddons.register(new GregTechAddon() {
            @Override
            public String id() {
                return MODID;
            }

            @Override
            public void onRecipesReady(Context context) {
                LOGGER.info("gt6ktfruaddon: ready on {} {} (GT6CE addon API {})",
                        context.platform(), context.minecraftVersion(), context.apiVersion());
                if (!GregTechMaterials.find("AmmoniumDichromate").valid()
                        || AddonItems.stack("dust", "AmmoniumDichromate", 1).isEmpty())
                    throw new IllegalStateException("ammonium dichromate dust is missing");
                if (!GregTechMaterials.find("Methanol").valid() || !GregTechMaterials.find("Acetylene").valid())
                    throw new IllegalStateException("surface fluid materials are missing");
                FluidStack methanol = AddonFluids.still("GenLiquid_Methanol", 250);
                if (methanol == null || methanol.isEmpty())
                    throw new IllegalStateException("methanol fluid is missing");
                FluidStack acetylene = AddonFluids.still("GenGas_Acetylene", 250);
                if (acetylene == null || acetylene.isEmpty())
                    throw new IllegalStateException("acetylene gas is missing");
                if (fluidItem(methanol) == Items.AIR)
                    throw new IllegalStateException("methanol fluid item is missing");
                if (fluidItem(acetylene) == Items.AIR)
                    throw new IllegalStateException("acetylene fluid item is missing");
                Gt6KtfruRecipes.register();
                if (stack("heat_mixer_lv").isEmpty())
                    throw new IllegalStateException("heat mixer is missing");
                if (stack("tiny_distillation_tower").isEmpty())
                    throw new IllegalStateException("tiny distillation tower is missing");
                if (stack("small_distillation_tower").isEmpty())
                    throw new IllegalStateException("small distillation tower is missing");
                if (stack("distillation_tower").isEmpty())
                    throw new IllegalStateException("distillation tower is missing");
                if (stack("ore_processing_system").isEmpty())
                    throw new IllegalStateException("ore processing system is missing");
                if (stack("gas_turbine_steel").isEmpty())
                    throw new IllegalStateException("steel gas turbine is missing");
                if (stack(Gt6KtfruMachines.STEEL_GAS_TANK).isEmpty()
                        || stack(Gt6KtfruMachines.STAINLESS_GAS_TANK).isEmpty())
                    throw new IllegalStateException("compressed gas tank valve is missing");
                if (stack(Gt6KtfruMachines.BRONZE_COMPRESSOR).isEmpty())
                    throw new IllegalStateException("bronze gas compressor is missing");
                if (stack(Gt6KtfruMachines.STEEL_GAS_DRUM).isEmpty())
                    throw new IllegalStateException("steel compressed gas drum is missing");
                if (stack(Gt6KtfruMachines.fluidPipeId("tiny")).isEmpty())
                    throw new IllegalStateException("tiny teflon pipe is missing");
                if (stack(Gt6KtfruMachines.itemPipeId("medium")).isEmpty())
                    throw new IllegalStateException("medium pvc item pipe is missing");
                if (stack("void_hopper_wood").isEmpty() || stack("miner_tin_alloy").isEmpty()
                        || stack("water_mill").isEmpty() || stack("manual_motor_wood").isEmpty()
                        || stack("manual_kinetic_motor_wood").isEmpty()
                        || stack("flywheel_wood").isEmpty()
                        || stack("large_vacuum_flywheel_adamantium").isEmpty())
                    throw new IllegalStateException("kinetic behavior machine is missing");
                if (stack("gas_battery_aluminium").isEmpty() || stack("gas_battery_stainless").isEmpty()
                        || stack("ktfru.item.battery.pole.nickel").isEmpty()
                        || stack("proton_exchange_membrane").isEmpty())
                    throw new IllegalStateException("gas battery is missing");
                if (stack("transformer_small_lv").isEmpty() || stack("light_transformer_large_luv").isEmpty()
                        || stack("flywheel_gear_small_t1").isEmpty() || stack("flywheel_gear_large_t6").isEmpty())
                    throw new IllegalStateException("transformer part is missing");
                if (stack("mask_aligner_uv").isEmpty() || stack("cnc_machine").isEmpty()
                        || stack("aluminium_wall").isEmpty() || stack("mask_aligner_energy_uv").isEmpty())
                    throw new IllegalStateException("mask aligner is missing");
                if (stack("research_reactor").isEmpty() || stack("optical_microscope").isEmpty()
                        || stack("electron_microscope").isEmpty() || stack("structure_glass").isEmpty()
                        || stack("wireless_compute_controller").isEmpty() || stack("spacetime_compute_t2").isEmpty()
                        || stack("quantum_compute_node_naquadah").isEmpty()
                        || stack("wireless_battery_inductive").isEmpty() || stack("wireless_battery_quantum").isEmpty()
                        || stack("wireless_receiver_inductive").isEmpty() || stack("wireless_receiver_quantum").isEmpty()
                        || stack("laser_fusion_industrial").isEmpty() || stack("catalytic_reactor_iv").isEmpty()
                        || stack("centrifugal_microgravity").isEmpty() || stack("ai_pattern_recognition").isEmpty()
                        || stack("fusion_tokamak_t1").isEmpty())
                    throw new IllegalStateException("late kTFRU machine is missing");
                if (stack(Gt6KtfruMachines.SUN_HEATER).isEmpty() || stack(Gt6KtfruMachines.SUN_MIRROR).isEmpty()
                        || stack(Gt6KtfruMachines.TIDAL).isEmpty() || stack(Gt6KtfruMachines.OIL_MINER).isEmpty()
                        || stack(Gt6KtfruMachines.MANTLE).isEmpty() || stack(Gt6KtfruMachines.FUEL_DEBURN_TYPE).isEmpty()
                        || stack("tidal_blade").isEmpty() || stack("enhanced_frame").isEmpty())
                    throw new IllegalStateException("heat machine is missing");
                FluidStack hydrogen = AddonFluids.still("Hydrogen", 250);
                if (hydrogen == null || hydrogen.isEmpty())
                    throw new IllegalStateException("GT fluid field Hydrogen is missing");
            }
        });
    }

    private static void acceptFluid(CreativeModeTab.Output output, MaterialDeclaration material) {
        String field = (material.gas() ? "GenGas_" : "GenLiquid_") + material.name();
        FluidStack fluid = AddonFluids.still(field, 250);
        Item item = fluidItem(fluid);
        if (item != Items.AIR) output.accept(new ItemStack(item));
    }

    private static Item fluidItem(FluidStack fluid) {
        if (fluid == null || fluid.isEmpty()) return Items.AIR;
        var id = BuiltInRegistries.FLUID.getKey(fluid.getFluid());
        if (id == null) return Items.AIR;
        return BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("gt6ce", "fluid_item_" + id.getPath()));
    }

    private static void acceptStack(CreativeModeTab.Output output, ItemStack stack) {
        if (stack != null && !stack.isEmpty()) output.accept(stack);
    }

    private static void accept(CreativeModeTab.Output output, String path) {
        acceptStack(output, stack(path));
    }

    private static ItemStack stack(String path) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(MODID, path));
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }
}
