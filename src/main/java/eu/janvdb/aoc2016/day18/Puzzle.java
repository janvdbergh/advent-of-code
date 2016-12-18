package eu.janvdb.aoc2016.day18;

public class Puzzle {

	private static final String INPUT0 = ".^^.^.^^^^";
	private static final int ROWS0 = 10;

	private static final String INPUT1 = "......^.^^.....^^^^^^^^^...^.^..^^.^^^..^.^..^.^^^.^^^^..^^.^.^.....^^^^^..^..^^^..^^.^.^..^^..^^^..";
	private static final int ROWS1 = 400000;

	private static final String INPUT = INPUT1;
	private static final int ROWS = ROWS1;

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		Row currentRow = Row.fromInput(INPUT);
		int count = currentRow.countSafeTiles();

		for(int i=1; i<ROWS; i++) {
			currentRow = currentRow.getNextRow();
			count += currentRow.countSafeTiles();
		}

		System.out.println(count);
	}
}
