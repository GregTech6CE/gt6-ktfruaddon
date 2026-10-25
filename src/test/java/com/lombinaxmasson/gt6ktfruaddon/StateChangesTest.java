package com.lombinaxmasson.gt6ktfruaddon;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.StateChanges;
class StateChangesTest {
    @Test void unchangedValuesDoNotMarkTheMachineDirty() {
        var machine=new FakeMachine();var state=machine.context.state();
        StateChanges.setLong(state,"progress",0);StateChanges.setInt(state,"spin",0);StateChanges.setString(state,"status","");
        assertEquals(0,machine.stateWrites);
        StateChanges.setLong(state,"progress",3);StateChanges.setInt(state,"spin",2);StateChanges.setString(state,"status","running");
        assertEquals(3,machine.stateWrites);
        StateChanges.setLong(state,"progress",3);StateChanges.setInt(state,"spin",2);StateChanges.setString(state,"status","running");
        assertEquals(3,machine.stateWrites);
    }
}
