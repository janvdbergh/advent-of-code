package eu.janvdb.aoc2015.day24;

import eu.janvdb.aocutil.java.Sets;
import io.vavr.collection.List;
import io.vavr.collection.Traversable;

public class Puzzle {

	private static final List<Integer> WEIGHTS = List.of(
			1, 2, 3, 5, 7, 13, 17, 19, 23, 29, 31, 37, 41, 43, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113
	);
	private static final int NUMBER_OF_GROUPS = 4;

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(10000);

		new Puzzle().execute();
	}

	private void execute() {
		int allowedWeight = WEIGHTS.sum().intValue() / NUMBER_OF_GROUPS;

		int minItems = Sets.sets(WEIGHTS)
				.filter(list -> list.sum().equals(allowedWeight))
				.map(List::size)
				.min()
				.getOrElseThrow(() -> new RuntimeException("No sets found."));

		System.out.println(minItems + " items in each set.");

		long minProduct = Sets.sets(WEIGHTS)
				.filter(list -> list.size() == minItems)
				.filter(list -> list.sum().equals(allowedWeight))
				.map(Traversable::product)
				.min()
				.getOrElseThrow(() -> new RuntimeException("No sets found."))
				.longValue();

		System.out.println(minProduct + " is the minimal entanglement.");
	}

}
