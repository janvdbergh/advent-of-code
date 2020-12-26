package eu.janvdb.aoc2017.day23;

public class ProgramInJava {

	public static void main(String[] args) {
		runProgram(false);
		runProgram(true);
	}

	private static void runProgram(boolean debug) {
		long numMuls = 0;
		long h = 0;

		long b, c;
		if (!debug) {
			b = 67;
			c = b;
		} else {
			numMuls++;
			b = 106700;
			c = b + 17000;
		}

		while (true) {
			numMuls += (b - 2) * (b - 2);
			if (!isPrime(b)) h++;

			if (b - c == 0) break;
			b += 17;
		}

		System.out.println(numMuls);
		System.out.println(h);
	}

	private static boolean isPrime(long number) {
		for (long i = 2; 2 * i < number; i++) {
			if (number % i == 0)
				return false;
		}
		return true;
	}
}
