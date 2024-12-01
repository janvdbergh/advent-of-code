package eu.janvdb.aoc2024.day01

import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.abs

//const val FILENAME = "input01-test.txt"
const val FILENAME = "input01.txt"

fun main() {
	val input = readLines(2024, FILENAME)
		.map { line -> line.split(Regex("\\s+")).map { it.toLong() } }
	val numbers1 = input.map { it[0] }.sorted()
	val numbers2 = input.map { it[1] }.sorted()

	val sum1 = numbers1.indices.sumOf { abs(numbers1[it] - numbers2[it]) }
	println(sum1)

	val sum2 = numbers1.sumOf { number1 -> number1 * numbers2.count { it == number1 } }
	println(sum2)
}