package com.lombinaxmasson.gt6ktfruaddon;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.gregtech.gregtech.api.addon.BehaviorMachine;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.RecipeRunner;
class RecipeSlotContractTest {
    @Test void runtimeTransactionSlotsMatchEveryDeclaredInventoryRole() throws Exception {
        int checked=0;
        for(var declaration:Declarations.machines()) {
            if(!(declaration instanceof BehaviorMachine machine)) continue;
            var behavior=Declarations.machineContributor().create(machine.casing().machineType());
            for(var field:behavior.getClass().getDeclaredFields()) {
                if(field.getType()!=RecipeRunner.class) continue;
                field.setAccessible(true);var slots=((RecipeRunner)field.get(behavior)).slots();
                String id=machine.casing().id();
                assertEquals(machine.items().size(),slots.itemsIn().size()+slots.itemsOut().size(),id);
                assertEquals(machine.fluids().size(),slots.fluidsIn().size()+slots.fluidsOut().size(),id);
                for(int i=0;i<machine.items().size();i++) assertEquals(machine.items().get(i).role(),slots.itemRole(i),id+" item "+i);
                for(int i=0;i<machine.fluids().size();i++) assertEquals(machine.fluids().get(i).role(),slots.fluidRole(i),id+" fluid "+i);
                checked++;
            }
        }
        assertEquals(10,checked);
    }
}
