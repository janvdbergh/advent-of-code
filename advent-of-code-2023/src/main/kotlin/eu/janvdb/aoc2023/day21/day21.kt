package eu.janvdb.aoc2023.day

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines


//const val FILENAME = "input21-test.txt"
const val FILENAME = "input21.txt"

fun main() {
	val (map, start) = parseMap(readLines(2023, FILENAME))

	var points = setOf(start)
	val result = mutableListOf<Int>()

	// Take value as indices size/2, size+size/2, 2*size+size/2 to get polynomial
	val indices = listOf(map.size / 2, map.size / 2 + map.size, map.size / 2 + 2 * map.size)
	(1..indices.last()).forEach {
		points = map.takeStep(points)
		if (it in indices) {
			result.add(points.size)
		}
	}

	println(result)
	println(extrapolate(result, (26_501_365L - map.size / 2) / map.size))
}

fun parseMap(lines: List<String>): Pair<Map, Point2D> {
	val size = lines.size
	val points = (0..<size).asSequence()
		.flatMap { y ->
			(0..<size).asSequence()
				.filter { x -> lines[y][x] != '#' }
				.map { x -> Point2D(x, y) }
		}
		.toSet()

	val start = (0..<size).asSequence()
		.flatMap { y ->
			(0..<size).asSequence()
				.filter { x -> lines[y][x] == 'S' }
				.map { x -> Point2D(x, y) }
		}
		.first()

	return Pair(Map(size, points), start)
}

fun extrapolate(y: List<Int>, value: Long): Any {
	// Taking into account that x[1]=3*x[0] and x[2]=5*x[0] and value = n*x[0]
	val a = (y[2] - (2 * y[1]) + y[0]) / 2
	val b = y[1] - y[0] - a
	val c = y[0]

	return value * value * a + value * b + c
}

data class Map(val size: Int, val points: Set<Point2D>) {

	fun takeStep(from: Set<Point2D>): Set<Point2D> {
		val result = from.asSequence()
			.flatMap { point -> sequenceOf(point.left(1), point.right(1), point.up(1), point.down(1)) }
			.filter { hasPoint(it) }
			.toSet()
		return result
	}

	private fun hasPoint(point: Point2D): Boolean {
		val x = point.x % size
		val y = point.y % size
		val x1 = if (x < 0) x + size else x
		val y1 = if (y < 0) y + size else y
		return points.contains(Point2D(x1, y1))
	}
}