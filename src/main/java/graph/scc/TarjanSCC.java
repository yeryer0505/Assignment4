package graph.scc;

import graph.utils.BasicMetrics;
import graph.utils.Metrics;

import java.util.*;

public class TarjanSCC {
    private final int V;
    private final List<List<Integer>> adj;
    private int time = 0;
    private final int[] disc;
    private final int[] low;
    private final boolean[] stackMember;
    private final Deque<Integer> stack;
    private final List<List<Integer>> components = new ArrayList<>();

    private final Metrics metrics = new BasicMetrics();

    public TarjanSCC(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        disc = new int[V];
        low = new int[V];
        stackMember = new boolean[V];
        stack = new ArrayDeque<>();
    }

    public void addEdge(int u, int v) {
        if (u < 0 || u >= V || v < 0 || v >= V) {
            throw new IllegalArgumentException("Vertex out of bounds");
        }
        adj.get(u).add(v);
    }

    public List<List<Integer>> run() {
        metrics.start();
        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
        for (int i = 0; i < V; i++) {
            if (disc[i] == -1) dfs(i);
        }
        metrics.stop();

        System.out.println("TarjanSCC time: " + metrics.getTime() + " ms");
        System.out.println("Counters: " + metrics.getCounters());
        System.out.println("Components: " + components);

        return components;
    }

    private void dfs(int u) {
        metrics.increment("dfs_visit");

        disc[u] = low[u] = ++time;
        stack.push(u);
        stackMember[u] = true;

        for (int v : adj.get(u)) {
            metrics.increment("edge");
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (stackMember[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> component = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                stackMember[w] = false;
                component.add(w);
            } while (w != u);
            components.add(component);
        }
    }
}
