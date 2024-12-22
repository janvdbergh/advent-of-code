package eu.janvdb.aoc2024.day22

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input22-test.txt"
//const val FILENAME = "input22-test2.txt"
const val FILENAME = "input22.txt"

const val NUMBER_OF_NUMBERS = 2000
const val SEQUENCE_SIZE = 4

fun main() {
	val monkeys = readLines(2024, FILENAME).map { Monkey(it.toLong()) }
//	val monkeys = listOf(Monkey(123L))

	// part 1
	println(monkeys.sumOf { it.numbers.last() })

	// part2
	val allSequences = monkeys.asSequence().flatMap { it.pricePerSequence.keys }.toSet()
	val result = allSequences.asSequence()
		.map { sequence -> Pair(sequence, monkeys.sumOf { monkey -> monkey.pricePerSequence[sequence] ?: 0 }) }
		.maxBy { it.second }
	println(result)
}

class Monkey(initialNumber: Long) {
	val numbers: List<Long> = numberSequence(initialNumber).toList()
	val prices: List<Int> = numbers.map { (it % 10).toInt() }
	val priceChanges: List<Int> = prices.zipWithNext { a, b -> b - a }
	val pricePerSequence: Map<List<Int>, Int> = priceChanges
		.drop(SEQUENCE_SIZE - 1).indices.reversed().asSequence()
		.map { index ->
			val sequence = priceChanges.subList(index, index + SEQUENCE_SIZE)
			val price = prices[index + SEQUENCE_SIZE]
			Pair(sequence, price)
		}
		.toMap()

	private fun numberSequence(initial: Long): Sequence<Long> {
		fun mix(number1: Long, number2: Long) = number1 xor number2
		fun prune(number: Long) = number % 16777216L

		return sequence {
			var current = initial
			yield(current)
			for (i in 0 until NUMBER_OF_NUMBERS) {
				val secret1 = prune(mix(current, current * 64))
				val secret2 = prune(mix(secret1, secret1 / 32))
				current = prune(mix(secret2, secret2 * 2048))
				yield(current)
			}
		}
	}
}