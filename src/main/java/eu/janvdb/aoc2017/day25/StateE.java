package eu.janvdb.aoc2017.day25;

class StateE implements State {

	/*
	  In state E:
	  If the current value is 0:
		- Write the value 1.
		- Move one slot to the left.
		- Continue with state F.
	  If the current value is 1:
		- Write the value 0.
		- Move one slot to the left.
		- Continue with state D.
	 */

	static final String NAME = "E";

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
		return StateF.NAME;
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
		return StateD.NAME;
	}
}
