package eu.janvdb.aoc2018.day5;

import eu.janvdb.aoc2018.util.FileReader;

import java.io.IOException;
import java.util.OptionalInt;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day5 {

	public static void main(String[] args) throws IOException {
		Pattern pattern = createReducePattern();

		String input = FileReader.readStringFile(Day5.class, "day5.txt").get(0);
		String reduced = reduceString(input, pattern);
		System.out.println(reduced.length());

		OptionalInt min = IntStream.range('a', 'z' + 1)
				.map(ch -> {
					String regex = "" + (char)ch + '|' + (char) Character.toUpperCase(ch);
					String newInput = input.replaceAll(regex, "");
					return reduceString(newInput, pattern).length();
				})
				.min();

		System.out.println(min);
	}

	private static String reduceString(String current, Pattern pattern) {
		while (true) {
			String next = pattern.matcher(current).replaceAll("");
			if (next.length() == current.length()) break;
			current = next;
			System.out.println(next.length());
		}
		return current;
	}

	private static Pattern createReducePattern() {
		StringBuilder patternBuilder = new StringBuilder();
		IntStream.range('a', 'z' + 1)
				.forEach(ch -> {
					patternBuilder.append('|');
					patternBuilder.append(createUnitRegExp(ch));
				});

		String patternStr = patternBuilder.substring(1);
		return Pattern.compile(patternStr);
	}

	private static String createUnitRegExp(int ch) {
		return "" + (char) ch + (char) Character.toUpperCase(ch) + '|' + (char) Character.toUpperCase(ch) + (char) ch;
	}
}
