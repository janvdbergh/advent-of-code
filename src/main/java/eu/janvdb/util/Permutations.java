package eu.janvdb.util;

import javaslang.collection.HashSet;
import javaslang.collection.List;
import javaslang.collection.Set;
import javaslang.collection.Traversable;

public class Permutations {

	public static <T> Set<List<T>> getAllPermutations(Traversable<T> values) {
		return getPermutations(values, HashSet.empty(), List.empty());
	}

	private static <T> Set<List<T>> getPermutations(Traversable<T> values, Set<List<T>> result, List<T> currentItems) {
		if (currentItems.size() == values.size()) {
			return result.add(currentItems);
		} else {
			return result.addAll(values.toStream()
					.filter(value -> !currentItems.contains(value))
					.map(currentItems::append)
					.flatMap(newItems -> getPermutations(values, result, newItems))
			);
		}
	}

}
