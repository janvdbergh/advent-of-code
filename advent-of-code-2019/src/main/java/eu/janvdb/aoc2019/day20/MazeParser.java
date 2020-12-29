package eu.janvdb.aoc2019.day20;

import eu.janvdb.aocutil.java.Point2D;

import java.util.ArrayList;
import java.util.List;

public class MazeParser {

	private final List<String> lines;

	public MazeParser(List<String> lines) {
		this.lines = lines;
	}

	public Maze1 parse() {
		int height = lines.size() - 4;
		int width = lines.stream().mapToInt(String::length).max().orElseThrow() - 4;
		boolean[][] walls = new boolean[height][];
		List<Portal> portals = new ArrayList<>();

		for (int y = 0; y < height; y++) {
			walls[y] = new boolean[width];
			for (int x = 0; x < width; x++) {
				if (getSymbolAt(x, y) != '.') {
					walls[y][x] = true;
				} else {
					String portalLabel = getPortalLabel(x, y);
					if (portalLabel!=null) {
						Portal.Type type = switch (portalLabel) {
							case "AA" -> Portal.Type.START;
							case "ZZ" -> Portal.Type.END;
							default -> (x == 0 || x == width - 1 || y == 0 || y == height - 1) ? Portal.Type.OUTER : Portal.Type.INNER;
						};
						portals.add(new Portal(type, portalLabel, new Point2D(x, y)));
					}
				}
			}
		}

		return new Maze1(walls, portals);
	}

	private String getPortalLabel(int x, int y) {
		char letter1, letter2;
		if ((Character.isLetter(letter1 = getSymbolAt(x - 2, y)) && Character.isLetter(letter2 = getSymbolAt(x - 1, y))) ||
				(Character.isLetter(letter1 = getSymbolAt(x + 1, y)) && Character.isLetter(letter2 = getSymbolAt(x + 2, y))) ||
				(Character.isLetter(letter1 = getSymbolAt(x, y - 2)) && Character.isLetter(letter2 = getSymbolAt(x, y - 1))) ||
				(Character.isLetter(letter1 = getSymbolAt(x, y + 1)) && Character.isLetter(letter2 = getSymbolAt(x, y + 2)))
		) return "" + letter1 + letter2;

		return null;
	}

	private char getSymbolAt(int x, int y) {
		if (y + 2 < 0 || y + 2 >= lines.size() || x + 2 < 0 || x + 2 >= lines.get(y + 2).length()) return '#';
		return lines.get(y + 2).charAt(x + 2);
	}
}
