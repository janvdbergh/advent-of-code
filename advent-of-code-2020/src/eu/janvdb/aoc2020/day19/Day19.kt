package eu.janvdb.aoc2020.day19

import eu.janvdb.aoc2020.common.readGroupedLines

fun main() {
	val groupedLines = readGroupedLines("input19b.txt")
	val rules = groupedLines[0].map(::parseRule).map { Pair(it.ruleNumber, it) }.toMap()
	val regexes = calculateRegexes(rules)
	val regex = Regex("^" + regexes[0] + "$")

	val messages = groupedLines[1]
	println(messages.filter(regex::matches).count())
}

fun parseRule(rule: String): Rule {
	val ruleParts = rule.split(": ")
	val ruleNumber = ruleParts[0].toInt()

	if (ruleParts[1][0] == '"') {
		return SimpleRule(ruleNumber, ruleParts[1][1])
	}

	val groups = ruleParts[1].split(" | ")
	val subRules = groups.map { it.split(" ").map(String::toInt) }
	return ComplexRule(ruleNumber, subRules)
}

fun calculateRegexes(rules: Map<Int, Rule>): Map<Int, String> {
	val regexes = mutableMapOf<Int, String>()
	while (regexes.size != rules.size) {
		for (rule in rules.values) {
			if (regexes.containsKey(rule.ruleNumber)) continue
			if (rule.canCalculateRegex(regexes)) {
				regexes[rule.ruleNumber] = rule.asRegex(regexes)
			}
		}
	}
	return regexes
}

abstract class Rule(val ruleNumber: Int) {
	abstract fun canCalculateRegex(regexes: Map<Int, String>): Boolean
	abstract fun asRegex(regexes: Map<Int, String>): String
}

class SimpleRule(ruleNumber: Int, val matchingChar: Char) : Rule(ruleNumber) {
	override fun canCalculateRegex(regexes: Map<Int, String>) = true

	override fun asRegex(regexes: Map<Int, String>): String {
		return "$matchingChar"
	}

	override fun toString(): String {
		return "$ruleNumber: \"$matchingChar\""
	}
}

class ComplexRule(ruleNumber: Int, val subRules: List<List<Int>>) : Rule(ruleNumber) {
	override fun canCalculateRegex(regexes: Map<Int, String>): Boolean {
		return subRules.asSequence().flatMap(List<Int>::asSequence).all(regexes::containsKey)
	}

	override fun asRegex(regexes: Map<Int, String>): String {
		fun asRegex(ruleNumber: Int): String {
			return regexes[ruleNumber]!!
		}

		fun asRegex(subRule: List<Int>): String {
			return subRule.joinToString(separator = "", transform = ::asRegex)
		}
		if (subRules.size == 1) return asRegex(subRules[0])
		return subRules.joinToString(separator = "|", prefix = "(", postfix = ")", transform = ::asRegex)
	}

	override fun toString(): String {
		val ruleList = subRules.map { it.joinToString(separator = " ") }.joinToString(separator = " | ")
		return "$ruleNumber: $ruleList"
	}
}