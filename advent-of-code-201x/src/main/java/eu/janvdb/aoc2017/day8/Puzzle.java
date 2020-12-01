package eu.janvdb.aoc2017.day8;

import eu.janvdb.util.InputReader;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.TreeMap;

public class Puzzle {

	public static void main(String[] args) {
		List<Instruction> instructions = InputReader.readInput(Puzzle.class.getResource("input.txt"))
				.map(Instruction::new)
				.toList();

		Map<String, Integer> registers = TreeMap.empty();
		int currentMax = 0;
		for (Instruction instruction : instructions) {
			registers = instruction.execute(registers);
			int registerMax = registers.values().max().getOrElse(0);
			if (registerMax > currentMax) {
				currentMax = registerMax;
			}
		}

		System.out.println(registers.values().max());
		System.out.println(currentMax);
	}
}
