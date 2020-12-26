package eu.janvdb.aoc2015.day18;

public class Puzzle {

	private static final String[] INPUT = Input.INPUT;
	private static final int SIZE = INPUT.length;

	public static void main(String[] args) {
		boolean[][] states = readInput();
		cornersOn(states);

		for (int i = 0; i < 100; i++) {
			states = getNextStates(states);
		}

		System.out.println(countStates(states));
	}

	private static boolean[][] readInput() {
		boolean[][] result = new boolean[SIZE][];
		for (int x = 0; x < SIZE; x++) {
			result[x] = new boolean[SIZE];
			for (int y = 0; y < SIZE; y++) {
				result[x][y] = INPUT[x].charAt(y) == '#';
			}
		}

		return result;
	}

	private static void print(boolean[][] states) {
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				System.out.print(states[x][y] ? '#' : '.');
			}
			System.out.println();
		}
		System.out.println();
	}

	private static boolean[][] getNextStates(boolean[][] states) {
		boolean[][] result = new boolean[SIZE][];
		for (int x = 0; x < SIZE; x++) {
			result[x] = new boolean[SIZE];
			for (int y = 0; y < SIZE; y++) {
				int numberOfNeighborsOn = numberOfNeighborsOn(states, x, y);
				result[x][y] = numberOfNeighborsOn == 3 || (numberOfNeighborsOn == 2 && states[x][y]);
			}
		}

		cornersOn(result);
		return result;
	}

	private static void cornersOn(boolean[][] states) {
		states[0][0] = true;
		states[0][SIZE - 1] = true;
		states[SIZE - 1][0] = true;
		states[SIZE - 1][SIZE - 1] = true;
	}

	private static int numberOfNeighborsOn(boolean[][] states, int x, int y) {
		int sum = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if ((i != 0 || j != 0) && getState(states, x + i, y + j)) {
					sum++;
				}
			}
		}
		return sum;
	}

	private static int countStates(boolean[][] states) {
		int sum = 0;
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				if (states[x][y]) {
					sum++;
				}
			}
		}

		return sum;
	}

	private static boolean getState(boolean[][] states, int x, int y) {
		return x >= 0 && y >= 0 && x < SIZE && y < SIZE && states[x][y];
	}

}
