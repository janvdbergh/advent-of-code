package eu.janvdb.aoc2019.day24;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class Day24 {

	private static final String[] INPUT = {
			"###..",
			".##..",
			"#....",
			"##..#",
			".###."
	};
	private static final String[] INPUT_TEST = {
			"....#",
			"#..#.",
			"#..##",
			"..#..",
			"#...."
	};
	public static final int SIZE = INPUT.length;

	public static void main(String[] args) {
		new Day24().run();
	}

	private void run() {
		BitSet input = readInput(INPUT);
		print(input);

		Set<BitSet> patterns = new HashSet<>();
		BitSet current = input;
		while (!patterns.contains(current)) {
			patterns.add(current);
			current = calculateNext(current);

			print(current);
		}

		int result = calculateDiversity(current);

		System.out.println(result);
	}

	private BitSet readInput(String[] input) {
		BitSet result = new BitSet(SIZE * SIZE);
		for (int row = 0; row < SIZE; row++) {
			for (int column = 0; column < SIZE; column++) {
				result.set(index(row, column), input[row].charAt(column) == '#');
			}
		}

		return result;
	}

	private BitSet calculateNext(BitSet current) {
		BitSet result = new BitSet(SIZE * SIZE);
		for (int row = 0; row < SIZE; row++) {
			for (int column = 0; column < SIZE; column++) {
				int count = (getValue(current, row - 1, column) ? 1 : 0) +
						(getValue(current, row + 1, column) ? 1 : 0) +
						(getValue(current, row, column - 1) ? 1 : 0) +
						(getValue(current, row, column + 1) ? 1 : 0);
				boolean currentValue = getValue(current, row, column);
				boolean newValue = (currentValue && count == 1) || (!currentValue && (count == 1 || count == 2));
				result.set(index(row, column), newValue);
			}
		}

		return result;
	}

	private void print(BitSet current) {
		for (int row = 0; row < SIZE; row++) {
			for (int column = 0; column < SIZE; column++) {
				System.out.print(getValue(current, row, column) ? '#' : '.');
			}
			System.out.println();
		}
		System.out.println();
	}

	private int calculateDiversity(BitSet current) {
		int power = 1;
		int result = 0;
		for (int row = 0; row < SIZE; row++) {
			for (int column = 0; column < SIZE; column++) {
				if (getValue(current, row, column)) result += power;
				power += power;
			}
		}
		return result;
	}

	private boolean getValue(BitSet bits, int row, int column) {
		if (row < 0 || column < 0 || row >= SIZE || column >= SIZE) return false;
		return bits.get(index(row, column));
	}

	private int index(int row, int column) {
		return row * SIZE + column;
	}

}
