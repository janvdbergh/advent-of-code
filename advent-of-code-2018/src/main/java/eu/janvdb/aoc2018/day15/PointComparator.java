package eu.janvdb.aoc2018.day15;

import eu.janvdb.aocutil.java.Point2D;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

public class PointComparator implements Comparator<Point2D> {

	public static PointComparator INSTANCE = new PointComparator();

	@Override
	public int compare(Point2D p1, Point2D p2) {
		if (p1.getY() != p2.getY()) return p1.getY() - p2.getY();
		return p1.getX() - p2.getX();
	}

	public static <T> SortedMap<Point2D, T> newSortedMap() {
		return new TreeMap<>(INSTANCE);
	}
}
