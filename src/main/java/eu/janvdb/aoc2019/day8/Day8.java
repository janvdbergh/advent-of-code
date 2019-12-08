package eu.janvdb.aoc2019.day8;

import eu.janvdb.util.InputReader;

public class Day8 {

	private final static int NUMBER_OF_ROWS = 6;
	private final static int NUMBER_OF_COLUMNS = 25;

	public static final int BLACK = 0;
	public static final int WHITE = 1;
	public static final int TRANSPARENT = 2;
	public static final String[] STRINGS = {"  ", "XX", ".."};

	private final String digits;
	private final int numberOfLayers;

	public static void main(String[] args) {
		new Day8().run();
	}

	private Day8() {
		digits = InputReader.readInputFully(Day8.class.getResource("day8.txt")).trim();
		numberOfLayers = digits.length() / NUMBER_OF_COLUMNS / NUMBER_OF_ROWS;
	}

	private void run() {
		part1();
		part2();
	}

	private void part1() {
		int minLayer = -1;
		int minZeroes = Integer.MAX_VALUE;
		for (int layer = 0; layer < numberOfLayers; layer++) {
			int zeroes = countDigits(layer, 0);
			if (zeroes < minZeroes) {
				minZeroes = zeroes;
				minLayer = layer;
			}
		}

		int count1 = countDigits(minLayer, 1);
		int count2 = countDigits(minLayer, 2);
		System.out.println(count1 * count2);
	}

	private int countDigits(int layer, int value) {
		int result = 0;
		for (int row = 0; row < NUMBER_OF_ROWS; row++) {
			for (int column = 0; column < NUMBER_OF_COLUMNS; column++) {
				if (getPixel(layer, row, column) == value) {
					result++;
				}
			}
		}

		return result;
	}

	private void part2() {
		for (int row = 0; row < NUMBER_OF_ROWS; row++) {
			for (int column = 0; column < NUMBER_OF_COLUMNS; column++) {
				int value = getColor(row, column);
				System.out.print(STRINGS[value]);
			}
			System.out.println();
		}
	}

	private int getColor(int row, int column) {
		for (int layer = 0; layer < numberOfLayers; layer++) {
			switch (getPixel(layer, row, column)) {
				case BLACK:
					return BLACK;
				case WHITE:
					return WHITE;
			}
		}
		return TRANSPARENT;
	}

	private int getPixel(int layer, int row, int column) {
		int index = column + (row + layer * NUMBER_OF_ROWS) * NUMBER_OF_COLUMNS;
		return digits.charAt(index) - '0';
	}
}
