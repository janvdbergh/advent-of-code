package eu.janvdb.aoc2017.day19;

import eu.janvdb.aocutil.java.InputReader;

public class Puzzle {

	public static void main(String[] args) {
		Maze maze = new Maze(InputReader.readInput(Puzzle.class.getResource("input.txt")).toList());

		Position currentPosition = maze.getStart();
		Position previousPosition = currentPosition.up();
		int steps = 0;
		while(maze.getNode(currentPosition) != NodeType.NONE) {
			maze.getLetter(currentPosition).forEach(System.out::print);

			Position nextPosition = maze.getNextPosition(previousPosition, currentPosition);
			previousPosition = currentPosition;
			currentPosition = nextPosition;
			steps++;
		}

		System.out.printf("\nExited at %s after %d steps.\n", currentPosition, steps);
	}

}
