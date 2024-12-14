package eu.janvdb.aoc2024.day14

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input14-test.txt"
//const val WIDTH = 11
//const val HEIGHT = 7

const val FILENAME = "input14.txt"
const val WIDTH = 101
const val HEIGHT = 103

// > 1000
// < 5_841_849

fun main() {
	val robots = Robots.fromLines(readLines(2024, FILENAME))
	robots.print()

	part1(robots)
	part2(robots)
}

private fun part1(robots: Robots) {
	var moved = robots
	for (i in 0 until 100) {
		moved = moved.move()
	}
	moved.print()

	println(moved.getScore())
}

private fun part2(robots: Robots) {
	var moved = robots.move()
	var moves = 1
	while (true) {
		if (moved.canBeTree()) {
			moved.print()
			println(moves)
			break
		}

		moved = moved.move()
		moves++
	}
}

data class Robots(val robots: List<Robot>) {

	fun print() {
		for (y in 0 until HEIGHT) {
			for (x in 0 until WIDTH) {
				val count = robots.count { it.position.x == x && it.position.y == y }
				print(if (count == 0) '.' else count)
			}
			println()
		}
		println()
	}

	fun move(): Robots {
		return Robots(robots.map(Robot::move))
	}

	fun getScore(): Int {
		val perQuadrant = robots.mapNotNull(Robot::getQuadrant).groupingBy { it }.eachCount()
		return perQuadrant.map { it.value }.fold(1) { x, y -> x * y }
	}

	fun canBeTree(): Boolean {
		val points = robots.asSequence().map(Robot::position).toSet()

		fun hasNeighbour(point: Point2D): Boolean {
			return point.allNeighbours().any(points::contains)
		}

		val nextToEachOther = points.count(::hasNeighbour)
		return nextToEachOther > points.size * 7 / 10
	}

	companion object {
		fun fromLines(lines: List<String>): Robots {
			return Robots(lines.map(Robot::fromString))
		}
	}
}

data class Robot(val position: Point2D, val velocity: Point2D) {

	fun move(): Robot {
		val newX = (position.x + velocity.x + WIDTH) % WIDTH
		val newY = (position.y + velocity.y + HEIGHT) % HEIGHT
		return Robot(Point2D(newX, newY), velocity)
	}

	fun getQuadrant(): Int? {
		if (position.x < WIDTH / 2 && position.y < HEIGHT / 2) return 0
		if (position.x > WIDTH / 2 && position.y < HEIGHT / 2) return 1
		if (position.x > WIDTH / 2 && position.y > HEIGHT / 2) return 2
		if (position.x < WIDTH / 2 && position.y > HEIGHT / 2) return 3
		return null
	}

	companion object {
		fun fromString(input: String): Robot {
			val digits = Regex("-?\\d+").findAll(input)
				.map { it.value.toInt() }
				.toList()
			return Robot(Point2D(digits[0], digits[1]), Point2D(digits[2], digits[3]))
		}
	}
}