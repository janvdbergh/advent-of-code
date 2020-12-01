package eu.janvdb.aoc2019.day13;

import eu.janvdb.aoc2019.common.ReactiveComputer;

import java.util.Scanner;

public class Console extends AbstractConsole {

	private final Scanner scanner;

	public Console(ReactiveComputer computer, Screen screen) {
		super(computer, screen);
		this.scanner = new Scanner(System.in);
	}

	protected Long getNextStep() {
		screen.print();
		System.out.print("? ");
		return scanner.nextLong();
	}
}
