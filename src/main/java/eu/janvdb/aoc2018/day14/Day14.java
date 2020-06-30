package eu.janvdb.aoc2018.day14;

public class Day14 {

	private static final int INPUT = 110201;

	private static final StringBuilder recipes = new StringBuilder("37");
	private static int elfIndex1 = 0, elfIndex2 = 1;

	public static void main(String[] args) {
		while (recipes.length() < INPUT + 10) {
			step();
		}
		System.out.println(recipes.substring(INPUT, INPUT + 10));

		String inputAsStr = String.valueOf(INPUT);
		while(!recipes.substring(recipes.length() - 15).contains(inputAsStr)) {
			step();
		}

		System.out.println(recipes.indexOf(inputAsStr));
	}

	private static void step() {
		int sum = recipeScore(elfIndex1) + recipeScore(elfIndex2);
		if (sum >= 10) {
			recipes.append((char) (sum / 10 + '0'));
		}
		recipes.append((char) (sum % 10 + '0'));

		elfIndex1 = (elfIndex1 + recipeScore(elfIndex1) + 1) % recipes.length();
		elfIndex2 = (elfIndex2 + recipeScore(elfIndex2) + 1) % recipes.length();
	}

	private static int recipeScore(int index) {
		return recipes.charAt(index) - '0';
	}

}
