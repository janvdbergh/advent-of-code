package eu.janvdb.aoc2021.day14

import eu.janvdb.aocutil.kotlin.readGroupedLines

const val FILENAME = "input14.txt"

const val NUMBER_OF_STEPS = 40

fun main() {
	val lines = readGroupedLines(2021, FILENAME)
	val template = PolymerizationTemplate.parse(lines[0][0])
	template.printValues()

	val rules = lines[1].map(InsertionRule::parse)

	var updatedTemplate = template
	for (i in 0 until NUMBER_OF_STEPS) {
		updatedTemplate = updatedTemplate.applyRules(rules)
		updatedTemplate.printValues()
	}
}

data class ElementCombination(val element1: Char, val element2: Char)

data class PolymerizationTemplate(val combinations: Map<ElementCombination, Long>) {
	fun applyRules(rules: List<InsertionRule>): PolymerizationTemplate {
		val newCombinations = mutableMapOf<ElementCombination, Long>()
		combinations.forEach { pair ->
			val rule = rules.find { it.startCombination == pair.key }
			rule?.resultCombinations?.forEach { resultingPair ->
				val currentValue = newCombinations.getOrDefault(resultingPair, 0L)
				newCombinations[resultingPair] = currentValue + pair.value
			}
		}
		return PolymerizationTemplate(newCombinations)
	}

	fun printValues() {
		val elementsPerType = combinations.asSequence()
			.flatMap { sequenceOf(Pair(it.key.element1, it.value), Pair(it.key.element2, it.value)) }
			.groupingBy { it.first }
			.aggregate { _, acc: Long?, element, _ -> (acc ?: 0L) + element.second }
			.map { entry -> Pair(entry.key, (entry.value + 1) / 2) }
		// This is a bit of a heck. Most characters occur double, except first and last.
		// By adding one to them and using integer division, the correct number gets out.

		val min = elementsPerType.minByOrNull { it.second }!!
		val max = elementsPerType.maxByOrNull { it.second }!!
		println("Length:${combinations.size - 1} Min:${min} Max:${max} Difference: ${max.second - min.second}")
	}

	companion object {
		fun parse(line: String): PolymerizationTemplate {
			val pairs = mutableMapOf<ElementCombination, Long>()
			for (i in 0 until line.length - 1) {
				val pair = ElementCombination(line[i], line[i + 1])
				val currentValue = pairs.getOrDefault(pair, 0L)
				pairs[pair] = currentValue + 1L
			}
			return PolymerizationTemplate(pairs)
		}
	}
}

data class InsertionRule(val startCombination: ElementCombination, val resultCombinations: List<ElementCombination>) {
	companion object {
		fun parse(line: String): InsertionRule {
			val parts = line.split("->").map(String::trim)
			val startCombination = ElementCombination(parts[0][0], parts[0][1])
			val resultCombination1 = ElementCombination(parts[0][0], parts[1][0])
			val resultCombination2 = ElementCombination(parts[1][0], parts[0][1])

			return InsertionRule(startCombination, listOf(resultCombination1, resultCombination2))
		}
	}
}