package eu.janvdb.aoc2022.day14

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import kotlin.math.max
import kotlin.math.min

val REGEX_POINTS = Regex(" -> ")
val REGEX_POINT = Regex(",")

data class Instruction(val points: List<Point2D>) {

	fun getWalls(): Sequence<Point2D> {
		return points.asSequence()
			.windowed(2)
			.flatMap {
				val p1 = it[0]
				val p2 = it[1]
				when {
					p1.x == p2.x -> IntRange(min(p1.y, p2.y), max(p1.y, p2.y)).asSequence()
						.map { y -> Point2D(p1.x, y) }

					p1.y == p2.y -> IntRange(min(p1.x, p2.x), max(p1.x, p2.x)).asSequence()
						.map { x -> Point2D(x, p1.y) }

					else -> throw IllegalArgumentException()
				}
			}
	}

	companion object {
		fun parse(s: String): Instruction {
			val points = s.split(REGEX_POINTS)
				.map { it.split(REGEX_POINT) }
				.map { Point2D(it[0].toInt(), it[1].toInt()) }
			return Instruction(points)
		}
	}
}