package eu.janvdb.aoc2017.day20;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Particle {

	private static final Pattern PATTERN = Pattern.compile("p=<(-?\\d+),(-?\\d+),(-?\\d+)>, v=<(-?\\d+),(-?\\d+),(-?\\d+)>, a=<(-?\\d+),(-?\\d+),(-?\\d+)>");

	private final int index;
	private final Vector position, velocity, acceleration;

	Particle(int index, String description) {
		this.index = index;

		Matcher matcher = PATTERN.matcher(description);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(description);
		}

		position = new Vector(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
		velocity = new Vector(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)));
		acceleration = new Vector(Integer.parseInt(matcher.group(7)), Integer.parseInt(matcher.group(8)), Integer.parseInt(matcher.group(9)));
	}

	private Particle(int index, Vector position, Vector velocity, Vector acceleration) {
		this.index = index;
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
	}

	Vector getPosition() {
		return position;
	}

	long getManhattanDistance() {
		return Math.abs(position.getX()) + Math.abs(position.getY()) + Math.abs(position.getZ());
	}

	Particle step() {
		Vector newVelocity = velocity.add(acceleration);
		Vector newPosition = position.add(newVelocity);
		return new Particle(index, newPosition, newVelocity, acceleration);
	}


	@Override
	public String toString() {
		return "Particle{" +
				"index=" + index +
				", position=" + position +
				", velocity=" + velocity +
				", acceleration=" + acceleration +
				'}';
	}
}
