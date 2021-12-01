package eu.janvdb.aoc2020.day10

import eu.janvdb.aocutil.kotlin.readLines

fun main() {
	val numbers = readLines(2020, "input10.txt").map(String::toInt)
	val numbersIncludingStartAndEndValue = listOf(listOf(0, numbers.maxOrNull()!! + 3), numbers).flatten().sorted()

	part1(numbersIncludingStartAndEndValue)
	part2(numbersIncludingStartAndEndValue)
}

private fun part1(numbers: List<Int>) {
	val differences = numbers.asSequence()
		.zipWithNext()
		.map { Math.abs(it.first - it.second) }
		.groupBy({ it })

	val difference1 = differences[1]?.size ?: 0
	val difference3 = differences[3]?.size ?: 0
	assert(difference1 + difference3 == numbers.size - 1)
	println(difference1 * difference3)
}

fun part2(numbers: List<Int>) {
	val numberOfPathsByStartValue = mutableMapOf(Pair(numbers[numbers.size - 1], 1L))

	fun getNumberOfPaths(index: Int): Long {
		return numberOfPathsByStartValue[index] ?: 0L
	}

	for (i in numbers.size - 2 downTo 0) {
		val value = numbers[i]
		val numberOfPaths = getNumberOfPaths(value + 1) + getNumberOfPaths(value + 2) + getNumberOfPaths(value + 3)
		numberOfPathsByStartValue[value] = numberOfPaths
	}

	println(getNumberOfPaths(0))
}
