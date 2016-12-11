package eu.janvdb.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Permutations {

	public static <T> Set<List<T>> getAllPermutations(Set<T> values) {
		Set<List<T>> result = new HashSet<>();
		getPermutations(result, values, new ArrayList<>());

		return result;
	}

	private static <T> void getPermutations(Set<List<T>> result, Set<T> values, List<T> currentItems) {
		if (currentItems.size() == values.size()) {
			result.add(currentItems);
		} else {
			values.stream()
					.filter(value -> !currentItems.contains(value))
					.forEach(value -> {
						List<T> newItems = new ArrayList<>(currentItems);
						newItems.add(value);
						getPermutations(result, values, newItems);
					});
		}
	}

}
