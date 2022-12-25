package eu.janvdb.aoc2022.day25

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input25-test.txt"
const val FILENAME = "input25.txt"

const val DIGITS = "=-012"
fun main() {
	runTests()
	part1()
}
fun runTests() {
	val testCases = listOf(
		Pair(1L, "1"), Pair(2L, "2"), Pair(3L, "1="), Pair(4L, "1-"), Pair(5L, "10"), Pair(6L, "11"), Pair(7L, "12"),
		Pair(8L, "2="), Pair(9L, "2-"), Pair(10L, "20"), Pair(15L, "1=0"), Pair(20L, "1-0"), Pair(2022L, "1=11-2"),
		Pair(12345L, "1-0---0"), Pair(314159265L, "1121-1110-1=0"),
	)

	testCases.forEach { (decimal, string) ->
		val from = string.fromSnafu()
		if (from != decimal) println("$string -> $decimal but was $from")
	}

	testCases.forEach { (decimal, string) ->
		val to = decimal.toSnafu()
		if (to != string) println("$decimal -> $string but was $to")
	}
}

fun part1() {
	val result = readLines(2022, FILENAME)
		.map(String::fromSnafu)
		.sum()
		.toSnafu()
	println(result)
}

fun String.fromSnafu(): Long {
	fun toDigit(ch: Char): Int {
		val index = DIGITS.indexOf(ch)
		if (index == -1) throw IllegalArgumentException("$ch")
		return index - 2
	}
	return this.map(::toDigit).fold(0L) { acc, digit -> acc * 5 + digit }
}

fun Long.toSnafu(): String {
	val builder = StringBuilder()
	var current = this
	while (current != 0L) {
		val temp = current + 2
		builder.append(DIGITS[(temp % 5).toInt()])
		current = temp / 5
	}

	return if (builder.isEmpty()) "0" else builder.reverse().toString()
}