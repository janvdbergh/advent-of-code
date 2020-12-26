package eu.janvdb.aoc2020.day08

import eu.janvdb.aocutil.kotlin.readLines
import java.util.function.BiConsumer
import java.util.function.Consumer

class ComputerState {
	var programCounter = 0
	var accumulator = 0L
}

enum class EndCondition {
	NORMAL_END, INFINITE_LOOP
}

class ExitState(val endState: ComputerState, val endCondition: EndCondition)

enum class InstructionType(var action: BiConsumer<ComputerState, Long>) {
	ACC({ state, operand -> state.accumulator += operand }),
	JMP({ state, operand -> state.programCounter += operand.toInt() - 1 }),
	NOP({ _, _ -> })
}

class Instruction(val instructionType: InstructionType, val operand: Long): Consumer<ComputerState> {
	override fun accept(state: ComputerState) {
		instructionType.action.accept(state, operand)
	}
}

class Program(val instructions: List<Instruction>) {
	val size: Int
		get() = instructions.size

	operator fun get(index: Int): Instruction {
		return instructions[index]
	}

	fun updateInstruction(index: Int, instruction: Instruction): Program {
		val newInstructions = instructions.toMutableList()
		newInstructions[index] = instruction
		return Program(newInstructions)
	}
}

class Computer(private val program: Program) {

	fun run(): ExitState {
		val instructionsRun = mutableListOf<Int>()
		val computerState = ComputerState()

		while (true) {
			instructionsRun.add(computerState.programCounter)

			program[computerState.programCounter].accept(computerState)
			computerState.programCounter++

			if (computerState.programCounter >= program.size) {
				return ExitState(computerState, EndCondition.NORMAL_END)
			}
			if(instructionsRun.contains(computerState.programCounter)) {
				return ExitState(computerState, EndCondition.INFINITE_LOOP)
			}
		}
	}
}

var INSTRUCTION_REGEX = Regex("([A-Z]+) ([+-][0-9]+)")
fun readProgram(fileName: String): Program {
	val instructions = readLines(fileName)
		.map(String::toUpperCase)
		.map(INSTRUCTION_REGEX::matchEntire)
		.map(::checkNotNull)
		.map { Instruction(InstructionType.valueOf(it.groupValues[1]), it.groupValues[2].toLong()) }
	return Program(instructions)
}