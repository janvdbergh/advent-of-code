package eu.janvdb.aoc2021.day21

import eu.janvdb.aocutil.kotlin.runWithTimer
import kotlin.math.max

//val START_1 = Player(4)
//val START_2 = Player(8)

val START_1 = Player(3)
val START_2 = Player(5)

fun main() {
	runWithTimer("Part 1", ::part1)
	runWithTimer("Part 2", ::part2)
}

private fun part1() {
	val die = DeterministicDie()
	var player1 = START_1
	var player2 = START_2

	while (true) {
		player1 = player1.play(die.roll() + die.roll() + die.roll())
		if (player1.hasWon1()) {
			println("P1: $player1, P2: $player2, Die: $die, Score: ${player2.score * die.numberOfRolls}")
			break
		}
		player2 = player2.play(die.roll() + die.roll() + die.roll())
		if (player2.hasWon1()) {
			println("P1: $player1, P2: $player2, Die: $die, Score: ${player1.score * die.numberOfRolls}")
			break
		}
	}
}

private fun part2() {
	val outcomesPerRollValue = /* How frequent a certain roll with three dice is */
		IntRange(1, 3).flatMap { n1 -> IntRange(1, 3).flatMap { n2 -> IntRange(1, 3).map { n3 -> n1 + n2 + n3 } } }
			.groupingBy { it }.eachCount()

	val numberOfWinsPerStartPosition = mutableMapOf<StateDirac, ScoreDirac>()

	fun determineNumberOfWins(state: StateDirac): ScoreDirac {
		if (numberOfWinsPerStartPosition.contains(state)) {
			return numberOfWinsPerStartPosition[state]!!
		}

		if (state.player1.hasWon2()) {
			val score = ScoreDirac(1, 0)
			numberOfWinsPerStartPosition[state] = score
			return score
		}

		if (state.player2.hasWon2()) {
			val score = ScoreDirac(0, 1)
			numberOfWinsPerStartPosition[state] = score
			return score
		}

		val score = outcomesPerRollValue.asSequence()
			.map { determineNumberOfWins(state.play(it.key)) * it.value }
			.fold(ScoreDirac.ZERO) { acc, it -> acc + it }
		numberOfWinsPerStartPosition[state] = score
		return score
	}

	val numberOfWins = determineNumberOfWins(StateDirac(START_1, START_2, 1))
	println("$numberOfWins ${max(numberOfWins.player1Won, numberOfWins.player2Won)}")
}

data class Player(val position: Int, val score: Int = 0) {
	fun play(dieValue: Int): Player {
		val newPosition = (position + dieValue - 1) % 10 + 1
		val newScore = score + newPosition
		return Player(newPosition, newScore)
	}

	fun hasWon1() = score >= 1000
	fun hasWon2() = score >= 21

	override fun toString() = "$position/$score"
}

class DeterministicDie {
	var numberOfRolls = 0

	fun roll(): Int {
		numberOfRolls++
		return (numberOfRolls - 1) % 100 + 1
	}

	override fun toString() = "$numberOfRolls"
}

data class StateDirac(val player1: Player, val player2: Player, val currentPlayer: Int) {
	fun play(dieValue: Int): StateDirac {
		return if (currentPlayer == 1) {
			StateDirac(player1.play(dieValue), player2, 2)
		} else {
			StateDirac(player1, player2.play(dieValue), 1)
		}
	}
}

data class ScoreDirac(val player1Won: Long, val player2Won: Long) {
	operator fun plus(other: ScoreDirac) = ScoreDirac(player1Won + other.player1Won, player2Won + other.player2Won)
	operator fun times(value: Int) = ScoreDirac(value * player1Won, value * player2Won)

	companion object {
		val ZERO = ScoreDirac(0L, 0L)
	}
}