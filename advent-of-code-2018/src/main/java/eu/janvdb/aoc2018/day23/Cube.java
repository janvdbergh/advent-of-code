package eu.janvdb.aoc2018.day23;

import eu.janvdb.aocutil.java.Point3D;

import java.util.List;
import java.util.function.Function;

public class Cube {
	private final Point3D coordinate;
	private final int radius;

	public Cube(Point3D coordinate, int radius) {
		this.coordinate = coordinate;
		this.radius = radius;
	}

	public Point3D getCoordinate() {
		return coordinate;
	}

	public int getRadius() {
		return radius;
	}

	public boolean isInRange(Point3D other) {
		return coordinate.getManhattanDistance(other) < radius;
	}

	public boolean overlaps(Cube other) {
		return overlapsOnAxis(other, Point3D::getX) && overlapsOnAxis(other, Point3D::getY) && overlapsOnAxis(other, Point3D::getZ);
	}

	private boolean overlapsOnAxis(Cube other, Function<Point3D, Integer> getter) {
		var distanceBetweenCenters = Math.abs(getter.apply(coordinate) - getter.apply(other.coordinate));
		var gap = distanceBetweenCenters - radius - other.radius;
		return gap < 0;
	}

	public List<Cube> split() {
		if (radius == 1) {
			return List.of(
					new Cube(coordinate.add(-1, -1, -1), 0),
					new Cube(coordinate.add(-1, -1, 0), 0),
					new Cube(coordinate.add(-1, -1, 1), 0),
					new Cube(coordinate.add(-1, 0, -1), 0),
					new Cube(coordinate.add(-1, 0, 0), 0),
					new Cube(coordinate.add(-1, 0, 1), 0),
					new Cube(coordinate.add(-1, 1, -1), 0),
					new Cube(coordinate.add(-1, 1, 0), 0),
					new Cube(coordinate.add(-1, 1, 1), 0),
					new Cube(coordinate.add(0, -1, -1), 0),
					new Cube(coordinate.add(0, -1, 0), 0),
					new Cube(coordinate.add(0, -1, 1), 0),
					new Cube(coordinate.add(0, 0, -1), 0),
					new Cube(coordinate.add(0, 0, 0), 0),
					new Cube(coordinate.add(0, 0, 1), 0),
					new Cube(coordinate.add(0, 1, -1), 0),
					new Cube(coordinate.add(0, 1, 0), 0),
					new Cube(coordinate.add(0, 1, 1), 0),
					new Cube(coordinate.add(1, -1, -1), 0),
					new Cube(coordinate.add(1, -1, 0), 0),
					new Cube(coordinate.add(1, -1, 1), 0),
					new Cube(coordinate.add(1, 0, -1), 0),
					new Cube(coordinate.add(1, 0, 0), 0),
					new Cube(coordinate.add(1, 0, 1), 0),
					new Cube(coordinate.add(1, 1, -1), 0),
					new Cube(coordinate.add(1, 1, 0), 0),
					new Cube(coordinate.add(1, 1, 1), 0)
			);
		}

		int newRadius = (radius + 1) / 2;
		return List.of(
				new Cube(coordinate.add(-newRadius, -newRadius, -newRadius), newRadius),
				new Cube(coordinate.add(-newRadius, -newRadius, newRadius), newRadius),
				new Cube(coordinate.add(-newRadius, newRadius, -newRadius), newRadius),
				new Cube(coordinate.add(-newRadius, newRadius, newRadius), newRadius),
				new Cube(coordinate.add(newRadius, -newRadius, -newRadius), newRadius),
				new Cube(coordinate.add(newRadius, -newRadius, newRadius), newRadius),
				new Cube(coordinate.add(newRadius, newRadius, -newRadius), newRadius),
				new Cube(coordinate.add(newRadius, newRadius, newRadius), newRadius)
		);
	}

	@Override
	public String toString() {
		return String.format("%s, r%d", coordinate, radius);
	}
}
