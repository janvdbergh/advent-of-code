package eu.janvdb.aoc2019.day12;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;

import static eu.janvdb.util.MathUtil.kgv;

public class Day12 {
	private static final List<Moon> TEST_MOONS_1 = List.of(
			new Moon(-1, 0, 2),
			new Moon(2, -10, -7),
			new Moon(4, -8, 8),
			new Moon(3, 5, -1)
	);

	private static final List<Moon> TEST_MOONS_2 = List.of(
			new Moon(-8, -10, 0),
			new Moon(5, 5, 10),
			new Moon(2, -7, 3),
			new Moon(9, -8, -3)
	);

	private static final List<Moon> REAL_MOONS = List.of(
			new Moon(-1, 7, 3),
			new Moon(12, 2, -13),
			new Moon(14, 18, -8),
			new Moon(17, 4, -4)
	);

	public static void main(String[] args) {
		new Day12().run();
	}

	private void run() {
//		part1();
		part2();
	}

	private void part1() {
		runMoonsForNSteps(TEST_MOONS_1, 10);
		runMoonsForNSteps(TEST_MOONS_2, 100);
		runMoonsForNSteps(REAL_MOONS, 1000);
	}

	private void runMoonsForNSteps(List<Moon> moons, int numberOfSteps) {
		printMoons(0, moons);

		for (int step = 1; step <= numberOfSteps; step++) {
			List<Moon> currentMoons = moons;
			moons = moons.map(moon -> moon.update(currentMoons));
		}

		printMoons(numberOfSteps, moons); // > 1803, < 7356
	}

	private void printMoons(int step, List<Moon> moons) {
		System.out.printf("After %d steps:\n", step);
		moons.forEach(System.out::println);
		System.out.printf("Total energy: %d\n\n", moons.map(Moon::getEnergy).sum().intValue());
	}

	private void part2() {
		findRepetition(TEST_MOONS_1);
		findRepetition(TEST_MOONS_2);
		findRepetition(REAL_MOONS);
	}

	private void findRepetition(List<Moon> moons) {
		int repetitionX = this.findRepetition1D(moons.map(Moon::getX));
		int repetitionY = this.findRepetition1D(moons.map(Moon::getY));
		int repetitionZ = this.findRepetition1D(moons.map(Moon::getZ));

		System.out.printf("%d, %d, %d, %d\n", repetitionX, repetitionY, repetitionZ, kgv(repetitionX, kgv(repetitionY, repetitionZ)));
	}

	private int findRepetition1D(List<Moon1D> moons) {
		HashSet<List<Moon1D>> foundStates = HashSet.of(moons);

		List<Moon1D> state = moveMoons(moons);
		while (!foundStates.contains(state)) {
			foundStates = foundStates.add(state);
			state = moveMoons(state);
		}

		return foundStates.length();
	}

	private List<Moon1D> moveMoons(List<Moon1D> moons) {
		return moons.map(moon -> moon.update(moons));
	}
}
