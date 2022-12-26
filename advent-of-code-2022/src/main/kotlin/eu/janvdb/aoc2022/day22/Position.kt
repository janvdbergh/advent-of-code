package eu.janvdb.aoc2022.day22

data class Position(val x: Int, val y: Int, val direction: Direction) {
	val score = 1000 * (y + 1) + 4 * (x + 1) + direction.score

	fun step(): Position {
		return when (direction) {
			Direction.RIGHT -> Position(x + 1, y, direction)
			Direction.LEFT -> Position(x - 1, y, direction)
			Direction.UP -> Position(x, y - 1, direction)
			Direction.DOWN -> Position(x, y + 1, direction)
		}
	}

	fun withDirection(newDirection: Direction) = Position(x, y, newDirection)

	override fun toString(): String {
		return "($x, $y, $direction) -> $score"
	}
}

enum class Direction(val score: Int, val ch: Char) {
	RIGHT(0, '>'), DOWN(1, 'V'), LEFT(2, '<'), UP(3, '^');

	fun rotateLeft(): Direction {
		return when (this) {
			RIGHT -> UP
			UP -> LEFT
			LEFT -> DOWN
			DOWN -> RIGHT
		}
	}

	fun rotateRight(): Direction {
		return when (this) {
			RIGHT -> DOWN
			DOWN -> LEFT
			LEFT -> UP
			UP -> RIGHT
		}
	}
}