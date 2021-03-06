package eu.janvdb.aocutil.java;

import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

public class Recipes {

	public static <T> Stream<Map<T, Integer>> getRecipes(List<T> items, int total) {
		return getRecipes(items, total, LinkedHashMap.empty());
	}

	private static <T> Stream<Map<T, Integer>> getRecipes(List<T> items, int totalRemaining, Map<T, Integer> itemsSet) {
		if (itemsSet.size() == items.size()) {
			return Stream.of(itemsSet);
		} else {
			T item = items.get(itemsSet.size());

			Stream<Integer> range = itemsSet.size() == items.size() - 1
					? Stream.range(totalRemaining, totalRemaining + 1)
					: Stream.range(0, totalRemaining + 1);
			return range
					.flatMap(
							numberAdded -> getRecipes(items, totalRemaining - numberAdded, itemsSet.put(item, numberAdded))
					);
		}
	}
}
