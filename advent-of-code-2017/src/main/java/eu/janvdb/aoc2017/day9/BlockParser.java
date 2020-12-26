package eu.janvdb.aoc2017.day9;

public class BlockParser {

	public int countGroups(String input) {
		String withoutIgnoredCharacters = removeIgnoredCharacters(input);
		String withoutGarbage = removeGarbage(withoutIgnoredCharacters);
		return doCountGroups(withoutGarbage);
	}

	public int countGarbage(String input) {
		String withoutIgnoredCharacters = removeIgnoredCharacters(input);
		return doCountGarbage(withoutIgnoredCharacters);
	}

	private String removeIgnoredCharacters(String s) {
		return s.replaceAll("!.", "");
	}

	private String removeGarbage(String s) {
		return s.replaceAll("<.*?>", "");
	}

	private int doCountGroups(String s) {
		int sum = 0;
		int currentValue = 0;
		for (char c : s.toCharArray()) {
			if (c=='{') {
				currentValue++;
				sum += currentValue;
			}
			if (c=='}') {
				currentValue--;
			}

			if (currentValue<0) {
				throw new IllegalArgumentException();
			}
		}

		if (currentValue!=0) {
			throw new IllegalArgumentException();
		}

		return sum;
	}

	private int doCountGarbage(String s) {
		int count = 0;
		boolean inGarbage = false;
		for (char c : s.toCharArray()) {
			if (c=='<' && !inGarbage) {
				inGarbage = true;
			} else if (c=='>' && inGarbage) {
				inGarbage = false;
			} else if (inGarbage) {
				count++;
			}
		}

		if (inGarbage) {
			throw new IllegalArgumentException();
		}

		return count;
	}

}
