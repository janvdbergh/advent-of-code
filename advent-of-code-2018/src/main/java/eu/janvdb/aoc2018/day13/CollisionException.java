package eu.janvdb.aoc2018.day13;

class CollisionException extends RuntimeException {

	private final int x;
	private final int y;

	CollisionException(int numberOfSteps, int x, int y) {
		super(String.format("Collision after %d steps at %d,%d.", numberOfSteps, x, y));
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	int getY() {
		return y;
	}
}
