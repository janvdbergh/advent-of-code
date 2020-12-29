package eu.janvdb.aocutil.java;

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

	public int getManhattanDistanceFromOrigin() {
		return Math.abs(x) + Math.abs(y) + Math.abs(z);
	}

	public int getManhattanDistance(Point3D other) {
		return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
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

	public Point3D add(Point3D other) {
		return new Point3D(x + other.x, y + other.y, z + other.z);
	}

	public Point3D add(int x1, int y1, int z1) {
		return new Point3D(x + x1, y + y1, z + z1);
	}

	public Point3D minus(Point3D other) {
		return new Point3D(x - other.x, y - other.y, z - other.z);
	}

	public Point3D move(int dx, int dy, int dz) {
		return new Point3D(x+dx, y+dy, z+dz);
	}
}
