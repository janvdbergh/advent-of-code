package eu.janvdb.aoc2016.day9;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class Puzzle {

	private static final Pattern PATTERN = Pattern.compile("\\((\\d+)x(\\d+)\\)");

	public static void main(String[] args) throws IOException {
		new Puzzle().execute();
	}

	private void execute() throws IOException {
		String input = FileUtils.readFileToString(new File(getClass().getResource("input.txt").getFile()), "UTF-8");
		System.out.println(getDecompressedLength1(input));
		System.out.println(getDecompressedLength2(input));
	}

	public long getDecompressedLength1(String input) {
		return getDecompressedLength(input, false);
	}

	public long getDecompressedLength2(String input) {
		return getDecompressedLength(input, true);
	}

	private long getDecompressedLength(String input, boolean recurse) {
		Matcher matcher = PATTERN.matcher(input);

		if (!matcher.find()) {
			return input.length();
		}

		int groupStart = matcher.start();
		int groupEnd = matcher.end();
		int length = Integer.parseInt(matcher.group(1));
		int number = Integer.parseInt(matcher.group(2));


		return groupStart
				+ number * (recurse ? getDecompressedLength(input.substring(groupEnd, groupEnd + length), recurse) : length)
				+ getDecompressedLength(input.substring(groupEnd + length), recurse);
	}
}
