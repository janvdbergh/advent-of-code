package eu.janvdb.aoc2019.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class Computer {

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

	private final Subject<Long> output = ReplaySubject.create();
	private final Queue<Long> inputQueue = new LinkedList<>();
	private final List<Disposable> subscriptions = new ArrayList<>();


	public Computer(long[] state) {
		this.state = new TreeMap<>();
		for (int i = 0; i < state.length; i++) {
			write(i, state[i]);
		}
	}

	public Computer(long[] state, Observable<Long> input, Consumer<Long> outputConsumer) {
		this(state);
		connectInput(input);
		subscriptions.add(getOutput().subscribe(outputConsumer::accept));
	}

	public void connectInput(Observable<Long> source) {
		subscriptions.add(source.subscribe(this::addInputValue));
	}

	public Observable<Long> getOutput() {
		return output;
	}

	private synchronized void addInputValue(long integer) {
		inputQueue.add(integer);
		notifyAll();
	}

	private synchronized long getNextInputValue() {
		while (inputQueue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new RuntimeException("Interrupted");
			}
		}
		return inputQueue.remove();
	}

	public long runWithInput(int verb, int noun) {
		write(1, verb);
		write(2, noun);
		return run();
	}

	public long run() {
		pc = 0;
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
					setArgument(0, getNextInputValue());
					pc += 2;
					break;
				case OUTPUT:
					output.onNext(getArgument(0));
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
					output.onComplete();
					subscriptions.forEach(Disposable::dispose);
					break;
				default:
					throw new IllegalStateException();
			}
		}

		return read(0);
	}

	public void dump() {
		state.forEach((key, value) -> System.out.printf("%d=%d; ", key, value));
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

	private void write(int address, long value) {
		state.put(address, value);
	}
}
