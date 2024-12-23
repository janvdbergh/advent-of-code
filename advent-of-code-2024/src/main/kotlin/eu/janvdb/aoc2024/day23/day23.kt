package eu.janvdb.aoc2024.day23

import eu.janvdb.aocutil.kotlin.findCombinations
import eu.janvdb.aocutil.kotlin.readLines
import java.util.*

//const val FILENAME = "input23-test.txt"
const val FILENAME = "input23.txt"

fun main() {
	val connections = readLines(2024, FILENAME).map(Connection::fromString).toSet()
	println(part1(connections))
	println(part2(connections))
}

fun part1(connections: Set<Connection>): Int {
	val nodes = getUniqueNodes(connections)
	return findCombinations(nodes, 3)
		.filter { group ->
			connections.contains(Connection(group[0], group[1])) &&
					connections.contains(Connection(group[0], group[2])) &&
					connections.contains(Connection(group[1], group[2]))
		}
		.filter { group -> group.any { node -> node.canBeHistorian() } }
		.count()
}

fun part2(connections: Set<Connection>): String {
	val nodes = getUniqueNodes(connections).toSet()
	var currentBest = setOf<Node>()
	val toDo = LinkedList(listOf(Pair<Set<Node>, Set<Node>>(emptySet(), nodes)))
	val done = mutableSetOf<Pair<Set<Node>, Set<Node>>>()

	while (toDo.isNotEmpty()) {
		val current = toDo.removeLast()
		if (done.contains(current)) continue
		done.add(current)

		val (currentNodes, remainingNodes) = current
		val connectedRemainingNodes = remainingNodes
			.filter { remainingNode -> currentNodes.all { connections.contains(Connection(remainingNode, it)) } }
			.toSet()

		if (currentBest.size >= currentNodes.size + connectedRemainingNodes.size) continue
		if (currentNodes.size > currentBest.size)
			currentBest = currentNodes

		connectedRemainingNodes
			.forEach { toDo.add(Pair(currentNodes + it, connectedRemainingNodes - it)) }
	}

	return currentBest.map { it.name }.sorted().joinToString(",")
}

private fun getUniqueNodes(connections: Set<Connection>) =
	connections.asSequence().flatMap { sequenceOf(it.from, it.to) }.distinct().toList()

data class Node(val name: String) {
	override fun toString() = name
	fun canBeHistorian() = name.startsWith('t')
}

data class Connection(val from: Node, val to: Node) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Connection) return false
		return (from == other.from && to == other.to) || (from == other.to && to == other.from)
	}

	override fun hashCode(): Int {
		return from.hashCode() + to.hashCode()
	}

	override fun toString(): String {
		return "$from-$to"
	}

	companion object {
		fun fromString(input: String): Connection {
			val (from, to) = input.split('-')
			return Connection(Node(from), Node(to))
		}
	}
}