package eu.janvdb.aoc2019.day18;

import eu.janvdb.aocutil.java.shortestpath.MapDescription;
import eu.janvdb.aocutil.java.shortestpath.ShortestPath;
import eu.janvdb.aocutil.java.shortestpath.ShortestPathBuilder;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Maze2 {

	private final Map<Character, Map<Character, Integer>> distances;
	private final Maze2MapState endState;
	private final int numberOfKeys;

	public Maze2(Map<Character, Map<Character, Integer>> distances) {
		this.distances = distances;
		this.numberOfKeys = (int) distances.keySet().stream().filter(ch -> ch >= 'a' && ch <= 'z').count();
		this.endState = new Maze2MapState('$', allSet());
	}

	public int getTotalDistance() {
		ShortestPath<Maze2MapState> shortestPath = ShortestPathBuilder.build(new Maze2MapDescription());
		return shortestPath.distanceTo(endState);
	}

	private BitSet allClear() {
		int numberOfKeys = this.numberOfKeys;
		return new BitSet(numberOfKeys);
	}

	private BitSet allSet() {
		BitSet bitSet = allClear();
		bitSet.set(0, numberOfKeys, true);
		return bitSet;
	}

	private class Maze2MapDescription implements MapDescription<Maze2MapState> {
		@Override
		public Maze2MapState getOrigin() {
			return new Maze2MapState('@', allClear());
		}

		@Override
		public List<Maze2MapState> getNeighbours(Maze2MapState state) {
			if (state == endState) return Collections.emptyList();
			if (state.keys.cardinality() == numberOfKeys) return Collections.singletonList(endState);

			Map<Character, Integer> thisDistances = distances.get(state.symbol);
			return thisDistances.keySet().stream()
					.filter(symbol -> isNotDoorWithoutKey(symbol, state.keys))
					.map(symbol -> createState(symbol, state.keys))
					.collect(Collectors.toList());
		}

		private boolean isNotDoorWithoutKey(char symbol, BitSet keys) {
			if (symbol >= 'A' && symbol <= 'Z') {
				int index = symbol - 'A';
				return keys.get(index);
			}
			return true;
		}

		private Maze2MapState createState(char symbol, BitSet keys) {
			if (symbol >= 'a' && symbol <= 'z') {
				int index = symbol - 'a';
				if (!keys.get(index)) {
					keys = (BitSet) keys.clone();
					keys.set(index);
				}
			}
			return new Maze2MapState(symbol, keys);
		}

		@Override
		public int getDistance(Maze2MapState from, Maze2MapState to) {
			if (to == endState) return 0;
			return distances.get(from.symbol).get(to.symbol);
		}
	}

	private static class Maze2MapState {

		private final char symbol;
		private final BitSet keys;

		private Maze2MapState(char symbol, BitSet keys) {
			this.symbol = symbol;
			this.keys = keys;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Maze2MapState that = (Maze2MapState) o;
			return symbol == that.symbol && keys.equals(that.keys);
		}

		@Override
		public int hashCode() {
			return Objects.hash(symbol, keys);
		}

		@Override
		public String toString() {
			return String.format("%s %s", symbol, keys);
		}
	}

}
