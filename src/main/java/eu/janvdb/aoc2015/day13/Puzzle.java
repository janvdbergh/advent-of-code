package eu.janvdb.aoc2015.day13;

import eu.janvdb.util.Matrix;
import eu.janvdb.util.Permutations;
import javaslang.collection.HashSet;
import javaslang.collection.List;
import javaslang.collection.Set;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final Pattern PATTERN = Pattern.compile(
			"(\\w+) would (gain|lose) (\\d+) happiness units by sitting next to (\\w+)."
	);

	private static Matrix<String, Integer> happinessMatrix = new Matrix<>((x, y) -> 0);

	public static void main(String[] args) {
		Arrays.stream(Input.INPUT)
				.forEach(Puzzle::parseLine);

		Set<String> persons = HashSet.ofAll(happinessMatrix.keys());
		persons.add("ME");
		int totalHappiness = Permutations.getAllPermutations(persons)
				.map(Puzzle::getTotalHappiness)
				.max().getOrElseThrow(IllegalArgumentException::new);

		System.out.println(totalHappiness);
	}

	private static void parseLine(String line) {
		Matcher matcher = PATTERN.matcher(line);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(line);
		}

		String name1 = matcher.group(1);
		String name2 = matcher.group(4);
		boolean gainOrLose = matcher.group(2).equals("gain");
		int points = Integer.parseInt(matcher.group(3));

		happinessMatrix = happinessMatrix.set(name1, name2, gainOrLose ? points : -points);
	}

	private static int getTotalHappiness(List<String> names) {
		int totalHappiness = 0;
		for(int i=0; i<names.size(); i++) {
			String name1 = names.get(i % names.size());
			String name2 = names.get((i+1) % names.size());
			totalHappiness += happinessMatrix.get(name1, name2) + happinessMatrix.get(name2, name1);
		}

		return totalHappiness;
	}

}
