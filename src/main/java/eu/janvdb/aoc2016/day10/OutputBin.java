package eu.janvdb.aoc2016.day10;

import javaslang.collection.List;

public class OutputBin extends Receiver {

	private List<Integer> values = List.empty();

	public OutputBin(int number) {
		super(number);
	}

	@Override
	public void receive(int value) {
		values = values.append(value);
	}

	public List<Integer> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return "Bin " + getNumber() + ": " + values;
	}
}
