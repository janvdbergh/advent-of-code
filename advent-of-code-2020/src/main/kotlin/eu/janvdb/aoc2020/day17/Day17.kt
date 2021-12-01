package eu.janvdb.aoc2020.day17

import eu.janvdb.aocutil.kotlin.Combinations
import eu.janvdb.aocutil.kotlin.MinMax
import eu.janvdb.aocutil.kotlin.readLines

fun main() {
	run(numberOfDimensions = 3)
	run(numberOfDimensions = 4)
}

private fun run(numberOfDimensions: Int) {
	println("$numberOfDimensions dimensions")
	val initialState = readInitialState("input17.txt", numberOfDimensions)
	initialState.print()

	var currentState = initialState
	for (i in 0 until 6) {
		println("Step $i")
		currentState = currentState.step()
	}

	currentState.print()
	println("${currentState.numberActive} cubes are active.")
}

fun readInitialState(fileName: String, numberOfDimensions: Int): Universe {
	val lines = readLines(2020, fileName)
	val points = mutableSetOf<Point>()
	for (y in lines.indices) {
		for (x in lines[y].indices) {
			if (lines[y][x] == '#') {
				val coordinates = MutableList(numberOfDimensions) { 0 }
				coordinates[0] = x
				coordinates[1] = y
				points += Point(coordinates)
			}
		}
	}

	return Universe(points)
}

data class Point(val coordinates: List<Int>)

data class Universe(val activeCubes: Set<Point>) {
	private val numberOfDimensions = activeCubes.first().coordinates.size
	private val minimums = IntRange(0, numberOfDimensions - 1)
		.map { index -> activeCubes.minOfOrNull { point -> point.coordinates[index] } ?: 0 }
	private val maximums = IntRange(0, numberOfDimensions - 1)
		.map { index -> activeCubes.maxOfOrNull { point -> point.coordinates[index] } ?: 0 }

	val numberActive get() = activeCubes.size

	fun step(): Universe {
		fun shouldBeActiveInNextRound(point: Point): Boolean {
			fun countNeighbours(point: Point): Int {
				val minMaxes = IntRange(0, numberOfDimensions - 1)
					.map { MinMax(point.coordinates[it] - 1, point.coordinates[it] + 1) }
				return Combinations.iterate(minMaxes).map(::Point).count { it != point && this.hasActiveCube(it) }
			}

			val count = countNeighbours(point)
			val currentActive = hasActiveCube(point)
			return count == 3 || (currentActive && count == 2)
		}

		val minMaxes = IntRange(0, numberOfDimensions - 1).reversed().map { MinMax(minimums[it] - 1, maximums[it] + 1) }
		val newActiveCubes = Combinations.iterate(minMaxes).map(List<Int>::reversed).map(::Point)
			.filter(::shouldBeActiveInNextRound)
			.toSet()

		return Universe(newActiveCubes)
	}

	fun print() {
		val minMaxes = IntRange(0, numberOfDimensions - 1).reversed().map { MinMax(minimums[it], maximums[it]) }
		Combinations.iterate(minMaxes).map(List<Int>::reversed).forEach { coordinates ->
			val newLine = coordinates[0] == minimums[0]
			if (newLine) println()

			val newBlock = newLine && coordinates[1] == minimums[1]
			if (newBlock) {
				println("At ${coordinates.subList(2, numberOfDimensions)}")
			}

			if (hasActiveCube(Point(coordinates))) print('#') else print('.')
		}
		println()
	}

	private fun hasActiveCube(point: Point): Boolean {
		return activeCubes.contains(point)
	}

}

