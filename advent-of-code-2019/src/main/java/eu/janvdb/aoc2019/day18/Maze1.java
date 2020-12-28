package eu.janvdb.aoc2019.day18;

import eu.janvdb.aocutil.java.Point2D;
import eu.janvdb.aocutil.java.shortestpath.MapDescription;
import eu.janvdb.aocutil.java.shortestpath.ShortestPath;
import eu.janvdb.aocutil.java.shortestpath.ShortestPathBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Maze1 {

	public static Maze1 parse(List<String> lines) {
		int height = lines.size();
		int width = lines.get(0).length();

		boolean[][] walls = new boolean[height][];
		Map<Character, Point2D> symbols = new HashMap<>();

		for (int y = 0; y < height; y++) {
			walls[y] = new boolean[width];
			for (int x = 0; x < width; x++) {
				char ch = lines.get(y).charAt(x);
				walls[y][x] = ch == '#';
				if (ch != '#' && ch != '.') symbols.put(ch, new Point2D(x, y));
			}
		}

		return new Maze1(walls, symbols);
	}

	private final boolean[][] walls;
	private final Map<Character, Point2D> symbols;

	private Maze1(boolean[][] walls, Map<Character, Point2D> symbols) {
		this.walls = walls;
		this.symbols = symbols;
	}

	public Map<Character, Map<Character, Integer>> getDistances() {
		return symbols.keySet().stream().collect(Collectors.toMap(key -> key, this::getDistances));
	}

	private Map<Character, Integer> getDistances(char from) {
		ShortestPath<Maze1MapState> shortestPath = ShortestPathBuilder.build(new Maze1MapDescription(from));

		return shortestPath.getReachablePoints().stream()
				.filter(state -> state.symbol != null)
				.collect(Collectors.toMap(state -> state.symbol, shortestPath::distanceTo));
	}

	private class Maze1MapDescription implements MapDescription<Maze1MapState> {
		private final char symbol;

		public Maze1MapDescription(char symbol) {
			this.symbol = symbol;
		}

		@Override
		public Maze1MapState getOrigin() {
			return new Maze1MapState(symbols.get(symbol), symbol);
		}

		@Override
		public List<Maze1MapState> getNeighbours(Maze1MapState state) {
			if (state.symbol != null && state.symbol != symbol) return Collections.emptyList();
			return Stream.of(
					getState(state, 0, -1),
					getState(state, -1, 0),
					getState(state, 1, 0),
					getState(state, 0, 1)
			)
					.filter(newState -> !walls[newState.location.getY()][newState.location.getX()])
					.collect(Collectors.toList());
		}

		private Maze1MapState getState(Maze1MapState currentState, int dx, int dy) {
			Point2D location = currentState.location.move(dx, dy);
			Character symbol = symbols.entrySet().stream()
					.filter(entry -> entry.getValue().equals(location))
					.map(Map.Entry::getKey).findAny()
					.orElse(null);
			return new Maze1MapState(location, symbol);
		}

		@Override
		public int getDistance(Maze1MapState from, Maze1MapState to) {
			return 1;
		}
	}

	private static class Maze1MapState {
		private final Point2D location;
		private final Character symbol;

		public Maze1MapState(Point2D location, Character symbol) {
			this.location = location;
			this.symbol = symbol;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Maze1MapState that = (Maze1MapState) o;
			return Objects.equals(location, that.location) && Objects.equals(symbol, that.symbol);
		}

		@Override
		public int hashCode() {
			return Objects.hash(location, symbol);
		}

		@Override
		public String toString() {
			return String.format("%s @ %s", symbol, location);
		}
	}
}
