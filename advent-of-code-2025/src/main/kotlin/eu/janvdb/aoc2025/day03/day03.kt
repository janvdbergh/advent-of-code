package eu.janvdb.aoc2025.day03

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input03-test.txt"
const val FILENAME = "input03.txt"

fun main() {
    val banks = readLines(2025, FILENAME)
        .map { line -> line.map { it.digitToInt() } }


    println(banks.sumOf { findMax(it, 2) })
    println(banks.sumOf { findMax(it, 12) })
}

fun findMax(bank: List<Int>, digitsRemaining: Int, start: Int = 0, currentSum: Long = 0): Long {
    if (digitsRemaining == 0) return currentSum
    if (start >= bank.size) return -1

    val steps = (start..<bank.size).asSequence()
        .map { Pair(it, currentSum * 10 + bank[it])}
        .sortedByDescending { it.second }

    for(step in steps) {
        val result = findMax(bank, digitsRemaining - 1, step.first + 1, step.second)
        if (result != -1L) return result
    }

    return -1
}
