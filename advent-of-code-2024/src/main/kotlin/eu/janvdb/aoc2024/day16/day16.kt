package eu.janvdb.aoc2024.day16

import eu.janvdb.aocutil.kotlin.*
import eu.janvdb.aocutil.kotlin.point2d.Direction
import eu.janvdb.aocutil.kotlin.point2d.Point2D

//const val FILENAME = "input16-test.txt"
//const val FILENAME = "input16-test2.txt"
const val FILENAME = "input16.txt"

fun main() {
    val maze = Maze.parse(readLines(2024, FILENAME))
    val paths = maze.findShortestPaths()
    println(paths.firstOrNull()?.cost)

    val points = paths.asSequence().flatMap { it.getStates() }.map {it.position}.toSet()
    println(points.size)
}

data class Maze(val width: Int, val height: Int, val walls: List<Boolean>, val start: Point2D, val end: Point2D) {

    fun findShortestPaths() = findShortestPaths(
        PlayerStatus(start, Direction.E),
        { status -> status.position == this.end },
        { status -> status.nextMoves().filter { !this.isWall(it.nextState.position) } }
    )

    private fun isWall(point: Point2D) = walls[point.y * width + point.x]

    companion object {
        fun parse(lines: List<String>): Maze {
            val height = lines.size
            val width = lines[0].length
            val chars = lines.joinToString("", transform = String::trim)
            val walls = chars.map { it == '#' }
            val start = chars.indices.first { chars[it] == 'S' }.let { Point2D(it % width, it / width) }
            val end = chars.indices.first { chars[it] == 'E' }.let { Point2D(it % width, it / width) }
            return Maze(width, height, walls, start, end)
        }
    }
}

data class PlayerStatus(val position: Point2D, val direction: Direction) {
    fun nextMoves(): Sequence<ShortestPathMove<PlayerStatus>> {
        return sequenceOf(
            ShortestPathMove(PlayerStatus(position.move(direction), direction), 1),
            ShortestPathMove(PlayerStatus(position, direction.rotateLeft()), 1000),
            ShortestPathMove(PlayerStatus(position, direction.rotateRight()), 1000)
        )
    }
}
