package eu.janvdb.aoc2020.day08

fun main() {
	val program = readProgram("input08.txt")
	val exitState1 = Computer(program).run()
	println(exitState1.endState.accumulator)

	val exitState2 = IntRange(0, program.size).asSequence()
		.map { swapNopWithJmpAtPosition(program, it) }
		.filterNotNull()
		.map(::Computer)
		.map(Computer::run)
		.find { it.endCondition == EndCondition.NORMAL_END }

	println(exitState2!!.endState.accumulator)
}

fun swapNopWithJmpAtPosition(program: Program, position: Int): Program? {
	if (program[position].instructionType == InstructionType.NOP) {
		return program.updateInstruction(position, Instruction(InstructionType.JMP, program[position].operand))
	}
	if (program[position].instructionType == InstructionType.JMP) {
		return program.updateInstruction(position, Instruction(InstructionType.NOP, program[position].operand))
	}
	return null
}

