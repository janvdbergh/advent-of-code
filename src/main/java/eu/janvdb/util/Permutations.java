package eu.janvdb.util;

import javaslang.collection.*;

public class Permutations {

	public static <T> Stream<List<T>> getAllPermutations(Traversable<T> values) {
		return getPermutations(values, HashSet.empty(), List.empty());
	}

	private static <T> Stream<List<T>> getPermutations(Traversable<T> values, Set<List<T>> result, List<T> currentItems) {
		if (currentItems.size() == values.size()) {
			return Stream.of(currentItems);
		} else {
			return values.toStream()
					.filter(value -> !currentItems.contains(value))
					.flatMap(newItem -> getPermutations(values, result, currentItems.append(newItem)));
		}
	}

}
