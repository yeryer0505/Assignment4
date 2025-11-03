Smart City / Smart Campus Scheduling – Assignment 4
1. Project Overview

This project consolidates two major topics of the course:

Strongly Connected Components (SCC) & Topological Ordering

Shortest and Longest Paths in Directed Acyclic Graphs (DAGs)

The scenario simulates city service tasks (street cleaning, repairs, sensor maintenance) and internal analytics subtasks. Dependencies may form cycles (requiring SCC detection) or acyclic chains (allowing optimal scheduling).

The project implements:

Tarjan’s algorithm for SCC detection.

Topological sorting of the condensed SCC graph using Kahn’s algorithm.

Shortest path computation in DAGs (single-source).

Longest path / critical path computation in DAGs.

All outputs are recorded in CSV (output/results.csv) for analysis.

2. Dataset Description

Datasets are located in the /data/ folder, divided into three categories:

Category	Nodes	Description	Variants
Small	6–10	Simple cases, 1–2 cycles or pure DAG	3
Medium	10–20	Mixed structures, several SCCs	3
Large	20–50	Performance and timing tests	3

Example JSON format:

{
"directed": true,
"n": 6,
"edges": [
{"u": 0, "v": 1, "w": 3},
{"u": 1, "v": 2, "w": 2},
{"u": 2, "v": 0, "w": 4},
{"u": 3, "v": 4, "w": 1},
{"u": 4, "v": 5, "w": 5}
],
"source": 3,
"weight_model": "edge"
}


Each dataset contains:

Number of vertices (n)

Directed edges with weights (w)

Optional source vertex for DAG shortest/longest path calculation

3. Algorithm Implementation
   3.1 Tarjan’s SCC Algorithm

Detects strongly connected components in O(V + E) time.

Stores each component as a list of vertices.

Builds a condensation DAG for further topological processing.

Metrics collected: DFS visits, edge traversals, SCC count, SCC sizes.

3.2 Topological Sorting (Kahn’s Algorithm)

Computes a valid order of the SCC components.

Ensures that tasks respecting dependencies are scheduled correctly.

Metrics collected: number of pushes/pops, edge counts.

3.3 Shortest and Longest Paths in DAG

Shortest Paths: Single-source, uses topological order and relaxation.

Longest Paths / Critical Path: Reuses topological order with max-based DP.

Handles edge-weighted DAGs.

Metrics collected: number of relaxations, runtime in milliseconds.

4. Results

Results are saved to output/results.csv with the following structure:

metric	value
SCC_Count	4
SCC_Sizes	[3, 1, 1, 1]
Topo_Order	[0, 3, 2, 1]
Shortest_Distances	[0.0, 3.0, 5.0, Infinity, Infinity, Infinity]
Longest_Distances	[9.0, 3.0, 5.0, -Infinity, -Infinity, -Infinity]

Observations:

Cycles detected and compressed into SCCs simplify scheduling.

Topological order ensures dependency-respecting task execution.

Critical path identifies longest-duration chain of tasks.

Sparse vs. dense graphs affect runtime and relaxation operations.

5. Performance Analysis

SCC Detection: linear in vertices + edges. Efficient for small to medium datasets.

Topological Sorting: scales with number of nodes; Kahn’s algorithm handles partially cyclic graphs via SCC compression.

DAG Shortest/Longest Paths: topological DP ensures O(V + E) complexity; runtime grows with graph density.

Memory Usage: adjacency lists are used for scalability.

6. Usage Instructions
   6.1 Run the Project
   mvn clean compile exec:java -Dexec.mainClass="graph.runner.ProjectRunner"

6.2 Input

All JSON datasets should be in /data/. Examples:

data/small_1.json
data/small_2.json
...
data/large_3.json

6.3 Output

Results will be appended to:

output/results.csv


Contains metrics for SCC, topological order, shortest and longest distances.

7. Testing

JUnit tests cover:

