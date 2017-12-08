package eu.janvdb.aoc2017.day3;

import javaslang.collection.Vector;

import java.util.Objects;

public class Position {
	private final int value;
	private final int x, y;

	static final int NUMBER_OF_POSITIONS = 300000;
	private static final Vector<Position> POSITIONS = calculatePositions();

	private static Vector<Position> calculatePositions() {
		int x = 0;
		int y = 0;
		int ring = 1;
		Direction direction = Direction.RIGHT;

		Vector<Position> positions = Vector.empty();
		for (int value = 1; value <= NUMBER_OF_POSITIONS; value++) {
			positions = positions.append(new Position(value, x, y));

			if (direction == Direction.RIGHT && x == ring && y == -ring) {
				ring++;
			}
			if (Math.abs(x + direction.dx) > ring || Math.abs(y + direction.dy) > ring) {
				direction = direction.next();
			}
			x += direction.dx;
			y += direction.dy;
		}

		return positions;
	}


	public static Position at(int value) {
		return POSITIONS.get(value - 1);
	}

	public static Position at(int x, int y) {
		return POSITIONS.toStream()
				.find(position -> position.x == x && position.y == y)
				.getOrElseThrow(IllegalArgumentException::new);
	}

	private Position(int value, int x, int y) {
		this.value = value;
		this.x = x;
		this.y = y;
	}

	public int getValue() {
		return value;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDistance() {
		return Math.abs(x) + Math.abs(y);
	}

	@Override
	public String toString() {
		return "Position{" +
				"value=" + value +
				", x=" + x +
				", y=" + y +
				", distance=" + getDistance() +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return value == position.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	private enum Direction {

		RIGHT(1, 0),
		UP(0, 1),
		LEFT(-1, 0),
		DOWN(0, -1);

		public final int dx, dy;

		Direction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}

		public Direction next() {
			Direction[] values = Direction.values();
			return values[(this.ordinal() + 1) % values.length];
		}
	}
}
