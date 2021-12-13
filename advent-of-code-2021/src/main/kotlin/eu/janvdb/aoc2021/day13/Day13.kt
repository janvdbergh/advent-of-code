package eu.janvdb.aoc2021.day13

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readGroupedLines

const val FILENAME = "input13.txt"

// < 912

fun main() {
	val groupedLines = readGroupedLines(2021, FILENAME)
	val sheet = Sheet.create(groupedLines[0])
	val instructions = groupedLines[1].map(Instruction::create)

	val result1 = instructions[0].apply(sheet)
	println(result1.points.size)

	var result2 = sheet
	instructions.forEach { result2 = it.apply(result2) }
	result2.print()
}

data class Sheet(val points: Set<Point2D>) {
	fun print() {
		val minX = points.minOf { it.x }
		val maxX = points.maxOf { it.x }
		val minY = points.minOf { it.y }
		val maxY = points.maxOf { it.y }
		for(y in minY .. maxY) {
			for(x in minX..maxX) {
				if(points.contains(Point2D(x,y))) {
					print("##")
				} else {
					print ("  ")
				}
			}
			println()
		}
	}

	companion object {
		fun create(coordinates: List<String>): Sheet {
			val points = coordinates.asSequence().map(Point2D.Companion::createCommaSeparated).toSet()
			return Sheet(points)
		}
	}
}

data class Instruction(val axis: Axis, val value: Int) {
	fun apply(sheet: Sheet): Sheet {
		return when (axis) {
			Axis.X -> fold(sheet) { point ->
				if (point.x <= value) point else Point2D(2 * value - point.x, point.y)
			}
			Axis.Y -> fold(sheet) { point ->
				if (point.y <= value) point else Point2D(point.x, 2 * value - point.y)
			}
		}
	}

	private fun fold(sheet: Sheet, foldFunction: (Point2D) -> Point2D): Sheet {
		val newPoints = sheet.points.asSequence().map(foldFunction).toSet()
		return Sheet(newPoints)
	}

	companion object {
		private val REGEX = Regex("fold along ([xy])=(\\d+)")

		fun create(description: String): Instruction {
			val match = REGEX.matchEntire(description)!!
			val axis = Axis.valueOf(match.groupValues[1].uppercase())
			val value = match.groupValues[2].toInt()
			return Instruction(axis, value)
		}
	}
}

enum class Axis { X, Y }