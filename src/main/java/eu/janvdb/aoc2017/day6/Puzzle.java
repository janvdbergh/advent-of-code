package eu.janvdb.aoc2017.day6;

import io.vavr.collection.List;
import io.vavr.collection.Vector;

public class Puzzle {

	private static final Integer[] INPUT0 = {0, 2, 7, 0};
	private static final Integer[] INPUT1 = {14, 0, 15, 12, 11, 11, 3, 5, 1, 6, 8, 4, 9, 1, 8, 4};

	private static final Integer[] INPUT = INPUT1;

	public static void main(String[] args) {
		Vector<Integer> current = Vector.of(INPUT);
		System.out.println(current);

		List<Vector<Integer>> states = List.empty();
		while(!states.contains(current)) {
			states = states.append(current);
			current = redistribute(current);
			System.out.println(current);
		}

		System.out.println(states.length());
		System.out.println(states.length() - states.indexOf(current));
	}

	private static Vector<Integer> redistribute(Vector<Integer> current) {
		Integer maxValue = current.max().getOrElseThrow(IllegalArgumentException::new);
		int indexToRedistribute = current.indexOf(maxValue);

		Vector<Integer> result = current.update(indexToRedistribute, 0);
		int index = (indexToRedistribute + 1) % current.length();
		for(int i=0; i<maxValue; i++) {
			result = result.update(index, result.get(index)+1);
			index = (index+1) % current.length();
		}

		return result;
	}
}
