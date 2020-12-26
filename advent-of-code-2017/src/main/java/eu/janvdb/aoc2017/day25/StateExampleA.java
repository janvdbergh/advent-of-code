package eu.janvdb.aoc2017.day25;

class StateExampleA implements State {

	static final String NAME = "exampleA";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean getValueToWriteOnZero() {
		return true;
	}

	@Override
	public Direction getDirectionToMoveOnZero() {
		return Direction.RIGHT;
	}

	@Override
	public String getNextStateOnZero() {
		return StateExampleB.NAME;
	}

	@Override
	public boolean getValueToWriteOnOne() {
		return false;
	}

	@Override
	public Direction getDirectionToMoveOnOne() {
		return Direction.LEFT;
	}

	@Override
	public String getNextStateOnOne() {
		return StateExampleB.NAME;
	}
}
