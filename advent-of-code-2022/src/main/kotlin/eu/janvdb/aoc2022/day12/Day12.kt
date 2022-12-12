package eu.janvdb.aoc2022.day12

import eu.janvdb.aocutil.kotlin.Move
import eu.janvdb.aocutil.kotlin.findShortestPath
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input12-test.txt"
const val FILENAME = "input12.txt"

fun main() {
	val map = Map.parse(readLines(2022, FILENAME))

	println(map.findShortestPath())
	println(map.findShortestPathWithAnyStart())
}

class Map(val width: Int, val height: Int, val elevations: List<Int>, val start: Point2D, val end: Point2D) {

	fun findShortestPath(): Int? {
		return findShortestPath(start, end, this::neighbours)
	}

	fun findShortestPathWithAnyStart(): Int {
		return elevations.asSequence()
			.mapIndexed() { index, elevation -> Pair(elevation, Point2D(index % width, index / width)) }
			.filter { it.first == 0 }
			.map { findShortestPath(it.second, end, this::neighbours) }
			.filter { it != null }
			.map { it!! }
			.min()
	}

	private fun neighbours(point: Point2D): Sequence<Move<Point2D>> {
		val startElevation = elevationAt(point)
		return sequenceOf(point.left(), point.up(), point.right(), point.down())
			.filter { it.x in 0 until width && it.y in 0 until height }
			.filter { elevationAt(it) - startElevation <= 1 }
			.map { Move(it, 1) }
	}

	private fun elevationAt(point: Point2D): Int {
		return elevations[point.x + point.y * width]
	}

	companion object {
		fun parse(lines: List<String>): Map {
			val width = lines[0].length
			val height = lines.size
			val elevations = mutableListOf<Int>()
			var start: Point2D? = null
			var end: Point2D? = null
			for (y in 0 until height) {
				for (x in 0 until width) {
					val ch = lines[y][x]
					elevations.add(
						when (ch) {
							'S' -> 0
							'E' -> 25
							in 'a'..'z' -> ch - 'a'
							else -> throw IllegalArgumentException("Invalid: $ch")
						}
					)
					if (ch == 'S') start = Point2D(x, y)
					if (ch == 'E') end = Point2D(x, y)
				}
			}

			return Map(width, height, elevations, start!!, end!!)
		}
	}
}