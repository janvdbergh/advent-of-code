package eu.janvdb.aoc2018.day25;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import eu.janvdb.aoc2018.util.Point4D;

class Constellation {

	private static final int CONSTELLATION_SIZE = 3;

	private final Set<Point4D> points = new HashSet<>();

	Constellation(Point4D firstPoint) {
		points.add(firstPoint);
	}

	boolean canMergeWith(Constellation constellation) {
		for (Point4D point1 : points) {
			for (Point4D point2 : constellation.points) {
				if (point1.getManhattanDistance(point2) <= CONSTELLATION_SIZE) return true;
			}
		}

		return false;
	}

	void mergeWith(Constellation constellation) {
		points.addAll(constellation.points);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Constellation that = (Constellation) o;
		return points.equals(that.points);
	}

	@Override
	public int hashCode() {
		return Objects.hash(points);
	}
}
