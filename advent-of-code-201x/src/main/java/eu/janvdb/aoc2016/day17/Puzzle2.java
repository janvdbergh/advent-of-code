package eu.janvdb.aoc2016.day17;

import io.vavr.collection.List;
import io.vavr.control.Option;

import static eu.janvdb.aoc2016.day17.Position.START;

public class Puzzle2 {

	public static void main(String[] args) {
		new Puzzle2().execute();
	}

	private void execute() {
		List<Position> toTry = List.of(START);
		Option<Position> longestPosition = Option.none();

		while(!toTry.isEmpty()) {
			Position current = toTry.get(0);
			toTry = toTry.removeAt(0);

			List<Position> newPositions = current.getPossibleMoves();
			toTry = toTry.appendAll(
					newPositions.filter(position -> !position.isSolution())
			);

			Option<Position> currentLongestPosition = longestPosition;
			longestPosition = newPositions
					.filter(Position::isSolution)
					.find(position -> currentLongestPosition.isEmpty() || currentLongestPosition.get().getPathLength()<position.getPathLength())
					.orElse(currentLongestPosition);
		}

		System.out.println(longestPosition);
	}
}
