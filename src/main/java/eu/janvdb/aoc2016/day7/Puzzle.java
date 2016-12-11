package eu.janvdb.aoc2016.day7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		System.out.println(matchesPattern1("abba[mnop]qrst"));
		System.out.println(matchesPattern1("abcd[bddb]xyyx"));
		System.out.println(matchesPattern1("aaaa[qwer]tyui"));
		System.out.println(matchesPattern1("ioxxoj[asdfgh]zxcvbn"));

		long count1 = Arrays.stream(Input.INPUT)
				.filter(this::matchesPattern1)
				.count();
		System.out.println(count1);
		System.out.println();

		System.out.println(matchesPattern2("aba[bab]xyz"));
		System.out.println(matchesPattern2("xyx[xyx]xyx"));
		System.out.println(matchesPattern2("aaa[kek]eke"));
		System.out.println(matchesPattern2("zazbz[bzb]cdb"));

		long count2 = Arrays.stream(Input.INPUT)
				.filter(this::matchesPattern2)
				.count();

		System.out.println(count2);
	}

	private boolean matchesPattern1(String line) {
		Pattern PATTERN1 = Pattern.compile(("\\[[a-z]*([a-z])([a-z])\\2\\1[a-z]*\\]"));
		Pattern PATTERN2 = Pattern.compile(("([a-z])([a-z])\\2\\1"));

		Matcher matcher1 = PATTERN1.matcher(line);
		Matcher matcher2 = PATTERN2.matcher(line);
		return !matcher1.find() && matcher2.find() && !matcher2.group(1).equals(matcher2.group(2));
	}

	private boolean matchesPattern2(String line) {
		List<String> nonBracketParts = new ArrayList<>();
		List<String> bracketParts = new ArrayList<>();

		boolean inBracketPart = false;
		int start = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '[') {
				if (inBracketPart) throw new IllegalArgumentException("Nested brackets.");
				nonBracketParts.add(line.substring(start, i));

				inBracketPart = true;
				start = i + 1;
			} else if (line.charAt(i) == ']') {
				if (!inBracketPart) throw new IllegalArgumentException("Non-matching closing brackerts.");
				bracketParts.add(line.substring(start, i));

				inBracketPart = false;
				start = i + 1;
			}
		}
		if (inBracketPart) throw new IllegalArgumentException("Non-matching closing brackerts.");
		nonBracketParts.add(line.substring(start));

		for (String nonBracketPart : nonBracketParts) {
			for (String bracketPart : bracketParts) {
				if (matches2(nonBracketPart, bracketPart)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean matches2(String nonBracketPart, String bracketPart) {
		for (int i = 0; i < nonBracketPart.length() - 2; i++) {
			if (nonBracketPart.charAt(i) == nonBracketPart.charAt(i + 2) && nonBracketPart.charAt(i) != nonBracketPart.charAt(i + 1)) {
				String bab = "" + nonBracketPart.charAt(i + 1) + nonBracketPart.charAt(i) + nonBracketPart.charAt(i + 1);

				if (bracketPart.contains(bab)) {
					return true;
				}
			}
		}
		return false;
	}
}
