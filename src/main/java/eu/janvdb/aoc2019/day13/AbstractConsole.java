package eu.janvdb.aoc2019.day13;

import eu.janvdb.aoc2019.common.ReactiveComputer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public abstract class AbstractConsole {

	protected final Screen screen;
	private final ReactiveComputer computer;
	private final Subject<Long> inputSubject = BehaviorSubject.create();

	protected AbstractConsole(ReactiveComputer computer, Screen screen) {
		this.computer = computer;
		this.screen = screen;
		computer.setInput(inputSubject);
	}

	public void run() {
		computer.getWaitingForInput().filter(value -> value)
				.subscribe(value -> inputSubject.onNext(getNextStep()));
	}

	protected abstract Long getNextStep();
}
