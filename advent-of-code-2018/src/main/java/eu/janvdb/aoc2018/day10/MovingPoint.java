package eu.janvdb.aoc2018.day10;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MovingPoint {

	private static final Pattern PATTERN = Pattern.compile("position=<\\s*(-?[\\d]+)\\s*,\\s*(-?[\\d]+)\\s*> velocity=<\\s*(-?[\\d]+)\\s*,\\s*(-?[\\d]+)\\s*>");

	private final int x, y, vx, vy;

	MovingPoint(String description) {
		Matcher matcher = PATTERN.matcher(description);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(description);
		}

		x = Integer.parseInt(matcher.group(1));
		y = Integer.parseInt(matcher.group(2));
		vx = Integer.parseInt(matcher.group(3));
		vy = Integer.parseInt(matcher.group(4));
	}

	int getXAt(int time) {
		return x + vx * time;
	}


	int getYAt(int time) {
		return y + vy * time;
	}
}
