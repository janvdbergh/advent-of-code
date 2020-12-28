package eu.janvdb.aoc2018.day23;

import eu.janvdb.aocutil.java.Point3D;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nanobot extends Cube {

	private static final Pattern NANOBOT_PATTERN = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");

	private Nanobot(Point3D coordinate, int signalRadius) {
		super(coordinate, signalRadius);
	}

	public static Nanobot parse(String line) {
		Matcher matcher = NANOBOT_PATTERN.matcher(line);
		if (!matcher.matches()) throw new IllegalArgumentException(line);

		int x = Integer.parseInt(matcher.group(1));
		int y = Integer.parseInt(matcher.group(2));
		int z = Integer.parseInt(matcher.group(3));
		int signalRadius = Integer.parseInt(matcher.group(4));

		return new Nanobot(new Point3D(x, y, z), signalRadius);
	}
}
