package eu.janvdb.aoc2023.day19

import eu.janvdb.aocutil.kotlin.readGroupedLines

//const val FILENAME = "input19-test.txt"
const val FILENAME = "input19.txt"

val VARIABLES = listOf("x", "m", "a", "s")

const val MIN_VALUE = 1
const val MAX_VALUE = 4000

fun main() {
    val lines = readGroupedLines(2023, FILENAME)
    val rules = Rules.parse(lines[0])
    val parts = lines[1].map(Part::parse)

    part1(parts, rules)

    part2(rules)
}

private fun part1(parts: List<Part>, rules: Rules) {
    val acceptedParts = parts.filter(rules::evaluate)
    println(acceptedParts.sumOf { it.rating })
}

private fun part2(rules: Rules) {
    val ranges = VARIABLES.associateWith { MIN_VALUE..MAX_VALUE }
    val result = rules.countAccepted("in", ranges)
    println(result)
}

data class Rules(val rules: Map<String, Rule>) {
    fun evaluate(part: Part): Boolean {
        var current = "in"
        while (!current.isAccepted() && !current.isRejected()) {
            val rule = rules[current]!!
            current = rule.evaluate(part)
        }

        return current.isAccepted()
    }

    fun countAccepted(outcome: String, ranges: Map<String, IntRange>, index: Int = 0): Long {
        val spaces = (0..<index).joinToString("", "", "+-") { "| " }
        println("$spaces$ranges -> $outcome")

        if (outcome.isAccepted())
            return 1L * ranges.values.fold(1L) { acc, range -> acc * (range.last - range.first + 1) }
        if (outcome.isRejected())
            return 0L
        if (ranges.values.any { it.last < it.first })
            return 0L

        val rule = rules[outcome]!!

        var currentRanges = ranges
        var sum = 0L
        rule.conditions.forEach { condition ->
            val currentRange = currentRanges[condition.variable]!!
            val newRange1 =
                if (condition.operation == Operation.LT) currentRange.first..<condition.value else condition.value + 1..currentRange.last
            val newRange2 =
                if (condition.operation == Operation.LT) condition.value..currentRange.last else currentRange.first..condition.value

            val newRanges = currentRanges + Pair(condition.variable, newRange1)
            sum += countAccepted(condition.outcome, newRanges, index + 1)

            currentRanges = currentRanges + Pair(condition.variable, newRange2)
        }

        sum += countAccepted(rule.defaultOutcome, currentRanges, index + 1)

        return sum
    }

    companion object {
        fun parse(lines: List<String>): Rules {
            val rules = lines.map(Rule::parse).associateBy { it.name }
            return Rules(rules)
        }
    }
}


data class Rule(val name: String, val conditions: List<Condition>, val defaultOutcome: String) {
    fun evaluate(part: Part): String {
        return conditions.find { it.matches(part) }?.outcome ?: defaultOutcome
    }

    companion object {
        fun parse(line: String): Rule {
            val parts = line.split("{")
            val name = parts[0]
            val parts2 = parts[1].substring(0, parts[1].length - 1).split(",")
            val conditions = parts2.subList(0, parts2.size - 1).map(Condition::parse)
            val defaultOutcome = parts2.last()
            return Rule(name, conditions, defaultOutcome)
        }
    }
}

data class Condition(val variable: String, val operation: Operation, val value: Int, val outcome: String) {
    fun matches(part: Part): Boolean {
        val partValue = part.values[variable]!!
        return operation.evaluate(partValue, value)
    }

    companion object {
        private val REGEX = Regex("(\\w+)([<>])(\\d+):(\\w+)")
        fun parse(value: String): Condition {
            val matchResult = REGEX.matchEntire(value)!!
            return Condition(
                matchResult.groupValues[1],
                Operation.parse(matchResult.groupValues[2]),
                matchResult.groupValues[3].toInt(),
                matchResult.groupValues[4]
            )
        }
    }
}

enum class Operation(val text: String) {
    GT(">"), LT("<");

    fun evaluate(x: Int, y: Int) = when (this) {
        LT -> x < y
        GT -> x > y
    }

    companion object {
        fun parse(value: String) = entries.find { it.text == value }!!
    }
}

fun String.isAccepted() = equals("A")
fun String.isRejected() = equals("R")

data class Part(val values: Map<String, Int>) {
    val rating = VARIABLES.sumOf { values.getOrDefault(it, 0) }

    companion object {
        private val REGEX = Regex("(\\w+)=(\\d+)")
        fun parse(value: String): Part {
            val values = REGEX.findAll(value).associate { Pair(it.groupValues[1], it.groupValues[2].toInt()) }
            return Part(values)
        }
    }
}