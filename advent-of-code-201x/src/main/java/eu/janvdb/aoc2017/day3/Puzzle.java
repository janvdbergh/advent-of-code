package eu.janvdb.aoc2017.day3;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public class Puzzle {

	private static final int INPUT = 265149;

	public static void main(String[] args) {
		part1();
		part2();
	}

	private static void part1() {
		Position position = Position.at(INPUT);
		System.out.println(position);
	}

	private static void part2() {
		Map<Position, Integer> sums = HashMap.of(Position.at(1), 1);

		for (int value = 1; value <= Position.NUMBER_OF_POSITIONS; value++) {
			Position position = Position.at(value);

			System.out.println(position + " -> " + getSum(sums, position));
			if (getSum(sums, position) > INPUT) {
				break;
			}

			sums = addToSum(sums, position, 1, 0);
			sums = addToSum(sums, position, 1, 1);
			sums = addToSum(sums, position, 0, 1);
			sums = addToSum(sums, position, -1, 1);
			sums = addToSum(sums, position, -1, 0);
			sums = addToSum(sums, position, -1, -1);
			sums = addToSum(sums, position, 0, -1);
			sums = addToSum(sums, position, 1, -1);
		}
	}

	private static int getSum(Map<Position, Integer> sums, Position position) {
		return sums.get(position).getOrElse(0);
	}

	private static Map<Position, Integer> addToSum(Map<Position, Integer> sums, Position current, int dx, int dy) {
		Position toUpdate = Position.at(current.getX() + dx, current.getY() + dy);
		int newSum = getSum(sums, toUpdate) + getSum(sums, current);
		return sums.put(toUpdate, newSum);
	}

}
