package eu.janvdb.aoc2016.day13;

import static eu.janvdb.aoc2016.day13.Constants.*;
import static eu.janvdb.aoc2016.day13.Constants.NOT_CONNECTED;

import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		Map<Coord, Integer> bestLengths = HashMap.of(START, 0);
		int bestLength = NOT_CONNECTED;

		List<Coord> toDo = List.of(START);
		while (!toDo.isEmpty()) {
			Coord current = toDo.get(0);
			int currentLength = bestLengths.get(current).get();
			toDo = toDo.removeAt(0);

			if (currentLength < bestLength - 1) {
				Map<Coord, Integer> tempLengths = bestLengths;
				List<Coord> betterMoves = current.getMoves()
						.filter(move -> isBetterMove(currentLength, move, tempLengths));

				for (Coord move : betterMoves) {
					bestLengths = bestLengths.put(move, currentLength + 1);

					if (move.isDestination()) {
						bestLength = Math.min(bestLength, currentLength + 1);
					}
				}
				toDo = toDo.appendAll(betterMoves);
			}
		}

		System.out.println(bestLength);
	}

	private boolean isBetterMove(int currentLength, Coord move, Map<Coord, Integer> bestLengths) {
		int moveLength = bestLengths.get(move).getOrElse(NOT_CONNECTED);
		return currentLength + 1 < moveLength;
	}

}
