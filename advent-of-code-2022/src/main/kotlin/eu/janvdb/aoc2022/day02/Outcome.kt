package eu.janvdb.aoc2022.day02

enum class Outcome {
	LOSE, DRAW, WIN;

	fun getAnswerForOutcome(input: RockPaperScissors): RockPaperScissors {
		return when (this) {
			LOSE -> when (input) {
				RockPaperScissors.ROCK -> RockPaperScissors.SCISSORS
				RockPaperScissors.SCISSORS -> RockPaperScissors.PAPER
				RockPaperScissors.PAPER -> RockPaperScissors.ROCK
			}

			DRAW -> input
			WIN -> when (input) {
				RockPaperScissors.ROCK -> RockPaperScissors.PAPER
				RockPaperScissors.SCISSORS -> RockPaperScissors.ROCK
				RockPaperScissors.PAPER -> RockPaperScissors.SCISSORS
			}
		}
	}

	companion object {
		fun fromAnswer(answer: String): Outcome {
			return when (answer) {
				"X" -> LOSE
				"Y" -> DRAW
				"Z" -> WIN
				else -> throw IllegalArgumentException(answer)
			}
		}
	}
}