package eu.janvdb.aoc2017.day10;

import javaslang.collection.Stream;
import javaslang.collection.Vector;

import java.util.stream.Collectors;

public class Knotter {

	private static final int[] FINAL_STEPS = {17, 31, 73, 47, 23};
	private static final int NUMBER_OF_ROUNDS = 64;

	public String knotString(String input) {
		Knot current = new Knot(256);

		for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
			for (char c : input.toCharArray()) {
				if (c > 255) {
					throw new IllegalArgumentException();
				}
				current = current.doStep(c);
			}

			for (int step : FINAL_STEPS) {
				current = current.doStep(step);
			}
		}

		Knot knot = current;
		return Stream.range(0, 16)
				.map(blockNumber -> makeDense(knot.getNumbers(), blockNumber))
				.toJavaStream()
				.collect(Collectors.joining());

	}

	private String makeDense(Vector<Integer> numbers, int blockNumber) {
		int xored = numbers.slice(blockNumber * 16, blockNumber * 16 + 16)
				.toJavaStream()
				.reduce(0, (x, y) -> x ^ y);

		return String.format("%02x", xored);
	}
}
