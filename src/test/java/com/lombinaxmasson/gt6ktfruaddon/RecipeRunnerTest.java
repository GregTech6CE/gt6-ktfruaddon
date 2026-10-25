package com.lombinaxmasson.gt6ktfruaddon;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import com.gregtech.gregtech.api.addon.platform.AddonRecipes;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.*;
class RecipeRunnerTest {
    static final Slots EMPTY = new Slots(List.of(),List.of(),List.of(),List.of());
    static AddonRecipes.Line line(long duration,long eu) { return new AddonRecipes.Line(null,null,null,null,duration,eu,0); }
    @Test void progressesAndPaysOncePerTickThenCompletes() {
        var machine=new FakeMachine();machine.energy=20;int[] completed={0};
        var runner=new RecipeRunner(EMPTY,c->line(2,3)).after(c->completed[0]++);
        runner.tick(machine.context);assertEquals(17,machine.energy);assertEquals(1,machine.longs.get("progress"));assertTrue(machine.working);
        runner.tick(machine.context);assertEquals(14,machine.energy);assertEquals(0,machine.longs.get("progress"));assertEquals(1,completed[0]);
    }
    @Test void insufficientEnergyPreservesProgressAndDoesNotLightMachine() {
        var machine=new FakeMachine();machine.energy=2;machine.longs.put("progress",1L);
        new RecipeRunner(EMPTY,c->line(2,3)).tick(machine.context);
        assertEquals(1,machine.longs.get("progress"));assertFalse(machine.working);assertEquals(2,machine.energy);
    }
    @Test void completedProgressDoesNotPayAgain() {
        var machine=new FakeMachine();machine.energy=20;machine.longs.put("progress",2L);
        new RecipeRunner(EMPTY,c->line(2,3)).tick(machine.context);
        assertEquals(20,machine.energy);assertEquals(0,machine.longs.get("progress"));assertFalse(machine.working);
    }
    @Test void auxiliaryEnergyAndCustomProgressAreSupported() {
        var machine=new FakeMachine();machine.auxiliary.put("EU",20L);
        new RecipeRunner(EMPTY,c->line(10,3)).energy("EU").step((c,l)->4).tick(machine.context);
        assertEquals(17,machine.auxiliary.get("EU"));assertEquals(4,machine.longs.get("progress"));
    }
    @Test void missingOrLockedRecipeResetsProgressWithoutSpendingEnergy() {
        for (boolean missing:new boolean[]{true,false}) {
            var machine=new FakeMachine();machine.energy=20;machine.longs.put("progress",3L);
            new RecipeRunner(EMPTY,c->missing?null:line(10,3)).requires((c,l)->false).tick(machine.context);
            assertEquals(0,machine.longs.get("progress"));assertEquals(20,machine.energy);assertFalse(machine.working);
        }
    }
    @Test void completeCanRunIndependentlyOfTick() {
        var machine=new FakeMachine();int[] count={0};
        assertTrue(new RecipeRunner(EMPTY,c->null).after(c->count[0]++).complete(machine.context,line(2,3)));
        assertEquals(1,count[0]);assertEquals(0,machine.energy);
    }
}
