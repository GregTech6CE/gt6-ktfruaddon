package com.lombinaxmasson.gt6ktfruaddon;

import org.slf4j.Logger;

import com.gregtech.gregtech.api.addon.FuelGeneratorDeclaration;
import com.gregtech.gregtech.api.addon.GregTechAddon;
import com.gregtech.gregtech.api.addon.GregTechAddons;
import com.gregtech.gregtech.api.addon.GregTechMaterials;
import com.gregtech.gregtech.api.addon.MachineDeclaration;
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
                        for (MachineDeclaration machine : machines.machines())
                            accept(output, machine.id());
                        for (FuelGeneratorDeclaration generator : machines.fuelGenerators())
                            accept(output, generator.id());
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
                if (stack("gas_turbine_steel").isEmpty())
                    throw new IllegalStateException("steel gas turbine is missing");
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
