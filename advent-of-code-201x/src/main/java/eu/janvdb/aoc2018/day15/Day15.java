package eu.janvdb.aoc2018.day15;

import eu.janvdb.aoc2018.util.FileReader;

import java.io.IOException;

public class Day15 {

	public static void main(String[] args) throws IOException {
		Map map = new Map(FileReader.readStringFile(Day15.class, "day15_test.txt"));
		map.print();
	}

}
