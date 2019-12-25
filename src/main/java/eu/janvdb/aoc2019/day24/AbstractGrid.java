package eu.janvdb.aoc2019.day24;

import java.util.BitSet;
import java.util.Objects;

public abstract class AbstractGrid<T extends AbstractGrid<T>> {

	public static final int SIZE = 5;

	protected final BitSet bitSet;

	protected AbstractGrid(String[] input) {
		BitSet result = new BitSet(SIZE * SIZE);
		for (int row = 0; row < SIZE; row++) {
			for (int column = 0; column < SIZE; column++) {
				result.set(index(row, column), input[row].charAt(column) == '#');
			}
		}

		this.bitSet = result;
	}

	protected AbstractGrid(BitSet bitSet) {
		this.bitSet = bitSet;
	}

	public T calculateNext() {
		BitSet newBits = new BitSet(SIZE * SIZE);
		for (int row = 0; row < SIZE; row++) {
			for (int column = 0; column < SIZE; column++) {
				int count = (getValue(row - 1, column) ? 1 : 0) +
						(getValue(row + 1, column) ? 1 : 0) +
						(getValue(row, column - 1) ? 1 : 0) +
						(getValue(row, column + 1) ? 1 : 0);
				boolean currentValue = getValue(row, column);
				boolean newValue = (currentValue && count == 1) || (!currentValue && (count == 1 || count == 2));
				newBits.set(index(row, column), newValue);
			}
		}

		return createGrid(newBits);
	}

	protected abstract T createGrid(BitSet bitSet);

	public void print() {
		for (int row = 0; row < SIZE; row++) {
			for (int column = 0; column < SIZE; column++) {
				System.out.print(getValue(row, column) ? '#' : '.');
			}
			System.out.println();
		}
		System.out.println();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractGrid<?> grid = this.getClass().cast(o);
		return bitSet.equals(grid.bitSet);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bitSet);
	}

	protected abstract boolean getValue(int row, int column);

	protected int index(int row, int column) {
		return row * SIZE + column;
	}
}
