package eu.janvdb.aoc2024.day10

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines
import kotlin.streams.toList

//const val FILENAME = "input10-test.txt"
const val FILENAME = "input10.txt"

private const val TRAIL_HEAD = 0
private const val TRAIL_END = 9

fun main() {
    val map = Map.fromLines(readLines(2024, FILENAME))
    val trailHeads = map.getTrailHeads()

    val reachableTrailEnds = trailHeads.map { trailHead -> map.countReachableTrailEnds(trailHead) }.sum()
    println(reachableTrailEnds)

    val trails = trailHeads.map { trailHead -> map.countTrails(trailHead) }.sum()
    println(trails)
}

data class Map(val lines: List<List<Int>>) {
    val height = lines.size
    val width = lines[TRAIL_HEAD].size

    fun getTrailHeads(): Sequence<Point2D> {
        return points().filter { point -> get(point) == TRAIL_HEAD }
    }

    fun countReachableTrailEnds(trailHead: Point2D): Int {
        var currentPoints = setOf(trailHead)
        var currentValue = 0

        while(currentValue != TRAIL_END) {
            currentPoints = currentPoints.asSequence()
                .flatMap { point -> point.horizontalNeighbors() }
                .filter { point -> get(point) == currentValue + 1 }
                .toSet()
            currentValue++
        }

        return currentPoints.size
    }

    fun countTrails(trailHead: Point2D): Int {
        var currentPoints = listOf(trailHead)
        var currentValue = 0

        while(currentValue != TRAIL_END) {
            currentPoints = currentPoints.asSequence()
                .flatMap { point -> point.horizontalNeighbors() }
                .filter { point -> get(point) == currentValue + 1 }
                .toList()
            currentValue++
        }

        return currentPoints.size
    }

    private fun get(point: Point2D): Int? {
        if(point.x<0 || point.x>=width || point.y<0 || point.y>=height) return null
        return lines[point.y][point.x]
    }

    private fun points(): Sequence<Point2D> = lines.indices.asSequence()
        .flatMap { y -> lines[y].indices.asSequence().map { x -> Point2D(x, y) } }

    companion object {
        fun fromLines(lines: List<String>): Map {
            val points = lines.map { line -> line.chars().map(Character::getNumericValue).toList() }
            return Map(points)
        }
    }
}
