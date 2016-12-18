package eu.janvdb.util;

import javaslang.collection.HashSet;
import javaslang.collection.List;
import javaslang.collection.Set;
import javaslang.collection.Stream;
import javaslang.collection.Traversable;

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
