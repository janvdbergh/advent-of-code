package eu.janvdb.aoc2018.day20;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

class Network {

	private final Map<Location, Set<Location>> connections = new HashMap<>();
	private Map<Location, Integer> shortestPath;

	void addConnection(Location location1, Location location2) {
		addSingleConnection(location1, location2);
		addSingleConnection(location2, location1);
	}

	private void addSingleConnection(Location location1, Location location2) {
		Set<Location> outgoingConnections = connections.computeIfAbsent(location1, k -> new HashSet<>());
		outgoingConnections.add(location2);
	}

	void calculateShortestPathFrom(Location start) {
		shortestPath = new HashMap<>();
		shortestPath.put(start, 0);

		LinkedList<Location> toDo = new LinkedList<>(connections.get(start));
		Set<Location> toDoSet = new HashSet<>(toDo);

		while (!toDo.isEmpty()) {
			Location current = toDo.removeFirst();
			toDoSet.remove(current);

			int minDistance = 1 + connections.get(current).stream()
					.mapToInt(this::getDistance)
					.min()
					.orElseThrow();

			if (minDistance < getDistance(current)) {
				shortestPath.put(current, minDistance);

				connections.get(current).stream()
						.filter(location -> !toDoSet.contains(location))
						.forEach(location -> {
							toDo.addLast(location);
							toDoSet.add(location);
						});
			}
		}
	}

	Set<Location> getLocations() {
		return connections.keySet();
	}

	int getDistance(Location location) {
		Integer result = shortestPath.get(location);
		return result != null ? result : Integer.MAX_VALUE / 2;
	}

	void print() {
		Set<Location> locations = connections.keySet();

		int minx = locations.stream().mapToInt(Location::getX).min().orElseThrow();
		int miny = locations.stream().mapToInt(Location::getY).min().orElseThrow();
		int maxx = locations.stream().mapToInt(Location::getX).max().orElseThrow();
		int maxy = locations.stream().mapToInt(Location::getY).max().orElseThrow();

		for (int y = miny; y <= maxy; y++) {
			for (int x = minx; x <= maxx; x++) {
				Location previous = new Location(x, y - 1);
				Location current = new Location(x, y);
				System.out.print('#');
				if (locations.contains(current) && connections.get(current).contains(previous))
					System.out.print('-');
				else
					System.out.print('#');
			}
			System.out.println('#');

			for (int x = minx; x <= maxx; x++) {
				Location previous = new Location(x - 1, y);
				Location current = new Location(x, y);
				if (locations.contains(current) && connections.get(current).contains(previous))
					System.out.print('|');
				else
					System.out.print('#');

				if (locations.contains(current))
					System.out.print('.');
				else
					System.out.print('#');
			}
			System.out.println('#');
		}

		for (int x = minx; x <= maxx; x++) {
			System.out.print("##");
		}
		System.out.println('#');
	}
}
