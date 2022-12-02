package eu.janvdb.aoc2022.day02

enum class RockPaperScissors {
	ROCK, PAPER, SCISSORS;

	fun beats(other: RockPaperScissors): Boolean {
		return when (this) {
			ROCK -> other == SCISSORS
			SCISSORS -> other == PAPER
			PAPER -> other == ROCK
		}
	}

	fun score(): Int {
		return when(this) {
			ROCK -> 1
			PAPER -> 2
			SCISSORS -> 3
		}
	}

	companion object {
		fun fromInput(input: String): RockPaperScissors {
			return when (input) {
				"A" -> ROCK
				"B" -> PAPER
				"C" -> SCISSORS
				else -> throw IllegalArgumentException(input)
			}
		}

		fun fromAnswer(answer: String): RockPaperScissors {
			return when (answer) {
				"X" -> ROCK
				"Y" -> PAPER
				"Z" -> SCISSORS
				else -> throw IllegalArgumentException(answer)
			}
		}
	}
}