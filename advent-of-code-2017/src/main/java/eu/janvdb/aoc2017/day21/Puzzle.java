package eu.janvdb.aoc2017.day21;

import eu.janvdb.aocutil.java.InputReader;
import io.vavr.collection.List;

public class Puzzle {

	private static final int NUMBER_OF_ITERATIONS = 18;

	public static void main(String[] args) {
		List<Rule> rules = InputReader.readInput(Puzzle.class.getResource("input.txt"))
				.map(Rule::new)
				.toList();

		Grid grid = new Grid();
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			grid = grid.update(rules);
			System.out.println("After iteration " + (i + 1));
			System.out.println(grid);
			System.out.println(grid.getBitMatrix().countPixels());
		}
	}

}
