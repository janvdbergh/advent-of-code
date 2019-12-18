package eu.janvdb.util.maze;

import eu.janvdb.util.Point2D;

public class ShortestPathMap {
	public static final int NO_PATH = Integer.MAX_VALUE / 4;
	public static final int WALL = Integer.MAX_VALUE / 2;

	private int[][] shortestPaths;

	public ShortestPathMap(int[][] shortestPaths) {
		this.shortestPaths = shortestPaths;
	}

	public int distanceTo(Point2D to) {
		return shortestPaths[to.getY()][to.getX()];
	}

	public int max() {
		int max = -1;
		for (int[] shortestPath : shortestPaths) {
			for (int value : shortestPath) {
				if (value != WALL && max < value) max = value;
			}
		}

		return max;
	}
}
