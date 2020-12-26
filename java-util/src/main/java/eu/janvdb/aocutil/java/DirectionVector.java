package eu.janvdb.aocutil.java;

import java.util.Objects;

public class DirectionVector {

	public static DirectionVector create(Point2D source, Point2D destination) {
		int dx = destination.getX() - source.getX();
		int dy = destination.getY() - source.getY();

		if (dx == 0 && dy == 0) {
			return new DirectionVector(0, 0);
		}
		if (dx == 0) {
			return new DirectionVector(0, dy / Math.abs(dy));
		}
		if (dy == 0) {
			return new DirectionVector(dx / Math.abs(dx), 0);
		}

		int gcd = (int) MathUtil.gcd(dx, dy);
		return new DirectionVector(dx / gcd, dy / gcd);
	}

	private final int x, y;

	DirectionVector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ')';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DirectionVector other = (DirectionVector) o;
		return x == other.x && y == other.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	public double getAngleInDegreesWithZeroOnTop() {
		double result = Math.atan2(x, -y) / Math.PI * 180.0;
		while (result < 0) result += 360;
		return result;
	}
}
