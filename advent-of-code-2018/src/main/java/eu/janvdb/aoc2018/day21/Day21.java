package eu.janvdb.aoc2018.day21;

import eu.janvdb.aoc2018.day16.RegisterFile;
import eu.janvdb.aoc2018.day19.Machine;
import eu.janvdb.aocutil.java.FileReader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Day21 {

	public static void main(String[] args) throws IOException {
		Machine machine = new Machine(FileReader.readStringFile(Day21.class, "day21.txt"));
		machine.addBreakpoint(29); // jump before exit

		RegisterFile registerFile = new RegisterFile();

		long lastValueAdded = -1;
		Set<Long> values = new HashSet<>();
		while(true) {
			machine.run(registerFile);
			long value = registerFile.get(4);
			if (values.contains(value)) break;
			values.add(value);
			lastValueAdded = value;

			machine.step(registerFile);
		}

		System.out.println(lastValueAdded);
	}
}
