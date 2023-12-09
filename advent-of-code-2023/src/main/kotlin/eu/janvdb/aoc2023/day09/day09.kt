package eu.janvdb.aoc2023.day09

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input09-test.txt"
const val FILENAME = "input09.txt"

fun main() {
	val histories = readLines(2023, FILENAME).map { History.parse(it) }

	val extrapolated = histories.map { it.extrapolate() }
	val sum = extrapolated.sumOf { it.values.last()  }
	println(sum)

	val extrapolatedBack = histories.map { it.extrapolateBack() }
	val sumBack = extrapolatedBack.sumOf { it.values.first()  }
	println(sumBack)
}

data class History (val values: List<Long>) {

	fun extrapolate(): History {
		if (this.containsOnlyZero()) return History(values + 0L)

		val next = differences().extrapolate()
		val extraValue = values.last() + next.values.last()
		return History(values + extraValue)
	}

	fun extrapolateBack(): History {
		if (this.containsOnlyZero()) return History(listOf(0L) + values)

		val next = differences().extrapolateBack()
		val extraValue = values.first() - next.values.first()
		return History(listOf(extraValue) + values)
	}

	private fun differences(): History {
		return History(values.zipWithNext { a, b -> b - a })
	}

	private fun containsOnlyZero(): Boolean {
		return values.all { it == 0L }
	}

	companion object {
		fun parse(input: String): History {
			return History(input.split(" ").map { it.toLong() })
		}
	}
}