package eu.janvdb.aoc2018.day12;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class Day12 {

	private static final int MIN_INDEX = -5;
	private static final int MAX_INDEX = 300;
	private static final int NUMBER_GENERATIONS = 200;

	private static final String INITIAL_STATE_REAL = "##.##.##..#..#.#.#.#...#...#####.###...#####.##..#####.#..#.##..#..#.#...#...##.##...#.##......####.";
	private static final String INITIAL_STATE_TEST = "#..#.#..##......###...###";

	private static final String[] PRODUCING_RULES_REAL = {
			"...##",
			"..#.#",
			".#...",
			".#..#",
			".#.#.",
			".#.##",
			"#..##",
			"#.#..",
			"#.##.",
			"#.###",
			"##..#",
			"##.#.",
			"##.##",
			"###..",
			"###.#",
			"####.",
			"#####"
	};
	private static final String[] PRODUCING_RULES_TEST = {
			"...##",
			"..#..",
			".#...",
			".#.#.",
			".#.##",
			".##..",
			".####",
			"#.#.#",
			"#.###",
			"##.#.",
			"##.##",
			"###..",
			"###.#",
			"####."
	};

	private static final String INITIAL_STATE = INITIAL_STATE_REAL;
	private static final String[] PRODUCING_RULES = PRODUCING_RULES_REAL;

	public static void main(String[] args) {
		part1();

		// <14068145161460046219
		// 3099999999491?
		part2();
	}

	private static void part1() {
		SortedSet<Integer> currentState = IntStream.range(0, INITIAL_STATE.length())
				.filter(index -> INITIAL_STATE.charAt(index) == '#')
				.collect(TreeSet::new, TreeSet::add, TreeSet::addAll);

		printState(currentState);
		for (long i = 0; i < NUMBER_GENERATIONS; i++) {
			currentState = getNextState(currentState);
			printState(currentState);
		}
	}

	private static SortedSet<Integer> getNextState(SortedSet<Integer> state) {
		int min = state.stream().mapToInt(x -> x).min().orElseThrow();
		int max = state.stream().mapToInt(x -> x).max().orElseThrow();

		return IntStream.range(min - 2, max + 2 + 1)
				.filter(position -> hasMatchingRule(state, position))
				.collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
	}

	private static boolean hasMatchingRule(SortedSet<Integer> state, int position) {
		return Arrays.stream(PRODUCING_RULES).anyMatch(rule -> ruleMatches(rule, state, position));
	}

	private static boolean ruleMatches(String rule, SortedSet<Integer> state, int position) {
		for (int i = -2; i <= 2; i++) {
			boolean valueInRule = rule.charAt(i + 2) == '#';
			boolean valueInState = state.contains(position + i);
			if (valueInRule != valueInState) return false;
		}

		return true;
	}

	private static void printState(SortedSet<Integer> currentState) {
		currentState.stream().mapToInt(x -> x).min().ifPresent(min -> {
			if (min < MIN_INDEX) {
				throw new IllegalArgumentException("Adjust MIN_INDEX: " + min);
			}
		});
		currentState.stream().mapToInt(x -> x).max().ifPresent(max -> {
			if (max > MAX_INDEX) {
				throw new IllegalArgumentException("Adjust MAX_INDEX: " + max);
			}
		});

		IntStream.range(MIN_INDEX, MAX_INDEX + 1)
				.mapToObj(value -> currentState.contains(value) ? "#" : " ")
				.forEach(System.out::print);

		int sum = currentState.stream().mapToInt(x -> x).sum();
		System.out.printf("%6d\n", sum);
	}

	private static void part2() {
		BigDecimal generation = BigDecimal.valueOf(200L);
		BigDecimal numberAtGeneration = BigDecimal.valueOf(11891L);
		BigDecimal incrementPerGeneration = BigDecimal.valueOf(62L);
		BigDecimal totalGenerations = BigDecimal.valueOf(50_000_000_000L);

		System.out.println(numberAtGeneration.add(incrementPerGeneration.multiply(totalGenerations.subtract(generation))));
	}


}
