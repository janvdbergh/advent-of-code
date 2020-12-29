package eu.janvdb.aoc2019.day20;

import eu.janvdb.aocutil.java.FileReader;

import java.util.Map;

public class Day20 {

	public static final int MAX_DEPTH = 50;

	public static void main(String[] args) {
		Maze1 maze1 = new MazeParser(FileReader.readStringFile(Day20.class, "input.txt")).parse();
		Map<Portal, Map<Portal, Integer>> minimumDistances = maze1.getMinimumDistances();

		Maze2 maze2 = new Maze2(minimumDistances);
		System.out.println(maze2.getMinimumDistance1());
		System.out.println(maze2.getMinimumDistance2());
	}
}
