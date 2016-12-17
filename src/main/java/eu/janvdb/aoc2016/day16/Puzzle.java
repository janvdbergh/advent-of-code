package eu.janvdb.aoc2016.day16;

public class Puzzle {

	private static final int REQUIRED_LENGTH = 35651584;
	private static final String INITIAL_INPUT = "11101000110010100";

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		String data = calculateData(REQUIRED_LENGTH, INITIAL_INPUT);

		System.out.println(data.substring(REQUIRED_LENGTH));
	}

	String calculateData(int requiredLength, String initialInput) {
		String result = initialInput;
		while (result.length() < requiredLength) {
			result = reverseDragon(result);
		}

		result = result.substring(0, requiredLength);
		result += calculateChecksum(result);
		return result;
	}

	String reverseDragon(String input) {
		StringBuilder result = new StringBuilder(input.length() * 2 + 1);
		result.append(input).append('0');

		for (int i = input.length() - 1; i >= 0; i--) {
			char ch = input.charAt(i);
			switch (ch) {
				case '0':
					result.append('1');
					break;
				case '1':
					result.append('0');
					break;
				default:
					result.append(ch);
			}
		}

		return result.toString();
	}


	String calculateChecksum(String s) {
		do {
			StringBuilder result = new StringBuilder(s.length() / 2);
			for (int i = 0; i < s.length(); i += 2) {
				char ch1 = s.charAt(i);
				char ch2 = s.charAt(i + 1);
				result.append(ch1 == ch2 ? '1' : '0');
			}

			s = result.toString();
		} while (s.length() % 2 == 0 && s.length() != 0);

		return s;
	}
}
