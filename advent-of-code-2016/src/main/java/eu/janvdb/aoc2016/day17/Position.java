package eu.janvdb.aoc2016.day17;

import eu.janvdb.aocutil.java.CryptoUtil;
import io.vavr.collection.List;

public class Position {

	private final String path;
	private final int x, y;

	public static final Position START = new Position("", 0, 0);

	private Position(String path, int x, int y) {
		this.path = path;
		this.x = x;
		this.y = y;
	}

	public List<Position> getPossibleMoves() {
		String hashedString = CryptoUtil.md5ToHexString(Config.INPUT + path);

		List<Position> results = List.empty();
		if (y > 0 && isValidChar(hashedString.charAt(0))) {
			results = results.append(new Position(path + "U", x, y - 1));
		}
		if (y < 3 && isValidChar(hashedString.charAt(1))) {
			results = results.append(new Position(path + "D", x, y + 1));
		}
		if (x > 0 && isValidChar(hashedString.charAt(2))) {
			results = results.append(new Position(path + "L", x - 1, y));
		}
		if (x < 3 && isValidChar(hashedString.charAt(3))) {
			results = results.append(new Position(path + "R", x + 1, y));
		}

		return results;
	}

	private boolean isValidChar(char c) {
		return c >= 'b' && c <= 'f';
	}

	public boolean isSolution() {
		return x == 3 && y == 3;
	}

	public int getPathLength() {
		return path.length();
	}

	@Override
	public String toString() {
		return "Position{" +
				"pathLength='" + getPathLength() + '\'' +
				", x=" + x +
				", y=" + y +
				", path='" + path + '\'' +
				'}';
	}
}
