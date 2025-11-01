package graph.scc;

public class MainSCC {
    public static void main(String[] args) {
        Graph g = new Graph(8);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 3);
        g.addEdge(6, 5);
        g.addEdge(6, 7);
        g.addEdge(7, 6);

        TarjanSCC tarjan = new TarjanSCC(g);
        System.out.println("SCC count: " + tarjan.getSccCount());
        int index = 1;
        for (var comp : tarjan.getSCCs()) {
            System.out.println("Component " + index++ + ": " + comp);
        }

        Graph dag = CondensationGraphBuilder.buildCondensation(g, tarjan.getSCCs());
        System.out.println("\nCondensation graph:");
        for (int i = 0; i < dag.size(); i++) {
            System.out.println(i + " -> " + dag.neighbors(i));
        }
    }
}
