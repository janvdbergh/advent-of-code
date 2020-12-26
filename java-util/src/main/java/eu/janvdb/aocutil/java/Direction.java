package eu.janvdb.aocutil.java;

public enum Direction {

	NORTH, EAST, SOUTH, WEST;

	public Direction left() {
		return switch (this) {
			case NORTH -> WEST;
			case WEST -> SOUTH;
			case SOUTH -> EAST;
			case EAST -> NORTH;
		};
	}

	public Direction right() {
		return switch (this) {
			case NORTH -> EAST;
			case EAST -> SOUTH;
			case SOUTH -> WEST;
			case WEST -> NORTH;
		};
	}

	public Direction reverse() {
		return switch (this) {
			case NORTH -> SOUTH;
			case EAST -> WEST;
			case SOUTH -> NORTH;
			case WEST -> EAST;
		};
	}
}
