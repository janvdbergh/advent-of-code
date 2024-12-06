package eu.janvdb.aoc2024.day06

import eu.janvdb.aocutil.kotlin.point2d.Direction
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input06-test.txt"
const val FILENAME = "input06.txt"

fun main() {
    val map = Map(readLines(2024, FILENAME))

    println(map.findPath())

    val count = map.mapsWithExtraObstacle()
        .map { it.findPath() }
        .filter { it.loop }
        .count()
    println(count)
}

data class Path(val length: Int, val loop: Boolean)

data class Map(val lines: List<String>) {
    fun findPath(): Path {
        var currentState = getInitialState()
        var loop = true
        val states = mutableSetOf<State>()
        while (!states.contains(currentState)) {
            states.add(currentState)
            val newState = move(currentState)
            if (newState == null) {
                loop = false
                break
            }
            currentState = newState
        }

        val length = states.asSequence().map { it.position }.distinct().count()
        return Path(length, loop)
    }

    private fun getInitialState(): State {
        for (y in lines.indices) {
            for (x in lines[y].indices) {
                val position = Point2D(x, y)
                val ch = lines[y][x]
                when (ch) {
                    '^' -> return State(position, Direction.N)
                    'V' -> return State(position, Direction.S)
                    '>' -> return State(position, Direction.E)
                    '<' -> return State(position, Direction.W)
                }
            }
        }
        throw IllegalArgumentException("No starting position found")
    }

    private fun move(state: State): State? {
        val newPosition = state.position.move(state.direction)
        val tile = getTile(newPosition)
        return when (tile) {
            Tile.OUTSIDE -> null
            Tile.OBSTACLE -> State(state.position, state.direction.rotateRight())
            Tile.OPEN -> State(newPosition, state.direction)
        }
    }

    private fun getTile(position: Point2D): Tile {
        if (position.y < 0 || position.y >= lines.size) return Tile.OUTSIDE
        val line = lines[position.y]
        if (position.x < 0 || position.x >= line.length) return Tile.OUTSIDE
        return if (line[position.x] == '#') Tile.OBSTACLE else Tile.OPEN
    }

    fun mapsWithExtraObstacle(): Sequence<Map> {
        return lines.indices.asSequence()
            .flatMap { y -> lines[y].indices.asSequence().map { x -> Point2D(x, y) } }
            .filter { lines[it.y][it.x] == '.' }
            .map {
                val newLines = lines.toMutableList()
                newLines[it.y] = newLines[it.y].substring(0, it.x) + '#' + newLines[it.y].substring(it.x + 1)
                Map(newLines)
            }
    }
}

data class State(val position: Point2D, val direction: Direction)

enum class Tile { OPEN, OBSTACLE, OUTSIDE }
