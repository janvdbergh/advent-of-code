package eu.janvdb.aoc2019.day15;

import eu.janvdb.aoc2019.common.ValueExchangeComputer;
import eu.janvdb.util.Direction;
import eu.janvdb.util.maze.MazeMapper;

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
