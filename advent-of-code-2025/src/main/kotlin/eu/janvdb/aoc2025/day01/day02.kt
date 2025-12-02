package eu.janvdb.aoc2025.day02

import eu.janvdb.aocutil.kotlin.readFile

const val FILENAME = "input02.txt"
//const val FILENAME = "input02-test.txt"

fun main() {
    validateAgainstRegex(Regex("([0-9]+)\\1"))
    validateAgainstRegex(Regex("([0-9]+)\\1+"))
}

private fun validateAgainstRegex(regEx: Regex) {
    val result = readFile(2025, FILENAME)
        .split(",")
        .map { it.trim() }
        .flatMap { Id.parse(it) }
        .filter { it.isInvalid(regEx) }
        .sumOf { it.value }
    println(result)
}

data class Id(val value: Long) {
    fun isInvalid(invalidRegex: Regex) = invalidRegex.matches(value.toString())

    companion object {
        fun parse(value: String): Sequence<Id> {
            val parts = value.split("-")
            val start = parts[0].toLong()
            val end = parts[1].toLong()
            return (start..end).asSequence().map { Id(it) }
        }
    }
}
