package com.lombinaxmasson.gt6ktfruaddon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.lombinaxmasson.gt6ktfruaddon.core.Gt6ce;

import net.minecraft.resources.ResourceLocation;

class Gt6ceTest {
    @Test
    void namingConventionsStayScopedToGt6ce() {
        assertTrue(Gt6ce.builderWand(Gt6ce.id("builder_wand")));
        assertFalse(Gt6ce.builderWand(ResourceLocation.parse("other:builder_wand")));
        assertFalse(Gt6ce.owns(null));
        assertEquals("", Gt6ce.springPath(ResourceLocation.parse("other:fluid_spring_lava")));
        assertEquals("", Gt6ce.springPath(Gt6ce.id("not_a_spring")));
        assertEquals("fluid_spring_lava", Gt6ce.springPath(Gt6ce.id("fluid_spring_lava")));
        assertEquals("lava", Gt6ce.springFluid("fluid_spring_lava"));
        assertThrows(IllegalArgumentException.class, () -> Gt6ce.springFluid("lava"));
        assertEquals(Gt6ce.id("fluid_item_water"), Gt6ce.fluidItem(ResourceLocation.parse("minecraft:water")));
    }
}
