package eu.janvdb.aocutil.java;

import java.util.Objects;

public class Point2D {

	private final int x, y;

	public Point2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point2D move(int dx, int dy) {
		return new Point2D(x + dx, y + dy);
	}

	public Point2D step(Direction direction) {
		return step(direction, 1);
	}

	public Point2D step(Direction direction, int steps) {
		return switch (direction) {
			case NORTH -> new Point2D(x, y - steps);
			case WEST -> new Point2D(x - steps, y);
			case SOUTH -> new Point2D(x, y + steps);
			case EAST -> new Point2D(x + steps, y);
		};
	}

	public int getManhattanDistance() {
		return Math.abs(x) + Math.abs(y);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ')';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Point2D point2D = (Point2D) o;
		return x == point2D.x && y == point2D.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	public DirectionVector getDirectionFrom(Point2D other) {
		return DirectionVector.create(other, this);
	}
}
