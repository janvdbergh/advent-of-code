package eu.janvdb.aoc2019.day18;

import eu.janvdb.aoc2019.day18.MapLocation.Type;
import eu.janvdb.aocutil.java.Point2D;
import eu.janvdb.aocutil.java.shortestpath.MapDescription;
import eu.janvdb.aocutil.java.shortestpath.ShortestPath;
import eu.janvdb.aocutil.java.shortestpath.ShortestPathBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Maze1 {

	public static Maze1 parse(List<String> lines) {
		int height = lines.size();
		int width = lines.get(0).length();

		boolean[][] walls = new boolean[height][];
		Map<Point2D, MapLocation> symbols = new HashMap<>();

		char currentStartSymbol = '1';

		for (int y = 0; y < height; y++) {
			walls[y] = new boolean[width];
			for (int x = 0; x < width; x++) {
				char ch = lines.get(y).charAt(x);
				walls[y][x] = ch == '#';
				Point2D location = new Point2D(x, y);
				if (ch == '@') symbols.put(location, new MapLocation(Type.START, currentStartSymbol++));
				if (ch >= 'a' && ch <= 'z') symbols.put(location, new MapLocation(Type.KEY, ch));
				if (ch >= 'A' && ch <= 'Z')
					symbols.put(location, new MapLocation(Type.DOOR, Character.toLowerCase(ch)));
			}
		}

		return new Maze1(walls, symbols);
	}

	private final boolean[][] walls;
	private final Map<Point2D, MapLocation> symbolsByLocation;

	private Maze1(boolean[][] walls, Map<Point2D, MapLocation> symbolsByLocation) {
		this.walls = walls;
		this.symbolsByLocation = symbolsByLocation;
	}

	public Map<MapLocation, Map<MapLocation, Integer>> getDistances() {
		return symbolsByLocation.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getValue, entry -> getDistances(entry.getKey())));
	}

	private Map<MapLocation, Integer> getDistances(Point2D from) {
		ShortestPath<Point2D> shortestPath = ShortestPathBuilder.build(new Maze1MapDescription(from));

		return shortestPath.getReachablePoints().stream()
				.filter(symbolsByLocation::containsKey)
				.collect(Collectors.toMap(symbolsByLocation::get, shortestPath::distanceTo));
	}

	private class Maze1MapDescription implements MapDescription<Point2D> {
		private final Point2D origin;

		public Maze1MapDescription(Point2D origin) {
			this.origin = origin;
		}

		@Override
		public Point2D getOrigin() {
			return origin;
		}

		@Override
		public List<Point2D> getNeighbours(Point2D location) {
			MapLocation mapLocation = symbolsByLocation.get(location);

			if (mapLocation != null && !location.equals(origin)) return Collections.emptyList();
			return Stream.of(location.move(0, -1), location.move(-1, 0), location.move(1, 0), location.move(0, 1))
					.filter(newLocation -> !walls[newLocation.getY()][newLocation.getX()])
					.collect(Collectors.toList());
		}

		@Override
		public int getDistance(Point2D from, Point2D to) {
			return 1;
		}
	}
}
