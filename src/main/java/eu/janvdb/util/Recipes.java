package eu.janvdb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Recipes {

	public static <T> List<Map<T, Integer>> getRecipes(List<T> items, int total) {
		List<Map<T, Integer>> result = new ArrayList<>();
		getRecipes(items, total, new LinkedHashMap<>(), result);

		return result;
	}

	private static <T> void getRecipes(List<T> items, int totalRemaining, Map<T, Integer> itemsSet, List<Map<T, Integer>> result) {
		if (itemsSet.size() == items.size()) {
			result.add(itemsSet);
		} else {
			T item = items.get(itemsSet.size());
			if (itemsSet.size() == items.size() - 1) {
				Map<T, Integer> newItemsSet = new LinkedHashMap<>(itemsSet);
				newItemsSet.put(item, totalRemaining);
				getRecipes(items, 0, newItemsSet, result);
			} else {
				IntStream.range(0, totalRemaining + 1).forEach(amount -> {
					Map<T, Integer> newItemsSet = new HashMap<>(itemsSet);
					newItemsSet.put(item, amount);
					getRecipes(items, totalRemaining - amount, newItemsSet, result);
				});
			}
		}
	}

}
