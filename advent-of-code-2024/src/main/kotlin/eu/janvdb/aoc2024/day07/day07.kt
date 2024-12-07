package eu.janvdb.aoc2024.day07

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input07-test.txt"
const val FILENAME = "input07.txt"

fun main() {
	val equations = readLines(2024, FILENAME).map { Equation.fromString(it) }

	val correctEquations = equations.filter { it.hasSolution() }
	val result = correctEquations.sumOf { it.result }
	println(result)
}

data class Equation(val operands: List<Long>, val result: Long) {

	fun hasSolution(currentOperators: List<Operator> = listOf()): Boolean {
		if (currentOperators.size == operands.size - 1) return isCorrect(currentOperators)

		return Operator.entries.asSequence()
			.map { currentOperators + it }
			.any { hasSolution(it) }
	}

	private fun isCorrect(operators: List<Operator>): Boolean {
		var solution = operands[0]
		for (i in 1 until operands.size) {
			solution = operators[i - 1].eval(solution, operands[i])
		}
		return solution == result
	}

	companion object {
		fun fromString(s: String): Equation {
			val parts1 = s.split(": ")
			val operands = parts1[1].split(" ").map { it.toLong() }
			val result = parts1[0].toLong()
			return Equation(operands, result)
		}
	}
}

enum class Operator(val eval: (Long, Long) -> Long) {
	PLUS({ x, y -> x + y }),
	MUL({ x, y -> x * y }),
	CONCAT({ x, y -> (x.toString() + y.toString()).toLong() })
}