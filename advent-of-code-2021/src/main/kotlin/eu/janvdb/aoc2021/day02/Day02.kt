package eu.janvdb.aoc2021.day02

import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input02.txt"

fun main() {
	runPart(Position1(0, 0))
	runPart(Position2(0, 0, 0))
}

private fun <T : Position<T>> runPart(start: T) {
	val result = readLines(2021, FILENAME)
		.map { Instruction.parse(it) }
		.fold(start) { position, instruction -> position.move(instruction) }

	println("(${result.horizontal}, ${result.vertical}) => ${result.score}")
}

enum class Direction {
	DOWN, UP, FORWARD
}

data class Instruction(val direction: Direction, val amount: Long) {
	companion object {
		fun parse(line: String): Instruction {
			val parts = line.split(" ")
			return Instruction(
				Direction.valueOf(parts[0].uppercase()),
				parts[1].toLong()
			)
		}
	}
}

abstract class Position<T : Position<T>>(val horizontal: Long, val vertical: Long) {
	val score: Long
		get() = horizontal * vertical

	abstract fun move(instruction: Instruction): T
}

class Position1(horizontal: Long, vertical: Long) : Position<Position1>(horizontal, vertical) {
	override fun move(instruction: Instruction): Position1 {
		return when (instruction.direction) {
			Direction.DOWN -> Position1(horizontal, vertical + instruction.amount)
			Direction.UP -> Position1(horizontal, vertical - instruction.amount)
			Direction.FORWARD -> Position1(horizontal + instruction.amount, vertical)
		}
	}
}

class Position2(horizontal: Long, vertical: Long, private val aim: Long) : Position<Position2>(horizontal, vertical) {
	override fun move(instruction: Instruction): Position2 {
		return when (instruction.direction) {
			Direction.DOWN -> Position2(horizontal, vertical, aim + instruction.amount)
			Direction.UP -> Position2(horizontal, vertical, aim - instruction.amount)
			Direction.FORWARD -> Position2(horizontal + instruction.amount, vertical + aim * instruction.amount, aim)
		}
	}
}