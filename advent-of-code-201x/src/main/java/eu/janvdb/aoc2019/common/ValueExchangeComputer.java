package eu.janvdb.aoc2019.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ValueExchangeComputer extends ThreadedComputer {

	private final BlockingQueue<Long> inputQueue = new LinkedBlockingQueue<>();
	private final BlockingQueue<Long> outputQueue = new LinkedBlockingQueue<>();

	public ValueExchangeComputer(long[] program) {
		super(program);
	}

	@Override
	protected Long produceInput() {
		try {
			return inputQueue.take();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void handleOutput(Long value) {
		outputQueue.add(value);
	}

	public long exchange(long input) throws InterruptedException {
		inputQueue.add(input);
		return outputQueue.take();
	}

}
