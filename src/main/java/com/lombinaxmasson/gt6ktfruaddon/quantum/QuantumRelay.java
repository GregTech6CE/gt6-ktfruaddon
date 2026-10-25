package com.lombinaxmasson.gt6ktfruaddon.quantum;

import java.util.List;
import java.util.UUID;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.TtlRegistry;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PlayerMemory;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimensionMemory;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.DimPos;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PosCodec;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.RecipeRunner;

import com.lombinaxmasson.gt6ktfruaddon.core.behavior.Slots;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Paired quantum communication relay ({@code QuantumCommunicationRelay}, 30074).
 * Accepts EU through {@code auxiliaryEnergy}, stores up to 32,768,000 QU in the main buffer,
 * adds 65,536 QU per completed recipe while paired in the same dimension, and emits 128 QU/tick from the top face.
 */
public final class QuantumRelay extends BehaviorBase {
    public static final long QU_PER_TICK = 128L;
    public static final long QU_PER_RECIPE = 65_536L;
    public static final long ENERGY_CAPACITY = 32_768_000L;

    private static final TtlRegistry<RelayEntry> RELAYS = new TtlRegistry<>();
    private static final PlayerMemory<DimPos> PENDING = new PlayerMemory<>(DimPos::dim);
    private static final DimensionMemory<BlockPos> NEW_PARTNERS = new DimensionMemory<>();

    static final Slots SLOTS = new Slots(List.of(0), List.of(1), List.of(), List.of());
    private final RecipeRunner runner = new RecipeRunner(SLOTS, List.of(RecipeMaps.QUANTUM_COMMUNICATION))
            .energy("EU").after(context -> context.generate(QU_PER_RECIPE));

    @Override
    public void tick(MachineBehaviorContext context) {
        if (context.capacity() != ENERGY_CAPACITY) {
            context.setCapacity(ENERGY_CAPACITY);
        }
        String dim = context.level().dimension().location().toString();
        DimPos self = new DimPos(dim, context.pos().immutable());
        BlockPos newlyLinked = NEW_PARTNERS.remove(self);
        if (newlyLinked != null) {
            context.state().setString("partner", formatPos(newlyLinked));
        }

        if (context.energy() >= QU_PER_TICK) {
            context.emit(QU_PER_TICK, 1);
        }
        BlockPos partnerPos = parsePos(context.state().getString("partner"));
        long now = context.level().getGameTime();
        long euStored = context.energy("EU");
        RELAYS.put(self, new RelayEntry(partnerPos, euStored), now);

        if (partnerPos == null || !isPaired(self, partnerPos, now)) {
            runner.idle(context);
            return;
        }
        runner.tick(context);
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
                RELAYS.remove(new DimPos(dim, context.pos().immutable()));

    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return slot == 0 && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        String dim = context.level().dimension().location().toString();
        BlockPos here = context.pos().immutable();
        DimPos self = new DimPos(dim, here);
        DimPos pending = PENDING.get(player.getUUID());
        if (player.isShiftKeyDown() || pending == null || pending.pos().equals(here)) {
            PENDING.put(player.getUUID(), self);
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.quantum_relay.recorded",
                    here.getX(), here.getY(), here.getZ()), false);
            return true;
        }
        if (!dim.equals(pending.dim())) {
            PENDING.remove(player.getUUID());
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.quantum_relay.same_dimension"), false);
            return true;
        }
        PENDING.remove(player.getUUID());
        context.state().setString("partner", formatPos(pending.pos()));
        NEW_PARTNERS.put(pending, here);
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.quantum_relay.linked",
                pending.pos().getX(), pending.pos().getY(), pending.pos().getZ(),
                context.energy(), ENERGY_CAPACITY), false);
        return true;

    }

    private static boolean isPaired(DimPos self, BlockPos partnerPos, long now) {
        RelayEntry partner = RELAYS.get(new DimPos(self.dim(), partnerPos), now);
        if (partner == null) return false;
        return self.pos().equals(partner.partner());

    }

    private static String formatPos(BlockPos pos) { return PosCodec.format(pos); }

    private static BlockPos parsePos(String raw) { return PosCodec.parse(raw); }

    private record RelayEntry(BlockPos partner, long euStored) {}
}
