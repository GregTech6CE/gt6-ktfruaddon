package com.lombinaxmasson.gt6ktfruaddon;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.CompressedGasTankDeclaration;
import com.gregtech.gregtech.api.addon.GregTechAddon;
import com.gregtech.gregtech.api.addon.GregTechAddons;
import com.gregtech.gregtech.api.addon.GregTechMaterials;
import com.gregtech.gregtech.api.addon.MaterialDeclaration;
import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.lombinaxmasson.gt6ktfruaddon.content.Gt6KtfruMaterials;
import com.lombinaxmasson.gt6ktfruaddon.generated.Gt6KtfruItems;
import com.lombinaxmasson.gt6ktfruaddon.registry.Features;
import com.lombinaxmasson.gt6ktfruaddon.registry.Gt6KtfruRecipes;
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
@Mod(Ktfru.MODID)
public final class Gt6KtfruAddon {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ktfru.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB =
            CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gt6ktfruaddon"))
                    .withTabsBefore(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                    .icon(() -> stack("heat_mixer_lv"))
                    .withSearchBar()
                    .displayItems((parameters, output) -> {
                        Features features = Features.get();
                        for (AddonMachine machine : features.machines())
                            accept(output, machine.casing().id());
                        for (CompressedGasTankDeclaration drum : features.gasTanks())
                            accept(output, drum.id());
                        for (String id : features.derivedBlockIds()) accept(output, id);
                        for (var item : Gt6KtfruItems.BY_PATH.values()) output.accept(item.get());
                        acceptStack(output, AddonItems.stack("flywheel", "Iron", 1));
                        acceptStack(output, AddonItems.stack("bladeLargeTurbine", "Steel", 1));
                        acceptStack(output, AddonItems.stack("dustCommercialPure", "Iron", 1));
                        acceptStack(output, AddonItems.stack("dustAnalyticalPure", "Iron", 1));
                        acceptStack(output, AddonItems.stack("dustAbsolutelyPure", "Iron", 1));
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
                return Ktfru.MODID;
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
                requireDeclaredIdsRegistered();
                if (AddonItems.stack("flywheel", "Iron", 1).isEmpty()
                        || AddonItems.stack("dustCommercialPure", "Iron", 1).isEmpty()
                        || Gt6KtfruItems.stack("nickel_catalyst_cluster", 1).isEmpty())
                    throw new IllegalStateException("kTFRU prefix or catalyst item is missing");
                FluidStack hydrogen = AddonFluids.still("Hydrogen", 250);
                if (hydrogen == null || hydrogen.isEmpty())
                    throw new IllegalStateException("GT fluid field Hydrogen is missing");
            }
        });
    }

    /**
     * Every block and item this addon declares has to be registered once GT6CE has dispatched
     * the recipe lifecycle. The ids come from the declarations, so a new machine is covered
     * without touching this method. All missing ids are reported at once.
     */
    private static void requireDeclaredIdsRegistered() {
        List<String> ids = new ArrayList<>(Features.get().blockIds());
        ids.addAll(Gt6KtfruItems.BY_PATH.keySet());
        List<String> missing = ids.stream().filter(id -> stack(id).isEmpty()).toList();
        if (!missing.isEmpty()) {
            throw new IllegalStateException(missing.size() + " declared blocks or items are not registered: "
                    + String.join(", ", missing));
        }
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
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(Ktfru.MODID, path));
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }
}
