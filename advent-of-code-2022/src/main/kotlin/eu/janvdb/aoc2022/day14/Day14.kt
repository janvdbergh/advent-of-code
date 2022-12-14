package eu.janvdb.aoc2022.day14

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input14-test.txt"
const val FILENAME = "input14.txt"

fun main() {
	val instructions = readLines(2022, FILENAME)
		.map(Instruction::parse)

	val maze1 = Maze.parse(instructions, false)
	runForMaze(maze1)

	val maze2 = Maze.parse(instructions, true)
	runForMaze(maze2)
}

private fun runForMaze(maze: Maze) {
	var count = 0
	while (maze.dropSand()) count++

	println(maze)
	println(count)
}