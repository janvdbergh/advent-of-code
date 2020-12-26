package eu.janvdb.aoc2017.day19;

class Position {
	private final int x, y;

	Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	Position left() {
		return new Position(x + 1, y);
	}

	Position right() {
		return new Position(x - 1, y);
	}

	Position down() {
		return new Position(x, y + 1);
	}

	Position up() {
		return new Position(x, y - 1);
	}

	Position forwardFrom(Position previous) {
		return new Position(2 * getX() - previous.getX(), 2 * getY() - previous.getY());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return x == position.x && y == position.y;
	}

	@Override
	public int hashCode() {
		return 31 * x + y;
	}

	@Override
	public String toString() {
		return "Position{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
