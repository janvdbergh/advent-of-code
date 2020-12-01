package eu.janvdb.aoc2016.day25;

import java.util.regex.Matcher;

public abstract class Instruction {

	final Matcher matcher;

	Instruction(Matcher matcher) {
		this.matcher = matcher;
	}

	public abstract State execute(State state);

	protected abstract Instruction toggle();

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
