package graph.scc;

import java.util.*;

public class CondensationGraphBuilder {

    public static Graph buildCondensation(Graph g, List<List<Integer>> sccs) {
        int n = sccs.size();
        Graph dag = new Graph(n);
        Map<Integer, Integer> nodeToScc = new HashMap<>();

        for (int i = 0; i < sccs.size(); i++) {
            for (int node : sccs.get(i)) {
                nodeToScc.put(node, i);
            }
        }

        for (int from = 0; from < g.size(); from++) {
            for (int to : g.neighbors(from)) {
                int sccFrom = nodeToScc.get(from);
                int sccTo = nodeToScc.get(to);
                if (sccFrom != sccTo) {
                    dag.addEdge(sccFrom, sccTo);
                }
            }
        }

        return dag;
    }
}
