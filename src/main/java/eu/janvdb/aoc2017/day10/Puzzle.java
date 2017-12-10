package eu.janvdb.aoc2017.day10;

public class Puzzle {

	private static final int[] LENGTHS = {199, 0, 255, 136, 174, 254, 227, 16, 51, 85, 1, 2, 22, 17, 7, 192};
	private static final String INPUT = "199,0,255,136,174,254,227,16,51,85,1,2,22,17,7,192";

	public static void main(String[] args) {
		Knot knot = new Knot(256);

		for (int length : LENGTHS) {
			knot = knot.doStep(length);
		}

		System.out.println(knot);
		System.out.println(knot.getNumbers().get(0) * knot.getNumbers().get(1));

		System.out.println(new Knotter().knotString(INPUT));
	}
}
