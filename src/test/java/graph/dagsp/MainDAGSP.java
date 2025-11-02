package graph.dagsp;

import java.util.*;

public class MainDAGSP {
    public static void main(String[] args) {
        WeightedGraph dag = new WeightedGraph(6);
        dag.addEdge(0, 1, 5);
        dag.addEdge(0, 2, 3);
        dag.addEdge(1, 3, 6);
        dag.addEdge(1, 2, 2);
        dag.addEdge(2, 4, 4);
        dag.addEdge(2, 5, 2);
        dag.addEdge(2, 3, 7);
        dag.addEdge(3, 5, 1);
        dag.addEdge(4, 5, -1);

        int source = 0;

        System.out.println("SHORTEST PATHS:");
        DagShortestPath sp = new DagShortestPath(dag, source);
        for (int i = 0; i < dag.size(); i++) {
            System.out.printf("Shortest from %d to %d: %.2f, Path: %s%n",
                    source, i, sp.distanceTo(i), sp.reconstructPath(i));
        }

        System.out.println("\nLONGEST PATH (Critical Path):");
        DagLongestPath lp = new DagLongestPath(dag, source);
        int end = lp.getCriticalEnd();
        System.out.printf("Critical path length: %.2f%n", lp.getCriticalLength());
        System.out.println("Critical path: " + lp.reconstructPath(end));
    }
}
