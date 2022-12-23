package eu.janvdb.aoc2022.day23

import eu.janvdb.aocutil.kotlin.point2d.Direction
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input23-test1.txt"
//const val FILENAME = "input23-test2.txt"
const val FILENAME = "input23.txt"

fun main() {
    val elves = readLines(2022, FILENAME).toElves()

    part1(elves)
    part2(elves)
}

fun part1(elves: Elves) {
    var current = elves
    repeat(10) {
        current = current.step()
    }
    println(current.emptyGrounds())
}

fun part2(elves: Elves) {
    var current = elves
    var rounds = 0
    while (true) {
        val next = current.step()
        rounds++

        if (current == next) break
        current = next
    }

    println("$rounds rounds")
}

class Elves(val elves: Set<Point2D>, val directions: List<Direction>) {
    val minY = elves.minOf { it.y }
    val maxY = elves.maxOf { it.y }
    val minX = elves.minOf { it.x }
    val maxX = elves.maxOf { it.x }

    fun step(): Elves {
        fun tryMoveElf(position: Point2D, position1: Point2D, position2: Point2D): Point2D? {
            return if (elves.contains(position) || elves.contains(position1) || elves.contains(position2)) null
            else position
        }

        fun tryMoveElf(position: Point2D): Point2D? {
            val up = position.up()
            val down = position.down()
            val left = position.left()
            val right = position.right()
            val upLeft = up.left()
            val upRight = up.right()
            val downLeft = down.left()
            val downRight = down.right()

            if (!elves.contains(up) && !elves.contains(down) && !elves.contains(left) && !elves.contains(right) &&
                !elves.contains(upLeft) && !elves.contains(upRight) && !elves.contains(downLeft) &&
                !elves.contains(downRight)
            )
                return position

            return directions.asSequence().map { direction ->
                when (direction) {
                    Direction.N -> tryMoveElf(up, upLeft, upRight)
                    Direction.S -> tryMoveElf(down, downLeft, downRight)
                    Direction.W -> tryMoveElf(left, upLeft, downLeft)
                    Direction.E -> tryMoveElf(right, upRight, downRight)
                }
            }.firstOrNull { it != null }
        }

        val moves = elves.asSequence().map { Pair(it, tryMoveElf(it)) }
            .filter { it.second != null }
            .map { Pair(it.first, it.second!!) }
            .toMap()

        val doubleLocations = moves.values.groupingBy { it }.eachCount().filter { it.value >= 2 }.keys
        val newElves = elves.asSequence().map {
            val move = moves[it]
            if (move != null && !doubleLocations.contains(move)) move else it
        }.toSet()

        val newDirections = directions.toMutableList()
        newDirections.add(newDirections.removeAt(0))
        return Elves(newElves, newDirections)
    }

    fun emptyGrounds(): Int {
        return (minY..maxY).map { y ->
            (minX..maxX).count { x -> !elves.contains(Point2D(x, y)) }
        }.sum()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val ch = if (elves.contains(Point2D(x, y))) '#' else '.'
                builder.append(ch)
            }
            builder.append('\n')
        }
        return builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Elves
        if (elves != other.elves) return false
        return true
    }

    override fun hashCode(): Int {
        return elves.hashCode()
    }
}

fun List<String>.toElves(): Elves {
    val elves = this.flatMapIndexed { y, line ->
        line
            .mapIndexed { x, ch -> Pair(Point2D(x, y), ch) }
            .filter { it.second == '#' }
            .map { it.first }
    }.toSet()
    return Elves(elves, listOf(Direction.N, Direction.S, Direction.W, Direction.E))
}