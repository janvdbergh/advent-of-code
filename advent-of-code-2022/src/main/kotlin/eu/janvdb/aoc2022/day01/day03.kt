package eu.janvdb.aoc2022.day01

import eu.janvdb.aocutil.kotlin.readGroupedLines

//const val FILENAME = "input01-test.txt"
const val FILENAME = "input01.txt"

fun main() {
	part1()
	part2()
}

private fun part1() {
	val result = readGroupedLines(2022, FILENAME)
		.map { lines -> lines.map { it.toInt() }.sum() }
		.max()

	println(result)
}

private fun part2() {
	val result = readGroupedLines(2022, FILENAME)
		.map { lines -> lines.map { it.toInt() }.sum() }
		.sortedDescending()
		.take(3)
		.sum()

	println(result)
}