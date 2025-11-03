package graph.scc;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {

    @Test
    public void testSimpleSCC() {
        TarjanSCC tarjan = new TarjanSCC(5);
        tarjan.addEdge(0, 1);
        tarjan.addEdge(1, 2);
        tarjan.addEdge(2, 0);
        tarjan.addEdge(1, 3);
        tarjan.addEdge(3, 4);

        List<List<Integer>> sccs = tarjan.run();
        assertEquals(3, sccs.size(), "Should find 3 SCCs");
    }

    @Test
    public void testNoEdges() {
        TarjanSCC tarjan = new TarjanSCC(4);
        List<List<Integer>> sccs = tarjan.run();
        assertEquals(4, sccs.size(), "Each vertex is its own SCC");
    }

    @Test
    public void testSingleCycle() {
        TarjanSCC tarjan = new TarjanSCC(3);
        tarjan.addEdge(0, 1);
        tarjan.addEdge(1, 2);
        tarjan.addEdge(2, 0);
        List<List<Integer>> sccs = tarjan.run();
        assertEquals(1, sccs.size(), "One big SCC expected");
        assertTrue(sccs.get(0).containsAll(Arrays.asList(0,1,2)));
    }
}
