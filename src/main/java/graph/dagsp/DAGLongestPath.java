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
        if (source < 0 || source >= n) {
            throw new IllegalArgumentException("Invalid source vertex: " + source);
        }

        dist = new double[n];
        prev = new int[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        Arrays.fill(prev, -1);

        order = topoOrder(dag);

        dist[source] = 0;

        for (int u : order) {
            if (dist[u] != Double.NEGATIVE_INFINITY) {
                for (WeightedGraph.Edge e : dag.getAdj().get(u)) {
                    double newDist = dist[u] + e.weight;
                    if (newDist > dist[e.to]) {
                        dist[e.to] = newDist;
                        prev[e.to] = u;
                    }
                }
            }
        }

        System.out.println("Topological order: " + order);
        System.out.println("Final distances: " + formatDistances());
    }

    private List<Integer> topoOrder(WeightedGraph g) {
        TopologicalSort topo = new TopologicalSort(g.size());
        for (int u = 0; u < g.size(); u++) {
            for (WeightedGraph.Edge e : g.getAdj().get(u)) {
                topo.addEdge(u, e.to);
            }
        }
        return topo.kahnSort();
    }

    public double[] getDistances() {
        return dist;
    }

    public List<Integer> reconstructPath(int target) {
        List<Integer> path = new ArrayList<>();
        if (target < 0 || target >= dist.length) return path;
        if (dist[target] == Double.NEGATIVE_INFINITY) return path;

        for (int at = target; at != -1; at = prev[at]) {
            path.add(at);
        }
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

    private String formatDistances() {
        List<String> list = new ArrayList<>();
        for (double d : dist) {
            if (d == Double.NEGATIVE_INFINITY) list.add("unreachable");
            else list.add(String.format("%.1f", d));
        }
        return list.toString();
    }
}
