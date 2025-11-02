package graph.topo;

import graph.scc.Graph;
import java.util.*;

public class TopologicalSorter {
    public static List<Integer> kahnSort(Graph dag) {
        int n = dag.size();
        int[] inDegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : dag.neighbors(u)) {
                inDegree[v]++;
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            topoOrder.add(u);
            for (int v : dag.neighbors(u)) {
                if (--inDegree[v] == 0)
                    queue.offer(v);
            }
        }

        if (topoOrder.size() != n)
            throw new IllegalStateException("Graph is not a DAG!");

        return topoOrder;
    }

    public static List<Integer> dfsSort(Graph dag) {
        int n = dag.size();
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i])
                dfs(i, dag, visited, stack);
        }

        List<Integer> order = new ArrayList<>(stack);
        Collections.reverse(order);
        return order;
    }

    private static void dfs(int v, Graph dag, boolean[] visited, Deque<Integer> stack) {
        visited[v] = true;
        for (int nei : dag.neighbors(v)) {
            if (!visited[nei])
                dfs(nei, dag, visited, stack);
        }
        stack.push(v);
    }
}
