package eu.janvdb.aoc2019.common;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReactiveComputer extends ThreadedComputer {

	private final BlockingQueue<Long> inputQueue = new LinkedBlockingQueue<>();
	private final Subject<Long> outputSubject = BehaviorSubject.create();
	private final Subject<Boolean> waitingForInputSubject = BehaviorSubject.create();

	public ReactiveComputer(long[] program) {
		super(program);
		waitingForInputSubject.onNext(false);
	}

	@Override
	protected synchronized Long produceInput() {
		try {
			if (inputQueue.isEmpty()) {
				waitingForInputSubject.onNext(true);
				Long result = inputQueue.take();
				waitingForInputSubject.onNext(false);
				return result;
			} else {
				return inputQueue.take();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void handleOutput(Long value) {
		outputSubject.onNext(value);
	}

	public Observable<Long> getOutput() {
		return outputSubject;
	}

	public Observable<Boolean> getWaitingForInput() {
		return waitingForInputSubject;
	}

	public void setInput(Observable<Long> input) {
		input.subscribe(inputQueue::add);
	}
}
