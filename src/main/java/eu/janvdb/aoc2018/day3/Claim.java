package eu.janvdb.aoc2018.day3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Claim {

	private static final Pattern PATTERN = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");

	private final int id;
	private final int x, y, width, height;

	static Claim read(String description) {
		Matcher matcher = PATTERN.matcher(description);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(description);
		}

		return new Claim(
				Integer.parseInt(matcher.group(1)),
				Integer.parseInt(matcher.group(2)),
				Integer.parseInt(matcher.group(3)),
				Integer.parseInt(matcher.group(4)),
				Integer.parseInt(matcher.group(5))
		);
	}

	private Claim(int id, int x, int y, int width, int height) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	int getId() {
		return id;
	}

	int getMinX() {
		return x;
	}

	int getMinY() {
		return y;
	}

	int getMaxX() {
		return x + width - 1;
	}

	int getMaxY() {
		return y + height - 1;
	}

	boolean contains(int x, int y) {
		return getMinX() <= x && x <= getMaxX() && getMinY() <= y && y <= getMaxY();
	}

	boolean overlapsWith(Claim other) {
		return getMinX() <= other.getMaxX() && other.getMinX() <= getMaxX() &&
				getMinY() <= other.getMaxY() && other.getMinY() <= getMaxY();
	}

}
