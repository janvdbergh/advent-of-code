package eu.janvdb.aoc2015.day20;

public class Puzzle {

	public static void main(String[] args) {
		int number1 = 1;
		while (sumDividers(number1) * 10 < 33100000) {
			number1++;
			if (number1 % 10000 == 0) {
				System.out.println(number1);
			}
		}
		System.out.println(number1);

		int number2 = 1;
		while (sumDividers2(number2) * 11 < 33100000) {
			number2++;
			if (number2 % 10000 == 0) {
				System.out.println(number2);
			}
		}
		System.out.println(number2);
	}

	private static int sumDividers(int number) {
		int sum = 1;

		int stop = (int) Math.sqrt(number);
		for (int i = 1; i < stop; i++) {
			if (number % i == 0) {
				sum += i;
				sum += (number / i);
			}
		}

		return sum;
	}

	private static int sumDividers2(int number) {
		int sum = 1;

		int stop = (int) Math.sqrt(number);
		for (int i = 1; i < stop; i++) {
			if (number % i == 0) {
				if (number / i <= 50) sum += i;
				if (i <= 50) sum += (number / i);
			}
		}

		return sum;
	}

}
