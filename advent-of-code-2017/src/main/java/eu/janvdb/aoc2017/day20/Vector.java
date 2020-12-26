package eu.janvdb.aoc2017.day20;

import java.util.Objects;

class Vector {

	private final long x, y, z;

	Vector(long x, long y, long z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	long getX() {
		return x;
	}

	long getY() {
		return y;
	}

	long getZ() {
		return z;
	}

	Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vector vector = (Vector) o;

		return x == vector.x && y == vector.y && z == vector.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + ']';
	}
}
