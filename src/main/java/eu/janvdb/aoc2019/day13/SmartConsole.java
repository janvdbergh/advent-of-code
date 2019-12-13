package eu.janvdb.aoc2019.day13;

import eu.janvdb.aoc2019.common.Computer;

public class SmartConsole {

	private final Screen screen;

	public SmartConsole(Computer computer, Screen screen) {
		this.screen = screen;
		computer.reconnectInput(this::getNextStep);
	}

	private Long getNextStep() {
		int locationOfBall = screen.find(Tile.BALL).getX();
		int locationOfPaddle = screen.find(Tile.PADDLE).getX();

		if (locationOfBall < locationOfPaddle) return -1L;
		if (locationOfBall > locationOfPaddle) return 1L;
		return 0L;
	}
}
