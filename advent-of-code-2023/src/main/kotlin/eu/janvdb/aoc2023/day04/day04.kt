package eu.janvdb.aoc2023.day04

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input04-test.txt"
const val FILENAME = "input04.txt"

val LINE_REGEX = Regex("^Card +(\\d+): ([\\d ]+)\\|([\\d ]+)$")
val SPLIT_REGEX = Regex(" +")

fun main() {
	val puzzle = Puzzle.parse(readLines(2023, FILENAME))

	println(puzzle.score1())
	println(puzzle.score2())
}

data class Puzzle(val cards: List<Card>) {
	private val numbersOfEachCard = MutableList(cards.size) { 1 }

	init {
		cards.forEachIndexed { index, puzzle ->
			IntRange(1, puzzle.matches).forEach { numbersOfEachCard[index + it] += numbersOfEachCard[index] }
		}
	}

	fun score1(): Int {
		return cards.sumOf { it.score1() }
	}

	fun score2(): Int {
		return numbersOfEachCard.sum()
	}

	companion object {
		fun parse(lines: List<String>): Puzzle {
			val cards = lines.map { Card.parse(it) }
			return Puzzle(cards)
		}
	}
}

data class Card(val id: Int, val values: Set<Int>, val winners: Set<Int>) {
	val matches = values.intersect(winners).size

	fun score1(): Int {
		if (matches == 0) return 0
		return 1 shl (matches - 1)
	}

	companion object {
		fun parse(input: String): Card {
			val matchResult = LINE_REGEX.find(input)!!

			val id = matchResult.groupValues[1].toInt()
			val values = matchResult.groupValues[2].trim().split(SPLIT_REGEX).map { it.toInt() }.toSet()
			val winners = matchResult.groupValues[3].trim().split(SPLIT_REGEX).map { it.toInt() }.toSet()

			return Card(id, values, winners)
		}
	}
}