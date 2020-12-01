package eu.janvdb.aoc2019.common;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BasicComputer {

	private static final int[] MODE_DIVIDERS = {100, 1000, 10000};

	private static final int POSITION = 0;
	private static final int IMMEDIATE = 1;
	private static final int RELATIVE = 2;

	private static final int ADD = 1;
	private static final int MUL = 2;
	private static final int INPUT = 3;
	private static final int OUTPUT = 4;
	private static final int JUMP_IF_TRUE = 5;
	private static final int JUMP_IF_FALSE = 6;
	private static final int LESS_THAN = 7;
	private static final int EQUALS = 8;
	private static final int ADJUST_RELATIVE_BASE = 9;
	private static final int HALT = 99;

	private final SortedMap<Integer, Long> state;
	private int pc;
	private int relativeBase;

	private Consumer<Long> output;
	private Supplier<Long> input;

	public BasicComputer(long[] state) {
		this(state, () -> 0L, System.out::println);
	}

	public BasicComputer(long[] state, Supplier<Long> input, Consumer<Long> output) {
		this.state = new TreeMap<>();
		for (int i = 0; i < state.length; i++) {
			write(i, state[i]);
		}

		this.input = input;
		this.output = output;
	}

	public long run() {
		pc = 0;
		try {
			while (read(pc) != 99) {
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
							pc = (int) getArgument(1);
						} else {
							pc += 3;
						}
						break;
					case JUMP_IF_FALSE:
						if (getArgument(0) == 0) {
							pc = (int) getArgument(1);
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
					case ADJUST_RELATIVE_BASE:
						relativeBase += getArgument(0);
						pc += 2;
						break;
					case HALT:
						break;
					default:
						throw new IllegalStateException();
				}
			}
		} catch (RuntimeException e) {
			System.err.println("Computer terminated: " + e.getMessage());
		}

		return read(0);
	}

	private int getCurrentOpcode() {
		return (int) read(pc) % 100;
	}

	private long getArgument(int index) {
		return read(getAddress(index));
	}

	private void setArgument(int index, long value) {
		write(getAddress(index), value);
	}

	private int getAddress(int index) {
		switch (getMode(index)) {
			case POSITION:
				return (int) read(pc + index + 1);
			case IMMEDIATE:
				return pc + index + 1;
			case RELATIVE:
				return (int) read(pc + index + 1) + relativeBase;
			default:
				throw new IllegalArgumentException(pc + "/" + read(pc));
		}
	}

	private int getMode(int index) {
		return (int) (read(pc) / MODE_DIVIDERS[index]) % 10;
	}

	private long read(int address) {
		return state.getOrDefault(address, 0L);
	}

	public void write(int address, long value) {
		state.put(address, value);
	}
}
