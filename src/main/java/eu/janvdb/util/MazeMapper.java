package eu.janvdb.util;

public class MazeMapper {

	private Maze maze;
	private Stepper stepper;

	public MazeMapper(Stepper stepper) {
		this.stepper = stepper;
	}

	public Maze createMaze() {
		initializeMaze();
		walkMazeRecursive(new Point2D(0, 0));
		return maze;
	}

	private void initializeMaze() {
		maze = new Maze();
	}

	private void walkMazeRecursive(Point2D location) {
		for (Direction direction : Direction.values()) {
			tryStep(location, direction);
		}
	}

	private void tryStep(Point2D location, Direction direction) {
		Point2D next = location.step(direction, 1);
		if (maze.getTypeAt(next) != Maze.Type.UNKNOWN) {
			return;
		}

		Maze.Type type = stepper.step(direction);
		maze.setTypeAt(next, type);
		if (type != Maze.Type.WALL) {
			walkMazeRecursive(next);
			Maze.Type typeBack = stepper.step(direction.reverse());
			if (typeBack == Maze.Type.WALL) throw new IllegalStateException();
		}
	}

	public interface Stepper {
		Maze.Type step(Direction direction);
	}
}
