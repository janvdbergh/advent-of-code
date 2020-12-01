package eu.janvdb.aoc2017.day25;

class StateC implements State {

	/*
	  In state C:
	  If the current value is 0:
		- Write the value 1.
		- Move one slot to the left.
		- Continue with state D.
	  If the current value is 1:
		- Write the value 0.
		- Move one slot to the right.
		- Continue with state C.
	 */

	static final String NAME = "C";

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
		return StateD.NAME;
	}

	@Override
	public boolean getValueToWriteOnOne() {
		return false;
	}

	@Override
	public Direction getDirectionToMoveOnOne() {
		return Direction.RIGHT;
	}

	@Override
	public String getNextStateOnOne() {
		return StateC.NAME;
	}
}
