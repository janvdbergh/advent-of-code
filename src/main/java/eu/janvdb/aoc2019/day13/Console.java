package eu.janvdb.aoc2019.day13;

import java.util.Scanner;

import eu.janvdb.aoc2019.common.Computer;

public class Console {

	private final Screen screen;
	private final Scanner scanner;

	public Console(Computer computer, Screen screen) {
		this.screen = screen;
		this.scanner = new Scanner(System.in);
		computer.reconnectInput(this::getNextStep);
	}

	private Long getNextStep() {
		screen.print();
		System.out.print("? ");
		return scanner.nextLong();
	}
}
