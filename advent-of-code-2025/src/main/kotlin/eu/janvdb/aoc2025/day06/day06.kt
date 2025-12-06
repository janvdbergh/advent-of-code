package eu.janvdb.aoc2025.day06

import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input06.txt"
//const val FILENAME = "input06-test.txt"

val REGEX = Regex("\\s+")

fun main() {
	val lines = readLines(2025, FILENAME)
	val sheet = Sheet.parse(lines)
	println(sheet.calculate1().sum())
	println(sheet.calculate2().sum())
}

data class Sheet(val data: List<String>, val operators: List<Operator>) {

	fun calculate1(): List<Long> {
		val values = data.map { line -> line.trim().split(REGEX).map { it.toLong() } }
		return (0..<operators.size).map { i ->
			val operator = operators[i]
			values.map { it[i] }.fold(operator.initialValue, operator.perform)
		}
	}

	fun calculate2(): List<Long> {
		val values = values2()
		return (0..<operators.size).map { i ->
			val operator = operators[i]
			values[i].fold(operator.initialValue, operator.perform)
		}
	}

	private fun values2(): List<List<Long>> {
		//  numberLocations: array of array containing the indices that build each number
		val numberLocations: MutableList<MutableList<Int>> = mutableListOf(mutableListOf())
		(0..<data[0].length).forEach { i ->
			val allBlanks = data.all { it[i] == ' ' }
			if (allBlanks) {
				numberLocations += mutableListOf<Int>()
			} else {
				numberLocations.last() += i
			}
		}

		// Use numberLocations to get the values
		return numberLocations.map { group ->
			group.map { loc ->
				data.map { line -> line[loc] }
					.fold(0L) { acc, value -> if (value == ' ') acc else acc * 10 + value.digitToInt() }
			}
		}
	}

	companion object {
		fun parse(lines: List<String>): Sheet {
			val data = lines.subList(0, lines.size - 1)
			val maxLength = data.maxOf { it.length }
			val paddedData = data.map { it + " ".repeat(maxLength - it.length) }
			val operators = lines.last().trim().split(REGEX).map { Operator.parse(it) }
			return Sheet(paddedData, operators)
		}
	}
}

enum class Operator(val operator: String, val initialValue: Long, val perform: (a: Long, b: Long) -> Long) {
	PLUS("+", 0, { a, b -> a + b }),
	MUL("*", 1, { a, b -> a * b });

	companion object {
		fun parse(value: String): Operator {
			return entries.find { it.operator == value } ?: throw IllegalArgumentException("Unknown operator: $value")
		}
	}
}