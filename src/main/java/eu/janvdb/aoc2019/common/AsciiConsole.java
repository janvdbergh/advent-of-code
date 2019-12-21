package eu.janvdb.aoc2019.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AsciiConsole {

	private InputStream input;
	private final OutputStream output;
	private final Thread computerThread;

	public AsciiConsole(long[] program, InputStream input, OutputStream output) {
		this.input = input;
		this.output = output;

		Computer computer = new Computer(program, this::read, this::write);
		computerThread = new Thread(computer::run, "computer");
	}

	private void write(Long value) {
		try {
			if (value <256) {
				output.write(value.intValue());
			} else {
				output.write(String.format("**%d**\n", value).getBytes());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private long read() {
		try {
			return input.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void start() {
		computerThread.start();
	}

	public void stop() throws InterruptedException {
		computerThread.interrupt();
		computerThread.join();
	}

}
