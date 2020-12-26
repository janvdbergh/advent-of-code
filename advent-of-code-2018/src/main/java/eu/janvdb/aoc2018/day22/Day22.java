package eu.janvdb.aoc2018.day22;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Day22 {

//	private static final int TARGET_X = 10;
//	private static final int TARGET_Y = 10;
//	private static final int DEPTH = 510;

	private static final int DEPTH = 3198;
	private static final int TARGET_X = 12;
	private static final int TARGET_Y = 757;

	private static final int SIZE = Math.max(TARGET_X, TARGET_Y) * 4 / 3;

	private static final int MODULATION_FACTOR = 20183;

	public static void main(String[] args) {
		// < 9659
		int[][] erosionLevels = getErosionLevels();

		int[][] types = getTypes(erosionLevels);

		part1(types);

		Set<Location> locations = new HashSet<>();
		Location startLocation = null;
		Location targetLocation = null;
		for (int y = 0; y <= TARGET_Y * 4 / 3; y++) {
			for (int x = 0; x <= TARGET_X * 4 / 3; x++) {
				Terrain terrain = Terrain.byType(types[x][y]);
				for (Equipment equipment : Equipment.values()) {
					if (equipment.canAccess(terrain)) {
						Location location = new Location(x, y, terrain, equipment);
						locations.add(location);
						if (x == 0 && y == 0 && equipment == Equipment.TORCH) startLocation = location;
						if (x == TARGET_X && y == TARGET_Y && equipment == Equipment.TORCH) targetLocation = location;
					}
				}
			}
		}
		if (startLocation == null || targetLocation == null) throw new IllegalStateException();

		Map<Location, Set<Connection>> connections = new HashMap<>();
		for (Location location1 : locations) {
			HashSet<Connection> connectionsForLocation = new HashSet<>();
			connections.put(location1, connectionsForLocation);
			for (Location location2 : locations) {
				location1.distance(location2)
						.ifPresent(distance -> connectionsForLocation.add(new Connection(location2, distance)));
			}
		}

		LinkedList<Location> toDo = new LinkedList<>();
		toDo.addLast(startLocation);
		startLocation.setBestDistance(0);
		while (!toDo.isEmpty()) {
			Location current = toDo.removeFirst();
			connections.get(current).forEach(connection -> {
				Location location2 = connection.getTo();
				int distanceFromCurrent = current.getBestDistance() + connection.getDistance();
				if (distanceFromCurrent < location2.getBestDistance()) {
					location2.setBestDistance(distanceFromCurrent);
					toDo.addLast(location2);
				}
			});
		}

		System.out.println(targetLocation.getBestDistance());
	}

	private static int[][] getErosionLevels() {
		int[][] erosionLevels = new int[SIZE + 1][SIZE + 1];
		for (int x = 0; x <= SIZE; x++) {
			erosionLevels[x][0] = erosionLevel(x * 16807);
		}
		for (int y = 0; y <= SIZE; y++) {
			erosionLevels[0][y] = erosionLevel(y * 48271);
		}

		for (int i = 1; i < SIZE; i++) {
			for (int y = i; y <= SIZE; y++) {
				if (i == TARGET_X && y == TARGET_Y) {
					erosionLevels[i][y] = erosionLevel(0);
				} else {
					erosionLevels[i][y] = erosionLevel(erosionLevels[i - 1][y] * erosionLevels[i][y - 1]);
				}
			}
			for (int x = i; x <= SIZE; x++) {
				if (x == TARGET_X && i == TARGET_Y) {
					erosionLevels[x][i] = erosionLevel(0);
				} else {
					erosionLevels[x][i] = erosionLevel(erosionLevels[x - 1][i] * erosionLevels[x][i - 1]);
				}
			}
		}
		return erosionLevels;
	}

	private static int erosionLevel(int geologicalIndex) {
		return (geologicalIndex + DEPTH) % MODULATION_FACTOR;
	}

	private static int[][] getTypes(int[][] erosionLevels) {
		int[][] types = new int[SIZE + 1][SIZE + 1];
		for (int y = 0; y <= SIZE; y++) {
			for (int x = 0; x <= SIZE; x++) {
				types[x][y] = erosionLevels[x][y] % 3;
			}
		}
		return types;
	}

	private static void part1(int[][] types) {
		int sum = 0;
		for (int y = 0; y <= TARGET_Y; y++) {
			for (int x = 0; x <= TARGET_X; x++) {
				sum += types[x][y];
			}
		}
		System.out.println(sum);
	}
}

