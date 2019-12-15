package eu.janvdb.aoc2019.day15;

import eu.janvdb.aoc2019.common.Computer;
import eu.janvdb.aoc2019.common.InputOutputExchanger;
import eu.janvdb.util.Direction;
import eu.janvdb.util.Maze;
import eu.janvdb.util.MazeMapper;

public class ComputerMazeStepper implements MazeMapper.Stepper {

	private final Thread computerThread;
	private final InputOutputExchanger inputOutputExchanger;

	public ComputerMazeStepper(long[] program) {
		Computer computer = new Computer(program);
		inputOutputExchanger = new InputOutputExchanger(computer);
		computerThread = new Thread(computer::run, "computer");
		computerThread.start();
	}

	public void stopComputer() {
		try {
			computerThread.interrupt();
			computerThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Maze.Type step(Direction direction) {
		try {
			int input = mapDirectionToValue(direction);
			int output = (int) inputOutputExchanger.exchange(input);
			return mapResultToType(output);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private int mapDirectionToValue(Direction direction) {
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
				return Maze.Type.TREASURE;
		}
		throw new IllegalArgumentException();
	}
}
