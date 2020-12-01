package eu.janvdb.aoc2018.day23;

import eu.janvdb.aoc2018.util.Point3D;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Nanobot {

	private static final Pattern NANOBOT_PATTERN = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(-?\\d+)");

	private final Point3D location;
	private final int radius;

	Nanobot(String description) {
		Matcher matcher = NANOBOT_PATTERN.matcher(description);
		if (!matcher.matches()) throw new IllegalArgumentException(description);

		int x = Integer.parseInt(matcher.group(1));
		int y = Integer.parseInt(matcher.group(2));
		int z = Integer.parseInt(matcher.group(3));
		location = new Point3D(x, y, z);
		radius = Integer.parseInt(matcher.group(4));
	}

	Point3D getLocation() {
		return location;
	}

	int getRadius() {
		return radius;
	}

	boolean isInRange(Nanobot other) {
		return isInRange(other.location);
	}

	boolean isInRange(Point3D point) {
		return location.getManhattanDistance(point) <= radius;
	}

	@Override
	public String toString() {
		return String.format("pos=%s, r=%d", location, radius);
	}
}

