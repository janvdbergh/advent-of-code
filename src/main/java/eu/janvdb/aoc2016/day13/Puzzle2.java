package eu.janvdb.aoc2016.day13;

import static eu.janvdb.aoc2016.day13.Constants.MAX_STEPS;
import static eu.janvdb.aoc2016.day13.Constants.NOT_CONNECTED;
import static eu.janvdb.aoc2016.day13.Constants.START;

import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;

public class Puzzle2 {

	public static void main(String[] args) {
		new Puzzle2().execute();
	}

	private void execute() {
		Map<Coord, Integer> bestLengths = HashMap.of(START, 0);

		List<Coord> toDo = List.of(START);
		while (!toDo.isEmpty()) {
			Coord current = toDo.get(0);
			int currentLength = bestLengths.get(current).get();
			toDo = toDo.removeAt(0);

			if (currentLength < MAX_STEPS) {
				Map<Coord, Integer> tempLengths = bestLengths;
				List<Coord> betterMoves = current.getMoves()
						.filter(move -> isBetterMove(currentLength, move, tempLengths));

				for (Coord move : betterMoves) {
					bestLengths = bestLengths.put(move, currentLength + 1);
				}
				toDo = toDo.appendAll(betterMoves);
			}
		}

		int count = bestLengths.count(coordIntegerTuple2 -> coordIntegerTuple2._2 <= MAX_STEPS);
		System.out.println(count);

		bestLengths.forEach((coord, length) -> System.out.println(coord + ": " + length + " - " + coord.isOpenSpace()));
	}

	private boolean isBetterMove(int currentLength, Coord move, Map<Coord, Integer> bestLengths) {
		int moveLength = bestLengths.get(move).getOrElse(NOT_CONNECTED);
		return currentLength + 1 < moveLength;
	}

}
