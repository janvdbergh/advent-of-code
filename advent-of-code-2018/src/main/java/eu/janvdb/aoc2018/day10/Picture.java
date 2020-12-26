package eu.janvdb.aoc2018.day10;

import java.util.List;

class Picture {

	private final List<MovingPoint> movingPoints;

	Picture(List<MovingPoint> movingPoints) {
		this.movingPoints = movingPoints;
	}

	int getSizeAt(int time) {
		return getMaxX(time) - getMinX(time) + getMaxY(time) - getMinY(time);
	}

	void printAt(int time) {
		int minY = getMinY(time);
		int maxY = getMaxY(time);
		int minX = getMinX(time);
		int maxX = getMaxX(time);
		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				int theX = x;
				int theY = y;
				boolean containsPoint = movingPoints.stream().anyMatch(point -> point.getXAt(time) == theX && point.getYAt(time) == theY);
				System.out.print(containsPoint ? "**" : "  ");
			}
			System.out.println();
		}
	}

	private int getMaxY(int time) {
		return movingPoints.stream().mapToInt(point -> point.getYAt(time)).max().orElseThrow();
	}

	private int getMinY(int time) {
		return movingPoints.stream().mapToInt(point -> point.getYAt(time)).min().orElseThrow();
	}

	private int getMaxX(int time) {
		return movingPoints.stream().mapToInt(point -> point.getXAt(time)).max().orElseThrow();
	}

	private int getMinX(int time) {
		return movingPoints.stream().mapToInt(point -> point.getXAt(time)).min().orElseThrow();
	}
}
