package eu.janvdb.aoc2022.day02

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input02-test.txt"
const val FILENAME = "input02.txt"

fun main() {
	val input = readLines(2022, FILENAME)

	val answer1 = input.asSequence()
		.map { it.split(" ") }
		.map { Pair(RockPaperScissors.fromInput(it[0]), RockPaperScissors.fromAnswer(it[1]))}
		.map { getGameScore(it.first, it.second) }
		.sum()
	println(answer1)

	val answer2 = input.asSequence()
		.map { it.split(" ") }
		.map { Pair(RockPaperScissors.fromInput(it[0]), Outcome.fromAnswer(it[1]))}
		.map { Pair(it.first, it.second.getAnswerForOutcome(it.first)) }
		.map { getGameScore(it.first, it.second) }
		.sum()
	println(answer2)
}

fun getGameScore(theirMove: RockPaperScissors, yourMove: RockPaperScissors): Int {
	fun getScoreForOutcome(): Int {
		if (yourMove.beats(theirMove)) return 6
		if (theirMove.beats(yourMove)) return 0
		return 3
	}

	return getScoreForOutcome() + yourMove.score()
}