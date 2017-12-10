package eu.janvdb.aoc2016.day15;

import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.control.Option;

public class Puzzle {

	private static final List<Integer> NUMBER_OF_POSITIONS = List.of(17, 3, 19, 13, 7, 5, 11);
	private static final List<Integer> INITIAL_POSITIONS = List.of(15, 2, 4, 2, 2, 0, 0);
//	private static final List<Integer> NUMBER_OF_POSITIONS = List.of(5,2 );
//	private static final List<Integer> INITIAL_POSITIONS = List.of(4, 1);
	private static final int NUMBER_OF_DISKS = NUMBER_OF_POSITIONS.size();

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		List<Option<Integer>> startTimeAtDiskPositions = List.of(Option.of(0));
		List<Integer> positions = INITIAL_POSITIONS;

		int max = NUMBER_OF_POSITIONS.product().intValue();

		int currentTime = 1;
		while (startTimeAtDiskPositions.size() <= NUMBER_OF_DISKS || startTimeAtDiskPositions.get(NUMBER_OF_DISKS).isEmpty()) {
			positions = rotateDisks(positions);
			startTimeAtDiskPositions = insertTimeAndMoveDisks(startTimeAtDiskPositions, positions, currentTime);

			currentTime++;
		}

		System.out.println(startTimeAtDiskPositions.get(NUMBER_OF_DISKS));
	}

	private List<Integer> rotateDisks(List<Integer> positions) {
		return positions.zip(NUMBER_OF_POSITIONS)
				.map(tuplePositionNumberOfPositions -> (tuplePositionNumberOfPositions._1 + 1) % tuplePositionNumberOfPositions._2);
	}

	private List<Option<Integer>> insertTimeAndMoveDisks(List<Option<Integer>> startTimeAtDiskPositions, List<Integer> positions, Integer currentTime) {
		return startTimeAtDiskPositions.toStream()
				.zip(positions)
				.insert(0, Tuple.of(Option.of(currentTime), 0))
				.map(tupleStartTimePosition -> tupleStartTimePosition._2 == 0 ? tupleStartTimePosition._1 : Option.<Integer>none())
				.take(NUMBER_OF_DISKS + 2)
				.toList();
	}

}
