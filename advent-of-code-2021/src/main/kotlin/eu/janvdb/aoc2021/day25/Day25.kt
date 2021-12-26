package eu.janvdb.aoc2021.day25

import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input25.txt"

fun main() {
	val board = Board.create()

	var currentBoard = board
	var nextBoard = board.move()
	var step = 1
	while(currentBoard != nextBoard) {
		currentBoard = nextBoard
		nextBoard = currentBoard.move()
		step++
	}

	println(step)
}

enum class Critter(val ch: Char) {
	EAST('>'),
	SOUTH('v');

	companion object {
		fun map(ch: Char) = values().find { it.ch == ch }
	}
}

data class Board(val height: Int, val width: Int, val map: List<Critter?>) {
	fun move(): Board {
		val newMap = map.toMutableList()

		fun moveOneDirection(type: Critter, nextIndex: (Int, Int) -> Int) {
			val toMove = mutableMapOf<Int, Int>()
			for (y in 0 until height) {
				for (x in 0 until width) {
					val current = index(x, y)
					val next = nextIndex(x, y)
					if (newMap[current] == type && newMap[next] == null) toMove[current] = next
				}
			}
			toMove.forEach {
				newMap[it.key] = null
				newMap[it.value] = type
			}
		}

		moveOneDirection(Critter.EAST) { x, y -> index(x + 1, y) }
		moveOneDirection(Critter.SOUTH) { x, y -> index(x, y + 1) }
		return Board(height, width, newMap)
	}


	override fun toString(): String {
		val result = StringBuilder()
		for (y in 0 until height) {
			for (x in 0 until width) {
				result.append(map[index(x, y)]?.ch ?: '.')
			}
			result.append('\n')
		}
		return result.toString()
	}

	private fun index(x: Int, y: Int) = (y + height) % height * width + (x + width) % width

	companion object {
		fun create(): Board {
			val lines = readLines(2021, FILENAME)
			val map = lines.flatMap { it.map(Critter::map) }
			return Board(lines.size, lines[0].length, map)
		}
	}
}