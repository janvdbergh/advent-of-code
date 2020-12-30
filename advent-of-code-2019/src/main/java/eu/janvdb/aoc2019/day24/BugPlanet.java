package eu.janvdb.aoc2019.day24;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class BugPlanet {

	public final static int SIZE = 5;

	private final SortedMap<Integer, List<List<Boolean>>> bugsPerLevel;
	private final boolean recursive;

	public static BugPlanet parse(List<String> lines, boolean recurse) {
		List<List<Boolean>> bugs = new ArrayList<>(SIZE);
		for (int y = 0; y < SIZE; y++) {
			bugs.add(new ArrayList<>(SIZE));
			for (int x = 0; x < SIZE; x++) {
				bugs.get(y).add(lines.get(y).charAt(x) == '#');
			}
		}

		SortedMap<Integer, List<List<Boolean>>> map = new TreeMap<>();
		map.put(0, bugs);
		return new BugPlanet(map, recurse);
	}

	public BugPlanet(SortedMap<Integer, List<List<Boolean>>> bugsPerLevel, boolean recursive) {
		this.bugsPerLevel = bugsPerLevel;
		this.recursive = recursive;
	}

	public Boolean isBug(int level, int x, int y) {
		List<List<Boolean>> bugsOnLevel = bugsPerLevel.get(level);
		return bugsOnLevel != null ? bugsOnLevel.get(y).get(x) : false;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public BugPlanet step() {
		Accumulator accumulator = new Accumulator(this);
		for (int level : bugsPerLevel.keySet()) {
			for (int y = 0; y < SIZE; y++) {
				for (int x = 0; x < SIZE; x++) {
					step(accumulator, level, x, y);
				}
			}
		}
		return accumulator.toNewBugPlanet();
	}

	private void step(Accumulator accumulator, int level, int x, int y) {
		if (isBug(level, x, y)) {
			accumulator.increment(level, x - 1, y, x, y);
			accumulator.increment(level, x + 1, y, x, y);
			accumulator.increment(level, x, y - 1, x, y);
			accumulator.increment(level, x, y + 1, x, y);
		}
	}

	public int biodiversityRating() {
		int sum = 0;
		int factor = 1;
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				if (isBug(0, x, y)) sum += factor;
				factor *= 2;
			}
		}
		return sum;
	}

	public int getNumberOfBugs() {
		return bugsPerLevel.keySet().stream()
				.mapToInt(this::getNumberOfBugsOnLevel)
				.sum();
	}

	private int getNumberOfBugsOnLevel(Integer level) {
		int sum = 0;
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				if (isBug(level, x, y)) sum++;
			}
		}
		return sum;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BugPlanet bugPlanet = (BugPlanet) o;
		return bugsPerLevel.equals(bugPlanet.bugsPerLevel);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(bugsPerLevel);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (int level : bugsPerLevel.keySet()) {
			buffer.append("Level ").append(level).append(":\n");
			for (int y = 0; y < SIZE; y++) {
				for (int x = 0; x < SIZE; x++) {
					if (isRecursive() && x == 2 && y == 2) {
						buffer.append('?');
					} else if (isBug(level, x, y)) {
						buffer.append('#');
					} else {
						buffer.append('.');
					}
				}
				buffer.append('\n');
			}
		}
		return buffer.toString();
	}
}
