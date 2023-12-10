package eu.janvdb.aoc2023.day10

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input10-test1.txt"
//const val FILENAME = "input10-test2.txt"
const val FILENAME = "input10.txt"

fun main() {
	val lines = readLines(2023, FILENAME)
	val loop = Loop.create(lines)
	println(loop.links.size / 2)
	println(loop.countInside())
	loop.print()
}

data class Point(val location: Point2D, val ch: Char) {
	fun isStart() = ch == 'S'

	fun getLinks(): List<Link> {
		return when (ch) {
			'-' -> listOf(Link(location, location.left()), Link(location, location.right()))
			'|' -> listOf(Link(location, location.up()), Link(location, location.down()))
			'L' -> listOf(Link(location, location.right()), Link(location, location.up()))
			'J' -> listOf(Link(location, location.left()), Link(location, location.up()))
			'7' -> listOf(Link(location, location.left()), Link(location, location.down()))
			'F' -> listOf(Link(location, location.right()), Link(location, location.down()))
			'S' -> listOf(
				Link(location, location.left()),
				Link(location, location.right()),
				Link(location, location.up()),
				Link(location, location.down())
			)

			'.' -> emptyList()
			else -> throw IllegalArgumentException("Unknown character: $ch")
		}
	}
}

data class Link(val from: Point2D, val to: Point2D)

data class Loop(val points: Map<Point2D, Point>, val links: Set<Link>) {
	private val minX = links.minBy { it.from.x }.from.x
	private val maxX = links.maxBy { it.from.x }.from.x
	private val minY = links.minBy { it.from.y }.from.y
	private val maxY = links.maxBy { it.from.y }.from.y

	fun countInside(): Int {
		return (minX..maxX).flatMap { x ->
			(minY..maxY)
				.map { y -> Point2D(x, y) }
		}
			.count { !has(it) && hasInside(it) }
	}

	private fun has(point: Point2D) = links.any { it.from == point }

	private fun hasInside(point: Point2D): Boolean {
		fun hasLink(from: Point2D, to: Point2D) = Link(from, to) in links || Link(to, from) in links

		val wallsLeft = (minX..point.x).count { x -> hasLink(Point2D(x, point.y - 1), Point2D(x, point.y)) }
		val wallsRight = (point.x..maxX).count { x -> hasLink(Point2D(x, point.y), Point2D(x, point.y + 1)) }
		val wallsUp = (minY..point.y).count { y -> hasLink(Point2D(point.x, y), Point2D(point.x - 1, y)) }
		val wallsDown = (point.y..maxY).count { y -> hasLink(Point2D(point.x + 1, y), Point2D(point.x, y)) }

		return wallsLeft % 2 == 1 && wallsRight % 2 == 1 && wallsUp % 2 == 1 && wallsDown % 2 == 1
	}

	fun print() {
		for (y in minY..maxY) {
			for (x in minX..maxX) {
				val point = Point2D(x, y)
				if (has(point)) {
					print(points[point]!!.ch)
				} else if (hasInside(point)) {
					print('I')
				} else {
					print('O')
				}
			}
			println()
		}
	}


	companion object {
		fun create(lines: List<String>): Loop {
			val allPoints = lines
				.flatMapIndexed { y, line -> line.mapIndexed { x, ch -> Point(Point2D(x, y), ch) } }
				.associateBy { it.location }
			val allLinks = allPoints.values.asSequence()
				.flatMap { it.getLinks() }.toSet()
			val recursiveLinksPerLocation = allLinks
				.asSequence()
				.filter { allLinks.contains(Link(it.to, it.from)) }
				.groupBy { it.from }
				.mapValues { entry -> entry.value.map { it.to } }

			val start = allPoints.values.find { it.isStart() }!!.location
			val visitedPoints = mutableListOf(start)
			val foundLinks = mutableSetOf<Link>()
			do {
				val last = visitedPoints.last()
				val next = recursiveLinksPerLocation[last]!!.find { it !in visitedPoints }
				if (next != null) {
					foundLinks.add(Link(last, next))
					visitedPoints.add(next)
				}
			} while (next != null)
			foundLinks.add(Link(visitedPoints.last(), start))

			return Loop(allPoints, foundLinks)
		}
	}
}