package eu.janvdb.aoc2019.day20;

import eu.janvdb.aocutil.java.Point2D;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Queue;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;

import static eu.janvdb.aocutil.java.Direction.EAST;
import static eu.janvdb.aocutil.java.Direction.NORTH;
import static eu.janvdb.aocutil.java.Direction.SOUTH;
import static eu.janvdb.aocutil.java.Direction.WEST;

public class MazeWithBypasses {

	private boolean[][] maze;
	private Set<Tuple2<Point2D, Point2D>> bypasses;
	private Point2D start;
	private Point2D end;

	public MazeWithBypasses(List<String> lines) {
		new Reader(lines).readMaze();
	}

	public int shortestPathUsingBypasses() {
		int[][] map = new int[maze.length][];
		for (int y = 0; y < maze.length; y++) {
			map[y] = new int[maze[y].length];
			for (int x = 0; x < maze[y].length; x++) {
				map[y][x] = Integer.MAX_VALUE / 2;
			}
		}

		Queue<Tuple2<Point2D, Integer>> toDo = Queue.of(Tuple.of(start, 0));
		while (!toDo.isEmpty()) {
			var item = toDo.dequeue();
			toDo = item._2;

			Point2D location = item._1._1;
			int score = item._1._2;

			int y = location.getY();
			int x = location.getX();
			if (maze[y][x] && map[y][x] > score) {
				map[y][x] = score;

				int newScore = score + 1;
				toDo = toDo.enqueue(Tuple.of(location.step(NORTH), newScore));
				toDo = toDo.enqueue(Tuple.of(location.step(SOUTH), newScore));
				toDo = toDo.enqueue(Tuple.of(location.step(WEST), newScore));
				toDo = toDo.enqueue(Tuple.of(location.step(EAST), newScore));
				toDo = toDo.enqueueAll(
						bypasses.toStream()
								.filter(bypass -> bypass._1.equals(location))
								.map(bypass -> Tuple.of(bypass._2, newScore))
				);
				toDo = toDo.enqueueAll(
						bypasses.toStream()
								.filter(bypass -> bypass._2.equals(location))
								.map(bypass -> Tuple.of(bypass._1, newScore))
				);
			}
		}

		return map[end.getY()][end.getX()];
	}

	public void print(PrintStream output) {
		for (boolean[] line : maze) {
			for (boolean cell : line) {
				output.print(cell ? '.' : '#');
			}
			output.println();
		}
		output.println();
	}

	private class Reader {

		private static final char WALL = '#';
		private static final char OPEN = '.';
		private static final String START = "AA";
		private static final String END = "ZZ";

		private final List<String> lines;
		private int minX, minY, maxX, maxY;

		private Reader(List<String> lines) {
			int lineLength = lines.map(String::length).max().getOrElse(0);

			this.lines = lines.map(line -> StringUtils.rightPad(line, lineLength));
		}

		private void readMaze() {
			findBoundaries();
			createMapWithWalls();
			readMazeMap();
			readBypasses();
		}

		private void findBoundaries() {
			minX = minY = Integer.MAX_VALUE;
			maxX = maxY = Integer.MIN_VALUE;
			for (int y = 0; y < lines.length(); y++) {
				for (int x = 0; x < lines.get(y).length(); x++) {
					char ch = lines.get(y).charAt(x);
					if (ch == WALL || ch == OPEN) {
						if (x < minX) minX = x;
						if (y < minY) minY = y;
						if (x > maxX) maxX = x;
						if (y > maxY) maxY = y;
					}
				}
			}
		}

		private void createMapWithWalls() {
			maze = new boolean[maxY - minY + 3][];
			for (int y = 0; y < maze.length; y++) {
				maze[y] = new boolean[maxX - minX + 3];
			}
		}

		private void readMazeMap() {
			for (int y = minY; y <= maxY; y++) {
				for (int x = minX; x <= maxX; x++) {
					char ch = lines.get(y).charAt(x);
					if (ch == OPEN) {
						maze[y - minY + 1][x - minX + 1] = true;
					}
				}
			}
		}

		private void readBypasses() {
			Set<Tuple2<String, Point2D>> specialCoordinates = HashSet.empty();
			for (int y = 2; y < lines.length() - 2; y++) {
				for (int x = 2; x < lines.get(y).length() - 2; x++) {
					specialCoordinates = specialCoordinates.addAll(findSpecialCoordinatesAt(x, y));
				}
			}

			start = findCoordinate(specialCoordinates, START);
			end = findCoordinate(specialCoordinates, END);

			bypasses = specialCoordinates.groupBy(Tuple2::_1)
					.filter(value -> !value._1.equals(START) && !value._1.equals(END))
					.map(entry -> {
						List<Point2D> values = entry._2.map(Tuple2::_2).toList();
						if (values.length() != 2) throw new IllegalArgumentException();
						return Tuple.of(values.get(0), values.get(1));
					})
					.toSet();
		}

		private Point2D findCoordinate(Set<Tuple2<String, Point2D>> specialCoordinates, String name) {
			return specialCoordinates.find(specialCoordinate -> specialCoordinate._1.equals(name)).getOrElseThrow(IllegalArgumentException::new)._2;
		}

		private Stream<Tuple2<String, Point2D>> findSpecialCoordinatesAt(int x, int y) {
			Stream<Tuple2<String, Point2D>> items = Stream.empty();
			if (lines.get(y).charAt(x) != OPEN) {
				return items;
			}

			items = items.appendAll(findTokenAt(x - 2, y, x - 1, y, x, y));
			items = items.appendAll(findTokenAt(x + 1, y, x + 2, y, x, y));
			items = items.appendAll(findTokenAt(x, y - 2, x, y - 1, x, y));
			items = items.appendAll(findTokenAt(x, y + 1, x, y + 2, x, y));

			return items;
		}

		private Stream<Tuple2<String, Point2D>> findTokenAt(int x1, int y1, int x2, int y2, int x3, int y3) {
			char ch1 = lines.get(y1).charAt(x1);
			char ch2 = lines.get(y2).charAt(x2);
			if (Character.isLetter(ch1) && Character.isLetter(ch2)) {
				return Stream.of(Tuple.of("" + ch1 + ch2, new Point2D(x3 - minX + 1, y3 - minY + 1)));
			}
			return Stream.empty();
		}
	}
}
