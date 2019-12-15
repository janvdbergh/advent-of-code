package eu.janvdb.aoc2019.common;

import eu.janvdb.util.SynchronizedHolder;
import io.reactivex.subjects.BehaviorSubject;

public class InputOutputExchanger {

	private final BehaviorSubject<Long> inputSubject = BehaviorSubject.create();
	private final SynchronizedHolder<Long> valueHolder = new SynchronizedHolder<>();

	public InputOutputExchanger(Computer computer) {
		computer.reconnectInput(inputSubject);
		computer.reconnectOutput().subscribe(valueHolder::setValue);
	}

	public long exchange(long input) throws InterruptedException {
		inputSubject.onNext(input);
		return valueHolder.getValue();
	}
}
