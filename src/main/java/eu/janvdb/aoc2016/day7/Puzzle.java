package eu.janvdb.aoc2016.day7;

import io.vavr.collection.List;
import io.vavr.collection.Stream;

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

		long count1 = Stream.of(Input.INPUT)
				.count(this::matchesPattern1);
		System.out.println(count1);
		System.out.println();

		System.out.println(matchesPattern2("aba[bab]xyz"));
		System.out.println(matchesPattern2("xyx[xyx]xyx"));
		System.out.println(matchesPattern2("aaa[kek]eke"));
		System.out.println(matchesPattern2("zazbz[bzb]cdb"));

		long count2 = Stream.of(Input.INPUT)
				.count(this::matchesPattern2);

		System.out.println(count2);
	}

	private boolean matchesPattern1(String line) {
		Pattern pattern1 = Pattern.compile(("\\[[a-z]*([a-z])([a-z])\\2\\1[a-z]*]"));
		Pattern pattern2 = Pattern.compile(("([a-z])([a-z])\\2\\1"));

		Matcher matcher1 = pattern1.matcher(line);
		Matcher matcher2 = pattern2.matcher(line);
		return !matcher1.find() && matcher2.find() && !matcher2.group(1).equals(matcher2.group(2));
	}

	private boolean matchesPattern2(String line) {
		List<String> nonBracketParts = List.empty();
		List<String> bracketParts = List.empty();

		boolean inBracketPart = false;
		int start = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '[') {
				if (inBracketPart) throw new IllegalArgumentException("Nested brackets.");
				nonBracketParts = nonBracketParts.append(line.substring(start, i));

				inBracketPart = true;
				start = i + 1;
			} else if (line.charAt(i) == ']') {
				if (!inBracketPart) throw new IllegalArgumentException("Non-matching closing brackerts.");
				bracketParts=bracketParts.append(line.substring(start, i));

				inBracketPart = false;
				start = i + 1;
			}
		}
		if (inBracketPart) throw new IllegalArgumentException("Non-matching closing brackerts.");
		nonBracketParts = nonBracketParts.append(line.substring(start));

		List<String> finalBracketParts = bracketParts;
		return nonBracketParts.toStream()
				.flatMap(nonBracketPart -> finalBracketParts.map(bracketPart -> matches2(nonBracketPart, bracketPart)))
				.find(x -> x)
				.isDefined();
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
