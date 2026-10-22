package com.lombinaxmasson.gt6ktfruaddon;

import java.util.List;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Chemistry membranes and battery poles from {@code itemChemistry} and
 * {@code itemBatteryPole}. Poles keep {@code ktfru.item.battery.pole} in the id
 * so the gas battery can recognize them.
 */
public final class Gt6KtfruItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.ITEM, Gt6KtfruAddon.MODID);

    public static final DeferredHolder<Item, Item> PROTON_EXCHANGE_MEMBRANE = item("proton_exchange_membrane");
    public static final DeferredHolder<Item, Item> ALKALINE_ION_EXCHANGE_MEMBRANE =
            item("alkaline_ion_exchange_membrane");
    public static final DeferredHolder<Item, Item> IR_ALKALINE_ION_EXCHANGE_MEMBRANE =
            item("ir_alkaline_ion_exchange_membrane");
    public static final DeferredHolder<Item, Item> BATTERY_POLE_NICKEL = item("ktfru.item.battery.pole.nickel");
    public static final DeferredHolder<Item, Item> BATTERY_POLE_CATIO3 = item("ktfru.item.battery.pole.catio3");
    public static final DeferredHolder<Item, Item> BATTERY_POLE_PLATINUM = item("ktfru.item.battery.pole.platinum");
    public static final DeferredHolder<Item, Item> BATTERY_POLE_CARBON = item("ktfru.item.battery.pole.carbon");

    public static final List<DeferredHolder<Item, Item>> ALL = List.of(
            PROTON_EXCHANGE_MEMBRANE, ALKALINE_ION_EXCHANGE_MEMBRANE, IR_ALKALINE_ION_EXCHANGE_MEMBRANE,
            BATTERY_POLE_NICKEL, BATTERY_POLE_CATIO3, BATTERY_POLE_PLATINUM, BATTERY_POLE_CARBON);

    private Gt6KtfruItems() {}

    private static DeferredHolder<Item, Item> item(String path) {
        return ITEMS.register(path, () -> new Item(new Item.Properties()));
    }
}
