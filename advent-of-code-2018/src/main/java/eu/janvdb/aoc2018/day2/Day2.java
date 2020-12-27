package eu.janvdb.aoc2018.day2;

import eu.janvdb.aoc2018.util.FileReader;
import eu.janvdb.aocutil.java.Pair;

import java.io.IOException;
import java.util.List;

public class Day2 {

	public static void main(String[] args) throws IOException {
		List<String> lines = FileReader.readStringFile(Day2.class, "day2.txt");
		part1(lines);
		part2(lines);
	}

	private static void part1(List<String> lines) {
		long numberOfDoubles = lines.stream()
				.filter(s -> hasRepeatedChar(s, 2))
				.count();
		long numberOfTriples = lines.stream()
				.filter(s -> hasRepeatedChar(s, 3))
				.count();
		System.out.println(numberOfDoubles * numberOfTriples);
	}

	private static boolean hasRepeatedChar(String s, int repetition) {
		return s.chars()
				.map(ch -> countChar(s, ch))
				.anyMatch(count -> count == repetition);
	}

	private static int countChar(String s, int ch) {
		return (int) s.chars()
				.filter(c -> ch == c)
				.count();
	}

	private static void part2(List<String> lines) {
		Pair<String, String> matchingPair = lines.stream()
				.flatMap(line1 -> lines.stream().map(line2 -> new Pair<>(line1, line2)))
				.filter(pair -> differsInOneChar(pair._1(), pair._2()))
				.findAny()
				.orElseThrow();

		System.out.println(sameChars(matchingPair._1(), matchingPair._2()));
	}

	private static boolean differsInOneChar(String s1, String s2) {
		if (s1.length() != s2.length()) return false;

		int differenceCount = 0;
		for(int i=0; i<s1.length(); i++) {
			if (s1.charAt(i) != s2.charAt(i)) differenceCount++;
		}

		return differenceCount == 1;
	}

	private static String sameChars(String s1, String s2) {
		if (s1.length() != s2.length()) return "";

		StringBuilder result = new StringBuilder();
		for(int i=0; i<s1.length(); i++) {
			if (s1.charAt(i) == s2.charAt(i)) result.append(s1.charAt(i));
		}

		return result.toString();
	}
}
