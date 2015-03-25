package part1;
import java.util.PriorityQueue;
import java.lang.Comparable;
import java.util.List;
import java.util.ArrayList;
//import GraphInputGenerator.java;

public class Kruskals {

	static PriorityQueue<Edge> edges;
	static PriorityQueue<Edge> tree;
	private static GraphInputGenerator graphGen;
	
	public static class UnionFind
	{
		public Node[] nodes;
		public UnionFind(int numNodes)
		{
			nodes = new Node[numNodes];
			for (int i = 0; i<numNodes; i++)
			{
				nodes[i] = new Node();
			}
		}
		public void Make_Set(Node x)
		{
			x.parent = x;
			x.rank = 0;
		}
		public Node Find(Node x)
		{
			if (x.parent!=x)
			{
				x.parent = Find(x.parent);
			}
			return x.parent;
		}
		public void Union(Node x, Node y)
		{
			Node xRoot = Find(x);
			Node yRoot = Find(y);
			//System.out.println(xRoot.value+": "+xRoot.rank+", "+yRoot.value+": "+yRoot.rank);
			if (xRoot == yRoot)
			{
				return;
			}
			else if (xRoot.rank < yRoot.rank)
			{
				xRoot.parent = yRoot;
			}
			else if (xRoot.rank > yRoot.rank)
			{
				yRoot.parent = xRoot;
			}
			else
			{
				yRoot.parent = xRoot;
				xRoot.rank++;
			}
		}
	}
	
	public static class Node
	{
		public Node parent;
		public int rank;
		public int value;
		public Node()
		{
		}
		public Node FindParent(Node x)
		{
			if (this.parent != x)
			{
				this.parent = FindParent(this.parent);
			}
			return this.parent;
		}
	}
	
	public static class VertexSet
	{
		List<Integer> vertices;
		public VertexSet(int v)
		{
			vertices = new ArrayList<Integer>();
			vertices.add(v);
		}
		public boolean contains(int i)
		{
			if(this.vertices.contains(i))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		public void addAll(VertexSet other)
		{
			this.vertices.addAll(other.getVertices());
		}
		public List<Integer> getVertices()
		{
			return this.vertices;
		}
	}
	
	public static class Edge implements Comparable<Edge>
	{
		public int start;
		public int end;
		public int weight;
		
		public Edge(int x, int y, int w)
		{
			this.start = x;
			this.end = y;
			this.weight = w;
		}
		@Override
		public int compareTo(Edge other) {
			// TODO Auto-generated method stub
			if(this.weight > other.weight)
			{
				return 1;
			}
			else if (this.weight < other.weight)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
		@Override
		public String toString()
		{
			return start+"->"+end+": "+weight;
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		edges = new PriorityQueue<Edge>();
		tree = new PriorityQueue<Edge>();
		int v = 40;
		int e = 30;
		boolean isWeighted = true;
		boolean isDirected = false;
		graphGen = new GraphInputGenerator(v, e, isWeighted, isDirected);
		System.out.println("Kruskals with Union Find");
		//record cpu time
		long startTime = System.nanoTime();
		KruskalUnionFind(graphGen.adj);
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		duration = duration/1000000;
		//print cpu time
		System.out.println("Program runtime: "+ duration+" milliseconds");
		System.out.println("Kruskals without Union Find");
		startTime = System.nanoTime();
		KruskalNoUnionFind(graphGen.adj);
		endTime = System.nanoTime();
		duration = endTime - startTime;
		duration = duration/1000000;
		System.out.println("Program runtime: "+ duration+" milliseconds");
	}
	
	public static boolean CreateCycle(int start, int[][] spanningTree, int[] visited)
	{
		int numNodes = spanningTree[0].length;
		visited[start] = 1;
		for (int i =0; i<numNodes; i++)
		{
			if (spanningTree[start][i] >=1 &&(visited[i] > 0 || CreateCycle(i, spanningTree, visited)))
			{
				return true;
			}
		}
		return false;
	}
	
	
	//takes in adjacency matrix, which is a 2d array of ints, 
	//where the int value is the weight of the edge between the two vertices, 
	//1 if unweighted, and 0 if no edge. Also Takes in num edges and vertices and whether directed or weighted
	//if directed, only 1 edge added, if undirected, edges added in both directions
	public static void KruskalNoUnionFind(int adjacencyMatrix[][])
	{
		int numNodes = adjacencyMatrix[0].length;
		List<VertexSet> sets = new ArrayList<VertexSet>();
		for (int s = 0; s < numNodes; s++)
		{   //destination
			sets.add(new VertexSet(s));
			for(int d = 0; d < numNodes; d++)
			{
				if (adjacencyMatrix[s][d] > 0 && s != d)
				{
					Edge edge = new Edge(s, d, adjacencyMatrix[s][d]);
					//adjacencyMatrix[d][s] = 0;
					edges.add(edge);
				}
			}
		}
		//System.out.println(edges.toString());
		while(sets.size()>1 && edges.size() > 0)
		{
			Edge current = edges.poll();
			//System.out.println(edges.size());
			int startIndex=0;
			int endIndex=0;
			for(int i = 0; i< sets.size(); i++)
			{
				if (sets.get(i).contains(current.start))
				{
					startIndex = i;
				}
				if (sets.get(i).contains(current.end))
				{
					endIndex = i;
				}
			}
			if(startIndex != endIndex)
			{
				sets.get(startIndex).addAll(sets.get(endIndex));
				sets.remove(endIndex);
				tree.add(current);
			}
		}
		int num = 0;
		System.out.println("Edges:");
		while(!tree.isEmpty())
		{
			Edge output = tree.poll();
			System.out.println(output);
			num++;
		}
		System.out.println(num);
        /*System.out.println("The spanning tree is ");
        for (int i = 0; i < numNodes; i++)
            System.out.print("\t" + i);
        System.out.println();
        for (int source = 0; source < numNodes; source++)
        {
            System.out.print(source + "\t");
            for (int destination = 0; destination < numNodes; destination++)
            {
                System.out.print(spanningTree[source][destination] + "\t");
            }
            System.out.println();
        }*/
	}
	
	public static void KruskalUnionFind(int adjacencyMatrix[][])
	{
		int numNodes = adjacencyMatrix[0].length;
		UnionFind UF = new UnionFind(numNodes);
		for (int s = 0; s < numNodes; s++)
		{   //destination
			UF.Make_Set(UF.nodes[s]);
			for(int d = 0; d < numNodes; d++)
			{
				if (adjacencyMatrix[s][d] > 0 && s != d)
				{
					Edge edge = new Edge(s, d, adjacencyMatrix[s][d]);
					edges.add(edge);
				}
			}
		}
		while(!edges.isEmpty())
		{
			Edge current = edges.poll();
			if(UF.Find(UF.nodes[current.start])!=UF.Find(UF.nodes[current.end]))
			{
				UF.Union(UF.nodes[current.start], UF.nodes[current.end]);
				tree.add(current);
			}
		}
		int num = 0;
		System.out.println("Edges:");
		while(!tree.isEmpty())
		{
			Edge output = tree.poll();
			System.out.println(output);
			num++;
		}
		System.out.println(num);
	}
}