package eu.janvdb.aoc2015.day12;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONObject) {
				removeRed((JSONObject) value);
			}
			if (value instanceof JSONArray) {
				removeRed((JSONArray) value);
			}
		}
	}

	private static void removeRed(JSONObject object) {
		List<String> keys = getKeys(object);
		for (String key : keys) {
			Object value = object.get(key);
			if ("red".equals(value)) {
				for (String key2 : keys) {
					object.remove(key2);
				}
				break;
			}
			if (value instanceof JSONObject) {
				removeRed((JSONObject) value);
			}
			if (value instanceof JSONArray) {
				removeRed((JSONArray) value);
			}
		}
	}

	private static int getSum(JSONArray array) {
		int sum = 0;
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONObject) {
				sum += getSum((JSONObject) value);
			}
			if (value instanceof JSONArray) {
				sum += getSum((JSONArray) value);
			}
			if (value instanceof Integer) {
				sum += (Integer) value;
			}
		}

		return sum;
	}

	private static int getSum(JSONObject object) {
		int sum = 0;
		List<String> keys = getKeys(object);
		for (String key : keys) {
			Object value = object.get(key);
			if (value instanceof Integer) {
				sum += (Integer) value;
			}
			if (value instanceof JSONObject) {
				sum += getSum((JSONObject) value);
			}
			if (value instanceof JSONArray) {
				sum += getSum((JSONArray) value);
			}
		}

		return sum;
	}

	private static List<String> getKeys(JSONObject object) {
		Set<?> keySet = object.keySet();
		return keySet.stream().map(String.class::cast).collect(Collectors.toList());
	}
}
