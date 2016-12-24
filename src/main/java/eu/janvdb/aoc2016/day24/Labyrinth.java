package eu.janvdb.aoc2016.day24;

import java.net.URL;

import eu.janvdb.util.InputReader;
import javaslang.collection.Array;
import javaslang.collection.HashMap;
import javaslang.collection.Map;

public class Labyrinth {

	private final int width, height;
	private final boolean[] walls;
	private final Map<Integer, Location> specialLocations;

	private Labyrinth(int width, int height, boolean[] walls, Map<Integer, Location> specialLocations) {
		this.width = width;
		this.height = height;
		this.walls = walls;
		this.specialLocations = specialLocations;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isWall(int row, int column) {
		return walls[getIndex(row, column)];
	}

	public int getIndex(Location location) {
		return getIndex(location.getRow(), location.getColumn());
	}

	public int getIndex(int row, int column) {
		return row * width + column;
	}

	public Map<Integer, Location> getSpecialLocations() {
		return specialLocations;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				if (isWall(row, column)) {
					builder.append('#');
				} else {
					builder.append('.');
				}
			}
			builder.append('\n');
		}
		return builder.toString();
	}

	public static class Reader {

		public static Labyrinth readFromFile(URL url) {
			Array<String> lines = InputReader.readInput(url).toArray();

			int width = lines.get(0).length();
			int height = lines.length();
			boolean[] walls = new boolean[width * height];
			Map<Integer, Location> specialLocations = HashMap.empty();

			for (int row = 0; row < height; row++) {
				String line = lines.get(row);
				for (int column = 0; column < width; column++) {
					char ch = line.charAt(column);
					walls[row * width + column] = ch == '#';

					if (ch >= '0' && ch <= '9') {
						specialLocations = specialLocations.put(ch - '0', new Location(row, column));
					}
				}
			}

			return new Labyrinth(width, height, walls, specialLocations);
		}
	}

}
