package eu.janvdb.aoc2021.day15

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readNonSeparatedDigits

const val FILENAME = "input15.txt"

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
		return findShortestPath(Point2D(0, 0), Point2D(factor * width - 1, factor * height - 1)) {
			sequenceOf(it.right(), it.down(), it.left(), it.up())
				.filter { (x,y) -> x>=0 && x<factor*width && y>=0 && y<factor*height }
				.map { Pair(it, getRiskLevel(it)) }
		}!!
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