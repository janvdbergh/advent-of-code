package eu.janvdb.aoc2018.day13;

import eu.janvdb.aoc2018.util.FileReader;

import java.io.IOException;
import java.util.List;

public class Day13 {

	public static void main(String[] args) throws IOException {
		List<String> trackLines = FileReader.readStringFile(Day13.class, "day13.txt");
		Track track = new Track(trackLines);

		while (track.getCarts().size() > 1) {
			track.move();
		}

		Cart cart = track.getCarts().iterator().next();
		System.out.printf("Cart left at %d,%d.\n", cart.getX(), cart.getY());
	}
}

