package graph.dagsp;

import graph.topo.TopologicalSort;
import java.util.*;

public class DAGLongestPath {
    private final WeightedGraph dag;
    private final double[] dist;
    private final int[] prev;
    private final List<Integer> order;

    public DAGLongestPath(WeightedGraph dag, int source) {
        this.dag = dag;
        int n = dag.size();
        dist = new double[n];
        prev = new int[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        Arrays.fill(prev, -1);

        order = topoOrder(dag);
        dist[source] = 0;

        for (int u : order) {
            if (dist[u] != Double.NEGATIVE_INFINITY) {
                for (WeightedGraph.Edge e : dag.neighbors(u)) {
                    if (dist[e.to] < dist[u] + e.weight) {
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

    public double[] getDistances() {
        return dist;
    }

    public List<Integer> reconstructPath(int target) {
        List<Integer> path = new ArrayList<>();
        if (dist[target] == Double.NEGATIVE_INFINITY) return path;
        for (int at = target; at != -1; at = prev[at]) path.add(at);
        Collections.reverse(path);
        return path;
    }

    public double getCriticalLength() {
        return Arrays.stream(dist).max().orElse(Double.NEGATIVE_INFINITY);
    }

    public int getCriticalEnd() {
        double max = Double.NEGATIVE_INFINITY;
        int node = -1;
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] > max) {
                max = dist[i];
                node = i;
            }
        }
        return node;
    }
}
