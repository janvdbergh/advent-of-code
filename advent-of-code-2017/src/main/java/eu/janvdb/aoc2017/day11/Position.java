package eu.janvdb.aoc2017.day11;

public class Position {

	private final int stepsRight, stepsUp;

	Position() {
		stepsRight = 0;
		stepsUp = 0;
	}

	private Position(int stepsRight, int stepsUp) {
		this.stepsRight = stepsRight;
		this.stepsUp = stepsUp;
	}

	public Position step(String direction) {
		return switch (direction.toLowerCase()) {
			case "n" -> new Position(stepsRight, stepsUp + 2);
			case "s" -> new Position(stepsRight, stepsUp - 2);
			case "ne" -> new Position(stepsRight + 1, stepsUp + 1);
			case "sw" -> new Position(stepsRight - 1, stepsUp - 1);
			case "nw" -> new Position(stepsRight - 1, stepsUp + 1);
			case "se" -> new Position(stepsRight + 1, stepsUp - 1);
			default -> throw new IllegalArgumentException(direction);
		};
	}

	public int getMinimalSteps() {
		int stepsHorizontal = Math.abs(stepsRight);
		int stepsVertical = Math.abs(stepsUp);
		int stepsVerticalWhileMovingHorizontal = stepsRight % 2 == stepsUp % 2 ? Math.min(stepsHorizontal, stepsVertical) : Math.min(stepsHorizontal, stepsVertical) - 1;

		return stepsHorizontal - Math.abs(stepsVerticalWhileMovingHorizontal - stepsVertical);
	}

	@Override
	public String toString() {
		return "Position{" +
				"stepsRight=" + stepsRight +
				", stepsUp=" + stepsUp +
				'}';
	}
}
