package eu.janvdb.aoc2024.day24

import eu.janvdb.aocutil.kotlin.readGroupedLines
import java.util.*

//const val FILENAME = "input24-test.txt"
//const val FILENAME = "input24.txt"
const val FILENAME = "input24-fixed.txt"

// Wires to be swapped (by examining truth tables)
// ctg,dmh,dvq,rpb,rpv,z11,z31,z38

fun main() {
	val statements = Statements.fromGroupedLines(readGroupedLines(2024, FILENAME))
	println(statements.evaluate())

	statements.logicalBlocks.forEach {
		it.print()
		it.printTruthTable()
	}
}

data class Statements(val inputs: List<Input>, val logicalBlocks: List<LogicalBlock>) {
	fun evaluate(): Long {
		val variables = inputs.associate { Pair(it.output, it.value) }.toMutableMap()
		val result = mutableListOf<Boolean>()

		logicalBlocks.forEach { logicalBlock ->
			val newVariables = logicalBlock.evaluate(variables)
			variables += newVariables
			newVariables.filter { it.key.startsWith('z') }.forEach { result += it.value }
		}

		return result
			.map { if (it) 1 else 0 }
			.foldRight(0L) { it, acc -> acc * 2 + it }
	}

	fun print() {
		inputs.forEach(::println)
		println()
		logicalBlocks.forEach(LogicalBlock::print)
	}

	companion object {
		fun fromGroupedLines(groupedLines: List<List<String>>): Statements {
			val inputs = groupedLines[0].map(Input::fromString).sorted()
			val expressions = groupedLines[1].map(Expression::fromString).sortedBy { it.output }

			val logicalBlocks = mutableListOf<LogicalBlock>()
			val inputsToDo = inputs.toMutableList()
			val expressionsToDo = expressions.toMutableList()
			val currentOutputs = mutableSetOf<String>()
			while (inputsToDo.isNotEmpty()) {
				val logicalBlockStatements = mutableListOf<Statement>()

				// take 2 inputs
				val input1 = inputsToDo.removeFirst()
				val input2 = inputsToDo.removeFirst()
				currentOutputs += input1.output
				currentOutputs += input2.output

				// add statements we can now evaluate
				while (true) {
					val canBeEvaluated = expressionsToDo
						.find { currentOutputs.contains(it.input1) && currentOutputs.contains(it.input2) }
					if (canBeEvaluated == null) break
					currentOutputs += canBeEvaluated.output
					logicalBlockStatements += canBeEvaluated
					expressionsToDo -= canBeEvaluated
				}

				logicalBlocks.add(LogicalBlock.fromStatements(logicalBlockStatements))
			}

			return Statements(inputs, logicalBlocks)
		}
	}
}

abstract class Statement(val output: String) {
	abstract fun getInputs(): List<String>
	abstract fun isInput(): Boolean
	abstract fun isOutput(): Boolean
	abstract fun evaluate(variables: Map<String, Boolean>): Boolean
}

class Input(output: String, val value: Boolean) : Statement(output), Comparable<Input> {
	private val letter = output[0]
	private val number = output.substring(1).toInt()

	override fun getInputs() = listOf<String>()
	override fun isInput(): Boolean = true
	override fun isOutput(): Boolean = false
	override fun toString() = "val $output = $value"
	override fun evaluate(variables: Map<String, Boolean>) = value

	override fun compareTo(other: Input): Int {
		if (number < other.number) return -1
		if (number > other.number) return 1
		if (letter < other.letter) return -1
		if (letter > other.letter) return 1
		return 0
	}

	companion object {
		fun fromString(line: String): Input {
			val (input, value) = line.split(": ")
			return Input(input, value == "1")
		}
	}
}

enum class Operator(val function: (Boolean, Boolean) -> Boolean) {
	AND({ x1, x2 -> x1 and x2 }),
	OR({ x1, x2 -> x1 or x2 }),
	XOR({ x1, x2 -> x1 xor x2 }),
}

class Expression(val input1: String, val input2: String, output: String, private val operator: Operator) : Statement(output) {
	override fun getInputs() = listOf(input1, input2)
	override fun isInput(): Boolean = false
	override fun isOutput(): Boolean = output.startsWith('z')
	override fun evaluate(variables: Map<String, Boolean>): Boolean {
		return operator.function.invoke(variables[input1]!!, variables[input2]!!)
	}

	override fun toString() = "val $output = $input1 ${operator.name.lowercase()} $input2"

	companion object {
		fun fromString(line: String): Expression {
			val (input1, operator, input2, _, output) = line.split(" ")
			return Expression(input1, input2, output, Operator.valueOf(operator))
		}
	}
}

data class LogicalBlock(val inputs: SortedSet<String>, val outputs: SortedSet<String>, val statements: List<Statement>) {
	fun evaluate(variables: Map<String, Boolean>): SortedMap<String, Boolean> {
		val myVariables = variables.filter { inputs.contains(it.key) }.toMutableMap()

		statements.forEach { statement ->
			val value = statement.evaluate(myVariables)
			myVariables[statement.output] = value
		}

		return myVariables.filter { outputs.contains(it.key) }.toSortedMap()
	}

	fun print() {
		statements.forEach(::println)
		println()
	}

	fun printTruthTable() {
		fun namesJoined(names: Set<String>) = names.joinToString("\t")

		fun printValues(variables: SortedMap<String, Boolean>) =
			variables.toList().joinToString("\t") { if (it.second) "1" else "0" }


		println("${namesJoined(inputs)}\t\t${namesJoined(outputs)}")

		val possibilities = 1.shl(inputs.size)
		for (bits in 0 until possibilities) {
			var current = bits
			val variables = inputs.reversed().associateWith { _ ->
				val next = current % 2
				current /= 2
				next == 1
			}.toSortedMap()
			val outputs = evaluate(variables)

			println(printValues(variables) + "\t\t" + printValues(outputs))
		}

		println()
	}

	companion object {
		fun fromStatements(statements: List<Statement>): LogicalBlock {
			val allInputs = statements.flatMap { it.getInputs() }.toSortedSet()
			val allOutputs = statements.filter { !it.isInput() }.map { it.output }.toSortedSet()
			val inputs = allInputs - allOutputs
			val outputs = allOutputs - allInputs
			return LogicalBlock(inputs.toSortedSet(), outputs.toSortedSet(), statements)
		}
	}
}