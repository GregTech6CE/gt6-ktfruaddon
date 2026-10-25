package com.lombinaxmasson.gt6ktfruaddon.quantum;

import java.util.List;
import java.util.UUID;

import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.BehaviorBase;
import com.lombinaxmasson.gt6ktfruaddon.recipe.RecipeMaps;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.PlayerMemory;
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

    private static final RelayPairing PAIRING = new RelayPairing();
    private record Mark(DimPos key, long revision) {}
    private static final PlayerMemory<Mark> PENDING = new PlayerMemory<>(mark -> mark.key().dim());

    private record Endpoint(com.gregtech.gregtech.api.addon.platform.BehaviorState state) implements RelayPairing.Endpoint {
        public BlockPos partner() { return PosCodec.parse(state.getString("partner")); }
        public long revision() { return state.getLong("partner_revision"); }
        public void link(BlockPos partner, long revision) {
            state.setString("partner", PosCodec.format(partner));
            state.setLong("partner_revision", revision);
        }
    }

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
        if (context.energy() >= QU_PER_TICK) {
            context.emit(QU_PER_TICK, 1);
        }
        long now = context.level().getGameTime();
        PAIRING.update(self, new Endpoint(context.state()), now, key -> context.level().hasChunkAt(key.pos()));
        if (!PAIRING.paired(self, now, key -> context.level().hasChunkAt(key.pos()))) {
            runner.idle(context);
            return;
        }
        runner.tick(context);
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
        PAIRING.remove(new DimPos(dim, context.pos().immutable()));

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
        PAIRING.update(self, new Endpoint(context.state()), context.level().getGameTime(),
                key -> context.level().hasChunkAt(key.pos()));
        Mark pending = PENDING.get(player.getUUID());
        if (player.isShiftKeyDown() || pending == null || pending.key().pos().equals(here)) {
            PENDING.put(player.getUUID(), new Mark(self, context.state().getLong("partner_revision")));
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.quantum_relay.recorded",
                    here.getX(), here.getY(), here.getZ()), false);
            return true;
        }
        if (!dim.equals(pending.key().dim())) {
            PENDING.remove(player.getUUID());
            player.displayClientMessage(Component.translatable("gt6ktfruaddon.quantum_relay.same_dimension"), false);
            return true;
        }
        PENDING.remove(player.getUUID());
        PAIRING.pair(self, new Endpoint(context.state()), pending.key(), pending.revision(), context.level().getGameTime(),
                key -> context.level().hasChunkAt(key.pos()));
        player.displayClientMessage(Component.translatable("gt6ktfruaddon.quantum_relay.linked",
                pending.key().pos().getX(), pending.key().pos().getY(), pending.key().pos().getZ(),
                context.energy(), ENERGY_CAPACITY), false);
        return true;

    }

}
