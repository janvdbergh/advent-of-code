package eu.janvdb.aocutil.java;

import java.util.stream.IntStream;

public class MathUtil {

	public static long kgv(long a, long b) {
		return a * b / gcd(a, b);
	}

	public static long gcd(long a, long b) {
		a = Math.abs(a);
		b = Math.abs(b);
		while (b != 0) {
			long remainder = a % b;
			a = b;
			b = remainder;
		}
		return a;
	}

	public static int max(int... numbers) {
		return IntStream.of(numbers).max().orElseThrow();
	}
}
