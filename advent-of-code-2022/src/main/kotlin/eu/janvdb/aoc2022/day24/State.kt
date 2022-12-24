package eu.janvdb.aoc2022.day24

import eu.janvdb.aocutil.kotlin.ShortestPathMove
import eu.janvdb.aocutil.kotlin.point2d.Direction
import eu.janvdb.aocutil.kotlin.point2d.Point2D

data class State(val maps: List<BlizzardMap>, val currentMapIndex: Int, val position: Point2D) {
	fun nextMoves(): Sequence<ShortestPathMove<State>> {
		val nextMapIndex = (currentMapIndex + 1) % maps.size
		return Direction.values().asSequence()
			.map { State(maps, nextMapIndex, position.move(it, 1)) }
			.plus(State(maps, nextMapIndex, position))
			.filter(State::isValid)
			.map { ShortestPathMove(it, 1) }
	}

	private fun isValid(): Boolean {
		val currentMap = maps[currentMapIndex]
		if (position == currentMap.startPosition || position == currentMap.endPosition) return true
		if (position.x < 0 || position.x >= currentMap.width || position.y < 0 || position.y >= currentMap.height) return false

		return !currentMap.blizzardCoordinates.contains(position)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as State

		return currentMapIndex == other.currentMapIndex && position == other.position
	}

	override fun hashCode(): Int {
		return 31 * currentMapIndex + position.hashCode()
	}

	override fun toString(): String {
		return "(currentMapIndex=$currentMapIndex, position=$position)"
	}
}