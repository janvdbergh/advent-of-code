package eu.janvdb.aoc2021.day03

import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input03.txt"

fun main() {
	val data = readLines(2021, FILENAME)
		.map { line -> line.toCharArray().asList().map { it - '0' } }
	part1(data)
	part2(data)
}

private fun part1(data: List<List<Int>>) {
	val gamma = keepBits(data, ::mostCommonAt)
	val epsilon = keepBits(data, ::leastCommonAt)
	println("gamma=$gamma, epsilon=$epsilon, result=${gamma * epsilon}")
}

private fun part2(data: List<List<Int>>) {
	val oxygen = reduceList(data, ::mostCommonAt)
	val co2scrubber = reduceList(data, ::leastCommonAt)

	println("oxygen=$oxygen, co2scrubber=$co2scrubber, result=${oxygen * co2scrubber}")
}

private fun keepBits(data: List<List<Int>>, reductor: (List<List<Int>>, Int) -> Int): Long {
	val bits = (0..data[0].size - 1).map { reductor(data, it) }
	return toDecimal(bits)
}

private fun reduceList(data: List<List<Int>>, reductor: (List<List<Int>>, Int) -> Int): Long {
	var dataCopy = data
	for (i in data[0].indices) {
		val bitValue = reductor(dataCopy, i)
		dataCopy = dataCopy.filter { it[i] == bitValue }
		if (dataCopy.size == 1) break
	}

	return toDecimal(dataCopy[0])
}

private fun mostCommonAt(data: List<List<Int>>, position: Int): Int = data.asSequence()
	.map { it[position] }
	.groupBy { it }
	.maxByOrNull { it.value.size * 2 + it.key }!!
	.key

private fun leastCommonAt(data: List<List<Int>>, position: Int): Int = data.asSequence()
	.map { it[position] }
	.groupBy { it }
	.minByOrNull { it.value.size * 2 + it.key }!!
	.key

private fun toDecimal(gammaBits: List<Int>) = gammaBits
	.fold(0L) { acc, value -> acc * 2 + value }

