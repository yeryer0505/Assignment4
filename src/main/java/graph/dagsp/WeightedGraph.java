package graph.dagsp;

import java.util.*;

public class WeightedGraph {
    private final int n;
    private final List<List<Edge>> adj;

    public static class Edge {
        public final int to;
        public final double weight;

        public Edge(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + to + ", w=" + weight + ")";
        }
    }

    public WeightedGraph(int n) {
        this.n = n;
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int from, int to, double weight) {
        adj.get(from).add(new Edge(to, weight));
    }

    public int size() {
        return n;
    }

    public List<Edge> neighbors(int v) {
        return adj.get(v);
    }
}
