package eu.janvdb.aoc2019.day18;

import eu.janvdb.aocutil.java.FileReader;

import java.util.List;
import java.util.Map;

public class Day18 {

	public static void main(String[] args) {
		List<String> lines = FileReader.readStringFile(Day18.class, "input.txt");
		Maze1 maze1 = Maze1.parse(lines);
		Map<Character, Map<Character, Integer>> distances = maze1.getDistances();

		Maze2 maze2 = new Maze2(distances);
		System.out.println(maze2.getTotalDistance());
	}

}
