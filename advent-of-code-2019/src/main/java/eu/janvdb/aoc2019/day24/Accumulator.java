package eu.janvdb.aoc2019.day24;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static eu.janvdb.aoc2019.day24.BugPlanet.SIZE;

class Accumulator {
	private final SortedMap<Integer, int[][]> numberOfNeighboursPerLevel;
	private final BugPlanet bugPlanet;

	public Accumulator(BugPlanet bugPlanet) {
		this.bugPlanet = bugPlanet;
		this.numberOfNeighboursPerLevel = new TreeMap<>();
	}

	public void increment(int level, int x, int y, int fromX, int fromY) {
		if (bugPlanet.isRecursive()) {
			incrementRecursive(level, x, y, fromX, fromY);
		} else {
			incrementNonRecursive(level, x, y);
		}
	}

	private void incrementNonRecursive(int level, int x, int y) {
		if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
			increment(level, x, y);
		}
	}

	private void incrementRecursive(int level, int x, int y, int fromX, int fromY) {
		if (x < 0) {
			increment(level - 1, 1, 2);
		} else if (x >= SIZE) {
			increment(level - 1, 3, 2);
		} else if (y < 0) {
			increment(level - 1, 2, 1);
		} else if (y >= SIZE) {
			increment(level - 1, 2, 3);
		} else if (x == 2 && y == 2) {
			for (int z = 0; z< SIZE; z++) {
				if (fromX == 1) increment(level + 1, 0, z);
				if (fromX == 3) increment(level + 1, SIZE-1, z);
				if (fromY == 1) increment(level + 1, z, 0);
				if (fromY == 3) increment(level + 1, z, SIZE-1);
			}
		} else {
			increment(level, x, y);
		}
	}

	private void increment(int level, int x, int y) {
		int[][] numberOfNeighbours = numberOfNeighboursPerLevel.computeIfAbsent(level, _level -> createAccumulator());
		numberOfNeighbours[y][x]++;
	}

	private int[][] createAccumulator() {
		int[][] result = new int[SIZE][];
		for (int i = 0; i < SIZE; i++) {
			result[i] = new int[SIZE];
		}
		return result;
	}

	public BugPlanet toNewBugPlanet() {
		Map<Integer, List<List<Boolean>>> newBugs = numberOfNeighboursPerLevel.keySet().stream().collect(Collectors.toMap(
				key -> key, this::mapLevel
		));
		return new BugPlanet(new TreeMap<>(newBugs), bugPlanet.isRecursive());
	}

	private List<List<Boolean>> mapLevel(int level) {
		List<List<Boolean>> newBugs = new ArrayList<>(SIZE);
		for (int y = 0; y < SIZE; y++) {
			newBugs.add(new ArrayList<>(SIZE));
			for (int x = 0; x < SIZE; x++) {
				int neighbours = numberOfNeighboursPerLevel.get(level)[y][x];
				boolean newBug = bugPlanet.isBug(level, x, y) ? neighbours == 1 : (neighbours == 1 || neighbours == 2);
				newBugs.get(y).add(newBug);
			}
		}
		return newBugs;
	}
}
