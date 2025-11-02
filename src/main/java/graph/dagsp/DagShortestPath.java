package graph.dagsp;

import graph.topo.TopologicalSorter;
import java.util.*;

public class DagShortestPath {
    private final WeightedGraph dag;
    private final int source;
    private final double[] dist;
    private final int[] prev;

    public DagShortestPath(WeightedGraph dag, int source) {
        this.dag = dag;
        this.source = source;
        int n = dag.size();
        dist = new double[n];
        prev = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);

        dist[source] = 0;

        List<Integer> order = topoOrder(dag);

        for (int u : order) {
            if (dist[u] != Double.POSITIVE_INFINITY) {
                for (WeightedGraph.Edge e : dag.neighbors(u)) {
                    if (dist[e.to] > dist[u] + e.weight) {
                        dist[e.to] = dist[u] + e.weight;
                        prev[e.to] = u;
                    }
                }
            }
        }
    }

    private List<Integer> topoOrder(WeightedGraph g) {
        int n = g.size();
        graph.scc.Graph tmp = new graph.scc.Graph(n);
        for (int u = 0; u < n; u++)
            for (var e : g.neighbors(u))
                tmp.addEdge(u, e.to);
        return TopologicalSorter.kahnSort(tmp);
    }

    public double distanceTo(int v) {
        return dist[v];
    }

    public List<Integer> reconstructPath(int target) {
        List<Integer> path = new ArrayList<>();
        if (dist[target] == Double.POSITIVE_INFINITY) return path;
        for (int at = target; at != -1; at = prev[at]) path.add(at);
        Collections.reverse(path);
        return path;
    }

    public double[] getAllDistances() {
        return dist;
    }
}
