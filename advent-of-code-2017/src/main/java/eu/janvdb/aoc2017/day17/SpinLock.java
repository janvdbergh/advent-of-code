package eu.janvdb.aoc2017.day17;

import java.util.ArrayList;
import java.util.List;

public class SpinLock {

	private final int steps;
	private final List<Integer> values;
	private int position;

	SpinLock(int steps, int initialCapacity) {
		this.steps = steps;
		position = 0;
		values = new ArrayList<>(initialCapacity);
		values.add(0);
	}

	void step() {
		position = (position + steps) % values.size() + 1;
		values.add(position, values.size());
	}

	int getValueAtNextPosition() {
		int thePosition = (position + 1) % values.size();
		return values.get(thePosition);
	}

	int getValueAtPositionAfterZero() {
		int thePosition = (values.indexOf(0) + 1) % values.size();
		return values.get(thePosition);
	}

	@Override
	public String toString() {
		return "SpinLock{" +
				"values=" + values +
				'}';
	}
}
