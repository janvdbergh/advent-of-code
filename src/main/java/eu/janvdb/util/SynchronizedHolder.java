package eu.janvdb.util;

import io.vavr.control.Option;

public class SynchronizedHolder<T> {

	private Option<T> value = Option.none();

	public synchronized void setValue(T value) {
		if (this.value.isDefined()) throw new IllegalStateException();

		this.value = Option.of(value);
		notifyAll();
	}

	public synchronized T getValue() throws InterruptedException {
		while (value.isEmpty()) wait();

		T result = value.get();
		this.value = Option.none();
		return result;
	}
}
