package eu.janvdb.aoc2017.day12;


import io.vavr.collection.Set;
import io.vavr.collection.TreeSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {

	private static final Pattern PATTERN = Pattern.compile("(\\d+) <-> (.*)");

	private final int id;
	private final Set<Integer> connections;

	public Program(String input) {
		Matcher matcher = PATTERN.matcher(input);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(input);
		}

		id = Integer.parseInt(matcher.group(1));
		connections = TreeSet.of(matcher.group(2).split("\\s*,\\s*"))
				.map(Integer::parseInt);
	}

	public int getId() {
		return id;
	}

	public Set<Integer> getConnections() {
		return connections;
	}
}
