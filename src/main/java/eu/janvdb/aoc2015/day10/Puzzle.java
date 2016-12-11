package eu.janvdb.aoc2015.day10;

public class Puzzle {

	public static void main(String[] args) {
		String input = "1113222113";
		for(int i=1; i<=50; i++) {
			input = lookAndSay(input);
			System.out.printf("%02d - %5d\n", i, input.length());
		}
	}

	private static String lookAndSay(String input) {
		StringBuilder result = new StringBuilder();
		char currentChar = 'x';
		int currentSize = 0;

		for (char ch : input.toCharArray()) {
			if (currentChar == ch) {
				currentSize++;
			} else {
				if (currentSize != 0) {
					result.append(currentSize);
					result.append(currentChar);
				}
				currentChar = ch;
				currentSize = 1;
			}
		}

		if (currentSize != 0) {
			result.append(currentSize);
			result.append(currentChar);
		}

		return result.toString();
	}
}
