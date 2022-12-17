package eu.janvdb.aoc2022.day17

import eu.janvdb.aocutil.kotlin.Bitmap
import eu.janvdb.aocutil.kotlin.RepeatingSequence
import eu.janvdb.aocutil.kotlin.findRepeatingPattern
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input17-test.txt"
const val FILENAME = "input17.txt"

const val DROP_SIZE = 5000
const val FIELD_WIDTH = 7

// Y increments going up

val SHAPES = listOf(
	Bitmap(setOf(Point2D(0, 0), Point2D(1, 0), Point2D(2, 0), Point2D(3, 0))),
	Bitmap(setOf(Point2D(1, 0), Point2D(0, 1), Point2D(1, 1), Point2D(2, 1), Point2D(1, 2))),
	Bitmap(setOf(Point2D(0, 0), Point2D(1, 0), Point2D(2, 0), Point2D(2, 1), Point2D(2, 2))),
	Bitmap(setOf(Point2D(0, 0), Point2D(0, 1), Point2D(0, 2), Point2D(0, 3))),
	Bitmap(setOf(Point2D(0, 0), Point2D(0, 1), Point2D(1, 0), Point2D(1, 1))),
)

fun main() {
	val heights = dropShapes(DROP_SIZE)
	val deltas = heights.windowed(2).map { it[1] - it[0] }
	val repetition = deltas.findRepeatingPattern() ?: throw IllegalStateException("No repetition found")
	println("Found repetition of length ${repetition.length} after ${repetition.offset} moves.")

	println(findValue(heights, 2022, repetition.offset, repetition.length))
	println(findValue(heights, 1_000_000_000_000L, repetition.offset, repetition.length))
}

fun findValue(heights: List<Long>, numberOfDrops: Long, offset: Int, length: Int): Long {
	if (numberOfDrops < heights.size) return heights[numberOfDrops.toInt() - 1]

	val numberAfterOffset = numberOfDrops - offset - 1
	val numberOfRepetitions = numberAfterOffset / length
	val remainder = numberAfterOffset % length

	return (numberOfRepetitions - 1) * (heights[offset + length] - heights[offset]) +
			heights[offset + length + remainder.toInt()]
}

fun dropShapes(size: Int): List<Long> {
	val shapes = RepeatingSequence(SHAPES)
	val jets = RepeatingSequence(readLines(2022, FILENAME).toJets())
	var playingField = Bitmap(List(9) { Point2D(it, 0) }.toSet())

	fun dropOneShape(): Long {
		fun moveShapeByJet(shape: Bitmap): Bitmap {
			val jet = jets.next()
			val newShape = if (jet == Jet.LEFT) {
				if (shape.minX <= 1) return shape
				shape.moveLeft()
			} else {
				if (shape.maxX >= FIELD_WIDTH) return shape
				shape.moveRight()
			}
			return if (newShape.intersectsWith(playingField)) shape else newShape
		}

		// Floor of shape is always at 0, so height is maxY
		var currentShape = shapes.next().move(3, playingField.maxY + 4)

		while (true) {
			currentShape = moveShapeByJet(currentShape)
			val newShape = currentShape.moveDown()
			if (newShape.intersectsWith(playingField)) break

			currentShape = newShape
		}

		playingField = playingField.add(currentShape)
		return playingField.height - 1L
	}

	return IntRange(1, size).asSequence()
		.map { dropOneShape() }
		.toList()
}



