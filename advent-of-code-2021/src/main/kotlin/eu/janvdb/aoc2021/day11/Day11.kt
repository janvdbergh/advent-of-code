package eu.janvdb.aoc2021.day11

import eu.janvdb.aocutil.kotlin.readNonSeparatedDigits

const val FILENAME = "input11.txt"
const val NUMBER_OF_STEPS = 100

fun main() {
	part1()
	part2()
}

private fun part1() {
	val board = Board.create()

	var result = 0
	for (i in 0 until NUMBER_OF_STEPS) {
		result += board.step()
	}

	board.print()
	println(result)
}

private fun part2() {
	val board = Board.create()

	var step = 0
	while (true) {
		step++
		val result = board.step()
		if (result == board.width * board.height) break
	}

	board.print()
	println(step)
}

data class Board(val width: Int, val height: Int, val digits: MutableList<Int>) {
	fun step(): Int {
		val hasFlashed = MutableList(digits.size) { false }

		fun getFlashed(i: Int, j: Int): Boolean {
			if (i < 0 || i >= width || j < 0 || j >= height) return false
			return hasFlashed[i * width + j]
		}

		fun setFlashed(i: Int, j: Int) {
			if (i < 0 || i >= width || j < 0 || j >= height) return
			hasFlashed[i * width + j] = true
		}

		digits.indices.forEach { digits[it] = digits[it] + 1 }

		var changed = true
		while (changed) {
			changed = false
			for (i in 0 until height) {
				for (j in 0 until width) {
					if (!getFlashed(i, j) && getValue(i, j) >= 10) {
						setFlashed(i, j)
						incrementValue(i - 1, j - 1)
						incrementValue(i - 1, j)
						incrementValue(i - 1, j + 1)
						incrementValue(i, j - 1)
						incrementValue(i, j + 1)
						incrementValue(i + 1, j - 1)
						incrementValue(i + 1, j)
						incrementValue(i + 1, j + 1)
						changed = true
					}
				}
			}
		}

		digits.indices.forEach { if (digits[it] >= 10) digits[it] = 0 }
		return hasFlashed.count { it }
	}

	fun print() {
		for (i in 0 until height) {
			for (j in 0 until width) {
				print(getValue(i, j))
			}
			println()
		}
		println()
	}

	private fun getValue(i: Int, j: Int): Int {
		if (i < 0 || i >= width || j < 0 || j >= height) return 0
		return digits[i * width + j]
	}

	private fun incrementValue(i: Int, j: Int) {
		if (i < 0 || i >= width || j < 0 || j >= height) return
		digits[i * width + j] = digits[i * width + j] + 1
	}

	companion object {
		fun create(): Board {
			val digits = readNonSeparatedDigits(2021, FILENAME)
			return Board(digits[0].size, digits.size, digits.flatten().toMutableList())
		}
	}
}