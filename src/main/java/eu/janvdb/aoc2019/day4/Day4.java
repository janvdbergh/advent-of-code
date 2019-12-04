package eu.janvdb.aoc2019.day4;

import java.util.stream.IntStream;

public class Day4 {

	private static final int MIN = 137683;
	private static final int MAX = 596253;
	private static final int NUMBER_OF_DIGITS = 6;

	public static void main(String[] args) {
		new Day4().run();
	}

	private void run() {
		part1();
	}

	private void part1() {
		long number = IntStream.range(MIN, MAX + 1)
				.filter(this::isCorrect)
				.count();
		System.out.println(number);
	}

	private boolean isCorrect(int guess) {
		int[] digits = new int[NUMBER_OF_DIGITS];
		for (int i = 0; i < NUMBER_OF_DIGITS; i++) {
			digits[5 - i] = guess % 10;
			guess = guess / 10;
		}

		boolean hasSameDigits =
				(digits[0] == digits[1] && digits[1] != digits[2]) ||
						(digits[1] == digits[2] && digits[1] != digits[0] && digits[1] != digits[3]) ||
						(digits[2] == digits[3] && digits[2] != digits[1] && digits[2] != digits[4]) ||
						(digits[3] == digits[4] && digits[3] != digits[2] && digits[3] != digits[5]) ||
						(digits[4] == digits[5] && digits[4] != digits[3]);
		boolean digitsInOrder = digits[0] <= digits[1] && digits[1] <= digits[2] && digits[2] <= digits[3] && digits[3] <= digits[4] && digits[4] <= digits[5];
		return hasSameDigits && digitsInOrder;
	}
}
