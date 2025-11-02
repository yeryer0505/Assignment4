package graph.topo;

import graph.scc.*;

import java.util.*;

public class MainTopo {
    public static void main(String[] args) {
        Graph g = new Graph(8);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 3);
        g.addEdge(6, 5);
        g.addEdge(6, 7);
        g.addEdge(7, 6);

        TarjanSCC tarjan = new TarjanSCC(g);
        List<List<Integer>> sccs = tarjan.getSCCs();

        System.out.println("Found " + tarjan.getSccCount() + " strongly connected components:");
        int id = 1;
        for (List<Integer> comp : sccs)
            System.out.println("Component " + (id++) + ": " + comp);

        Graph dag = CondensationGraphBuilder.buildCondensation(g, sccs);
        System.out.println("\nCondensation DAG edges:");
        for (int i = 0; i < dag.size(); i++)
            System.out.println(i + " -> " + dag.neighbors(i));

        System.out.println("\nTopological order (Kahn): " + TopologicalSorter.kahnSort(dag));
        System.out.println("Topological order (DFS): " + TopologicalSorter.dfsSort(dag));
    }
}
