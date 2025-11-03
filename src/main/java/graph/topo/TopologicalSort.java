package graph.topo;

import graph.utils.BasicMetrics;
import graph.utils.Metrics;

import java.util.*;

public class TopologicalSort {
    private final int V;
    private final List<List<Integer>> adj;
    private final Metrics metrics = new BasicMetrics();

    public TopologicalSort(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v) {
        if (u < 0 || u >= V || v < 0 || v >= V) {
            throw new IllegalArgumentException("Vertex out of range");
        }
        adj.get(u).add(v);
    }

    public List<Integer> kahnSort() {
        metrics.start();

        int[] indegree = new int[V];
        for (int u = 0; u < V; u++) {
            for (int v : adj.get(u)) {
                indegree[v]++;
                metrics.increment("edge_count");
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < V; i++) {
            if (indegree[i] == 0) {
                q.add(i);
                metrics.increment("push");
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            metrics.increment("pop");
            topoOrder.add(u);

            for (int v : adj.get(u)) {
                indegree[v]--;
                if (indegree[v] == 0) {
                    q.add(v);
                    metrics.increment("push");
                }
            }
        }

        metrics.stop();

        if (topoOrder.size() != V) {
            System.out.println("TopologicalSort: graph has at least one cycle — returned partial order of size "
                    + topoOrder.size() + " / " + V);
        }

        System.out.println("TopologicalSort time: " + metrics.getTime() + " ms");
        System.out.println("Counters: " + metrics.getCounters());
        System.out.println("Topological order: " + topoOrder);

        return topoOrder;
    }
}
