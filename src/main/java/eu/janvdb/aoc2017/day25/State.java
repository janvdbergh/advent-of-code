package eu.janvdb.aoc2017.day25;

interface State {

	String getName();

	boolean getValueToWriteOnZero();
	boolean getValueToWriteOnOne();

	Direction getDirectionToMoveOnZero();
	Direction getDirectionToMoveOnOne();

	String getNextStateOnZero();
	String getNextStateOnOne();
}

