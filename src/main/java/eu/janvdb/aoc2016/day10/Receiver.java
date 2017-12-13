package eu.janvdb.aoc2016.day10;

public abstract class Receiver {

	private final int number;

	Receiver(int number) {
		this.number = number;
	}

	protected abstract void receive(int value);

	public int getNumber() {
		return number;
	}
}
