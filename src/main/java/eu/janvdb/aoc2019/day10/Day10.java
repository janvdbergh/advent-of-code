package eu.janvdb.aoc2019.day10;

import eu.janvdb.util.Direction;
import eu.janvdb.util.Point2D;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

import java.util.Comparator;

public class Day10 {

	private static final String[] TEST_INPUT1_1 = {
			".#..#",
			".....",
			"#####",
			"....#",
			"...##"
	};

	private static final String[] TEST_INPUT1_2 = {
			"......#.#.",
			"#..#.#....",
			"..#######.",
			".#.#.###..",
			".#..#.....",
			"..#....#.#",
			"#..#....#.",
			".##.#..###",
			"##...#..#.",
			".#....####"
	};

	private static final String[] TEST_INPUT1_3 = {
			"#.#...#.#.",
			".###....#.",
			".#....#...",
			"##.#.#.#.#",
			"....#.#.#.",
			".##..###.#",
			"..#...##..",
			"..##....##",
			"......#...",
			".####.###."
	};

	private static final String[] TEST_INPUT1_4 = {
			".#..#..###",
			"####.###.#",
			"....###.#.",
			"..###.##.#",
			"##.##.#.#.",
			"....###..#",
			"..#.#..#.#",
			"#..#.#.###",
			".##...##.#",
			".....#.#.."
	};

	private static final String[] TEST_INPUT1_5 = {
			".#..##.###...#######",
			"##.############..##.",
			".#.######.########.#",
			".###.#######.####.#.",
			"#####.##.#.##.###.##",
			"..#####..#.#########",
			"####################",
			"#.####....###.#.#.##",
			"##.#################",
			"#####.##.###..####..",
			"..######..##.#######",
			"####.##.####...##..#",
			".#####..#.######.###",
			"##...#.##########...",
			"#.##########.#######",
			".####.#.###.###.#.##",
			"....##.##.###..#####",
			".#.#.###########.###",
			"#.#.#.#####.####.###",
			"###.##.####.##.#..##"
	};

	private static final String[] INPUT = {
			"#.....#...#.........###.#........#..",
			"....#......###..#.#.###....#......##",
			"......#..###.......#.#.#.#..#.......",
			"......#......#.#....#.##....##.#.#.#",
			"...###.#.#.......#..#...............",
			"....##...#..#....##....#...#.#......",
			"..##...#.###.....##....#.#..##.##...",
			"..##....#.#......#.#...#.#...#.#....",
			".#.##..##......##..#...#.....##...##",
			".......##.....#.....##..#..#..#.....",
			"..#..#...#......#..##...#.#...#...##",
			"......##.##.#.#.###....#.#..#......#",
			"#..#.#...#.....#...#...####.#..#...#",
			"...##...##.#..#.....####.#....##....",
			".#....###.#...#....#..#......#......",
			".##.#.#...#....##......#.....##...##",
			".....#....###...#.....#....#........",
			"...#...#....##..#.#......#.#.#......",
			".#..###............#.#..#...####.##.",
			".#.###..#.....#......#..###....##..#",
			"#......#.#.#.#.#.#...#.#.#....##....",
			".#.....#.....#...##.#......#.#...#..",
			"...##..###.........##.........#.....",
			"..#.#..#.#...#.....#.....#...###.#..",
			".#..........#.......#....#..........",
			"...##..#..#...#..#...#......####....",
			".#..#...##.##..##..###......#.......",
			".##.....#.......#..#...#..#.......#.",
			"#.#.#..#..##..#..............#....##",
			"..#....##......##.....#...#...##....",
			".##..##..#.#..#.................####",
			"##.......#..#.#..##..#...#..........",
			"#..##...#.##.#.#.........#..#..#....",
			".....#...#...#.#......#....#........",
			"....#......###.#..#......##.....#..#",
			"#..#...##.........#.....##.....#...."

	};
	private static final String[] TEST_INPUT2_1 = {
			".#....#####...#..",
			"##...##.#####..##",
			"##...#...#.#####.",
			"..#.....#...###..",
			"..#.#.....#....##"
	};

	public static void main(String[] args) {
		new Day10().run();
	}

	private void run() {
		run(TEST_INPUT1_1);
		run(TEST_INPUT1_2);
		run(TEST_INPUT1_3);
		run(TEST_INPUT1_4);
		run(TEST_INPUT1_5);
		run(TEST_INPUT2_1);
		run(INPUT);
	}

	private void run(String[] input) {
		List<Point2D> asteroids = readAsteroids(input);
		Point2D monitoringStation = findMonitoringStation(asteroids);
		fireAsteroids(asteroids, monitoringStation);
	}

	private List<Point2D> readAsteroids(String[] input) {
		return List.ofAll(
				Stream.range(0, input.length)
						.flatMap(y -> Stream.range(0, input[0].length()).map(x -> new Point2D(x, y)))
						.filter(point -> input[point.getY()].charAt(point.getX()) == '#')
		);
	}

	private Point2D findMonitoringStation(List<Point2D> asteroids) {
		Tuple2<Point2D, Integer> best = asteroids.toStream()
				.map(asteroid -> Tuple.of(asteroid, asteroidsInView(asteroids, asteroid)))
				.maxBy(Tuple2::_2)
				.getOrElseThrow(IllegalStateException::new);
		System.out.println(best);

		return best._1;
	}

	private int asteroidsInView(List<Point2D> asteroids, Point2D source) {
		return mapToRelativeLocations(asteroids, source)
				.groupBy(Tuple2::_2)
				.length();
	}

	private void fireAsteroids(List<Point2D> asteroids, Point2D monitoringStation) {
		mapToRelativeLocations(asteroids, monitoringStation)
				.groupBy(Tuple2::_2)
				.flatMap(tuple -> tuple._2.toStream()
						.reverse()
						.zipWithIndex()
						.map(tuple2 -> Tuple.of(tuple2._1._1, getWeight(tuple._1, tuple2._2)))
				)
				.sorted(Comparator.comparing(Tuple2::_2))
				.map(Tuple2::_1)
				.drop(199)
				.take(1)
				.forEach(System.out::println);
	}

	private double getWeight(Direction direction, Integer index) {
		return direction.getAngleInDegreesWithZeroOnTop() + 1000.0 * index;
	}

	private Stream<Tuple2<Point2D, Direction>> mapToRelativeLocations(List<Point2D> asteroids, Point2D source) {
		return Stream.ofAll(asteroids.toStream()
				.filter(destination -> !source.equals(destination))
				.map(destination -> Tuple.of(destination, destination.getDirectionFrom(source)))
		);
	}
}
