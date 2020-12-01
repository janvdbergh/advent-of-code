package eu.janvdb.aoc2018.util;

public class Holder<T> {

	private T value;

	public Holder(T value) {
		this.value = value;
	}

	public T get() {
		return value;
	}

	public void set(T newValue) {
		value = newValue;
	}
}
