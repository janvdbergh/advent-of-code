package eu.janvdb.aoc2021.day07

import eu.janvdb.aocutil.kotlin.readCommaSeparatedNumbers
import kotlin.math.abs

const val FILENAME = "input07.txt"

fun main() {
	val positions = readCommaSeparatedNumbers(2021, FILENAME).map(Int::toLong)
	executePart(positions, ::fuelNeeded1)
	executePart(positions, ::fuelNeeded2)
}

private fun executePart(positions: List<Long>, fuelNeededFunction: (List<Long>, Long) -> Long) {
	val result = LongRange(positions.minOrNull()!!, positions.maxOrNull()!!)
		.asSequence()
		.map { Pair(it, fuelNeededFunction(positions, it)) }
		.minByOrNull { it.second }

	println(result)
}

fun fuelNeeded1(positions: List<Long>, destination: Long): Long {
	return positions.sumOf { abs(it - destination) }
}

fun fuelNeeded2(positions: List<Long>, destination: Long): Long {
	return positions.sumOf {
		val delta = abs(it - destination)
		delta * (delta + 1L) / 2L
	}
}