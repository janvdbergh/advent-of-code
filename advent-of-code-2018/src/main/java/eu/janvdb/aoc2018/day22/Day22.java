package eu.janvdb.aoc2018.day22;

import eu.janvdb.aocutil.java.Point2D;

public class Day22 {

//	public static final Point2D TARGET = new Point2D(10, 10);
//	public static final int DEPTH = 510;
	public static final Point2D TARGET = new Point2D(12, 757);
	public static final int DEPTH = 3198;

	public static void main(String[] args) {
		Maze maze = new Maze(TARGET);
		maze.print();

		System.out.println(maze.getRiskLevel());
		// < 1045
		System.out.println(maze.getFastestTimeToTarget());
	}
}