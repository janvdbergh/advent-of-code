package eu.janvdb.aoc2015.day19;

import java.util.Map;
import java.util.TreeMap;

public class Alternative {

	public static void main(String[] args) {
		Map<Character, Integer> histogram = new TreeMap<>();

		for (char ch : Puzzle.START.toCharArray()) {
			if (!histogram.containsKey(ch)) {
				histogram.put(ch, 0);
			}
			histogram.put(ch, histogram.get(ch) + 1);
		}

		for (Map.Entry<Character, Integer> characterIntegerEntry : histogram.entrySet()) {
			System.out.println(characterIntegerEntry.getKey() + ": " + characterIntegerEntry.getValue());
		}
		System.out.println(Puzzle.START.length());
		System.out.println(
				Puzzle.START.length()
						- 2 * histogram.get('(')
						- 2 * histogram.get(',')
		);
	}

}
