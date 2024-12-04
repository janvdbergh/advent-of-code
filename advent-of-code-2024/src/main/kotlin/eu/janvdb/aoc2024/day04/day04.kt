package eu.janvdb.aoc2024.day04

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input04-test.txt"
//const val FILENAME = "input04.txt"

const val WORD = "XMAS"
val DIRECTIONS = listOf(
    Point2D(-1, -1),
    Point2D(-1, 0),
    Point2D(-1, 1),
    Point2D(0, -1),
    Point2D(0, 1),
    Point2D(1, -1),
    Point2D(1, 0),
    Point2D(1, 1),
)

fun main() {
    val puzzle = PuzzleState(readLines(2024, FILENAME))
    println(puzzle.findWord1().count())
    println(puzzle.findWord2().count())
}

data class PuzzleState(val lines: List<String>) {

    fun findWord1(): Sequence<Pair<Point2D, Point2D>> {
        return coordinates().flatMap { point ->
            DIRECTIONS
                .map { direction -> Pair(point, direction) }
                .filter { location -> wordExistsAt(location) }
        }
    }

    fun findWord2(): Sequence<Point2D> {
        return coordinates()
            .filter { getLetter(it.x, it.y) == 'A' }
            .filter {
                val letter1 = getLetter(it.x - 1, it.y - 1)
                val letter2 = getLetter(it.x + 1, it.y + 1)
                (letter1 == 'M' && letter2 == 'S') || (letter2 == 'M' && letter1 == 'S')
            }.filter {
                val letter1 = getLetter(it.x - 1, it.y + 1)
                val letter2 = getLetter(it.x + 1, it.y - 1)
                (letter1 == 'M' && letter2 == 'S') || (letter2 == 'M' && letter1 == 'S')
            }
    }

    private fun coordinates(): Sequence<Point2D> {
        return lines.indices.asSequence().flatMap { y ->
            lines[y].indices.map { x -> Point2D(x, y) }
        }
    }

    private fun wordExistsAt(locationWithDirection: Pair<Point2D, Point2D>): Boolean {
        for (i in WORD.indices) {
            val x = locationWithDirection.first.x + i * locationWithDirection.second.x
            val y = locationWithDirection.first.y + i * locationWithDirection.second.y
            val letter = getLetter(x, y)
            if (letter != WORD[i]) {
                return false
            }
        }
        return true
    }

    private fun getLetter(x: Int, y: Int): Char {
        if (y < 0 || y >= lines.size) return ' '
        val line = lines[y]
        if (x < 0 || x >= line.length) return ' '
        return line[x]
    }
}
