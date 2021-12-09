package eu.janvdb.aoc2021.day08

import eu.janvdb.aocutil.kotlin.readLines

private const val FILENAME = "input08.txt"

fun main() {
	val digits = readLines(2021, FILENAME)
		.map(::parseLine)
		.map { getDigits(it.first, it.second) }

	val result1 = digits.flatten().count { it == 1 || it == 4 || it == 7 || it == 8 }
	println(result1)

	val result2 = digits.map(::toNumber).sum()
	println(result2)
}

fun parseLine(line: String): Pair<List<Set<Char>>, List<Set<Char>>> {
	val parts = line.split(" | ")
	return Pair(parsePart(parts[0]), parsePart(parts[1]))
}

fun parsePart(s: String): List<Set<Char>> {
	return s.split(" ").map { it.trim().toCharArray().toSet() }
}

fun getDigits(inputs: List<Set<Char>>, outputs: List<Set<Char>>): List<Int> {
	var remainingInputs: List<Set<Char>> = inputs
	val pattern1 = remainingInputs.find { it.size == 2 }!!
	val pattern4 = remainingInputs.find { it.size == 4 }!!
	val pattern7 = remainingInputs.find { it.size == 3 }!!
	val pattern8 = remainingInputs.find { it.size == 7 }!!
	remainingInputs = remainingInputs - setOf(pattern1, pattern4, pattern7, pattern8)

	val pattern3 = remainingInputs.find { it.size == 5 && it.containsAll(pattern1) }!!
	val pattern9 = remainingInputs.find { it.size == 6 && it.containsAll(pattern3) }!!
	remainingInputs = remainingInputs - setOf(pattern3, pattern9)

	val pattern0 = remainingInputs.find { it.size == 6 && it.containsAll(pattern1) }!!
	remainingInputs = remainingInputs - setOf(pattern0)

	val pattern6 = remainingInputs.find { it.size == 6 }!!
	remainingInputs = remainingInputs - setOf(pattern6)

	val pattern5 = remainingInputs.find { it.size == 5 && pattern6.containsAll(it) }!!
	remainingInputs = remainingInputs - setOf(pattern5)

	val pattern2 = remainingInputs.find { it.size == 5 }!!

	return outputs.map {
		when (it) {
			pattern0 -> 0
			pattern1 -> 1
			pattern2 -> 2
			pattern3 -> 3
			pattern4 -> 4
			pattern5 -> 5
			pattern6 -> 6
			pattern7 -> 7
			pattern8 -> 8
			pattern9 -> 9
			else -> -1
		}
	}
}

fun toNumber(digits: List<Int>): Int {
	val result = digits.fold(0) { value, digit -> value * 10 + digit }
	println(result)
	return result
}