package eu.janvdb.aoc2020.day06

import eu.janvdb.aocutil.kotlin.readGroupedLines

fun main() {
	val groups = readGroups()

	println(groups.map(Group::combineOr).map(Answers::count).sum())
	println(groups.map(Group::combineAnd).map(Answers::count).sum())
}

private fun readGroups(): List<Group> {
	return readGroupedLines("input06.txt").map { Group(it.map(::Answers))	}
}

class Answers {
	private val values: List<Boolean>

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

class Group(private val answers: List<Answers>) {

	fun combineOr(): Answers {
		return answers.reduce { a1, a2 -> a1.or(a2) }
	}

	fun combineAnd(): Answers {
		return answers.reduce { a1, a2 -> a1.and(a2) }
	}

}