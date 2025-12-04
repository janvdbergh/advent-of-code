package eu.janvdb.aoc2025.day04

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input04.txt"
//const val FILENAME = "input04-test.txt"

fun main() {
    val map = Map.parse(readLines(2025, FILENAME))

    val reachable = map.getReachableRolls()
    println(reachable.size)

    var previousCount: Int
    var currentMap = map
    do {
        previousCount = currentMap.numberOfRolls
        val reachableRolls = currentMap.getReachableRolls()
        println("Removing ${reachableRolls.size} rolls")
        currentMap = currentMap.remove(reachableRolls)
    } while(previousCount != currentMap.numberOfRolls)

    println(map.numberOfRolls - currentMap.numberOfRolls)
}

data class Map(val coordinates: Set<Point2D>) {
    val numberOfRolls = coordinates.size

    fun allLocations(): Sequence<Point2D> {
        val maxX = coordinates.maxOf { it.x }
        val maxY = coordinates.maxOf { it.y }
        return (0..maxY).asSequence().flatMap { y -> (0..maxX).asSequence().map { x -> Point2D(x, y) } }
    }

    fun hasRoll(point: Point2D) = coordinates.contains(point)

    fun neighbours(point: Point2D) = point.allNeighbours().count { hasRoll(it) }

    fun remove(rolls: Set<Point2D>): Map = Map(coordinates - rolls)

    fun getReachableRolls() =  allLocations().filter{hasRoll(it) && neighbours(it) <4}.toSet()

    companion object {
        fun parse(lines: List<String>): Map {
            val coordinates = lines.flatMapIndexed { y, line ->
                line.mapIndexed { x, c -> Pair(x, c) }
                    .filter { it.second == '@' }
                    .map { Point2D(it.first, y) }
            }.toSet()
            return Map(coordinates)
        }
    }
}
