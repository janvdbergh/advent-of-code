package eu.janvdb.aoc2019.common;

public abstract class ThreadedComputer {

	private final BasicComputer computer;
	protected final Thread computerThread;

	public ThreadedComputer(long[] program) {
		computer = new BasicComputer(program, this::produceInput, this::handleOutput);
		computerThread = new Thread(computer::run, "Computer");
	}

	protected abstract Long produceInput();

	protected abstract void handleOutput(Long value);

	public void start() {
		computerThread.start();
	}

	public void stop() {
		computerThread.interrupt();
		join();
	}

	public void join() {
		try {
			computerThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public  void write(int address, long value) {
		computer.write(address, value);
	}

}
