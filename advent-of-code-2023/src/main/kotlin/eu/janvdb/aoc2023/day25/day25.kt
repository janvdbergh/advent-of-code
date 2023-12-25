package eu.janvdb.aoc2023.day25

import eu.janvdb.aocutil.kotlin.readLines
import java.util.*

//const val FILENAME = "input25-test.txt"
const val FILENAME = "input25.txt"

fun main() {
	val diagram = Diagram.parse(readLines(2023, FILENAME))
	val firstConnection = diagram.getMostUsedConnection()

	val diagram2 = diagram.removeConnections(setOf(firstConnection))
	val secondConnection = diagram2.getMostUsedConnection()

	val diagram3 = diagram2.removeConnections(setOf(secondConnection))
	val thirdConnection = diagram3.getMostUsedConnection()

	val diagram4 = diagram3.removeConnections(setOf(thirdConnection))
	val sizes = diagram4.getGroups().map { it.size }
	val product = sizes.fold(1) { acc: Int, i: Int -> acc * i }
	println("$firstConnection $secondConnection $thirdConnection $sizes $product")
}

data class Diagram(val components: Set<String>, val connections: Set<Connection>) {
	private val connectionsMap: Map<String, List<Connection>> = components.associateWith { from ->
		connections.filter { it.first == from } +
				connections.filter { it.second == from }.map { Connection(it.second, it.first) }
	}

	fun getMostUsedConnection(): Connection {
		val longestPaths = components.associateWith { start ->
			buildShortestPathMap(start).maxBy { it.value.size }.value
		}

		return longestPaths.values.asSequence().flatMap { it }
			.groupingBy { it }.eachCount()
			.maxBy { it.value }.key
	}

	private fun buildShortestPathMap(from: String): Map<String, List<Connection>> {
		val bestPaths = mutableMapOf(Pair(from, listOf<Connection>()))
		val toDo = LinkedList<String>()
		toDo.add(from)

		while (toDo.isNotEmpty()) {
			val current = toDo.removeFirst()
			val currentPath = bestPaths[current]!!
			val currentLength = currentPath.size

			connectionsMap[current]!!.forEach { next ->
				if (next.second !in bestPaths || bestPaths[next.second]!!.size > currentLength + 1) {
					bestPaths[next.second] = currentPath + next
					toDo.add(next.second)
				}
			}
		}

		return bestPaths
	}

	fun getGroups(): List<Set<String>> {
		val remainingComponents = components.toMutableSet()
		val remainingConnections = connections.toMutableSet()
		val result = mutableListOf<MutableSet<String>>()

		while (remainingComponents.isNotEmpty()) {
			val first = remainingComponents.first()
			val group = mutableSetOf(first)
			remainingComponents.remove(first)

			while (true) {
				val connections1 = remainingConnections.asSequence()
					.filter { it.first in group }
					.map { it.second }
					.toSet()
				val connections2 = remainingConnections.asSequence()
					.filter { it.second in group }
					.map { it.first }
					.toSet()
				if (connections1.isEmpty() && connections2.isEmpty()) break

				group.addAll(connections1)
				group.addAll(connections2)
				remainingComponents.removeAll(connections1)
				remainingComponents.removeAll(connections2)
				remainingConnections.removeIf { it.first in group && it.second in group }
			}

			result.add(group)
		}

		return result
	}

	fun removeConnections(connectionsToRemove: Set<Connection>): Diagram {
		return Diagram(components, connections - connectionsToRemove)
	}

	companion object {
		fun parse(lines: List<String>): Diagram {
			val components = mutableSetOf<String>()
			val connections = mutableSetOf<Connection>()

			lines.forEach { line ->
				val parts = line.replace(":", "").split(" ")
				components.addAll(parts)
				connections.addAll(parts.asSequence().drop(1).map { Connection(parts[0], it) })
			}

			return Diagram(components, connections)
		}
	}
}

data class Connection(val first: String, val second: String) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Connection
		return (first == other.first && second == other.second) || (first == other.second && second == other.first)
	}

	override fun hashCode() = first.hashCode() + second.hashCode()
}