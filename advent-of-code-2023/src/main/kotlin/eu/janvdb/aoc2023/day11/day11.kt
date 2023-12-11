package eu.janvdb.aoc2023.day11

import eu.janvdb.aocutil.kotlin.point2d.Point2DLong
import eu.janvdb.aocutil.kotlin.readLines


//private const val FILENAME = "input11-test.txt"
private const val FILENAME = "input11.txt"

fun main() {
	runWithDrift(1)
	runWithDrift(999_999)
}

private fun runWithDrift(drift: Int) {
	val galaxies = parseGalaxies(drift)
	val sum = combinations(galaxies).sumOf { it.first.manhattanDistanceTo(it.second) }
	println(sum)
}

private fun parseGalaxies(drift: Int): List<Point2DLong> {
	val unshiftedPoints = parseInput()
	return shiftPoints(unshiftedPoints, drift)
}

private fun parseInput() = readLines(2023, FILENAME).flatMapIndexed { y, line ->
	line.mapIndexed { x, c ->
		if (c == '#') Point2DLong(x, y) else null
	}
}.filterNotNull().sorted()

private fun shiftPoints(unshiftedPoints: List<Point2DLong>, drift: Int): List<Point2DLong> {
	val minY = unshiftedPoints.minOf { it.y }
	val maxY = unshiftedPoints.maxOf { it.y }
	val jefke = (minY..maxY).reversed()
		.filter { y -> unshiftedPoints.none { it.y == y } }
	val shiftedByRows = jefke
		.fold(unshiftedPoints) { points, y -> points.map { if (it.y > y) Point2DLong(it.x, it.y + drift) else it } }

	val minX = unshiftedPoints.minOf { it.x }
	val maxX = unshiftedPoints.maxOf { it.x }
	val joske = (minX..maxX).reversed()
		.filter { x -> unshiftedPoints.none { it.x == x } }
	val shiftedBxCols = joske
		.fold(shiftedByRows) { points, x -> points.map { if (it.x > x) Point2DLong(it.x + drift, it.y) else it } }

	return shiftedBxCols
}

private fun combinations(galaxies: List<Point2DLong>): Sequence<Pair<Point2DLong, Point2DLong>> {
	return (0..<galaxies.size).asSequence().flatMap { i ->
		(i + 1..<galaxies.size).asSequence().map { j -> galaxies[i] to galaxies[j] }
	}
}