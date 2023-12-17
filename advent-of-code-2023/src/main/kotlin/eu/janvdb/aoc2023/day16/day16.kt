package eu.janvdb.aoc2023.day16

import eu.janvdb.aocutil.kotlin.point2d.Direction
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines
import java.util.*

//const val FILENAME = "input16-test.txt"
const val FILENAME = "input16.txt"

fun main() {
	val puzzle = Puzzle.parse(readLines(2023, FILENAME))

	part1(puzzle)
	part2(puzzle)
}

private fun part1(puzzle: Puzzle) {
	val result = puzzle.followBeam(Pair(Point2D(0, 0), Direction.E)).getTilesHit()
	println(result)
}

private fun part2(puzzle: Puzzle) {
	val result = puzzle.entryPoints().map { puzzle.followBeam(it).getTilesHit() }.max()
	println(result)
}

data class Puzzle(val size: Int, val tiles: List<Tile>, val directionsHit: List<Set<Direction>>) {

	fun entryPoints(): Sequence<Pair<Point2D, Direction>> {
		val fromLeft = (0 until size).asSequence().map { Point2D(0, it) to Direction.E }
		val fromTop = (0 until size).asSequence().map { Point2D(it, 0) to Direction.S }
		val fromRight = (0 until size).asSequence().map { Point2D(size - 1, it) to Direction.W }
		val fromBottom = (0 until size).asSequence().map { Point2D(it, size - 1) to Direction.N }
		return fromLeft + fromTop + fromRight + fromBottom
	}

	fun followBeam(start: Pair<Point2D, Direction>): Puzzle {
		val toDo = LinkedList<Pair<Point2D, Direction>>()
		toDo.add(start)

		var current = this
		while (!toDo.isEmpty()) {
			val (point, direction) = toDo.removeFirst()
			if (current.hasDirection(point, direction)) continue

			current = current.addDirection(point, direction)

			getTile(point)
				.handleDirection(direction)
				.map { Pair(point.move(it, 1), it) }
				.filter { it.first.x in 0 until size && it.first.y in 0 until size }
				.forEach { toDo.add(it) }
		}

		return current
	}

	private fun getIndex(point: Point2D) = point.y * size + point.x
	private fun getTile(point: Point2D) = tiles[getIndex(point)]
	private fun hasDirection(point: Point2D, direction: Direction) = directionsHit[getIndex(point)].contains(direction)

	fun getTilesHit() = directionsHit.count { it.isNotEmpty() }

	private fun addDirection(point: Point2D, direction: Direction): Puzzle {
		val index = getIndex(point)
		val newDirectionsHit = directionsHit.toMutableList()
		newDirectionsHit[index] = newDirectionsHit[index] + direction
		return Puzzle(size, tiles, newDirectionsHit)
	}

	companion object {
		fun parse(lines: List<String>): Puzzle {
			val size = lines.size
			val tiles = lines.flatMap { line -> line.map { ch -> Tile.fromChar(ch) } }
			val directionsHit = List(size * size) { mutableSetOf<Direction>() }
			return Puzzle(size, tiles, directionsHit)
		}
	}
}

enum class Tile(val char: Char) {
	EMPTY('.'),
	MIRROR_SW('/'),
	MIRROR_NW('\\'),
	SPLITTER_HORIZONTAL('-'),
	SPLITTER_VERTICAL('|');

	fun handleDirection(direction: Direction): Sequence<Direction> {
		return when (this) {
			EMPTY -> sequenceOf(direction)
			MIRROR_SW -> when (direction) {
				Direction.W -> sequenceOf(Direction.S)
				Direction.S -> sequenceOf(Direction.W)
				Direction.E -> sequenceOf(Direction.N)
				Direction.N -> sequenceOf(Direction.E)
			}

			MIRROR_NW -> when (direction) {
				Direction.W -> sequenceOf(Direction.N)
				Direction.N -> sequenceOf(Direction.W)
				Direction.E -> sequenceOf(Direction.S)
				Direction.S -> sequenceOf(Direction.E)
			}

			SPLITTER_HORIZONTAL -> when (direction) {
				Direction.W, Direction.E -> sequenceOf(direction)
				Direction.N, Direction.S -> sequenceOf(Direction.W, Direction.E)
			}

			SPLITTER_VERTICAL -> when (direction) {
				Direction.N, Direction.S -> sequenceOf(direction)
				Direction.W, Direction.E -> sequenceOf(Direction.N, Direction.S)
			}
		}
	}

	companion object {
		fun fromChar(char: Char) = entries.first { it.char == char }
	}
}