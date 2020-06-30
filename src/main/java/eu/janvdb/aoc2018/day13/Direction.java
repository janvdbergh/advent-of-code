package eu.janvdb.aoc2018.day13;

enum Direction {
	LEFT, UP, RIGHT, DOWN;

	static Direction fromChar(char ch) {
		switch (ch) {
			case '<':
				return LEFT;
			case '>':
				return RIGHT;
			case '^':
				return UP;
			case 'v':
				return DOWN;
			default:
				throw new IllegalArgumentException(String.valueOf(ch));
		}
	}

	Direction turn(Turn turn) {
		switch (turn) {
			case LEFT:
				return rotateLeft();
			case RIGHT:
				return rotateRight();
			case STRAIGHT:
				return this;
		}
		throw new IllegalArgumentException();
	}

	private Direction rotateLeft() {
		Direction[] values = Direction.values();
		int index = (this.ordinal() + values.length - 1) % values.length;
		return values[index];
	}

	private Direction rotateRight() {
		Direction[] values = Direction.values();
		int index = (this.ordinal() + 1) % values.length;
		return values[index];
	}

	Direction handleCornerTopLeftOrDownRight() { /* / */
		switch (this) {
			case UP:
				return RIGHT;
			case RIGHT:
				return UP;
			case DOWN:
				return LEFT;
			case LEFT:
				return DOWN;
		}
		throw new IllegalArgumentException();
	}

	Direction handleCornerTopRightOrDownLeft() {  /* \ */
		switch (this) {
			case UP:
				return LEFT;
			case LEFT:
				return UP;
			case DOWN:
				return RIGHT;
			case RIGHT:
				return DOWN;
		}
		throw new IllegalArgumentException();
	}
}
