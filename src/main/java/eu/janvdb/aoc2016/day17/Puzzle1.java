package eu.janvdb.aoc2016.day17;

import javaslang.collection.List;
import javaslang.control.Option;

import static eu.janvdb.aoc2016.day17.Position.START;

public class Puzzle1 {

	public static void main(String[] args) {
		new Puzzle1().execute();
	}

	private void execute() {
		List<Position> toTry = List.of(START);
		Option<Position> bestPosition = Option.none();

		while(!toTry.isEmpty()) {
			Position current = toTry.get(0);
			toTry = toTry.removeAt(0);

			Option<Position> currentBestPosition = bestPosition;
			List<Position> newPositions = current.getPossibleMoves()
					.filter(position -> currentBestPosition.isEmpty() || currentBestPosition.get().getPathLength()>position.getPathLength());
			toTry = toTry.appendAll(newPositions);

			bestPosition = newPositions.find(Position::isSolution).orElse(bestPosition);
		}

		System.out.println(bestPosition);
	}
}
