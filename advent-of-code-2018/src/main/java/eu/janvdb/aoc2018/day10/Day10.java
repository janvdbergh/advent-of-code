package eu.janvdb.aoc2018.day10;

import eu.janvdb.aoc2018.util.FileReader;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {

	public static void main(String[] args) throws IOException {
		List<MovingPoint> movingPoints = FileReader.readStringFile(Day10.class, "day10.txt").stream()
				.map(MovingPoint::new)
				.collect(Collectors.toList());

		Picture picture = new Picture(movingPoints);

		for (int i = 0; i < 100000; i++) {
			int size = picture.getSizeAt(i);
			if (size < 200) {
				System.out.printf("%5d %3d\n", i, size);
			}
		}

		int min = Integer.MAX_VALUE;
		int time = 0;
		while (true) {
			int size = picture.getSizeAt(time);
			if (size > min) break;

			min = size;
			time++;
		}

		System.out.println(time - 1);
		System.out.println(picture.getSizeAt(time - 1));
		picture.printAt(time - 1);
	}
}

