import eu.janvdb.aoc2023.day03.Puzzle
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input03-test.txt"
const val FILENAME = "input03.txt"

fun main() {
	val puzzle = Puzzle(readLines(2023, FILENAME))

	println(puzzle.sumOfAdjacentNumbers())
	println(puzzle.getSumOfGearRatios())
}