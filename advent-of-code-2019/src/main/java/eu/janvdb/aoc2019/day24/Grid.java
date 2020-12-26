package eu.janvdb.aoc2019.day24;

import java.util.BitSet;

public class Grid extends AbstractGrid<Grid> {

	public Grid(String[] input) {
		super(input);
	}

	private Grid(BitSet bitSet) {
		super(bitSet);
	}

	@Override
	protected Grid createGrid(BitSet bitSet) {
		return new Grid(bitSet);
	}

	@Override
	protected boolean getValue(int row, int column) {
		if (row < 0 || column < 0 || row >= SIZE || column >= SIZE) return false;
		return bitSet.get(index(row, column));
	}

	public int calculateDiversity() {
		int power = 1;
		int result = 0;
		for (int row = 0; row < SIZE; row++) {
			for (int column = 0; column < SIZE; column++) {
				if (getValue(row, column)) result += power;
				power += power;
			}
		}
		return result;
	}
}
