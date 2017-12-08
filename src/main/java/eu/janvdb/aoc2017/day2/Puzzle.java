package eu.janvdb.aoc2017.day2;

import eu.janvdb.util.InputReader;
import javaslang.Tuple;
import javaslang.collection.List;
import javaslang.collection.Stream;

public class Puzzle {

	public static void main(String[] args) {
		Number result1 = InputReader.readInput(Puzzle.class.getResource("input.txt"))
				.map(line -> List.of(line.split("\\s+")).map(Integer::parseInt))
				.map(items -> items.max().getOrElse(0) - items.min().getOrElse(0))
				.sum();

		System.out.println(result1);

		Number result2 = InputReader.readInput(Puzzle.class.getResource("input.txt"))
				.map(line -> List.of(line.split("\\s+")).map(Integer::parseInt))
				.flatMap(Puzzle::getDividers)
				.sum();

		System.out.println(result2);
	}

	private static Stream<Integer> getDividers(List<Integer> values) {
		return values.toStream()
				.combinations(2)
				.map(items -> Tuple.of(items.get(0), items.get(1)))
				.map(tuple -> tuple._1 > tuple._2 ? Tuple.of(tuple._2, tuple._1) : tuple)
				.filter(tuple -> tuple._2 % tuple._1 == 0)
				.map(tuple -> tuple._2 / tuple._1);
	}

}
