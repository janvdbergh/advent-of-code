package eu.janvdb.aoc2021.day05

import eu.janvdb.aocutil.kotlin.point2d.Line2D
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input05.txt"

fun main() {
	val lines = readLines(2021, FILENAME)
		.map { parseLine(it) }

	val heatMap = HashMap<Point2D, Int>()
	lines.asSequence()
		.flatMap { it.getPointsHorizontallyVerticallyOrDiagonally() }
		.forEach { addToHeatMap(heatMap, it) }

	val result = heatMap.count { it.value >= 2 }
	println(result)
}

fun addToHeatMap(heatMap: MutableMap<Point2D, Int>, point: Point2D) {
	val current = heatMap.getOrDefault(point, 0)
	heatMap[point] = current + 1
}

private fun parseLine(line: String): Line2D {
	val parts = line.split(Regex(" -> "))
	return Line2D(parsePoint(parts[0]), parsePoint(parts[1]))
}

private fun parsePoint(s: String): Point2D {
	val parts = s.split(",")
	return Point2D(parts[0].toInt(), parts[1].toInt())
}

