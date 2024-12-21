package eu.janvdb.aoc2024.day20

import eu.janvdb.aocutil.kotlin.ShortestPathMove
import eu.janvdb.aocutil.kotlin.ShortestPathState
import eu.janvdb.aocutil.kotlin.findShortestPath
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines
import java.util.*

// Example
//const val FILENAME = "input20-test.txt"
//private const val MIN_SHORTCUT_GAIN_1 = 0
//private const val MIN_SHORTCUT_GAIN_2 = 50

// The real thing
const val FILENAME = "input20.txt"
private const val MIN_SHORTCUT_GAIN_1 = 100
private const val MIN_SHORTCUT_GAIN_2 = 100

fun main() {
    val raceTrack = RaceTrack.parse(readLines(2024, FILENAME))
    val shortestPath = raceTrack.findShortestPath()!!
    println(shortestPath.cost)

    val count1 = raceTrack.findCheats(2, MIN_SHORTCUT_GAIN_1, shortestPath.getStatesWithCost().toMap())
    println("${count1.values.sum()}: $count1")

    val count2 = raceTrack.findCheats(20, MIN_SHORTCUT_GAIN_2, shortestPath.getStatesWithCost().toMap())
    println("${count2.values.sum()}: $count2")
}

data class RaceTrack(val walls: Set<Point2D>, val openSpaces: Set<Point2D>, val start: Point2D, val end: Point2D) {

    fun findShortestPath(): ShortestPathState<Point2D, Nothing?>? {
        fun getNeighbours(point: Point2D) =
            point.horizontalAndVerticalNeighbors().filter { it in openSpaces }.map { ShortestPathMove(it, null,1) }

        return findShortestPath(start, end, ::getNeighbours)
    }

    fun findCheats(maxLength: Int, minShortcutGain: Int, bestPath: Map<Point2D, Int>): SortedMap<Int, Int> {
        fun distanceUsingShortcut(shortcutStart: Point2D, shortcutEnd: Point2D): Int {
            val startToShortcut = bestPath[shortcutStart]!!
            val shortCutToEnd = bestPath.size - bestPath[shortcutEnd]!!
            return startToShortcut + shortcutStart.manhattanDistanceTo(shortcutEnd) + shortCutToEnd
        }

        val pointsOnPath = bestPath.keys.toList()
        return pointsOnPath.asSequence()
            .flatMap { p1 -> pointsOnPath.asSequence().filter { it != p1 }.map { p2 -> Pair(p1, p2) } }
            .filter { (p1, p2) -> p1.manhattanDistanceTo(p2) <= maxLength }
            .map { (p1, p2) -> distanceUsingShortcut(p1, p2) }
            .map { bestPath.size - it }
            .filter { it >= minShortcutGain }
            .groupingBy { it }.eachCount().toSortedMap()
    }

    companion object {
        fun parse(lines: List<String>): RaceTrack {
            fun iteratePoints() = lines.indices.asSequence().flatMap { y ->
                lines[y].indices.asSequence().map { x -> Pair(Point2D(x, y), lines[y][x]) }
            }

            val walls = iteratePoints().filter { it.second == '#' }.map { it.first }.toSet()
            val openSpaces = iteratePoints().filter { it.second != '#' }.map { it.first }.toSet()
            val start = iteratePoints().filter { it.second == 'S' }.map { it.first }.single()
            val end = iteratePoints().filter { it.second == 'E' }.map { it.first }.single()
            return RaceTrack(walls, openSpaces, start, end)
        }
    }
}
