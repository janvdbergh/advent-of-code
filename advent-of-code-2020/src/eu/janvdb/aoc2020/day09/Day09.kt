package eu.janvdb.aoc2020.day09

import eu.janvdb.aoc2020.common.readLines

const val PREAMBLE_SIZE = 25
const val INPUT_FILE = "input09.txt"

fun main() {
	val numbers = readLines(INPUT_FILE).map(String::toLong)

	var index = PREAMBLE_SIZE
	while (index < numbers.size) {
		val summableNumbers = numbers.subList(index - PREAMBLE_SIZE, index)
		val currentNumber = numbers[index]
		val hasCorrectSum = summableNumbers.asSequence()
			.flatMap { number1 -> summableNumbers.asSequence().map { number2 -> number1 + number2 } }
			.any { sum -> sum == currentNumber }
		if (!hasCorrectSum) break

		index++
	}
	val invalidNumber = numbers[index]
	println(invalidNumber)

	var rangeAsSum: List<Long>? = null
	outerloop@ for (i in 0..numbers.size - 1) {
		for (j in 0..numbers.size - 1) {
			var sum = 0L
			for (k in i..j) {
				sum += numbers[k]
			}
			if (sum == invalidNumber) {
				rangeAsSum = numbers.subList(i, j + 1)
				break@outerloop
			}
		}
	}

	val min = rangeAsSum!!.minOrNull()!!
	val max = rangeAsSum.maxOrNull()!!
	println(min + max)
}
