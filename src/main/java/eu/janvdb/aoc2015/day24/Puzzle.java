package eu.janvdb.aoc2015.day24;

import java.util.Arrays;
import java.util.List;

import eu.janvdb.util.Sets;

public class Puzzle {

	private static final List<Integer> WEIGHTS = Arrays.asList(
			1, 2, 3, 5, 7, 13, 17, 19, 23, 29, 31, 37, 41, 43, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113
	);
	private static final int NUMBER_OF_GROUPS = 4;

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(10000);

		new Puzzle().execute();
	}

	private void execute() {
		int allowedWeight = sum(WEIGHTS) / NUMBER_OF_GROUPS;

		int minItems = Sets.sets(WEIGHTS)
				.filter(list -> sum(list) == allowedWeight)
				.mapToInt(List::size)
				.min()
				.orElseThrow(() -> new RuntimeException("No sets found."));

		System.out.println(minItems + " items in each set.");

		long minProduct = Sets.sets(WEIGHTS)
				.filter(list -> list.size() == minItems)
				.filter(list -> sum(list) == allowedWeight)
				.mapToLong(this::product)
				.min()
				.orElseThrow(() -> new RuntimeException("No sets found."));

		System.out.println(minProduct + " is the minimal entanglement.");
	}

	private int sum(List<Integer> items) {
		return items.stream()
				.mapToInt(i -> i)
				.sum();
	}

	private long product(List<Integer> items) {
		return items.stream()
				.mapToLong(i -> i)
				.reduce(1L, (a, b) -> a * b);
	}
}
