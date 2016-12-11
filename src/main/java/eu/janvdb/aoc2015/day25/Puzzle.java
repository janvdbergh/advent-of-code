package eu.janvdb.aoc2015.day25;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		int column = 1;
		int row = 1;
		long value = 20151125L;

		while (!(row == 3010 && column==3019)) {
			if (row == 1) {
				row = column + 1;
				column = 1;
			} else {
				row--;
				column++;
			}

			value = value * 252533L % 33554393L;
		}

		System.out.printf("row=%d, column=%d, value=%d\n", row, column, value);
	}
}
