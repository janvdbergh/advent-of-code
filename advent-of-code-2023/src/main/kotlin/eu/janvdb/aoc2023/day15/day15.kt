package eu.janvdb.aoc2023.day15

import eu.janvdb.aocutil.kotlin.readLines
import kotlin.streams.asSequence

//const val FILENAME = "input15-test.txt"
const val FILENAME = "input15.txt"

fun main() {
    val instructions = readLines(2023, FILENAME)[0].split(",")
    part1(instructions)
    part2(instructions)
}

private fun part1(instructions: List<String>) {
    val hashes = instructions.map { calculateHash(it) }
    println(hashes.sum())
}

private fun part2(instructions: List<String>) {
    val boxes = instructions.fold(mapOf<Int, Map<String, Int>>()) { acc, it -> executeInstruction(acc, it) }
    val focusingPower = calculateFocusingPower(boxes)
    println(focusingPower)
}

fun executeInstruction(boxes: Map<Int, Map<String, Int>>, instruction: String): Map<Int, Map<String, Int>> {
    val label = instruction.replace(Regex("[-=].*"), "")
    val boxNumber = calculateHash(label)

    var box = boxes.getOrElse(boxNumber) { mapOf() }
    if (instruction.contains("-")) {
        box = box.minus(label)
    } else {
        val value = instruction.replace(Regex(".*="), "").toInt()
        box = box.plus(Pair(label, value))
    }

    val result = if (box.isEmpty()) boxes.minus(boxNumber) else boxes + Pair(boxNumber, box)
    println("$instruction\t-> $result")
    return result
}

fun calculateFocusingPower(boxes: Map<Int, Map<String, Int>>): Int {
    val scores = boxes.flatMap { entry ->
        entry.value.entries.mapIndexed { index, entry2 ->
            Pair(entry2.key, (entry.key + 1) * (index + 1) * entry2.value)
        }
    }
    println(scores)

    return scores.sumOf { it.second }
}

fun calculateHash(input: String): Int {
    return input.chars().asSequence().fold(0) { acc, it -> (acc + it) * 17 % 256 }
}