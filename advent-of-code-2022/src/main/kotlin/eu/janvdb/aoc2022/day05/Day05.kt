package eu.janvdb.aoc2022.day05

import eu.janvdb.aocutil.kotlin.readGroupedLines

//const val FILENAME = "input05-test.txt"
const val FILENAME = "input05.txt"

val INSTRUCTION_REGEX = Regex("move (\\d+) from (\\d+) to (\\d+)")

fun main() {
    val lines = readGroupedLines(2022, FILENAME)

    val crates = Crates.parse(lines[0])
    val instructions = lines[1].map(String::toInstruction)

    runPart(crates, instructions, Crates::moveOneByOne)
    runPart(crates, instructions, Crates::moveAllAtOnce)
}

private fun runPart(
    crates: Crates, instructions: List<Instruction>,
    mapper: (crates: Crates, instruction: Instruction) -> Crates
) {
    var currentCrates = crates
    for (instruction in instructions) {
        currentCrates = mapper.invoke(currentCrates, instruction)
    }

    println(currentCrates.tops())
}

data class Crates(private val stacks: List<List<Char>> = listOf()) {
    fun addTo(pos: Int, ch: Char): Crates {
        val newStacks = stacks.toMutableList()
        while (newStacks.size <= pos) newStacks.add(listOf())
        newStacks[pos] = newStacks[pos] + ch
        return Crates(newStacks)
    }

    fun moveOneByOne(instruction: Instruction): Crates {
        val newStacks = stacks.map { it.toMutableList() }
        for (i in 1..instruction.numberOfCrates) {
            val crate = newStacks[instruction.from - 1].removeLast()
            newStacks[instruction.to - 1].add(crate)
        }
        return Crates(newStacks)
    }

    fun moveAllAtOnce(instruction: Instruction): Crates {
        val newStacks = stacks.toMutableList()
        val fromStack = newStacks[instruction.from - 1]
        val cratesToMove = fromStack.subList(fromStack.size - instruction.numberOfCrates, fromStack.size)

        newStacks[instruction.from - 1] = fromStack.subList(0, fromStack.size - instruction.numberOfCrates)
        newStacks[instruction.to - 1] = newStacks[instruction.to - 1] + cratesToMove

        return Crates(newStacks)
    }

    fun tops(): String {
        return stacks.map { it.last() }.joinToString("")
    }

    companion object {
        fun parse(lines: List<String>): Crates {
            var result = Crates()
            lines.reversed().forEach { line ->
                line.toCharArray()
                    .mapIndexed { index, ch -> Pair(index, ch) }
                    .filter { it.second.isLetter() }
                    .forEach {
                        val crateIndex = (it.first - 1) / 4
                        result = result.addTo(crateIndex, it.second)
                    }
            }
            return result
        }
    }
}

data class Instruction(val numberOfCrates: Int, val from: Int, val to: Int) {
}

fun String.toInstruction(): Instruction {
    val match = INSTRUCTION_REGEX.matchEntire(this) ?: throw IllegalArgumentException(this)
    return Instruction(match.groupValues[1].toInt(), match.groupValues[2].toInt(), match.groupValues[3].toInt())
}