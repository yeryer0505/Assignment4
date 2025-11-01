package graph.scc;

import java.util.*;

public class Graph {
    private final int n;
    private final List<List<Integer>> adj;

    public Graph(int n) {
        this.n = n;
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int from, int to) {
        adj.get(from).add(to);
    }

    public int size() {
        return n;
    }

    public List<Integer> neighbors(int v) {
        return adj.get(v);
    }

    public List<List<Integer>> getAdj() {
        return adj;
    }
}
