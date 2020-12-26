package eu.janvdb.aoc2017.day16;

import eu.janvdb.aocutil.java.InputReader;
import io.vavr.collection.List;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final Pattern SPIN = Pattern.compile("s(\\d+)");
	private static final Pattern EXCHANGE = Pattern.compile("x(\\d+)/(\\d+)");
	private static final Pattern PARTNER = Pattern.compile("p([a-z])/([a-z])");
	private static final int REPETITIONS = 1_000_000_000;

	public static void main(String[] args) {
		String[] inputs = InputReader.readInputFully(Puzzle.class.getResource("input.txt")).split("\\s*,\\s*");
		List<Function<ProgramSet, ProgramSet>> instructions = List.of(inputs).map(Puzzle::parseInstruction);

		part1(instructions);
		part2(instructions);
	}

	private static Function<ProgramSet, ProgramSet> parseInstruction(String input) {
		Matcher spinMatcher = SPIN.matcher(input);
		if (spinMatcher.matches()) {
			return programSet -> programSet.spin(Integer.parseInt(spinMatcher.group(1)));
		}

		Matcher exchangeMatcher = EXCHANGE.matcher(input);
		if (exchangeMatcher.matches()) {
			return programSet -> programSet.exchange(Integer.parseInt(exchangeMatcher.group(1)), Integer.parseInt(exchangeMatcher.group(2)));
		}

		Matcher partnerMatcher = PARTNER.matcher(input);
		if (partnerMatcher.matches()) {
			return programSet -> programSet.partner(partnerMatcher.group(1).charAt(0), partnerMatcher.group(2).charAt(0));
		}

		throw new IllegalArgumentException(input);
	}

	private static void part1(List<Function<ProgramSet, ProgramSet>> instructions) {
		ProgramSet programSet = new ProgramSet(16);
		programSet = executeInstructions(instructions, programSet);
		System.out.println(programSet.getProgramList());
	}

	private static void part2(List<Function<ProgramSet, ProgramSet>> instructions) {
		ProgramSet programSet = new ProgramSet(16);
		List<String> foundPatterns = List.of(programSet.getProgramList());

		while(true) {
			programSet = executeInstructions(instructions, programSet);

			String programList = programSet.getProgramList();
			if (foundPatterns.contains(programList)) break;
			foundPatterns = foundPatterns.append(programList);
		}

		System.out.println(foundPatterns.get(REPETITIONS % foundPatterns.size()));
	}

	private static ProgramSet executeInstructions(List<Function<ProgramSet, ProgramSet>> instructions, ProgramSet programSet) {
		for (Function<ProgramSet, ProgramSet> instruction : instructions) {
			programSet = instruction.apply(programSet);
		}
		return programSet;
	}
}
