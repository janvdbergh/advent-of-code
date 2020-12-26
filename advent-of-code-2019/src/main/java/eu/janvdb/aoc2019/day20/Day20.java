package eu.janvdb.aoc2019.day20;

import eu.janvdb.aocutil.java.InputReader;
import io.vavr.collection.Stream;

public class Day20 {

	public static void main(String[] args) {
		new Day20().run();
	}

	private void run() {
		Stream<String> input = InputReader.readInput(Day20.class.getResource("input.txt"));
		MazeWithBypasses maze = new MazeWithBypasses(input.toList());

		maze.print(System.out);
		System.out.println(maze.shortestPathUsingBypasses());
	}
}
