package eu.janvdb.aoc2017.day25;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class TuringMachine {

	private final Map<String, State> states;

	private final Set<Integer> onesOnTape = new HashSet<>();
	private int cursor = 0;

	TuringMachine(State... states) {
		this.states = new HashMap<>();
		Arrays.asList(states).forEach(state -> this.states.put(state.getName(), state));
	}

	int countOnes() {
		return onesOnTape.size();
	}

	void executeStates(int iterations, String startState) {
		String stateName = startState;

		for (int i = 0; i < iterations; i++) {
			stateName = executeStateAndReturnsNextStateName(stateName);
		}
	}

	private String executeStateAndReturnsNextStateName(String stateName) {
		State state = states.get(stateName);
		if (state == null) {
			throw new IllegalStateException();
		}

		if (getCurrentValue()) {
			setCurrentValue(state.getValueToWriteOnOne());
			cursor += state.getDirectionToMoveOnOne() == Direction.RIGHT ? 1 : -1;
			return state.getNextStateOnOne();
		} else {
			setCurrentValue(state.getValueToWriteOnZero());
			cursor += state.getDirectionToMoveOnZero() == Direction.RIGHT ? 1 : -1;
			return state.getNextStateOnZero();
		}
	}

	private boolean getCurrentValue() {
		return onesOnTape.contains(cursor);
	}

	private void setCurrentValue(boolean newValue) {
		if (newValue) {
			onesOnTape.add(cursor);
		} else {
			onesOnTape.remove(cursor);
		}
	}

	@Override
	public String toString() {
		return "TuringMachine{" +
				"cursor=" + cursor +
				", onesOnTape=" + onesOnTape +
				'}';
	}
}
