package eu.janvdb.aoc2018.util;

import java.util.Objects;

public class Point4D {

	private final int x, y, z, t;

	public Point4D(int x, int y, int z, int t) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.t = t;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getT() {
		return t;
	}

	public int getManhattanDistance(Point4D other) {
		return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z) + Math.abs(t - other.t);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Point4D point4D = (Point4D) o;
		return getX() == point4D.getX() &&
				getY() == point4D.getY() &&
				getZ() == point4D.getZ() &&
				getT() == point4D.getT();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY(), getZ(), getT());
	}

	@Override
	public String toString() {
		return "<" + x + ',' + y + ',' + z + ',' + t + '>';
	}
}
