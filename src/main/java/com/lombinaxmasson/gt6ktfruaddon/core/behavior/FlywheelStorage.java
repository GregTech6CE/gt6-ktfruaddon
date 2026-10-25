package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import java.util.List;

import com.gregtech.gregtech.api.addon.GregTechMaterials;
import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.world.item.ItemStack;

/**
 * Capacity formulas adapted from kuzuanpa's kTFRUAddon 6404ab69:
 * item/items/itemFlywheel.java and tile/energy/storage/FlywheelBox.java.
 * Uses only the public prefix composition, material catalog and capacity APIs.
 */
public final class FlywheelStorage {
    public static final Wheel EMPTY = new Wheel(0, 0);

    private FlywheelStorage() {}

    public record Wheel(float rpm, long storage) {}
    public record Box(long capacity, float rpm) {}

    /** Recognize the prefix, never the item path: a machine named flywheel is not a wheel. */
    public static Wheel wheel(ItemStack stack) {
        if (stack.isEmpty()) return EMPTY;
        var composition = AddonItems.composition(stack);
        String name = materialName(composition);
        if (name.isEmpty()) return EMPTY;
        var material = GregTechMaterials.find(name);
        return material == null || !material.valid() ? EMPTY
                : wheel(material.mass(), material.toolQuality(), material.toolSpeed());
    }

    public static String materialName(GregTechMaterials.StackComposition composition) {
        if (!"flywheel".equals(composition.prefix()) || composition.units().size() != 1) return "";
        var unit = composition.units().getFirst();
        return unit.amount() > 0 ? unit.material() : "";
    }

    public static Wheel wheel(long mass, int quality, float speed) {
        if (mass <= 0 || speed <= 0 || !Float.isFinite(speed)) return EMPTY;
        float rpm = (float) (Math.pow(Math.max(0.4, quality), 2.4) * speed * 4);
        if (rpm <= 0 || !Float.isFinite(rpm)) return EMPTY;
        // Preserve source float rounding for ordinary catalog masses; avoid long multiplication overflow.
        long storage = mass <= Long.MAX_VALUE / 128
                ? (long) Math.floor(mass * 128 * rpm)
                : (long) Math.floor(mass * 128.0 * rpm);
        return new Wheel(rpm, Math.max(0, storage));
    }

    /** A box counts one wheel per occupied slot, as in the source, and runs at the slowest RPM. */
    public static Box box(List<Wheel> wheels, long packetMax, int amperes) {
        if (packetMax < 1 || amperes < 1) throw new IllegalArgumentException("invalid flywheel rating");
        float rpm = Float.MAX_VALUE;
        long perRotation = 0;
        for (Wheel wheel : wheels) {
            if (wheel.rpm() <= 0 || wheel.storage() <= 0) continue;
            rpm = Math.min(rpm, wheel.rpm());
            perRotation = add(perRotation, (long) Math.floor(wheel.storage() / wheel.rpm()));
        }
        if (perRotation == 0) return new Box(0, 0);
        rpm = Math.min(rpm, (float) ((double) amperes * packetMax));
        return new Box((long) (perRotation * rpm), rpm);
    }

    /** RU transform batteries sum each wheel's individual maximum, including the stack count. */
    public static long addWheels(long capacity, Wheel wheel, int count) {
        if (count <= 0 || wheel.storage() <= 0) return capacity;
        long amount = wheel.storage() > Long.MAX_VALUE / count ? Long.MAX_VALUE : wheel.storage() * count;
        return add(capacity, amount);
    }

    private static long add(long left, long right) {
        return right > Long.MAX_VALUE - left ? Long.MAX_VALUE : left + right;
    }

    /** Shrinking or emptying the inventory cannot retain energy above the new physical capacity. */
    public static void resize(MachineBehaviorContext context, long capacity) {
        if (capacity < 0) throw new IllegalArgumentException("negative flywheel capacity");
        if (context.energy() > capacity) context.consume(context.energy() - capacity);
        if (context.capacity() != capacity) context.setCapacity(capacity);
    }
}
