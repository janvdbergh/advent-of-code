package eu.janvdb.aocutil.java;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.collection.Traversable;

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
