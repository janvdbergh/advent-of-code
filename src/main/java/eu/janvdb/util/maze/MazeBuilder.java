package eu.janvdb.util.maze;

import eu.janvdb.util.Point2D;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;

import static eu.janvdb.util.maze.MazeMapper.Stepper.WALL;

public class MazeBuilder {

	private Map<Point2D, Character> maze = HashMap.empty();

	public Option<Character> getTypeAt(Point2D point2D) {
		return maze.get(point2D);
	}

	public void setTypeAt(Point2D point2D, Character type) {
		maze = maze.put(point2D, type);
	}

	public Maze getMaze() {
		int minX = maze.keySet().minBy(Point2D::getX).get().getX();
		int maxX = maze.keySet().maxBy(Point2D::getX).get().getX();
		int minY = maze.keySet().minBy(Point2D::getY).get().getY();
		int maxY = maze.keySet().maxBy(Point2D::getY).get().getY();

		char[][] mapChars = new char[maxY - minY + 1][];
		for (int y = minY; y <= maxY; y++) {
			mapChars[y - minY] = new char[maxX - minX + 1];
			for (int x = minX; x <= maxX; x++) {
				mapChars[y - minY][x - minX] = getTypeAt(new Point2D(x, y)).getOrElse(WALL);
			}
		}

		return new Maze(mapChars, String.valueOf(WALL));
	}
}
