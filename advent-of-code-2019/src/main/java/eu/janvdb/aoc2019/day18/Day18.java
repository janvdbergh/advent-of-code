package eu.janvdb.aoc2019.day18;

import eu.janvdb.aocutil.java.FileReader;

import java.util.List;
import java.util.Map;

public class Day18 {

	public static void main(String[] args) {
//		solveMaze("input_test5.txt");
		solveMaze("input.txt");
		solveMaze("input2.txt");
	}

	private static void solveMaze(String fileName) {
		List<String> lines = FileReader.readStringFile(Day18.class, fileName);
		Maze1 maze1 = Maze1.parse(lines);
		Map<MapLocation, Map<MapLocation, Integer>> distances = maze1.getDistances();

		Maze2 maze2 = new Maze2(distances);
		System.out.println(maze2.getTotalDistance());
	}
}
