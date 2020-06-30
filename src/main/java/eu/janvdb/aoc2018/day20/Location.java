package eu.janvdb.aoc2018.day20;

import java.util.Objects;

class Location implements Comparable<Location> {

	private final int x;
	private final int y;

	Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location location = (Location) o;
		return getX() == location.getX() &&
				getY() == location.getY();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY());
	}

	@Override
	public int compareTo(Location o) {
		return x != o.x ? x - o.x : y - o.y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ')';
	}

	Location north() {
		return new Location(x, y - 1);
	}

	Location south() {
		return new Location(x, y + 1);
	}

	Location east() {
		return new Location(x + 1, y);
	}

	Location west() {
		return new Location(x - 1, y);
	}
}
