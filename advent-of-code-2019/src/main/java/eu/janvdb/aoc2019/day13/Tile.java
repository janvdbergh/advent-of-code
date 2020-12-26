package eu.janvdb.aoc2019.day13;

import java.util.Arrays;

public enum Tile {

	EMPTY(0, ' '),
	WALL(1, '+'),
	BLOCK(2, '*'),
	PADDLE(3, '-'),
	BALL(4, 'o');

	public static Tile findByValue(int value) {
		return Arrays.stream(Tile.values())
				.filter(tile -> tile.value == value)
				.findAny().orElseThrow();
	}

	private final int value;
	private final char display;

	Tile(int value, char display) {
		this.value = value;
		this.display = display;
	}

	public char getDisplayChar() {
		return display;
	}
}
