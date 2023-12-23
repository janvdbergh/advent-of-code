package eu.janvdb.aoc2023.day23

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input23-test.txt"
const val FILENAME = "input23.txt"

fun main() {
	val map = Puzzle.parse(readLines(2023, FILENAME))
	val nodes = map.getNodes()
	println(nodes.longestPath())

	val map2 = map.removeSlopes()
	val nodes2 = map2.getNodes()
	println(nodes2.longestPath())

	// < 6400
}

enum class Tile(val ch: Char) {
	PATH('.'),
	FOREST('#'),
	UP_SLOPE('^'),
	DOWN_SLOPE('v'),
	LEFT_SLOPE('<'),
	RIGHT_SLOPE('>');

	companion object {
		fun fromChar(ch: Char) = entries.first { it.ch == ch }
	}
}

data class Nodes(val start: Point2D, val end: Point2D, val distances: Map<Point2D, Map<Point2D, Int>>) {
	fun longestPath(): Int {
		return shortestPathFrom(start, mutableSetOf(), 0)
	}

	private fun shortestPathFrom(current: Point2D, currentPath: MutableSet<Point2D>, currentDistance: Int): Int {
		if (current == end) return currentDistance

		currentPath.add(current)
		val result = distances.getOrDefault(current, emptyMap())
			.filter { it.key !in currentPath }
			.maxOfOrNull { shortestPathFrom(it.key, currentPath, currentDistance + it.value) }
		currentPath.remove(current)

		return result ?: -1
	}
}

data class Puzzle(val width: Int, val height: Int, val tiles: List<Tile>) {

	fun getNodes(): Nodes {
		val nodeSet = (0..<height).flatMap { y -> (0..<width).map { x -> Point2D(x, y) } }
			.filter { getTile(it) != Tile.FOREST }
			.filter { it.y == 0 || it.y == height - 1 || getNeighbours(it).count() > 2 }
			.toSet()


		fun longestPath(node1: Point2D, node2: Point2D): Int {
			fun visitRestOfPath2(visited: MutableSet<Point2D>, current: Point2D): Int {
				if (current == node2)
					return visited.size

				if (current != node1 && current in nodeSet)
					return -1

				visited.add(current)
				val result = getNeighbours(current)
					.filter { it !in visited }
					.maxOfOrNull { visitRestOfPath2(visited, it) } ?: -1
				visited.remove(current)

				return result
			}

			return visitRestOfPath2(mutableSetOf(), node1)
		}

		val distances = nodeSet.associateWith { node1 ->
			nodeSet.asSequence()
				.filter { it != node1 }
				.associateWith { node2 -> longestPath(node1, node2) }
				.filter { it.value != -1 }
		}

		return Nodes(nodeSet.first(), nodeSet.last(), distances)
	}

	private fun getNeighbours(point: Point2D): Sequence<Point2D> {
		return when (getTile(point)) {
			Tile.PATH -> sequenceOf(point.left(), point.right(), point.up(), point.down())
			Tile.FOREST -> sequenceOf()
			Tile.UP_SLOPE -> sequenceOf(point.up())
			Tile.DOWN_SLOPE -> sequenceOf(point.down())
			Tile.LEFT_SLOPE -> sequenceOf(point.left())
			Tile.RIGHT_SLOPE -> sequenceOf(point.right())
		}.filter { getTile(it) != Tile.FOREST }
	}

	private fun getTile(point: Point2D) =
		if (point.y < 0 || point.y >= height || point.x < 0 || point.x >= width) Tile.FOREST else tiles[point.y * width + point.x]

	fun removeSlopes(): Puzzle {
		val newTiles = tiles.map { if (it == Tile.FOREST) Tile.FOREST else Tile.PATH }
		return Puzzle(width, height, newTiles)
	}

	companion object {
		fun parse(lines: List<String>): Puzzle {
			val width = lines[0].length
			val height = lines.size
			val tiles = lines.flatMap { line -> line.map { Tile.fromChar(it) } }
			return Puzzle(width, height, tiles)
		}
	}
}