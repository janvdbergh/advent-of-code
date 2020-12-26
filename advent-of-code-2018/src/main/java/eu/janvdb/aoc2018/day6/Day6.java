package eu.janvdb.aoc2018.day6;

import eu.janvdb.aoc2018.util.Coordinate;
import eu.janvdb.aoc2018.util.FileReader;

import java.io.IOException;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6 {

	private static final int MAX_DISTANCE = 10000;

	public static void main(String[] args) throws IOException {
		List<Coordinate> coordinates = FileReader.readStringFile(Day6.class, "day6.txt").stream()
				.map(Coordinate::new)
				.collect(Collectors.toList());

		int minx = reduce(coordinates, Coordinate::getX, IntStream::min) - 1;
		int maxx = reduce(coordinates, Coordinate::getX, IntStream::max) + 1;
		int miny = reduce(coordinates, Coordinate::getY, IntStream::min) - 1;
		int maxy = reduce(coordinates, Coordinate::getY, IntStream::max) + 1;

		part1(coordinates, minx, maxx, miny, maxy);
		part2(coordinates, minx, maxx, miny, maxy);
	}

	private static int reduce(List<Coordinate> coordinates, ToIntFunction<Coordinate> mapper, Function<IntStream, OptionalInt> reducer) {
		return reducer.apply(coordinates.stream().mapToInt(mapper)).orElseThrow();
	}

	private static void part1(List<Coordinate> coordinates, int minx, int maxx, int miny, int maxy) {
		Grid grid = new Grid(minx, maxx, miny, maxy);

		for (int x = minx; x <= maxx; x++) {
			for (int y = miny; y <= maxy; y++) {
				Coordinate current = new Coordinate(x, y);
				int minDistance = Integer.MAX_VALUE;
				int minIndex = 0;
				int count = 0;

				for (int index = 0; index < coordinates.size(); index++) {
					Coordinate coordinate = coordinates.get(index);
					int distance = coordinate.manhattanDistance(current);
					if (distance < minDistance) {
						minDistance = distance;
						minIndex = index;
						count = 1;
					} else if (distance == minDistance) {
						count++;
					}
				}

				grid.set(x, y, count == 1 ? minIndex : -1);
			}
		}

		coordinates.stream()
				.filter(coordinate -> !grid.isOnEdge(coordinate))
				.mapToInt(grid::getCountForThis)
				.max()
				.ifPresent(System.out::println);
	}

	private static void part2(List<Coordinate> coordinates, int minx, int maxx, int miny, int maxy) {
		int count = 0;
		for (int x = -MAX_DISTANCE + minx; x <= MAX_DISTANCE + maxx; x++) {
			for (int y = -MAX_DISTANCE + miny; y <= MAX_DISTANCE + maxy; y++) {
				Coordinate current = new Coordinate(x, y);
				int totalDistance = coordinates.stream()
						.mapToInt(coordinate -> coordinate.manhattanDistance(current))
						.sum();
				if (totalDistance < MAX_DISTANCE) count++;
			}
		}

		System.out.println(count);
	}
}

