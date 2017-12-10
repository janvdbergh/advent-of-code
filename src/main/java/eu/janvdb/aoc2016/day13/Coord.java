package eu.janvdb.aoc2016.day13;

import io.vavr.collection.List;

public class Coord {

	private final int x, y;

	Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isOpenSpace() {
		int zz = x * x + 3 * x + 2 * x * y + y + y * y + Constants.MAGIC_NUMBER;
		return Integer.bitCount(zz) % 2 == 0;
	}

	public boolean isDestination() {
		return this.equals(Constants.DESTINATION);
	}

	public List<Coord> getMoves() {
		return List.of(new Coord(x + 1, y), new Coord(x, y + 1), new Coord(x - 1, y), new Coord(x, y - 1))
				.filter(c -> c.x >= 0 && c.y >= 0)
				.filter(Coord::isOpenSpace);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Coord coord = (Coord) o;
		return x == coord.x && y == coord.y;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
