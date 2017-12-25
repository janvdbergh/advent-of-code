package eu.janvdb.aoc2017.day25;

class StateB implements State {

	/*
	  In state B:
	  If the current value is 0:
		- Write the value 0.
		- Move one slot to the left.
		- Continue with state B.
	  If the current value is 1:
		- Write the value 1.
		- Move one slot to the left.
		- Continue with state C.
	 */

	static final String NAME = "B";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean getValueToWriteOnZero() {
		return false;
	}

	@Override
	public Direction getDirectionToMoveOnZero() {
		return Direction.LEFT;
	}

	@Override
	public String getNextStateOnZero() {
		return StateB.NAME;
	}

	@Override
	public boolean getValueToWriteOnOne() {
		return true;
	}

	@Override
	public Direction getDirectionToMoveOnOne() {
		return Direction.LEFT;
	}

	@Override
	public String getNextStateOnOne() {
		return StateC.NAME;
	}
}
