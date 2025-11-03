package graph.dagsp;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DagShortestPathTest {

    @Test
    public void testShortestPath() {
        DAGShortestPath dag = new DAGShortestPath(6);
        dag.addEdge(0, 1, 5);
        dag.addEdge(0, 2, 3);
        dag.addEdge(1, 3, 6);
        dag.addEdge(1, 2, 2);
        dag.addEdge(2, 4, 4);
        dag.addEdge(2, 5, 2);
        dag.addEdge(2, 3, 7);
        dag.addEdge(3, 4, -1);
        dag.addEdge(4, 5, -2);

        dag.shortestPaths(1);
        assertEquals(0.0, dag.getDistance(1));
        assertTrue(dag.getDistance(3) <= 6);
    }
    @Test
    public void testLongestPath() {
        DAGShortestPath dag = new DAGShortestPath(5);
        dag.addEdge(0, 1, 2);
        dag.addEdge(0, 2, 3);
        dag.addEdge(1, 3, 4);
        dag.addEdge(2, 3, 2);
        dag.addEdge(3, 4, 1);

        dag.longestPaths(0);
        assertEquals(7.0, dag.getDistance(4), 0.01);
    }

    @Test
    public void testSingleNode() {
        DAGShortestPath dag = new DAGShortestPath(1);
        dag.shortestPaths(0);
        assertEquals(0.0, dag.getDistance(0));
    }
}
