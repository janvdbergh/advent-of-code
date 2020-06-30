package eu.janvdb.aoc2018.util;

import java.util.Objects;

public class Coordinate {

	private final int x, y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(String coordinateStr) {
		String[] values = coordinateStr.split("\\s*,\\s*");
		x = Integer.parseInt(values[0]);
		y = Integer.parseInt(values[1]);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int manhattanDistance(Coordinate current) {
		return Math.abs(current.x - x) + Math.abs(current.y - y);
	}

	public Coordinate down() {
		return new Coordinate(x, y + 1);
	}

	public Coordinate up() {
		return new Coordinate(x, y - 1);
	}

	public Coordinate left() {
		return new Coordinate(x - 1, y);
	}

	public Coordinate right() {
		return new Coordinate(x + 1, y);
	}

	@Override
	public String toString() {
		return "(x=" + x + ", " + y + ')';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Coordinate that = (Coordinate) o;
		return getX() == that.getX() &&
				getY() == that.getY();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY());
	}
}
