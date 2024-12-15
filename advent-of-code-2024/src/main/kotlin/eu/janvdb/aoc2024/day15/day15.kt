package eu.janvdb.aoc2024.day15

import eu.janvdb.aocutil.kotlin.point2d.Direction
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readGroupedLines

//const val FILENAME = "input15-test.txt"
//const val FILENAME = "input15-test2.txt"
//const val FILENAME = "input15-test3.txt"
const val FILENAME = "input15.txt"

fun main() {
	val lines = readGroupedLines(2024, FILENAME)
	val map = Map.fromLines(lines[0])
	val moves = lines[1].joinToString("").map { Direction.parse(it)!! }

//	map.print()
	println(map.move(moves).getScore())

	val map2 = map.double()
//	map2.print()
	println(map2.move(moves).getScore())
}

data class Map(
	val width: Int,
	val height: Int,
	val boxWidth: Int,
	val robot: Point2D,
	val walls: Set<Point2D>,
	val boxes: Set<Point2D>
) {
	fun double(): Map {
		val newRobot = Point2D(2 * robot.x, robot.y)
		val newWalls = walls.asSequence()
			.flatMap { sequenceOf(Point2D(2 * it.x, it.y), Point2D(2 * it.x + 1, it.y)) }.toSet()
		val newBoxes = boxes.asSequence().map { Point2D(2 * it.x, it.y) }.toSet()
		return Map(width * 2, height, boxWidth * 2, newRobot, newWalls, newBoxes)
	}

	fun move(moves: List<Direction>): Map {
		var current = this
		moves.forEach {
			current = current.move(it)
//			println(it)
//			current.print()
		}
		return current
	}

	private fun move(move: Direction): Map {
		val newRobot = robot.move(move)
		if (isWall(newRobot)) return this

		return clearSpace(setOf(newRobot), move)?.withRobot(newRobot) ?: return this
	}

	private fun clearSpace(coordinates: Set<Point2D>, move: Direction): Map? {
		val boxesToMove = coordinates.asSequence().mapNotNull { getBox(it) }.toSet()
		if (boxesToMove.isEmpty()) return this

		val movedBoxes = boxesToMove.map { it.move(move) }
		val movedBoxesCoordinates = movedBoxes.flatMap(this::getBoxCoordinates).toSet()
		if (movedBoxesCoordinates.any(this::isWall)) return null

		return minusBoxes(boxesToMove).clearSpace(movedBoxesCoordinates, move)?.plusBoxes(movedBoxes)
	}

	private fun plusBoxes(toAdd: Collection<Point2D>) = Map(width, height, boxWidth, robot, walls, boxes + toAdd)
	private fun minusBoxes(toRemove: Set<Point2D>) = Map(width, height, boxWidth, robot, walls, boxes - toRemove)
	private fun withRobot(newRobot: Point2D) = Map(width, height, boxWidth, newRobot, walls, boxes)

	fun print() {
		for (y in 0 until height) {
			for (x in 0 until width) {
				val pos = Point2D(x, y)
				val boxIndex = getBoxIndex(pos)
				print(
					when {
						robot == pos -> '@'
						boxIndex == 0 && boxWidth == 1 -> 'O'
						boxIndex == 0 -> '['
						boxIndex == boxWidth - 1 -> ']'
						boxIndex != null -> '*'
						isWall(pos) -> '#'
						else -> '.'
					}
				)
			}
			println()
		}
		println()
	}

	private fun isWall(position: Point2D) =
		position.x <= 0 || position.x >= width - 1 || position.y <= 0 || position.y >= height - 1
				|| walls.contains(position)

	private fun getBoxCoordinates(box: Point2D) = (0 until boxWidth).asSequence().map { box.right(it) }

	private fun getBox(position: Point2D) = getBoxAndBoxIndex(position)?.first
	private fun getBoxIndex(position: Point2D) = getBoxAndBoxIndex(position)?.second

	private fun getBoxAndBoxIndex(position: Point2D): Pair<Point2D, Int>? =
		(0 until boxWidth)
			.map { Pair(position.left(it), it) }
			.find { boxes.contains(it.first) }

	fun getScore() = boxes.sumOf { it.x + 100 * it.y }

	companion object {
		fun fromLines(lines: List<String>): Map {
			fun iterateLines(): Sequence<Point2D> = lines.indices.asSequence()
				.flatMap { y -> lines[y].indices.asSequence().map { x -> Point2D(x, y) } }

			val height = lines.size
			val width = lines[0].length
			val robot = iterateLines().find { lines[it.y][it.x] == '@' }!!
			val walls = iterateLines().filter { lines[it.y][it.x] == '#' }.toSet()
			val boxes = iterateLines().filter { lines[it.y][it.x] == 'O' }.toSet()
			return Map(width, height, 1, robot, walls, boxes)
		}
	}
}
