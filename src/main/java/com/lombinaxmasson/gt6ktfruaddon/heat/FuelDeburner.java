package com.lombinaxmasson.gt6ktfruaddon.heat;

import java.util.List;

import com.gregtech.gregtech.api.addon.platform.AddonItems;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.RecipeRunner;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.Slots;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.StateChanges;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Fuel deburn factory. Temperature is kelvin stored in milli-units. Ambient is gregapi
 * {@code C}, 293 K. Self mass stays 81920 because item and fluid mass are not readable.
 * HU adds {@code energy * 64 / mass}. Cooling is {@code min(10 * |T - C| / mass, |T - C|)}.
 * Progress for the tick is {@code EU * temperature factor}, and one completion spends the
 * circuit and the fluids.
 */
public final class FuelDeburner extends BehaviorBase {
    static final long MASS = 81920;
    static final long AMBIENT_MILLI = 293_000;
    static final long MAX_MILLI = 4_000_000;
    static final int OUT = 3;
    static final Slots SLOTS = new Slots(List.of(0), List.of(), Slots.range(0, OUT), List.of(OUT));
    private final RecipeRunner runner = new RecipeRunner(SLOTS, List.of(RecipeMaps.FUEL_DEBURN))
            .step((context, line) -> Math.round(Math.max(1, line.euPerTick()) * factor(context.state().getLong("temp"), line)));

    @Override
    public void tick(MachineBehaviorContext context) {
        heat(context);
        if (!context.structureFormed() || context.state().getLong("temp") > MAX_MILLI) {
            runner.idle(context);
            return;
        }
        runner.tick(context);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return slot == 0 && context.item(0).isEmpty() && circuit(stack) >= 0;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return tank >= 0 && tank < OUT && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.deburn.temperature",
                context.state().getLong("temp") / 1000.0), false);
        return true;
    }

    private static void heat(MachineBehaviorContext context) {
        var state = context.state();
        long temp = state.getLong("temp");
        if (temp == 0) temp = AMBIENT_MILLI;
        long hu = context.energy("HU");
        if (hu > 0 && context.consume("HU", hu)) temp += hu * 64L * 1000L / MASS;
        long delta = Math.abs(temp - AMBIENT_MILLI);
        long cool = Math.min(10L * delta / MASS, delta);
        if (temp > AMBIENT_MILLI) temp -= cool;
        else temp += cool;
        StateChanges.setLong(state, "temp", temp);
    }

    /** Source {@code getTempFactor}. A zero difference is the cap, 128. */
    static double factor(long tempMilli, AddonRecipes.Line line) {
        double recipeFactor = 20.1 - 20.0 * Math.min(1.0, line.euPerTick() / 1024.0);
        if (recipeFactor <= 0) return 0;
        double delta = Math.abs(tempMilli / 1000.0 - line.special());
        if (delta < 1.0e-6) return 128;
        double value = (256.0 * recipeFactor / Math.sqrt(delta) - Math.pow(delta, 1.5) / recipeFactor) / recipeFactor;
        return Math.min(128.0, value);
    }

    static int circuit(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return -1;
        for (int n = 0; n <= 4; n++) {
            ItemStack tag = AddonItems.selectorTag(n);
            if (tag != null && !tag.isEmpty() && ItemStack.isSameItem(tag, stack)) return n;
        }
        return -1;
    }
}
