package eu.janvdb.aoc2018.day9;

import java.util.ArrayList;
import java.util.List;

public class Day9 {

	private static final int NUMBER_OF_ELVES = 448;
	private static final int NUMBER_OF_MARBLES = 7162800;

	public static void main(String[] args) {
		MarbleCircle marbleCircle = new MarbleCircle();
//		System.out.println(marbleCircle);

		List<Long> scores = new ArrayList<>();
		for(int i=0; i<NUMBER_OF_ELVES; i++) scores.add(0L);

		int currentElf = 0;
		for(int i = 0; i < NUMBER_OF_MARBLES; i++) {
			scores.set(currentElf, scores.get(currentElf) + marbleCircle.addMarbleAndReturnScore());
			currentElf = (currentElf + 1) % NUMBER_OF_ELVES;
		}

		long maxScore = scores.stream().mapToLong(x -> x).max().orElseThrow();
		System.out.println(maxScore);
	}
}

