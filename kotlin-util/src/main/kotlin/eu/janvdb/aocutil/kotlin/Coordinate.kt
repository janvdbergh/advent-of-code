package eu.janvdb.aocutil.kotlin

import java.lang.Math.abs

data class Coordinate(val x: Int, val y: Int) {
	fun manhattanDistance(): Int {
		return abs(x) + abs(y)
	}

	fun move(direction: Direction, amount: Int): Coordinate {
		return when (direction) {
			Direction.N -> Coordinate(x, y + amount)
			Direction.E -> Coordinate(x + amount, y)
			Direction.S -> Coordinate(x, y - amount)
			Direction.W -> Coordinate(x - amount, y)
		}
	}

	fun move(direction: Coordinate, amount: Int): Coordinate {
		return Coordinate(x + amount * direction.x, y + amount * direction.y)
	}

	fun rotateLeft(amount: Int): Coordinate {
		var actualAmount = amount % 360
		if (actualAmount < 0) actualAmount += 360
		return when (actualAmount) {
			0 -> this
			90 -> Coordinate(-y, x)
			180 -> Coordinate(-x, -y)
			270 -> Coordinate(y, -x)
			else -> throw IllegalArgumentException(amount.toString())
		}
	}

	fun rotateRight(amount: Int): Coordinate {
		return rotateLeft(360 - amount)
	}
}