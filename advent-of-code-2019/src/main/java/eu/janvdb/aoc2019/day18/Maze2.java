package eu.janvdb.aoc2019.day18;

import eu.janvdb.aocutil.java.shortestpath.MapDescription;
import eu.janvdb.aocutil.java.shortestpath.ShortestPath;
import eu.janvdb.aocutil.java.shortestpath.ShortestPathBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Maze2 {

	private final Map<MapLocation, Map<MapLocation, Integer>> distances;
	private final int numberOfKeys;
	private final List<MapLocation> startLocations;
	private final FindKeysState endState;

	public Maze2(Map<MapLocation, Map<MapLocation, Integer>> distances) {
		this.distances = distances;
		this.numberOfKeys = getLocationsOfType(MapLocation.Type.KEY).size();
		this.startLocations = getLocationsOfType(MapLocation.Type.START);
		this.endState = new FindKeysState(KeyChain.all(numberOfKeys), Collections.emptyList());
	}

	private List<MapLocation> getLocationsOfType(MapLocation.Type type) {
		return distances.keySet().stream()
				.filter(mapLocation -> mapLocation.getType() == type)
				.collect(Collectors.toList());
	}

	public int getTotalDistance() {
		FindAllKeysMapDescription mapDescription = new FindAllKeysMapDescription();
		ShortestPath<FindKeysState> shortestPath = ShortestPathBuilder.build(mapDescription);

		shortestPath.printRouteTo(endState);
		return shortestPath.distanceTo(endState);
	}

	private class FindReachableKeysMapDescription implements MapDescription<MapLocation> {
		private final KeyChain currentKeys;
		private final MapLocation startLocation;

		public FindReachableKeysMapDescription(KeyChain currentKeys, MapLocation startLocation) {
			this.currentKeys = currentKeys;
			this.startLocation = startLocation;
		}

		@Override
		public MapLocation getOrigin() {
			return startLocation;
		}

		@Override
		public List<MapLocation> getNeighbours(MapLocation currentLocation) {
			return distances.get(currentLocation).keySet().stream()
					.filter(location -> !location.equals(currentLocation))
					.filter(location -> !location.isDoor() || currentKeys.hasKey(location.getSymbol()))
					.collect(Collectors.toList());
		}

		@Override
		public int getDistance(MapLocation from, MapLocation to) {
			return distances.get(from).get(to);
		}
	}

	private class FindAllKeysMapDescription implements MapDescription<FindKeysState> {
		private final Map<FindKeysState, Map<FindKeysState, Integer>> distancesBetweenStates = new HashMap<>();

		@Override
		public FindKeysState getOrigin() {
			return new FindKeysState(KeyChain.empty(numberOfKeys), startLocations);
		}

		@Override
		public List<FindKeysState> getNeighbours(FindKeysState currentState) {
			if (currentState.equals(endState)) return Collections.emptyList();
			if (currentState.currentKeys.isComplete()) return List.of(endState);

			List<FindKeysState> result = new ArrayList<>();
			for (int index = 0; index < currentState.locations.size(); index++) {
				Map<FindKeysState, Integer> newReachableKeys = getNewReachableKeys(currentState, index);
				distancesBetweenStates.computeIfAbsent(currentState, findKeysState -> new HashMap<>()).putAll(newReachableKeys);
				result.addAll(newReachableKeys.keySet());
			}
			return result;
		}

		private Map<FindKeysState, Integer> getNewReachableKeys(FindKeysState currentState, int index) {
			FindReachableKeysMapDescription mapDescription = new FindReachableKeysMapDescription(currentState.currentKeys, currentState.locations.get(index));
			ShortestPath<MapLocation> shortestPath = ShortestPathBuilder.build(mapDescription);
			return shortestPath.getReachablePoints().stream()
					.filter(MapLocation::isKey)
					.filter(location -> !currentState.currentKeys.hasKey(location.getSymbol()))
					.collect(Collectors.toMap(
							mapLocation -> {
								KeyChain newKeys = currentState.currentKeys.setKey(mapLocation.getSymbol());
								List<MapLocation> newLocations = new ArrayList<>(currentState.locations);
								newLocations.set(index, mapLocation);
								return new FindKeysState(newKeys, newLocations);
							},
							shortestPath::distanceTo)
					);
		}

		@Override
		public int getDistance(FindKeysState from, FindKeysState to) {
			if (to.equals(endState)) return 0;
			return distancesBetweenStates.get(from).get(to);
		}
	}

	private static class FindKeysState {
		private final KeyChain currentKeys;
		private final List<MapLocation> locations;

		public FindKeysState(KeyChain currentKeys, List<MapLocation> locations) {
			this.currentKeys = currentKeys;
			this.locations = locations;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			FindKeysState that = (FindKeysState) o;
			return currentKeys.equals(that.currentKeys) && locations.equals(that.locations);
		}

		@Override
		public int hashCode() {
			return Objects.hash(currentKeys, locations);
		}

		@Override
		public String toString() {
			return String.format("%s (%s)", locations, currentKeys);
		}
	}
}
