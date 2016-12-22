package eu.janvdb.aoc2016.day21;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janvdb.util.InputReader;
import eu.janvdb.util.Permutations;
import javaslang.Function1;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;

public class Puzzle {

	private static final Map<Pattern, Function1<Matcher, Function1<String, String>>> INSTRUCTIONS = HashMap.ofEntries(
			Tuple.of(
					Pattern.compile("swap position (\\d+) with position (\\d+)"),
					Puzzle::swapPositions
			),
			Tuple.of(
					Pattern.compile("swap letter (\\w) with letter (\\w)"),
					Puzzle::swapLetters
			),
			Tuple.of(
					Pattern.compile("rotate left (\\d+) steps?"),
					Puzzle::rotateLeft
			),
			Tuple.of(
					Pattern.compile("rotate right (\\d+) steps?"),
					Puzzle::rotateRight
			),
			Tuple.of(
					Pattern.compile("rotate based on position of letter (\\w)"),
					Puzzle::rotateBasedOnPositionOfLetter
			),
			Tuple.of(
					Pattern.compile("reverse positions (\\d+) through (\\d+)"),
					Puzzle::reversePositions
			),
			Tuple.of(
					Pattern.compile("move position (\\d+) to position (\\d+)"),
					Puzzle::movePositionTo
			)
	);

	private static Function1<String, String> swapPositions(Matcher matcher) {
		int position1 = Integer.parseInt(matcher.group(1));
		int position2 = Integer.parseInt(matcher.group(2));
		return input -> swapPositions(input, position1, position2);
	}

	private static Function1<String, String> swapLetters(Matcher matcher) {
		char letter1 = matcher.group(1).charAt(0);
		char letter2 = matcher.group(2).charAt(0);
		return input -> {
			int position1 = input.indexOf(letter1);
			int position2 = input.indexOf(letter2);

			return swapPositions(input, position1, position2);
		};
	}

	private static Function1<String, String> rotateLeft(Matcher matcher) {
		int numberOfPlaces = Integer.parseInt(matcher.group(1));
		return input -> {
			int rotation = numberOfPlaces % input.length();
			String temp = input;
			for (int i = 0; i < rotation; i++) {
				temp = temp.substring(1) + temp.charAt(0);
			}
			return temp;
		};
	}

	private static Function1<String, String> rotateRight(Matcher matcher) {
		int numberOfPlaces = Integer.parseInt(matcher.group(1));
		return input -> rotateRight(input, numberOfPlaces);
	}

	private static Function1<String, String> rotateBasedOnPositionOfLetter(Matcher matcher) {
		char letter = matcher.group(1).charAt(0);
		return input -> {
			int position = input.indexOf(letter);
			int numberOfRotations = 1 + position + (position >= 4 ? 1 : 0);
			return rotateRight(input, numberOfRotations);
		};
	}

	private static Function1<String, String> reversePositions(Matcher matcher) {
		int position1 = Integer.parseInt(matcher.group(1));
		int position2 = Integer.parseInt(matcher.group(2));
		return input -> input.substring(0, position1) + reverse(input.substring(position1, position2 + 1)) + input.substring(position2 + 1);
	}

	private static Function1<String, String> movePositionTo(Matcher matcher) {
		int position1 = Integer.parseInt(matcher.group(1));
		int position2 = Integer.parseInt(matcher.group(2));
		return input -> {
			char ch = input.charAt(position1);
			String temp = input.substring(0, position1) + input.substring(position1 + 1);
			return temp.substring(0, position2) + ch + temp.substring(position2);
		};
	}

	private static String swapPositions(String input, int position1, int position2) {
		char[] chars = input.toCharArray();
		char temp = chars[position1];
		chars[position1] = chars[position2];
		chars[position2] = temp;
		return new String(chars);
	}

	private static String rotateRight(String input, int numberOfPlaces) {
		int rotation = numberOfPlaces % input.length();
		String temp = input;
		for (int i = 0; i < rotation; i++) {
			temp = temp.charAt(temp.length() - 1) + temp.substring(0, temp.length() - 1);
		}
		return temp;
	}

	private static String reverse(String input) {
		StringBuilder result = new StringBuilder(input.length());
		for (int i = input.length() - 1; i >= 0; i--) {
			result.append(input.charAt(i));
		}
		return result.toString();
	}

	public static void main(String[] args) {
		new Puzzle().execute2();
	}

	private void execute1() {
		List<Function1<String, String>> instructions = readInstructions();

		String current = "abcdefgh";
		current = processInstructions(instructions, current);
		System.out.println(current);
	}

	private void execute2() {
		List<Function1<String, String>> instructions = readInstructions();

		Map<String, String> rainbowTable = Permutations.getAllPermutations(List.of("a", "b", "c", "d", "e", "f", "g", "h"))
				.map(strings -> strings.reduce((s1, s2) -> s1 + s2))
				.toMap(s -> Tuple.of(this.processInstructions(instructions, s), s));

		String result = rainbowTable.get("fbgdceah").getOrElseThrow(IllegalStateException::new);
		System.out.println(result);
		System.out.println(processInstructions(instructions, result));
	}

	private String processInstructions(List<Function1<String, String>> instructions, String input) {
		for (Function1<String, String> instruction : instructions) {
			input = instruction.apply(input);
		}
		return input;
	}

	private List<Function1<String, String>> readInstructions() {
		return InputReader.readInput(getClass().getResource("input1.txt"))
				.map(this::parse)
				.toList();
	}

	private Function1<String, String> parse(String line) {
		for (Tuple2<Pattern, Function1<Matcher, Function1<String, String>>> instruction : INSTRUCTIONS) {
			Matcher matcher = instruction._1.matcher(line);
			if (matcher.matches()) {
				return instruction._2.apply(matcher);
			}
		}
		throw new IllegalArgumentException(line);
	}


}
