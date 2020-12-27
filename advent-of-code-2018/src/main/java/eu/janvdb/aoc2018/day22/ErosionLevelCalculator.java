package eu.janvdb.aoc2018.day22;

import eu.janvdb.aocutil.java.Point2D;

import java.util.HashMap;
import java.util.Map;

public class ErosionLevelCalculator {

	private static final Map<Point2D, Integer> EROSION_LEVEL_CACHE = new HashMap<>();

	public static int getErosionLevel(Point2D location) {
		if (!EROSION_LEVEL_CACHE.containsKey(location)) {
			EROSION_LEVEL_CACHE.put(location, calculateErosionLevel(location));
		}
		return EROSION_LEVEL_CACHE.get(location);
	}

	private static int calculateErosionLevel(Point2D point) {
		int x = point.getX();
		int y = point.getY();

		int geologicIndex;
		if (point.equals(Point2D.ORIGIN) || point.equals(Day22.TARGET)) {
			geologicIndex = 0;
		} else if (y == 0) {
			geologicIndex = x * 16807;
		} else if (x == 0) {
			geologicIndex = y * 48271;
		} else {
			int erosionLevel1 = getErosionLevel(new Point2D(x - 1, y));
			int erosionLevel2 = getErosionLevel(new Point2D(x, y - 1));
			geologicIndex = erosionLevel1 * erosionLevel2;
		}

		return (geologicIndex + Day22.DEPTH) % 20183;
	}
}