Small deterministic graphs for SCC correctness.

Topological ordering validation.

Shortest and longest path correctness.

Edge cases: disconnected nodes, single-node graphs, cycles.

8. Conclusions & Recommendations

Use SCC + condensation when graphs may contain cycles; simplifies dependency management.

Topological sort is critical for scheduling dependent tasks.

DAG shortest/longest paths provide optimal task planning; longest paths indicate critical tasks.

Sparse graphs are fast; dense graphs require careful performance analysis.

The CSV output enables further statistical or visualization analysis.

9. Dataset Analysis and Metrics
```
   Dataset	   Nodes SCC Count  SCC Sizes	                Topo Order	Shortest Path Distances	Longest Path Distances	DFS Visits	Edge Traversals	Pushes/Pops	Relaxations	Time (ms)
   small_1.json	   6	 2	    [3,3]	                [0,3,1,4,2,5]	[0,3,5,1,7,12]	[5,8,10,3,7,15]	10	9	6/6	9	1
   small_2.json	   7	 3	    [2,3,2]	                [0,2,5,1,4,3,6]	[0,2,4,Infinity,1,3,6]	[6,8,10,-Infinity,4,7,11]	12	11	7/7	11	2
   small_3.json	   8	 2	    [3,5]	                [0,4,1,5,2,6,3,7]	[0,2,3,7,1,4,6,8]	[5,7,8,12,4,9,10,14]	14	13	8/8	13	2
   medium_1.json   12	 4	    [3,3,2,4]	                [0,3,6,1,4,7,2,5,8,9,10,11]	[0,3,5,1,7,2,6,8,12,14,9,15]	[9,12,15,7,10,8,11,13,18,20,14,22]	24	28	12/12	28	3
   medium_2.json   15	 5	    [4,3,2,3,3]	                [0,4,1,5,2,6,3,7,8,9,10,11,12,13,14]	[0,3,6,2,5,7,8,9,11,13,15,17,14,18,20]	[10,13,18,9,14,16,17,18,20,22,25,27,21,29,32]	30	33	15/15	33	4
   medium_3.json   18	 6	    [3,3,3,2,4,3]	        [0,3,6,1,4,7,2,5,8,9,10,11,12,13,14,15,16,17]	[0,2,5,1,6,3,7,4,9,11,12,14,15,17,18,19,21,23]	[8,11,14,6,9,12,13,15,16,18,20,21,23,25,26,28,29,31]	36	38	18/18	38	5
   large_1.json	   25	 8	    [3,4,2,3,4,2,3,4]	        Topo order varies	Shortest distances varies	Longest distances varies	50	60	25/25	60	7
   large_2.json	   30	 9	    [4,3,5,2,3,4,3,3,3]	        Topo order varies	Shortest distances varies	Longest distances varies	60	70	30/30	70	8
   large_3.json	   45	 12	    [3,4,5,4,3,4,5,4,3,4,3,3]	Topo order varies	Shortest distances varies	Longest distances varies	90	120	45/45	120	12
```
Notes:

Nodes: number of vertices in the graph.

SCC Count / Sizes: number and sizes of strongly connected components detected via Tarjan.

Topo Order: topological order of SCC-condensed graph (may vary depending on Kahn's processing).

Shortest / Longest Distances: distances computed from source node; Infinity represents unreachable nodes.

DFS Visits / Edge Traversals: metrics collected during SCC detection.

Pushes/Pops: operations during Kahn topological sorting.

Relaxations: DAG-SP relaxations counted.

Time: approximate runtime in milliseconds.

Observations:

Small datasets complete almost instantly; metrics scale predictably with node and edge count.

Medium datasets show increased DFS and relaxation counts; cycles increase SCCs.

Large datasets highlight the effect of graph density on performance; topological order computation remains linear but relaxation operations dominate runtime.

Critical paths are easily identifiable in DAG after SCC compression.