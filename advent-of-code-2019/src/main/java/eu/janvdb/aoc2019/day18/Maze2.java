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

	private final Map<MapLocation, Map<MapLocation, Integer>> distances;
	private final Maze2MapState endState;
	private final int numberOfKeys;
	private final MapLocation startLocation;

	public Maze2(Map<MapLocation, Map<MapLocation, Integer>> distances) {
		this.distances = distances;
		this.numberOfKeys = getLocationsOfType(MapLocation.Type.KEY).size();
		this.startLocation = getLocationsOfType(MapLocation.Type.START).get(0);
		this.endState = new Maze2MapState(null, allSet());
	}

	private List<MapLocation> getLocationsOfType(MapLocation.Type type) {
		return distances.keySet().stream()
				.filter(mapLocation -> mapLocation.getType() == type)
				.collect(Collectors.toList());
	}

	public int getTotalDistance() {
		ShortestPath<Maze2MapState> shortestPath = ShortestPathBuilder.build(new Maze2MapDescription());
		printShortestPath(shortestPath, endState);

		return shortestPath.distanceTo(endState);
	}

	private void printShortestPath(ShortestPath<Maze2MapState> shortestPath, Maze2MapState to) {
		if (to != null) {
			printShortestPath(shortestPath, shortestPath.stepTo(to));
			System.out.printf("%d to %s%n", shortestPath.distanceTo(to), to);
		}
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

	private static String keysToString(BitSet keys) {
		return keys.stream().mapToObj(x -> String.valueOf((char) (x + 'a'))).collect(Collectors.joining());
	}

	private class Maze2MapDescription implements MapDescription<Maze2MapState> {
		@Override
		public Maze2MapState getOrigin() {
			return new Maze2MapState(startLocation, allClear());
		}

		@Override
		public List<Maze2MapState> getNeighbours(Maze2MapState state) {
			if (state == endState) return Collections.emptyList();
			if (state.keys.cardinality() == numberOfKeys) return Collections.singletonList(endState);

			Map<MapLocation, Integer> thisDistances = distances.get(state.location);
			return thisDistances.keySet().stream()
					.filter(location -> isNotDoorWithoutKey(location, state.keys))
					.map(symbol -> createState(symbol, state.keys))
					.collect(Collectors.toList());
		}

		private boolean isNotDoorWithoutKey(MapLocation location, BitSet keys) {
			if (location.getType() == MapLocation.Type.DOOR) {
				int index = location.getSymbol() - 'a';
				return keys.get(index);
			}
			return true;
		}

		private Maze2MapState createState(MapLocation location, BitSet keys) {
			if (location.getType() == MapLocation.Type.KEY) {
				int index = location.getSymbol() - 'a';
				if (!keys.get(index)) {
					keys = (BitSet) keys.clone();
					keys.set(index);
				}
			}
			return new Maze2MapState(location, keys);
		}

		@Override
		public int getDistance(Maze2MapState from, Maze2MapState to) {
			if (to == endState) return 0;
			return distances.get(from.location).get(to.location);
		}
	}

	private static class Maze2MapState {

		private final MapLocation location;
		private final BitSet keys;

		private Maze2MapState(MapLocation location, BitSet keys) {
			this.location = location;
			this.keys = keys;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Maze2MapState that = (Maze2MapState) o;
			return location == that.location && keys.equals(that.keys);
		}

		@Override
		public int hashCode() {
			return Objects.hash(location, keys);
		}

		@Override
		public String toString() {
			return String.format("%s %s", location, keysToString(keys));
		}
	}

}
