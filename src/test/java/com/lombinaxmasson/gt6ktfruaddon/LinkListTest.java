package com.lombinaxmasson.gt6ktfruaddon;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import net.minecraft.core.BlockPos;
import com.lombinaxmasson.gt6ktfruaddon.core.behavior.*;
class LinkListTest {
    private static final DimPos A = new DimPos("minecraft:overworld",new BlockPos(-1,2,3));
    @Test void readsSingleLegacyPosition() { assertEquals(List.of(A),new LinkList().read("-1,2,3",A.dim())); }
    @Test void readsLegacySemicolonList() {
        assertEquals(2,new LinkList().read("-1,2,3;4,5,6",A.dim()).size());
    }
    @Test void readsDimensionPrefixedLegacyList() {
        var links=new LinkList().read("minecraft:overworld,-1,2,3|minecraft:the_nether,4,5,6",A.dim());
        assertEquals(A,links.getFirst()); assertEquals("minecraft:the_nether",links.get(1).dim());
    }
    @Test void newFormatRoundTripsAcrossDimensions() {
        var expected=List.of(A,new DimPos("minecraft:the_nether",new BlockPos(4,5,6)));
        assertEquals(expected,new LinkList().read(LinkList.write(expected),"minecraft:the_end"));
    }
    @Test void cacheIsImmutableAndInvalidatedByTextOrDimension() {
        var codec=new LinkList();var first=codec.read("-1,2,3",A.dim());
        assertSame(first,codec.read("-1,2,3",A.dim()));
        assertThrows(UnsupportedOperationException.class,()->first.add(A));
        assertEquals("minecraft:the_end",codec.read("-1,2,3","minecraft:the_end").getFirst().dim());
        assertEquals(4,codec.read("4,5,6",A.dim()).getFirst().pos().getX());
    }
    @Test void corruptEntriesDoNotHideValidLinks() {
        assertEquals(List.of(A),new LinkList().read("garbage|bad dim,1,2,3|-1,2,3|1,overflow,3",A.dim()));
        assertEquals(List.of(),new LinkList().read(null,A.dim()));
        assertEquals("",LinkList.write(List.of()));
    }
}
