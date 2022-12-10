package eu.janvdb.aoc2022.day10

import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.abs

//const val FILENAME = "input10-test.txt"
const val FILENAME = "input10.txt"

const val WIDTH = 40
const val HEIGHT = 6

fun main() {
	val instructions = readLines(2022, FILENAME)
		.map(String::toInstruction)
	val computer = Computer(instructions)

	var sum = 0
	for (i in 1..WIDTH * HEIGHT) {
		if (i % 40 == 20) sum += computer.x * i

		val x = (i - 1) % WIDTH
		val ch = if (abs(x - computer.x) <= 1) "##" else "  "
		print(ch)
		if (i % 40 == 0) println()

		computer.step()
	}
	println(sum)
}

interface Instruction {
	val numberOfCycles: Int
	fun execute(computer: Computer)
}

class AddXInstruction(private val value: Int) : Instruction {
	override val numberOfCycles = 2

	override fun execute(computer: Computer) {
		computer.x += value
	}

	override fun toString(): String = "addx $value"
}

class NoopInstruction : Instruction {
	override val numberOfCycles = 1

	override fun execute(computer: Computer) {}

	override fun toString(): String = "noop"
}

fun String.toInstruction(): Instruction {
	val parts = this.split(" ")
	return when (parts[0]) {
		"addx" -> AddXInstruction(parts[1].toInt())
		"noop" -> NoopInstruction()
		else -> throw IllegalArgumentException(this)
	}
}

class Computer(private val program: List<Instruction>) {
	private var pc = 0
	private var currentInstruction: Instruction = program[0]
	private var cycleInInstruction = 0
	var x = 1

	fun step() {
		val complete = cycleInInstruction == currentInstruction.numberOfCycles - 1
		if (complete) {
			currentInstruction.execute(this)

			pc = (pc + 1) % program.size
			currentInstruction = program[pc]
			cycleInInstruction = 0
		} else {
			cycleInInstruction++
		}
	}

	override fun toString(): String = "Computer(pc=$pc, x=$x)"
}
