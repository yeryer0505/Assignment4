package graph.scc;

import java.util.*;

public class TarjanSCC {
    private final Graph graph;
    private final int[] ids, low;
    private final boolean[] onStack;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final List<List<Integer>> sccs = new ArrayList<>();

    private int id = 0;
    private int sccCount = 0;

    public TarjanSCC(Graph g) {
        this.graph = g;
        int n = g.size();
        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        Arrays.fill(ids, -1);

        for (int i = 0; i < n; i++) {
            if (ids[i] == -1)
                dfs(i);
        }
    }

    private void dfs(int at) {
        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = id++;

        for (int to : graph.neighbors(at)) {
            if (ids[to] == -1) {
                dfs(to);
                low[at] = Math.min(low[at], low[to]);
            } else if (onStack[to]) {
                low[at] = Math.min(low[at], ids[to]);
            }
        }

        if (ids[at] == low[at]) {
            List<Integer> component = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                component.add(node);
                if (node == at) break;
            }
            sccs.add(component);
            sccCount++;
        }
    }

    public List<List<Integer>> getSCCs() {
        return sccs;
    }

    public int getSccCount() {
        return sccCount;
    }
}
