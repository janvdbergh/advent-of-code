package eu.janvdb.aoc2020.day03

import eu.janvdb.aocutil.kotlin.readLines

fun main() {
	val lines = readLines("input03.txt")
	val width = lines[0].length
	val height = lines.size

	fun isTree(down: Int, right: Int): Boolean {
		if (down < 0 || down >= height || right < 0) return false
		return lines[down][right % width] == '#'
	}

	fun checkSlope(stepsDown: Int, stepsRight: Int): Int {
		var right = 0
		var count = 0
		for (down in 0..height + 1 step stepsDown) {
			if (isTree(down, right)) count++
			right += stepsRight
		}
		return count
	}

	println(checkSlope(1, 3))

	/*
	Right 1, down 1.
	Right 3, down 1. (This is the slope you already checked.)
	Right 5, down 1.
	Right 7, down 1.
	Right 1, down 2.
	 */
	val slope1 = checkSlope(1, 1)
	val slope2 = checkSlope(1, 3)
	val slope3 = checkSlope(1, 5)
	val slope4 = checkSlope(1, 7)
	val slope5 = checkSlope(2, 1)
	println(1L * slope1 * slope2 * slope3 * slope4 * slope5)
}