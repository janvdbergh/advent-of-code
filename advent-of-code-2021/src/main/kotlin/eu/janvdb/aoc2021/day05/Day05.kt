package eu.janvdb.aoc2021.day05

import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.max
import kotlin.math.min

const val FILENAME = "input05.txt"

fun main() {
	val lines = readLines(2021, FILENAME)
		.map { parseLine(it) }

	val heatMap = HashMap<Point2D, Int>()
	lines.asSequence()
		.flatMap { it.getPoints() }
		.forEach { it -> addToHeatMap(heatMap, it) }

	val result = heatMap.count { it.value >= 2 }
	println(result)
}

fun addToHeatMap(heatMap: MutableMap<Point2D, Int>, point: Point2D) {
	val current = heatMap.getOrDefault(point, 0)
	heatMap.put(point, current + 1)
}

private fun parseLine(line: String): Line2D {
	val parts = line.split(Regex(" -> "))
	return Line2D(parsePoint(parts[0]), parsePoint(parts[1]))
}

private fun parsePoint(s: String): Point2D {
	val parts = s.split(",")
	return Point2D(parts[0].toInt(), parts[1].toInt())
}

data class Point2D(val x: Int, val y: Int)

data class Line2D(val a: Point2D, val b: Point2D) {
	fun getPoints(): Sequence<Point2D> {
		if (a.x == b.x) {
			return IntRange(min(a.y, b.y), max(a.y, b.y)).asSequence().map { y -> Point2D(a.x, y) }
		}

		val direction = (a.y - b.y) / (a.x - b.x)
		val offset = a.y - direction * a.x
		return IntRange(min(a.x, b.x), max(a.x, b.x)).asSequence().map { x -> Point2D(x, x * direction + offset) }
	}
}