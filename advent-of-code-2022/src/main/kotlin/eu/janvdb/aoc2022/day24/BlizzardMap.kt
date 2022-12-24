package eu.janvdb.aoc2022.day24

import eu.janvdb.aocutil.kotlin.point2d.Direction
import eu.janvdb.aocutil.kotlin.point2d.Point2D

data class BlizzardMap(val width: Int, val height: Int, val blizzards: List<Blizzard>) {
	val blizzardCoordinates = blizzards.asSequence().map { it.location }.toSet()
	val startPosition = Point2D(0, -1)
	val endPosition = Point2D(width-1, height)

	fun move(): BlizzardMap {
		val newBlizzards = blizzards.map { it.move(this) }
		return BlizzardMap(width, height, newBlizzards)
	}

	override fun toString(): String {
		val builder = StringBuilder()
		for (y in 0 until height) {
			for (x in 0 until width) {
				val bl = blizzards.filter { it.location.x == x && it.location.y == y }
				when (bl.size) {
					0 -> builder.append('.')
					1 -> builder.append(bl[0])
					else -> builder.append(bl.size)
				}
			}
			builder.append('\n')
		}
		return builder.toString()
	}
}

data class Blizzard(val location: Point2D, val direction: Direction) {
	fun move(map: BlizzardMap) = Blizzard(location.moveWithingBounds(direction, 1, map.width, map.height), direction)

	override fun toString() = direction.ch.toString()
}

fun List<String>.toBlizzardMap(): BlizzardMap {
	val height = this.size - 2
	val width = this[0].length - 2
	val blizzards = (1..size - 2).flatMap { y ->
		val line = this[y]
		(1..line.length - 2).map { x ->
			val direction = Direction.parse(line[x])
			if (direction != null) Blizzard(Point2D(x - 1, y - 1), direction) else null
		}
	}
		.filter { it != null }
		.map { it!! }

	return BlizzardMap(width, height, blizzards)
}