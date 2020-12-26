package eu.janvdb.aoc2015.day4;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

public class Puzzle {

	private final static String SECRET_KEY = "iwrupvqb";

	public static void main(String[] args) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("md5");

		for (int i = 0; ; i++) {
			byte[] digest = md5.digest((SECRET_KEY + i).getBytes());
			String digestString = Hex.encodeHexString(digest);
			if (digestString.startsWith("000000")) {
				System.out.println(i);
				break;
			}
		}
	}

}
