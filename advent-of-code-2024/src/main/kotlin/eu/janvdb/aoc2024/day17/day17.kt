package eu.janvdb.aoc2024.day17


// Test input
val PROGRAM_TEST1 = listOf(0, 1, 5, 4, 3, 0)
const val INITIAL_VALUE_TEST1 = 729L

val PROGRAM_TEST2 = listOf(0, 3, 5, 4, 3, 0)

// Actual input
val PROGRAM = listOf(2, 4, 1, 2, 7, 5, 1, 3, 4, 3, 5, 5, 0, 3, 3, 0)
const val INITIAL_VALUE = 64584136L

fun main() {
    part1()
    part2()
}

private fun part1() {
    val result = runProgram(PROGRAM_TEST1, INITIAL_VALUE_TEST1)
    println(result.joinToString(","))

    val result1 = runProgram(PROGRAM, INITIAL_VALUE)
    println(result1.joinToString(","))
}

private fun runProgram(program: List<Int>, initialValue: Long) =
    Program.parse(program).run(ExecutionState.initial(initialValue))

private fun part2() {
    val result = findProgram(PROGRAM_TEST2, 0L, 0)
    println(result)

    val result1 = findProgram(PROGRAM, 0L, 0)
    print(result1)
}

fun findProgram(programData: List<Int>, currentValue: Long, instructionsFound: Int): Long? {
    if (instructionsFound == programData.size) return currentValue

    for (nextInstruction in 0..7) {
        val newValue = (currentValue shl 3) + nextInstruction
        val output = runProgram(programData, newValue)
        if (programData.subList(programData.size - instructionsFound - 1, programData.size) == output) {
            val result = findProgram(programData, newValue, instructionsFound + 1)
            if (result != null) return result
        }
    }

    return null
}

data class ExecutionState(val a: Long, val b: Long, val c: Long, val pc: Int, val output: List<Int>) {
    fun withA(newA: Long) = ExecutionState(newA, b, c, pc, output)
    fun withB(newB: Long) = ExecutionState(a, newB, c, pc, output)
    fun withC(newC: Long) = ExecutionState(a, b, newC, pc, output)
    fun withPC(newPC: Int) = ExecutionState(a, b, c, newPC, output)
    fun withOutput(value: Int) = ExecutionState(a, b, c, pc, output + value)

    fun comboOperand(value: Int): Long = when (value) {
        0, 1, 2, 3 -> value.toLong()
        4 -> a
        5 -> b
        6 -> c
        else -> throw IllegalArgumentException("Invalid value: $value")
    }

    companion object {
        fun initial(a: Long) = ExecutionState(a, 0L, 0L, 0, emptyList())
    }
}

typealias InstructionFunction = (ExecutionState, Int) -> ExecutionState

enum class InstructionType(val identifier: Int, val function: InstructionFunction) {
    ADV(0, { registers, value -> registers.withA(registers.a.shr(registers.comboOperand(value).toInt())) }),
    BXL(1, { registers, value -> registers.withB(registers.b xor value.toLong()) }),
    BST(2, { registers, value -> registers.withB(registers.comboOperand(value).mod(8).toLong()) }),
    JNZ(3, { registers, value -> if (registers.a == 0L) registers else registers.withPC(value / 2 - 1) }),
    BXC(4, { registers, _ -> registers.withB(registers.b xor registers.c) }),
    OUT(5, { registers, value -> registers.withOutput(registers.comboOperand(value).mod(8)) }),
    BDV(6, { registers, value -> registers.withB(registers.a.shr(registers.comboOperand(value).toInt())) }),
    CDV(7, { registers, value -> registers.withC(registers.a.shr(registers.comboOperand(value).toInt())) });

    companion object {
        fun fromIdentifier(identifier: Int): InstructionType = entries.first { it.identifier == identifier }
    }
}

data class Instruction(val type: InstructionType, val value: Int) {
    fun execute(executionState: ExecutionState): ExecutionState = type.function(executionState, value)
}

data class Program(val instructions: List<Instruction>) {
    fun run(initialExecutionState: ExecutionState): List<Int> {
        var executionState = initialExecutionState
        while (executionState.pc < instructions.size) {
            executionState = instructions[executionState.pc].execute(executionState)
            executionState = executionState.withPC(executionState.pc + 1)
        }
        return executionState.output
    }

    fun print() {
        instructions.forEach { println("${it.type} ${it.value}") }
    }

    companion object {
        fun parse(program: List<Int>): Program {
            val instructions = program.chunked(2).map { Instruction(InstructionType.fromIdentifier(it[0]), it[1]) }
            return Program(instructions)
        }
    }
}
