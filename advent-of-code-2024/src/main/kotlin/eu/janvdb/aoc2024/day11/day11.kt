package eu.janvdb.aoc2024.day11

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input11-test.txt"
const val FILENAME = "input11.txt"

fun main() {
    val input = readLines(2024, FILENAME)
        .flatMap { it.split(" ") }
        .map { it.toLong() }
        .groupingBy { it }
        .fold(0L) { acc, _ -> acc + 1 }

    iterate(input, 25)
    iterate(input, 75)
}

private fun iterate(input: Map<Long, Long>, iterations: Int) {
    var numbers = input
    for (i in 0 until iterations) {
        numbers = tick(numbers)
    }
    println(numbers.map { it.value }.sum())
}

fun tick(numbers: Map<Long, Long>): Map<Long, Long> {
    return numbers.asSequence()
        .flatMap { (number, count) -> split(number, count) }
        .groupingBy { it.first }
        .fold(0) { acc, (_, count) -> acc + count }
}

fun split(number: Long, count: Long): Sequence<Pair<Long, Long>> {
    if (number == 0L) return sequenceOf(Pair(1, count))
    val str = number.toString()
    if (str.length % 2 == 0) {
        return sequenceOf(
            Pair(str.substring(0, str.length / 2).toLong(), count),
            Pair(str.substring(str.length / 2).toLong(), count)
        )
    }
    return sequenceOf(Pair(number * 2024, count))
}

