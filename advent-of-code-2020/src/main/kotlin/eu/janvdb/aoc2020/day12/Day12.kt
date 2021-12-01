package eu.janvdb.aoc2020.day12

import eu.janvdb.aocutil.kotlin.Coordinate
import eu.janvdb.aocutil.kotlin.Direction
import eu.janvdb.aocutil.kotlin.readLines

val INSTRUCTION_REGEX = Regex("([NSEWLRF])(\\d+)")

fun main() {
	val instructions = readLines(2020, "input12.txt").map(::parseInstruction)

	var position1 = Position1(Coordinate(0, 0), Direction.E)
	instructions.forEach { position1 = position1.step(it) }
	println("At $position1 with distance ${position1.coordinate.manhattanDistance()}")

	var position2 = Position2(Coordinate(0, 0), Coordinate(10, 1))
	instructions.forEach { position2 = position2.step(it) }
	println("At $position2 with distance ${position2.coordinate.manhattanDistance()}")
}

fun parseInstruction(instruction: String): Instruction {
	val matchResult = INSTRUCTION_REGEX.matchEntire(instruction) ?: throw IllegalArgumentException(instruction)
	return Instruction(InstructionType.valueOf(matchResult.groupValues[1]), matchResult.groupValues[2].toInt())
}

enum class InstructionType {
	N, S, E, W, L, R, F
}

data class Instruction(val instructionType: InstructionType, val amount: Int)

data class Position1(val coordinate: Coordinate, val direction: Direction) {
	fun step(instruction: Instruction): Position1 {
		val amount = instruction.amount
		return when (instruction.instructionType) {
			InstructionType.N -> Position1(coordinate.move(Direction.N, amount), direction)
			InstructionType.E -> Position1(coordinate.move(Direction.E, amount), direction)
			InstructionType.S -> Position1(coordinate.move(Direction.S, amount), direction)
			InstructionType.W -> Position1(coordinate.move(Direction.W, amount), direction)
			InstructionType.L -> Position1(coordinate, direction.rotateLeft(amount))
			InstructionType.R -> Position1(coordinate, direction.rotateRight(amount))
			InstructionType.F -> Position1(coordinate.move(direction, amount), direction)
		}
	}
}

data class Position2(val coordinate: Coordinate, val waypoint: Coordinate) {
	fun step(instruction: Instruction): Position2 {
		val amount = instruction.amount
		return when (instruction.instructionType) {
			InstructionType.N -> Position2(coordinate, waypoint.move(Direction.N, amount))
			InstructionType.E -> Position2(coordinate, waypoint.move(Direction.E, amount))
			InstructionType.S -> Position2(coordinate, waypoint.move(Direction.S, amount))
			InstructionType.W -> Position2(coordinate, waypoint.move(Direction.W, amount))
			InstructionType.L -> Position2(coordinate, waypoint.rotateLeft(amount))
			InstructionType.R -> Position2(coordinate, waypoint.rotateRight(amount))
			InstructionType.F -> Position2(coordinate.move(waypoint, amount), waypoint)
		}
	}
}
