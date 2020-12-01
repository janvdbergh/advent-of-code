package eu.janvdb.aoc2018.day19;

public class Simple {

	public static void main(String[] args) {
		int r0, r1, r2, r3, r5;

		r0 = 0;
		r5 = 10551339;

		for(r3 = 1; r3<=r5; r3++) {
			if (r5 % r3 == 0) r0 += r3;
		}

		System.out.println(r0);
	}
}
