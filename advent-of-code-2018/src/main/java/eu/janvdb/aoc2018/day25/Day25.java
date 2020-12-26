package eu.janvdb.aoc2018.day25;

import eu.janvdb.aoc2018.util.FileReader;
import eu.janvdb.aoc2018.util.Point4D;

import java.io.IOException;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Day25 {

	public static void main(String[] args) throws IOException {
		LinkedList<Constellation> currentConstellations = FileReader.readStringFile(Day25.class, "day25.txt").stream()
				.map(Day25::readPoint)
				.map(Constellation::new).collect(Collectors.toCollection(LinkedList::new));


		boolean continueMerging = true;
		while (continueMerging) {
			continueMerging = false;
			LinkedList<Constellation> newConstellations = new LinkedList<>();

			while (!currentConstellations.isEmpty()) {
				Constellation current = currentConstellations.removeFirst();

				LinkedList<Constellation> remainingConstellations = new LinkedList<>();
				while (!currentConstellations.isEmpty()) {
					Constellation constellation = currentConstellations.removeFirst();
					if (current.canMergeWith(constellation)) {
						current.mergeWith(constellation);
						continueMerging = true;
					} else {
						remainingConstellations.addLast(constellation);
					}
				}

				newConstellations.addLast(current);
				currentConstellations = remainingConstellations;
			}

			currentConstellations = newConstellations;
		}

		System.out.println(currentConstellations.size());
	}

	private static Point4D readPoint(String line) {
		String[] parts = line.split("\\s*,\\s*");
		return new Point4D(
				Integer.parseInt(parts[0]),
				Integer.parseInt(parts[1]),
				Integer.parseInt(parts[2]),
				Integer.parseInt(parts[3])
		);
	}
}
