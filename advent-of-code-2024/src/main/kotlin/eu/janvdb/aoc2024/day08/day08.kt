package eu.janvdb.aoc2024.day08

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input08-test.txt"
const val FILENAME = "input08.txt"

fun main() {
	val antennas = Antennas.fromLines(readLines(2024, FILENAME))

	val antiNodes1 = antennas.findAntiNodes1()
	print(antennas, antiNodes1)
	println(antiNodes1.size)

	val antiNodes2 = antennas.findAntiNodes2()
	print(antennas, antiNodes2)
	println(antiNodes2.size)
}

fun print(antennas: Antennas, antiNodes: Set<Point2D>) {
	for (y in 0 until antennas.height - 1) {
		for (x in 0 until antennas.width - 1) {
			val ch = if (antiNodes.contains(Point2D(x, y))) '#' else '.'
			print(ch)
		}
		println()
	}
}

data class Antenna(val symbol: Char, val locations: List<Point2D>) {
	fun findAntiNodes1(antennas: Antennas): Sequence<Point2D> {
		return getAntennaPairs()
			.flatMap {
				val dx = it.second.x - it.first.x
				val dy = it.second.y - it.first.y
				sequenceOf(Point2D(it.first.x - dx, it.first.y - dy), Point2D(it.second.x + dx, it.second.y + dy))
			}
			.filter { antennas.onMap(it.x, it.y) }
	}

	fun findAntiNodes2(antennas: Antennas): Sequence<Point2D> {
		return getAntennaPairs().flatMap { AntiNodeIterator(it.first, it.second, antennas).asSequence() }
	}

	private fun getAntennaPairs(): Sequence<Pair<Point2D, Point2D>> {
		return locations.indices
			.asSequence()
			.flatMap { index1 ->
				(index1 + 1 until locations.size)
					.map { index2 -> Pair(locations[index1], locations[index2]) }
			}
	}

	class AntiNodeIterator(private val first: Point2D, private val second: Point2D, private val antennas: Antennas) :
		Iterator<Point2D> {
		private val dx = second.x - first.x
		private val dy = second.y - first.y
		private var next1 = 1 // counter going in one direction
		private var next2 = 1 // counter going in the other direction
		private var nextPoint: Point2D? = null

		override fun hasNext(): Boolean {
			val x1 = first.x + next1 * dx
			val y1 = first.y + next1 * dy
			if (onMap(x1, y1)) {
				next1++
				nextPoint = Point2D(x1, y1)
				return true
			}
			val x2 = second.x - next2 * dx
			val y2 = second.y - next2 * dy
			if (onMap(x2, y2)) {
				next2++
				nextPoint = Point2D(x2, y2)
				return true
			}
			return false
		}

		private fun onMap(x: Int, y: Int): Boolean {
			return x >= 0 && x < antennas.width && y >= 0 && y < antennas.height
		}

		override fun next(): Point2D {
			return nextPoint!!
		}
	}
}

data class Antennas(val height: Int, val width: Int, val antennas: List<Antenna>) {

	fun findAntiNodes1(): Set<Point2D> {
		return antennas.asSequence()
			.flatMap { it.findAntiNodes1(this) }
			.toSet()
	}

	fun findAntiNodes2(): Set<Point2D> {
		return antennas.asSequence()
			.flatMap { it.findAntiNodes2(this) }
			.toSet()
	}

	fun onMap(x: Int, y: Int): Boolean {
		return x in 0..<width && y in 0..<height
	}

	companion object {
		fun fromLines(lines: List<String>): Antennas {
			val antennas = lines.indices
				.flatMap { y -> lines[y].indices.map { x -> Pair(Point2D(x, y), lines[y][x]) } }
				.filter { it.second != '.' }
				.groupBy { it.second }
				.map { item -> Antenna(item.key, item.value.map { it.first }) }
			return Antennas(lines.size, lines[0].length, antennas)
		}
	}
}