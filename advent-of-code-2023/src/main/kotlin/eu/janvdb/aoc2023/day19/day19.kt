package eu.janvdb.aoc2023.day19

import eu.janvdb.aocutil.kotlin.readGroupedLines

//const val FILENAME = "input19-test.txt"
const val FILENAME = "input19.txt"

fun main() {
    val lines = readGroupedLines(2023, FILENAME)
    val rules = Rules.parse(lines[0])
    val parts = lines[1].map(Part::parse)

    val acceptedParts = parts.filter(rules::evaluate)
    println(acceptedParts.sumOf { it.rating })
}

data class Rules(val rules: Map<String, Rule>) {
    fun evaluate(part: Part): Boolean {
        var current = Outcome("in")
        while (!current.isAccepted() && !current.isRejected()) {
            val rule = rules[current.text]!!
            current = rule.evaluate(part)
        }

        return current.isAccepted()
    }

    companion object {
        fun parse(lines: List<String>): Rules {
            val rules = lines.map(Rule::parse).associateBy { it.name }
            return Rules(rules)
        }
    }
}


data class Rule(val name: String, val conditions: List<Condition>, val defaultOutcome: Outcome) {
    fun evaluate(part: Part): Outcome {
        return conditions.find { it.matches(part) }?.outcome ?: defaultOutcome
    }

    companion object {
        fun parse(line: String): Rule {
            val parts = line.split("{")
            val name = parts[0]
            val parts2 = parts[1].substring(0, parts[1].length - 1).split(",")
            val conditions = parts2.subList(0, parts2.size - 1).map(Condition::parse)
            val defaultOutcome = Outcome(parts2.last())
            return Rule(name, conditions, defaultOutcome)
        }
    }
}

data class Condition(val variable: String, val operation: Operation, val value: Int, val outcome: Outcome) {
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
                Outcome(matchResult.groupValues[4])
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

data class Outcome(val text: String) {
    fun isAccepted() = text == "A"
    fun isRejected() = text == "R"
}

data class Part(val values: Map<String, Int>) {
    val rating = sequenceOf("x", "m", "a", "s").sumOf { values.getOrDefault(it, 0) }

    companion object {
        private val REGEX = Regex("(\\w+)=(\\d+)")
        fun parse(value: String): Part {
            val values = REGEX.findAll(value).associate { Pair(it.groupValues[1], it.groupValues[2].toInt()) }
            return Part(values)
        }
    }
}