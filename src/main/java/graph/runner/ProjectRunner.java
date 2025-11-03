package graph.runner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import graph.dagsp.DAGShortestPath;
import graph.dagsp.WeightedGraph;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class ProjectRunner {

    private static final String INPUT_FILE = "data/small_1.json";
    private static final String OUTPUT_FILE = "output/results.csv";

    public static void main(String[] args) throws IOException {
        System.out.println("=== Project Runner Start ===");

        WeightedGraph graph = WeightedGraph.fromJson(INPUT_FILE);
        int V = graph.size();
        List<List<WeightedGraph.Edge>> adj = graph.getAdj();

        // === 1. Tarjan SCC ===
        TarjanSCC scc = new TarjanSCC(V);
        for (int u = 0; u < V; u++) {
            for (WeightedGraph.Edge e : adj.get(u)) {
                scc.addEdge(u, e.to);
            }
        }

        List<List<Integer>> components = scc.run();

        // === 2. Build condensation graph ===
        Map<Integer, Set<Integer>> condensation = buildCondensation(components, adj);
        System.out.println("Condensation graph: " + condensation);

        // === 3. Topological Sort (Kahn’s Algorithm) ===
        TopologicalSort topo = new TopologicalSort(condensation.size());
        for (var entry : condensation.entrySet()) {
            int u = entry.getKey();
            for (int v : entry.getValue()) {
                topo.addEdge(u, v);
            }
        }
        List<Integer> topoOrder = topo.kahnSort();

        // === 4. DAG Shortest/Longest Paths ===
        DAGShortestPath dagSP = new DAGShortestPath(V);
        for (int u = 0; u < V; u++) {
            for (WeightedGraph.Edge e : adj.get(u)) {
                dagSP.addEdge(u, e.to, (int) e.weight);
            }
        }

        double[] shortest = dagSP.shortestPaths(0);
        double[] longest = dagSP.longestPaths(0);

        // === 5. Export results to CSV ===
        exportResultsToCSV(OUTPUT_FILE, components, topoOrder, shortest, longest);

        System.out.println("Results saved to: " + OUTPUT_FILE);
        System.out.println("=== Project Runner End ===");
    }

    // ---------------- Utility methods ----------------

    private static WeightedGraph loadGraphFromJson(String filename) throws IOException {
        String json = Files.readString(Path.of(filename));
        Gson gson = new Gson();
        Type edgeListType = new TypeToken<List<List<WeightedGraph.Edge>>>() {}.getType();
        List<List<WeightedGraph.Edge>> adj = gson.fromJson(json, edgeListType);
        return new WeightedGraph(adj);
    }

    private static Map<Integer, Set<Integer>> buildCondensation(
            List<List<Integer>> components,
            List<List<WeightedGraph.Edge>> adj
    ) {
        Map<Integer, Integer> nodeToComp = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (int node : components.get(i)) {
                nodeToComp.put(node, i);
            }
        }

        Map<Integer, Set<Integer>> cond = new HashMap<>();
        for (int i = 0; i < components.size(); i++) cond.put(i, new HashSet<>());

        for (int u = 0; u < adj.size(); u++) {
            int cu = nodeToComp.get(u);
            for (WeightedGraph.Edge e : adj.get(u)) {
                int cv = nodeToComp.get(e.to);
                if (cu != cv) cond.get(cu).add(cv);
            }
        }

        return cond;
    }

    private static void exportResultsToCSV(
            String filename,
            List<List<Integer>> scc,
            List<Integer> topoOrder,
            double[] shortest,
            double[] longest
    ) {
        try {
            Files.createDirectories(Path.of("output"));
            try (FileWriter writer = new FileWriter(filename, false)) {
                writer.write("metric,value\n");
                writer.write("SCC_Count," + scc.size() + "\n");
                writer.write("SCC_Sizes," + scc.stream().map(List::size).toList() + "\n");
                writer.write("Topo_Order," + topoOrder + "\n");
                writer.write("Shortest_Distances," + Arrays.toString(shortest) + "\n");
                writer.write("Longest_Distances," + Arrays.toString(longest) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
