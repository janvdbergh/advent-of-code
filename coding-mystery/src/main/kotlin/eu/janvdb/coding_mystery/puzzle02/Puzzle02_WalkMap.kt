package eu.janvdb.coding_mystery.puzzle02

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines
import eu.janvdb.coding_mystery.PREFIX

fun main() {
	val instructions = readLines(PREFIX, "puzzle02_instructions.txt")
		.flatMap { it.split(",") }
		.map { it.trim() }
		.map { Instruction.valueOf(it) }

	val map = Map.create(readLines(PREFIX, "puzzle02_map.txt"))

	var current = map.getStartLocation()
	instructions.forEach { current = map.move(current, it) }
	println(current)
}

enum class MapType(val ch: Char) {
	WALL('#'), EXIT('X'), ENTRY('I'), EMPTY(' ');
}

enum class Instruction {
	N, E, S, W
}

data class Map(val height: Int, val width: Int, val data: List<MapType>) {
	fun getStartLocation(): Point2D {
		for (y in 1..height) {
			for (x in 1..width) {
				if (at(x, y) == MapType.ENTRY) return Point2D(x, y)
			}
		}
		throw IllegalArgumentException()
	}

	fun move(current: Point2D, instruction: Instruction): Point2D {
		val next = when (instruction) {
			Instruction.N -> current.up()
			Instruction.E -> current.right()
			Instruction.S -> current.down()
			Instruction.W -> current.left()
		}

		if (at(next.x, next.y) == MapType.WALL) {
			return current
		}
		return next
	}

	private fun at(x: Int, y: Int) = data[x - 1 + (y - 1) * width]

	companion object {
		fun create(lines: List<String>): Map {
			val data = lines.flatMap { line -> line.map { ch -> MapType.values().find { it.ch == ch }!! } }
			return Map(lines.size, lines[0].length, data)
		}
	}
}