package eu.janvdb.aoc2016.day10;

import java.util.ArrayList;
import java.util.List;

public class OutputBin extends Receiver {

	private final List<Integer> values = new ArrayList<>();

	public OutputBin(int number) {
		super(number);
	}

	@Override
	public void receive(int value) {
		values.add(value);
	}

	public List<Integer> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return "Bin " + getNumber() + ": " + values;
	}
}
