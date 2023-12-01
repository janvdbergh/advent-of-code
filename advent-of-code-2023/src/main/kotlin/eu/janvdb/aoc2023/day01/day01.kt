package eu.janvdb.aoc2023.day01

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input01-test.txt"
//const val FILENAME = "input01-test2.txt"
const val FILENAME = "input01.txt"

val DIGIT_VALUES_1 = mapOf(
	Pair("0", 0),
	Pair("1", 1),
	Pair("2", 2),
	Pair("3", 3),
	Pair("4", 4),
	Pair("5", 5),
	Pair("6", 6),
	Pair("7", 7),
	Pair("8", 8),
	Pair("9", 9)
)
val DIGIT_VALUES_2 = mapOf(
	Pair("zero", 0),
	Pair("one", 1),
	Pair("two", 2),
	Pair("three", 3),
	Pair("four", 4),
	Pair("five", 5),
	Pair("six", 6),
	Pair("seven", 7),
	Pair("eight", 8),
	Pair("nine", 9),
)

fun main() {
	runWithValues(DIGIT_VALUES_1)
	runWithValues(DIGIT_VALUES_1 + DIGIT_VALUES_2)
}

private fun runWithValues(digitValues: Map<String, Int>) {
	val sum = readLines(2023, FILENAME).sumOf { line ->
		val digits = IntRange(0, line.length - 1)
			.map { Pair(it, line.substring(it)) }
			.flatMap { pair -> digitValues.filter { pair.second.startsWith(it.key) }.map { Pair(pair.first, it.value) } }
			.sortedBy { it.first }

		val digit0 = digits.first().second
		val digit1 = digits.last().second

		digit0 * 10 + digit1
	}

	println(sum)
}