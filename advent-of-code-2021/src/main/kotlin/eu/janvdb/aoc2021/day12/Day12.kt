package eu.janvdb.aoc2021.day12

import eu.janvdb.aocutil.kotlin.readLines
import java.util.*

const val FILENAME = "input12.txt"

fun main() {
	val connections = readLines(2021, FILENAME)
		.map { it.split('-') }
		.flatMap { listOf(Pair(it[0], it[1]), Pair(it[1], it[0])) }

	val paths1 = determinePathsWithoutLoops("start", "end", connections, ::canVisit1)
	println(paths1.size)

	val paths2 = determinePathsWithoutLoops("start", "end", connections, ::canVisit2)
	println(paths2.size)
}

fun canVisit1(currentPath: List<String>, cave: String): Boolean {
	return cave.uppercase() == cave || !currentPath.contains(cave)
}

fun canVisit2(currentPath: List<String>, cave: String): Boolean {
	if (!currentPath.contains(cave)) return true
	if (cave.uppercase() == cave) return true
	if (cave=="start" || cave=="end") return false

	return currentPath.filter { it.lowercase()==it }
		.groupBy { it }
		.none { it.value.size>1 }
}

fun determinePathsWithoutLoops(
	start: String,
	end: String,
	connections: Collection<Pair<String, String>>,
	canVisitFunction: (List<String>, String) -> Boolean
): List<List<String>> {
	val result = mutableListOf<List<String>>()
	val pathsToVisit: Deque<List<String>> = LinkedList()
	pathsToVisit.addFirst(listOf(start))

	while (!pathsToVisit.isEmpty()) {
		val pathToVisit = pathsToVisit.removeFirst()

		val endPoint = pathToVisit.last()
		if (endPoint == end) {
			result.add(pathToVisit)
		} else {
			connections.asSequence()
				.filter { it.first == endPoint }
				.filter { canVisitFunction(pathToVisit, it.second) }
				.forEach {
					val newPath = pathToVisit + it.second
					pathsToVisit.addFirst(newPath)
				}
		}
	}

	return result
}