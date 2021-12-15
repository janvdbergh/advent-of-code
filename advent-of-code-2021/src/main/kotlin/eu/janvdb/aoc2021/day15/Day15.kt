package eu.janvdb.aoc2021.day15

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readNonSeparatedDigits
import java.util.*

const val FILENAME = "input15.txt"
const val NO_PATH = Int.MAX_VALUE / 2

fun main() {
	runWithFactor(1)
	runWithFactor(5)
}

private fun runWithFactor(factor: Int) {
	val map = RiskMap.create(factor)
	val shortestPath = map.shortestPath()
	println(shortestPath)
}

data class RiskMap(val riskLevels: List<List<Int>>, val factor: Int) {
	val height = riskLevels.size
	val width = riskLevels[0].size

	fun shortestPath(): Int {
		val openList = PriorityQueue<Position>(Comparator.comparingInt { it.risk })
		openList.add(Position(Point2D.ORIGIN, 0))

		val bestMap = Array(height * factor) { IntArray(width * factor) { NO_PATH } }
		bestMap[0][0] = 0

		while (!openList.isEmpty()) {
			val current = openList.remove()

			sequenceOf(current.point.left(), current.point.down(), current.point.right(), current.point.up())
				.filter { it.x >= 0 && it.y >= 0 && it.x < width * factor && it.y < height * factor }
				.forEach {
					val cost = current.risk + getRiskLevel(it)
					if (cost < bestMap[it.y][it.x]) {
						bestMap[it.y][it.x] = cost
						openList.add(Position(it, cost))
					}
				}
		}

		return bestMap[factor * height - 1][factor * width - 1]
	}

	private fun getRiskLevel(point: Point2D): Int {
		val incrementX = point.x / width
		val incrementY = point.y / height
		return (riskLevels[point.y % height][point.x % width] + incrementX + incrementY - 1) % 9 + 1
	}

	companion object {
		fun create(factor: Int = 1): RiskMap {
			val riskLevels = readNonSeparatedDigits(2021, FILENAME)
			return RiskMap(riskLevels, factor)
		}
	}
}

data class Position(val point: Point2D, val risk: Int)
