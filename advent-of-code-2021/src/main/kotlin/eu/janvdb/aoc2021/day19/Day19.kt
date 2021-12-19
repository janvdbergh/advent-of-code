package eu.janvdb.aoc2021.day19

import eu.janvdb.aocutil.kotlin.point3d.Point3D
import eu.janvdb.aocutil.kotlin.point3d.Transformation
import eu.janvdb.aocutil.kotlin.point3d.Transformation.Companion.ALL_ROTATIONS
import eu.janvdb.aocutil.kotlin.readGroupedLines

const val FILENAME = "input19.txt"

fun main() {
	val scanners = readGroupedLines(2021, FILENAME).map(Scanner::parse)

	var combinedScanner = scanners[0]
	val remainingScanners = scanners.toMutableList()
	remainingScanners.remove(combinedScanner)
	val scannerCoordinates = mutableListOf(Point3D(0, 0, 0))
	while (remainingScanners.isNotEmpty()) {
		val match = remainingScanners
			.map { Pair(it, combinedScanner.combineWith(it)) }
			.find { it.second != null } ?: throw RuntimeException("No result")

		remainingScanners.remove(match.first)
		combinedScanner = match.second!!.first
		scannerCoordinates.add(match.second!!.second)
	}
	println("Part 1: ${combinedScanner.beacons.size}")

	val maxDistance = scannerCoordinates.flatMap { scannerCoordinates.map(it::manhattanDistance) }.maxOrNull()
	println("Part 2: $maxDistance")
}

class Scanner(val name: String, val beacons: Set<Point3D>) {
	/**
	 * If a match is found with this rotation: returns the combined scanner and the relative position of the other scanner
	 */
	fun combineWith(other: Scanner): Pair<Scanner, Point3D>? {
		val match = ALL_ROTATIONS.map { tryRotation(it, other) }
			.find { it != null } ?: return null

		return Pair(Scanner(name + "/" + other.name, beacons + match.first), match.second)
	}

	/**
	 * If a match is found with this rotation: returns the transformed point and the relative position of the other scanner
	 */
	private fun tryRotation(rotation: Transformation, other: Scanner): Pair<List<Point3D>, Point3D>? {
		val transformedPoints = other.beacons.map { rotation * it }
		val mostCommonDistance = beacons.asSequence()
			.flatMap { mine -> transformedPoints.map { mine - it } }
			.groupingBy { it }.eachCount()
			.maxByOrNull { it.value }!!

		if (mostCommonDistance.value < 12) return null

		return Pair(transformedPoints.map { mostCommonDistance.key + it }, mostCommonDistance.key)
	}

	override fun toString() = "$name (${beacons.size})"

	companion object {
		fun parse(lines: List<String>): Scanner {
			val name = lines[0].substring(4, lines[0].length - 4)
			val coordinates = lines.asSequence().drop(1).map { Point3D.createCommaSeparated(it) }.toSet()

			return Scanner(name, coordinates)
		}
	}
}
