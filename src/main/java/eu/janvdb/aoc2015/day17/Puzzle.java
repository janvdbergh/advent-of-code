package eu.janvdb.aoc2015.day17;


import eu.janvdb.util.Sets;
import javaslang.collection.List;
import javaslang.collection.Traversable;

public class Puzzle {

	private static final Integer[] ITEMS = {11, 30, 47, 31, 32, 36, 3, 1, 5, 3, 32, 36, 15, 11, 46, 26, 28, 1, 19, 3};

	public static void main(String[] args) {
		List<Integer> items = List.of(ITEMS);

		int minSize = Sets.sets(items)
				.filter(list -> list.sum().equals(150))
				.map(Traversable::size)
				.min()
				.getOrElseThrow(() -> new RuntimeException("No solution found"));

		System.out.println(minSize);

		long count = Sets.sets(items)
				.filter(list -> list.size()==4)
				.count(list -> list.sum().equals(150));

		System.out.println(count);
	}

}
