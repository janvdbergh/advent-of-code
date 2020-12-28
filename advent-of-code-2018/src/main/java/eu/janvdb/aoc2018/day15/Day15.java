package eu.janvdb.aoc2018.day15;

import eu.janvdb.aocutil.java.FileReader;

import java.io.IOException;

public class Day15 {

	public static void main(String[] args) throws IOException {
		// part 1
		runAndReportDeadElves();

		// part 2
		while (runAndReportDeadElves() != 0) {
			ActorType.ELF.setAttackPower(ActorType.ELF.getAttackPower() + 1);
		}
		System.out.println(ActorType.ELF.getAttackPower());

		// != 10
	}

	private static int runAndReportDeadElves() {
		Maze maze = Maze.parse(FileReader.readStringFile(Day15.class, "day15.txt"));
		int initialNumberOfElves = maze.getElves().size();
		maze.print();

		while (!maze.isFinished()) {
			maze.executeTurn();
			maze.print();
		}
		System.out.println(maze.getScore());

		int remainingNumberOfElves = maze.getElves().size();
		return remainingNumberOfElves - initialNumberOfElves;
	}

}
