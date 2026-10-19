package com.lombinaxmasson.ktfruaddon;

import org.slf4j.Logger;

import com.gregtech.gregtech.api.addon.GregTechAddon;
import com.gregtech.gregtech.api.addon.GregTechAddons;
import com.gregtech.gregtech.api.addon.GregTechMaterials;
import com.gregtech.gregtech.api.addon.platform.AddonFluids;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
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
@Mod(KtfruAddon.MODID)
public final class KtfruAddon {
    public static final String MODID = "ktfruaddon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB =
            CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ktfruaddon"))
                    .withTabsBefore(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                    .icon(() -> stack("crystal_puller"))
                    .displayItems((parameters, output) -> {
                        accept(output, "crystal_puller");
                        accept(output, "hydrogen_burner");
                    })
                    .build());

    public KtfruAddon(IEventBus modEventBus, ModContainer modContainer) {
        CREATIVE_MODE_TABS.register(modEventBus);
        GregTechAddons.register(new GregTechAddon() {
            @Override
            public String id() {
                return MODID;
            }

            @Override
            public void onRecipesReady(Context context) {
                LOGGER.info("ktfruaddon: ready on {} {} (GT6CE addon API {})",
                        context.platform(), context.minecraftVersion(), context.apiVersion());
                if (!GregTechMaterials.find("Silicon").valid())
                    throw new IllegalStateException("built-in Silicon is missing");
                ItemStack silicon = AddonItems.stack("dust", "Silicon", 1);
                ItemStack pulled = AddonItems.stack("dust", "MonocrystallineSilicon", 1);
                if (silicon.isEmpty() || pulled.isEmpty())
                    throw new IllegalStateException("silicon dust or monocrystalline dust is missing");
                FluidStack hydrogen = AddonFluids.still("Hydrogen", 250);
                if (hydrogen == null || hydrogen.isEmpty())
                    throw new IllegalStateException("GT fluid field Hydrogen is missing");
                AddonRecipes.Line added = AddonRecipes.map("ktfruaddon.recipe.crystal_puller").add(
                        new AddonRecipes.Line(
                                new ItemStack[] { silicon },
                                new ItemStack[] { pulled },
                                new FluidStack[0],
                                new FluidStack[0],
                                200,
                                128,
                                0));
                if (added == null)
                    throw new IllegalStateException("crystal puller rejected the silicon row");
            }
        });
    }

    private static void accept(CreativeModeTab.Output output, String path) {
        ItemStack stack = stack(path);
        if (!stack.isEmpty()) output.accept(stack);
    }

    private static ItemStack stack(String path) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(MODID, path));
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }
}
