package eu.janvdb.aoc2018.day16;

import eu.janvdb.aoc2018.util.FileReader;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Part1 {

	private static final int NUMBER_OF_OPCODES = 16;

	public static void main(String[] args) throws IOException {
		// < 751
		Queue<String> lines = new LinkedList<>(FileReader.readStringFile(Part1.class, "day16_part1.txt"));

		int superCount = 0;
		for (; ; ) {
			String registersInStr = nextNonEmptyLine(lines);
			String instructionStr = nextNonEmptyLine(lines);
			String registersOutStr = nextNonEmptyLine(lines);
			if (registersInStr == null || instructionStr == null || registersOutStr == null) break;

			RegisterFile registersOut = new RegisterFile(registersOutStr);
			Instruction instruction = new Instruction(instructionStr);

			int count = 0;
			for (int i = 0; i < NUMBER_OF_OPCODES; i++) {
				RegisterFile registersIn = new RegisterFile(registersInStr);
				instruction.withOpcode(i).execute(registersIn);
				if (registersIn.equals(registersOut)) count++;
			}

			if (count == 0) throw new IllegalStateException();
			if (count >= 3) superCount++;
		}

		System.out.println(superCount);
	}

	private static String nextNonEmptyLine(Queue<String> lines) {
		for (; ; ) {
			if (lines.isEmpty()) return null;
			String line = lines.remove();
			if (StringUtils.isNotBlank(line)) return line;
		}
	}
}

