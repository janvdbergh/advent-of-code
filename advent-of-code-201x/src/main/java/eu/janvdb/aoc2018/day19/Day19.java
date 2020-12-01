package eu.janvdb.aoc2018.day19;

import eu.janvdb.aoc2018.day16.RegisterFile;
import eu.janvdb.aoc2018.util.FileReader;

import java.io.IOException;

public class Day19 {

	public static void main(String[] args) throws IOException {
		Machine machine = new Machine(FileReader.readStringFile(Day19.class, "day19.txt"));

		part1(machine);
//		part2(machine);
	}

	private static void part1(Machine machine) {
		RegisterFile registers = new RegisterFile();
		machine.run(registers);
		System.out.println(registers);
	}

	private static void part2(Machine machine) {
		RegisterFile registers = new RegisterFile();
		registers.set(0, 1);
		machine.run(registers);
		System.out.println(registers);
	}
}
