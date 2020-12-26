package eu.janvdb.aoc2018.day17;

import eu.janvdb.aoc2018.util.Coordinate;

import java.util.Arrays;
import java.util.Collection;

class Map {

	private static final char SAND = '.';
	private static final char CLAY = '#';
	private static final char SOURCE = '+';
	private static final char FLOW = '|';
	private static final char WATER = '~';

	private final int minx, miny, maxx, maxy;
	private final char[] map;

	Map(Collection<Block> blocks) {
		minx = blocks.stream().mapToInt(Block::getX1).min().orElseThrow() - 1;
		maxx = blocks.stream().mapToInt(Block::getX2).max().orElseThrow() + 1;
		miny = blocks.stream().mapToInt(Block::getY1).min().orElseThrow() - 1;
		maxy = blocks.stream().mapToInt(Block::getY2).max().orElseThrow();

		map = new char[(maxy - miny + 1) * (maxx - minx + 1)];
		Arrays.fill(map, SAND);
		blocks.forEach(this::addBlockToMap);
	}

	private void addBlockToMap(Block block) {
		for (int x = block.getX1(); x <= block.getX2(); x++) {
			for (int y = block.getY1(); y <= block.getY2(); y++) {
				set(new Coordinate(x, y), CLAY);
			}
		}
	}

	void print() {
		for (int y = miny; y <= maxy; y++) {
			for (int x = minx; x <= maxx; x++) {
				System.out.print(get(new Coordinate(x, y)));
			}
			System.out.println();
		}

		countBlocksOfType(CLAY);
		countBlocksOfType(SAND);
		countBlocksOfType(SOURCE);
		countBlocksOfType(FLOW);
		countBlocksOfType(WATER);
	}

	void fill() {
		set(new Coordinate(500, miny), SOURCE);
		fill(new Coordinate(500, miny+1));
	}

	private void fill(Coordinate coordinate) {
		if (get(coordinate) != SAND || isOutsideMap(coordinate)) return;

		fill(coordinate.down());

		if (isClayOrWater(get(coordinate.down()))) {
			Coordinate bassinBorderLeft = getBassinBorderLeft(coordinate);
			Coordinate bassinBorderRight = getBassinBorderRight(coordinate);
			if (bassinBorderLeft != null && bassinBorderRight != null) {
				for(Coordinate current = bassinBorderLeft.right(); !current.equals(bassinBorderRight); current = current.right()) {
					set(current, WATER);
				}

				return;
			}
		}

		set(coordinate, FLOW);
		if (isClayOrWater(get(coordinate.down()))) {
			fill(coordinate.left());
			fill(coordinate.right());
		} else {
			if (isClayOrWater(get(coordinate.left().down()))) fill(coordinate.left());
			if (isClayOrWater(get(coordinate.right().down()))) fill(coordinate.right());
		}
	}

	private boolean isClayOrWater(char material) {
		return material == WATER || material == CLAY;
	}

	private void countBlocksOfType(char type) {
		int result = 0;
		for (int y = miny; y <= maxy; y++) {
			for (int x = minx; x <= maxx; x++) {
				char material = get(new Coordinate(x, y));
				if (material == type) result++;
			}
		}

		System.out.printf("%s: %d\n", type, result);
	}

	private Coordinate getBassinBorderLeft(Coordinate coordinate) {
		Coordinate current = coordinate.left();
		while (current.getX() >= minx) {
			if (get(current) == CLAY) return current;
			if (!isClayOrWater(get(current.down()))) return null;
			current = current.left();
		}
		return null;
	}

	private Coordinate getBassinBorderRight(Coordinate coordinate) {
		Coordinate current = coordinate.right();
		while (current.getX() <= maxx) {
			if (get(current) == CLAY) return current;
			if (!isClayOrWater(get(current.down()))) return null;
			current = current.right();
		}
		return null;
	}

	private char get(Coordinate coordinate) {
		if (isOutsideMap(coordinate)) return SAND;
		return map[coord(coordinate.getX(), coordinate.getY())];
	}

	private boolean isOutsideMap(Coordinate coordinate) {
		return coordinate.getX() < minx || coordinate.getX() > maxx || coordinate.getY() < miny || coordinate.getY() > maxy;
	}

	private void set(Coordinate coordinate, char type) {
		map[coord(coordinate.getX(), coordinate.getY())] = type;
	}

	private int coord(int x, int y) {
		return (x - minx) + (maxx - minx + 1) * (y - miny);
	}

}
