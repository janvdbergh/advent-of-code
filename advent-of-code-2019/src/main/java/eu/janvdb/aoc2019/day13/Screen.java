package eu.janvdb.aoc2019.day13;

import eu.janvdb.aoc2019.common.ReactiveComputer;
import eu.janvdb.aocutil.java.Point2D;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

import java.util.function.Predicate;

public class Screen {

	private Map<Point2D, Tile> values = HashMap.empty();
	private int score;

	public Screen(ReactiveComputer computer) {
		connectComputer(computer);
	}

	private void connectComputer(ReactiveComputer computer) {
		computer.getOutput()
				.buffer(3)
				.subscribe(values -> write(values.get(0).intValue(), values.get(1).intValue(), values.get(2).intValue()));
	}

	public void write(int x, int y, int value) {
		if (x == -1 && y == 0) {
			this.score = value;
		} else {
			Point2D point = new Point2D(x, y);
			Tile tile = Tile.findByValue(value);
			if (tile == Tile.EMPTY) {
				values = values.remove(point);
			} else {
				values = values.put(point, tile);
			}
		}
	}

	public int countMatching(Predicate<Tile> matcher) {
		return values.values().count(matcher);
	}

	public void print() {
		System.out.printf("Score: %d\n", score);
		int minX = values.keySet().toStream().map(Point2D::getX).min().getOrElse(0);
		int maxX = values.keySet().toStream().map(Point2D::getX).max().getOrElse(0);
		int minY = values.keySet().toStream().map(Point2D::getY).min().getOrElse(0);
		int maxY = values.keySet().toStream().map(Point2D::getY).max().getOrElse(0);

		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				System.out.print(values.get(new Point2D(x, y)).getOrElse(Tile.EMPTY).getDisplayChar());
			}
			System.out.println();
		}
	}

	public Point2D find(Tile value) {
		return values.find(entry -> entry._2 == value).map(Tuple2::_1).get();
	}
}
