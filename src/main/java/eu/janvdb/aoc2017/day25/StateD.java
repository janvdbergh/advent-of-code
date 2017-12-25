package eu.janvdb.aoc2017.day25;

class StateD implements State {

	/*
	  In state D:
	  If the current value is 0:
		- Write the value 1.
		- Move one slot to the left.
		- Continue with state E.
	  If the current value is 1:
		- Write the value 1.
		- Move one slot to the right.
		- Continue with state A.
	 */

	static final String NAME = "D";

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
		return StateE.NAME;
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
		return StateA.NAME;
	}
}
