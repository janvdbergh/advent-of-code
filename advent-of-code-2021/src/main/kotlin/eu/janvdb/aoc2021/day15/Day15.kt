package eu.janvdb.aoc2021.day15

import eu.janvdb.aocutil.kotlin.readNonSeparatedDigits
import kotlin.math.min

const val FILENAME = "input15.txt"
const val NO_PATH = Int.MAX_VALUE / 2

fun main() {
	runWithFactor(1)
	runWithFactor(5)
}

private fun runWithFactor(factor: Int) {
	val map = RiskMap.create(factor)
	val shortestPath = map.shortestPathFrom(0, 0)
	println(shortestPath.last().last())
}

data class RiskMap(val riskLevels: List<List<Int>>, val factor: Int) {
	val height = riskLevels.size
	val width = riskLevels[0].size

	fun shortestPathFrom(fromX: Int, fromY: Int): Array<IntArray> {
		val result = Array(height * factor) { IntArray(width * factor) { NO_PATH } }
		result[fromY][fromX] = 0

		fun resultAt(x1: Int, y1: Int): Int {
			if (x1 < 0 || x1 >= width * factor || y1 < 0 || y1 >= height * factor) return NO_PATH
			return result[y1][x1]
		}

		var done = false
		while (!done) {
			done = true

			for (y in 0 until height * factor) {
				for (x in 0 until width * factor) {
					val currentBest = result[y][x]
					val newBest = min(
						min(resultAt(x - 1, y), resultAt(x + 1, y)),
						min(resultAt(x, y - 1), resultAt(x, y + 1))
					) + getRiskLevel(x, y)

					if (newBest < currentBest) {
						result[y][x] = newBest
						done = false
					}
				}
			}
		}

		return result
	}

	private fun getRiskLevel(x: Int, y: Int): Int {
		val incrementX = x / width
		val incrementY = y / height
		return (riskLevels[y % height][x % width] + incrementX + incrementY - 1) % 9 + 1
	}

	companion object {
		fun create(factor: Int = 1): RiskMap {
			val riskLevels = readNonSeparatedDigits(2021, FILENAME)
			return RiskMap(riskLevels, factor)
		}
	}
}