package eu.janvdb.aoc2017.day25;

class StateExampleB implements State {

	static final String NAME = "exampleB";

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
		return Direction.LEFT;
	}

	@Override
	public String getNextStateOnZero() {
		return StateExampleA.NAME;
	}

	@Override
	public boolean getValueToWriteOnOne() {
		return true;
	}

	@Override
	public Direction getDirectionToMoveOnOne() {
		return Direction.RIGHT;
	}

	@Override
	public String getNextStateOnOne() {
		return StateExampleA.NAME;
	}
}
