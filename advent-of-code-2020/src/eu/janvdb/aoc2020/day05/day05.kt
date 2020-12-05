package eu.janvdb.aoc2020.day05

import java.io.File

// Format: [FB]{8}[LR]{3} with B and R meaning 1
private val REGEX_ZEROES = Regex("[FL]", RegexOption.IGNORE_CASE)
val REGEX_ONES = Regex("[BR]", RegexOption.IGNORE_CASE)

fun main() {
	val list = File("inputs/input05.txt").readLines()
			.map (::getSeatNumber)
			.sorted()

	val max = list.maxOf { it }
	val min = list.minOf { it }
	println(max)

	IntRange(min + 1, max)
			.filter { !list.contains(it) }
			.forEach(::println)
}

fun getSeatNumber(value: String): Int {
	val binaryString = value.replace(REGEX_ONES, "1").replace(REGEX_ZEROES, "0")

	return binaryString.toInt(2)
}