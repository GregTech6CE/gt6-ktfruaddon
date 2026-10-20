package com.lombinaxmasson.gt6ktfruaddon;

import java.util.HashMap;
import java.util.Map;

import com.gregtech.gregtech.api.addon.MachineLayout;
import com.gregtech.gregtech.api.addon.StructureRole;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Large recipe controllers do not take the builder wand, so a right-click
 * never places their layout. This listener places it. The same facing rule as
 * the structure check: right is clockwise, back is opposite the facing.
 */
@EventBusSubscriber(modid = Gt6KtfruAddon.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class Gt6KtfruStructureBuild {
    private static final int WAND_DAMAGE = 10;
    private static Map<String, MachineLayout> layouts;

    private Gt6KtfruStructureBuild() {}

    @SubscribeEvent
    public static void useWand(PlayerInteractEvent.RightClickBlock event) {
        ItemStack wand = event.getItemStack();
        if (!builderWand(wand)) return;
        Level level = event.getLevel();
        BlockPos origin = event.getPos();
        BlockState state = level.getBlockState(origin);
        if (!state.hasProperty(HorizontalDirectionalBlock.FACING)) return;
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (!Gt6KtfruAddon.MODID.equals(blockId.getNamespace())) return;
        MachineLayout layout = layouts().get(blockId.getPath());
        if (layout == null) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        if (level.isClientSide()) return;
        Player player = event.getEntity();
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        if (place(level, player, event.getHand(), wand, origin, facing, layout)) {
            wand.hurtAndBreak(WAND_DAMAGE, player, handSlot(event.getHand()));
            level.playSound(null, origin, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private static boolean place(Level level, Player player, InteractionHand hand, ItemStack wand,
            BlockPos origin, Direction facing, MachineLayout layout) {
        boolean placed = false;
        for (MachineLayout.Cell cell : layout.cells()) {
            if (cell.role() == StructureRole.AIR) continue;
            Block part = partBlock(cell.part());
            if (part == Blocks.AIR) continue;
            BlockPos pos = origin.relative(facing.getClockWise(), cell.right())
                    .above(cell.up())
                    .relative(facing.getOpposite(), cell.back());
            if (!canEdit(level, player, pos)) continue;
            BlockState there = level.getBlockState(pos);
            if (there.is(part)) continue;
            if (!canReplace(there)) continue;
            ItemStack source = source(player, part);
            if (source.isEmpty() || !(source.getItem() instanceof BlockItem item)) continue;
            if (item.place(placeAt(level, player, hand, source, pos)).consumesAction()
                    && level.getBlockState(pos).is(part)) {
                placed = true;
            }
        }
        return placed;
    }

    private static BlockPlaceContext placeAt(Level level, Player player, InteractionHand hand, ItemStack stack, BlockPos pos) {
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false);
        return new BlockPlaceContext(level, player, hand, stack, hit) {
            @Override
            public BlockPos getClickedPos() {
                return pos;
            }

            @Override
            public boolean canPlace() {
                return true;
            }

            @Override
            public boolean replacingClickedOnBlock() {
                return true;
            }
        };
    }

    private static ItemStack source(Player player, Block part) {
        if (player.getAbilities().instabuild) return new ItemStack(part);
        for (int slot = player.getInventory().getContainerSize() - 1; slot >= 0; slot--) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.is(part.asItem())) return stack;
        }
        return ItemStack.EMPTY;
    }

    private static boolean canEdit(Level level, Player player, BlockPos pos) {
        return level.isInWorldBounds(pos)
                && level.hasChunkAt(pos)
                && level.getWorldBorder().isWithinBounds(pos)
                && level.mayInteract(player, pos);
    }

    private static boolean canReplace(BlockState state) {
        Block block = state.getBlock();
        return state.isAir()
                || state.canBeReplaced()
                || state.is(BlockTags.LEAVES)
                || block instanceof BushBlock
                || block instanceof SnowLayerBlock
                || block instanceof BaseFireBlock;
    }

    private static Block partBlock(String part) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("gt6ce", part);
        if (!BuiltInRegistries.BLOCK.containsKey(id)) return Blocks.AIR;
        return BuiltInRegistries.BLOCK.get(id);
    }

    private static boolean builderWand(ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return "gt6ce".equals(id.getNamespace()) && id.getPath().contains("builder_wand");
    }

    private static EquipmentSlot handSlot(InteractionHand hand) {
        return hand == InteractionHand.OFF_HAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
    }

    private static Map<String, MachineLayout> layouts() {
        if (layouts == null) {
            Map<String, MachineLayout> found = new HashMap<>();
            for (MachineLayout layout : new Gt6KtfruMachines().layouts()) {
                found.put(layout.machineName(), layout);
            }
            layouts = Map.copyOf(found);
        }
        return layouts;
    }
}
