package eu.janvdb.aoc2015.day15;

import eu.janvdb.aocutil.java.Recipes;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final int TOTAL = 100;
	private static final int CALORIES = 500;

	private static final String[] INPUT0 = {
			"Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8",
			"Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"
	};

	private static final String[] INPUT = {
			"Sprinkles: capacity 2, durability 0, flavor -2, texture 0, calories 3",
			"Butterscotch: capacity 0, durability 5, flavor -3, texture 0, calories 3",
			"Chocolate: capacity 0, durability 0, flavor 5, texture -1, calories 8",
			"Candy: capacity 0, durability -1, flavor 0, texture 5, calories 8"
	};

	public static void main(String[] args) {
		List<Ingredient> ingredients = Stream.of(INPUT)
				.map(Ingredient::new)
				.toList();

		Stream<Map<Ingredient, Integer>> combinations = Recipes.getRecipes(ingredients, TOTAL);

		Option<Integer> max = combinations
				.map(Recipe::new)
				.filter(recipe -> recipe.getCalories()==CALORIES)
				.map(Recipe::getCalories)
				.max();

		System.out.println(max);
	}

	static class Ingredient {

		private static final Pattern PATTERN = Pattern.compile(
				"(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)"
		);

		private final String name;
		private final int capacity, durability, flavor, texture, calories;

		Ingredient(String description) {
			Matcher matcher = PATTERN.matcher(description);
			if (!matcher.matches()) {
				throw new IllegalArgumentException(description);
			}

			name = matcher.group(1);
			capacity = Integer.parseInt(matcher.group(2));
			durability = Integer.parseInt(matcher.group(3));
			flavor = Integer.parseInt(matcher.group(4));
			texture = Integer.parseInt(matcher.group(5));
			calories = Integer.parseInt(matcher.group(6));
		}

		@Override
		public String toString() {
			return name;
		}

		public String getName() {
			return name;
		}

		int getCapacity() {
			return capacity;
		}

		int getDurability() {
			return durability;
		}

		int getFlavor() {
			return flavor;
		}

		int getTexture() {
			return texture;
		}

		int getCalories() {
			return calories;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Ingredient that = (Ingredient) o;
			return name.equals(that.name);
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}
	}

	static class Recipe {

		private final Map<Ingredient, Integer> amounts;

		Recipe(Map<Ingredient, Integer> amounts) {
			this.amounts = amounts;
		}

		public int getScore() {
			return sumProductWithMin0(Ingredient::getCapacity)
					* sumProductWithMin0(Ingredient::getDurability)
					* sumProductWithMin0(Ingredient::getFlavor)
					* sumProductWithMin0(Ingredient::getTexture);
		}

		int getCalories() {
			return sumProductWithMin0(Ingredient::getCalories);
		}

		private int sumProductWithMin0(Function<Ingredient, Integer> extractor) {
			return amounts.toStream()
					.map(entry -> extractor.apply(entry._1) * entry._2)
					.sum()
					.intValue();
		}
	}
}
