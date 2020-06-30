package eu.janvdb.aoc2018.day16;

import java.io.IOException;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;

import eu.janvdb.aoc2018.util.FileReader;

public class Part2 {

	private static final int NUMBER_OF_OPCODES = 16;

	public static void main(String[] args) throws IOException {
		RegisterFile registers = new RegisterFile();
		FileReader.readStringFile(Part2.class, "day16_part2.txt").stream()
				.map(Instruction::new)
				.forEach(instruction -> instruction.execute(registers));

		System.out.println(registers);
	}

	private static String nextNonEmptyLine(Queue<String> lines) {
		for (; ; ) {
			if (lines.isEmpty()) return null;
			String line = lines.remove();
			if (StringUtils.isNotBlank(line)) return line;
		}
	}
}

