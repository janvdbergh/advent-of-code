package eu.janvdb.aoc2024.day19

import eu.janvdb.aocutil.kotlin.readGroupedLines

//const val FILENAME = "input19-test.txt"
const val FILENAME = "input19.txt"

fun main() {
    val input = readGroupedLines(2024, FILENAME)
    val towels = input[0][0].split(",").map { it.trim() }
    val sequences = input[1]

    val numberOfMappings = sequences.map { getNumberOfMappings(towels, it) }
    println(numberOfMappings.count { it != 0L })
    println(numberOfMappings.sum())
}

val cache = mutableMapOf<String, Long>()
fun getNumberOfMappings(towels: List<String>, sequence: String) =
    cache.getOrPut(sequence) { getNumberOfMappingsInternal(towels, sequence) }

fun getNumberOfMappingsInternal(towels: List<String>, sequence: String): Long {
    if (sequence.isEmpty()) return 1L
    return towels.filter { sequence.startsWith(it) }
        .sumOf { getNumberOfMappings(towels, sequence.substring(it.length)) }
}
