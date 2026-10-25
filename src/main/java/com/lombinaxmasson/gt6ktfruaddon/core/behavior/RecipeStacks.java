package com.lombinaxmasson.gt6ktfruaddon.core.behavior;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/** Minecraft adapters; matching uses the public stack API, never GT implementation classes. */
final class RecipeStacks {
    private RecipeStacks() {}
    static final Ledger.Stack<ItemStack> ITEMS = new Ledger.Stack<>() {
        public int amount(ItemStack stack) { return stack == null || stack.isEmpty() ? 0 : stack.getCount(); }
        public ItemStack amount(ItemStack stack, int amount) { return stack.copyWithCount(amount); }
        public boolean same(ItemStack a, ItemStack b) { return ItemStack.isSameItemSameComponents(a, b); }
    };
    static final Ledger.Stack<FluidStack> FLUIDS = new Ledger.Stack<>() {
        public int amount(FluidStack stack) { return stack == null || stack.isEmpty() ? 0 : stack.getAmount(); }
        public FluidStack amount(FluidStack stack, int amount) { return stack.copyWithAmount(amount); }
        public boolean same(FluidStack a, FluidStack b) { return FluidStack.isSameFluidSameComponents(a, b); }
    };
}
