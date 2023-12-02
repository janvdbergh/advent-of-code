package eu.janvdb.aoc2023.day02

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input02-test.txt"
const val FILENAME = "input02.txt"

fun main() {
	val puzzles = readLines(2023, FILENAME)
		.map { Puzzle.parse(it) }

	val possible = puzzles.filter { it.matches(12, 13, 14) }.sumOf { it.id }
	println(possible)

	val colorPowers = puzzles.sumOf { it.minColorsNeeded().power() }
	println(colorPowers)
}

data class ColorTriplet(val red: Int, val green: Int, val blue: Int) {
	fun power() = red * green * blue

	companion object {
		fun parse(input: String): ColorTriplet {
			val parts = input.split(",")
				.map { it.trim().split(" ") }
				.map { Pair(it[1], it[0].toInt()) }

			fun findColor(color: String): Int {
				return parts.find { it.first == color }?.second ?: 0
			}

			return ColorTriplet(findColor("red"), findColor("green"), findColor("blue"))
		}
	}
}

data class Puzzle(val id: Int, val entries: List<ColorTriplet>) {

	fun matches(totalRed: Int, totalGreen: Int, totalBlue: Int): Boolean {
		return entries.all { it.red <= totalRed && it.green <= totalGreen && it.blue <= totalBlue }
	}

	fun minColorsNeeded() = ColorTriplet(
		entries.maxOfOrNull { it.red } ?: 0,
		entries.maxOfOrNull { it.green } ?: 0,
		entries.maxOfOrNull { it.blue } ?: 0
	)

	companion object {
		fun parse(input: String): Puzzle {
			val parts = input.split(":")
			val id = parts[0].split(" ")[1].toInt()
			val entries = parts[1].split(";").map { ColorTriplet.parse(it) }
			return Puzzle(id, entries)
		}
	}
}