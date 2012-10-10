/**
 * Solution to 2010H - Underground Cables
 * 
 * Successfully ran the ICPC inputs with 
 * two different implementations:
 * 
 * 		- Naive disjoint set: 					2.200 sec
 * 		- Disjoint set with path compression: 	2.092 sec
 * 
 * Was surprised at the lack of performance increase!
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Cables {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		int N = in.nextInt();

		while (N != 0) {

			ArrayList<Point> points = new ArrayList<Point>();
			ArrayList<Edge> edges = new ArrayList<Edge>();
			DisjointSet set = new DisjointSet(N);

			// Input points
			for (int i = 0; i < N; i++) {
				points.add(new Point(in.nextInt(), in.nextInt()));
			}

			// Populate edges
			for (int i = 0; i < N; i++) {
				Point p1 = points.get(i);

				for (int j = i + 1; j < N; j++) {
					Point p2 = points.get(j);
					double distance = p1.distance(p2.x, p2.y);

					edges.add(new Edge(i, j, distance));
				}
			}

			// Sort based on edge length
			Collections.sort(edges, new EdgeComparator());

			int connected = 0;
			double distance = 0;

			// Walk the sorted list of edges
			// and add disjoint set connectors
			for (int i = 0; i < edges.size(); i++) {

				if (connected == N - 1) {
					break;
				}

				Edge e = edges.get(i);

				if (set.root(e.in) != set.root(e.out)) {
					connected++;
					distance += e.length;
					set.union(e.in, e.out);
				}
			}

			System.out.format("%.2f\n", distance);
			N = in.nextInt();
		}
	}

	/**
	 * Disjoint set implementation that uses path compression.
	 * 
	 */
	static class DisjointSet {

		int[] sets;

		public DisjointSet(int N) {
			sets = new int[N];

			for (int i = 0; i < N; i++) {
				sets[i] = i;
			}
		}

		// Returns the root of the set i
		public int root(int i) {

			while (i != sets[i]) {
				sets[i] = sets[sets[i]];
				i = sets[i];
			}

			return i;
		}

		// Unions two sets
		public void union(int s1, int s2) {

			int newRoot = root(s1);
			int toChange = root(s2);

			sets[toChange] = newRoot;

		}

	}

	static class EdgeComparator implements Comparator<Edge> {

		@Override
		public int compare(Edge e1, Edge e2) {
			return Double.compare(e1.length, e2.length);
		}

	}

	static class Edge {
		int in, out;
		double length;

		public Edge(int in, int out, double distance) {
			this.in = in;
			this.out = out;
			this.length = distance;
		}

	}

	static class Point {
		int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public double distance(int x0, int y0) {

			return Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0));
		}

	}

}
