package eu.janvdb.aoc2024.day02

import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.abs

//const val FILENAME = "input02-test.txt"
const val FILENAME = "input02.txt"

fun main() {
    val numbers = readLines(2024, FILENAME).map { line -> line.split(Regex("\\s+")).map { number -> number.toInt() } }
    println(numbers.count { isSafe(it) })
    println(numbers.count { isSafeDampened(it) })
}

fun isSafe(levels: List<Int>): Boolean {
    val increasing = levels.zipWithNext().all { (a, b) -> b >= a }
    val decreasing = levels.zipWithNext().all { (a, b) -> b <= a }
    val differenceOk = levels.zipWithNext().map { (a, b) -> abs(b - a) }.all { it in 1..3 }
    return (increasing || decreasing) && differenceOk
}

fun isSafeDampened(levels: List<Int>): Boolean {
    if (isSafe(levels)) return true
    return levels.indices.any { index ->
        val newLevels =   levels.toMutableList()
        newLevels.removeAt(index)
        isSafe(newLevels)
    }
}
