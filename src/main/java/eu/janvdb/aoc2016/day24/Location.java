package eu.janvdb.aoc2016.day24;

public class Location {

	private final int row, column;

	public Location(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Location location = (Location) o;
		return row == location.row && column == location.column;
	}

	@Override
	public int hashCode() {
		int result = row;
		result = 31 * result + column;
		return result;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ')';
	}
}
