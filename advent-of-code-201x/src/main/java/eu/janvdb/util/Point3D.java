package eu.janvdb.util;

import java.util.Objects;

public class Point3D {

	private final int x, y, z;

	public Point3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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

	public int getEnergy() {
		return Math.abs(x) + Math.abs(y) + Math.abs(z);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ')';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Point3D other = (Point3D) o;
		return x == other.x && y == other.y && z == other.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	public Point3D add(Point3D vector) {
		return new Point3D(x + vector.x, y + vector.y, z + vector.z);
	}
}
