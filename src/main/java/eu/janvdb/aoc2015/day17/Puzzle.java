package eu.janvdb.aoc2015.day17;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import eu.janvdb.util.Sets;

public class Puzzle {

	private static final int[] ITEMS = {11, 30, 47, 31, 32, 36, 3, 1, 5, 3, 32, 36, 15, 11, 46, 26, 28, 1, 19, 3};

	public static void main(String[] args) {
		List<Integer> items = Arrays.stream(ITEMS)
				.mapToObj(Integer::new)
				.collect(Collectors.toList());

		int minSize = Sets.sets(items)
				.filter(list -> sum(list) == 150)
				.mapToInt(List::size)
				.min().getAsInt();

		System.out.println(minSize);

		long count = Sets.sets(items)
				.filter(list -> list.size()==4)
				.filter(list -> sum(list) == 150)
				.count();

		System.out.println(count);
	}

	private static int sum(List<Integer> values) {
		return values.stream().mapToInt(i -> i).sum();
	}
}
