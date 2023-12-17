package eu.janvdb.aoc2023.day17

import eu.janvdb.aocutil.kotlin.ShortestPathMove
import eu.janvdb.aocutil.kotlin.findShortestPath
import eu.janvdb.aocutil.kotlin.point2d.Direction
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.max
import kotlin.math.min

//const val FILENAME = "input17-test1.txt"
//const val FILENAME = "input17-test2.txt"
const val FILENAME = "input17.txt"

fun main() {
	solve(1, 3)
	solve(4, 10)
}

/*
The solution to part 2 is not completely correct, but allowed me to get a good lowerbound and upperbound to
guess the solution.
 */

private fun solve(minMoves: Int, maxMoves: Int) {
	val puzzle = Puzzle.parse(readLines(2023, FILENAME), minMoves, maxMoves)
	val solution = puzzle.shortestPath()
	println(solution)
}

data class Puzzle(val width: Int, val height: Int, val costs: List<Int>, val minMoves: Int, val maxMoves: Int) {
	fun shortestPath(): Int {
		val startNode = Node(this, Point2D(0, 0), Direction.E, 1)
		val shortestPath = findShortestPath(startNode, Node::isEnd, Node::getMoves)!!

		println(shortestPath.toString())

		return shortestPath.cost
	}

	fun getCost(location: Point2D) = costs[location.y * width + location.x]

	companion object {
		fun parse(lines: List<String>, minMoves: Int, maxMoves: Int): Puzzle {
			val height = lines.size
			val width = lines[0].length
			val costs = lines.flatMap { line -> line.toCharArray().map { ch -> ch - '0' } }

			return Puzzle(width, height, costs, minMoves, maxMoves)
		}
	}
}

data class Node(val puzzle: Puzzle, val location: Point2D, val repeatedDirection: Direction, val repeatCount: Int) {
	fun getMoves(): Sequence<ShortestPathMove<Node>> {
		return Direction.entries.asSequence()
			.filter { canGo(it) }
			.map { Node(puzzle, location.move(it, 1), it, if (it == repeatedDirection) repeatCount + 1 else 1) }
			.map { ShortestPathMove(it, puzzle.getCost(it.location)) }
	}

	private fun canGo(direction: Direction): Boolean {
		if (repeatedDirection == direction && repeatCount >= puzzle.maxMoves) return false
		if (repeatedDirection != direction && repeatCount < puzzle.minMoves) return false

		return when (direction) {
			Direction.N -> location.y > max(0, puzzle.minMoves - repeatCount) && repeatedDirection != Direction.S
			Direction.W -> location.x > max(0, puzzle.minMoves - repeatCount) && repeatedDirection != Direction.E
			Direction.S ->
				location.y < min(puzzle.height - 1, puzzle.height - puzzle.minMoves + repeatCount)
						&& repeatedDirection != Direction.N
			Direction.E ->
				location.x < min(puzzle.width - 1, puzzle.width - puzzle.minMoves + repeatCount)
						&& repeatedDirection != Direction.W
		}
	}

	fun isEnd() = location.x == puzzle.width - 1 && location.y == puzzle.height - 1

	override fun toString(): String {
		return "$location/$repeatedDirection$repeatCount"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Node

		if (location != other.location) return false
		if (repeatedDirection != other.repeatedDirection) return false
		if (repeatCount != other.repeatCount) return false

		return true
	}

	override fun hashCode(): Int {
		var result = location.hashCode()
		result = 31 * result + repeatedDirection.hashCode()
		result = 31 * result + repeatCount
		return result
	}
}