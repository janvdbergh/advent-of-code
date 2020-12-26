package eu.janvdb.aoc2017.day22;

public class VirusCarrier {

	private final Grid grid;

	private Position currentPosition;
	private Direction currentDirection;
	private int nodesInfected;

	VirusCarrier(Grid grid) {
		this.grid = grid;

		this.currentPosition = new Position(0, 0);
		this.currentDirection = Direction.UP;
		this.nodesInfected = 0;
	}

	void performBurst() {
		switch (grid.getState(currentPosition)) {
			case CLEAN -> {
				currentDirection = currentDirection.turnLeft();
				grid.setState(currentPosition, Grid.State.WEAKENED);
			}
			case WEAKENED -> {
				grid.setState(currentPosition, Grid.State.INFECTED);
				nodesInfected++;
			}
			case INFECTED -> {
				grid.setState(currentPosition, Grid.State.FLAGGED);
				currentDirection = currentDirection.turnRight();
			}
			case FLAGGED -> {
				grid.setState(currentPosition, Grid.State.CLEAN);
				currentDirection = currentDirection.reverse();
			}
			default -> throw new IllegalStateException();
		}

		currentPosition = currentDirection.move(currentPosition);
	}

	@Override
	public String toString() {
		return "VirusCarrier{" +
				"currentPosition=" + currentPosition +
				", currentDirection=" + currentDirection +
				", nodesInfected=" + nodesInfected +
				'}';
	}

	enum Direction {
		UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

		private final int dx, dy;

		Direction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}

		Position move(Position position) {
			return new Position(position.getX() + dx, position.getY() + dy);
		}

		Direction turnRight() {
			int newOrdinal = (this.ordinal() + 1) % Direction.values().length;
			return Direction.values()[newOrdinal];
		}

		Direction turnLeft() {
			int newOrdinal = (this.ordinal() + Direction.values().length - 1) % Direction.values().length;
			return Direction.values()[newOrdinal];
		}

		public Direction reverse() {
			int newOrdinal = (this.ordinal() + 2) % Direction.values().length;
			return Direction.values()[newOrdinal];
		}
	}

}
