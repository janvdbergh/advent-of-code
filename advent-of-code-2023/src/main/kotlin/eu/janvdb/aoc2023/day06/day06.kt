package eu.janvdb.aoc2023.day06

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input06-test.txt"
const val FILENAME = "input06.txt"

fun main() {
	val lines = readLines(2023, FILENAME)
	part1(lines)
	part2(lines)
}

private fun part1(lines: List<String>) {
	val times = lines[0].split(":")[1].trim().split(Regex(" +")).map { it.toInt() }
	val distances = lines[1].split(":")[1].trim().split(Regex(" +")).map { it.toLong() }
	val puzzles = times.zip(distances).map { (time, distance) -> Puzzle(time, distance) }

	val totalWinningStarts = puzzles.map { it.numberOfWinningStarts() }.fold(1L) { acc, x -> acc * x }

	println(totalWinningStarts)
}

private fun part2(lines: List<String>) {
	val time = lines[0].split(":")[1].replace(Regex(" "), "").toInt()
	val distance = lines[1].split(":")[1].replace(Regex(" "), "").toLong()
	val puzzle = Puzzle(time, distance)

	println(puzzle.numberOfWinningStarts())
}

data class Puzzle(val time: Int, val winningDistance: Long) {
	fun numberOfWinningStarts(): Int {
		return (0..time).count { isWinningStart(it) }
	}

	private fun isWinningStart(startTime: Int): Boolean {
		val distance = 1L * (time - startTime) * startTime
		return distance > winningDistance
	}
}
