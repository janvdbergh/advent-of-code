package eu.janvdb.aoc2025.day07

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input07.txt"
//const val FILENAME = "input07-test.txt"

fun main() {
	val lines = readLines(2025, FILENAME)
	val splitters = findChars(lines, '^').toSet()
	val start = findChars(lines, 'S')[0]

	val result = countSplits(start, splitters)
	println(result)
}

fun findChars(lines: List<String>, char: Char): List<Point2D> = lines.flatMapIndexed { y, line ->
	line
		.mapIndexed { x, ch -> Pair(Point2D(x, y), ch) }
		.filter { p -> p.second == char }
		.map { p -> p.first }
}

fun countSplits(start: Point2D, splitters: Set<Point2D>): Pair<Long, Long> {
	var beams = listOf(Pair(start.x, 1L))
	val maxY = splitters.maxOf { it.y }
	var y = start.y + 1
	var splitCount = 0L

	while (y <= maxY) {
		val splitBeams = beams.filter { splitters.contains(Point2D(it.first, y)) }
		splitCount += splitBeams.size
		val newBeams = splitBeams
			.flatMap { beam ->
				sequenceOf(
					Pair(beam.first - 1, beam.second),
					Pair(beam.first + 1, beam.second)
				)
			}
		val allBeams = beams - splitBeams + newBeams
		beams = allBeams.groupBy { it.first }
			.map { Pair(it.key, it.value.fold(0) { acc, beam -> acc + beam.second }) }

		y++
	}

	return Pair(splitCount, beams.sumOf { it.second })
}
