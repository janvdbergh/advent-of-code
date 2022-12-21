package eu.janvdb.aoc2022.day21

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input21-test.txt"
const val FILENAME = "input21.txt"

val PATTERN_VARIABLE = Regex("[a-z]+")
val PATTERN_OPERATION = Regex("\\((\\d+) ([+*/-]) (\\d+)\\)")
val PATTERN_EQUATION_1 = Regex("\\((.*) ([+*/-]) (\\d+)\\) = (\\d+)")
val PATTERN_EQUATION_2 = Regex("\\((\\d+) ([+*/-]) (.*)\\) = (\\d+)")

fun main() {
	val puzzles = readLines(2022, FILENAME).associate {
		val parts = it.split(": ")
		Pair(parts[0], parts[1])
	}
	solvePuzzles(puzzles)

	val puzzles2 = puzzles.toMutableMap()
	puzzles2["humn"] = "X"
	puzzles2["root"] = puzzles2["root"]!!.replace('+', '=')
	solvePuzzles(puzzles2)
}

fun solvePuzzles(puzzles: Map<String, String>) {
	val puzzleString = buildLargeEquation(puzzles)
	val puzzleString2 = simplifyEquation(puzzleString)
	val puzzleString3 = solveEquation(puzzleString2)
	println(puzzleString3)
}

private fun buildLargeEquation(puzzles2: Map<String, String>): String {
	var puzzleString = puzzles2["root"]!!
	if (!puzzleString.contains("=")) puzzleString = "($puzzleString)"
	while (true) {
		val matchResult = PATTERN_VARIABLE.find(puzzleString) ?: break
		val replacement = puzzles2[matchResult.value]!!
		val replacementBraces = if (replacement.contains(PATTERN_VARIABLE)) "($replacement)" else replacement
		puzzleString = puzzleString.replaceRange(matchResult.range, replacementBraces)
	}
	return puzzleString
}


fun simplifyEquation(puzzle: String): String {
	var current = puzzle
	while (true) {
		val matchResult = PATTERN_OPERATION.find(current) ?: break

		val value1 = matchResult.groupValues[1].toBigInteger()
		val operator = matchResult.groupValues[2]
		val value2 = matchResult.groupValues[3].toBigInteger()
		val solution = when (operator) {
			"+" -> value1 + value2
			"-" -> value1 - value2
			"*" -> value1 * value2
			"/" -> value1 / value2
			else -> throw IllegalArgumentException(operator)
		}

		current = current.replaceRange(matchResult.range, solution.toString())
	}

	return current
}

fun solveEquation(puzzle: String): String {
	var current = puzzle
	while (true) {
		if (current[0] == '(' && current[current.length - 1] == ')')
			current = current.substring(2, current.length - 1)

		val matchResult1 = PATTERN_EQUATION_1.matchEntire(current)
		if (matchResult1 != null) {
			val largePart = matchResult1.groupValues[1]
			val operator = matchResult1.groupValues[2]
			val number = matchResult1.groupValues[3].toBigInteger()
			val otherNumber = matchResult1.groupValues[4].toBigInteger()
			val result = when (operator) {
				"+" -> otherNumber - number
				"-" -> otherNumber + number
				"*" -> otherNumber / number
				"/" -> otherNumber * number
				else -> throw IllegalArgumentException(operator)
			}
			current = "$largePart = $result"
			continue
		}

		val matchResult2 = PATTERN_EQUATION_2.matchEntire(current)
		if (matchResult2 != null) {
			val number = matchResult2.groupValues[1].toBigInteger()
			val operator = matchResult2.groupValues[2]
			val largePart = matchResult2.groupValues[3]
			val otherNumber = matchResult2.groupValues[4].toBigInteger()
			val result = when (operator) {
				"+" -> otherNumber - number
				"-" -> number - otherNumber
				"*" -> otherNumber / number
				"/" -> number / otherNumber
				else -> throw IllegalArgumentException(operator)
			}
			current = "$largePart = $result"
			continue
		}

		break
	}

	return current
}