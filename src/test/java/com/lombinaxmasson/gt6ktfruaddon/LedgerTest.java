package com.lombinaxmasson.gt6ktfruaddon;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.Ledger;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.Slots;
class LedgerTest {
    record Stack(String type, int amount) {}
    static final Ledger.Stack<Stack> STACKS = new Ledger.Stack<>() {
        public int amount(Stack s) { return s.amount(); }
        public Stack amount(Stack s, int amount) { return new Stack(s.type(), amount); }
        public boolean same(Stack a, Stack b) { return a.type().equals(b.type()); }
    };
    final Ledger<Stack> ledger = new Ledger<>(STACKS);
    static Stack s(String type,int n) { return new Stack(type,n); }
    static Ledger.Need<Stack> need(int n,boolean kept) { return new Ledger.Need<>(s("a",n),kept); }
    @Test void inputCanSpanStacks() {
        assertEquals(List.of(s("a",0),s("a",1)),ledger.consume(List.of(s("a",2),s("a",3)),List.of(need(4,false)),STACKS::same));
    }
    @Test void repeatedIngredientsCannotDoubleSpendOneStack() {
        var inventory=List.of(s("a",3));
        assertNull(ledger.consume(inventory,List.of(need(2,false),need(2,false)),STACKS::same));
        assertEquals(List.of(s("a",3)),inventory);
    }
    @Test void catalystsReserveTheirShareRegardlessOfOrder() {
        for (var needs:List.of(List.of(need(2,true),need(2,false)),List.of(need(2,false),need(2,true)))) {
            assertNull(ledger.consume(List.of(s("a",3)),needs,STACKS::same));
            assertEquals(List.of(s("a",2)),ledger.consume(List.of(s("a",4)),needs,STACKS::same));
        }
    }
    @Test void keptFluidAcrossTanksIsCheckedAndRetained() {
        assertEquals(List.of(s("a",2),s("a",3)),ledger.consume(List.of(s("a",2),s("a",3)),List.of(need(5,true)),STACKS::same));
    }
    @Test void consumesOnlyMatchingInputs() {
        assertNull(ledger.consume(List.of(s("b",100)),List.of(need(1,false)),STACKS::same));
    }
    @Test void matcherCanRepresentEquivalentIngredients() {
        assertEquals(List.of(s("b",1)),ledger.consume(List.of(s("b",2)),List.of(need(1,false)),(a,b)->true));
    }
    @Test void outputsShareSpaceAcrossTheWholeTransaction() {
        var inventory=List.of(s("",0));
        assertNull(ledger.produce(inventory,List.of(s("a",40),s("a",30)),(i,s)->64));
        assertNull(ledger.produce(inventory,List.of(s("a",1),s("b",1)),(i,s)->64));
        assertEquals(List.of(s("",0)),inventory);
    }
    @Test void mergesBeforeUsingEmptySlotsAndSplitsLargeOutputs() {
        assertEquals(List.of(s("a",64),s("a",64),s("a",12)),ledger.produce(
                List.of(s("a",60),s("",0),s("",0)),List.of(s("a",80)),(i,s)->64));
    }
    @Test void fluidTankCapacitiesAreIndividualAndEmptyTanksAreChecked() {
        assertEquals(List.of(s("a",2),s("a",5)),ledger.produce(List.of(s("",0),s("",0)),List.of(s("a",7)),(i,s)->i==0?2:5));
        assertNull(ledger.produce(List.of(s("",0)),List.of(s("a",3)),(i,s)->2));
    }
    @Test void slotsRejectDuplicatesAndCopyCallerLists() {
        assertThrows(IllegalArgumentException.class,()->new Slots(List.of(0),List.of(0),List.of(),List.of()));
        assertThrows(IllegalArgumentException.class,()->new Slots(List.of(-1),List.of(),List.of(),List.of()));
        var source=new java.util.ArrayList<>(List.of(0));var slots=new Slots(source,List.of(1),List.of(),List.of());
        source.clear();assertEquals(List.of(0),slots.itemsIn());
    }
}
