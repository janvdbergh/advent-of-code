package eu.janvdb.aoc2021.day01

import eu.janvdb.aocutil.kotlin.readLines

val FILENAME = "input01.txt"

fun main() {
	part1()
	part2()
}

private fun part1() {
	val count = readLines(2021, FILENAME).asSequence()
		.map { Integer.parseInt(it) }
		.windowed(2)
		.count { it[0] < it[1] }
	println(count)
}

private fun part2() {
	val count = readLines(2021, FILENAME).asSequence()
		.map { Integer.parseInt(it) }
		.windowed(3)
		.map { it.sum() }
		.windowed(2)
		.count { it[0] < it[1] }
	println(count)
}