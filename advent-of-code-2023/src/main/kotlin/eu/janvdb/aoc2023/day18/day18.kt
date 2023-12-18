package eu.janvdb.aoc2023.day18

import eu.janvdb.aocutil.kotlin.point2d.Direction
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines
import java.util.*
import kotlin.math.max
import kotlin.math.min

//const val FILENAME = "input18-test.txt"
const val FILENAME = "input18.txt"

fun main() {
	val instructions1 = readLines(2023, FILENAME).map { Instruction.parse1(it) }
	runWith(instructions1)

	val instructions2 = readLines(2023, FILENAME).map { Instruction.parse2(it) }
	runWith(instructions2)
}

private fun runWith(instructions: List<Instruction>) {
	val grid = Grid.create(instructions)
	grid.print()

	val filledGrid = grid.fill()
	filledGrid.print()

	println(filledGrid.score())
}

data class Grid(val weightsX: List<Int>, val weightsY: List<Int>, val points: Set<Point2D>) {
	private val fillStart = (0..points.maxOf { it.x }).asSequence()
		.filter { x -> points.any { it.x == x && it.y == 0 } }
		.map { Point2D(it + 1, 1) }
		.first()

	fun fill(): Grid {
		val newPoints = points.toMutableSet()
		val toDo = LinkedList<Point2D>()
		toDo.add(fillStart)

		while (!toDo.isEmpty()) {
			val point = toDo.removeFirst()

			sequenceOf(point.left(), point.up(), point.right(), point.down())
				.filter { !newPoints.contains(it) }
				.forEach { toDo.add(it); newPoints.add(it) }
		}

		return Grid(weightsX, weightsY, newPoints)
	}

	fun score() = points.sumOf { 1L * weightsX[it.x] * weightsY[it.y] }

	fun print() {
		val minX = points.minOf { it.x }
		val maxX = points.maxOf { it.x }
		val minY = points.minOf { it.y }
		val maxY = points.maxOf { it.y }

		for (y in minY..maxY) {
			for (x in minX..maxX) {
				val point = Point2D(x, y)
				if (point == fillStart)
					print('X')
				else if (points.contains(point))
					print('#')
				else print('.')
			}
			println()
		}
		println()
	}

	companion object {
		fun create(instructions: List<Instruction>): Grid {
			val cornerPoints = getCornerPointsMovedToOrigin(instructions)
			val weightedX = calculatedWeightedValues(cornerPoints) { it.x }
			val weightedY = calculatedWeightedValues(cornerPoints) { it.y }
			val foldedPoints = foldPoints(weightedX, cornerPoints, weightedY)
			val connectedPoints = connectPoints(foldedPoints)

			return Grid(
				weightedX.values.map { it.weight },
				weightedY.values.map { it.weight },
				connectedPoints.toSet()
			)
		}

		private fun getCornerPointsMovedToOrigin(instructions: List<Instruction>): List<Point2D> {
			val cornerPoints = instructions.runningFold(Point2D(0, 0)) { current, instruction ->
				current.move(instruction.direction, instruction.distance)
			}
			val minX = cornerPoints.minOf { it.x }
			val minY = cornerPoints.minOf { it.y }
			val movedPoints = cornerPoints.map { Point2D(it.x - minX, it.y - minY) }
			return movedPoints
		}

		private fun calculatedWeightedValues(points: List<Point2D>, extractor: (Point2D) -> Int): WeightedValues {
			val values = points.map(extractor).distinct().sorted()
			val weightedValues = values.indices.asSequence().drop(1)
				.flatMap { index ->
					sequenceOf(
						WeightedCoordinate(values[index - 1], 1),
						WeightedCoordinate(values[index - 1] + 1, values[index] - values[index - 1] - 1)
					)
				}
				.filter { it.weight != 0 }
				.plus(WeightedCoordinate(values.last(), 1))
				.toList()
			return WeightedValues(weightedValues)
		}

		private fun foldPoints(weightedX: WeightedValues, movedPoints: List<Point2D>, weightedY: WeightedValues):
				List<Point2D> {
			val foldedByX = weightedX.values.reversed().fold(movedPoints) { current, weightedValue ->
				current.map {
					Point2D(
						if (it.x > weightedValue.original) it.x - weightedValue.weight + 1 else it.x,
						it.y
					)
				}
			}

			return weightedY.values.reversed().fold(foldedByX) { current, weightedValue ->
				current.map {
					Point2D(
						it.x,
						if (it.y > weightedValue.original) it.y - weightedValue.weight + 1 else it.y
					)
				}
			}
		}

		private fun connectPoints(foldedPoints2: List<Point2D>): List<Point2D> {
			val connectedPoints = foldedPoints2.zipWithNext { point1, point2 ->
				if (point1.x == point2.x) {
					(min(point1.y, point2.y)..max(point1.y, point2.y)).map { Point2D(point1.x, it) }
				} else {
					(min(point1.x, point2.x)..max(point1.x, point2.x)).map { Point2D(it, point1.y) }
				}
			}.flatten()
			return connectedPoints
		}
	}
}


data class WeightedCoordinate(val original: Int, val weight: Int)

data class WeightedValues(val values: List<WeightedCoordinate>)

data class Instruction(val direction: Direction, val distance: Int) {
	companion object {
		fun parse1(line: String): Instruction {
			fun parseDirection(direction: String) = when (direction) {
				"U" -> Direction.N
				"D" -> Direction.S
				"L" -> Direction.W
				"R" -> Direction.E
				else -> throw IllegalArgumentException("Unknown direction: $direction")
			}

			val parts = line.split(' ')
			return Instruction(
				parseDirection(parts[0]),
				parts[1].toInt()
			)
		}

		fun parse2(line: String): Instruction {
			fun parseDirection(direction: Char) = when (direction) {
				'3' -> Direction.N
				'1' -> Direction.S
				'2' -> Direction.W
				'0' -> Direction.E
				else -> throw IllegalArgumentException("Unknown direction: $direction")
			}

			val hex = line.split(' ')[2].substring(2, 8)
			return Instruction(
				parseDirection(hex[5]),
				hex.substring(0, 5).toInt(16)
			)
		}
	}
}