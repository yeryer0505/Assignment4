package graph.dagsp;

import graph.utils.BasicMetrics;
import graph.utils.Metrics;

import java.util.*;

public class DAGShortestPath {
    private final int V;
    private final List<List<Edge>> adj;
    private final Metrics metrics = new BasicMetrics();

    public static class Edge {
        int to;
        int weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public DAGShortestPath(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int weight) {
        adj.get(u).add(new Edge(v, weight));
    }

    public List<Integer> topologicalOrder() {
        boolean[] visited = new boolean[V];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < V; i++) {
            if (!visited[i]) dfs(i, visited, stack);
        }
        List<Integer> order = new ArrayList<>(stack);
        Collections.reverse(order);
        return order;
    }

    private void dfs(int u, boolean[] visited, Deque<Integer> stack) {
        visited[u] = true;
        for (Edge e : adj.get(u)) {
            if (!visited[e.to]) dfs(e.to, visited, stack);
        }
        stack.push(u);
    }

    public int[] shortestPaths(int src) {
        metrics.start();

        List<Integer> order = topologicalOrder();
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for (int u : order) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Edge e : adj.get(u)) {
                    metrics.increment("relax");
                    if (dist[e.to] > dist[u] + e.weight) {
                        dist[e.to] = dist[u] + e.weight;
                    }
                }
            }
        }

        metrics.stop();
        System.out.println("DAG Shortest Paths time: " + metrics.getTime() + " ms");
        System.out.println("Counters: " + metrics.getCounters());

        return dist;
    }

    public int[] longestPaths(int src) {
        metrics.start();

        List<Integer> order = topologicalOrder();
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[src] = 0;

        for (int u : order) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (Edge e : adj.get(u)) {
                    metrics.increment("relax");
                    if (dist[e.to] < dist[u] + e.weight) {
                        dist[e.to] = dist[u] + e.weight;
                    }
                }
            }
        }

        metrics.stop();
        System.out.println("DAG Longest Path time: " + metrics.getTime() + " ms");
        System.out.println("Counters: " + metrics.getCounters());

        return dist;
    }
}
