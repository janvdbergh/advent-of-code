package eu.janvdb.aoc2025.day09

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

//const val FILENAME = "input09-test.txt"
const val FILENAME = "input09.txt"

fun main() {
    // < 4773451098
    val lines = readLines(2025, FILENAME)
    val tileMap = TileMap.parse(lines)

    println(tileMap.biggestRectangle1())
    println(tileMap.biggestRectangle2())
}

// The tile map compacts the graph, with coordinates starting at 0 and compacting empty spaces into one
// The width and heights contain the exact width and height of each part
data class TileMap(val tiles: List<Point2D>, val realXValues: List<Int>, val realYValues: List<Int>) {
    val rectangles = (0..<tiles.size).flatMap { i -> (i + 1..<tiles.size).map { j -> Pair(tiles[i], tiles[j]) } }
    val fullyContainedPoints = calculateFullyContainedPoints()

    fun biggestRectangle1(): Long {
        return rectangles.asSequence()
            .map { Pair(realTile(it.first), realTile(it.second)) }
            .maxOf { (tile1, tile2) -> 1L * (abs(tile2.x - tile1.x) + 1) * (abs(tile2.y - tile1.y) + 1) }
    }

    fun biggestRectangle2(): Long {
        return rectangles.asSequence()
            .filter(this::isFullyContained)
            .map { Pair(realTile(it.first), realTile(it.second)) }
            .maxOf { (tile1, tile2) -> 1L * (abs(tile2.x - tile1.x) + 1) * (abs(tile2.y - tile1.y) + 1) }
    }

    private fun realTile(tile: Point2D) = Point2D(realXValues[tile.x], realYValues[tile.y])

    private fun isFullyContained(rectangle: Pair<Point2D, Point2D>): Boolean {
        val points = (min(rectangle.first.x, rectangle.second.x)..max(rectangle.first.x, rectangle.second.x)).flatMap { x ->
            (min(rectangle.first.y, rectangle.second.y)..max(rectangle.first.y, rectangle.second.y)).map { y -> Point2D(x, y) }
        }
        return points.all { fullyContainedPoints.contains(it) }
    }

    private fun calculateFullyContainedPoints(): Set<Point2D> {
        val edges = (tiles + tiles[0]).windowed(2) { Edge(it[0], it[1]) }
        val directlyContainedPoints = edges.flatMap(Edge::getPoints).toSet()

        val allPoints = directlyContainedPoints.toMutableSet()
        val start = Point2D(tiles.filter { it.y == 0 }.minOf { it.x } + 1, 1)
        val toFill = mutableSetOf(start)

        while (!toFill.isEmpty()) {
            val current = toFill.first()
            toFill.remove(current)

            val newElements = (current.horizontalAndVerticalNeighbors() - allPoints).toSet()
            toFill += newElements
            allPoints += newElements
        }

        println()
        printPoints(allPoints, directlyContainedPoints)

        return allPoints
    }

    private fun printPoints(points: Set<Point2D>, edgePoints: Set<Point2D>) {
        for (y in 0..<realYValues.size) {
            for (x in 0..<realXValues.size) {
                val point = Point2D(x, y)
                if (edgePoints.contains(point))
                    print("#")
                else if (points.contains(point))
                    print(".")
                else
                    print(' ')
            }
            println()
        }
    }

    companion object {
        fun parse(lines: List<String>): TileMap {
            val tiles = lines
                .map { it.split(",") }
                .map { Point2D(it[0].toInt(), it[1].toInt()) }

            val xValues = tiles.asSequence().map { it.x }.sorted().distinct().toList()
            val yValues = tiles.asSequence().map { it.y }.sorted().distinct().toList()

            val realXValues = xValues.flatMap { sequenceOf(it, it + 1) }.dropLast(1)
            val realYValues = yValues.flatMap { sequenceOf(it, it + 1) }.dropLast(1)
            val mappedTiles = tiles.map { tile -> Point2D(realXValues.indexOf(tile.x), realYValues.indexOf(tile.y)) }

            return TileMap(mappedTiles, realXValues, realYValues)
        }
    }
}

data class Edge(val p1: Point2D, val p2: Point2D) {
    fun getPoints(): Sequence<Point2D> {
        // Edges are either horizontal or vertical
        return (min(p1.x, p2.x)..max(p1.x, p2.x)).asSequence()
            .flatMap { x ->
                (min(p1.y, p2.y)..max(p1.y, p2.y)).asSequence()
                    .map { y -> Point2D(x, y) }
            }
    }
}
