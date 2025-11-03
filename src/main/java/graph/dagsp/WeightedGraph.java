package graph.dagsp;

import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WeightedGraph {
    private final int n;
    private final List<List<Edge>> adj;
    private int source = 0; // default if not provided
    private String weightModel = "edge"; // default mode

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
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public WeightedGraph(List<List<Edge>> adj) {
        this.n = adj.size();
        this.adj = adj;
    }

    public void addEdge(int from, int to, double weight) {
        adj.get(from).add(new Edge(to, weight));
    }

    public int size() {
        return n;
    }

    public List<List<Edge>> getAdj() {
        return adj;
    }

    public int getSource() {
        return source;
    }

    public String getWeightModel() {
        return weightModel;
    }

    public static WeightedGraph fromJson(String path) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(path)) {
            JsonObject obj = gson.fromJson(reader, JsonObject.class);
            int n = obj.get("n").getAsInt();
            WeightedGraph graph = new WeightedGraph(n);

            if (obj.has("source")) {
                graph.source = obj.get("source").getAsInt();
            }
            if (obj.has("weight_model")) {
                graph.weightModel = obj.get("weight_model").getAsString();
            }

            JsonArray edges = obj.getAsJsonArray("edges");
            for (JsonElement e : edges) {
                JsonObject edge = e.getAsJsonObject();
                int from = edge.get("u").getAsInt();
                int to = edge.get("v").getAsInt();
                double weight = edge.get("w").getAsDouble();
                graph.addEdge(from, to, weight);
            }

            return graph;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON graph from: " + path, e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WeightedGraph(n=" + n + ")\n");
        for (int i = 0; i < n; i++) {
            sb.append(i).append(": ").append(adj.get(i)).append("\n");
        }
        sb.append("Source: ").append(source).append("\n");
        sb.append("Weight model: ").append(weightModel).append("\n");
        return sb.toString();
    }
}
