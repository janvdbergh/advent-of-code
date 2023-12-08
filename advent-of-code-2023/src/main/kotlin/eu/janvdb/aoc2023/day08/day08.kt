package eu.janvdb.aoc2023.day08

import eu.janvdb.aocutil.kotlin.lcm
import eu.janvdb.aocutil.kotlin.readGroupedLines

//const val FILENAME = "input08-test1.txt"
//const val FILENAME = "input08-test2.txt"
//const val FILENAME = "input08-test3.txt"
const val FILENAME = "input08.txt"

val LINE_REGEX = Regex("(\\S+) = \\((\\S+), (\\S+)\\)")

fun main() {
	val lines = readGroupedLines(2023, FILENAME)
	val instructions = lines[0][0].toCharArray().map { Instruction.valueOf(it.toString()) }
	val nodeMap = NodeMap.parse(lines[1])

	part1(instructions, nodeMap)
	part2(instructions, nodeMap) // > 24253
}

private fun part1(instructions: List<Instruction>, nodeMap: NodeMap) {
	println(findIterations(nodeMap, instructions, "AAA"))
}

private fun part2(instructions: List<Instruction>, nodeMap: NodeMap) {
	// apparently the input is structured, so we can use the least common multiple for each start node
	// see https://www.reddit.com/r/adventofcode/comments/18dfpub/2023_day_8_part_2_why_is_spoiler_correct/
	val startNodes = nodeMap.nodes.keys.filter { it.endsWith('A') }
	val steps = startNodes.map { findIterations(nodeMap, instructions, it) }
	val result = steps.reduce { a, b -> lcm(a, b) }
	println(result)
}

private fun findIterations(nodeMap: NodeMap, instructions: List<Instruction>, start: String): Long {
	var iterations = 0L
	var current = start
	while (!current.endsWith('Z')) {
		iterations++
		current = nodeMap.apply(current, instructions)
	}
	val numberOfSteps = iterations * instructions.size
	return numberOfSteps
}

data class Node(val from: String, val right: String, val left: String) {
	companion object {
		fun parse(line: String): Node {
			val matchResult = LINE_REGEX.matchEntire(line) ?: throw IllegalArgumentException("Invalid line: $line")
			val (from, right, left) = matchResult.destructured
			return Node(from, left, right)
		}
	}
}

data class NodeMap(val nodes: Map<String, Node>) {
	fun apply(nodeName: String, instructions: List<Instruction>): String {
		return instructions.fold(nodeName) { current, instruction ->
			val node = nodes[current] ?: throw IllegalArgumentException("Unknown node: $current")
			when (instruction) {
				Instruction.R -> node.right
				Instruction.L -> node.left
			}
		}
	}

	companion object {
		fun parse(lines: List<String>): NodeMap {
			val instructions = lines
				.map { Node.parse(it) }
				.associateBy { it.from }
			return NodeMap(instructions)
		}
	}
}

enum class Instruction { R, L }