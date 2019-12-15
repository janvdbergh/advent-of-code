package eu.janvdb.aoc2019.day15;

import eu.janvdb.aoc2019.common.Computer;
import eu.janvdb.util.Direction;
import eu.janvdb.util.Point2D;
import io.reactivex.subjects.BehaviorSubject;

public class MazeWalker {

	private final long[] program;

	private DataExchanger dataExchanger;
	private Maze maze;
	private Thread computerThread;

	public MazeWalker(long[] program) {
		this.program = program;
	}

	public Maze walkMaze() {
		startComputer();
		initializeMaze();
		walkMazeRecursive(new Point2D(0, 0));

		stopComputer();
		return maze;
	}

	private void startComputer() {
		Computer computer = new Computer(program);
		dataExchanger = new DataExchanger(computer);
		computerThread = new Thread(computer::run, "computer");
		computerThread.start();
	}

	private void stopComputer() {
		try {
			computerThread.interrupt();
			computerThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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

		Maze.Type type = stepInDirection(direction);
		maze.setTypeAt(next, type);
		if (type != Maze.Type.WALL) {
			walkMazeRecursive(next);
			Maze.Type typeBack = stepInDirection(direction.reverse());
			if (typeBack == Maze.Type.WALL) throw new IllegalStateException();
		}
	}

	private Maze.Type stepInDirection(Direction direction) {
		return dataExchanger.exchangeData(direction);
	}

	private void initializeMaze() {
		maze = new Maze();
	}

	private static class DataExchanger {

		private BehaviorSubject<Long> inputSubject = BehaviorSubject.create();
		private Integer lastValue = null;

		public DataExchanger(Computer computer) {
			computer.reconnectInput(inputSubject);
			computer.reconnectOutput().subscribe(this::storeValue);
		}

		private synchronized void storeValue(long value) {
			if (lastValue != null) throw new IllegalStateException("Value already set");
			lastValue = (int) value;
			notifyAll();
		}

		public synchronized Maze.Type exchangeData(Direction direction) {
			inputSubject.onNext(mapDirectionToValue(direction));

			while (lastValue == null) {
				try {
					wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

			Maze.Type result = mapResultToType(lastValue);
			lastValue = null;
			return result;
		}

		private long mapDirectionToValue(Direction direction) {
			switch (direction) {
				case NORTH:
					return 1;
				case SOUTH:
					return 2;
				case WEST:
					return 3;
				case EAST:
					return 4;
			}
			throw new IllegalArgumentException();
		}

		private Maze.Type mapResultToType(int lastValue) {
			switch (lastValue) {
				case 0:
					return Maze.Type.WALL;
				case 1:
					return Maze.Type.EMPTY;
				case 2:
					return Maze.Type.OXYGEN;
			}
			throw new IllegalArgumentException();
		}
	}
}
