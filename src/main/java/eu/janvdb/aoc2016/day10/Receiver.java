package eu.janvdb.aoc2016.day10;

public abstract class Receiver {

	private final int number;

	public Receiver(int number) {
		this.number = number;
	}

	public abstract void receive(int value);

	public int getNumber() {
		return number;
	}
}
