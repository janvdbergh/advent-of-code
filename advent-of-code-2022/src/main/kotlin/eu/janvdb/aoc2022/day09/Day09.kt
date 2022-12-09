package eu.janvdb.aoc2022.day09

import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.abs

//const val FILENAME = "input09-test.txt"
//const val FILENAME = "input09-test2.txt"
const val FILENAME = "input09.txt"

val ORIGIN = Location(0, 0)

fun main() {
	runWithSize(2)
	runWithSize(10)
}

private fun runWithSize(size: Int) {
	var state = State(List(size) { ORIGIN })
	val locations = mutableSetOf(ORIGIN)

	readLines(2022, FILENAME)
		.map(String::toInstruction)
		.forEach {
			for (i in 0 until it.steps) {
				state = state.move(it.direction)
				locations += state.locations.last()
			}
		}

	println(locations.size)
}

data class Location(val x: Int, val y: Int) {
	fun move(direction: Direction): Location {
		return when (direction) {
			Direction.L -> Location(x - 1, y)
			Direction.R -> Location(x + 1, y)
			Direction.U -> Location(x, y - 1)
			Direction.D -> Location(x, y + 1)
		}
	}

	fun follow(head: Location): Location {
		val dx = head.x - x
		val dy = head.y - y

		val absX = abs(dx)
		val absY = abs(dy)

		val dirX = if (dx == 0) 0 else dx / absX
		val dirY = if (dy == 0) 0 else dy / absY

		val valX = if (absX == 2 || (absX == 1 && absY == 2)) 1 else 0
		val valY = if (absY == 2 || (absX == 2 && absY == 1)) 1 else 0

		return Location(x + valX * dirX, y + valY * dirY)
	}
}


data class State(val locations: List<Location>) {
	fun move(direction: Direction): State {
		val newLocations = locations.toMutableList()
		newLocations[0] = locations[0].move(direction)
		for (i in 1 until locations.size) {
			newLocations[i] = locations[i].follow(newLocations[i - 1])
		}
		return State(newLocations)
	}
}

enum class Direction { L, U, R, D }

data class Instruction(val direction: Direction, val steps: Int)

fun String.toInstruction(): Instruction = Instruction(
	Direction.valueOf(substring(0, 1)),
	substring(2).toInt()
)