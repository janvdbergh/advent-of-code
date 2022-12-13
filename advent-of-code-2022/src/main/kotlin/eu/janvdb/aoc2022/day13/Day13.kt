package eu.janvdb.aoc2022.day13

import eu.janvdb.aocutil.kotlin.readGroupedLines
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input13-test.txt"
const val FILENAME = "input13.txt"

fun main() {
	part1()
	part2()
}

private fun part1() {
	val pairs = readGroupedLines(2022, FILENAME)
		.map { Pair(it[0].toTreeNode(), it[1].toTreeNode()) }

	val sum = pairs
		.mapIndexed { index, it -> Pair(index + 1, it) }
		.filter { it.second.first < it.second.second }
		.sumOf { it.first }

	println(sum)
}

private fun part2() {
	val divider1 = TreeNode(listOf(TreeLeaf(2)))
	val divider2 = TreeNode(listOf(TreeLeaf(6)))

	val packets = readLines(2022, FILENAME)
		.filter { it.isNotBlank() }
		.map { it.toTreeNode() }
		.plus(listOf(divider1, divider2))
		.sorted()

	val result = (1 + packets.indexOf(divider1)) * (1 + packets.indexOf(divider2))
	println(result)
}
