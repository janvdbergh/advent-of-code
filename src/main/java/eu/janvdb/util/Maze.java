package eu.janvdb.util;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
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

	public int shortestPath(Point2D from, Point2D to) {
		return buildShortestPathMap(from)
				.get(to)
				.getOrElseThrow(IllegalStateException::new);
	}

	public int stepsToPaint(Point2D from) {
		return buildShortestPathMap(from)
				.values()
				.max()
				.getOrElseThrow(IllegalStateException::new);
	}

	private Map<Point2D, Integer> buildShortestPathMap(Point2D origin) {
		Map<Point2D, Integer> shortestPathToOrigin = maze.keySet().toStream()
				.filter(point2D -> getTypeAt(point2D) == Type.EMPTY || getTypeAt(point2D) == Type.TREASURE)
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

	private int findShortest(Map<Point2D, Integer> shortestMap, Tuple2<Point2D, Integer> current) {
		Integer shortestViaNeighbour = Stream.of(Direction.values())
				.map(direction -> current._1().step(direction, 1))
				.map(point2D -> shortestMap.getOrElse(point2D, maze.length() + 1))
				.min().getOrElse(maze.length() + 1);

		return Math.min(current._2, shortestViaNeighbour + 1);
	}

	public Set<Point2D> find(Type type) {
		return maze.keySet().filter(point2D -> getTypeAt(point2D) == type);
	}

	public enum Type {
		UNKNOWN('?'),
		WALL('#'),
		EMPTY(' '),
		TREASURE('.');

		private char displayChar;

		Type(char displayChar) {
			this.displayChar = displayChar;
		}

		public char getDisplayChar() {
			return displayChar;
		}
	}
}
