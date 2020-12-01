package eu.janvdb.aoc2016.day18;

public class Row {

	private final boolean[] safeTiles;

	public static Row fromInput(String input) {
		boolean[] safeTiles = new boolean[input.length()];
		for(int i=0; i<input.length(); i++) {
			safeTiles[i] = input.charAt(i) == '.';
		}

		return new Row(safeTiles);
	}

	private Row(boolean[] safeTiles) {
		this.safeTiles = safeTiles;
	}

	public Row getNextRow() {
		boolean[] resultTiles = new boolean[safeTiles.length];
		for(int i=0; i<safeTiles.length; i++) {
			resultTiles[i] = isSafeOnNextRowAt(i);
		}

		return new Row(resultTiles);
	}

	private boolean isSafeOnNextRowAt(int i) {
		boolean leftSafe = i==0 || safeTiles[i-1];
		boolean centerSafe = safeTiles[i];
		boolean rightSafe = i==safeTiles.length-1 || safeTiles[i+1];

		return (leftSafe && rightSafe) || (!leftSafe && !rightSafe);
	}

	public int countSafeTiles() {
		int count = 0;
		for (boolean safeTile : safeTiles) {
			if (safeTile) {
				count++;
			}
		}
		return count;
	}
}
