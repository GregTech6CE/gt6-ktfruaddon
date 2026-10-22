package com.lombinaxmasson.gt6ktfruaddon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.gregtech.gregtech.api.addon.platform.AddonTools;
import com.gregtech.gregtech.api.addon.platform.MachineBehavior;
import com.gregtech.gregtech.api.addon.platform.MachineBehaviorContext;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Paired quantum communication relay ({@code QuantumCommunicationRelay}, 30074).
 * Accepts EU through {@code auxiliaryEnergy}, stores up to 32,768,000 QU in the main buffer,
 * adds 65,536 QU per completed recipe while paired in the same dimension, and emits 128 QU/tick from the top face.
 */
public final class Gt6KtfruQuantumRelay implements MachineBehavior {
    public static final long QU_PER_TICK = 128L;
    public static final long QU_PER_RECIPE = 65_536L;
    public static final long ENERGY_CAPACITY = 32_768_000L;
    private static final long EXPIRY_TICKS = 20L;

    private static final Map<Key, RelayEntry> RELAYS = new HashMap<>();
    private static final Map<UUID, Key> PENDING = new HashMap<>();
    private static final Map<Key, BlockPos> NEW_PARTNERS = new HashMap<>();

    @Override
    public void tick(MachineBehaviorContext context) {
        if (context.capacity() != ENERGY_CAPACITY) {
            context.setCapacity(ENERGY_CAPACITY);
        }
        String dim = context.level().dimension().location().toString();
        Key self = new Key(dim, context.pos().immutable());
        synchronized (RELAYS) {
            BlockPos newlyLinked = NEW_PARTNERS.remove(self);
            if (newlyLinked != null) {
                context.state().setString("partner", formatPos(newlyLinked));
            }
        }
        if (context.energy() >= QU_PER_TICK) {
            context.emit(QU_PER_TICK, 1);
        }
        BlockPos partnerPos = parsePos(context.state().getString("partner"));
        long now = context.level().getGameTime();
        long euStored = context.energy("EU");
        synchronized (RELAYS) {
            RELAYS.put(self, new RelayEntry(partnerPos, euStored, now));
        }
        if (partnerPos == null || !isPaired(self, partnerPos, now)) {
            context.working(false);
            context.progress(0);
            return;
        }
        AddonRecipes.Line line = AddonRecipes.map(Gt6KtfruLateMachines.RELAY_MAP).find(
                List.of(context.item(0)),
                List.of(),
                true, 1, 0);
        if (line == null) {
            context.state().setLong("progress", 0);
            context.progress(0);
            context.working(false);
            return;
        }
        long duration = Math.max(1L, line.duration());
        long progress = context.state().getLong("progress");
        long eu = Math.max(1L, line.euPerTick());
        if (progress >= duration) {
            if (finish(context, line)) progress = 0;
        } else if (context.energy("EU") >= eu && context.consume("EU", eu)) {
            progress++;
            if (progress >= duration && finish(context, line)) progress = 0;
            context.working(true);
        } else {
            context.working(false);
        }
        context.state().setLong("progress", progress);
        context.progress(Math.min(1.0F, progress / (float) duration));
    }

    @Override
    public void beforeBreak(MachineBehaviorContext context) {
        String dim = context.level().dimension().location().toString();
        synchronized (RELAYS) {
            RELAYS.remove(new Key(dim, context.pos().immutable()));
        }
    }

    @Override
    public boolean canInsert(MachineBehaviorContext context, int slot, ItemStack stack) {
        return slot == 0 && stack != null && !stack.isEmpty();
    }

    @Override
    public boolean canFill(MachineBehaviorContext context, int tank, FluidStack stack) {
        return false;
    }

    @Override
    public boolean onUse(MachineBehaviorContext context, Player player, InteractionHand hand) {
        if (!AddonTools.isMagnifyingGlass(player.getItemInHand(hand))) return false;
        String dim = context.level().dimension().location().toString();
        BlockPos here = context.pos().immutable();
        Key self = new Key(dim, here);
        synchronized (RELAYS) {
            Key pending = PENDING.get(player.getUUID());
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
    }

    private static boolean isPaired(Key self, BlockPos partnerPos, long now) {
        synchronized (RELAYS) {
            RelayEntry partner = RELAYS.get(new Key(self.dim(), partnerPos));
            if (partner == null || now - partner.lastTick() > EXPIRY_TICKS) return false;
            return self.pos().equals(partner.partner());
        }
    }

    private static boolean finish(MachineBehaviorContext context, AddonRecipes.Line line) {
        ItemStack[] inItems = line.itemInputs();
        if (inItems != null && inItems.length > 0 && inItems[0] != null && !inItems[0].isEmpty()) {
            ItemStack have = context.item(0);
            if (have.isEmpty() || !ItemStack.isSameItemSameComponents(have, inItems[0])
                    || have.getCount() < inItems[0].getCount()) {
                return false;
            }
        }
        ItemStack[] outItems = line.itemOutputs();
        if (outItems != null && outItems.length > 0 && outItems[0] != null && !outItems[0].isEmpty()) {
            ItemStack have = context.item(1);
            if (!have.isEmpty() && (!ItemStack.isSameItemSameComponents(have, outItems[0])
                    || have.getCount() + outItems[0].getCount() > have.getMaxStackSize())) {
                return false;
            }
        }
        if (inItems != null && inItems.length > 0 && inItems[0] != null && !inItems[0].isEmpty() && !kept(line, 0)) {
            ItemStack have = context.item(0).copy();
            have.shrink(inItems[0].getCount());
            context.setItem(0, have.isEmpty() ? ItemStack.EMPTY : have);
        }
        if (outItems != null && outItems.length > 0 && outItems[0] != null && !outItems[0].isEmpty()) {
            ItemStack out = outItems[0].copy();
            ItemStack have = context.item(1);
            if (!have.isEmpty()) out.grow(have.getCount());
            context.setItem(1, out);
        }
        context.generate(QU_PER_RECIPE);
        return true;
    }

    private static boolean kept(AddonRecipes.Line line, int index) {
        int[] kept = line.keptInputs();
        if (kept == null) return false;
        for (int k : kept) if (k == index) return true;
        return false;
    }

    private static String formatPos(BlockPos pos) {
        return pos.getX() + "," + pos.getY() + "," + pos.getZ();
    }

    private static BlockPos parsePos(String raw) {
        if (raw == null || raw.isEmpty()) return null;
        String[] parts = raw.split(",");
        if (parts.length != 3) return null;
        try {
            return new BlockPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private record Key(String dim, BlockPos pos) {}
    private record RelayEntry(BlockPos partner, long euStored, long lastTick) {}
}
