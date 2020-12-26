package eu.janvdb.aoc2018.day16;

import eu.janvdb.aoc2018.util.FileReader;

import java.io.IOException;

public class Part2 {

	public static void main(String[] args) throws IOException {
		RegisterFile registers = new RegisterFile();
		FileReader.readStringFile(Part2.class, "day16_part2.txt").stream()
				.map(Instruction::new)
				.forEach(instruction -> instruction.execute(registers));

		System.out.println(registers);
	}

}

