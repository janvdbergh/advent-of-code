package eu.janvdb.aoc2016.day8;

import java.util.regex.Pattern;

public class Command {

	private final Instruction instruction;
	private final int arg1, arg2;

	public Command(Instruction instruction, int arg1, int arg2) {
		this.instruction = instruction;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public int getArg1() {
		return arg1;
	}

	public int getArg2() {
		return arg2;
	}

	@Override
	public String toString() {
		return instruction.name() + " " + arg1 + " " + arg2;
	}

	public enum Instruction {
		RECT("rect (\\d+)x(\\d+)"),
		ROTATE_COLUMN("rotate column x=(\\d+) by (\\d+)"),
		ROTATE_ROW("rotate row y=(\\d+) by (\\d+)");

		private final Pattern pattern;

		Instruction(String pattern) {
			this.pattern = Pattern.compile(pattern);
		}

		public Pattern getPattern() {
			return pattern;
		}
	}
}
