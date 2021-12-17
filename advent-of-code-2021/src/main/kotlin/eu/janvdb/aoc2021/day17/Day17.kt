package eu.janvdb.aoc2021.day17

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import kotlin.math.max
import kotlin.math.min

//val TARGET = Target(20, 30, -10, -5)
val TARGET = Target(155, 182, -117, -67)

fun main() {
	part1()
	part2()
}

private fun part1() {
	var startX = 1
	while (true) {
		val reachedX = startX * (startX + 1) / 2
		if (reachedX > TARGET.x2) {
			println("No startX found")
			return
		}
		if (reachedX >= TARGET.x1) break

		startX++
	}

	val bestTrajectory = IntRange(0, 1000)
		.map { calculateTrajectory(Point2D(startX, it)) }
		.filter { it.reachedTarget }
		.maxByOrNull { it.maxHeight() }!!

	bestTrajectory.print()
}

private fun part2() {
	val result = IntRange(1, TARGET.x2)
		.flatMap { x -> IntRange(TARGET.y1, 1000).map { Point2D(x, it) } }
		.map { calculateTrajectory(it) }
		.count { it.reachedTarget }

	println(result)
}

fun calculateTrajectory(startVector: Point2D): Trajectory {
	var currentPoint = Point2D(0, 0)
	var currentVector = startVector
	val trajectory = mutableListOf(currentPoint)

	while (!TARGET.isOutOfRangeOf(currentPoint)) {
		currentPoint = Point2D(currentPoint.x + currentVector.x, currentPoint.y + currentVector.y)
		currentVector = Point2D(max(0, currentVector.x - 1), currentVector.y - 1)

		trajectory.add(currentPoint)
		if (TARGET.contains(currentPoint)) return Trajectory(startVector, trajectory, true)
	}

	return Trajectory(startVector, trajectory, false)
}

data class Trajectory(val startVector: Point2D, val trajectory: List<Point2D>, val reachedTarget: Boolean) {
	fun print() {
		val minX = min(trajectory.minOf { it.x }, TARGET.x1)
		val maxX = max(trajectory.maxOf { it.x }, TARGET.x2)
		val minY = min(trajectory.minOf { it.y }, TARGET.y1)
		val maxY = max(maxHeight(), TARGET.y2)
		for (y in maxY downTo minY) {
			for (x in minX..maxX) {
				val point = Point2D(x, y)
				val ch = if (trajectory.contains(point)) '#' else if (TARGET.contains(point)) 'T' else '.'
				print(ch)
			}
			println()
		}
		println("Reached target: $reachedTarget")
		println("Max height: ${maxHeight()}")
	}

	fun maxHeight() = trajectory.maxOf { it.y }
}

data class Target(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {
	fun contains(point: Point2D): Boolean {
		return (point.x in x1..x2) && (point.y in y1..y2)
	}

	fun isOutOfRangeOf(point: Point2D): Boolean {
		return point.x > x2 || point.y < y1
	}
}