package eu.janvdb.aoc2019.day24;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day24 {

	private static final List<String> INPUT_TEST = List.of(
			"....#",
			"#..#.",
			"#..##",
			"..#..",
			"#...."
	);

	private static final List<String> INPUT = List.of(
			"###..",
			".##..",
			"#....",
			"##..#",
			".###."
	);

	public static void main(String[] args) {
		part1(INPUT);
		part2(INPUT, 200);
	}

	private static void part1(List<String> input) {
		BugPlanet bugPlanet = BugPlanet.parse(input, false);

		Set<BugPlanet> bugPlanets = new HashSet<>();
		while (!bugPlanets.contains(bugPlanet)) {
			bugPlanets.add(bugPlanet);
			bugPlanet = bugPlanet.step();
		}

		System.out.println(bugPlanet);
		System.out.println(bugPlanets.size());
		System.out.println(bugPlanet.getNumberOfBugs());
		System.out.println(bugPlanet.biodiversityRating());
	}

	private static void part2(List<String> input, int numberOfSteps) {
		BugPlanet bugPlanet = BugPlanet.parse(input, true);

		for(int i=0;i<numberOfSteps; i++) {
			bugPlanet = bugPlanet.step();
		}

		System.out.println(bugPlanet);
		System.out.println(bugPlanet.getNumberOfBugs());
	}
}
