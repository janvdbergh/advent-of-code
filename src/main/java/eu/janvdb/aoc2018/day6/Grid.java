package eu.janvdb.aoc2018.day6;

import java.util.Arrays;

import eu.janvdb.aoc2018.util.Coordinate;

class Grid {
	private final int minx;
	private final int maxx;
	private final int miny;
	private final int maxy;
	private final int[] values;

	Grid(int minx, int maxx, int miny, int maxy) {
		this.minx = minx;
		this.miny = miny;
		this.maxx = maxx;
		this.maxy = maxy;
		this.values = new int[(maxx - minx + 1) * (maxy - miny + 1)];
	}

	void set(int x, int y, int value) {
		values[arrayIndex(x, y)] = value;
	}

	private int get(Coordinate coordinate) {
		return get(coordinate.getX(), coordinate.getY());
	}

	private int get(int x, int y) {
		return values[arrayIndex(x, y)];
	}

	private int arrayIndex(int x, int y) {
		if (x < minx || x > maxx || y < miny || y > maxy) throw new IllegalArgumentException();
		return (x - minx) + (y - miny) * (maxx - minx + 1);
	}

	boolean isOnEdge(Coordinate coordinate) {
		int value = get(coordinate);
		for (int x = minx; x <= maxx; x++) {
			if (get(new Coordinate(x, miny)) == value || get(new Coordinate(x, maxy)) == value) {
				return true;
			}
		}
		for (int y = miny; y <= maxy; y++) {
			if (get(new Coordinate(minx, y)) == value || get(new Coordinate(maxx, y)) == value) {
				return true;
			}
		}
		return false;
	}

	int getCountForThis(Coordinate coordinate) {
		int valueAtCoordinate = get(coordinate);
		return (int) Arrays.stream(values)
				.filter(value -> value == valueAtCoordinate)
				.count();
	}
}
