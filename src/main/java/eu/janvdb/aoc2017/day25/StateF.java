package eu.janvdb.aoc2017.day25;

class StateF implements State {

	/*
	  In state F:
	  If the current value is 0:
		- Write the value 1.
		- Move one slot to the right.
		- Continue with state A.
	  If the current value is 1:
		- Write the value 0.
		- Move one slot to the left.
		- Continue with state E.
	 */

	static final String NAME = "F";

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
		return StateA.NAME;
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
		return StateE.NAME;
	}
}
