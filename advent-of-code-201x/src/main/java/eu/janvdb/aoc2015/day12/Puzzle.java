package eu.janvdb.aoc2015.day12;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import org.json.JSONArray;
import org.json.JSONObject;

public class Puzzle {

	public static void main(String[] args) {
		JSONArray array = new JSONArray(Input.INPUT);
		removeRed(array);
		int sum = getSum(array);

		System.out.println(sum);
	}

	private static void removeRed(JSONArray array) {
		Stream.range(0, array.length())
				.map(array::get)
				.forEach((value) -> {
					if (value instanceof JSONObject) {
						removeRed((JSONObject) value);
					}
					if (value instanceof JSONArray) {
						removeRed((JSONArray) value);
					}
				});
	}

	private static void removeRed(JSONObject object) {
		Set<String> keys = getKeys(object);
		keys.map(object::get)
				.forEach(value -> {
					if ("red".equals(value)) {
						for (String key2 : keys) {
							object.remove(key2);
						}
					}
					if (value instanceof JSONObject) {
						removeRed((JSONObject) value);
					}
					if (value instanceof JSONArray) {
						removeRed((JSONArray) value);
					}
				});
	}

	private static int getSum(JSONArray array) {
		return Stream.range(0, array.length())
				.map(array::get)
				.map(Puzzle::getSum)
				.sum()
				.intValue();
	}

	private static int getSum(JSONObject object) {
		return getKeys(object).toStream()
				.map(key -> getSum(object.get(key)))
				.sum()
				.intValue();
	}

	private static Integer getSum(Object value) {
		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof JSONObject) {
			return getSum((JSONObject) value);
		}
		if (value instanceof JSONArray) {
			return getSum((JSONArray) value);
		}
		return 0;
	}

	private static Set<String> getKeys(JSONObject object) {
		Set<?> keySet = HashSet.ofAll(object.keySet());
		return keySet.map(String.class::cast);
	}
}
