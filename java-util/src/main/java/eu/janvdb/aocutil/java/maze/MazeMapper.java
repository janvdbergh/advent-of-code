package eu.janvdb.aocutil.java.maze;

import eu.janvdb.aocutil.java.Direction;
import eu.janvdb.aocutil.java.Point2D;

public class MazeMapper {

	private final Stepper stepper;
	private MazeBuilder mazeBuilder;

	public MazeMapper(Stepper stepper) {
		this.stepper = stepper;
	}

	public Maze createMaze() {
		initializeMaze();
		walkMazeRecursive(new Point2D(0, 0));
		return mazeBuilder.getMaze();
	}

	private void initializeMaze() {
		mazeBuilder = new MazeBuilder();
		mazeBuilder.setTypeAt(new Point2D(0, 0), Stepper.START);
	}

	private void walkMazeRecursive(Point2D location) {
		for (Direction direction : Direction.values()) {
			tryStep(location, direction);
		}
	}

	private void tryStep(Point2D location, Direction direction) {
		Point2D next = location.step(direction, 1);
		if (mazeBuilder.getTypeAt(next).isDefined()) {
			return;
		}

		char type = stepper.step(direction);
		mazeBuilder.setTypeAt(next, type);
		if (type != Stepper.WALL) {
			walkMazeRecursive(next);
			char typeBack = stepper.step(direction.reverse());
			if (typeBack == Stepper.WALL) throw new IllegalStateException();
		}
	}

	public interface Stepper {
		char START = 'X';
		char EMPTY = '.';
		char WALL = '#';

		char step(Direction direction);
	}
}
