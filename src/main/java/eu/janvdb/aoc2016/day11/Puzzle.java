package eu.janvdb.aoc2016.day11;

import static eu.janvdb.aoc2016.day11.State.Builder.aState;

import javaslang.Function1;
import javaslang.Tuple;
import javaslang.collection.LinkedHashMap;
import javaslang.collection.LinkedHashSet;
import javaslang.collection.Map;
import javaslang.collection.Set;

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
		Set<State> statesToVisit = LinkedHashSet.of(initialState);
		Map<State, Set<State>> connectedStates = LinkedHashMap.empty();
		while (!statesToVisit.isEmpty()) {
			State currentState = statesToVisit.iterator().next();
			statesToVisit = statesToVisit.remove(currentState);

			Set<State> connections = currentState.getConnectedStates();
			connectedStates = connectedStates.put(currentState, connections);

			Set<State> newStates = connections.removeAll(connectedStates.keySet());
			statesToVisit = statesToVisit.addAll(newStates);

			if (++step % 1000 == 0) {
				System.out.println(statesToVisit.size() + " / " + connectedStates.size());
			}
		}

		return connectedStates;
	}

	private Map<State, Integer> getDistanceMap(State initialState, Map<State, Set<State>> connectedStates) {
		Set<State> states = connectedStates.keySet();
		Map<State, Integer> distances = states
				.toMap(state -> Tuple.of(state, MAX))
				.put(initialState, 0);

		boolean retry = true;
		while (retry) {
			Map<State, Integer> tempDistances = distances;
			distances = states
					.flatMap(state -> connectedStates.get(state).get()
							.flatMap(tempDistances::get)
							.min()
							.map(min -> Tuple.of(state, Math.min(tempDistances.get(state).getOrElse(MAX), min + 1)))
					)
					.toMap(Function1.identity());

			retry = !tempDistances.equals(distances);
		}

		return distances;
	}

	private void printEndPositions(Map<State, Integer> distances) {
		distances.toStream()
				.filter(entry -> entry._1.isSolved())
				.forEach(System.out::println);
	}

}
