package eu.janvdb.aoc2020.day07

import eu.janvdb.aocutil.kotlin.readLines

val SHINY_GOLD = "shiny gold"
fun main() {
	val bags = readLines("input07.txt")
		.map(::Bag)
		.map { Pair(it.name, it) }
		.toMap()


	val results = bags.values.filter { bag -> bag.canContain(SHINY_GOLD, bags) }
		.map(Bag::name)
	println(results.size)

	val shinyGoldBag = bags.get(SHINY_GOLD)
	if (shinyGoldBag != null) {
		println(shinyGoldBag.countIncludingNestedBags(bags) - 1)
	}
}

val LINE_REGEX = Regex("^(.*) contain (.*)\\.$")
val CONTENTS_DELIMITER_REGEX = Regex("\\s*,\\s*")

class Bag(description: String) {
	val name: String
	val contents: List<BagDescription>

	init {
		val matchResult = LINE_REGEX.matchEntire(description)
		if (matchResult == null) {
			throw IllegalArgumentException(description)
		}

		this.name = removeBagsFromName(matchResult.groupValues[1])

		val contents = matchResult.groupValues[2]
		if (contents == "no other bags")
			this.contents = listOf()
		else
			this.contents = contents.split(CONTENTS_DELIMITER_REGEX).map(::BagDescription)
	}

	override fun toString(): String {
		return "$name contains $contents"
	}

	fun canContain(bagToFind: String, bags: Map<String, Bag>): Boolean {
		for (content in contents) {
			if (content.name == bagToFind) return true
			val nestedBag = bags[content.name]
			if (nestedBag != null && nestedBag.canContain(bagToFind, bags)) return true
		}

		return false
	}

	fun countIncludingNestedBags(bags: Map<String, Bag>): Int {
		var count = 1
		for (content in contents) {
			val nestedBag = bags[content.name]
			if (nestedBag != null) count += content.number * nestedBag.countIncludingNestedBags(bags)
		}

		return count
	}
}

val BAG_DESCRIPTION_REGEX = Regex("^(\\d+) (.*)$")

class BagDescription(description: String) {
	val number: Int
	val name: String

	init {
		val matchResult = BAG_DESCRIPTION_REGEX.matchEntire(description)
		if (matchResult == null) {
			throw IllegalArgumentException(description)
		}

		this.number = matchResult.groupValues[1].toInt()
		this.name = removeBagsFromName(matchResult.groupValues[2])
	}

	override fun toString(): String {
		return "$number $name"
	}
}

val BAGS_REGEX = Regex(" bags?$")
fun removeBagsFromName(input: String): String {
	return input.replace(BAGS_REGEX, "")
}
