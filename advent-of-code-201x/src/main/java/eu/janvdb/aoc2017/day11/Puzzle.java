package eu.janvdb.aoc2017.day11;

import eu.janvdb.util.Holder;
import eu.janvdb.util.InputReader;
import io.vavr.collection.Stream;

public class Puzzle {

	public static void main(String[] args) {
		part1();
		part2();
	}

	private static void part1() {
		Position finalPosition = streamPositions().last();
		System.out.println(finalPosition);
		System.out.println(finalPosition.getMinimalSteps());
	}

	private static void part2() {
		Position furthestPosition = streamPositions().maxBy(Position::getMinimalSteps).getOrElseThrow(IllegalArgumentException::new);
		System.out.println(furthestPosition);
		System.out.println(furthestPosition.getMinimalSteps());
	}

	private static Stream<Position> streamPositions() {
		String input = InputReader.readInputFully(Puzzle.class.getResource("input.txt"));

		Holder<Position> currentPositionHolder = new Holder<>(new Position());
		return Stream.of(input.split("\\s*,\\s*"))
				.map(direction -> currentPositionHolder.update(position -> position.step(direction)));
	}

}
