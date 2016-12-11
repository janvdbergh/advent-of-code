package eu.janvdb.aoc2016.day3;

import static eu.janvdb.aoc2016.day3.Input.INPUT;

import java.util.Arrays;

public class Puzzle {

	public static void main(String[] args) {
		int count = 0;
		for(int i=0; i<INPUT.length; i++) {
			if (isValidTriangleAt(i/3*3, i%3)) {
				count++;
			}
		}

		System.out.println(count);
	}

	private static boolean isValidTriangleAt(int x, int y) {
		int[] lengths = new int[] { INPUT[x][y], INPUT[x+1][y], INPUT[x+2][y]};
		return isValidTriangle(lengths);
	}

	private static boolean isValidTriangle(int[] lengths) {
		Arrays.sort(lengths);
		return lengths[0] + lengths[1] > lengths[2];
	}
}
