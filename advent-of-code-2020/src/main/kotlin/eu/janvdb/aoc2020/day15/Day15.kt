package eu.janvdb.aoc2020.day15

val STARTING_NUMBERS = listOf(1, 0, 16, 5, 17, 4)
const val NUMBER_OF_ROUNDS = 30_000_000

fun main() {
	val numbersSpoken = mutableMapOf<Int, Int>()
	var lastNumber = STARTING_NUMBERS.asSequence()
		.mapIndexed { index, number -> round(numbersSpoken, number, index) }
		.last()

	for (i in STARTING_NUMBERS.size until NUMBER_OF_ROUNDS - 1) {
		lastNumber = round(numbersSpoken, lastNumber, i)
	}
	println(lastNumber)
}

fun round(numbersSpoken: MutableMap<Int, Int>, number: Int, round: Int): Int {
	val previousRound = numbersSpoken[number]
	val result = if (previousRound == null) 0 else round - previousRound

	numbersSpoken[number] = round
	return result
}