package eu.janvdb.aoc2016.day22;

import javaslang.collection.Stream;

public class Location {

	private final int x, y;

	public Location(int x, int y) {
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Location location = (Location) o;
		return x == location.x && y == location.y;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

	public boolean isNextTo(Location other) {
		return (other.x == this.x && (other.y == this.y - 1 || other.y == this.y + 1)) ||
				(other.y == this.y && (other.x == this.x - 1 || other.x == this.x + 1));
	}

	public Stream<Location> findNeighbourLocations() {
		return Stream.of(
				new Location(x + 1, y),
				new Location(x - 1, y),
				new Location(x, y + 1),
				new Location(x, y - 1)
		);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ')';
	}
}
