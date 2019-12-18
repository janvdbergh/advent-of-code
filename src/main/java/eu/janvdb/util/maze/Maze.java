package eu.janvdb.util.maze;

import eu.janvdb.util.Point2D;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;

public class Maze {

	private final char[][] mapChars;
	private final String wallChars;

	public Maze(char[][] mapChars, String wallChars) {
		this.mapChars = mapChars;
		this.wallChars = wallChars;
	}

	public void print() {
		for (char[] mapChar : mapChars) {
			for (char c : mapChar) {
				System.out.print(c);
			}
			System.out.println();
		}
	}

	public ShortestPathMap buildShortestPathMap(Point2D origin) {
		int[][] shortestPaths = new int[mapChars.length][];
		for (int y = 0; y < mapChars.length; y++) {
			shortestPaths[y] = new int[mapChars[y].length];
			for (int x = 0; x < mapChars[y].length; x++) {
				boolean wall = wallChars.indexOf(mapChars[y][x]) != -1;
				shortestPaths[y][x] = wall ? ShortestPathMap.WALL : ShortestPathMap.NO_PATH;
			}
		}
		shortestPaths[origin.getY()][origin.getX()] = 0;

		boolean canWeStop = false;
		while (!canWeStop) {
			canWeStop = true;
			for (int y = 0; y < mapChars.length; y++) {
				for (int x = 0; x < mapChars[y].length; x++) {
					if (shortestPaths[y][x] != ShortestPathMap.WALL) {
						canWeStop &= updateShortestValue(shortestPaths, y, x, -1, 0);
						canWeStop &= updateShortestValue(shortestPaths, y, x, 1, 0);
						canWeStop &= updateShortestValue(shortestPaths, y, x, 0, -1);
						canWeStop &= updateShortestValue(shortestPaths, y, x, 0, 1);
					}
				}
			}
		}

		return new ShortestPathMap(shortestPaths);
	}

	private boolean updateShortestValue(int[][] shortestPaths, int y, int x, int dy, int dx) {
		if (y + dy < 0) return true;
		if (y + dy >= shortestPaths.length) return true;
		if (x + dx < 0) return true;
		if (x + dx >= shortestPaths[y + dy].length) return true;

		if (shortestPaths[y][x] > shortestPaths[y + dy][x + dx] + 1) {
			shortestPaths[y][x] = shortestPaths[y + dy][x + dx] + 1;
			return false;
		}

		return true;
	}

	public Set<Point2D> find(char type) {
		return Stream.range(0, mapChars.length)
				.flatMap(y -> Stream.range(0, mapChars[y].length)
						.filter(x -> mapChars[y][x] == type)
						.map(x -> new Point2D(x, y))
				)
				.toSet();
	}
}
