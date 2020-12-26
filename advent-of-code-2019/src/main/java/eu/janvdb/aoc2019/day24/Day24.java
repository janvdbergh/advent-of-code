package eu.janvdb.aoc2019.day24;

import java.util.HashSet;
import java.util.Set;

public class Day24 {

	private static final String[] INPUT = {
			"###..",
			".##..",
			"#....",
			"##..#",
			".###."
	};
	private static final String[] INPUT_TEST = {
			"....#",
			"#..#.",
			"#..##",
			"..#..",
			"#...."
	};

	public static void main(String[] args) {
		new Day24().run();
	}

	private void run() {
		part1();
	}

	private void part1() {
		Grid grid = new Grid(INPUT);
		grid.print();

		Set<Grid> patterns = new HashSet<>();
		Grid current = grid;
		while (!patterns.contains(current)) {
			patterns.add(current);
			current = current.calculateNext();

			current.print();
		}

		System.out.println(current.calculateDiversity());
	}

}
