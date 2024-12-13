package eu.janvdb.aoc2024.day13

import eu.janvdb.aocutil.kotlin.readGroupedLines
import java.math.BigDecimal

//const val FILENAME = "input13-test.txt"
const val FILENAME = "input13.txt"

const val PRIZE_OFFSET_1 = 0L
const val PRIZE_OFFSET_2 = 10_000_000_000_000L
const val TOKEN_COST_A = 3
const val TOKEN_COST_B = 1
const val PRECISION = 20

fun main() {
    val machines = readGroupedLines(2024, FILENAME).map { Machine.parse(it) }
    println(machines.mapNotNull { it.findSolutionCost(PRIZE_OFFSET_1) }.sum())
    println(machines.mapNotNull { it.findSolutionCost(PRIZE_OFFSET_2) }.sum())
}

data class Machine(val aX: Long, val aY: Long, val bX: Long, val bY: Long, val prizeX: Long, val prizeY: Long) {

    fun findSolutionCost(prizeOffset: Long): Long? {
        fun bd(x: Long): BigDecimal {
            return BigDecimal.valueOf(x).setScale(PRECISION)
        }

        fun round(x: BigDecimal): Long {
            return x.plus(BigDecimal.valueOf(0.5)).toLong()
        }

        fun intersectLines(): Pair<Long, Long> {
            val mX = -bd(aX) / bd(bX)
            val mY = -bd(aY) / bd(bY)
            val qX = (bd(prizeX) + bd(prizeOffset)) / bd(bX)
            val qY = (bd(prizeY) + bd(prizeOffset)) / bd(bY)
            val a = -(qY - qX) / (mY - mX)
            val b = mX * a + qX
            return Pair(round(a), round(b))
        }

        val (a, b) = intersectLines()
        val hasSolution = (aX * a + bX * b == prizeX + prizeOffset) && (aY * a + bY * b == prizeY + prizeOffset)
        return if (hasSolution) a * TOKEN_COST_A + b * TOKEN_COST_B else null
    }

    companion object {
        private val REGEX_A = Regex("Button A: X\\+(\\d+), Y\\+(\\d+)")
        private val REGEX_B = Regex("Button B: X\\+(\\d+), Y\\+(\\d+)")
        private val REGEX_PRIZE = Regex("Prize: X=(\\d+), Y=(\\d+)")

        fun parse(lines: List<String>): Machine {
            fun parseLine(line: String, regex: Regex): List<Long> {
                val matchResult = regex.matchEntire(line) ?: throw IllegalArgumentException("Invalid line: $line")
                return matchResult.groupValues.drop(1).map { it.toLong() }
            }

            val (aX, aY) = parseLine(lines[0], REGEX_A)
            val (bX, bY) = parseLine(lines[1], REGEX_B)
            val (prizeX, prizeY) = parseLine(lines[2], REGEX_PRIZE)
            return Machine(aX, aY, bX, bY, prizeX, prizeY)
        }
    }
}
