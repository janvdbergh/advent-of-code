package eu.janvdb.aoc2024.day03

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input03-test.txt"
const val FILENAME = "input03.txt"

fun main() {
    val input = readLines(2024, FILENAME).joinToString("")
    part1(input)
    part2(input)
}

private fun part1(input: String) {
    val state = State()
    val sum = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")
        .findAll(input)
        .map { calculate(it.value, state) }
        .sum()
    println(sum)
}

private fun part2(input: String) {
    val state = State()
    val sum = Regex("mul\\(\\d{1,3},\\d{1,3}\\)|do\\(\\)|don't\\(\\)")
        .findAll(input)
        .map { calculate(it.value, state) }
        .sum()
    println(sum)
}

private fun calculate(instruction: String, state: State): Long {
    if (instruction == "do()") {
        state.enabled = true
        return 0
    }
    if (instruction == "don't()") {
        state.enabled = false
        return 0
    }
    if (!state.enabled) {
        return 0
    }

    val numbers = Regex("\\d+")
        .findAll(instruction)
        .map { it.value.toLong() }
        .toList()
    return numbers[0] * numbers[1]
}

class State {
    var enabled = true
}
