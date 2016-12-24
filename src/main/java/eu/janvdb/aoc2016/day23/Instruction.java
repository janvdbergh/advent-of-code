package eu.janvdb.aoc2016.day23;

import java.util.regex.Matcher;

public abstract class Instruction {

	protected final Matcher matcher;

	protected Instruction(Matcher matcher) {
		this.matcher = matcher;
	}

	public abstract State execute(State state);

	public abstract Instruction toggle();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName().toUpperCase()).append(" ");

		for(int i=1; i<matcher.groupCount()+1; i++) {
			builder.append(matcher.group(i)).append(" ");
		}

		return builder.toString();
	}
}
