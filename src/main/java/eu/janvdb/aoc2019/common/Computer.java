package eu.janvdb.aoc2019.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Computer {

	private static final int[] MODE_DIVIDERS = {100, 1000, 10000};

	private static final int POSITION = 0;
	private static final int IMMEDIATE = 1;

	private static final int ADD = 1;
	private static final int MUL = 2;
	private static final int INPUT = 3;
	private static final int OUTPUT = 4;
	private static final int JUMP_IF_TRUE = 5;
	private static final int JUMP_IF_FALSE = 6;
	private static final int LESS_THAN = 7;
	private static final int EQUALS = 8;

	private final int[] state;
	private final Supplier<Integer> input;
	private final Consumer<Integer> output;

	private int pc;

	public Computer(int[] state, Supplier<Integer> input, Consumer<Integer> output) {
		this.state = state.clone();
		this.input = input;
		this.output = output;
	}

	public Computer(int[] state) {
		this(state, () -> 0, System.out::println);
	}

	public int runWithInput(int verb, int noun) {
		state[1] = verb;
		state[2] = noun;
		return run();
	}

	public int run() {
		pc = 0;
		while (state[pc] != 99) {
			switch (getCurrentOpcode()) {
				case ADD:
					setArgument(2, getArgument(0) + getArgument(1));
					pc += 4;
					break;
				case MUL:
					setArgument(2, getArgument(0) * getArgument(1));
					pc += 4;
					break;
				case INPUT:
					setArgument(0, input.get());
					pc += 2;
					break;
				case OUTPUT:
					output.accept(getArgument(0));
					pc += 2;
					break;
				case JUMP_IF_TRUE:
					if (getArgument(0) != 0) {
						pc = getArgument(1);
					} else {
						pc += 3;
					}
					break;
				case JUMP_IF_FALSE:
					if (getArgument(0) == 0) {
						pc = getArgument(1);
					} else {
						pc += 3;
					}
					break;
				case LESS_THAN:
					setArgument(2, getArgument(0) < getArgument(1) ? 1 : 0);
					pc += 4;
					break;
				case EQUALS:
					setArgument(2, getArgument(0) == getArgument(1) ? 1 : 0);
					pc += 4;
					break;
				case 99:
					break;
				default:
					throw new IllegalStateException();
			}
		}

		return state[0];
	}

	private int getCurrentOpcode() {
		return state[pc] % 100;
	}

	private int getArgument(int index) {
		switch (getMode(index)) {
			case POSITION:
				return state[state[pc + index + 1]];
			case IMMEDIATE:
				return state[pc + index + 1];
			default:
				throw new IllegalArgumentException(pc + "/" + state[pc]);
		}
	}

	private void setArgument(int index, int value) {
		switch (getMode(index)) {
			case POSITION:
				state[state[pc + index + 1]] = value;
				break;
			case IMMEDIATE:
				state[pc + index + 1] = value;
				break;
			default:
				throw new IllegalArgumentException(pc + "/" + state[pc]);
		}
	}

	private int getMode(int index) {
		return (state[pc] / MODE_DIVIDERS[index]) % 10;
	}
}
