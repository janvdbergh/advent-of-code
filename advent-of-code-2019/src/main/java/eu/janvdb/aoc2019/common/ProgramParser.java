package eu.janvdb.aoc2019.common;

import eu.janvdb.aocutil.java.InputReader;

import java.net.URL;

public class ProgramParser {

	public static long[] parseProgram(URL url) {
		String input = InputReader.readInputFully(url).trim();
		String[] parts = input.split("\\s*,\\s*");
		long[] program = new long[parts.length];
		for(int i=0;i<parts.length; i++) {
			program[i] = Long.parseLong(parts[i]);
		}

		return program;
	}
}
