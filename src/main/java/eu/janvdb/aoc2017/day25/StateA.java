package eu.janvdb.aoc2017.day25;

class StateA implements State {

	/*
	  In state A:
	  If the current value is 0:
		- Write the value 1.
		- Move one slot to the right.
		- Continue with state B.
	  If the current value is 1:
		- Write the value 0.
		- Move one slot to the right.
		- Continue with state F.
	 */

	static final String NAME = "A";

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
		return StateB.NAME;
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
		return StateF.NAME;
	}
}
