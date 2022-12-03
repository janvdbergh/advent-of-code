package eu.janvdb.aoc2022.day03

import eu.janvdb.aocutil.kotlin.assertTrue
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input03-test.txt"
const val FILENAME = "input03.txt"

fun main() {
	val priorityLists = readLines(2022, FILENAME)
		.map(::toPriorities)
		.toList()

	part1(priorityLists)
	part2(priorityLists)
}

fun part1(priorityLists: List<List<Int>>) {
	val priorities = priorityLists
		.map(::split)
		.map { it.first.intersect(it.second).sum() }
		.sum()
	println(priorities)
}

fun part2(priorityLists: List<List<Int>>) {
	val groups = priorityLists
		.mapIndexed { index, list -> Pair(index, list) }
		.groupBy { it.first / 3 }
		.map { it.value }
		.map { it.map { it.second.toSet() } }
	assertTrue(groups.size * 3 == priorityLists.size)

	val unique = groups.map { it.reduce(Set<Int>::intersect) }
	println(unique)

	println(unique.sumOf { it.sum() })
}

fun <T> split(list: List<T>): Pair<List<T>, List<T>> {
	val mid = list.size / 2
	assertTrue(mid * 2 == list.size)

	val result = Pair(list.subList(0, mid), list.subList(mid, list.size))
	assertTrue(result.first.size == mid)
	assertTrue(result.second.size == mid)
	return result
}

fun toPriorities(part: String): List<Int> {
	return part.toCharArray().asSequence()
		.map(::mapToPriority)
		.toList()
}

fun mapToPriority(ch: Char): Int {
	if (ch in 'a'..'z') return ch - 'a' + 1
	return ch - 'A' + 27
}
