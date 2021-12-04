package eu.janvdb.aoc2021.day04

import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input04.txt"

const val CARD_SIZE = 5

fun main() {
	val lines = readLines(2021, FILENAME).map { it.trim() }.filter { it.isNotBlank() }
	val numbers = lines[0].split(",").map { it.toInt() }
	val cards = lines.asSequence()
		.drop(1)
		.map { line -> line.split(Regex(" +")).map { it.toInt() } }
		.chunked(CARD_SIZE)
		.map { it.flatten() }
		.map { BingoCard(it) }
		.toList()

	val wins = cards.map { it.calculateWin(numbers) }

	val win = wins.minByOrNull { it.numberOfTurns }
	println(win)

	val lose = wins.maxByOrNull { it.numberOfTurns }
	println(lose)
}

data class BingoCard(val values: List<Int>) {

	fun calculateWin(numbers: List<Int>): Win {
		for (index in 1..numbers.size) {
			val subList = numbers.subList(0, index).toSet()
			if (hasWin(subList)) {
				val numbersRemaining = values.minus(subList)
				return Win(index, numbersRemaining.sum() * numbers[index - 1])
			}
		}
		return Win(Int.MAX_VALUE, 0)
	}

	private fun hasWin(subList: Set<Int>): Boolean {
		val foundNumbers = values.map { subList.contains(it) }

		for (x in 0 until CARD_SIZE) {
			var won1 = true
			var won2 = true
			for (y in 0 until CARD_SIZE) {
				if (!foundNumbers[x * 5 + y]) won1 = false
				if (!foundNumbers[y * 5 + x]) won2 = false
			}

			if (won1 || won2) {
				return true
			}
		}

		return false
	}
}

data class Win(val numberOfTurns: Int, val score: Int)