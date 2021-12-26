package eu.janvdb.aoc2021.day23

import eu.janvdb.aocutil.kotlin.Move
import eu.janvdb.aocutil.kotlin.findShortestPath
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines
import eu.janvdb.aocutil.kotlin.runWithTimer
import kotlin.math.abs

const val FILENAME = "input23.txt"

val costs = listOf(1, 10, 100, 1000)

fun main() {
	val game = Game()

	val cost = runWithTimer("Game") { game.solve() }
	println(cost)
}

class Game {
	private val lines = readLines(2021, FILENAME)
	private val numberOfTypes = (lines[1].length - 4) / 2
	private val corridorSize = lines[1].length - 2 - numberOfTypes
	private val numberPerType = lines.size - 3
	private val locations = generateLocations()
	private val corridorIndicesPerX = locations.filter { it.type == null }.associate { Pair(it.position.x, it.index) }

	private val startState = parseStartState()
	private val endState = generateEndState()


	private fun generateLocations(): List<Location> {
		val result = mutableListOf<Location>()

		for (i in 1 until lines[1].length - 1)
			if (i < 3 || i > numberOfTypes * 2 + 1 || i % 2 == 0)
				result.add(Location(result.size, Point2D(i, 0), null, null))

		for (number in 0 until numberPerType)
			for (type in 0 until numberOfTypes)
				result.add(Location(result.size, Point2D(type * 2 + 3, number + 1), type, number))

		return result
	}

	private fun parseStartState(): State {
		val result = IntArray(corridorSize + numberOfTypes * numberPerType) { -1 }
		for (y in lines.indices) {
			for (x in lines[y].indices) {
				if (lines[y][x] != '.') {
					val location = locations.find { it.position.x == x && it.position.y == y - 1 }
					if (location != null) result[location.index] = lines[y][x] - 'A'
				}
			}
		}
		return State(result)
	}

	private fun generateEndState(): State {
		val result = IntArray(corridorSize + numberOfTypes * numberPerType) { -1 }
		for (it in corridorSize until result.size) {
			result[it] = (it - corridorSize) % numberOfTypes
		}
		return State(result)
	}

	fun solve(): Int? {
		return findShortestPath(startState, endState, State::getSteps, State::estimateRemainingCost)
	}

	data class Location(val index: Int, val position: Point2D, val type: Int?, val number: Int?)

	inner class State(val value: IntArray) {
		fun getSteps(): Sequence<Move<State>> {
			return locations
				.asSequence()
				.filter { value[it.index] != -1 }
				.flatMap { generateMovesFrom(it) }
		}

		private fun generateMovesFrom(location: Location): Sequence<Move<State>> {
			// Handle moves from corridor later
			if (location.type == null) return sequenceOf()

			// Do not move if this one and all ones behind it are in the correct place
			val allCorrect = IntRange(location.number!!, numberPerType - 1)
				.all { value[indexOfHome(location.type, it)] == location.type }
			if (allCorrect) return sequenceOf()

			// Do not move if route to corridor not empty
			val routeToCorridorEmpty = IntRange(0, location.number - 1)
				.all { value[indexOfHome(location.type, it)] == -1 }
			if (!routeToCorridorEmpty) return sequenceOf()

			// Try to move left and right
			val toLocations = mutableListOf<Location>()
			for (index in corridorIndicesPerX[location.position.x - 1]!! downTo 0) {
				if (value[index] != -1) break
				toLocations.add(locations[index])
			}
			for (index in corridorIndicesPerX[location.position.x + 1]!! until corridorSize) {
				if (value[index] != -1) break
				toLocations.add(locations[index])
			}

			// Try the moves
			return toLocations.asSequence().map { createMove(location, it) }
		}

		private fun createMove(from: Location, to: Location): Move<State> {
			// Do the move
			val newValue = value.clone()
			var totalCost = 0

			fun move(from: Location, to: Location) {
				newValue[to.index] = newValue[from.index]
				newValue[from.index] = -1
				totalCost += costs[newValue[to.index]] * from.position.manhattanDistanceTo(to.position)
			}

			fun homeCanBeOccupied(type: Int): Boolean {
				return IntRange(0, numberPerType - 1).map { newValue[indexOfHome(type, it)] }
					.all { it == -1 || it == type }
			}

			fun homeLastFreeIndex(type: Int): Int {
				return (numberPerType - 1 downTo 0).map { indexOfHome(type, it) }.first { newValue[it] == -1 }
			}

			move(from, to)

			// See if we can move back home
			var checkMoveHome = true
			while (checkMoveHome) {
				checkMoveHome = false
				for (type in 0 until numberOfTypes) {
					if (homeCanBeOccupied(type)) {
						// Try to move left and right
						val xOfHome = xOfHome(type)
						for (index in corridorIndicesPerX[xOfHome - 1]!! downTo 0) {
							if (newValue[index] == type) {
								move(locations[index], locations[homeLastFreeIndex(type)])
								checkMoveHome = true
							} else if (newValue[index] != -1) {
								break
							}
						}
						for (index in corridorIndicesPerX[xOfHome + 1]!! until corridorSize) {
							if (newValue[index] == type) {
								move(locations[index], locations[homeLastFreeIndex(type)])
								checkMoveHome = true
							} else if (newValue[index] != -1) {
								break
							}
						}
					}
				}
			}

			val state = State(newValue)
			return Move(state, totalCost)
		}

		private fun xOfHome(type: Int) = type * 2 + 3
		private fun indexOfHome(type: Int, number: Int) = corridorSize + number * numberOfTypes + type

		fun estimateRemainingCost(): Int {
			val result = value.mapIndexed { index, ch ->
				val type = value[index]
				val location = locations[index]
				val correctX = 2 * type + 3

				if (type == -1 || location.position.x == correctX)
					0
				else
					10000 + costs[type] * (numberPerType + location.position.y + abs(location.position.x - correctX))
			}.sum()
			return result
		}

		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false
			other as State
			return value.contentEquals(other.value)
		}

		override fun hashCode(): Int {
			return value.hashCode()
		}

		override fun toString() = value
			.map { if (it == -1) '.' else 'A' + it }
			.foldIndexed("") { index, acc, ch ->
				acc + (if (index >= corridorSize && (index - corridorSize) % numberOfTypes == 0) "|" else "") + ch
			}
	}
}