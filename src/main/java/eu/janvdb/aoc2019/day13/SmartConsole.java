package eu.janvdb.aoc2019.day13;

import eu.janvdb.aoc2019.common.ReactiveComputer;

public class SmartConsole extends AbstractConsole {

	protected SmartConsole(ReactiveComputer computer, Screen screen) {
		super(computer, screen);
	}

	protected Long getNextStep() {
		int locationOfBall = screen.find(Tile.BALL).getX();
		int locationOfPaddle = screen.find(Tile.PADDLE).getX();

		if (locationOfBall < locationOfPaddle) return -1L;
		if (locationOfBall > locationOfPaddle) return 1L;
		return 0L;
	}
}
