package eu.janvdb.aoc2017.day7;

import eu.janvdb.util.InputReader;
import javaslang.Tuple;
import javaslang.collection.HashSet;
import javaslang.collection.Map;
import javaslang.collection.Set;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final Pattern PATTERN = Pattern.compile("(\\w+)\\W+\\((\\d+)\\)(\\W+->\\W+(.*))?");

	public static void main(String[] args) {
		Map<String, Program> programsByName = loadPrograms();

		Program rootProgram = getRootProgram(programsByName);
		System.out.println(rootProgram.getName());

		printUnbalancedRecursive(rootProgram);
	}

	private static Map<String, Program> loadPrograms() {
		Map<String, Program> programsByName = InputReader.readInput(Puzzle.class.getResource("input.txt"))
				.map(Puzzle::parseProgram)
				.toMap(program -> Tuple.of(program.getName(), program));
		programsByName.values().forEach(program -> program.mapPrograms(programsByName));
		return programsByName;
	}

	private static Program parseProgram(String line) {
		Matcher matcher = PATTERN.matcher(line);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(line);
		}

		String name = matcher.group(1);
		int weight= Integer.parseInt(matcher.group(2));
		Set<String> children;
		if (matcher.group(4) != null) {
			children = HashSet.of(matcher.group(4).split("\\s*,\\s*"));
		} else {
			children = HashSet.empty();
		}

		return new Program(name, weight, children);
	}

	private static Program getRootProgram(Map<String, Program> programsByName) {
		Program current = programsByName.values().get();
		while(current.getParent() != null) {
			current = current.getParent();
		}

		return current;
	}

	private static void printUnbalancedRecursive(Program program) {
		if (!program.isBalanced()) {
			printUnbalancedProgram(program);
			program.getChildren().forEach(Puzzle::printUnbalancedRecursive);
		}
	}

	private static void printUnbalancedProgram(Program program) {
		System.out.println("Unbalanced: " + program.getName());
		program.getChildren().forEach(
				child -> System.out.println(" - " + child.getName() + ": " + child.getWeight() + " - " + child.getTotalWeight())
		);
	}
}
