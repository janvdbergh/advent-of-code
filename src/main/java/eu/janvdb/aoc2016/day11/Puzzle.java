package eu.janvdb.aoc2016.day11;

import static eu.janvdb.aoc2016.day11.State.Builder.aState;
import static java.util.Collections.singleton;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import eu.janvdb.util.Holder;

public class Puzzle {

	private static final int MAX = Integer.MAX_VALUE / 2;

	public static void main(String[] args) {
		new Puzzle().execute0();
	}

	private void execute0() {
//		State initialState = getExampleInitialState();
		State initialState = getRealInitialState();
		Map<State, Set<State>> connectedStates = getConnectedStates(initialState);
		Map<State, Integer> distances = getDistanceMap(initialState, connectedStates);
		printEndPositions(distances);
	}

	private State getExampleInitialState() {
		return aState()
				.withElevatorFloor(1)
				.withItem(1, 2)
				.withItem(1, 3)
				.build();
	}

	private State getRealInitialState() {
		/*
		 * The first floor contains a thulium generator, a thulium-compatible microchip, a plutonium generator, and a strontium generator.
		 * The second floor contains a plutonium-compatible microchip and a strontium-compatible microchip.
		 * The third floor contains a promethium generator, a promethium-compatible microchip, a ruthenium generator, and a ruthenium-compatible microchip.
		 * The fourth floor contains nothing relevant.
		 */
		return aState()
				.withElevatorFloor(1)
				.withItem(1, 1) // thulium
				.withItem(2, 1) // plutonium
				.withItem(2, 1) // strontium
				.withItem(3, 3) // promethium
				.withItem(3, 3) // ruthenium
				.withItem(1, 1) // elerium
				.withItem(1, 1) // dilithium
				.build();
	}

	private Map<State, Set<State>> getConnectedStates(State initialState) {
		int step = 0;
		Set<State> statesToVisit = new LinkedHashSet<>(singleton(initialState));
		Map<State, Set<State>> connectedStates = new LinkedHashMap<>();
		while (!statesToVisit.isEmpty()) {
			State currentState = statesToVisit.iterator().next();
			statesToVisit.remove(currentState);

			Set<State> connections = currentState.getConnectedStates();
			connectedStates.put(currentState, connections);

			Set<State> newStates = new HashSet<>(connections);
			newStates.removeAll(connectedStates.keySet());
			statesToVisit.addAll(newStates);

			if (++step % 1000 == 0) {
				System.out.println(statesToVisit.size() + " / " + connectedStates.size());
			}
		}

		return connectedStates;
	}

	private Map<State, Integer> getDistanceMap(State initialState, Map<State, Set<State>> connectedStates) {
		Set<State> states = connectedStates.keySet();
		Map<State, Integer> distances = states.stream()
				.collect(Collectors.toMap(Function.identity(), (state) -> MAX));
		distances.put(initialState, 0);

		Holder<Integer> numberChanged = new Holder<>(1);
		while (numberChanged.getValue() != 0) {
			numberChanged.setValue(0);

			states.forEach(state -> {
				int minDistance = 1 + connectedStates.get(state).stream()
						.mapToInt(distances::get)
						.min().orElse(MAX);

				if (minDistance < distances.get(state)) {
					distances.put(state, minDistance);
					numberChanged.setValue(numberChanged.getValue()+1);
				}
			});
		}

		return distances;
	}

	private void printEndPositions(Map<State, Integer> distances) {
		distances.entrySet().stream()
				.filter(entry -> entry.getKey().isSolved())
				.forEach(System.out::println);
	}

}
