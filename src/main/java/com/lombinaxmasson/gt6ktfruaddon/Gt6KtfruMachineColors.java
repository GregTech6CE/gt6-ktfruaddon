package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.AddonMachine;
import com.gregtech.gregtech.api.addon.GregTechMaterials;
import com.lombinaxmasson.gt6ktfruaddon.registry.Features;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/**
 * The casing layer uses tint index 0. GT only paints its own machines, so the
 * material color comes from the public material catalog.
 */
@EventBusSubscriber(modid = Ktfru.MODID, value = Dist.CLIENT)
public final class Gt6KtfruMachineColors {
    private Gt6KtfruMachineColors() {}

    @SubscribeEvent
    public static void blocks(RegisterColorHandlersEvent.Block event) {
        paintAll((id, material) -> paint(event, id, material));
    }

    @SubscribeEvent
    public static void items(RegisterColorHandlersEvent.Item event) {
        paintAll((id, material) -> paint(event, id, material));
    }

    private static void paintAll(java.util.function.BiConsumer<String, String> paint) {
        for (AddonMachine machine : Features.get().machines())
            paint.accept(machine.casing().id(), machine.casing().material());
    }

    private static void paint(RegisterColorHandlersEvent.Block event, String id, String material) {
        Block block = block(id);
        if (block != null) event.register((state, level, pos, index) -> color(material), block);
    }

    private static void paint(RegisterColorHandlersEvent.Item event, String id, String material) {
        Block block = block(id);
        if (block != null) event.register((stack, index) -> color(material), block);
    }

    private static Block block(String id) {
        ResourceLocation name = ResourceLocation.fromNamespaceAndPath(Ktfru.MODID, id);
        if (!BuiltInRegistries.BLOCK.containsKey(name)) return null;
        return BuiltInRegistries.BLOCK.get(name);
    }

    private static int color(String material) {
        GregTechMaterials.Material found = GregTechMaterials.find(material);
        if (found == null || !found.valid()) return 0xFFFFFF;
        return found.color() & 0xFFFFFF;
    }
}
