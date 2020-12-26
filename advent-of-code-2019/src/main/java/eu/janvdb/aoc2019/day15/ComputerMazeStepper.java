package eu.janvdb.aoc2019.day15;

import eu.janvdb.aoc2019.common.ValueExchangeComputer;
import eu.janvdb.aocutil.java.Direction;
import eu.janvdb.aocutil.java.maze.MazeMapper;

public class ComputerMazeStepper implements MazeMapper.Stepper {

	public static final char OXYGEN = 'o';

	private final ValueExchangeComputer valueExchangeComputer;

	public ComputerMazeStepper(long[] program) {
		valueExchangeComputer = new ValueExchangeComputer(program);
		valueExchangeComputer.start();
	}

	public void stopComputer() {
		valueExchangeComputer.stop();
	}

	@Override
	public char step(Direction direction) {
		try {
			int input = mapDirectionToValue(direction);
			int output = (int) valueExchangeComputer.exchange(input);
			return mapResultToType(output);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private int mapDirectionToValue(Direction direction) {
		return switch (direction) {
			case NORTH -> 1;
			case SOUTH -> 2;
			case WEST -> 3;
			case EAST -> 4;
		};
	}

	private char mapResultToType(int lastValue) {
		switch (lastValue) {
			case 0:
				return WALL;
			case 1:
				return EMPTY;
			case 2:
				return OXYGEN;
		}
		throw new IllegalArgumentException();
	}
}
