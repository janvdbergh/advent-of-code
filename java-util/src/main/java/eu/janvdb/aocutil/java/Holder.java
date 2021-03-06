package eu.janvdb.aocutil.java;

import java.util.function.Function;

public class Holder<T> {

	private T value;

	public Holder() {
		this.value = null;
	}

	public Holder(T value) {
		this.value = value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public T update(Function<T, T> updater) {
		value = updater.apply(value);
		return value;
	}
}
