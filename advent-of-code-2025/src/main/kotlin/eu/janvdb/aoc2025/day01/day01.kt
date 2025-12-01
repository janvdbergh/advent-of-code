package eu.janvdb.aoc2025.day01

import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.abs

//const val FILENAME = "input01-test.txt"
const val FILENAME = "input01.txt"

fun main() {
	val values = readLines(2025, FILENAME)
		.map { line -> (if (line.startsWith('L')) -1 else 1) * line.substring(1).toInt() }
	println(values)

	val result = values.fold(Triple(50, 0, 0)) { accumulator, value ->
		var next = accumulator.first
		var passingZero = 0
		(0..<abs(value)).forEach { _ ->
			next += if (value < 0) -1 else 1
			if (next == -1) {
				next = 99
			}
			if (next == 100) {
				next = 0
			}
			if (next == 0) {
				passingZero++
			}
		}

		val atZero = if (next == 0) 1 else 0

		Triple(next, accumulator.second + atZero, accumulator.third + passingZero)
	}
	println(result)
}