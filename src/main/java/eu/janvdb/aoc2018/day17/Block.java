package eu.janvdb.aoc2018.day17;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Block {

	private final static Pattern PATTERN_X = Pattern.compile("x=(\\d+)(..(\\d+))?");
	private final static Pattern PATTERN_Y = Pattern.compile("y=(\\d+)(..(\\d+))?");

	private final int x1, x2, y1, y2;

	Block(String description) {
		Matcher matcherX = PATTERN_X.matcher(description);
		if (matcherX.find()) {
			x1 = Integer.parseInt(matcherX.group(1));
			x2 = matcherX.group(3) != null ? Integer.parseInt(matcherX.group(3)) : x1;
		} else {
			throw new IllegalArgumentException(description);
		}

		Matcher matcherY = PATTERN_Y.matcher(description);
		if (matcherY.find()) {
			y1 = Integer.parseInt(matcherY.group(1));
			y2 = matcherY.group(3) != null ? Integer.parseInt(matcherY.group(3)) : y1;
		} else {
			throw new IllegalArgumentException(description);
		}
	}

	int getX1() {
		return x1;
	}

	int getX2() {
		return x2;
	}

	int getY1() {
		return y1;
	}

	int getY2() {
		return y2;
	}
}
