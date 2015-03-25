package part1;

import java.util.Random;
/**
* Class to generate graph input data.
*
* @author Chinmay
*
*/
public class GraphInputGenerator {
	private static Random rand = new Random();
	// Adjacency matrix
	public int[][] adj;
	// Number of vertices & edges
	public int vertices, edges;
	private boolean isWeighted, isDirected;
	public GraphInputGenerator(int v, int e, boolean isWeighted,
			boolean isDirected) {
		adj = new int[v][v];
		this.vertices = v;
		this.isWeighted = isWeighted;
		this.isDirected = isDirected;
		// Maximum possible edges check
		this.edges = isDirected ? Math.min(e, v * (v - 1)) :
			Math.min(e, v
					* (v - 1) / 2);
		fillAdj();
	}
	/**
	 * Fills adjacency matrix randomly.
	 */
	private void fillAdj() {
		int count = 0;
		while (count < edges) {
			int v1 = rand.nextInt(vertices);
			int v2 = v1;
			while (v2 == v1) {
				v2 = rand.nextInt(vertices);
			}
			if (addEdge(v1, v2))
				count += 1;
		}
	}
	/**
	 * Adds a edge between vertices v1 and v2 and makes entry into
	the adjacency
	 * matrix accordingly. For non weighted graphs, the edge cost 
	is 1. For non
	 * directed graphs, the adjacency matrix is updated for both
	the vertices.
	 *
	 */
	private boolean addEdge(int v1, int v2) {
		if (adj[v1][v2] == 0) {
			int e = 1;
			if (isWeighted) {
				e += rand.nextInt(edges * edges / 2);
			}
			adj[v1][v2] = e;
			if (!isDirected)
				adj[v2][v1] = e;
			return true;
		}
		return false;
	}
	/**
	 * Displays adjacency matrix.
	 */
	public void showAdj() {
		for (int[] row : adj) {
			for (int col : row) {
				System.out.print(col + " ");
			}
			System.out.println();
		}
	}
	/**
	 * Prints the graph data. First line contains number of
	vertices and number
	 * of edges. Following lines show edges in the graph. If the
	graph is
	 * weighted, the cost is also shown.
	 */
	public void genGraphInput() {
		System.out.println(vertices + " " + edges);
		for (int i = 0; i < adj.length; i++) {
			for (int j = isDirected ? 0 : i; j <
			adj.length; j++) {
				if (adj[i][j] != 0) {
					System.out.println(i + " " + j
							+
							((isWeighted) ? " " + adj[i][j] : ""));
				}
			}
		}
	}
	public static void main(String[] args) {
		int v = Integer.parseInt(args[0]);
		int e = Integer.parseInt(args[1]);
		System.out.println("Non-weighted undirected graph:");
		GraphInputGenerator g = new GraphInputGenerator(v, e,
				false, false);
		g.genGraphInput();
		System.out.println("Weighted undirected graph:");
		g = new GraphInputGenerator(v, e, true, false);
		g.genGraphInput();
		System.out.println("Non-weighted directed graph:");
		g = new GraphInputGenerator(v, e, false, true);
		g.genGraphInput();
		System.out.println("Weighted directed graph:");
		g = new GraphInputGenerator(v, e, true, true);
		g.genGraphInput();
	}
}