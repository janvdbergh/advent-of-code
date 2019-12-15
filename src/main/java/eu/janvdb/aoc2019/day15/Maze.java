package eu.janvdb.aoc2019.day15;

import eu.janvdb.util.Direction;
import eu.janvdb.util.Point2D;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

public class Maze {

	private Map<Point2D, Type> maze = HashMap.of(new Point2D(0, 0), Type.EMPTY);

	public Type getTypeAt(Point2D point2D) {
		return maze.get(point2D).getOrElse(Type.UNKNOWN);
	}

	public void setTypeAt(Point2D point2D, Type type) {
		maze = maze.put(point2D, type);
	}

	public void print() {
		int minX = maze.keySet().minBy(Point2D::getX).get().getX();
		int maxX = maze.keySet().maxBy(Point2D::getX).get().getX();
		int minY = maze.keySet().minBy(Point2D::getY).get().getY();
		int maxY = maze.keySet().maxBy(Point2D::getY).get().getY();

		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				if (x == 0 && y == 0) {
					System.out.print('x');
				} else {
					System.out.print(getTypeAt(new Point2D(x, y)).getDisplayChar());
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public int shortestPathFromOriginToOxygen() {
		Map<Point2D, Integer> shortestPathToOrigin = buildShortestPathMap(new Point2D(0, 0));
		Point2D oxygen = getOxygenLocation();
		return shortestPathToOrigin.get(oxygen).getOrElseThrow(IllegalStateException::new);
	}

	public int timeToFillWithOxygen() {
		Map<Point2D, Integer> shortestPathToOxygen = buildShortestPathMap(findOxygen());
		return shortestPathToOxygen.values().max().getOrElseThrow(IllegalStateException::new);
	}

	private Map<Point2D, Integer> buildShortestPathMap(Point2D origin) {
		Map<Point2D, Integer> shortestPathToOrigin = maze.keySet().toStream()
				.filter(point2D -> getTypeAt(point2D) == Type.EMPTY || getTypeAt(point2D) == Type.OXYGEN)
				.toMap(point2D -> Tuple.of(point2D, maze.length() + 1))
				.put(origin, 0);

		boolean stop = false;
		while (!stop) {
			Map<Point2D, Integer> originalMap = shortestPathToOrigin;
			shortestPathToOrigin = originalMap.toMap(
					Tuple2::_1, point2D -> findShortest(originalMap, point2D)
			);
			stop = originalMap.equals(shortestPathToOrigin);
		}
		return shortestPathToOrigin;
	}

	private Point2D getOxygenLocation() {
		return maze.keySet().find(point2D -> getTypeAt(point2D) == Type.OXYGEN).getOrElseThrow(IllegalStateException::new);
	}

	private int findShortest(Map<Point2D, Integer> shortestMap, Tuple2<Point2D, Integer> current) {
		Integer shortestViaNeighbour = Stream.of(Direction.values())
				.map(direction -> current._1().step(direction, 1))
				.map(point2D -> shortestMap.getOrElse(point2D, maze.length() + 1))
				.min().getOrElse(maze.length() + 1);

		return Math.min(current._2, shortestViaNeighbour + 1);
	}

	public Point2D findOxygen() {
		return maze.find(entry -> entry._2 == Type.OXYGEN).getOrElseThrow(IllegalStateException::new)._1;
	}

	public enum Type {
		UNKNOWN('?'),
		WALL('#'),
		EMPTY(' '),
		OXYGEN('.');

		private char displayChar;

		Type(char displayChar) {
			this.displayChar = displayChar;
		}

		public char getDisplayChar() {
			return displayChar;
		}
	}
}
