package eu.janvdb.aoc2015.day11;

public class Puzzle {

	private static final String START = "vzbxkghb";

	public static void main(String[] args) {
		String password = START;

		for(int i=0; i<5; i++) {
			password = increment(password);
			while (!isValid(password)) {
				password = increment(password);
			}

			System.out.printf("%d - %s\n", i, password);
		}
	}

	private static String increment(String password) {
		StringBuilder newPassword = new StringBuilder(password);
		for (int position = password.length() - 1; position >= 0; position--) {
			if (newPassword.charAt(position) != 'z') {
				newPassword.replace(position, position + 1, Character.toString((char) (newPassword.charAt(position) + 1)));
				break;
			} else {
				newPassword.replace(position, position+1, "a");
			}
		}

		return newPassword.toString();
	}

	private static boolean isValid(String password) {
		return password.matches(".*(abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz).*") &&
				!password.matches(".*[iol].*") &&
				password.matches(".*([a-z])\\1.*([a-z])\\2.*");
	}
}
