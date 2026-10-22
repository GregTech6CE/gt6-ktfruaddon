package com.lombinaxmasson.gt6ktfruaddon;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/** The microgravity room spins while it can pay 2048 EU. It does not change world rules. AI centers do not run minigames. */
public final class Gt6KtfruLateBehavior implements MachineBehavior {
    private final boolean room;
    private final long points;

    public Gt6KtfruLateBehavior(boolean room, long points) {
        this.room = room;
        this.points = points;
    }

    @Override
    public void tick(MachineBehaviorContext context) {
        if (!room) {
            context.working(false);
            return;
        }
        if (!context.structureFormed() || !context.consume(2048)) {
            context.state().setInt("spin", 0);
            context.working(false);
            context.progress(0);
            return;
        }
        int spin = Math.min(600, context.state().getInt("spin") + 1);
        context.state().setInt("spin", spin);
        context.progress(spin / 600.0F);
        context.working(spin >= 600);
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return false;
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (room || !AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.ai.points",
                points, context.structureFormed() ? 1 : 0), false);
        return true;
    }
}
