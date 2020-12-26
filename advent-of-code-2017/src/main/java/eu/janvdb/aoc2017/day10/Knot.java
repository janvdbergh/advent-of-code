package eu.janvdb.aoc2017.day10;

import io.vavr.collection.Vector;

public class Knot {

	private final Vector<Integer> numbers;
	private final int position;
	private final int skipSize;

	Knot(int size) {
		this.numbers = Vector.rangeBy(0, size, 1);
		this.position = 0;
		this.skipSize = 0;
	}

	private Knot(Vector<Integer> numbers, int position, int skipSize) {
		this.numbers = numbers;
		this.position = position;
		this.skipSize = skipSize;
	}

	Knot doStep(int length) {
		Vector<Integer> numbersAtPosition = numbers.slice(position, numbers.size()).appendAll(numbers.slice(0, position));
		Vector<Integer> nextNumbersAtPosition = numbersAtPosition.slice(0, length).reverse().appendAll(numbersAtPosition.slice(length, numbers.size()));
		Vector<Integer> nextNumbers = nextNumbersAtPosition.slice(numbers.size()-position, numbers.size()).appendAll(nextNumbersAtPosition.slice(0, numbers.size()-position));

		int nextPosition = (position + length + skipSize) % numbers.size();
		int nextSkipSize = skipSize + 1;

		return new Knot(nextNumbers, nextPosition, nextSkipSize);
	}

	public Vector<Integer> getNumbers() {
		return numbers;
	}

	@Override
	public String toString() {
		return String.valueOf(numbers);
	}
}
