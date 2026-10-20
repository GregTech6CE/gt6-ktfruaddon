package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.GregTechMaterials;
import com.gregtech.gregtech.api.addon.MachineDeclaration;

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
@EventBusSubscriber(modid = Gt6KtfruAddon.MODID, value = Dist.CLIENT)
public final class Gt6KtfruMachineColors {
    private Gt6KtfruMachineColors() {}

    @SubscribeEvent
    public static void blocks(RegisterColorHandlersEvent.Block event) {
        for (MachineDeclaration machine : new Gt6KtfruMachines().machines()) {
            Block block = block(machine.id());
            if (block == null) continue;
            String material = machine.material();
            event.register((state, level, pos, index) -> color(material), block);
        }
    }

    @SubscribeEvent
    public static void items(RegisterColorHandlersEvent.Item event) {
        for (MachineDeclaration machine : new Gt6KtfruMachines().machines()) {
            Block block = block(machine.id());
            if (block == null) continue;
            String material = machine.material();
            event.register((stack, index) -> color(material), block);
        }
    }

    private static Block block(String id) {
        ResourceLocation name = ResourceLocation.fromNamespaceAndPath(Gt6KtfruAddon.MODID, id);
        if (!BuiltInRegistries.BLOCK.containsKey(name)) return null;
        return BuiltInRegistries.BLOCK.get(name);
    }

    private static int color(String material) {
        GregTechMaterials.Material found = GregTechMaterials.find(material);
        if (found == null || !found.valid()) return 0xFFFFFF;
        return found.color() & 0xFFFFFF;
    }
}
