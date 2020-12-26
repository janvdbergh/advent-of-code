package eu.janvdb.aoc2018.util;

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

	public int getManhattanDistance(Point3D other) {
		return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
	}

	@Override
	public String toString() {
		return "<" + x + ',' + y + ',' + z + '>';
	}

	public Point3D middle(Point3D point2) {
		return new Point3D ((x + point2.x) / 2, (y + point2.y) / 2, (z + point2.z) / 2);
	}
}
