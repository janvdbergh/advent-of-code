package eu.janvdb.aoc2024.day18

import eu.janvdb.aocutil.kotlin.ShortestPathMove
import eu.janvdb.aocutil.kotlin.ShortestPathState
import eu.janvdb.aocutil.kotlin.findShortestPath
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input18-test.txt"
//const val SIZE = 6
//const val LIMIT = 12

// !(24, 30)

const val FILENAME = "input18.txt"
const val SIZE = 70
const val LIMIT = 1024

fun main() {
    val coordinates = readCoordinates()
    part1(coordinates)

    part2(coordinates)
}

private fun readCoordinates(): List<Point2D> {
    return readLines(2024, FILENAME).asSequence()
        .map { line -> line.split(",").map { it.toInt() } }
        .map { Point2D(it[0], it[1]) }
        .toList()
}

private fun part1(coordinates: List<Point2D>) {
    val memory = Memory(coordinates.asSequence().take(LIMIT).toSet())
    println(memory.shortestPath()?.cost)
}

private fun part2(coordinates: List<Point2D>) {
    fun isBlocked(size: Int): Boolean {
        val memory = Memory(coordinates.asSequence().take(size+1).toSet())
        return memory.shortestPath() == null
    }

    var min = 0
    var max = coordinates.size - 1
    while(min < max) {
        val middle = (min + max) / 2
        val blocked = isBlocked(middle)
        println("[$min - $middle - $max] -> $blocked @ ${coordinates[middle]}")

        if (blocked) {
            max = middle
        } else {
            min = middle + 1
        }
    }

    println("$min, ${coordinates[min]}")
}

data class Memory(val coordinates: Set<Point2D>) {
    fun shortestPath(): ShortestPathState<Point2D>? {
        return findShortestPath(Point2D(0, 0), Point2D(SIZE, SIZE), ::neighbours)
    }

    private fun neighbours(point: Point2D): Sequence<ShortestPathMove<Point2D>> {
        return point.horizontalAndVerticalNeighbors()
            .filter { it.x in 0..SIZE && it.y in 0..SIZE && !coordinates.contains(it) }
            .map { ShortestPathMove(it, 1) }
    }
}
