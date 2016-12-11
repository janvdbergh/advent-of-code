package eu.janvdb.aoc2016.day8;

import java.util.regex.Matcher;

public class Parser {

	public Command parse(String line) {
		for (Command.Instruction instruction : Command.Instruction.values()) {
			Matcher matcher = instruction.getPattern().matcher(line);
			if (matcher.matches()) {
				return new Command(instruction, Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
			}
		}

		throw new IllegalArgumentException(line);
	}
}
