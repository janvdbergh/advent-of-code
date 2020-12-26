package eu.janvdb.aoc2017.day17;

public class Puzzle {

	private static final int ITERATIONS1 = 2017;
	private static final int ITERATIONS2 = 50_000_000;
	private static final int STEPS = 344;

	public static void main(String[] args) {
		useSpinLock(ITERATIONS1);
		useLimitedState(ITERATIONS2);
	}

	private static void useSpinLock(int iterations) {
		SpinLock spinLock = new SpinLock(STEPS, iterations);
		for (int i = 0; i < iterations; i++) {
			spinLock.step();
		}

		System.out.println(spinLock.getValueAtNextPosition());
		System.out.println(spinLock.getValueAtPositionAfterZero());
	}

	private static void useLimitedState(int iterations) {
		// We keep 0 always at position 0 and insert new values after it
		// We keep the value of the element after 0. Initially this is 1.
		// Then we go over the next values and see where they belong.
		int valueAfterZero = 1;
		int currentPosition = 1;

		for (int value = 2; value <= iterations; value++) {
			currentPosition = (currentPosition + STEPS) % value + 1;
			if (currentPosition == 1) {
				valueAfterZero = value;
			}
		}

		System.out.println(valueAfterZero);
	}

}
