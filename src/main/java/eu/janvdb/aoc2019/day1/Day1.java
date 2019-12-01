package eu.janvdb.aoc2019.day1;

import eu.janvdb.util.InputReader;

public class Day1 {

	public static void main(String[] args) {
		new Day1().run();
	}

	private void run() {
		long fuel1 = InputReader.readInput(Day1.class.getResource("input01.txt"))
				.map(Long::parseLong)
				.map(this::calculateFuel1)
				.sum().longValue();
		System.out.println(fuel1);

		long fuel2 = InputReader.readInput(Day1.class.getResource("input01.txt"))
				.map(Long::parseLong)
				.map(this::calculateFuel2)
				.sum().longValue();
		System.out.println(fuel2);
	}

	long calculateFuel1(long mass) {
		return mass / 3 - 2;
	}

	long calculateFuel2(long mass) {
		long value = calculateFuel1(mass);
		if (value <= 0) return 0;
		return value + calculateFuel2(value);
	}

}
