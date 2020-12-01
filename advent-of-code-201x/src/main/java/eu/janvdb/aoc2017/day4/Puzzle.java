package eu.janvdb.aoc2017.day4;

import eu.janvdb.util.InputReader;
import io.vavr.collection.List;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class Puzzle {

	public static void main(String[] args) {
		System.out.println(part1());
		System.out.println(part2());
	}

	private static int part1() {
		return InputReader.readInput(Puzzle.class.getResource("input.txt"))
				.count(line -> isValidPassphraseUsingAnagrams(line, s -> s));
	}

	private static int part2() {
		return InputReader.readInput(Puzzle.class.getResource("input.txt"))
				.count(line -> isValidPassphraseUsingAnagrams(line, Puzzle::sortWord));
	}

	private static boolean isValidPassphraseUsingAnagrams(String line, Function<String, String> transformation) {
		return List.of(line.split("\\s+")).toStream()
				.map(transformation)
				.combinations(2)
				.forAll(words -> !Objects.equals(words.get(0), words.get(1)));
	}

	private static String sortWord(String word) {
		char[] chars = word.toCharArray();
		Arrays.sort(chars);
		return new String(chars);
	}

}
