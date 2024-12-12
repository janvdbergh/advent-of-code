package eu.janvdb.aoc2024.day12

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input12-test.txt"
const val FILENAME = "input12.txt"

fun main() {
    val map = Map.parse(readLines(2024, FILENAME))
    val areas = map.getAreas()

    val fenceCost1 = areas.sumOf { map.getFenceLength1(it) * it.size }
    println(fenceCost1)

    val fenceCost2 = areas.sumOf { map.getFenceLength2(it) * it.size }
    println(fenceCost2)
}

data class Map(val height: Int, val width: Int, val characters: List<Char>) {

    fun getAreas(): Set<Set<Point2D>> {
        val remainingPoints = (0 until height).flatMap { y -> (0 until width).map { x -> Point2D(x, y) } }.toMutableSet()
        val result = mutableSetOf<Set<Point2D>>()
        while (remainingPoints.isNotEmpty()) {
            result.add(getNextEnclosedArea(remainingPoints))
        }
        return result
    }

    private fun getNextEnclosedArea(remainingPoints: MutableSet<Point2D>): Set<Point2D> {
        val firstPoint = remainingPoints.first()
        val result = mutableSetOf(firstPoint)
        val toDo = mutableListOf(firstPoint)
        val type = getType(firstPoint)!!

        while (toDo.isNotEmpty()) {
            val point = toDo.removeAt(0)
            point.horizontalNeighbors()
                .filter { !result.contains(it) }
                .filter { getType(it) == type }
                .forEach { result.add(it); toDo.add(it) }
        }
        remainingPoints.removeAll(result)
        return result
    }

    fun getFenceLength1(area: Set<Point2D>): Int {
        return getFences(area).size
    }

    fun getFenceLength2(area: Set<Point2D>): Int {
        val fences = getFences(area)
        return fences.count { !hasPreviousSegment(fences, it) }
    }

    private fun hasPreviousSegment(fences: Set<Pair<Point2D, Point2D>>, fence: Pair<Point2D, Point2D>): Boolean {
        val previousSegment = if (fence.first.x == fence.second.x) {
            Pair(Point2D(fence.first.x - 1, fence.first.y), Point2D(fence.second.x - 1, fence.second.y))
        } else {
            Pair(Point2D(fence.first.x, fence.first.y - 1), Point2D(fence.second.x, fence.second.y - 1))
        }

        return fences.contains(previousSegment)
    }

    private fun getFences(area: Set<Point2D>): Set<Pair<Point2D, Point2D>> {
        val type = getType(area.first())!!
        return area.flatMap { point ->
            point.horizontalNeighbors().filter { getType(it) != type }.map { Pair(point, it) }
        }.toSet()
    }

    private fun getType(point: Point2D): Char? {
        if (point.x < 0 || point.x >= width || point.y < 0 || point.y >= height) return null
        return characters[point.y * width + point.x]
    }

    companion object {
        fun parse(lines: List<String>): Map {
            val height = lines.size
            val width = lines[0].length
            val characters = lines.flatMap { it.toList() }
            return Map(height, width, characters)
        }
    }
}
