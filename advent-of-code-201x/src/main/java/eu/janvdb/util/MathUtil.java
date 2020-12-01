package eu.janvdb.util;

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
}
