package eu.janvdb.aoc2016.day5;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

public class Puzzle {

	private static final String START = "ffykfhsq";

	public static void main(String[] args) throws Exception {
		new Puzzle().execute();
	}

	private void execute() throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");


		int index = 0;
		char[] result = "********".toCharArray();
		int charsFound = 0;
		while (true) {
			byte[] digest = md5.digest((START + index).getBytes());
			String chars = Hex.encodeHexString(digest);
			if (chars.startsWith("00000")) {
				int position = chars.charAt(5) - '0';

				if (position >= 0 && position < 8 && result[position] == '*') {
					result[position] = chars.charAt(6);
					System.out.println(result);
					charsFound++;
					if (charsFound == 8) {
						break;
					}
				}
			}

			index++;
		}
	}
}
