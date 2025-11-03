package graph.dagsp;

import graph.utils.BasicMetrics;
import graph.utils.Metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DAGShortestPath {
    private final int V;
    private final List<List<Edge>> adj;
    private final Metrics metrics = new BasicMetrics();
    private double[] lastDistances;

    public static class Edge {
        int to;
        int weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public DAGShortestPath(int V) {
        if (V < 0) throw new IllegalArgumentException("V must be >= 0");
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int weight) {
        if (u < 0 || u >= V || v < 0 || v >= V) {
            throw new IllegalArgumentException("Vertex out of bounds");
        }
        adj.get(u).add(new Edge(v, weight));
    }

    private void dfs(int u, boolean[] visited, Deque<Integer> stack) {
        visited[u] = true;
        metrics.increment("dfs_visit");
        for (Edge e : adj.get(u)) {
            metrics.increment("edge");
            if (!visited[e.to]) dfs(e.to, visited, stack);
        }
        stack.push(u);
    }

    public List<Integer> topologicalOrder() {
        boolean[] visited = new boolean[V];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < V; i++) {
            if (!visited[i]) dfs(i, visited, stack);
        }
        List<Integer> order = new ArrayList<>();
        while (!stack.isEmpty()) order.add(stack.pop());
        return order;
    }

    public double[] shortestPaths(int src) {
        if (src < 0 || src >= V) throw new IllegalArgumentException("Invalid source");
        metrics.start();

        List<Integer> order = topologicalOrder();
        double[] dist = new double[V];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[src] = 0;

        for (int u : order) {
            if (dist[u] != Double.POSITIVE_INFINITY) {
                for (Edge e : adj.get(u)) {
                    metrics.increment("relax");
                    double nd = dist[u] + e.weight;
                    if (dist[e.to] > nd) {
                        dist[e.to] = nd;
                    }
                }
            }
        }

        metrics.stop();
        lastDistances = dist;

        System.out.println("DAG Shortest Paths time: " + metrics.getTime() + " ms");
        System.out.println("Counters: " + metrics.getCounters());
        System.out.println("Distances: " + Arrays.toString(dist));

        return dist;
    }

    public double[] longestPaths(int src) {
        if (src < 0 || src >= V) throw new IllegalArgumentException("Invalid source");
        metrics.start();

        List<Integer> order = topologicalOrder();
        double[] dist = new double[V];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        dist[src] = 0;

        for (int u : order) {
            if (dist[u] != Double.NEGATIVE_INFINITY) {
                for (Edge e : adj.get(u)) {
                    metrics.increment("relax");
                    double nd = dist[u] + e.weight;
                    if (dist[e.to] < nd) {
                        dist[e.to] = nd;
                    }
                }
            }
        }

        metrics.stop();
        lastDistances = dist;

        System.out.println("DAG Longest Path time: " + metrics.getTime() + " ms");
        System.out.println("Counters: " + metrics.getCounters());
        System.out.println("Distances: " + Arrays.toString(dist));

        return dist;
    }

    public double getDistance(int v) {
        if (lastDistances == null) {
            throw new IllegalStateException("No path computation has been run yet.");
        }
        if (v < 0 || v >= V) throw new IllegalArgumentException("Vertex out of bounds");
        return lastDistances[v];
    }

    public void exportResultsToCSV(String filename, String algorithmType) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            if (new java.io.File(filename).length() == 0) {
                writer.write("algorithm_type,vertices,time_ms,relax_ops,distances\n");
            }

            int relaxOps = metrics.getCounters().getOrDefault("relax", 0);

            writer.write(String.format(
                    "%s,%d,%d,%d,\"%s\"\n",
                    algorithmType,
                    V,
                    metrics.getTime(),
                    relaxOps,
                    Arrays.toString(lastDistances)
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
