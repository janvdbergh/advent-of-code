package eu.janvdb.aoc2018.day13;

enum Direction {
	LEFT, UP, RIGHT, DOWN;

	static Direction fromChar(char ch) {
		return switch (ch) {
			case '<' -> LEFT;
			case '>' -> RIGHT;
			case '^' -> UP;
			case 'v' -> DOWN;
			default -> throw new IllegalArgumentException(String.valueOf(ch));
		};
	}

	Direction turn(Turn turn) {
		return switch (turn) {
			case LEFT -> rotateLeft();
			case RIGHT -> rotateRight();
			case STRAIGHT -> this;
		};
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
		return switch (this) {
			case UP -> RIGHT;
			case RIGHT -> UP;
			case DOWN -> LEFT;
			case LEFT -> DOWN;
		};
	}

	Direction handleCornerTopRightOrDownLeft() {  /* \ */
		return switch (this) {
			case UP -> LEFT;
			case LEFT -> UP;
			case DOWN -> RIGHT;
			case RIGHT -> DOWN;
		};
	}
}
