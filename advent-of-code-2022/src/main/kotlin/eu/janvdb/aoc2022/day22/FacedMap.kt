package eu.janvdb.aoc2022.day22

import eu.janvdb.aocutil.kotlin.SquareArrayMatrix
import eu.janvdb.aocutil.kotlin.point2d.Point2D

data class FacedMap(val faces: Map<Point2D, SquareArrayMatrix<Tile>>) {
	val dimension = faces.values.first().dimension
	private val maxTileX = faces.keys.maxOf { it.x }
	private val maxTileY = faces.keys.maxOf { it.y }
	val width = (maxTileX + 1) * dimension
	val height = (maxTileY + 1) * dimension

	fun tile(x: Int, y: Int): Tile {
		if (x < 0 || y < 0) return Tile.NOTHING
		val face = faces[Point2D(x / dimension, y / dimension)] ?: return Tile.NOTHING
		return face.tile(x % dimension, y % dimension)
	}

	fun move(
		position: Position,
		instruction: Instruction,
		strategy: WrapStrategy,
		trace: MutableList<Position>
	): Position {
		var currentPosition = position
		for (step in 0 until instruction.steps) {
			var nextPosition = currentPosition.step()
			if (tile(nextPosition.x, nextPosition.y) == Tile.NOTHING)
				nextPosition = strategy.wrap(currentPosition, nextPosition)

			if (tile(nextPosition.x, nextPosition.y) == Tile.WALL) break

			currentPosition = nextPosition
			trace.add(currentPosition)
		}

		if (instruction.rotation != null) currentPosition = instruction.rotation.apply(currentPosition)
		return currentPosition
	}

	fun initialPosition(): Position {
		val y = 0
		val x = (0..height).find { tile(it, y) == Tile.OPEN }!!
		return Position(x, y, Direction.RIGHT)
	}

	override fun toString() = toString(listOf())

	fun toString(position: Position) = toString(listOf(position))

	fun toString(trace: List<Position>): String {
		val builder = StringBuilder()
		for (y in 0 until height) {
			if (y != 0 && y % dimension == 0) {
				for (x in 0 until width) {
					if (x != 0 && x % dimension == 0) builder.append('+')
					builder.append('-')
				}
				builder.append('\n')
			}

			for (x in 0 until width) {
				if (x != 0 && x % dimension == 0) builder.append('|')

				val position = trace.find { it.x == x && it.y == y }

				val ch = position?.direction?.ch ?: tile(x, y).ch
				builder.append(ch)
			}
			builder.append('\n')

		}
		return builder.toString()
	}

	fun structure(): String {
		val builder = StringBuilder()
		for (y in 0..height step dimension) {
			for (x in 0..width step dimension) {
				val ch = if (tile(x, y) == Tile.NOTHING) '.' else '#'
				builder.append(ch)
			}
			builder.append('\n')
		}
		return builder.toString()
	}
}

fun List<String>.toMap(): FacedMap {
	val dimension = this.map { it.trim() }.minOfOrNull { it.length }!!

	val mapFaces = mutableMapOf<Point2D, SquareArrayMatrix<Tile>>()
	for (y in indices step dimension)
		for (x in 0 until this[y].length step dimension) {
			if (this[y][x] != ' ') {
				val tiles = (0 until dimension).flatMap { y1 ->
					(0 until dimension).map { x1 -> Tile.parse(this[y + y1][x + x1]) }
				}

				mapFaces[Point2D(x / dimension, y / dimension)] = SquareArrayMatrix(dimension, tiles)
			}
		}

	return FacedMap(mapFaces)
}

enum class Tile(val ch: Char) {
	NOTHING(' '),
	OPEN('.'),
	WALL('#');

	companion object {
		fun parse(ch: Char) =
			Tile.values().find { it.ch == ch } ?: throw IllegalArgumentException("Tile $ch")
	}
}