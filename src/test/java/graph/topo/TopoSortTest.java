package graph.topo;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TopoSortTest {

    @Test
    public void testBasicTopoOrder() {
        TopologicalSort topo = new TopologicalSort(6);
        topo.addEdge(5, 2);
        topo.addEdge(5, 0);
        topo.addEdge(4, 0);
        topo.addEdge(4, 1);
        topo.addEdge(2, 3);
        topo.addEdge(3, 1);

        List<Integer> order = topo.kahnSort();
        assertEquals(6, order.size());
        assertTrue(order.indexOf(5) < order.indexOf(2));
        assertTrue(order.indexOf(2) < order.indexOf(3));
        assertTrue(order.indexOf(3) < order.indexOf(1));
    }

    @Test
    public void testEmptyGraph() {
        TopologicalSort topo = new TopologicalSort(3);
        List<Integer> order = topo.kahnSort();
        assertEquals(3, order.size());
    }

    @Test
    public void testDisconnectedGraph() {
        TopologicalSort topo = new TopologicalSort(4);
        topo.addEdge(0, 1);
        topo.addEdge(2, 3);
        List<Integer> order = topo.kahnSort();
        assertEquals(4, order.size());
    }
}
