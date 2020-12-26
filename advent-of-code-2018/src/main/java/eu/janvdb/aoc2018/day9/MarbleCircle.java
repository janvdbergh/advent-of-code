package eu.janvdb.aoc2018.day9;

import org.apache.commons.collections4.list.TreeList;

import java.util.List;

class MarbleCircle {

	private final List<Integer> marbles;
	private int currentMarbleIndex;
	private int nextMarbleToAdd;

	MarbleCircle() {
		this.marbles = new TreeList<>();
		marbles.add(0);
		currentMarbleIndex = 0;
		nextMarbleToAdd = 1;
	}

	int addMarbleAndReturnScore() {
		int result;
		if (nextMarbleToAdd % 23 != 0) {
			moveClockWise();
			insertMarbleAfterCurrentPosition(nextMarbleToAdd);
			result = 0;
		} else {
			for (int i = 0; i < 7; i++) moveCounterClockwise();
			result = marbles.remove(currentMarbleIndex) + nextMarbleToAdd;
		}

		nextMarbleToAdd++;
		return result;
	}

	private void moveClockWise() {
		currentMarbleIndex = (currentMarbleIndex + 1) % marbles.size();
	}

	private void moveCounterClockwise() {
		currentMarbleIndex--;
		if (currentMarbleIndex == -1) currentMarbleIndex = marbles.size() - 1;
	}

	private void insertMarbleAfterCurrentPosition(int value) {
		marbles.add(currentMarbleIndex + 1, value);
		moveClockWise();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("[");
		for (int i = 0; i < marbles.size(); i++) {
			if (i != 0) stringBuilder.append(' ');
			if (i == currentMarbleIndex) {
				stringBuilder.append('(').append(marbles.get(i)).append(')');
			} else {
				stringBuilder.append(marbles.get(i));
			}
		}
		stringBuilder.append(']');

		return stringBuilder.toString();
	}
}
