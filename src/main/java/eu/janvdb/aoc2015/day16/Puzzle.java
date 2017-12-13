package eu.janvdb.aoc2015.day16;

import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	public static void main(String[] args) {
		List<Sue> sues = Stream.of(Input.INPUT)
				.map(Sue::new)
				.toList();

		sues.toStream()
				.filter(sue -> sue.matches("children", x -> x == 3))
				.filter(sue -> sue.matches("cats", x -> x > 7))
				.filter(sue -> sue.matches("samoyeds", x -> x == 2))
				.filter(sue -> sue.matches("pomeranians", x -> x < 3))
				.filter(sue -> sue.matches("akitas", x -> x == 0))
				.filter(sue -> sue.matches("vizslas", x -> x == 0))
				.filter(sue -> sue.matches("goldfish", x -> x < 5))
				.filter(sue -> sue.matches("trees", x -> x > 3))
				.filter(sue -> sue.matches("cars", x -> x == 2))
				.filter(sue -> sue.matches("perfumes", x -> x == 1))
				.forEach(System.out::println);
	}

	private static class Sue {
		private static final Pattern SUE_INDEX_PATTERN = Pattern.compile("Sue (\\d+):");
		private static final Pattern SUE_PROPERTY_PATTERN = Pattern.compile("(\\w+): (\\d+)");

		private int index;
		private Map<String, Integer> properties = HashMap.empty();

		Sue(String description) {
			Matcher matcher1 = SUE_INDEX_PATTERN.matcher(description);
			if (!matcher1.find()) {
				throw new IllegalArgumentException(description);
			}
			index = Integer.parseInt(matcher1.group(1));

			Matcher matcher2 = SUE_PROPERTY_PATTERN.matcher(description);
			while (matcher2.find()) {
				properties = properties.put(
						matcher2.group(1),
						Integer.parseInt(matcher2.group(2))
				);
			}
		}

		boolean matches(String name, Function1<Integer, Boolean> evaluator) {
			return properties.get(name)
					.map(evaluator)
					.getOrElse(true);
		}

		@Override
		public String toString() {
			return "Sue{" +
					"index=" + index +
					", properties=" + properties +
					'}';
		}
	}
}
