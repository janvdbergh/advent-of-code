package eu.janvdb.aoc2018.day13;

class Cart {

	private int x, y;
	private Direction direction;
	private Turn nextTurn;
	private int numberOfSteps;

	Cart(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.nextTurn = Turn.LEFT;
		this.numberOfSteps = 0;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	void move(Track track) {
		numberOfSteps++;

		switch (direction) {
			case UP:
				y--;
				break;
			case DOWN:
				y++;
				break;
			case LEFT:
				x--;
				break;
			case RIGHT:
				x++;
				break;
		}

		if (track.isCornerTopLeftOrDownRight(x, y)) {
			direction = direction.handleCornerTopLeftOrDownRight();
		}

		if (track.isCornerTopRightOrDownLeft(x, y)) {
			direction = direction.handleCornerTopRightOrDownLeft();
		}

		if (track.isIntersection(x, y)) {
			direction = direction.turn(nextTurn);
			nextTurn = nextTurn.next();
		}

		if (track.containsMultipleCarts(x, y)) {
			throw new CollisionException(numberOfSteps, x, y);
		}
	}
}
