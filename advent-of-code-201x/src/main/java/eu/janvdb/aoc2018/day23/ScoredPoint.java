package eu.janvdb.aoc2018.day23;

import eu.janvdb.aoc2018.util.Point3D;

class ScoredPoint implements Comparable<ScoredPoint> {

	private final Point3D location;
	private final int score;

	ScoredPoint(Point3D location, int score) {
		this.location = location;
		this.score = score;
	}

	Point3D getLocation() {
		return location;
	}

	private int getManhattanDistance() {
		return location.getManhattanDistance(Day23.ORIGIN);
	}

	@Override
	public int compareTo(ScoredPoint o) {
		return score != o.score ? o.score - score : getManhattanDistance() - o.getManhattanDistance();
	}

	@Override
	public String toString() {
		return String.format("%s: %d (%d)", location, score, getManhattanDistance());
	}
}
