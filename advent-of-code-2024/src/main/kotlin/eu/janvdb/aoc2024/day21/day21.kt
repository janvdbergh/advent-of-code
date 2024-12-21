package eu.janvdb.aoc2024.day21

import eu.janvdb.aocutil.kotlin.ShortestPathMove
import eu.janvdb.aocutil.kotlin.findShortestPaths
import eu.janvdb.aocutil.kotlin.point2d.Direction
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input21-test.txt"
const val FILENAME = "input21.txt"

const val RIGHT = '>'
const val UP = '^'
const val LEFT = '<'
const val DOWN = 'v'

// +---+---+---+
// | 7 | 8 | 9 |
// +---+---+---+
// | 4 | 5 | 6 |
// +---+---+---+
// | 1 | 2 | 3 |
// +---+---+---+
//     | 0 | A |
//     +---+---+
val NUMERIC_KEYPAD = Keypad(
	mapOf(
		Pair(Point2D(0, 0), '7'),
		Pair(Point2D(1, 0), '8'),
		Pair(Point2D(2, 0), '9'),
		Pair(Point2D(0, 1), '4'),
		Pair(Point2D(1, 1), '5'),
		Pair(Point2D(2, 1), '6'),
		Pair(Point2D(0, 2), '1'),
		Pair(Point2D(1, 2), '2'),
		Pair(Point2D(2, 2), '3'),
		Pair(Point2D(1, 3), '0'),
		Pair(Point2D(2, 3), 'A'),
	)
)

//     +---+---+
//     | ^ | A |
// +---+---+---+
// | < | v | > |
// +---+---+---+
val DIRECTIONAL_KEYPAD = Keypad(
	mapOf(
		Pair(Point2D(1, 0), UP),
		Pair(Point2D(2, 0), 'A'),
		Pair(Point2D(0, 1), LEFT),
		Pair(Point2D(1, 1), DOWN),
		Pair(Point2D(2, 1), RIGHT),
	)
)

fun main() {
	val lines = readLines(2024, FILENAME)
	execute(lines, 3)
	execute(lines, 26)
}

private fun execute(lines: List<String>, numberOfRobots: Int) {
	val typers = createRobotStack(numberOfRobots)

	fun mapCode(code: String): Long {
		val shortest = typers.numberOfStepsForCode(code.toList())
		println("$code: $shortest")
		val value = code.substring(0, code.length - 1).toLong()
		return shortest * value
	}

	println(lines.map(::mapCode).sum())
}

fun createRobotStack(size: Int): Typer {
	var current: Typer = HumanTyper()
	for (i in 0 until size)
		current = RobotTyper(if (i == size - 1) NUMERIC_KEYPAD else DIRECTIONAL_KEYPAD, current)

	return current
}

class Keypad(private val positionToKey: Map<Point2D, Char>) {
	private val shortestPaths = calculateShortestPaths()

	fun getShortestPath(from: Char, to: Char): List<List<Char>> {
		if (from == to) return listOf(listOf())
		return shortestPaths[Pair(from, to)]!!
	}

	private fun calculateShortestPaths(): Map<Pair<Char, Char>, List<List<Char>>> {
		fun getNeighbours(position: Point2D): Sequence<ShortestPathMove<Point2D, Direction>> {
			return Direction.entries.asSequence()
				.map { ShortestPathMove(position.move(it), it, 1) }
				.filter { it.nextState in positionToKey }
		}

		return positionToKey.keys.asSequence()
			.flatMap { from -> positionToKey.keys.asSequence().filter { it != from }.map { Pair(from, it) } }
			.map { fromTo ->
				val shortestPaths =
					findShortestPaths(fromTo.first, fromTo.second, ::getNeighbours)
						.map { result ->
							result.getMoves().map { it.ch }.toList().asReversed()
						}
				Pair(Pair(positionToKey[fromTo.first]!!, positionToKey[fromTo.second]!!), shortestPaths)
			}
			.toMap()
	}
}

interface Typer {
	fun numberOfStepsForCode(code: List<Char>): Long
}

class HumanTyper : Typer {
	override fun numberOfStepsForCode(code: List<Char>) = code.size.toLong()
}

class RobotTyper(private val keyPad: Keypad, private val next: Typer) : Typer {
	private val cache = mutableMapOf<List<Char>, Long>()

	override fun numberOfStepsForCode(code: List<Char>): Long {
		return cache.computeIfAbsent(code, ::numberOfStepsForCodeNoCache)
	}

	private fun numberOfStepsForCodeNoCache(code: List<Char>): Long {
		var currentPosition = 'A'
		var completeSequence = 0L

		code.forEach { ch ->
			val sequence = (keyPad.getShortestPath(currentPosition, ch))
				.minOf { sequence -> next.numberOfStepsForCode(sequence + 'A') }
			completeSequence += sequence
			currentPosition = ch
		}

		return completeSequence
	}

}