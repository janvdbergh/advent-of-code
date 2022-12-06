package eu.janvdb.aoc2022.day06

import eu.janvdb.aocutil.kotlin.readFile

const val FILENAME = "input06.txt"

fun main() {
	val input = readFile(2022, FILENAME)
	run(input, 4)
	run(input, 14)
}

private fun run(input: String, windowSize: Int) {
	// Test cases
	println(getStartPosition("mjqjpqmgbljsphdztnvjfqwrcgsmlb", windowSize)) // 7, 19
	println(getStartPosition("bvwbjplbgvbhsrlpgdmjqwftvncz", windowSize)) // 5, 23
	println(getStartPosition("nppdvjthqldpwncqszvftbrmjlhg", windowSize)) // 6, 23
	println(getStartPosition("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", windowSize)) // 10, 29
	println(getStartPosition("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", windowSize)) // 11, 26

	// Actual case
	println(getStartPosition(input, windowSize))
	println()
}

fun getStartPosition(input: String, windowSize: Int): Int {
	for (i in 0..input.length - windowSize) {
		val window = input.substring(i, i + windowSize)
		val differentChars = window.toCharArray().toSet().size
		if (differentChars == windowSize) {
			return i + windowSize
		}
	}

	return -1
}