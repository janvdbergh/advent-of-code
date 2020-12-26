package eu.janvdb.aoc2017.day14;

import static eu.janvdb.aoc2017.day14.DataGrid.NOT_IN_GROUP;
import static eu.janvdb.aoc2017.day14.DataGrid.SIZE;

public class Puzzle {

	private static final String INPUT1 = "jzgqcdpd";
	private static final String INPUT = INPUT1;

	public static void main(String[] args) {
		DataGrid dataGrid = new DataGrid(INPUT);
		System.out.println(dataGrid.countSetBits());

		DataGrid markedDataGrid = markGroups(dataGrid);
		System.out.println(markedDataGrid.getMaxGroup());
	}

	private static DataGrid markGroups(DataGrid currentGrid) {
		int currentGroup = 1;
		for (int row = 0; row < SIZE; row++) {
			for (int column = 0; column < SIZE; column++) {
				if (currentGrid.getBit(row, column) == NOT_IN_GROUP) {
					currentGrid = currentGrid.markGroup(row, column, currentGroup);
					currentGroup++;
				}
			}
		}

		return currentGrid;
	}

}
