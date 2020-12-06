package eu.janvdb.aoc2020.day06

import java.io.File

fun main() {
	val groups = readGroups()

	println(groups.map(Group::combineOr).map(Answers::count).sum())
	println(groups.map(Group::combineAnd).map(Answers::count).sum())
}

private fun readGroups(): MutableList<Group> {
	val groups = mutableListOf<Group>()

	val lines = File("inputs/input06.txt").readLines()
	var currentLines = mutableListOf<Answers>()
	for (line in lines) {
		if (line.isBlank() && !currentLines.isEmpty()) {
			groups.add(Group(currentLines))
			currentLines = mutableListOf()
		} else {
			currentLines.add(Answers(line))
		}
	}

	if (!currentLines.isEmpty()) {
		groups.add(Group(currentLines))
	}

	return groups
}

class Answers {
	val values: List<Boolean>

	constructor(line: String) {
		values = CharRange('a', 'z').map(line::contains)
	}

	constructor(values: List<Boolean>) {
		this.values = values
	}

	fun count(): Int {
		return values.count { it }
	}

	fun or(answers: Answers): Answers {
		val newValues = values.mapIndexed { index, value -> value || answers.values[index] }
		return Answers(newValues)
	}

	fun and(answers: Answers): Answers {
		val newValues = values.mapIndexed { index, value -> value && answers.values[index] }
		return Answers(newValues)
	}
}

class Group(val answers: List<Answers>) {

	fun combineOr(): Answers {
		return answers.reduce { a1, a2 -> a1.or(a2) }
	}

	fun combineAnd(): Answers {
		return answers.reduce { a1, a2 -> a1.and(a2) }
	}

}