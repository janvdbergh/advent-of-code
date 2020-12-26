package eu.janvdb.aoc2017.day22;

import eu.janvdb.aocutil.java.InputReader;
import io.vavr.collection.List;

public class Puzzle {

	private static final int NUMBER_OF_ITERATIONS = 10000000;

	public static void main(String[] args) {
		List<String> input = InputReader.readInput(Puzzle.class.getResource("input.txt")).toList();

		Grid grid = new Grid(input);
		VirusCarrier virusCarrier = new VirusCarrier(grid);

		for(int i = 0; i< NUMBER_OF_ITERATIONS; i++) {
			virusCarrier.performBurst();
		}

		System.out.println(virusCarrier);
		System.out.println(grid);
	}
}
