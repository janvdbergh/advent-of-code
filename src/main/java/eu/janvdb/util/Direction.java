package eu.janvdb.util;

public enum Direction {

	NORTH, EAST, SOUTH, WEST;

	public Direction left() {
		switch (this) {
			case NORTH:
				return WEST;
			case WEST:
				return SOUTH;
			case SOUTH:
				return EAST;
			case EAST:
				return NORTH;
			default:
				throw new IllegalStateException();
		}
	}

	public Direction right() {
		switch (this) {
			case NORTH:
				return EAST;
			case EAST:
				return SOUTH;
			case SOUTH:
				return WEST;
			case WEST:
				return NORTH;
			default:
				throw new IllegalStateException();
		}
	}

	public Direction reverse() {
		switch (this) {
			case NORTH:
				return SOUTH;
			case EAST:
				return WEST;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
			default:
				throw new IllegalStateException();
		}
	}
}
