package eu.janvdb.aoc2019.common;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

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
	private int pc;

	private final Subject<Integer> output = ReplaySubject.create();
	private final Queue<Integer> inputQueue = new LinkedList<>();
	private final List<Disposable> subscriptions = new ArrayList<>();


	public Computer(int[] state) {
		this.state = state.clone();
	}

	public Computer(int[] state, Observable<Integer> input, Consumer<Integer> outputConsumer) {
		this(state);
		connectInput(input);
		subscriptions.add(getOutput().subscribe(outputConsumer::accept));
	}

	public void connectInput(Observable<Integer> source) {
		subscriptions.add( source.subscribe(this::addInputValue));
	}

	public Subject<Integer> getOutput() {
		return output;
	}

	private synchronized void addInputValue(int integer) {
		inputQueue.add(integer);
		notifyAll();
	}

	private synchronized int getNextInputValue() {
		while (inputQueue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new RuntimeException("Interrupted");
			}
		}
		return inputQueue.remove();
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
					setArgument(0, getNextInputValue());
					pc += 2;
					break;
				case OUTPUT:
					output.onNext(getArgument(0));
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
					output.onComplete();
					subscriptions.forEach(Disposable::dispose);
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
