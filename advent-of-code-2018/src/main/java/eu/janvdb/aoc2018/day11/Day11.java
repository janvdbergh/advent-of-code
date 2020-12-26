package eu.janvdb.aoc2018.day11;

public class Day11 {

//	private final static int GRID_SERIAL_NUMBER = 42;
	private final static int GRID_SERIAL_NUMBER = 4151;

	private static final int TOTAL_SIZE = 300;
	private static final int[] POWERS = new int[TOTAL_SIZE * TOTAL_SIZE];

	public static void main(String[] args) {
		initializePowers();

		int bestX = -1;
		int bestY = -1;
		int bestSize = -1;
		int bestPower = Integer.MIN_VALUE;

		for (int size = 1; size < TOTAL_SIZE; size++) {
			for (int x = 1; x <= TOTAL_SIZE - size + 1; x++) {
				for (int y = 1; y <= TOTAL_SIZE - size + 1; y++) {
					int power = getPowerOfCellBlock(x, y, size);

					if (power > bestPower) {
						bestPower = power;
						bestX = x;
						bestY = y;
						bestSize = size;
					}
				}
			}
		}

		System.out.printf("Power %d at %d,%d,%d\n", bestPower, bestX, bestY, bestSize);
	}

	private static void initializePowers() {
		for (int x = 1; x <= TOTAL_SIZE; x++) {
			for (int y = 1; y <= TOTAL_SIZE; y++) {
				int rackId = x + 10;
				int power = getHundreds((rackId * y + GRID_SERIAL_NUMBER) * rackId) - 5;
				POWERS[getCoordinate(x, y)] = power;
			}
		}
	}

	private static int getHundreds(int number) {
		return number % 1000 / 100;
	}

	private static int getPowerOfCellBlock(int x, int y, int size) {
		int power = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				power += getPower(x + i, y + j);
			}
		}
		return power;
	}

	private static int getPower(int x, int y) {
		return POWERS[getCoordinate(x, y)];
	}

	private static int getCoordinate(int x, int y) {
		return (x - 1) + TOTAL_SIZE * (y - 1);
	}
}
