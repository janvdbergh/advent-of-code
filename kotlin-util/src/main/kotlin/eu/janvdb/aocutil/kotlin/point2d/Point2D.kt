package eu.janvdb.aocutil.kotlin.point2d

import java.lang.Math.abs

data class Point2D(val x: Int, val y: Int) {
	fun manhattanDistance(): Int {
		return abs(x) + abs(y)
	}

	fun move(direction: Direction, amount: Int): Point2D {
		return when (direction) {
			Direction.N -> Point2D(x, y + amount)
			Direction.E -> Point2D(x + amount, y)
			Direction.S -> Point2D(x, y - amount)
			Direction.W -> Point2D(x - amount, y)
		}
	}

	fun move(direction: Point2D, amount: Int): Point2D {
		return Point2D(x + amount * direction.x, y + amount * direction.y)
	}

	fun left(amount: Int = 1) = Point2D(x - amount, y)
	fun right(amount: Int = 1) = Point2D(x + amount, y)
	fun up(amount: Int = 1) = Point2D(x, y - amount)
	fun down(amount: Int = 1) = Point2D(x, y + amount)

	fun rotateLeft(amount: Int): Point2D {
		var actualAmount = amount % 360
		if (actualAmount < 0) actualAmount += 360
		return when (actualAmount) {
			0 -> this
			90 -> Point2D(-y, x)
			180 -> Point2D(-x, -y)
			270 -> Point2D(y, -x)
			else -> throw IllegalArgumentException(amount.toString())
		}
	}

	fun rotateRight(amount: Int): Point2D {
		return rotateLeft(360 - amount)
	}

	companion object {
		fun createCommaSeparated(s: String): Point2D {
			val pair = s.split(",")
			return Point2D(pair[0].trim().toInt(), pair[1].trim().toInt())
		}
	}
}